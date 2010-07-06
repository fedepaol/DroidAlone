package com.fede;

import android.os.Bundle;

import com.fede.MessageException.InvalidCommandException;
import com.fede.Utilities.GeneralUtils;

public class InactiveState implements ServiceState {
	@Override
	public boolean getServiceState() {
		return false;
	}
	
	@Override
	public void handleIncomingCall(HomeAloneService s, Bundle b) {
		// if inactive returns 
		return;

	}

	@Override
	public void handleSms(HomeAloneService s, Bundle b) {
		String body = b.getString(HomeAloneService.MESSAGE_BODY);
		
		if(!CommandSms.isCommandSms(body)){
			return;
		}
		
		String number = b.getString(HomeAloneService.NUMBER);

		try{
			CommandSms command = new CommandSms(b, body, number, s);
			command.execute();
			
			if(command.getStatus() == CommandSms.BoolCommand.ENABLED){
				s.setState(new ActiveState());
			}
		}catch (InvalidCommandException p){
			// in case of failure the user must be notified anyway
			GeneralUtils.sendSms(number, p.getMessage());
		}		
	}

}
