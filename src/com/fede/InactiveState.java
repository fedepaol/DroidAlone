package com.fede;

import android.os.Bundle;

import com.fede.MessageException.InvalidCommandException;

public class InactiveState implements ServiceState {

	@Override
	public void handleIncomingCall(HomeAloneService s, Bundle b) {
		// if inactive returns 
		return;

	}

	@Override
	public void handleSms(HomeAloneService s, Bundle b) {
		try{
			CommandSms command = new CommandSms(b, s);
			if(command.getStatus() == CommandSms.BoolCommand.ENABLED){
				s.setState(new ActiveState());
			}
		}catch (InvalidCommandException e){
			
		}
		
		
		
		
	}

}
