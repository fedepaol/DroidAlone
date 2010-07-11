package com.fede;

import java.util.Date;

import fede.geotagger.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
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

	
	private java.text.DateFormat mDateFormat;
	private java.text.DateFormat mTimeFormat;


	
	
	protected DbAdapter mDbHelper;
	private static final int MENU_DELETE = Menu.FIRST;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);
        mDbHelper = new DbAdapter(this);
        mDbHelper.open();
        mDateFormat = android.text.format.DateFormat.getDateFormat(this);    // short date
        mTimeFormat = android.text.format.DateFormat.getTimeFormat(this);    // 12/24 time

        fillData();
    }
    
	@Override
	protected void onPause() {		
		super.onPause();
//TODO
		}

	@Override
	protected void onResume() {
		super.onResume();
//TODO
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		int groupId = 0;
		int menuItemId = MENU_DELETE;
		int menuItemOrder = Menu.NONE;	 
		int menuItemText = R.string.delete_all;
		
		menu.add(groupId, menuItemId, menuItemOrder, menuItemText).setIcon(android.R.drawable.ic_menu_preferences);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		super.onOptionsItemSelected(item);
		switch(item.getItemId()){
			case MENU_DELETE:{
				mDbHelper.removeAllEvents();
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
    	Cursor eventCursor = mDbHelper.getAllEvents();
    	startManagingCursor(eventCursor);
        // Now create a simple cursor adapter and set it to display
        EventListAdapter events = 
        	    new EventListAdapter(this, eventCursor);
        
        setListAdapter(events);
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