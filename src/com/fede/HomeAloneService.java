package com.fede;

import com.fede.Utilities.PrefUtils;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class HomeAloneService extends IntentService {
	public static final String EVENT_TYPE = "Event type";
	public static final String RECEIVED_CALL = "Received Call";	
	public static final String NUMBER = "Number";
	
	public static final String RECEIVED_SMS = "Received Sms";
	public static final String MESSAGE_BODY = "Message body";
	public static final String HOMEALONE_EVENT_PROCESSED = "com.fede.action.EVENTPROCESSED";
	private ServiceState state;
	
	@Override public void onCreate() {
		super.onCreate();
		if(PrefUtils.homeAloneEnabled(this)){
			state = new ActiveState();
		} else {
			state = new InactiveState();
		}
	}
	
	
	public HomeAloneService() {
        super("HomeAloneService");
    } 
	
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	// this is not that useful since this is an use & throw away service
	public void setState(ServiceState s)
	{
		state = s;
		PrefUtils.setStatus(s.getServiceState(), this);
	}

	
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();            
		String type = extras.getString(EVENT_TYPE);
		if(type.equals(RECEIVED_SMS)){
			state.handleSms(this, extras);
		}
		
		if(type.equals(RECEIVED_CALL)){
			state.handleIncomingCall(this, extras);
		}
		// TODO Rimuovere se non si fanno i test?
		Intent i = new Intent(HOMEALONE_EVENT_PROCESSED);
		sendBroadcast(i);
	}

}
