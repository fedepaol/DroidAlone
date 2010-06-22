package com.fede;

import android.os.Bundle;

public class InactiveState implements ServiceState {

	@Override
	public void handleIncomingCall(HomeAloneService s, Bundle b) {
		// if inactive returns 
		return;

	}

	@Override
	public void handleSms(HomeAloneService s, Bundle b) {
		if(!CommandSms.isCommandSms(b)){	// if the state is inactive we can only process commands
			return;
		}

	}

}
