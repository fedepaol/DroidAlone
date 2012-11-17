/**********************************************************************************************************************************************************************
****** AUTO GENERATED FILE BY ANDROID SQLITE HELPER SCRIPT BY FEDERICO PAOLINELLI. ANY CHANGE WILL BE WIPED OUT IF THE SCRIPT IS PROCESSED AGAIN. *******
**********************************************************************************************************************************************************************/
package com.fede;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Date;

public class DroidContentProviderClient{
    public enum EventType {FAILURE, FORWARDED_CALL, COMMAND, REPLY} ;

	// -------------- EVENT HELPERS ------------------
    public static Uri addEvent(String Description, Date Time, String ShortDesc, EventType type, Context c){
     ContentValues contentValues = new ContentValues();
       contentValues.put(DroidContentProvider.EVENT_DESCRIPTION_COLUMN, Description);
       contentValues.put(DroidContentProvider.EVENT_TIME_COLUMN, Time.getTime());
       contentValues.put(DroidContentProvider.EVENT_SHORTDESC_COLUMN, ShortDesc);
       contentValues.put(DroidContentProvider.EVENT_EVENTTYPE_COLUMN, type.ordinal());
    	ContentResolver cr = c.getContentResolver();
    	return cr.insert(DroidContentProvider.EVENT_URI, contentValues);
    }

    public static int removeEvent(long rowIndex, Context c){
       ContentResolver cr = c.getContentResolver();
        Uri rowAddress = ContentUris.withAppendedId(DroidContentProvider.EVENT_URI, rowIndex);
        return cr.delete(rowAddress, null, null);
    }

    public static int removeAllEvent(Context c){
       ContentResolver cr = c.getContentResolver();
        return cr.delete(DroidContentProvider.EVENT_URI, null, null);
    }

    public static Cursor getAllEvent(Context c){
    	ContentResolver cr = c.getContentResolver();
        String[] result_columns = new String[] {
        	DroidContentProvider.ROW_ID,
    		DroidContentProvider.EVENT_DESCRIPTION_COLUMN,
    		DroidContentProvider.EVENT_TIME_COLUMN,
    		DroidContentProvider.EVENT_SHORTDESC_COLUMN,
    		DroidContentProvider.EVENT_EVENTTYPE_COLUMN  }; 
    
        String where = null;    
        String whereArgs[] = null;
        String order = null;
    
        Cursor resultCursor = cr.query(DroidContentProvider.EVENT_URI, result_columns, where, whereArgs, order);
        return resultCursor;
    }

    public static Cursor getEvent(long rowId, Context c){
    	ContentResolver cr = c.getContentResolver();
        String[] result_columns = new String[] {
        	DroidContentProvider.ROW_ID,
    		DroidContentProvider.EVENT_DESCRIPTION_COLUMN,
    		DroidContentProvider.EVENT_TIME_COLUMN,
    		DroidContentProvider.EVENT_SHORTDESC_COLUMN,
    		DroidContentProvider.EVENT_EVENTTYPE_COLUMN  };
    
        Uri rowAddress = ContentUris.withAppendedId(DroidContentProvider.EVENT_URI, rowId);
    
        String where = null;    
        String whereArgs[] = null;
        String order = null;
    
        Cursor resultCursor = cr.query(rowAddress, result_columns, where, whereArgs, order);
        return resultCursor;
    }

    public static int updateEvent(long rowId, String Description, Date Time, String ShortDesc, long EventType, Context c){
     ContentValues contentValues = new ContentValues();
       contentValues.put(DroidContentProvider.EVENT_DESCRIPTION_COLUMN, Description);
       contentValues.put(DroidContentProvider.EVENT_TIME_COLUMN, Time.getTime());
       contentValues.put(DroidContentProvider.EVENT_SHORTDESC_COLUMN, ShortDesc);
       contentValues.put(DroidContentProvider.EVENT_EVENTTYPE_COLUMN, EventType);
    
        Uri rowURI = ContentUris.withAppendedId(DroidContentProvider.EVENT_URI, rowId); 
    
        String where = null;
        String whereArgs[] = null;
    
        ContentResolver cr = c.getContentResolver();
        int updatedRowCount = cr.update(rowURI, contentValues, where, whereArgs);
        return updatedRowCount;
    }

	// -------------- CALL HELPERS ------------------
    public static Uri addCall(String Number, Date Time, Context c){
     ContentValues contentValues = new ContentValues();
       contentValues.put(DroidContentProvider.CALL_NUMBER_COLUMN, Number);
       contentValues.put(DroidContentProvider.CALL_TIME_COLUMN, Time.getTime());
    	ContentResolver cr = c.getContentResolver();
    	return cr.insert(DroidContentProvider.CALL_URI, contentValues);
    }

    public static int removeCall(long rowIndex, Context c){
       ContentResolver cr = c.getContentResolver();
        Uri rowAddress = ContentUris.withAppendedId(DroidContentProvider.CALL_URI, rowIndex);
        return cr.delete(rowAddress, null, null);
    }

    public static int removeAllCall(Context c){
       ContentResolver cr = c.getContentResolver();
        return cr.delete(DroidContentProvider.CALL_URI, null, null);
    }

    public static Cursor getAllCall(Context c){
    	ContentResolver cr = c.getContentResolver();
        String[] result_columns = new String[] {
        	DroidContentProvider.ROW_ID,
    		DroidContentProvider.CALL_NUMBER_COLUMN,
    		DroidContentProvider.CALL_TIME_COLUMN  }; 
    
        String where = null;    
        String whereArgs[] = null;
        String order = null;
    
        Cursor resultCursor = cr.query(DroidContentProvider.CALL_URI, result_columns, where, whereArgs, order);
        return resultCursor;
    }

    public static Cursor getCall(long rowId, Context c){
    	ContentResolver cr = c.getContentResolver();
        String[] result_columns = new String[] {
        	DroidContentProvider.ROW_ID,
    		DroidContentProvider.CALL_NUMBER_COLUMN,
    		DroidContentProvider.CALL_TIME_COLUMN  };
    
        Uri rowAddress = ContentUris.withAppendedId(DroidContentProvider.CALL_URI, rowId);
    
        String where = null;    
        String whereArgs[] = null;
        String order = null;
    
        Cursor resultCursor = cr.query(rowAddress, result_columns, where, whereArgs, order);
        return resultCursor;
    }

    public static int updateCall(long rowId, String Number, Date Time, Context c){
     ContentValues contentValues = new ContentValues();
       contentValues.put(DroidContentProvider.CALL_NUMBER_COLUMN, Number);
       contentValues.put(DroidContentProvider.CALL_TIME_COLUMN, Time.getTime());
    
        Uri rowURI = ContentUris.withAppendedId(DroidContentProvider.CALL_URI, rowId); 
    
        String where = null;
        String whereArgs[] = null;
    
        ContentResolver cr = c.getContentResolver();
        int updatedRowCount = cr.update(rowURI, contentValues, where, whereArgs);
        return updatedRowCount;
    }

}