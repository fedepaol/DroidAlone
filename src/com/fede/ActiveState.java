package com.fede;

import android.os.Bundle;

import com.fede.MessageException.InvalidCommandException;

public class ActiveState implements ServiceState {
	@Override
	public boolean getServiceState() {
		return true;
	}
	
	@Override
	public void handleIncomingCall(HomeAloneService s, Bundle b) {
		String number =  b.getString(HomeAloneService.NUMBER);
		
		EventForwarder f = new EventForwarder(String.format("call from %s %s", getCallerNameString(b, number, s), 
																		number),
											  s);
		
		f.forward(); 
		return;

	}

	private void handleCommandSms(HomeAloneService s, Bundle b, String body)
	{
		String number = b.getString(HomeAloneService.NUMBER);
		try{
			CommandSms command = new CommandSms(b, body, number, s);				
			command.execute();
			if(command.getStatus() == CommandSms.BoolCommand.ENABLED){
				s.setState(new ActiveState());
			}
		}catch (InvalidCommandException e){
			PrefUtils.sendSms(number, e.getMessage());
		}
	}
	
	private String getCallerNameString(Bundle b, String number, HomeAloneService s){
		String callerName = "";
		try{
			 callerName = PrefUtils.getNameFromNumber(number, s);
			
		}catch (NameNotFoundException e){
		}
		return callerName;
	}
	
	private void handleSmsToNotify(HomeAloneService s, Bundle b, String body){
		String number =  b.getString(HomeAloneService.NUMBER);
		
		EventForwarder f = new EventForwarder(String.format("%s%s:%s", getCallerNameString(b, number, s), 
																		number, body),
											  s);
		
		f.forward();
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
