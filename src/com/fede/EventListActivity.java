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

import java.util.Date;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class EventListActivity extends ListActivity {

	private Cursor mEventCursor;
	private java.text.DateFormat mDateFormat;
	private java.text.DateFormat mTimeFormat;
	private BroadcastReceiver mBroadcastRecv;
	
	private IntentFilter mFilter;

	
	protected DbAdapter mDbHelper;
	private static final int MENU_DELETE = Menu.FIRST;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.event_list);
        mDbHelper = new DbAdapter(this);
        mDbHelper.open();
        mEventCursor = mDbHelper.getAllEvents();

        mDateFormat = android.text.format.DateFormat.getDateFormat(this);    // short date
        mTimeFormat = android.text.format.DateFormat.getTimeFormat(this);    // 12/24 time
        mFilter = new IntentFilter(HomeAloneService.HOMEALONE_EVENT_PROCESSED);
        
        fillData();
        
        mBroadcastRecv = new BroadcastReceiver(){
    		@Override
    		public void onReceive(Context context, Intent intent) {	
    			mEventCursor.requery();
    		}
    	};
        
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
		mEventCursor.requery();
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		int groupId = 0;
		int menuItemId = MENU_DELETE;
		int menuItemOrder = Menu.NONE;	 
		int menuItemText = R.string.delete_all;
		
		menu.add(groupId, menuItemId, menuItemOrder, menuItemText).setIcon(android.R.drawable.ic_menu_delete);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		super.onOptionsItemSelected(item);
		switch(item.getItemId()){
			case MENU_DELETE:{
				mDbHelper.removeAllEvents();
				mEventCursor.requery();
			}
		}
	
		return true;
	} 
	
	public void showDialog(String message, String title)
	{
    	String button1String = getString(R.string.ok_name); 
    	AlertDialog.Builder ad = new AlertDialog.Builder(this); 
    	ad.setTitle(title); 
    	ad.setMessage(message); 
    	ad.setPositiveButton(button1String,
    						 new OnClickListener() { 
	    						public void onClick(DialogInterface dialog, int arg1) {
	    							// do nothing
	    						} });
    	ad.show();
    	return;    
	}	
	   
	    
		
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Cursor c = mDbHelper.getEvent(id);
		c.moveToFirst();
		String fullStatus = c.getString(DbAdapter.EVENT_DESCRIPTION_COLUMN);
		String shortStatus = c.getString(DbAdapter.SHORT_DESC_COLUMN);
		Long time = c.getLong(DbAdapter.EVENT_TIME_COLUMN);
		String timeString = mTimeFormat.format(time);
		String dateString = mDateFormat.format(time);
		
		String message = String.format("%s\n%s\n%s", dateString, timeString, fullStatus);
		showDialog(message, shortStatus);
	}
	
    private void fillData(){
    	startManagingCursor(mEventCursor);
        // Now create a simple cursor adapter and set it to display
    	EventListAdapter mEventsAdapter = 
        	    new EventListAdapter(this, mEventCursor);
        setListAdapter(mEventsAdapter);
    }
    
 
    
    /* package */ class EventListAdapter extends CursorAdapter {

        Context mContext;
        private LayoutInflater mInflater;

        public EventListAdapter(Context context, Cursor c) {
            super(context, c, true);
            mContext = context;
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

    
        @Override
        protected synchronized void onContentChanged() {
        	super.onContentChanged(); 
        }

        private boolean isDateToday(Date date) {
            Date today = new Date();
            if (date.getYear() == today.getYear() &&
                    date.getMonth() == today.getMonth() &&
                    date.getDate() == today.getDate()) {
                return true;
            }
            return false;
        }
        

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Reset the view (in case it was recycled) and prepare for binding
            
            TextView dateView = (TextView) view.findViewById(R.id.event_elem_time);
            long timestamp = cursor.getLong(DbAdapter.EVENT_TIME_COLUMN);
            Date date = new Date(timestamp);
            String text = "";
            if (isDateToday(date)) {
                text = mTimeFormat.format(date);
            } else {
                text = mDateFormat.format(date);
            }
            dateView.setText(text);
            
            TextView eventDescView = (TextView) view.findViewById(R.id.event_elem_desc);
            String desc = cursor.getString(DbAdapter.SHORT_DESC_COLUMN);
            
            // TODO Accorciare se troppo lungo
            eventDescView.setText(desc);

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return mInflater.inflate(R.layout.event_list_elem, parent, false);
        }

    }
    
}