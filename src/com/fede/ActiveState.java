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
		// forwards incoming call number 
		return;

	}

	@Override
	public void handleSms(HomeAloneService s, Bundle b) 
	{
		String body = b.getString(HomeAloneService.MESSAGE_BODY);
		if(CommandSms.isCommandSms(body)){
			try{
				CommandSms command = new CommandSms(b, body, s);				
				command.execute();
				if(command.getStatus() == CommandSms.BoolCommand.ENABLED){
					s.setState(new ActiveState());
				}
			}catch (InvalidCommandException e){
				// TODO
			}
		}else{
			// TODO Notify
		}
	}

}
