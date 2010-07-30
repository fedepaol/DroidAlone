package com.fede;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class IncomingCallReceiver extends BroadcastReceiver {	
	@Override
	public void onReceive(Context context, Intent intent) {
		String type = intent.getStringExtra(android.telephony.TelephonyManager.EXTRA_STATE);
		
		if(type.equals(android.telephony.TelephonyManager.EXTRA_STATE_RINGING)){
			String number = intent.getStringExtra(android.telephony.TelephonyManager.EXTRA_INCOMING_NUMBER);
			if(number == null){
				number = "unknown";
			}
			notifyIncomingNumber(context, number);
		}
		
		if(type.equals(android.telephony.TelephonyManager.EXTRA_STATE_IDLE)){
			Intent myServiceIntent = new Intent(context, HomeAloneService.class);
			
			myServiceIntent.putExtra(HomeAloneService.EVENT_TYPE, 
									 HomeAloneService.PHONE_IDLE);
			context.startService(myServiceIntent);
		}
	}
	
	private void notifyIncomingNumber(Context context, String number){
		

		Intent myServiceIntent = new Intent(context, HomeAloneService.class);
		
		myServiceIntent.putExtra(HomeAloneService.EVENT_TYPE, 
								 HomeAloneService.PHONE_RINGING);
		
		myServiceIntent.putExtra(HomeAloneService.NUMBER, number);		
		context.startService(myServiceIntent);
	}

}
