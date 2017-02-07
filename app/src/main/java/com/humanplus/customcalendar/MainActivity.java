package com.humanplus.customcalendar;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();
    }

    protected void onClickButton(View v) {
        switch(v.getId()) {
            case R.id.button:
                addCalendar();
                break;

            case R.id.button2:
                viewCalendar();
                break;
        }
    }

    private void viewCalendar() {
        String[] PROJECTION = new String[] {
                CalendarContract.Instances.EVENT_ID,
                CalendarContract.Instances.BEGIN,
                CalendarContract.Instances.TITLE
        };

        int id_index = 0, begin_index = 1, title_index = 2;

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2017, 1, 3);
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2017, 1, 20);
        long endMillis = endTime.getTimeInMillis();

        Cursor c = null;
        ContentResolver cr = getContentResolver();

        String selection = CalendarContract.Instances.EVENT_ID + " = ?";
        String[] selectionArgs = new String[] {"207"};

        // Construct the query with the desired date range.
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);

        c = cr.query(builder.build(), PROJECTION,
                null, null, null);

        while(c.moveToNext()) {
            String title = null;
            long eventID = 0;
            long beginVal = 0;

            eventID = c.getLong(id_index);
            beginVal = c.getLong(begin_index);
            title = c.getString(title_index);

            // Do something with the values.
            Log.i("Calendar : ", "Event:  " + title);
            Calendar calendar = Calendar.getInstance();

            // Get time in millis
            calendar.setTimeInMillis(beginVal);
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Log.i("Calendar : ", "Date: " + formatter.format(calendar.getTime()));
        }
    }

    // Add to Calendar
    private void addCalendar() {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2017, 2, 19, 7, 30);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2017, 3, 19, 8, 30);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)

                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, "Yoga")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym");

                // Two things below are do not need to touch.
                /*
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
                */
        startActivity(intent);
    }

    // 권한 요청 부분
    private void requestPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR))) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CALENDAR,
                                Manifest.permission.WRITE_CALENDAR},
                        12);
            }
        }
    }
}
