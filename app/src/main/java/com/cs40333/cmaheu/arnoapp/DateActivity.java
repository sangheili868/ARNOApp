package com.cs40333.cmaheu.arnoapp;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

/**
 * Created by chris_000 on 4/4/2017.
 */


public class DateActivity  extends AppCompatActivity {

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
    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setContentView(R.layout.activity_date);

        User me=new User(FirebaseAuth.getInstance().getCurrentUser());
        FBHelper myfb = new FBHelper();
        myfb.clearUsers();
        myfb.insertUser(me);
        myfb.insertShift(me,new Shift("Monday","Evening"));
        myfb.insertShift(me,new Shift("Sunday","Morning"));
        myfb.insertShift(me,new Shift("Sunday","Evening"));
        myfb.insertExcShift(me,new ExceptionShift(makeDate(2017,5,1),"Evening",false));
        myfb.insertExcShift(me,new ExceptionShift(makeDate(2017,5,3),"Evening",true));
        myfb.insertExcShift(me,new ExceptionShift(makeDate(2017,5,5),"Evening",false));
        myfb.insertExcShift(me,new ExceptionShift(makeDate(2017,5,7),"Evening",true));

        int mornVolunteers=2;
        int eveVolunteers=7;
        String date="February 27";

        TextView monthDay = (TextView) findViewById(R.id.date_monthday);
        TextView mornVolText = (TextView) findViewById(R.id.date_morningvolunteers);
        LinearLayout mornLay = (LinearLayout) findViewById(R.id.date_morninglayout);
        TextView eveVolText = (TextView) findViewById(R.id.date_eveningvolunteers);
        LinearLayout eveLay = (LinearLayout) findViewById(R.id.date_eveninglayout);

        monthDay.setText(date);
        mornVolText.setText(mornVolunteers + " Volunteers");
        if(mornVolunteers < 4)
            mornLay.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.bad,null));
        else if (mornVolunteers <6)
            mornLay.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.okay,null));
        else
            mornLay.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.good,null));
        eveVolText.setText(eveVolunteers + " Volunteers");
        if(eveVolunteers < 4)
            eveLay.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.bad,null));
        else if (eveVolunteers <6)
            eveLay.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.okay,null));
        else
            eveLay.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.good,null));
    }
}
