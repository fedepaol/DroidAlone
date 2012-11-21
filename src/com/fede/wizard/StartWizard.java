/*
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package com.fede.wizard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.actionbarsherlock.app.SherlockActivity;
import com.fede.R;
import com.fede.Utilities.GeneralUtils;
import com.fede.Utilities.PrefUtils;

public class StartWizard extends SherlockActivity {
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
        mPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setNextEnabled();
            }
        });

        fillValues();
    }
    

    private void setNextEnabled(){
        if(mPwd.getText().equals("")){
            mNextButton.setEnabled(false);
        }else{
            mNextButton.setEnabled(true);
        }
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