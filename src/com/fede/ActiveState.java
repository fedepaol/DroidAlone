package com.fede;

import android.database.Cursor;
import android.os.Bundle;

import com.fede.MessageException.InvalidCommandException;
import com.fede.Utilities.GeneralUtils;
import com.fede.Utilities.PrefUtils;

public class ActiveState implements ServiceState {
	@Override
	public boolean getServiceState() {
		return true;
	}
	
	
	private void notifyReply(HomeAloneService s, String number, String reply){
		String message = String.format(s.getString(R.string.reply_notified_to), reply, number);
		GeneralUtils.notifyEvent(s.getString(R.string.reply_notified), message, s);
	}
	
	public void sendReply(HomeAloneService s, String number)
	{
		String reply = PrefUtils.getReply(s);
		if(!reply.equals("")){
			notifyReply(s, number, reply);
			GeneralUtils.sendSms(number, reply);
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
		DbAdapter DbHelper = new DbAdapter(s);
		DbHelper.open();
		DbHelper.addCall(b.getString(HomeAloneService.NUMBER));
		DbHelper.close();
	}
	
	
	private void notifyCall(String number, HomeAloneService s){
		String callString = s.getString(R.string.call_from);
		String msg = String.format(callString, getCallerNameString(number, s), number);
		
		EventForwarder f = new EventForwarder(msg, s);
		
		f.forward(); 
		sendReply(s, number);
		return;

	}
	
	@Override
	public void handlePhoneIdle(HomeAloneService s){
	// when idle I need to check if I have one or more pending calls. This should be done also
	// in onidle of inactivestate
		DbAdapter db = new DbAdapter(s);
		db.open();
		Cursor c = db.getAllCalls();
		if (c.moveToFirst()) {
            do{                           
               notifyCall(c.getString(DbAdapter.CALL_NUMBER_DESC_COLUMN), s);
            } while (c.moveToNext());
         }
		c.close();
		db.removeAllCalls();
		db.close();
	}

	private void handleSmsToNotify(HomeAloneService s, Bundle b, String body){
		String number =  b.getString(HomeAloneService.NUMBER);
		String msg = String.format("Sms %s %s:%s", getCallerNameString(number, s),number, body);
		EventForwarder f = new EventForwarder(msg, s);
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
			GeneralUtils.sendSms(number, e.getMessage());
		}
	}
	

	

	@Override
	public void handleSms(HomeAloneService s, Bundle b) 
	{
		String body = b.getString(HomeAloneService.MESSAGE_BODY);
		if(CommandSms.isCommandSms(body)){
			handleCommandSms(s, b, body);		
		}else{
			handleSmsToNotify(s, b, body);
		}
	}

}
