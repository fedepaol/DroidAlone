package com.fede;

import android.os.Bundle;

import com.fede.MessageException.InvalidCommandException;

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
		try{
			CommandSms command = new CommandSms(b, s);
			command.updatePreferences();
			if(command.getStatus() == CommandSms.BoolCommand.ENABLED){
				s.setState(new ActiveState());
			}			
		}catch (InvalidCommandException e){
			
		}
		
		
		
		
	}

}
