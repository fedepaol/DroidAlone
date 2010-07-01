package com.fede;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;


public class PrefUtils {
	public static final String PREF_NAME = "Preferences";
	public static final String STATUS_ENABLED = "Enabled";
	
	
	public static boolean homeAloneEnabled(Context c)
	{
		int mode = Activity.MODE_PRIVATE;
		SharedPreferences mySharedPreferences = c.getSharedPreferences(PREF_NAME, mode);		
		return mySharedPreferences.getBoolean(STATUS_ENABLED, false);
	}
	
	public static void sendSms(String number, String message)
	{
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(number, null, message, null, null);
	}
	
	public static void setStatus(boolean enabled, Context c)
	{
		int mode = Activity.MODE_PRIVATE;
		SharedPreferences mySharedPreferences = c.getSharedPreferences(PREF_NAME, mode);
		SharedPreferences.Editor editor = mySharedPreferences.edit();	
		editor.putBoolean(STATUS_ENABLED, enabled);
	}
	
	public static String getPreferencesStatus(Context c)
	{
		StringBuffer b = new StringBuffer();
		if(homeAloneEnabled(c)){
			b.append("s:on ");	
		}else{
			b.append("s:off ");
		}
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		
		String SMS_ENABLE_KEY = c.getString(R.string.forward_to_sms_key);
		String SMS_TO_FWD_KEY = c.getString(R.string.sms_to_forward_key);
		
		if(prefs.getBoolean(SMS_ENABLE_KEY, false) == true){
			b.append("sms:" + prefs.getString(SMS_TO_FWD_KEY, "") + " ");
		}
		
		String MAIL_ENABLE_KEY = c.getString(R.string.forward_to_mail_key);
		String MAIL_TO_FWD_KEY = c.getString(R.string.mail_to_forward_key);

		if(prefs.getBoolean(MAIL_ENABLE_KEY, false) == true){
			b.append("mail:" + prefs.getString(MAIL_TO_FWD_KEY, "") + " ");
		}
		
		String REPLYL_ENABLE_KEY = c.getString(R.string.reply_enable_key);
		String REPLY_KEY = c.getString(R.string.reply_key);

		if(prefs.getBoolean(REPLYL_ENABLE_KEY, false) == true){
			b.append("reply:" + prefs.getString(REPLY_KEY, ""));
		}

		return b.toString();
	}
}
