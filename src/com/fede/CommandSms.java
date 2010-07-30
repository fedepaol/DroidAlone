package com.fede;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.fede.MessageException.CommandParseException;
import com.fede.MessageException.ForwardingDisabledException;
import com.fede.MessageException.InvalidCommandException;
import com.fede.MessageException.InvalidPasswordException;
import com.fede.MessageException.LocationNotFoundException;
import com.fede.Utilities.GeneralUtils;
import com.fede.Utilities.LocationUpdater;
import com.fede.Utilities.PrefUtils;


/* Sms structure :
 * #password-e-s:on-m:fedepaol@gmail.com-sms:3286991883-r:I left my phone at home. Call me at office-g:fedepaol
 * 
 * e : sends an echo message to show the state
 * s + on / off: status. May be on or off
 * m + mail address : mail to send the notifications  to. Null disables mail notifications
 * sms + number : number to send the notifications to. Null disables sms notifications
 * r + reply: reply message to caller / sms sender. Null disables replies
 * g + name: tries to retrieve the number(s) associated to the name and returns them
 * 
 * Only password #password shows help
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
	public static final String GET_NUMBER_COMMAND = "g";
	public static final String GET_POSITION_COMMAND = "pos";	
	public static final String BEGIN_STRING = "#";
	public static final String EXAMPLE = "#pwd-e-s:on/off-m:aa@bb.com-sms:123123-r:hello-g:fedepaol-pos  -m, -sms, -r without arg reset mail dest";
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
	private boolean mIsHelpMessage;
	private boolean retrieveNumber = false;
	private String numberToRetrieve = "";
	private boolean getPosition;
	
	private String smsBody;
	private String incomingNumber;
	private Context context;
	
	
	
	// Checks the password with the one stored in the preferences
	private void checkPassword(String pwd) throws InvalidPasswordException{
		String PASSWORD_KEY = context.getString(R.string.password_key);
		String prefPwd = prefs.getString(PASSWORD_KEY, "*");
		
		if(!pwd.equals(prefPwd)){
			throw new InvalidPasswordException(pwd + " is not the current password", context);
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
		this(b,  b.getString(HomeAloneService.MESSAGE_BODY), b.getString(HomeAloneService.NUMBER), c);		
	}
	
	public CommandSms(Bundle b, String body,String number, Context c) throws InvalidCommandException 
	{	
		context = c;
		incomingNumber = number;
		smsBody = body;
		mIsHelpMessage = false;
		
		prefs = PreferenceManager.getDefaultSharedPreferences(c);

		if(!isCommandSms(smsBody)){	// if it doesn't start with  a # is not a valid command message
			throw new InvalidCommandException(context.getString(R.string.doesnt_start_with) + " " + BEGIN_STRING, context);
		}
		
		String[] commands = smsBody.split("-");
				
		String password = getPasswordFromCommands(commands);
		checkPassword(password);
		
		if(commands.length == 1){ // only password
			mIsHelpMessage = true;
		}
	
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
				throw new CommandParseException(context.getString(R.string.status_expect_value), context);
		
			if(commandValue.equals(STATUS_ON)){
				if(PrefUtils.homeAloneEnabled(context))
					throw new CommandParseException(context.getString(R.string.already_enabled_error), context);
				status = BoolCommand.ENABLED;
			}else if(commandValue.equals(STATUS_OFF)){
				if(!PrefUtils.homeAloneEnabled(context))
					throw new CommandParseException(context.getString(R.string.already_disabled_error), context);
				status = BoolCommand.DISABLED;
			}else
				throw new CommandParseException(context.getString(R.string.invalid_status_value) + ":" + commandValue, context);
			
			return;
		}
		
		if(commandName.equals(SMS_DEST_COMMAND)){
			smsDestChange = true;
			if(commandValue != null){
				if(GeneralUtils.isPhoneNumber(commandValue))
					smsDest = commandValue;
				else
					throw new CommandParseException(commandValue + " " + context.getString(R.string.not_valid_number_message), context);				
			}else{
				smsDest = "";
			}
			return;
		}
		
		if(commandName.equals(GET_NUMBER_COMMAND)){
			retrieveNumber = true;
			if(commandValue != null){
				numberToRetrieve = commandValue;								
			}else{
				throw new CommandParseException(context.getString(R.string.get_number_invalid_name), context);
			}
			return;
		}
		
		// TODO
		/* if(commandName.equals(GET_POSITION_COMMAND)){
			getPosition = true;
			if(commandValue != null){
				throw new CommandParseException(context.getString(R.string.invalid_status_value) + ":" + commandValue, context);								
			}
			return;
		}*/
		
		if(commandName.equals(MAIL_DEST_COMMAND)){
			mailDestChange = true;
			if(commandValue != null){
				if(!GeneralUtils.isMail(commandValue)){
					throw new CommandParseException(commandValue + " " + context.getString(R.string.not_valid_mail_message), context);
				}
				if(!PrefUtils.validMailUserPwd(context, prefs)){
					throw new CommandParseException(context.getString(R.string.invalid_mail_user), context);
				}
		
				if(!GeneralUtils.isNetworkAvailable(context)){
					throw new CommandParseException(context.getString(R.string.network_not_available), context);
				}
				mailDest = commandValue;						
			}else{
				mailDest = "";								
			}
			return;
		}
		
		if(commandName.equals(REPLY_COMMAND)){
			replyCommandChange = true;
			if(commandValue != null){
				replyCommand = commandValue;
			}else{
				replyCommand = "";
			}
			return;
		}
		
		if(commandName.equals(ECHO_STATUS_COMMAND)){
			echoCommand = BoolCommand.ENABLED;
			return;
		}
		
		throw new CommandParseException("Invalid command name " + commandName, context);
	}
	
	
	// tells if the command wants to disable the feature sms: with no string means to disable the 
	// sms
	private boolean isDisableFeatureCommand(String s)
	{
		if(s.equals(""))
			return true;
		return false;
	}
	
	private void sendHelpMessage(){
		GeneralUtils.sendSms(incomingNumber, EXAMPLE, context);
		
		GeneralUtils.notifyEvent(context.getString(R.string.help_requested), String.format(context.getString(R.string.help_requested_full), incomingNumber), context);
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
		prefEditor.commit();
	}

	private void sendNumbersForName(String name){
		try{
			String[] res = GeneralUtils.getContactNumbers(name, context);
			
			StringBuffer buf = new StringBuffer();
		
			for (String number:res){
				buf.append(number);
				buf.append(" ");
			}
			GeneralUtils.sendSms(incomingNumber,
					String.format(context.getString(R.string.number_for_contact), name, buf.toString()),
					 context);

		
		}catch(NameNotFoundException e){
			GeneralUtils.sendSms(incomingNumber,
				String.format(context.getString(R.string.no_name_found), name),
				 context);
		}
		
		
	}
	
	private void sendActivationMail(Context c, String number) throws InvalidCommandException{
		try{
			GeneralUtils.sendMail(c, String.format(c.getString(R.string.mail_activated_message), number));
		}catch (Exception e){
			throw new InvalidCommandException(c.getString(R.string.failed_to_send_activ_mail), c);
		}
	}
	
	
	private void flushMissedCalls(){
		String[] missedCalls = GeneralUtils.getMissedCalls(context); 
		
		if(missedCalls.length == 0)
			return;
		
		StringBuffer buf = new StringBuffer();
		for(String missed : missedCalls){
			buf.append(missed);
			buf.append("  ");	
		}
		EventForwarder f = new EventForwarder(buf.toString(), context);
		f.forward();
	}
	
	// Action to be performed if the command enables homealone
	private void execEnableActions() throws InvalidCommandException{
		if(!PrefUtils.checkForwardingEnabled(context)){
			throw new ForwardingDisabledException(context.getString(R.string.forwarding_not_enabled), context);
		}
		
		if(PrefUtils.mailForwardingEnabled(context)){
			sendActivationMail(context, incomingNumber);
		}
		
		flushMissedCalls();
	}
	
	// Notifies current phone positions using configured forwarding options
	private void notifyPosition() throws LocationNotFoundException{
			LocationUpdater updater = new LocationUpdater(context);
			Location l = updater.getLocation();
			
			//TODO Notify
	}
	
	public void execute() throws InvalidCommandException
	{
		if(mIsHelpMessage){
			sendHelpMessage();
		}else{
			updatePreferences();
			String status = PrefUtils.getPreferencesStatus(context);
	
			if(echoCommand == BoolCommand.ENABLED){
				GeneralUtils.sendSms(incomingNumber, status, context);
			}
			
			if(retrieveNumber){
				sendNumbersForName(numberToRetrieve);
			}
			
			/*if(getPosition){ // TODO
				notifyPosition();
			}*/
			
			if(getStatus() == BoolCommand.ENABLED){
				execEnableActions();
			}
			
			notifyCommandExecution(status, echoCommand);
		}
	}
	
	private void notifyCommandExecution(String status, BoolCommand echoCommand){
		String shortDesc = context.getString(R.string.command_executed) + " " + incomingNumber;
		String fullDesc = context.getString(R.string.incoming_sms) + " " + smsBody;
		GeneralUtils.notifyEvent(shortDesc, fullDesc, context);
	}
	
	public BoolCommand getStatus() {
		return status;
	}
	
	public BoolCommand getEcho() {
		return echoCommand;
	}
	
}
