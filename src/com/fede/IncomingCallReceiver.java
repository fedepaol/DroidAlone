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
		
		if(type.equals(android.telephony.TelephonyManager.EXTRA_STATE_OFFHOOK)){
			Intent myServiceIntent = new Intent(context, HomeAloneService.class);
			
			myServiceIntent.putExtra(HomeAloneService.EVENT_TYPE, 
									 HomeAloneService.HANDLING_CALL);
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
