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
		boolean forwardedToSms = false;
		boolean forwardedToMail = false;
		if(PrefUtils.getBoolPreference(prefs, R.string.forward_to_sms_key, c) == true){
			GeneralUtils.sendSms(PrefUtils.getStringPreference(prefs, R.string.sms_to_forward_key, c), 
					toForward);
			forwardedToSms = true;
		}
		
	
		if(PrefUtils.getBoolPreference(prefs, R.string.forward_to_mail_key, c) == true){
			GeneralUtils.sendMail(c, toForward);
			forwardedToMail = true;
		}
		
		String shortDesc = toForward + c.getString(R.string.notified);
		String forwardedToSmsDesc = "";
		String forwardedToMailDesc = ""; 
	
		if(forwardedToSms){
			forwardedToSmsDesc = c.getString(R.string.sent_via_sms) + " " + PrefUtils.getStringPreference(prefs, R.string.sms_to_forward_key, c);
		}
		
		if(forwardedToMail){
			forwardedToMailDesc = c.getString(R.string.sent_via_mail) + " " + PrefUtils.getStringPreference(prefs, R.string.mail_to_forward_key, c);
		}
		String fullDesc = shortDesc + " " + forwardedToSmsDesc + " " + forwardedToMailDesc;
		
		GeneralUtils.notifyEvent(shortDesc, fullDesc, c);
	}

}
