package com.cs40333.cmaheu.arnoapp;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
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
        removeExcShiftsForShift(user,shift);
    }

    public void removeShift(User user, Shift shift) {
        mydb.child("users").child(user.getFBUser().getUid()).child("shifts").child(shift.getKey()).removeValue();
        removeExcShiftsForShift(user,shift);
    }

    public void insertExcShift(User user, ExceptionShift es) {
        mydb.child("users").child(user.getFBUser().getUid()).child("excshifts").child(es.getKey()).setValue(es);
    }

    public void removeExcShift(User user, ExceptionShift es) {
        mydb.child("users").child(user.getFBUser().getUid()).child("excshifts").child(es.getKey()).removeValue();
    }

    // To use get functions:
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
        myfb.getDBRef().child("users").child(me.getFBUser().getUid()).addValueEventListener(shiftListener);
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

    // Return a vector containing the shifts this user has signed up for
    //myfb.getDBRef().child("users").child(me.getFBUser().getUid()).addValueEventListener(shiftListener);
    public Vector<Shift> getUserShifts(DataSnapshot DS) {
        Vector<Shift> myShifts=new Vector<>();
        for(DataSnapshot thisShift:DS.child("shifts").getChildren()){
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

    // Return a vector containing the exception shifts this user has signed up for
    // myfb.getDBRef().child("users").child(me.getFBUser().getUid()).addValueEventListener(shiftListener);
    public Vector<ExceptionShift> getUserExcShifts(DataSnapshot DS) {
        Vector<ExceptionShift> myExcShifts=new Vector<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        for(DataSnapshot thisShift:DS.child("excshifts").getChildren()){
            ExceptionShift myES=new ExceptionShift();
            for(DataSnapshot thisChild:thisShift.getChildren()){
                if(thisChild.getKey().equals("time"))
                    myES.setTime(String.valueOf(thisChild.getValue()));
                else if(thisChild.getKey().equals("date")) {
                        myES.setDate(thisChild.getValue(Date.class));
                }
                else if(thisChild.getKey().equals("going"))
                    myES.setGoing(Boolean.parseBoolean(String.valueOf(thisChild.getValue())));
            }
            myExcShifts.add(myES);
        }
        return myExcShifts;
    }

    // Remove all exception shifts that would overlap with a regular shift
    // To use:
    public ValueEventListener removeExcShiftsForShift(User user, final Shift shift) {
        ValueEventListener shiftListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot DS) {
                boolean rightTime;
                boolean rightDay;
                for(DataSnapshot thisShift:DS.child("excshifts").getChildren()){
                    ExceptionShift myES=new ExceptionShift();
                    rightTime=false;
                    rightDay=false;
                    for(DataSnapshot thisChild:thisShift.getChildren()){
                        if(thisChild.getKey().equals("time"))
                            rightTime=shift.getTime().equals(String.valueOf(thisChild.getValue()));
                        else if(thisChild.getKey().equals("day"))
                            rightDay=shift.getDay().equals(String.valueOf(thisChild.getValue()));
                    }
                    // The shift is a match, so remove it
                    if(rightDay && rightTime)
                        thisShift.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        mydb.child("users").child(user.getFBUser().getUid()).addListenerForSingleValueEvent(shiftListener);
        return shiftListener;
    }

    //myfb.getDBRef().child("users").addValueEventListener(shiftListener);
    public int getVolunteersforShift(DataSnapshot DS, Date mydate, String mytime) {
        int count=0;
        String thistime="",thisday="";
        Date thisdate=new Date();
        boolean going=false;
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
        for(DataSnapshot thisUser:DS.getChildren()) {
            for (DataSnapshot thisShift : thisUser.child("excshifts").getChildren()) {
                for (DataSnapshot thisChild : thisShift.getChildren()) {
                    if (thisChild.getKey().equals("time"))
                        thistime=String.valueOf(thisChild.getValue());
                    else if(thisChild.getKey().equals("date")) {
                        thisdate=thisChild.getValue(Date.class);
                    }
                    else if(thisChild.getKey().equals("going"))
                        going=Boolean.parseBoolean(String.valueOf(thisChild.getValue()));
                }
                if(thistime.equals(mytime) && thisdate.equals(mydate))
                    if(going) count++;
                    else count--;
            }
        }

        return count;
    }
}
