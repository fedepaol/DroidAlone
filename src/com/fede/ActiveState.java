/*
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/
package com.fede;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.fede.MessageException.InvalidCommandException;
import com.fede.Utilities.GeneralUtils;
import com.fede.Utilities.PrefUtils;

import java.util.Date;

public class ActiveState implements ServiceState {
	@Override
	public boolean getServiceState() {
		return true;
	}
	 
	
	private void notifyReply(HomeAloneService s, String number, String reply){
		String message = String.format(s.getString(R.string.reply_notified_to), reply, number);
		GeneralUtils.notifyEvent(s.getString(R.string.reply_notified), message, DroidContentProviderClient.EventType.REPLY, s);
	}
	
	public void sendReply(HomeAloneService s, String number)
	{
		String reply = PrefUtils.getReply(s);
		if(!reply.equals("") && !number.equals("unknown")){
			notifyReply(s, number, reply);
			GeneralUtils.sendSms(number, reply, s);
		}	
	}
	
	
	// Tells caller name from number
	private String getCallerNameString(String number, HomeAloneService s){
		try{
			 return GeneralUtils.getNameFromNumber(number, s);
		}catch (NameNotFoundException e){
			return "";
		}
	}
	
	

	
	// Handle ringing state and store the number to forwards to sms / email later
	@Override
	public void handleIncomingCall(HomeAloneService s, Bundle b) {
		String numberWithQuotes = b.getString(HomeAloneService.NUMBER);
        DroidContentProviderClient.addCall(numberWithQuotes, new Date(), s);
	}
	
	
	private void notifyCall(String number, HomeAloneService s){
		String callString = s.getString(R.string.call_from);
		String msg = String.format(callString, getCallerNameString(number, s), number);
		
		EventForwarder f = new EventForwarder(msg, DroidContentProviderClient.EventType.FORWARDED_CALL, s);
		
		f.forward(); 
		sendReply(s, number);

	}
	
	private void notifySkippedCall(String number, HomeAloneService s){
		String callString = s.getString(R.string.call_from);
		String msg = s.getString(R.string.skipped) + " " + String.format(callString, getCallerNameString(number, s), number);
		GeneralUtils.notifyEvent(s.getString(R.string.skipped_call), msg, DroidContentProviderClient.EventType.FORWARDED_CALL , s);
	}
	
	@Override
	public void handlePhoneIdle(HomeAloneService s){
	// when idle I need to check if I have one or more pending calls. This should be done also
	// in onidle of inactivestate
		Cursor c = DroidContentProviderClient.getAllCall(s);
		if (c.moveToFirst()) {
            do{           
            	String number = c.getString(DroidContentProvider.CALL_NUMBER_COLUMN_POSITION);
            	if(number == null){
            		number = "unknown number";
            	}
               notifyCall(number, s);	
            } while (c.moveToNext());
         }
		c.close();

        DroidContentProviderClient.removeAllCall(s);
	}

	private void handleSmsToNotify(HomeAloneService s, Bundle b, String body){
		String number =  b.getString(HomeAloneService.NUMBER);
		String msg = String.format("Sms %s %s:%s", getCallerNameString(number, s),number, body);
		EventForwarder f = new EventForwarder(msg, DroidContentProviderClient.EventType.FORWARDED_SMS, s);
		f.forward();
		sendReply(s, number);
	}
	
	
	private void handleCommandSms(HomeAloneService s, Bundle b, String body)
	{
		String number = b.getString(HomeAloneService.NUMBER);
		try{
			CommandSms command = new CommandSms(b, body, number, s);				
			command.execute();
			if(command.getStatus() == CommandSms.BoolCommand.DISABLED){
				s.setState(new InactiveState());
			}
		}catch (InvalidCommandException e){
			GeneralUtils.sendSms(number, e.getMessage(), s);
		}
	}
	

	private boolean isDroidAloneMessage(String msg, Context c) {
		return msg.startsWith(c.getString(R.string.message_header));
	}

	@Override
	public void handleSms(HomeAloneService s, Bundle b)  {
		String body = b.getString(HomeAloneService.MESSAGE_BODY);
		if(isDroidAloneMessage(body, s)){	// Do nothing to avoid loops
			GeneralUtils.notifyEvent(s.getString(R.string.loop_message),
						String.format(s.getString(R.string.loop_message_full), body), DroidContentProviderClient.EventType.FAILURE, s);
			
		}else if(CommandSms.isCommandSms(body)){
			handleCommandSms(s, b, body);		
		}else{
			handleSmsToNotify(s, b, body);
		}
	}


	@Override
	public void handlePhoneOffHook(HomeAloneService s) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(s);
		if(!PrefUtils.getBoolPreference(prefs, R.string.skip_handled_key, s))
			return;
		
		Cursor c = DroidContentProviderClient.getAllCall(s);
		if (c.moveToFirst()) {
            do{           
            	String number = c.getString(DroidContentProvider.CALL_NUMBER_COLUMN_POSITION);
            	if(number == null){
            		number = "unknown number";
            	}
               notifySkippedCall(number, s);
            } while (c.moveToNext());
         }
		c.close();

	}

}
