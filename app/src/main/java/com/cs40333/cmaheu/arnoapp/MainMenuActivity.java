package com.cs40333.cmaheu.arnoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

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
            Intent intent = new Intent(MainMenuActivity.this, DateActivity.class);
            startActivity(intent);
        }
        else if (i == R.id.signOutButton) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else if (i == R.id.animalButton) {
            Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        }

}
