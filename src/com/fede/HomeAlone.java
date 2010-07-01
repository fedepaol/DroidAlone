package com.fede;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HomeAlone extends Activity {
	static final int MENU_OPTIONS = Menu.FIRST;
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
    
    private void setupButtons()
    {

		// BUTTONS
		Button activateButton = (Button) findViewById(R.id.ActivateButton);
		activateButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				//
			}});
		
		Button disableButton = (Button) findViewById(R.id.DisableButton);
		disableButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				//
			}});

    }
}