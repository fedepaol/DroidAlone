package com.fede;

import android.os.Bundle;

public interface ServiceState {
	// handles an incoming sms stored in bundle from the service
	public void handleSms(HomeAloneService s, Bundle b);
	
	// handles an incoming call stored in bundle from the service
	public void handleIncomingCall(HomeAloneService s, Bundle b);
	
	// handles idle phone event
	public void handlePhoneIdle(HomeAloneService s);
	
	// tells the value to store in the service state
	public boolean getServiceState();
}
