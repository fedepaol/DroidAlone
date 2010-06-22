package com.fede;

import android.os.Bundle;

public interface ServiceState {
	public void handleSms(HomeAloneService s, Bundle b);
	
	public void handleIncomingCall(HomeAloneService s, Bundle b);
}
