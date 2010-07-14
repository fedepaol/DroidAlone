package com.fede;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.fede.MessageException.ForwardingDisabledException;
import com.fede.MessageException.InvalidCommandException;
import com.fede.Utilities.GeneralUtils;
import com.fede.Utilities.PrefUtils;

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

	
	private void sendActivationMail(Context c, String number) throws InvalidCommandException{
		try{
			GeneralUtils.sendMail(c, String.format(c.getString(R.string.mail_activated_message), number));
		}catch (Exception e){
			throw new InvalidCommandException(c.getString(R.string.failed_to_send_activ_mail), c);
		}
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
				if (!PrefUtils.checkForwardingEnabled(s)){
					throw new ForwardingDisabledException(s.getString(R.string.forwarding_not_enabled), s);
				}
				if(PrefUtils.mailForwardingEnabled(s)){
					sendActivationMail(s, number);
				}
				s.setState(new ActiveState());
			}
		}catch (InvalidCommandException p){
			// in case of failure the user must be notified anyway
			GeneralUtils.sendSms(number, p.getMessage());
		}
		
	}

}
