package com.fede;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class HomeAlonePreferences extends PreferenceActivity {
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState); 
		addPreferencesFromResource(R.xml.userprefs);
	}
}
