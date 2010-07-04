package com.fede;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fede.geotagger.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.SmsManager;


public class PrefUtils {
	public static final String PREF_NAME = "Preferences";
	public static final String STATUS_ENABLED = "Enabled";
	
	
	public static boolean homeAloneEnabled(Context c)
	{
		int mode = Activity.MODE_PRIVATE;
		SharedPreferences mySharedPreferences = c.getSharedPreferences(PREF_NAME, mode);		
		return mySharedPreferences.getBoolean(STATUS_ENABLED, false);
	}
	
	public static void sendSms(String number, String message)
	{
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(number, null, message, null, null);
	}
	
	public static void setStatus(boolean enabled, Context c)
	{
		int mode = Activity.MODE_PRIVATE;
		SharedPreferences mySharedPreferences = c.getSharedPreferences(PREF_NAME, mode);
		SharedPreferences.Editor editor = mySharedPreferences.edit();	
		editor.putBoolean(STATUS_ENABLED, enabled);
		editor.commit();
	}
	
	
	// Returns contact name from number
	public static String getNameFromNumber(String number, Context c) throws NameNotFoundException
	{
		String name = "";
		String[] columns = {ContactsContract.PhoneLookup.DISPLAY_NAME};
		
		Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
							number);
		Cursor idCursor = c.getContentResolver().query(lookupUri, columns, null, null, null);
		if (idCursor.moveToFirst()) { 
			int nameIdx = idCursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME); 
			name = idCursor.getString(nameIdx); 
		}else{
			throw new NameNotFoundException(number);
		}
		idCursor.close();
		return name;
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
	
	static public boolean isPhoneNumber(String number){
		// TODO Singleton is more efficent
		Pattern phoneNumPattern = Pattern.compile("\\+?[0-9]+"); // Begins (or not) with a plus and then the number
	    Matcher matcher = phoneNumPattern.matcher(number);
	    if(matcher.matches()) 
	    	return true;
	    else 
	    	return false;
	}
	
	static public boolean isMail(String number){
		Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
	    Matcher matcher = pattern.matcher(number);
	    if(matcher.matches()) 
	    	return true;
	    else 
	    	return false;
	}
	
	public static void showErrorDialog(String errorString, Context context)
	{
    	String button1String = context.getString(R.string.ok_name); 
    	AlertDialog.Builder ad = new AlertDialog.Builder(context); 
    	ad.setTitle(context.getString(R.string.error_name)); 
    	ad.setMessage(errorString); 
    	ad.setPositiveButton(button1String,
    						 new OnClickListener() { 
	    						public void onClick(DialogInterface dialog, int arg1) {
	    							// do nothing
	    						} });
    	ad.show();
    	return;    
	}
	
	public static boolean validMailUserPwd(Context c, SharedPreferences prefs)
	{
		String MAIL_USER_KEY = c.getString(R.string.gmail_user_key);
		String MAIL_PWD_KEY = c.getString(R.string.gmail_pwd_key);
		String mailUser = prefs.getString(MAIL_USER_KEY, "");
		String mailPassword = prefs.getString(MAIL_PWD_KEY, "");
		
		if(mailUser.length() == 0){
			return false;	
		}else if(mailPassword.length() == 0){
			return false;
		}
	}
}
