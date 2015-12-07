package com.example.devon.snzbigcamp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import calendarview.CalendarEvent;
import calendarview.CalendarView;
import calendarview.DateTimeInterpreter;
import model.Event;
import model.EventDataSource;

public class CalendarActivity extends AppCompatActivity {

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private CalendarView mCalendarView;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Get a reference for the week view in the layout.
        mCalendarView = (CalendarView) findViewById(R.id.calender);
        mCalendarView.goToHour(9.0);

//        // Set an action when any event is clicked.
//        mWeekView.setOnEventClickListener(mEventClickListener);
//
//        // The week view has infinite scrolling horizontally. We have to provide the events of a
//        // month every time the month changes on the week view.
        mCalendarView.setMonthChangeListener(new CalendarView.MonthChangeListener() {
            @Override
            public List<CalendarEvent> onMonthChange(int newYear, int newMonth) {
                List<Event> events = null;
                try {
                    events = new GetMonthEventsTask(CalendarActivity.this).execute(newYear, newMonth).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                List<CalendarEvent> calendarEvents = convertEventsToCalendarEvents(events);

                return calendarEvents;
            }
        });
//
//        // Set long press listener for events.
//        mWeekView.setEventLongPressListener(mEventLongPressListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id){
            case R.id.action_today:
                mCalendarView.goToToday();
                return true;
            case R.id.action_refresh:
                //TODO get events from the cloud then refresh the view
                new GetEventsTask(this, mCalendarView).execute();
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    double hour = mCalendarView.getFirstVisibleHour();
                    mWeekViewType = TYPE_DAY_VIEW;
                    mCalendarView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mCalendarView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mCalendarView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mCalendarView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mCalendarView.goToHour(hour);
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    double hour = mCalendarView.getFirstVisibleHour();
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mCalendarView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mCalendarView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mCalendarView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mCalendarView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mCalendarView.goToHour(hour);
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    double hour = mCalendarView.getFirstVisibleHour();
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mCalendarView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mCalendarView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mCalendarView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mCalendarView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mCalendarView.goToHour(hour);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<CalendarEvent> convertEventsToCalendarEvents(List<Event> events) {
        List<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();
        if (events == null) return calendarEvents;

        for (Event event : events) {
            CalendarEvent calendarEvent = convertEventToCalendarEvent(event);
            calendarEvents.add(calendarEvent);
        }

        return calendarEvents;
    }

    private CalendarEvent convertEventToCalendarEvent(Event event) {
        CalendarEvent calendarEvent = new CalendarEvent(event.getId(), event.getName(),
                event.getDetail(), event.getDescription(), event.getStartYear(),
                event.getStartMonth(), event.getStartDay(), event.getStartHour(),
                event.getStartMinute(), event.getEndYear(), event.getEndMonth(),
                event.getEndDay(), event.getEndHour(), event.getEndMinute());
        calendarEvent.setColor(event.getColor());
        return calendarEvent;
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mCalendarView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" d/M", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                String amPm;
                if (hour >= 0 && hour < 12) amPm = "AM";
                else amPm = "PM";
                if (hour == 0) hour = 12;
                if (hour > 12) hour -= 12;
                return String.format("%02d %s", hour, amPm);
            }
        });
    }

    private class GetEventsTask extends AsyncTask<URL, Void, Void> {
        private final CalendarActivity calendarActivity;
        private final CalendarView calendarView;

        public GetEventsTask(CalendarActivity calendarActivity, CalendarView calendarView) {
            this.calendarActivity = calendarActivity;
            this.calendarView = calendarView;
        }

        @Override
        protected Void doInBackground(URL... urls) {
            EventDataSource eventDataSource = new EventDataSource(calendarActivity);
            try {
                eventDataSource.open();
                eventDataSource.createEvent("Night Meeting", "Teen Tent", "Speaker: Bob Hope.", 2015, 12, 9, 10, 0, 2015, 12, 9, 11, 0, R.color.event_color_03);
                eventDataSource.createEvent("Bowling", "At the buses", "Buses leave at 1:45pm", 2015, 12, 9, 14, 0, 2015, 12, 9, 17, 0, R.color.event_color_03);
                eventDataSource.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            calendarView.notifyDatasetChanged();
        }
    }

    private class GetMonthEventsTask extends AsyncTask<Integer, Void, List<Event>> {
        private final CalendarActivity calendarActivity;
        private List<Event> events;

        public GetMonthEventsTask(CalendarActivity calendarActivity) {
            this.calendarActivity = calendarActivity;
        }

        @Override
        protected List<Event> doInBackground(Integer... integers) {
            List<Event> events = new ArrayList<Event>();
            EventDataSource eventDataSource = new EventDataSource(calendarActivity);
            try {
                eventDataSource.open();
                events = eventDataSource.getMonthEvents(integers[0], integers[1]);
                eventDataSource.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return events;
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            this.events = events;
        }

        public List<Event> getEvents() {
            return events;
        }
    }
}
