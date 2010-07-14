package com.fede.Utilities;

import java.util.Calendar;
import java.util.Date;
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
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.SmsManager;

import com.fede.DbAdapter;
import com.fede.GMailSender;
import com.fede.HomeAloneService;
import com.fede.MainTabActivity;
import com.fede.NameNotFoundException;
import com.fede.OkDialogInterface;
import com.fede.R;
import com.fede.TestStubInterface;


public class GeneralUtils {
	public static final String EVENT_LIST_INTENT = "ShowEventList";
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
	
	
	public static void sendMail(Context c, String body) throws Exception{
		if(mTest != null){	// TODO Test only
			mTest.sendMail(body);
			return;
		}
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		String mailDest = PrefUtils.getStringPreference(prefs, R.string.mail_to_forward_key, c);
		String mailUser = PrefUtils.getStringPreference(prefs, R.string.gmail_user_key, c);
		String mailPwd = PrefUtils.getStringPreference(prefs, R.string.gmail_pwd_key, c);

		GMailSender sender = new GMailSender(mailUser, mailPwd);
		
		try{
			sender.sendMail("HOMEALONE", 
				  		 body, 
				  		 "HomeAloneSoftware", 
				  		 mailDest);
			
		}catch (Exception e){
			String shortDesc = c.getString(R.string.failed_to_send_email_to) + " " + mailDest ;
			String fullDesc = c.getString(R.string.email_body_not_sent) + " " + body ;
			notifyEvent(fullDesc, shortDesc, c);
			throw e;
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
		showErrorDialog(errorString, context,
											new OnClickListener() { 
												public void onClick(DialogInterface dialog, int arg1) {
													// do nothing
												} });
			
			
    	return;    
	}
	
	
	public static void showErrorDialog(String errorString, Context context, OnClickListener l)
	{
    	String button1String = context.getString(R.string.ok_name); 
    	AlertDialog.Builder ad = new AlertDialog.Builder(context); 
    	ad.setTitle(context.getString(R.string.error_name)); 
    	ad.setMessage(errorString); 
    	
    	OkDialogInterface j;
    	ad.setPositiveButton(button1String,l);
    	ad.show();
    	return;    
	}
	
	public static void notifyEvent(String event, String fullDescEvent, Context c, DbAdapter dbHelper){
		String svcName = Context.NOTIFICATION_SERVICE;
		NotificationManager notificationManager = (NotificationManager)c.getSystemService(svcName);
		
		
		// TODO set a valid icon
		int icon = android.R.drawable.stat_notify_chat;
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, event, when);
		notification.number = -1;
		
		String expandedText = fullDescEvent;
		String expandedTitle = c.getString(R.string.home_alone_event);
		
		// Intent to launch an activity when the extended text is clicked		
		Intent intent = new Intent(c, MainTabActivity.class);
		intent.putExtra(EVENT_LIST_INTENT, true);
		PendingIntent launchIntent = PendingIntent.getActivity(c, 0, intent, 0);
		notification.setLatestEventInfo(c,
		                                expandedTitle,
		                                expandedText,
		                                launchIntent);
		int notificationRef = 1;
		notificationManager.notify(notificationRef, notification);
		
		dbHelper.addEvent(fullDescEvent, event);


	}
	
	public static void removeNotifications(Context c){
		String svcName = Context.NOTIFICATION_SERVICE;
		NotificationManager notificationManager = (NotificationManager)c.getSystemService(svcName);
		notificationManager.cancel(1);
	}

	public static void notifyEvent(String event, String fullDescEvent, Context c){
		DbAdapter dbHelper = new DbAdapter(c);
		dbHelper.open();
		notifyEvent(event, fullDescEvent, c, dbHelper);
		dbHelper.close();
		
		Intent i = new Intent(HomeAloneService.HOMEALONE_EVENT_PROCESSED);
		c.sendBroadcast(i);
	}

	
	public static String[] getContactNumbers(String contact, Context c) throws NameNotFoundException{
	// Find a contact using a partial name match
		Uri lookupUri =
		Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, contact);
		Cursor idCursor = c.getContentResolver().query(lookupUri, null, null, null,
		                                              null);
		String id = null;
		if (idCursor.moveToFirst()) {	// I try with the first record
			int idIdx = idCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
			id = idCursor.getString(idIdx);
		}
		idCursor.close();
		if (id != null) {
		   // Return all the contact details of type PHONE for the contact we found
			String where = ContactsContract.Data.CONTACT_ID + " = " + id + " AND " +
									ContactsContract.Data.MIMETYPE + " = '" +
									ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE +"'";
			
			Cursor dataCursor = c.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, where, null, null);
		   // Use the convenience properties to get the index of the columns
			int nameIdx = dataCursor.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME);
			int phoneIdx = dataCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);
		
			String[] result = new String[dataCursor.getCount()];
		
			if (dataCursor.moveToFirst())
				do {
					// Extract the name.
					String name = dataCursor.getString(nameIdx);
					// Extract the phone number.
					String number = dataCursor.getString(phoneIdx);
					result[dataCursor.getPosition()] = name + " (" + number + ")";
				} while(dataCursor.moveToNext());
			dataCursor.close();
			return result;
		}
		throw new NameNotFoundException("");
	
	}
	
	private static Long get24HoursAgoTime(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR, -24);	 
		return cal.getTimeInMillis();
	}
	
	public static String[] getMissedCalls(Context context){
		Long lastFlushed = PrefUtils.getLastFlushedCalls(context);
		java.text.DateFormat timeForm = android.text.format.DateFormat.getTimeFormat(context);
		
		Long yesterday = get24HoursAgoTime();
		
		
		if(lastFlushed == 0 || lastFlushed < yesterday){
			lastFlushed = yesterday;	// not later than 24 hours ago
		}
		
		
		Cursor c = context.getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI,
				null, null, null, android.provider.CallLog.Calls.DATE + " DESC");
		String where = android.provider.CallLog.Calls.TYPE + " = " + android.provider.CallLog.Calls.MISSED_TYPE +
					" and " + android.provider.CallLog.Calls.DATE + " > " +  lastFlushed;
		
		int nameIdx = c.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME);
		int numbIdx = c.getColumnIndexOrThrow(CallLog.Calls.NUMBER);
		int dateIdx = c.getColumnIndexOrThrow(CallLog.Calls.DATE);
		
	
		String[] result = new String[c.getCount()];
	
		if (c.moveToFirst())
			do {
				String name = c.getString(nameIdx);
				String number = c.getString(numbIdx);
				String when = timeForm.format(c.getLong(numbIdx));
				result[c.getPosition()] = String.format(context.getString(R.string.flushed_calls), name, number, when);
			} while(c.moveToNext());
		c.close();
		return result;
		}
}
