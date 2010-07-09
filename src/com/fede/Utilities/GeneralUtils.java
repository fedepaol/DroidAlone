package com.fede.Utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.SmsManager;

import com.fede.EventListActivity;
import com.fede.GMailSender;
import com.fede.NameNotFoundException;
import com.fede.R;
import com.fede.TestStubInterface;


public class GeneralUtils {
	static TestStubInterface mTest = null;
	
	public static void setStubInterface(TestStubInterface test)
	{
		mTest = test; 
	}
	
	public static void sendSms(String number, String message)
	{
		if(mTest != null){	// TODO Test only
			mTest.sendSms(number, message);
			return;
		}
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(number, null, message, null, null);
	}
	
	public static void sendMail(Context c, String body){
		if(mTest != null){	// TODO Test only
			mTest.sendMail(body);
			return;
		}
		
		String MAIL_TO_FWD_KEY = c.getString(R.string.mail_to_forward_key);	
		String USER_KEY = c.getString(R.string.gmail_user_key);
		String PWD_KEY = c.getString(R.string.gmail_pwd_key);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);

		GMailSender sender = new GMailSender(prefs.getString(USER_KEY, ""), 
								 prefs.getString(PWD_KEY, ""));
		
		try{
			sender.sendMail("HOMEALONE", 
				  		 body, 
				  		 "HomeAloneSoftware", 
				  		 prefs.getString(MAIL_TO_FWD_KEY, ""));
			
		}catch (Exception e){
			// TODO Logging
		}
	}

	
	// tells if the network is available
	public static boolean isNetworkAvailable(Context context) {
        boolean value = false;
        ConnectivityManager manager = (ConnectivityManager) context
                         .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
                       value = true;
        }
        return value;
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
	
	// Tells if is a valid phone number	
	static public boolean isPhoneNumber(String number){
		// TODO Singleton is more efficent
		Pattern phoneNumPattern = Pattern.compile("\\+?[0-9]+"); // Begins (or not) with a plus and then the number
	    Matcher matcher = phoneNumPattern.matcher(number);
	    if(matcher.matches()) 
	    	return true;
	    else 
	    	return false;
	}
	
	// Tells if is a valid email address
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
	
	public static void notifyEvent(String event, String fullDescEvent, Context c){
		String svcName = Context.NOTIFICATION_SERVICE;
		NotificationManager notificationManager;
		notificationManager = (NotificationManager)c.getSystemService(svcName);
		// TODO set a valid icon
		int icon = R.drawable.icon;
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, event, when);
		notification.number++;
		// Text to display in the extended status window
		String expandedText = fullDescEvent;
		// Title for the expanded status
		String expandedTitle = c.getString(R.string.home_alone_event);
		// Intent to launch an activity when the extended text is clicked
		Intent intent = new Intent(c, EventListActivity.class);
		PendingIntent launchIntent = PendingIntent.getActivity(c, 0, intent, 0);
		notification.setLatestEventInfo(c,
		                                expandedTitle,
		                                expandedText,
		                                launchIntent);
		int notificationRef = 1;
		notificationManager.notify(notificationRef, notification);
		

	}
	
}
