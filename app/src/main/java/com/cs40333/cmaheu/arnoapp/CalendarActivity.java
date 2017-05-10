package com.cs40333.cmaheu.arnoapp;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by eakla on 5/9/2017.
 */

public class CalendarActivity extends AppCompatActivity{
    FBHelper myfb;
    private ValueEventListener mUserListener;

    public static Date makeDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        CalendarView calendarView=(CalendarView) findViewById(R.id.calendarview);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {

                final FBHelper myfb = new FBHelper();
                final Date mydate = makeDate(year,month+1,dayOfMonth);
                ValueEventListener userListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot DS) {
                        Intent intent;
                        if (myfb.isLead(DS))
                            intent = new Intent(CalendarActivity.this, LeadDateActivity.class);
                        else
                            intent = new Intent(CalendarActivity.this, DateActivity.class);
                        intent.putExtra("date", mydate.getTime());
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                myfb.getDBRef().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(userListener);
            }
        });

        myfb = new FBHelper();
        ValueEventListener userListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot DS) {
                ArrayList<Need> needarray = myfb.getNeedsforWeek(DS);
                NeedAdapter adapter = new NeedAdapter(CalendarActivity.this, needarray);
                ListView listView = (ListView) findViewById(R.id.needlistview);
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        myfb.getDBRef().child("users").addValueEventListener(userListener);
        mUserListener=userListener;
    }
    @Override
    public void onStop() {
        super.onStop();
        // Remove event listeners
        if (mUserListener != null) {
            myfb.getDBRef().removeEventListener(mUserListener);
        }
    }
}
