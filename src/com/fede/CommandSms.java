package com.fede;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.fede.MessageException.InvalidCommandException;
import com.fede.MessageException.InvalidPasswordException;


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
	public enum BoolCommand {UNDEF, ENABLED, DISABLED};
	public static final String STATUS_COMMAND = "s";
	public static final String SMS_DEST_COMMAND = "sms";
	public static final String MAIL_DEST_COMMAND = "m";
	public static final String REPLY_COMMAND = "r";
	public static final String ECHO_STATUS_COMMAND = "e";
	SharedPreferences prefs;
	
	/* status variables */
	private BoolCommand status;
	private String smsDest = "";
	private String mailDest = "";
	private String replyCommand = "";
	private BoolCommand echoCommand;
	
	private String smsBody;
	
	
	
	// Checks the password with the one stored in the preferences
	private void checkPassword(String pwd) throws InvalidPasswordException{
		String prefPwd = prefs.getString("PASSWORD", "*");
		if(pwd != prefPwd){
			throw new InvalidPasswordException(pwd + " is not the current password");
		}
	}
	
	private String getPasswordFromCommands(String[] commands)
	{
		return commands[0].substring(1);	
	}
	
	
	public CommandSms(Bundle b, String incomingNum, Context c) throws InvalidCommandException {
		smsBody = b.getString(HomeAloneService.MESSAGE_BODY);
		prefs = PreferenceManager.getDefaultSharedPreferences(c);
		
		if(!smsBody.startsWith("#")){	// if it doesn't start with  a # is not a valid command message
			throw new InvalidCommandException("Does not start with # ");
		}
		
		String[] commands = smsBody.split("-");
				
		String password = getPasswordFromCommands(commands);
		checkPassword(password);
		
		for (String cmnd : commands){
			parseCommand(cmnd);
		}
		
	}
	
	// Command structure: com[:value]
	void parseCommand(String c){
		String[] parseRes = c.split(":");
		c = parseRes[0];
	}
}
