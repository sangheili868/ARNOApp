package com.cs40333.cmaheu.arnoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

/**
 * Created by chris_000 on 5/8/2017.
 */

public class VolunteerListActivity extends AppCompatActivity  implements View.OnClickListener{
    ValueEventListener mShiftListener;
    FBHelper myfb;
    FirebaseUser me;
    Date mydate;
    @Override
    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setContentView(R.layout.activity_volunteer_list);
        me= FirebaseAuth.getInstance().getCurrentUser();
        myfb = new FBHelper();

        mydate = new Date();
        mydate.setTime(getIntent().getLongExtra("date", -1));
        final String mytime = getIntent().getStringExtra("time");

        String dateText=new SimpleDateFormat("MMMM dd", Locale.US).format(mydate);
        TextView monthDay = (TextView) findViewById(R.id.date_monthday);
        monthDay.setText(dateText);

        ValueEventListener shiftListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot DS) {
                ArrayList<String> names = myfb.getUsers(DS,mydate,mytime);
                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_volunteer,names);
                ListView listView = (ListView) findViewById(R.id.list_volunteers);
                listView.setAdapter(itemsAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        myfb.getDBRef().child("users").addValueEventListener(shiftListener);
        mShiftListener=shiftListener;
        findViewById(R.id.date_backbutton).setOnClickListener(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        // Remove event listeners
        if (mShiftListener != null) {
            myfb.getDBRef().removeEventListener(mShiftListener);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.date_backbutton) {
            // Return to Date ACtivity
            Intent intent = new Intent(VolunteerListActivity.this, LeadDateActivity.class);
            intent.putExtra("date", mydate.getTime());
            startActivity(intent);
        }
    }
}
