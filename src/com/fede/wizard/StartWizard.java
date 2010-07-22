package com.fede.wizard;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fede.R;
import com.fede.Utilities.GeneralUtils;
import com.fede.Utilities.PrefUtils;

public class StartWizard extends Activity {
	private Button 				mNextButton;
	private Button 				mCancelButton;
	private EditText			mPwd;
	private SharedPreferences	mPrefs;
	private SharedPreferences.Editor mEditor;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_password_intro);
        
        mPwd = (EditText) findViewById(R.id.Wizard_password);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mEditor = mPrefs.edit();

        setupButtons();
        fillValues();

    }
    
    

    private void fillValues(){
    	String pwd = PrefUtils.getStringPreference(mPrefs, R.string.password_key, this);
    	mPwd.setText(pwd);
    }
    
    private void storeValues(){
    	PrefUtils.setStringPreference(mEditor, R.string.password_key, mPwd.getText().toString().trim(), this);
    	mEditor.commit();
    }
    
    private void setupButtons()
    {

		// BUTTONS
    	mNextButton = (Button) findViewById(R.id.NextButton);
    	mCancelButton = (Button) findViewById(R.id.CancelButton);
		
		
		mNextButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				String pwd = mPwd.getText().toString();
				if(!pwd.equals("")){
					storeValues();
					Intent i = new Intent(view.getContext(), MailWizard.class);
			        startActivity(i);
			        finish();
				}else{
					GeneralUtils.showErrorDialog(getString(R.string.empty_password), view.getContext());
				}
			}
		});
	
		
		mCancelButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
					finish();
			}
		});
    }
}