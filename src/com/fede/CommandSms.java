package com.fede;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class CommandSms {
	public static boolean isCommandSms(Bundle b){
		String body = b.getString(HomeAloneService.MESSAGE_BODY);
		if(body.startsWith("#")){
			return true;
		}
		return false;
	}
	
	public CommandSms(Bundle b, Context c){
		String body = b.getString(HomeAloneService.MESSAGE_BODY);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);

		
	}
}
