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

package com.fede;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.fede.Utilities.GeneralUtils;
import com.fede.Utilities.PrefUtils;
import com.fede.wizard.StartWizard;

public class HomeAlone extends Activity {
	static final int MENU_OPTIONS = Menu.FIRST;	
	static final int MENU_WIZARD = Menu.FIRST + 1;
	static final int MENU_HELP = Menu.FIRST + 2;
	
	private BroadcastReceiver 	mBroadcastRecv;
	private IntentFilter 		mFilter;
	private ImageView			mStatusImage;
	IncomingCallReceiver 		mReceiver;


	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setupButtons();

        mBroadcastRecv = new BroadcastReceiver(){
    		@Override
    		public void onReceive(Context context, Intent intent) {	
    			setButtonCaption();
    		}
    	};
    	
    	performFirstLaunch();
    	
    	
    }
    
    
    private void performFirstLaunch(){
    	if(PrefUtils.showChangeLog(this)){
    	   	AlertDialog.Builder ad = new AlertDialog.Builder(this); 
    	   	ad.setTitle(R.string.change_log_title);
        	ad.setMessage(R.string.change_log); 
        	ad.setPositiveButton(R.string.ok_name,null);
        	ad.show();
    	}
    	
    	if(PrefUtils.showWizard(this)){
    		launchWizard();
    	}
	}
    
	@Override
	protected void onPause() {		
		super.onPause();
		unregisterReceiver(mBroadcastRecv);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mBroadcastRecv, mFilter);
		setButtonCaption();
	}
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		int groupId = 0;
		int menuItemId = MENU_OPTIONS;
		int menuItemOrder = Menu.NONE;	 
		int menuItemText = R.string.options;
		
		menu.add(groupId, menuItemId, menuItemOrder, menuItemText).setIcon(android.R.drawable.ic_menu_preferences);
		 
		menuItemText = R.string.wizard;
		menuItemId = MENU_WIZARD;
		// TODO Icona
		menu.add(groupId, menuItemId, menuItemOrder, menuItemText).setIcon(android.R.drawable.ic_menu_manage );
		
		menuItemText = R.string.help_name;
		menuItemId = MENU_HELP;
		// TODO Icona
		menu.add(groupId, menuItemId, menuItemOrder, menuItemText).setIcon(android.R.drawable.ic_menu_help );

		
		return true;
	}
    
    private void launchWizard(){	// Will this throw a magician?
    	Intent i = new Intent(this, StartWizard.class); 
		startActivity(i);
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		super.onOptionsItemSelected(item);
		switch(item.getItemId()){
			case MENU_OPTIONS:{
				Intent i = new Intent(this, HomeAlonePreferences.class); 
				startActivity(i);
				break;
			}
			case MENU_WIZARD:{
				launchWizard();
				break;
			}
			case MENU_HELP:{
				Intent i = new Intent(this, HomeAloneHelp.class); 
				startActivity(i);
				break;
			}
		}
	
		return true;
	}
    
    
    private void setButtonCaption()
    {	
    	if(PrefUtils.homeAloneEnabled(this) == false){
    		mStatusImage.setImageResource(R.drawable.disable_state_image);
    	}else{
    		mStatusImage.setImageResource(R.drawable.enabled_state_image);
    	}
    }
    
    
    private void setupButtons()
    {

		// BUTTONS
		
		mStatusImage = (ImageView) findViewById(R.id.StatusImageView);
		mStatusImage.setClickable(true);
		mStatusImage.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
		    	if(PrefUtils.homeAloneEnabled(view.getContext()) == true){
		    		PrefUtils.setStatus(false, view.getContext());
		    	}else{
		    		if(PrefUtils.checkForwardingEnabled(view.getContext())){	
		    			PrefUtils.setStatus(true, view.getContext());
		    		}else{
		    			GeneralUtils.showErrorDialog(view.getContext().getString(R.string.forwarding_not_enabled), view.getContext());
		    		}
		    	}
    			setButtonCaption();
			}
		});
		setButtonCaption();	
    }
}