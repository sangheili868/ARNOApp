package com.cs40333.cmaheu.arnoapp;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

/**
 * Created by chris_000 on 5/6/2017.
 */

public class FBHelper {
    private FirebaseUser me;
    private FirebaseAuth mAuth;
    private DatabaseReference mydb;

    public FBHelper() {
        mAuth = FirebaseAuth.getInstance();
        me = mAuth.getCurrentUser();
        mydb = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getDBRef() { return mydb;}

    public void clearUsers() {
        mydb.child("users").removeValue();
    }
    public String getDayOfWeek(Date mydate) {
        // Given a Date, returns the day of week
        // Ex: Given 5/1/2017, returns "Monday"
        return (new SimpleDateFormat("EEEE", Locale.US)).format(mydate);
    }

    public void insertUser(User user) {
            mydb.child("users").child(user.getFBUser().getUid()).setValue(user);
            for (Shift shift: user.getShifts()) this.insertShift(user,shift);
            //for (ExceptionShift es: user.getExcShifts()) this.insertExcShift(user,es);
    }

    public void insertShift(User user, Shift shift) {
        mydb.child("users").child(user.getFBUser().getUid()).child("shifts").child(shift.getKey()).setValue(shift);
    }

    public void removeShift(User user, Shift shift) {
        mydb.child("users").child(user.getFBUser().getUid()).child("shifts").child(shift.getKey()).removeValue();
    }

    // Return a vector containing the shifts this user has signed up for
    // To use:
    /*
        ValueEventListener shiftListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot DS) {
                // Get the user's shifts
                Vector<Shift> myShifts=myfb.getUserShifts(DS);
                // Do things with the shifts (i.e. update UI)
                ...
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        myfb.getDBRef().child("users").child(me.getFBUser().getUid()).child("shifts").addValueEventListener(shiftListener);
        mShiftListener=shiftListener;
     */
    // In that class, add this function:
    /*

    @Override
    public void onStop() {
        super.onStop();
        // Remove post value event listener
        if (mShiftListener != null) {
            myfb.getDBRef().removeEventListener(mShiftListener);
        }
    }

     */
    public Vector<Shift> getUserShifts(DataSnapshot DS) {
        Vector<Shift> myShifts=new Vector<>();
        for(DataSnapshot thisShift:DS.getChildren()){
            Shift myShift=new Shift();
            for(DataSnapshot thisChild:thisShift.getChildren()){
                if(thisChild.getKey().equals("time"))
                    myShift.setTime(String.valueOf(thisChild.getValue()));
                else if(thisChild.getKey().equals("day"))
                    myShift.setDay(String.valueOf(thisChild.getValue()));
            }
            myShifts.add(myShift);
        }
        return myShifts;
    }

    public int getVolunteersforShift(DataSnapshot DS, Date mydate, String mytime) {
        int count=0;
        String thistime="",thisday="";
        for(DataSnapshot thisUser:DS.getChildren()) {
            for (DataSnapshot thisShift : thisUser.child("shifts").getChildren()) {
                for (DataSnapshot thisChild : thisShift.getChildren()) {
                    if (thisChild.getKey().equals("time"))
                        thistime=String.valueOf(thisChild.getValue());
                    else if (thisChild.getKey().equals("day"))
                        thisday=String.valueOf(thisChild.getValue());
                }
                if(thistime.equals(mytime) && thisday.equals(getDayOfWeek(mydate)))
                    count++;
            }
        }

        return count;
    }
}
