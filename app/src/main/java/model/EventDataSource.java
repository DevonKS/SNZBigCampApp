package model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devon on 4/12/2015.
 */
public class EventDataSource {

    private SQLiteDatabase database;
    private EventSQLHelper eventSQLHelper;
    private String[] allColumns = {EventSQLHelper.COLUMN_ID, EventSQLHelper.COLUMN_NAME,
            EventSQLHelper.COLUMN_DETAIL, EventSQLHelper.COLUMN_DESCRIPTION,
            EventSQLHelper.COLUMN_STARTYEAR, EventSQLHelper.COLUMN_STARTMONTH,
            EventSQLHelper.COLUMN_STARTDAY, EventSQLHelper.COLUMN_STARTHOUR,
            EventSQLHelper.COLUMN_STARTMINUTE, EventSQLHelper.COLUMN_ENDYEAR,
            EventSQLHelper.COLUMN_ENDMONTH, EventSQLHelper.COLUMN_ENDDAY,
            EventSQLHelper.COLUMN_ENDHOUR, EventSQLHelper.COLUMN_ENDMINUTE, EventSQLHelper.COLUMN_COLOR};

    public EventDataSource(Context context) {
        this.eventSQLHelper = new EventSQLHelper(context);
    }

    public void open() throws SQLException {
        database = eventSQLHelper.getWritableDatabase();
    }

    public void close() throws SQLException {
        eventSQLHelper.close();
    }

    public Event createEvent(String name, String detail, String description,
                             int startYear, int startMonth, int startDay, int startHour, int startMinute,
                             int endYear, int endMonth, int endDay, int endHour, int endMinute, int color) {
        ContentValues values = new ContentValues();
        values.put(EventSQLHelper.COLUMN_NAME, name);
        values.put(EventSQLHelper.COLUMN_DETAIL, detail);
        values.put(EventSQLHelper.COLUMN_DESCRIPTION, description);
        values.put(EventSQLHelper.COLUMN_STARTYEAR, startYear);
        values.put(EventSQLHelper.COLUMN_STARTMONTH, startMonth);
        values.put(EventSQLHelper.COLUMN_STARTDAY, startDay);
        values.put(EventSQLHelper.COLUMN_STARTHOUR, startHour);
        values.put(EventSQLHelper.COLUMN_STARTMINUTE, startMinute);
        values.put(EventSQLHelper.COLUMN_ENDYEAR, endYear);
        values.put(EventSQLHelper.COLUMN_ENDMONTH, endMonth);
        values.put(EventSQLHelper.COLUMN_ENDDAY, endDay);
        values.put(EventSQLHelper.COLUMN_ENDHOUR, endHour);
        values.put(EventSQLHelper.COLUMN_ENDMINUTE, endMinute);
        values.put(EventSQLHelper.COLUMN_COLOR, color);

        long insertId = database.insert(EventSQLHelper.TABLE_EVENT, null, values);
        Cursor cursor = database.query(EventSQLHelper.TABLE_EVENT, allColumns, EventSQLHelper.COLUMN_ID + "=" + insertId, null, null, null, null);
        cursor.moveToFirst();
        Event newEvent = cursorToEvent(cursor);
        cursor.close();
        return newEvent;
    }

    public void removeEvent(Event event) {
        long id = event.getId();
        database.delete(EventSQLHelper.TABLE_EVENT, EventSQLHelper.COLUMN_ID + "=" + id, null);
    }

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<Event>();

        String query = "SELECT * FROM " + EventSQLHelper.TABLE_EVENT;

        Cursor cursor = database.rawQuery(query, null);

        Event event = null;
        if (cursor.moveToFirst()) {
            do {
                event = new Event();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setName(cursor.getString(1));
                event.setDetail(cursor.getString(2));
                event.setDescription(cursor.getString(3));
                event.setStartYear(Integer.parseInt(cursor.getString(4)));
                event.setStartMonth(Integer.parseInt(cursor.getString(5)));
                event.setStartDay(Integer.parseInt(cursor.getString(6)));
                event.setStartHour(Integer.parseInt(cursor.getString(7)));
                event.setStartMinute(Integer.parseInt(cursor.getString(8)));
                event.setEndYear(Integer.parseInt(cursor.getString(9)));
                event.setEndMonth(Integer.parseInt(cursor.getString(10)));
                event.setEndDay(Integer.parseInt(cursor.getString(11)));
                event.setEndHour(Integer.parseInt(cursor.getString(12)));
                event.setEndMinute(Integer.parseInt(cursor.getString(13)));
                event.setColor(Integer.parseInt(cursor.getString(14)));
                events.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return events;
    }

    public List<Event> getMonthEvents(int year, int month) {
        List<Event> events = new ArrayList<Event>();

        String query = "SELECT * FROM " + EventSQLHelper.TABLE_EVENT
                + " WHERE " + EventSQLHelper.COLUMN_STARTYEAR + " = " + year + " AND "
                + EventSQLHelper.COLUMN_STARTMONTH + " = " + month;

        Cursor cursor = database.rawQuery(query, null);

        Event event = null;
        if (cursor.moveToFirst()) {
            do {
                event = new Event();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setName(cursor.getString(1));
                event.setDetail(cursor.getString(2));
                event.setDescription(cursor.getString(3));
                event.setStartYear(Integer.parseInt(cursor.getString(4)));
                event.setStartMonth(Integer.parseInt(cursor.getString(5)));
                event.setStartDay(Integer.parseInt(cursor.getString(6)));
                event.setStartHour(Integer.parseInt(cursor.getString(7)));
                event.setStartMinute(Integer.parseInt(cursor.getString(8)));
                event.setEndYear(Integer.parseInt(cursor.getString(9)));
                event.setEndMonth(Integer.parseInt(cursor.getString(10)));
                event.setEndDay(Integer.parseInt(cursor.getString(11)));
                event.setEndHour(Integer.parseInt(cursor.getString(12)));
                event.setEndMinute(Integer.parseInt(cursor.getString(13)));
                event.setColor(Integer.parseInt(cursor.getString(14)));
                events.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return events;
    }

    private Event cursorToEvent(Cursor cursor) {
        Event event = new Event();
        event.setId(cursor.getLong(0));
        event.setName(cursor.getString(1));
        event.setDetail(cursor.getString(2));
        event.setDescription(cursor.getString(3));
        event.setStartYear(cursor.getInt(4));
        event.setStartMonth(cursor.getInt(5));
        event.setStartDay(cursor.getInt(6));
        event.setStartHour(cursor.getInt(7));
        event.setStartMinute(cursor.getInt(8));
        event.setEndYear(cursor.getInt(9));
        event.setEndMonth(cursor.getInt(10));
        event.setEndDay(cursor.getInt(11));
        event.setEndHour(cursor.getInt(12));
        event.setEndMinute(cursor.getInt(13));
        event.setColor(cursor.getInt(14));
        return event;
    }
}