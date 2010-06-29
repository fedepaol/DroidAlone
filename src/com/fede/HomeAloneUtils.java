package com.fede;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class HomeAloneUtils {
	public static final String PREF_NAME = "Preferences";
	public static final String STATUS_ENABLED = "Enabled";
	
	
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
	}
}
