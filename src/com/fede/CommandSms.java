package com.fede;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

/* Sms structure :
 * #password-e-s:on-m:fedepaol@gmail.com-sms:3286991883-r:I left my phone at home. Call me at office
 * 
 * e : sends an echo message to show the state
 * s : status. May be on or off
 * m : mail to send the notifications  to. Null disables mail notifications
 * sms : number to send the notifications to. Null disables sms notifications
 * r: reply message to caller / sms sender. Null disables replies
 * 
 */



public class CommandSms {
	private String smsBody;
	public static final String STATUS_COMMAND = "s";
	public static final String SMS_DEST_COMMAND = "sms";
	public static final String MAIL_DEST_COMMechoAND = "m";
	public static final String REPLY_COMMAND = "r";
	public static final String ECHO_STATUS_COMMAND = "e";
	SharedPreferences prefs;
	
	
	// Checks the password with the one stored in the preferences
	private void checkPassword(String pwd){
		String prefPwd = prefs.getString("PASSWORD", "*");
		if(pwd != prefPwd){
			// TODO Throw exception
		}
	}
	
	private String getPasswordFromCommands(String[] commands)
	{
		return commands[0].substring(1);	
	}
	
	
	public CommandSms(Bundle b, String incomingNum, Context c){
		smsBody = b.getString(HomeAloneService.MESSAGE_BODY);
		prefs = PreferenceManager.getDefaultSharedPreferences(c);
		
		if(!smsBody.startsWith("#")){	// if it doesn't start with  a # is not a valid command message
			// TODO Throw exception
		}
		
		String[] commands = smsBody.split("-");
				
		String password = getPasswordFromCommands(commands);
		checkPassword(password);
		
		for (int i = 0; i < commands.length; i++){
			parseCommand(commands[i]);
		}
		
	}
	
	// Command structure: com[:value]
	void parseCommand(String c){
		String[] parseRes = c.split(":");
		c = parseRes[0];
	}
}
