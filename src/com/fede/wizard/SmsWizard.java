package com.fede.wizard;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fede.R;
import com.fede.Utilities.GeneralUtils;
import com.fede.Utilities.PrefUtils;

public class SmsWizard extends Activity {
	private Button 				mFinishButton;
	private Button 				mBackButton;
	private EditText			mSmsNumber;
	private EditText			mReply;
	
	private SharedPreferences.Editor mEditor;
	private SharedPreferences	mPrefs;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_sms_and_reply);
        
        mSmsNumber = (EditText) findViewById(R.id.Wizard_sms_target);
        mReply = (EditText) findViewById(R.id.Wizard_reply_message);
        
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mEditor = mPrefs.edit();

        setupButtons();
        fillValues();

    }
    
    

    private void fillValues(){
    	String smsTarget = PrefUtils.getStringPreference(mPrefs, R.string.sms_to_forward_key, this);
    	String reply = PrefUtils.getStringPreference(mPrefs, R.string.reply_key, this);
    	
    	mSmsNumber.setText(smsTarget);
    	mReply.setText(reply);
    	
    }
    
    private void storeValues(boolean enabled){
    	PrefUtils.setStringPreference(mEditor, R.string.sms_to_forward_key, mSmsNumber.getText().toString().trim(), this);
    	PrefUtils.setStringPreference(mEditor, R.string.reply_key, mReply.getText().toString().trim(), this);
    	PrefUtils.setBoolPreference(mEditor, R.string.forward_to_sms_key, enabled, this);
    	PrefUtils.setBoolPreference(mEditor, R.string.reply_enable_key, !(mReply.getText().toString().equals("")),
    			this);
    	mEditor.commit();
    }
 
   
    
    
    private void setupButtons()
    {

		// BUTTONS
    	mFinishButton = (Button) findViewById(R.id.SmsNextButton);
    	mBackButton = (Button) findViewById(R.id.SmsBackButton);
		
    	mFinishButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				if(mSmsNumber.getText().toString().equals("")){
					GeneralUtils.showConfirmDialog(view.getContext().getString(R.string.empty_sms_number), view.getContext(), new OnClickListener(){
						public void onClick(DialogInterface intf, int a){
							storeValues(false);
					        finish();
						}
					});
				}else{
					storeValues(true);
			        finish();
				}
			}
		});
	
		
		mBackButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				Intent i = new Intent(view.getContext(), MailWizard.class);
		        startActivity(i);
		        finish();
			}
		});
    }
}