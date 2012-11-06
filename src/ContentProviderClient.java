import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.fede.DroidContentProvider;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: fede
 * Date: 11/6/12
 * Time: 5:36 PM
 */
public class ContentProviderClient {
    private Context mContext;




    public ContentProviderClient getInstance(){
        return new ContentProviderClient();
    }


    public Uri addCall(String number, long time, Context c){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DroidContentProvider.CALL_NUMBER_DESC_KEY, number);
        contentValues.put(DroidContentProvider.CALL_TIME_KEY, time);

        ContentResolver cr = c.getContentResolver();
        return cr.insert(DroidContentProvider.CALLS_URI,
                contentValues);

    }



    public int removeCall(long _rowIndex, Context c) {
        ContentResolver cr = c.getContentResolver();
        Uri rowAddress = ContentUris.withAppendedId(DroidContentProvider.CALLS_URI, _rowIndex);
        return cr.delete(rowAddress, null, null);
    }

    public int removeAllCalls(Context c) {
        ContentResolver cr = c.getContentResolver();
        return cr.delete(DroidContentProvider.CALLS_URI, null, null);
    }


    public Cursor getAllCalls () {
        String orderBy = CALL_TIME_KEY + " desc";
        return db.query(CALL_TABLE, new String[] {ROW_ID,
                CALL_NUMBER_DESC_KEY,
                CALL_TIME_KEY},
                null, null, null, null, orderBy);
    }

    // EVENTS
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
        String orderBy = EVENT_TIME_KEY + " desc";
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

}
