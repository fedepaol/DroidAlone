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

package com.fede.Utilities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import com.fede.DroidContentProviderClient;
import com.fede.HomeAloneService;
import com.fede.IncomingCallReceiver;
import com.fede.R;

import java.util.Date;


public class PrefUtils {
	public static final String PREF_NAME = "Preferences";
	public static final String STATUS_ENABLED = "Enabled";
	private static final String LAST_FLUSHED = "LastFlushed";
	private static final String PREFERENCE_WIZARD_SHOWN = "mustShowWizard";
	private static final String PREFERENCE_LAST_VERSION = "lastVersion";
	private static final int ACTUAL_VERSION = 6;

	
	public static boolean showWizard(Context c)
	{
		int mode = Activity.MODE_PRIVATE;
		SharedPreferences mySharedPreferences = c.getSharedPreferences(PREF_NAME, mode);		
		boolean res = mySharedPreferences.getBoolean(PREFERENCE_WIZARD_SHOWN, true);
		
		if(res){
			SharedPreferences.Editor editor = mySharedPreferences.edit();
			editor.putBoolean(PREFERENCE_WIZARD_SHOWN, false);
			editor.commit();
		}
		return res;
	}
	
	public static boolean showChangeLog(Context c)
	{
		int mode = Activity.MODE_PRIVATE;
		SharedPreferences mySharedPreferences = c.getSharedPreferences(PREF_NAME, mode);		
		Long lastVersion = mySharedPreferences.getLong(PREFERENCE_LAST_VERSION, 0);
		
		if(ACTUAL_VERSION > lastVersion){
			SharedPreferences.Editor editor = mySharedPreferences.edit();
			editor.putLong(PREFERENCE_LAST_VERSION, ACTUAL_VERSION);
			editor.commit();
			return true;
		}
		return false;
	}
	
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
			GeneralUtils.notifyEvent(c.getString(R.string.status_changed), c.getString(R.string.inactive_state), DroidContentProviderClient.EventType.COMMAND, c);
			setLastFlushedCalls(c);
			GeneralUtils.removeNotifications(c);
		}else{	//Enabled
			GeneralUtils.notifyEvent(c.getString(R.string.status_changed), c.getString(R.string.active_state), DroidContentProviderClient.EventType.COMMAND, c);
		}

        PackageManager pm = c.getPackageManager();
        ComponentName myReceiverName = new ComponentName(c, IncomingCallReceiver.class);
        pm.setComponentEnabledSetting(myReceiverName, enabled? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                                     , PackageManager.DONT_KILL_APP);
		
		Intent i = new Intent(HomeAloneService.STATE_CHANGED);
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
