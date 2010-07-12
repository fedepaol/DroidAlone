package com.fede;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.fede.Utilities.GeneralUtils;
import com.fede.Utilities.PrefUtils;

public class HomeAlone extends Activity {
	static final int MENU_OPTIONS = Menu.FIRST;	
	
	Button mActivateButton;
	Button mDisableButton;
	IncomingCallReceiver mReceiver;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setupButtons();
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		int groupId = 0;
		int menuItemId = MENU_OPTIONS;
		int menuItemOrder = Menu.NONE;	 
		int menuItemText = R.string.options;
		
		menu.add(groupId, menuItemId, menuItemOrder, menuItemText).setIcon(android.R.drawable.ic_menu_preferences);
		
		return true;
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
			
		}
	
		return true;
	}
    
    
    private void enableValidButton()
    {	
    	if(PrefUtils.homeAloneEnabled(this) == false){
    		mActivateButton.setClickable(true);
    		mDisableButton.setClickable(false);
    	}else{
    		mActivateButton.setClickable(true);
    		mDisableButton.setClickable(false);
    	}
    }
    
    
    private void setupButtons()
    {

		// BUTTONS
		mActivateButton = (Button) findViewById(R.id.ActivateButton);
		mDisableButton = (Button) findViewById(R.id.DisableButton);
		
		mActivateButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				if(PrefUtils.checkForwardingEnabled(view.getContext())){	
					PrefUtils.setStatus(true, view.getContext());
					enableValidButton();
				}else{
					GeneralUtils.showErrorDialog(view.getContext().getString(R.string.forwarding_not_enabled), view.getContext());
				}
				
			}});
		
		
		mDisableButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				PrefUtils.setStatus(false, view.getContext());
				enableValidButton();
			}});
		
		enableValidButton();
    }
}