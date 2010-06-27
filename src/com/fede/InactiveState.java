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
		
		
		
		
		
	}

}
