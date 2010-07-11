package com.fede;

import java.util.Date;

import android.app.ListActivity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class EventListActivity extends ListActivity {

	protected DbAdapter mDbHelper;
	private static final int MENU_DELETE = Menu.FIRST;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);
        mDbHelper = new DbAdapter(this);
        mDbHelper.open();
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
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	// Per ora vuoto TODO
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
		private java.text.DateFormat mDateFormat;
		private java.text.DateFormat mTimeFormat;

        Context mContext;
        private LayoutInflater mInflater;

        public EventListAdapter(Context context, Cursor c) {
            super(context, c, true);
            mContext = context;
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            mDateFormat = android.text.format.DateFormat.getDateFormat(context);    // short date
            mTimeFormat = android.text.format.DateFormat.getTimeFormat(context);    // 12/24 time
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