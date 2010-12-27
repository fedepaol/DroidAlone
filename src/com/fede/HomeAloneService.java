/*
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package com.fede;

import com.fede.Utilities.PrefUtils;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class HomeAloneService extends IntentService {
	public static final String EVENT_TYPE = "Event type";
	public static final String PHONE_RINGING = "Received Call";	
	public static final String NUMBER = "Number";
	public static final String PHONE_IDLE = "Idle";
	public static final String HANDLING_CALL = "On Call";
	
	public static final String RECEIVED_SMS = "Received Sms";
	public static final String MESSAGE_BODY = "Message body";
	public static final String HOMEALONE_EVENT_PROCESSED = "com.fede.action.EVENTPROCESSED";
	public static final String HOMEALONE_TEST_EVENT_PROCESSED = "com.fede.action.TESTEVENTPROCESSED";

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
		
		if(type.equals(PHONE_RINGING)){
			state.handleIncomingCall(this, extras);
		}
		if(type.equals(PHONE_IDLE)){
			state.handlePhoneIdle(this);
		}
		
		if(type.equals(HANDLING_CALL)){
			state.handlePhoneOffHook(this);
		}

		
	}
	

}
