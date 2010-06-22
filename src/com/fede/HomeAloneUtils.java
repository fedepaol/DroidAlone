package com.fede;

import java.util.prefs.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class HomeAloneUtils {
	public static Preferences getPreferences(Context c)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		
		return new Preferences (prefs.getBoolean("PREF_ENABLE_GPS", true),	// better being more accurate
								prefs.getBoolean("PREF_ENABLE_CELL", true));	
	}
}
