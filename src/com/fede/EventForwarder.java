package com.fede;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fede.Utilities.GeneralUtils;
import com.fede.Utilities.PrefUtils;

// Forwards the string to a certain destination
public class EventForwarder {
	private String toForward;
	private Context c;
	
	public EventForwarder(String toForward, Context c) {
		this.toForward = toForward;
		this.c = c;
	}
	
	public void forward()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		
		String SMS_ENABLE_KEY = c.getString(R.string.forward_to_sms_key);
		String SMS_TO_FWD_KEY = c.getString(R.string.sms_to_forward_key);
		
		if(prefs.getBoolean(SMS_ENABLE_KEY, false) == true){
			GeneralUtils.sendSms(prefs.getString(SMS_TO_FWD_KEY, ""), 
					toForward);
		}
		
		String MAIL_ENABLE_KEY = c.getString(R.string.forward_to_mail_key);
		
		if(prefs.getBoolean(MAIL_ENABLE_KEY, false) == true){
			GeneralUtils.sendMail(c, toForward);
		}
	}

}
