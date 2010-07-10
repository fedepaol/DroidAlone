package com.fede;


import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DbAdapter {
  private static final String DATABASE_NAME = "homeAloneDb.db";
  
  private static final int DATABASE_VERSION = 1;
  
  //Events
  	private static final String EVENT_TABLE = "Events";
  	public static final String EVENT_DESCRIPTION_KEY = "Description";
	public static final int EVENT_DESCRIPTION_COLUMN = 1;
	public static final String EVENT_TIME_KEY = "Time";
	public static final int EVENT_TIME_COLUMN = 2;
	public static final String SHORT_DESC_KEY = "Short description";
	public static final int SHORT_DESC_COLUMN = 3;
		
	
	public static final String ROW_ID = "_id";
	
	
  
  // SQL Statement to create a new database.
  private static final String DATABASE_EVENT_CREATE = "create table " + 
  EVENT_TABLE + " (" + ROW_ID + 
    " integer primary key autoincrement, " +
    EVENT_DESCRIPTION_KEY + " string, " + 
    EVENT_TIME_KEY + " integer, " +
    SHORT_DESC_KEY + " string;";
  
    			
    			
  // Variable to hold the database instance
  private SQLiteDatabase db;
  // Context of the application using the database.
  private final Context context;
  // Database open/upgrade helper
  private myDbHelper dbHelper;

  public DbAdapter(Context _context) {
    context = _context;
    dbHelper = new myDbHelper(context, DATABASE_NAME, null, DATABASE_VERSION);

  }

  public DbAdapter open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
  }

  public void close() {
	  db.close();
  }

  // POSITION
  
  public long addEvent(String event, String shortDesc, Date date)
  {
	    ContentValues contentValues = new ContentValues();
	    contentValues.put(EVENT_DESCRIPTION_KEY, event);
  	    contentValues.put(EVENT_TIME_KEY, date.getTime());
  	    contentValues.put(SHORT_DESC_KEY, shortDesc);
  	    return db.insert(EVENT_TABLE, null, contentValues);
  }
  
  public long addEvent(String event, String shortDesc)
  {
	Date d = new Date();
	return addEvent(event, shortDesc, d);
  }
  

  public boolean removeEvent(Long _rowIndex) {
	  return db.delete(EVENT_TABLE, ROW_ID + "=" + _rowIndex, null) > 0;
  }

  public boolean removeAllEvents()
  {
		return db.delete(EVENT_TABLE, null, null) > 0;
  }
   
  
  public Cursor getAllEvents () {
	String orderBy = "order by " + EVENT_TIME_KEY + "Desc";
    return db.query(EVENT_TABLE, new String[] {ROW_ID, 
    											  EVENT_DESCRIPTION_KEY, 
    											  EVENT_TIME_KEY,
    											  SHORT_DESC_KEY}, 
                    null, null, null, null, orderBy);
  }


  
    
  public Cursor getEvent(long _rowIndex) {
    
    Cursor res = db.query(EVENT_TABLE, new String[] {ROW_ID, 
    		EVENT_DESCRIPTION_KEY, 
    		EVENT_TIME_KEY,
    		SHORT_DESC_KEY}, ROW_ID + " = " + _rowIndex, 
    		null, null, null, null);
    
    if(res != null){
    	res.moveToFirst();
    }
    
    return res;
  }
  
  

  private static class myDbHelper extends SQLiteOpenHelper {

    public myDbHelper(Context context, String name, CursorFactory factory, int version) {
      super(context, name, factory, version);
    }

    // Called when no database exists in disk and the helper class needs
    // to create a new one. 
    @Override
    public void onCreate(SQLiteDatabase _db) {      
      _db.execSQL(DATABASE_EVENT_CREATE);
    }

    // Called when there is a database version mismatch meaning that the version
    // of the database on disk needs to be upgraded to the current version.
    @Override
    public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
      // Log the version upgrade.
      Log.w("TaskDBAdapter", "Upgrading from version " + 
                             _oldVersion + " to " +
                             _newVersion + ", which will destroy all old data");
        
      // Upgrade the existing database to conform to the new version. Multiple 
      // previous versions can be handled by comparing _oldVersion and _newVersion
      // values.

      // The simplest case is to drop the old table and create a new one.
      _db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE + ";");
      // Create a new one.
      onCreate(_db);
    }
  }
 
  /** Dummy object to allow class to compile */

}

