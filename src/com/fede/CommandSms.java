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
	public static final String BEGIN_STRING = "#";
	SharedPreferences prefs;
	
	/* status variables */
	private BoolCommand status = BoolCommand.UNDEF;
	private String smsDest = "";
	private boolean smsDestChange = false;
	private String mailDest = "";
	private boolean mailDestChange = false;
	private String replyCommand = "";
	private boolean replyCommandChange = false;
	private BoolCommand echoCommand = BoolCommand.UNDEF;
	
	private String smsBody;
	private String incomingNumber;
	private Context context;
	
	
	// Checks the password with the one stored in the preferences
	private void checkPassword(String pwd) throws InvalidPasswordException{
		String PASSWORD_KEY = context.getString(R.string.password_key);
		String prefPwd = prefs.getString(PASSWORD_KEY, "*");
		
		if(!pwd.equals(prefPwd)){
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
		if(!body.startsWith(BEGIN_STRING)){	// if it doesn't start with  a # is not a valid command message
			return false;
		}else 
			return true;
	}
	
	public CommandSms(Bundle b, Context c) throws InvalidCommandException 
	{
		this(b,  b.getString(HomeAloneService.MESSAGE_BODY), c);		
	}
	
	public CommandSms(Bundle b, String body, Context c) throws InvalidCommandException 
	{	
		context = c;
		incomingNumber = b.getString(HomeAloneService.NUMBER);
		smsBody = body;
		
		prefs = PreferenceManager.getDefaultSharedPreferences(c);
		
		if(!isCommandSms(smsBody)){	// if it doesn't start with  a # is not a valid command message
			throw new InvalidCommandException("Does not start with " + BEGIN_STRING);
		}
		
		String[] commands = smsBody.split("-");
				
		String password = getPasswordFromCommands(commands);
		checkPassword(password);
		
		for (String cmnd : commands){
			if(!cmnd.startsWith(BEGIN_STRING))	// Skipping first command
				parseCommand(cmnd);
		}
		
	}
	
	// Command structure: com[:value]
	void parseCommand(String c) throws InvalidCommandException{
		String[] parseRes = c.split(":");
		
		String commandName = parseRes[0];
		String commandValue = parseRes.length > 1? parseRes[1] : null;
		
		
		if(commandName.equals(STATUS_COMMAND)){
			if(commandValue == null)
				throw new CommandParseException("Status expects value");
		
			if(commandValue.equals(STATUS_ON)){
				status = BoolCommand.ENABLED;
			}else if(commandValue.equals(STATUS_OFF)){
				status = BoolCommand.DISABLED;
			}else
				throw new CommandParseException("Invalid status value");
			
			return;
		}
		
		if(commandName.equals(SMS_DEST_COMMAND)){
			smsDestChange = true;
			if(commandValue != null){
				smsDest = "";
			}else{
				smsDest = commandValue;
			}
			return;
		}
		
		if(commandName.equals(MAIL_DEST_COMMAND)){
			mailDestChange = true;
			if(commandValue != null){
				mailDest = "";
			}else{
				mailDest = commandValue;
			}
			return;
		}
		
		if(commandName.equals(REPLY_COMMAND)){
			replyCommandChange = true;
			if(commandValue != null){
				replyCommand = "";
			}else{
				replyCommand = commandValue;
			}
			return;
		}
		
		if(commandName.equals(ECHO_STATUS_COMMAND)){
			echoCommand = BoolCommand.ENABLED;
			return;
		}
		
		throw new CommandParseException("Invalid command name " + commandName);
	}
	
	
	// tells if the command wants to disable the feature sms: with no string means to disable the 
	// sms
	private boolean isDisableFeatureCommand(String s)
	{
		if(s.equals(""))
			return true;
		return false;
	}
	
	// updates preferences from command instructions
	private void updatePreferences()
	{
		SharedPreferences.Editor prefEditor = prefs.edit();
		
		if(smsDestChange){
			String SMS_ENABLE_KEY = context.getString(R.string.forward_to_sms_key);
			String SMS_TO_FWD_KEY = context.getString(R.string.sms_to_forward_key);
			if(isDisableFeatureCommand(smsDest)){
				prefEditor.putBoolean(SMS_ENABLE_KEY, false);
			}else{
				prefEditor.putBoolean(SMS_ENABLE_KEY, true);
				prefEditor.putString(SMS_TO_FWD_KEY, smsDest);
			}
		}
		
		if(mailDestChange){
			String MAIL_ENABLE_KEY = context.getString(R.string.forward_to_mail_key);
			String MAIL_TO_FWD_KEY = context.getString(R.string.mail_to_forward_key);
			if(isDisableFeatureCommand(mailDest)){				
				prefEditor.putBoolean(MAIL_ENABLE_KEY, false);
			}else{
				prefEditor.putBoolean(MAIL_ENABLE_KEY, true);
				prefEditor.putString(MAIL_TO_FWD_KEY, mailDest);

			}
		}
		
		if(replyCommandChange){
			String REPLY_ENABLE_KEY = context.getString(R.string.reply_enable_key);
			String REPLY_KEY = context.getString(R.string.reply_key);
			if(isDisableFeatureCommand(replyCommand)){				
				prefEditor.putBoolean(REPLY_ENABLE_KEY, false);
			}else{
				prefEditor.putBoolean(REPLY_ENABLE_KEY, true);
				prefEditor.putString(REPLY_KEY, replyCommand);
			}
		}		
	}

	public void execute()
	{
		updatePreferences();
		if(echoCommand == BoolCommand.ENABLED){
			String status = PrefUtils.getPreferencesStatus(context);
			PrefUtils.sendSms(incomingNumber, status);
		}
	}
	
	public BoolCommand getStatus() {
		return status;
	}
	
	public BoolCommand getEcho() {
		return echoCommand;
	}
	
}
