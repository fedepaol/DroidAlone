package com.fede;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class HomeAlonePreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	SharedPreferences prefs;
	
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState); 
		Context context = getApplicationContext(); 
		prefs = PreferenceManager.getDefaultSharedPreferences(context); 
		addPreferencesFromResource(R.xml.userprefs);
	}
	
	
	@Override
	protected void onPause() {		
		super.onPause();
		prefs.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Register this OnSharedPreferenceChangeListener 
		
		prefs.registerOnSharedPreferenceChangeListener(this);
	}
	
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) { 
		SharedPreferences.Editor prefEditor = prefs.edit();
	

		String SMS_TO_FWD_KEY = getString(R.string.sms_to_forward_key);
		if(key.equals(SMS_TO_FWD_KEY)){
			String smsNumber = prefs.getString(SMS_TO_FWD_KEY, "");
			if(smsNumber.length() > 0){
				if(!PrefUtils.isPhoneNumber(smsNumber)){
					PrefUtils.showErrorDialog(getString(R.string.not_valid_number_message), this);
					prefEditor.putString(SMS_TO_FWD_KEY, "");
					prefEditor.commit();
				}
			}
		}
		String SMS_ENABLE_KEY = getString(R.string.forward_to_sms_key);
		if(key.equals(SMS_ENABLE_KEY)){
			String smsNumber = prefs.getString(SMS_TO_FWD_KEY, "");
			if(!PrefUtils.isPhoneNumber(smsNumber)){
				PrefUtils.showErrorDialog(getString(R.string.not_valid_number_message), this);
				prefEditor.putBoolean(SMS_ENABLE_KEY, false);
				prefEditor.commit();
			}
		
		}
	
	
	
		String MAIL_TO_FWD_KEY = getString(R.string.mail_to_forward_key);
		
	
		if(key.equals(MAIL_TO_FWD_KEY)){
			String mailAddress = prefs.getString(MAIL_TO_FWD_KEY, "");
			if(mailAddress.length() > 0){
				if(!PrefUtils.isMail(mailAddress)){
					PrefUtils.showErrorDialog(getString(R.string.not_valid_mail_message), this);
					prefEditor.putString(MAIL_TO_FWD_KEY, "");
					prefEditor.commit();
				}
			}
		}
	
		String MAIL_ENABLE_KEY = getString(R.string.forward_to_mail_key);	
		if(key.equals(MAIL_ENABLE_KEY)){
			String mailAddress = prefs.getString(MAIL_TO_FWD_KEY, "");
			
			String MAIL_USER_KEY = getString(R.string.gmail_user_key);
			String MAIL_PWD_KEY = getString(R.string.gmail_pwd_key);
			String mailUser = prefs.getString(MAIL_USER_KEY, "");
			String mailPassword = prefs.getString(MAIL_PWD_KEY, "");
			
			if(mailUser.length() == 0){
				PrefUtils.showErrorDialog(getString(R.string.invalid_mail_user), this);
				prefEditor.putBoolean(MAIL_ENABLE_KEY, false);
				prefEditor.commit();	
			}else if(mailPassword.length() == 0){
				PrefUtils.showErrorDialog(getString(R.string.invalid_mail_pwd), this);
				prefEditor.putBoolean(MAIL_ENABLE_KEY, false);
				prefEditor.commit();
			}if(!PrefUtils.isMail(mailAddress)){
				PrefUtils.showErrorDialog(getString(R.string.not_valid_mail_message), this);
				prefEditor.putBoolean(MAIL_ENABLE_KEY, false);
				prefEditor.commit();
			}
			
		}
	
		String REPLY_ENABLE_KEY = getString(R.string.reply_enable_key);
	
		if(key.equals(REPLY_ENABLE_KEY)){
			String REPLY_KEY = getString(R.string.reply_key);
			String reply = prefs.getString(REPLY_KEY, "");
			if(reply.length() == 0){
				PrefUtils.showErrorDialog(getString(R.string.empty_reply_message), this);
				prefEditor.putBoolean(REPLY_ENABLE_KEY, false);
				prefEditor.commit();
			}
			
		}
	
	}
}
