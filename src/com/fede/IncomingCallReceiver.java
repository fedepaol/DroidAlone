package com.fede;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class IncomingCallReceiver extends BroadcastReceiver {
	private static final String RECEIVER_TAG = "recv";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String type = intent.getStringExtra(android.telephony.TelephonyManager.EXTRA_STATE);
		
		if(type == android.telephony.TelephonyManager.EXTRA_STATE_RINGING){
			String number = intent.getStringExtra(android.telephony.TelephonyManager.EXTRA_INCOMING_NUMBER);
			notifyIncomingNumber(context, number);
		}
	}
	
	private void notifyIncomingNumber(Context context, String number){
		Log.d(RECEIVER_TAG, "Received incoming call from :" + number);

		Intent myServiceIntent = new Intent(context, HomeAloneService.class);
		
		myServiceIntent.putExtra(HomeAloneService.EVENT_TYPE, 
								 HomeAloneService.RECEIVED_CALL);
		
		myServiceIntent.putExtra(HomeAloneService.NUMBER, number);		
		context.startService(myServiceIntent);
	}

}
