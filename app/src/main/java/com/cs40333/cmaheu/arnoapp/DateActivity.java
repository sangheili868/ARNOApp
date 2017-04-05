package com.cs40333.cmaheu.arnoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by chris_000 on 4/4/2017.
 */

public class DateActivity  extends AppCompatActivity {

    @Override
    public void onCreate(Bundle bundle) {
        int mornVolunteers=2;
        int eveVolunteers=7;
        String date="February 27";

        TextView monthDay = (TextView) findViewById(R.id.date_monthday);
        TextView mornVolText = (TextView) findViewById(R.id.date_morningvolunteers);
        TextView eveVolText = (TextView) findViewById(R.id.date_eveningvolunteers);

        monthDay.setText(date);
        mornVolText.setText(mornVolunteers + " Volunteers");
        eveVolText.setText(eveVolunteers + " Volunteers");
    }
}
