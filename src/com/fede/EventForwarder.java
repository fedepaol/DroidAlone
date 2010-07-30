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
					toForward, c);
			forwardedToSms = true;
		}
		
	
		if(PrefUtils.getBoolPreference(prefs, R.string.forward_to_mail_key, c) == true){
			try{
				GeneralUtils.sendMail(c, toForward);
				forwardedToMail = true;
			}catch (Exception e){
				;	// I made the beer with it
			}
		}
		
		String shortDesc = String.format("%s", toForward);
		String forwardedToSmsDesc = "";
		String forwardedToMailDesc = ""; 
	
		if(forwardedToSms){
			forwardedToSmsDesc = String.format("%s %s", c.getString(R.string.sent_via_sms), PrefUtils.getStringPreference(prefs, R.string.sms_to_forward_key, c));
		}
		
		if(forwardedToMail){
			forwardedToMailDesc = String.format("%s %s", c.getString(R.string.sent_via_mail), PrefUtils.getStringPreference(prefs, R.string.mail_to_forward_key, c)); 
		}
		String fullDesc = String.format("%s %s %s", toForward, forwardedToSmsDesc, forwardedToMailDesc);
		
		GeneralUtils.notifyEvent(shortDesc, fullDesc, c);
	}

}
