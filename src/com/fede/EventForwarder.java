package com.fede;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
			PrefUtils.sendSms(prefs.getString(SMS_TO_FWD_KEY, ""), 
					toForward);
		}
		
		String MAIL_ENABLE_KEY = c.getString(R.string.forward_to_mail_key);
		String MAIL_TO_FWD_KEY = c.getString(R.string.mail_to_forward_key);

		if(prefs.getBoolean(MAIL_ENABLE_KEY, false) == true){
			
			String USER_KEY = c.getString(R.string.gmail_user_key);
			String PWD_KEY = c.getString(R.string.gmail_pwd_key);
			GMailSender sender = new GMailSender(prefs.getString(USER_KEY, ""), 
									 prefs.getString(PWD_KEY, ""));
			
			try{
				sender.sendMail("HOMEALONE", 
					  		 toForward, 
					  		 "HomeAloneSoftware", 
					  		 prefs.getString(MAIL_TO_FWD_KEY, ""));
				
			}catch (Exception e){
				// TODO Logging
			}
		}
	}

}
