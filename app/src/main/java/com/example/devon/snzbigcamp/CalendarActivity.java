package com.example.devon.snzbigcamp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import calendarview.CalendarEvent;
import calendarview.CalendarView;
import calendarview.DateTimeInterpreter;

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
                List<CalendarEvent> events = new ArrayList<>();
                CalendarEvent event = new CalendarEvent(1, "Night Meeting", "Teen Tent", "Speaker: Bob Hope.", newYear, newMonth, 1, 10, 0, newYear, newMonth, 1, 11, 0);
                event.setColor(getResources().getColor(R.color.event_color_03));
                events.add(event);

                return events;
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
}
