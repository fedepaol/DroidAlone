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

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

import com.fede.Utilities.GeneralUtils;

public class MainTabActivity extends TabActivity {
	@Override
	public void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.tab_layout);
	    	
	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab
	
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, HomeAlone.class);
	
	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("artists").setIndicator(getString(R.string.app_name),
	                      res.getDrawable(R.drawable.ic_tab_main))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, EventListActivity.class);
	    spec = tabHost.newTabSpec("albums").setIndicator(getString(R.string.events),
	                      res.getDrawable(R.drawable.ic_tab_events ))
	                  .setContent(intent);
	    tabHost.addTab(spec);
		
	    Bundle extras = getIntent().getExtras();
	    boolean showEvents = extras != null ? extras.getBoolean(GeneralUtils.EVENT_LIST_INTENT, false) 
		        : false;
	    
	    if(!showEvents){
	    	tabHost.setCurrentTab(0);
	    }else{
	    	tabHost.setCurrentTab(1);
	    }
	
	}
}