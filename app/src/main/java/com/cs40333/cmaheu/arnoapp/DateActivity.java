package com.cs40333.cmaheu.arnoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

/**
 * Created by chris_000 on 4/4/2017.
 */


public class DateActivity  extends AppCompatActivity implements View.OnClickListener{
    FBHelper myfb;
    FirebaseUser me;
    Date thisDate;
    private ValueEventListener mShiftListener;
    private ValueEventListener mUserListener;

    public int getBGColor(int numVols) {

        if(numVols< 4)
            return ResourcesCompat.getColor(getResources(),R.color.bad,null);
        else if (numVols <6)
            return ResourcesCompat.getColor(getResources(),R.color.okay,null);
        else
            return ResourcesCompat.getColor(getResources(),R.color.good,null);
    }

    @Override
    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setContentView(R.layout.activity_date);

        me=FirebaseAuth.getInstance().getCurrentUser();
        myfb = new FBHelper();
        thisDate = new Date();
        thisDate.setTime(getIntent().getLongExtra("date", -1));
        String dateText=new SimpleDateFormat("MMMM dd", Locale.US).format(thisDate);
        TextView monthDay = (TextView) findViewById(R.id.date_monthday);
        monthDay.setText(dateText);

        // Change the colors based on numbers of volunteers
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot DS) {
                // Count the morning shifts
                int mornVolunteers=myfb.getVolunteersforShift(DS, thisDate, "Morning");
                TextView mornVolText = (TextView) findViewById(R.id.date_morningvolunteers);
                LinearLayout mornLay = (LinearLayout) findViewById(R.id.date_morninglayout);
                mornVolText.setText(mornVolunteers + " Volunteers");
                mornLay.setBackgroundColor(getBGColor(mornVolunteers));

                // Count the evening Shifts
                int eveVolunteers=myfb.getVolunteersforShift(DS, thisDate, "Evening");
                TextView eveVolText = (TextView) findViewById(R.id.date_eveningvolunteers);
                LinearLayout eveLay = (LinearLayout) findViewById(R.id.date_eveninglayout);
                eveVolText.setText(eveVolunteers + " Volunteers");
                eveLay.setBackgroundColor(getBGColor(eveVolunteers));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        myfb.getDBRef().child("users").addValueEventListener(userListener);
        mUserListener=userListener;

        // Add value event listener to see if the user has signed up
        ValueEventListener shiftListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot DS) {
                // Get the user's shifts and exception shifts
                Vector<Shift> myShifts=myfb.getUserShifts(DS);
                Vector<ExceptionShift> myExcShifts=myfb.getUserExcShifts(DS);
                // Get buttons and images
                Button everymornbutton = (Button) findViewById(R.id.date_everymorningbutton);
                Button everyevebutton = (Button) findViewById(R.id.date_everyeveningbutton);
                Button thismornbutton = (Button) findViewById(R.id.date_thismorningbutton);
                Button thisevebutton = (Button) findViewById(R.id.date_thiseveningbutton);
                ImageView mornImage = (ImageView) findViewById(R.id.date_mornimg);
                ImageView eveImage = (ImageView) findViewById(R.id.date_eveimg);

                // Buttons default to "Sign up"
                everymornbutton.setText(getResources().getText(R.string.signup_everyweek));
                everyevebutton.setText(getResources().getText(R.string.signup_everyweek));
                thismornbutton.setText(getResources().getText(R.string.signup_thisday));
                thisevebutton.setText(getResources().getText(R.string.signup_thisday));
                mornImage.setBackgroundResource(android.R.drawable.checkbox_off_background);
                eveImage.setBackgroundResource(android.R.drawable.checkbox_off_background);
                // Check if they are opted in for this day
                for(ExceptionShift myExcShift:myExcShifts) {
                    if (myExcShift.equals(new ExceptionShift(thisDate, "Morning", true))) {
                        // User is opted into of this specific day
                        thismornbutton.setText(getResources().getText(R.string.signout_thisday));
                        mornImage.setBackgroundResource(android.R.drawable.checkbox_on_background);
                    }
                    if (myExcShift.equals(new ExceptionShift(thisDate, "Evening", true))) {
                        // User is opted into of this specific day
                        thisevebutton.setText(getResources().getText(R.string.signout_thisday));
                        eveImage.setBackgroundResource(android.R.drawable.checkbox_on_background);
                    }
                }
                // Check if user is signed up for this morning or evening shift regularly
                for(Shift myShift:myShifts)
                {
                    // User is signed up for morning shift every week
                    if(myShift.equals(new Shift(myfb.getDayOfWeek(thisDate),"Morning"))) {
                        everymornbutton.setText(getResources().getText(R.string.signout_everyweek));
                        thismornbutton.setText(getResources().getText(R.string.signout_thisday));
                        mornImage.setBackgroundResource(android.R.drawable.checkbox_on_background);
                        for(ExceptionShift myExcShift:myExcShifts)
                            if(myExcShift.equals(new ExceptionShift(thisDate,"Morning",false))) {
                                // User is opted out of this specific day
                                thismornbutton.setText(getResources().getText(R.string.signup_thisday));
                                mornImage.setBackgroundResource(android.R.drawable.checkbox_off_background);
                            }
                    }
                    // User is signed up for the evening shift every week
                    if(myShift.equals(new Shift(myfb.getDayOfWeek(thisDate),"Evening"))) {
                        everyevebutton.setText(getResources().getText(R.string.signout_everyweek));
                        thisevebutton.setText(getResources().getText(R.string.signout_thisday));
                        eveImage.setBackgroundResource(android.R.drawable.checkbox_on_background);
                        for(ExceptionShift myExcShift:myExcShifts)
                            if(myExcShift.equals(new ExceptionShift(thisDate,"Evening",false))) {
                                // User is opted out of this specific day
                                thisevebutton.setText(getResources().getText(R.string.signup_thisday));
                                eveImage.setBackgroundResource(android.R.drawable.checkbox_off_background);
                            }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        myfb.getDBRef().child("users").child(me.getUid()).addValueEventListener(shiftListener);
        mShiftListener=shiftListener;


        // Buttons
        findViewById(R.id.date_backbutton).setOnClickListener(this);
        findViewById(R.id.date_everymorningbutton).setOnClickListener(this);
        findViewById(R.id.date_everyeveningbutton).setOnClickListener(this);
        findViewById(R.id.date_thismorningbutton).setOnClickListener(this);
        findViewById(R.id.date_thiseveningbutton).setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Remove event listeners
        if (mShiftListener != null) {
            myfb.getDBRef().removeEventListener(mShiftListener);
        }
        if (mUserListener != null) {
            myfb.getDBRef().removeEventListener(mUserListener);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.date_backbutton) {
            // Return to calendar
            Intent intent = new Intent(DateActivity.this, CalendarActivity.class);
            startActivity(intent);
        } else if(i == R.id.date_everymorningbutton) {
            // Either sign up for or sign out of this morning shift every week
            Button thisBtn = (Button)findViewById(i);
            if(thisBtn.getText().equals(getResources().getText(R.string.signup_everyweek)))
                myfb.insertShift(me, new Shift(myfb.getDayOfWeek(thisDate), "Morning"));
            else if(thisBtn.getText().equals(getResources().getText(R.string.signout_everyweek)))
                myfb.removeShift(me, new Shift(myfb.getDayOfWeek(thisDate), "Morning"));
        } else if(i == R.id.date_everyeveningbutton) {
            // Either sign up for or sign out of this evening shift every week
            Button thisBtn = (Button)findViewById(i);
            if(thisBtn.getText().equals(getResources().getText(R.string.signup_everyweek)))
                myfb.insertShift(me,new Shift(myfb.getDayOfWeek(thisDate),"Evening"));
            else if(thisBtn.getText().equals(getResources().getText(R.string.signout_everyweek)))
                myfb.removeShift(me,new Shift(myfb.getDayOfWeek(thisDate),"Evening"));
        } else if(i == R.id.date_thismorningbutton) {
            // Either sign up for or sign out of this morning shift just this day
            Button thisBtn = (Button)findViewById(i);
            Button everyBtn = (Button) findViewById(R.id.date_everymorningbutton);
            if(thisBtn.getText().equals(getResources().getText(R.string.signup_thisday))) {
                if (everyBtn.getText().equals(getResources().getText(R.string.signup_everyweek)))
                    myfb.insertExcShift(me, new ExceptionShift(thisDate, "Morning", true));
                else {
                    myfb.removeExcShift(me,new ExceptionShift(thisDate,"Morning",false));
                }
            }
            else if(thisBtn.getText().equals(getResources().getText(R.string.signout_thisday)))
                if (everyBtn.getText().equals(getResources().getText(R.string.signup_everyweek)))
                    myfb.removeExcShift(me, new ExceptionShift(thisDate, "Morning", true));
                else {
                    myfb.insertExcShift(me, new ExceptionShift(thisDate, "Morning", false));
                }
        } else if(i == R.id.date_thiseveningbutton) {
            // Either sign up for or sign out of this evening shift just this day
            Button thisBtn = (Button)findViewById(i);
            Button everyBtn = (Button) findViewById(R.id.date_everyeveningbutton);
            if(thisBtn.getText().equals(getResources().getText(R.string.signup_thisday))) {
                if (everyBtn.getText().equals(getResources().getText(R.string.signup_everyweek)))
                    myfb.insertExcShift(me, new ExceptionShift(thisDate, "Evening", true));
                else {
                    myfb.removeExcShift(me,new ExceptionShift(thisDate,"Evening",false));
                }
            }
            else if(thisBtn.getText().equals(getResources().getText(R.string.signout_thisday)))
                if (everyBtn.getText().equals(getResources().getText(R.string.signup_everyweek)))
                    myfb.removeExcShift(me, new ExceptionShift(thisDate, "Evening", true));
                else {
                    myfb.insertExcShift(me, new ExceptionShift(thisDate, "Evening", false));
                }
        }
    }
}
