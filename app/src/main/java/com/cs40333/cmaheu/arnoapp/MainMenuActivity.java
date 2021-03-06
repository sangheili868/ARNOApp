package com.cs40333.cmaheu.arnoapp;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by JoeS on 5/6/2017.
 */

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Buttons
        findViewById(R.id.calandarButton).setOnClickListener(this);
        findViewById(R.id.signOutButton).setOnClickListener(this);
        findViewById(R.id.animalButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.calandarButton) {
            final FBHelper myfb = new FBHelper();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot DS) {
                Intent intent;
                if(myfb.isLead(DS))
                    intent = new Intent(MainMenuActivity.this, CalendarActivity.class);
                else
                    intent = new Intent(MainMenuActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        myfb.getDBRef().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(userListener);
    }
        else if (i == R.id.signOutButton) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else if (i == R.id.animalButton) {
            Intent intent = new Intent(MainMenuActivity.this, AnimalActivity.class);
            startActivity(intent);
        }
    }

}
