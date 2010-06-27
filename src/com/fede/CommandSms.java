package com.fede;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.fede.MessageException.CommandParseException;
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
	public static final String STATUS_OFF = "off";
	public static final String STATUS_ON = "on";
	
	public static final String SMS_DEST_COMMAND = "sms";
	public static final String MAIL_DEST_COMMAND = "m";
	public static final String REPLY_COMMAND = "r";
	public static final String ECHO_STATUS_COMMAND = "e";
	SharedPreferences prefs;
	
	/* status variables */
	private BoolCommand status = BoolCommand.UNDEF;
	private String smsDest = "";
	private String mailDest = "";
	private String replyCommand = "";
	private BoolCommand echoCommand = BoolCommand.UNDEF;
	
	private String smsBody;
	private String incomingNumber;
	
	
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
	
	public String getSmsBody() {
		return smsBody;
	}

	public String getIncomingNumber() {
		return incomingNumber;
	}
	
	
	
	public static boolean isCommandSms(String body)
	{
		if(!body.startsWith("#")){	// if it doesn't start with  a # is not a valid command message
			return false;
		}else 
			return true;
	}
	
	public CommandSms(Bundle b, Context c) throws InvalidCommandException 
	{
		String body = b.getString(HomeAloneService.MESSAGE_BODY);
		new CommandSms(b, body, c);	// TODO Controllare se va bene cosi'
	}
	
	public CommandSms(Bundle b, String body, Context c) throws InvalidCommandException 
	{	
		incomingNumber = b.getString(HomeAloneService.NUMBER);
		smsBody = body;
		
		prefs = PreferenceManager.getDefaultSharedPreferences(c);
		
		if(!isCommandSms(smsBody)){	// if it doesn't start with  a # is not a valid command message
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
	void parseCommand(String c) throws InvalidCommandException{
		String[] parseRes = c.split(":");
		
		String commandName = parseRes[0];
		String commandValue = parseRes.length > 1? parseRes[1] : null;
		
		
		if(commandName == STATUS_COMMAND){
			if(commandValue == null)
				throw new CommandParseException("Status expects value");
		
			if(commandValue == STATUS_ON){
				status = BoolCommand.ENABLED;
			}else if(commandValue == STATUS_OFF){
				status = BoolCommand.DISABLED;
			}else
				throw new CommandParseException("Invalid status value");
			
			return;
		}
		
		if(commandName == SMS_DEST_COMMAND){
			if(commandValue != null){
				smsDest = "";
			}else{
				smsDest = commandValue;
			}
			return;
		}
		
		if(commandName == MAIL_DEST_COMMAND){
			if(commandValue != null){
				mailDest = "";
			}else{
				mailDest = commandValue;
			}
			return;
		}
		
		if(commandName == REPLY_COMMAND){
			if(commandValue != null){
				replyCommand = "";
			}else{
				replyCommand = commandValue;
			}
			return;
		}
		
		if(commandName == ECHO_STATUS_COMMAND){
			echoCommand = BoolCommand.ENABLED;
			return;
		}
		
		throw new CommandParseException("Invalid command name " + commandName);
	}

	public BoolCommand getStatus() {
		return status;
	}
}
