package com.fede;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fede.Utilities.GeneralUtils;
import com.fede.Utilities.PrefUtils;

public class BootReceiver extends BroadcastReceiver {	
	@Override
	public void onReceive(Context c, Intent intent) {
		if(PrefUtils.homeAloneEnabled(c)){
			GeneralUtils.notifyEvent(c.getString(R.string.active_state), c.getString(R.string.active_state), c);
		}
	}
	
}
