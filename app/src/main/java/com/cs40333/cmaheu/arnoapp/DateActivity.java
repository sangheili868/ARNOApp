package com.cs40333.cmaheu.arnoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
    User me;
    Date thisDate;
    private ValueEventListener mShiftListener;
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

        me=new User(FirebaseAuth.getInstance().getCurrentUser());
        myfb = new FBHelper();
        int eveVolunteers=0;
        thisDate=makeDate(2017,2,27);
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
                // Get the user's shifts
                Vector<Shift> myShifts=myfb.getUserShifts(DS);
                // Buttons default to "Sign up"
                ((Button) findViewById(R.id.date_everymorningbutton)).setText(getResources().getText(R.string.signup_everyweek));
                ((Button) findViewById(R.id.date_everyeveningbutton)).setText(getResources().getText(R.string.signup_everyweek));
                // Check if user is signed up for this morning or eveningshift
                for(Shift myShift:myShifts)
                {
                    if(myShift.equals(new Shift(myfb.getDayOfWeek(thisDate),"Morning")))
                        ((Button) findViewById(R.id.date_everymorningbutton)).setText(getResources().getText(R.string.signout_everyweek));
                    if(myShift.equals(new Shift(myfb.getDayOfWeek(thisDate),"Evening")))
                        ((Button) findViewById(R.id.date_everyeveningbutton)).setText(getResources().getText(R.string.signout_everyweek));
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        myfb.getDBRef().child("users").child(me.getFBUser().getUid()).child("shifts").addValueEventListener(shiftListener);
        mShiftListener=shiftListener;


        // Buttons
        findViewById(R.id.date_backbutton).setOnClickListener(this);
        findViewById(R.id.date_everymorningbutton).setOnClickListener(this);
        findViewById(R.id.date_everyeveningbutton).setOnClickListener(this);
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
            // Return to Main Menu
            Intent intent = new Intent(DateActivity.this, MainMenuActivity.class);
            startActivity(intent);
        } else if(i == R.id.date_everymorningbutton) {
            // Either sign up for or sign out of this morning shift every week
            Button thisBtn = (Button)findViewById(i);
            if(thisBtn.getText().equals(getResources().getText(R.string.signup_everyweek)))
                myfb.insertShift(me,new Shift(myfb.getDayOfWeek(thisDate),"Morning"));
            else if(thisBtn.getText().equals(getResources().getText(R.string.signout_everyweek)))
                myfb.removeShift(me,new Shift(myfb.getDayOfWeek(thisDate),"Morning"));
        } else if(i == R.id.date_everyeveningbutton) {
            // Either sign up for or sign out of this evening shift every week
            Button thisBtn = (Button)findViewById(i);
            if(thisBtn.getText().equals(getResources().getText(R.string.signup_everyweek)))
                myfb.insertShift(me,new Shift(myfb.getDayOfWeek(thisDate),"Evening"));
            else if(thisBtn.getText().equals(getResources().getText(R.string.signout_everyweek)))
                myfb.removeShift(me,new Shift(myfb.getDayOfWeek(thisDate),"Evening"));
        }
    }
}
