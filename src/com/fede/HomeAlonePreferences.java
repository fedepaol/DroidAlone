package com.fede;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.fede.Utilities.GeneralUtils;

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
	
	private void reload(){
		startActivity(getIntent()); 
		finish();
	}
	
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) { 
		SharedPreferences.Editor prefEditor = prefs.edit();
		OnClickListener l = new OnClickListener() { 
			public void onClick(DialogInterface dialog, int arg1) {
				reload();
			} };

		String SMS_TO_FWD_KEY = getString(R.string.sms_to_forward_key);
		if(key.equals(SMS_TO_FWD_KEY)){
			String smsNumber = prefs.getString(SMS_TO_FWD_KEY, "");
			if(smsNumber.length() > 0){
				if(!GeneralUtils.isPhoneNumber(smsNumber)){
					GeneralUtils.showErrorDialog(getString(R.string.not_valid_number_message), this, l);
					prefEditor.putString(SMS_TO_FWD_KEY, "");
					prefEditor.commit();
				}
			}
			return;
		}
		String SMS_ENABLE_KEY = getString(R.string.forward_to_sms_key);
		if(key.equals(SMS_ENABLE_KEY) && prefs.getBoolean(SMS_ENABLE_KEY, false)){
			
			String smsNumber = prefs.getString(SMS_TO_FWD_KEY, "");			
			if(!GeneralUtils.isPhoneNumber(smsNumber)){
				GeneralUtils.showErrorDialog(getString(R.string.not_valid_number_message), this, l);
				prefEditor.putBoolean(SMS_ENABLE_KEY, false);
				prefEditor.commit();
			}
			return;
		}
	
	
	
		String MAIL_TO_FWD_KEY = getString(R.string.mail_to_forward_key);
		
	
		if(key.equals(MAIL_TO_FWD_KEY)){
			String mailAddress = prefs.getString(MAIL_TO_FWD_KEY, "");
			if(mailAddress.length() > 0){
				if(!GeneralUtils.isMail(mailAddress)){
					GeneralUtils.showErrorDialog(getString(R.string.not_valid_mail_message), this, l);
					prefEditor.putString(MAIL_TO_FWD_KEY, "");
					prefEditor.commit();
				}
			}
			return;
		}
	
		String MAIL_ENABLE_KEY = getString(R.string.forward_to_mail_key);	
		if(key.equals(MAIL_ENABLE_KEY) && prefs.getBoolean(MAIL_ENABLE_KEY, false)){
			String mailAddress = prefs.getString(MAIL_TO_FWD_KEY, "");
			
			String MAIL_USER_KEY = getString(R.string.gmail_user_key);
			String MAIL_PWD_KEY = getString(R.string.gmail_pwd_key);
			String mailUser = prefs.getString(MAIL_USER_KEY, "");
			String mailPassword = prefs.getString(MAIL_PWD_KEY, "");
			
			if(mailUser.length() == 0){
				GeneralUtils.showErrorDialog(getString(R.string.invalid_mail_user), this, l);
				prefEditor.putBoolean(MAIL_ENABLE_KEY, false);
				prefEditor.commit();
			}else if(mailPassword.length() == 0){
				GeneralUtils.showErrorDialog(getString(R.string.invalid_mail_pwd), this, l);
				prefEditor.putBoolean(MAIL_ENABLE_KEY, false);
				prefEditor.commit();
			}else if(!GeneralUtils.isMail(mailAddress)){
				GeneralUtils.showErrorDialog(getString(R.string.not_valid_mail_message), this, l);
				prefEditor.putBoolean(MAIL_ENABLE_KEY, false);
				prefEditor.commit();
			}else if(!GeneralUtils.isNetworkAvailable(this)){
				GeneralUtils.showErrorDialog(getString(R.string.network_not_available), this);
				startActivity(new
						Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)); 
			}
			return;
			
		}
	
		String REPLY_ENABLE_KEY = getString(R.string.reply_enable_key);
	
		if(key.equals(REPLY_ENABLE_KEY) && prefs.getBoolean(REPLY_ENABLE_KEY, false)){
			String REPLY_KEY = getString(R.string.reply_key);
			String reply = prefs.getString(REPLY_KEY, "");
			if(reply.length() == 0){
				GeneralUtils.showErrorDialog(getString(R.string.empty_reply_message), this, l);
				prefEditor.putBoolean(REPLY_ENABLE_KEY, false);
				prefEditor.commit();
				}
			return;
		}
	
	}
}
