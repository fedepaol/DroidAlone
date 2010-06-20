package com.fede;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HomeAloneService extends Service {
	static final String EVENT_TYPE = "Event type";
	static final String RECEIVED_CALL = "Received Call";	
	static final String NUMBER = "Number";
	
	static final String RECEIVED_SMS = "Received Sms";
	
	
	@Override public void onCreate() {
		// TODO: Actions to perform when service is created.
	}
	
	@Override public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Launch a background thread to do processing. 
		return Service.START_NOT_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
