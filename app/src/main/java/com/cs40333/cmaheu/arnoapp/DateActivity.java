package com.cs40333.cmaheu.arnoapp;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by chris_000 on 4/4/2017.
 */

public class DateActivity  extends AppCompatActivity {

    @Override
    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setContentView(R.layout.activity_date);

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
