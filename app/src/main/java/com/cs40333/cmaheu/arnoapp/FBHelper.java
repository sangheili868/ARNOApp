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
            for (ExceptionShift es: user.getExcShifts()) this.insertExcShift(user,es);
    }

    public void insertShift(User user, Shift shift) {
        mydb.child("users").child(user.getFBUser().getUid()).child("shifts").child(shift.getKey()).setValue(shift);
    }

    public void insertExcShift(User user, ExceptionShift _es) {
        final ExceptionShift es =_es;
        final DatabaseReference myref=mydb.child("users").child(user.getFBUser().getUid());
        myref.child("shifts").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // What type is this????
                        Map<String,Shift> shiftmap = (Map<String,Shift>)dataSnapshot.getValue();
                        for (Shift shift : shiftmap.values()) {
                            if(es.getDay().equals(shift.getDay()) && es.getTime().equals(shift.getTime())) {
                                // Don't allow a user to say they are going when they are already signed up
                                if (!es.isGoing())
                                    myref.child("excshifts").child(es.getKey()).setValue(es);
                            } else {
                                // Don't allow a user to say they are not going when they are not already signed up
                                if(es.isGoing())
                                    myref.child("excshifts").child(es.getKey()).setValue(es);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );


    }
/*
    public Vector<Shift> getShiftsByID(int userID)
    {
        Vector<Shift> myShifts = new Vector<>();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myShifts.add(dataSnapshot.getValue(Shift.class));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Get Shifts Error");
            }
        };
        mydb.addValueEventListener(postListener);

        return myShifts;
    }
*/

    // Firebase Unit Tests
/*
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

        User me=new User(FirebaseAuth.getInstance().getCurrentUser());
        FBHelper myfb = new FBHelper();
        //myfb.insertUser(me);
        myfb.insertShift(me,new Shift("Monday","Evening"));
        myfb.insertShift(me,new Shift("Monday","Morning"));
        myfb.insertShift(me,new Shift("Tuesday","Evening"));
        myfb.insertExcShift(me,new ExceptionShift(makeDate(2017,5,1),"Evening",false));
        myfb.insertExcShift(me,new ExceptionShift(makeDate(2017,5,3),"Evening",true));

*/
    // END UNIT TESTS

}
