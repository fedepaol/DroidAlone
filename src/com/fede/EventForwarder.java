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
		
		GeneralUtils.notifyEvent(shortDesc, fullDesc, DroidContentProviderClient.EventType.REPLY, c);
	}

}
