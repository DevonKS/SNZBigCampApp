package model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Devon on 4/12/2015.
 */
public class EventSQLHelper extends SQLiteOpenHelper {

    public static final String TABLE_EVENT = "event";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "event_name";
    public static final String COLUMN_DETAIL = "event_detail";
    public static final String COLUMN_DESCRIPTION = "event_description";
    public static final String COLUMN_STARTYEAR = "event_startyear";
    public static final String COLUMN_STARTMONTH = "event_startmonth";
    public static final String COLUMN_STARTDAY = "event_startday";
    public static final String COLUMN_STARTHOUR = "event_starthour";
    public static final String COLUMN_STARTMINUTE = "event_startminute";
    public static final String COLUMN_ENDYEAR = "event_endyear";
    public static final String COLUMN_ENDMONTH = "event_endmonth";
    public static final String COLUMN_ENDDAY = "event_endday";
    public static final String COLUMN_ENDHOUR = "event_endhour";
    public static final String COLUMN_ENDMINUTE = "event_endminute";
    public static final String COLUMN_COLOR = "event_color";

    public static final String DATABASE_NAME = "bigcamp.db";
    public static final int DATABASE_VERSION = 1;

    private static String DATABASE_CREATE = "create table " + TABLE_EVENT
            + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + '`' + COLUMN_NAME + '`' + " TEXT NOT NULL, "
            + '`' + COLUMN_DETAIL + '`' + " TEXT, "
            + '`' + COLUMN_DESCRIPTION + '`' + "TEXT, "
            + '`' + COLUMN_STARTYEAR + '`' + "INTEGER NOT NULL, "
            + '`' + COLUMN_STARTMONTH + '`' + "INTEGER NOT NULL, "
            + '`' + COLUMN_STARTDAY + '`' + "INTEGER NOT NULL, "
            + '`' + COLUMN_STARTHOUR + '`' + "INTEGER NOT NULL, "
            + '`' + COLUMN_STARTMINUTE + '`' + "INTEGER NOT NULL, "
            + '`' + COLUMN_ENDYEAR + '`' + "INTEGER NOT NULL, "
            + '`' + COLUMN_ENDMONTH + '`' + "INTEGER NOT NULL, "
            + '`' + COLUMN_ENDDAY + '`' + "INTEGER NOT NULL, "
            + '`' + COLUMN_ENDHOUR + '`' + "INTEGER NOT NULL, "
            + '`' + COLUMN_ENDMINUTE + '`' + "INTEGER NOT NULL, "
            + '`' + COLUMN_COLOR + '`' + "INTEGER NOT NULL);"; //TODO color may be null need to check

    public EventSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(EventSQLHelper.class.getName(), "Upgrading database from version " + oldVersion
                + " to " + newVersion + ", which will destroy old data");
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_EVENT);
        onCreate(db);
    }
}
