package com.fede;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class IncomingSmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		Bundle bundle = intent.getExtras(); 
		if (bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus"); 
			SmsMessage[] messages = new SmsMessage[pdus.length]; 
			for (int i = 0; i < pdus.length; i++)
				messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			notifyMessages(context, messages);
		}		
	}
	
	private void notifyMessages(Context c, SmsMessage[] messages)
	{
		// TODO
		Log.d("SMS", "Received message");
		for(int i = 0; i < messages.length; i++){
			Intent myServiceIntent = new Intent(c, HomeAloneService.class);
			
			myServiceIntent.putExtra(HomeAloneService.EVENT_TYPE, 
									 HomeAloneService.RECEIVED_SMS);
			
			String body = messages[i].getMessageBody();
			String from = messages[i].getOriginatingAddress();
			myServiceIntent.putExtra(HomeAloneService.NUMBER, from);
			myServiceIntent.putExtra(HomeAloneService.MESSAGE_BODY, body);
			c.startService(myServiceIntent);
		}
	}

}
