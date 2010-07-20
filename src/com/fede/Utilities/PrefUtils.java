package com.fede.Utilities;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fede.HomeAlone;
import com.fede.R;


public class PrefUtils {
	public static final String PREF_NAME = "Preferences";
	public static final String STATUS_ENABLED = "Enabled";
	private static final String LAST_FLUSHED = "LastFlushed";
	
	public static boolean homeAloneEnabled(Context c)
	{
		int mode = Activity.MODE_PRIVATE;
		SharedPreferences mySharedPreferences = c.getSharedPreferences(PREF_NAME, mode);		
		return mySharedPreferences.getBoolean(STATUS_ENABLED, false);
	}

	public static void setStatus(boolean enabled, Context c)
	{
		int mode = Activity.MODE_PRIVATE;
		SharedPreferences mySharedPreferences = c.getSharedPreferences(PREF_NAME, mode);
		SharedPreferences.Editor editor = mySharedPreferences.edit();	
		editor.putBoolean(STATUS_ENABLED, enabled);
		editor.commit();
		if(!enabled){
			setLastFlushedCalls(c);
		}
		
		Intent i = new Intent(HomeAlone.STATE_CHANGED);
		c.sendBroadcast(i);
	}
	
	public static Long getLastFlushedCalls(Context c)
	{
		int mode = Activity.MODE_PRIVATE;
		SharedPreferences mySharedPreferences = c.getSharedPreferences(PREF_NAME, mode);		
		return mySharedPreferences.getLong(LAST_FLUSHED, 0);
	}
	
	public static void setLastFlushedCalls(Long lastFlushed, Context c)
	{
		int mode = Activity.MODE_PRIVATE;
		SharedPreferences mySharedPreferences = c.getSharedPreferences(PREF_NAME, mode);		
		SharedPreferences.Editor editor = mySharedPreferences.edit();	
		editor.putLong(LAST_FLUSHED, lastFlushed);
		editor.commit();

	}

	public static void setLastFlushedCalls(Context c)
	{
		int mode = Activity.MODE_PRIVATE;
		SharedPreferences mySharedPreferences = c.getSharedPreferences(PREF_NAME, mode);		
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		Date now = new Date();
		editor.putLong(LAST_FLUSHED, now.getTime());
		editor.commit();

	}
	
	public static String getReply(Context c)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		String REPLYL_ENABLE_KEY = c.getString(R.string.reply_enable_key);
		String REPLY_KEY = c.getString(R.string.reply_key);
		if(prefs.getBoolean(REPLYL_ENABLE_KEY, false)){
			return prefs.getString(REPLY_KEY, "");
		}else{
			return "";
		}
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
	
	 public static boolean validMailUserPwd(Context c, SharedPreferences prefs)
	{
		String mailUser = getStringPreference(prefs, R.string.gmail_user_key, c);
		String mailPassword = getStringPreference(prefs, R.string.gmail_pwd_key, c);
		        
		if(mailUser.length() == 0){
			return false;   
		}else if(mailPassword.length() == 0){
			return false;
		}
		return true;
	}
	 
	public static String getStringPreference(SharedPreferences prefs, int resId, Context c){
		String key = c.getString(resId);
		return prefs.getString(key, "");
	}
	
	public static void setStringPreference(SharedPreferences.Editor editor, int resId, String value, Context c){
		String key = c.getString(resId);
		editor.putString(key, value);
	}
	
	public static void setBoolPreference(SharedPreferences.Editor editor, int resId, Boolean value, Context c){
		String key = c.getString(resId);
		editor.putBoolean(key, value);
	}
	public static boolean getBoolPreference(SharedPreferences prefs, int resId, Context c){
		String key = c.getString(resId);
		return prefs.getBoolean(key, false);
	}

	public static boolean checkForwardingEnabled(Context s) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(s);

		return (PrefUtils.getBoolPreference(prefs, R.string.forward_to_mail_key, s) ||
				PrefUtils.getBoolPreference(prefs, R.string.forward_to_sms_key, s));
		
	}
	
	public static boolean mailForwardingEnabled(Context s) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(s);

		return (PrefUtils.getBoolPreference(prefs, R.string.forward_to_mail_key, s));
		
	}
}
