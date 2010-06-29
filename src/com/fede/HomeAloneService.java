package com.fede;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class HomeAloneService extends Service {
	static final String EVENT_TYPE = "Event type";
	static final String RECEIVED_CALL = "Received Call";	
	static final String NUMBER = "Number";
	
	static final String RECEIVED_SMS = "Received Sms";
	static final String MESSAGE_BODY = "Message body";
	private ServiceState state;
	
	@Override public void onCreate() {
		if(HomeAloneUtils.homeAloneEnabled(this)){
			state = new ActiveState();
		} else {
			state = new InactiveState();
		}
	}
	
	@Override public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle extras = intent.getExtras();            
		String type = extras.getString(EVENT_TYPE);
		if(type == RECEIVED_SMS){
			state.handleSms(this, extras);
		}
		
		if(type == RECEIVED_CALL){
			state.handleIncomingCall(this, extras);
		}
						
		return Service.START_NOT_STICKY;
	}
	
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setState(ServiceState s)
	{
		state = s;
		HomeAloneUtils.setStatus(s.getServiceState(), this);
	}

}
