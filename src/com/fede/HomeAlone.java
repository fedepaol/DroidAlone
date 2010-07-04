package com.fede;

import com.fede.Utilities.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HomeAlone extends Activity {
	static final int MENU_OPTIONS = Menu.FIRST;
	Button activateButton;
	Button disableButton;
	
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
			}
		}
	
		return true;
	}
    
    
    private void enableValidButton()
    {	
    	if(PrefUtils.homeAloneEnabled(this) == false){
    		activateButton.setClickable(true);
    		disableButton.setClickable(false);
    	}else{
    		activateButton.setClickable(true);
    		disableButton.setClickable(false);
    	}
    }
    
    
    private void setupButtons()
    {

		// BUTTONS
		activateButton = (Button) findViewById(R.id.ActivateButton);
		disableButton = (Button) findViewById(R.id.DisableButton);
		
		activateButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				PrefUtils.setStatus(true, view.getContext());
				enableValidButton();
			}});
		
		
		disableButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				PrefUtils.setStatus(false, view.getContext());
				enableValidButton();
			}});
		
		enableValidButton();
    }
}