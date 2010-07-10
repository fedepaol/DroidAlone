package com.fede;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

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
		mDbHelper.open();		
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
    	// Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{DbAdapter.EVENT_TIME_KEY,
        							 DbAdapter.SHORT_DESC_KEY};
        
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.event_elem_time, 
        					 R.id.event_elem_desc};
        
        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter events = 
        	    new SimpleCursorAdapter(this, R.layout.event_list_elem, eventCursor, from, to);
        setListAdapter(events);
    }
}