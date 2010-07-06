package com.fede;

import android.os.Bundle;

import com.fede.MessageException.InvalidCommandException;
import com.fede.Utilities.GeneralUtils;
import com.fede.Utilities.PrefUtils;

public class ActiveState implements ServiceState {
	@Override
	public boolean getServiceState() {
		return true;
	}
	
	
	public void sendReply(HomeAloneService s, String number)
	{
		String reply = PrefUtils.getReply(s);
		if(!reply.equals("")){
			GeneralUtils.sendSms(number, reply);
		}	
	}
	
	
	// Tells caller name from number
	private String getCallerNameString(Bundle b, String number, HomeAloneService s){
		try{
			 return GeneralUtils.getNameFromNumber(number, s);
	
		}catch (NameNotFoundException e){
			return "";
		}
	}
	
	
	// Handle incoming call and forwards to sms / email
	@Override
	public void handleIncomingCall(HomeAloneService s, Bundle b) {
		String number =  b.getString(HomeAloneService.NUMBER);
		String callString = s.getString(R.string.call_from);
		
		String msg = String.format("%s %s %s", callString,getCallerNameString(b, number, s), number);
		
		EventForwarder f = new EventForwarder(msg, s);
		
		f.forward(); 
		sendReply(s, number);
		return;

	}

	private void handleSmsToNotify(HomeAloneService s, Bundle b, String body){
		String number =  b.getString(HomeAloneService.NUMBER);
		String msg = String.format("%s%s:%s", getCallerNameString(b, number, s),number, body);
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
