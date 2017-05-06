package com.cs40333.cmaheu.arnoapp;

import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

/**
 * Created by chris_000 on 4/12/2017.
 */

public class User {

    private FirebaseUser fbuser;
    private Vector<Shift> shifts;
    private Vector<ExceptionShift> excShifts;

    public User(FirebaseUser _fbuser) {
        this.fbuser=_fbuser;
        this.shifts=new Vector<>();
        this.excShifts=new Vector<>();
    }

    public void addShift(Shift shift)
    {
        shifts.add(shift);
    }
    public void setShifts(Vector<Shift> shifts) {this.shifts = shifts;}
    public boolean deleteShift(Shift shift) {
        return shifts.remove(shift);
    }
    public Vector<Shift> getShifts() {return shifts;}
    public void clearShifts() {shifts.clear();}

    public void addExcShift(ExceptionShift es)
    {
        excShifts.add(es);
    }
    public void setExcShifts(Vector<ExceptionShift> es) {this.excShifts = es;}
    public boolean deleteExcShift(ExceptionShift es) {
        return excShifts.remove(es);
    }
    public Vector<ExceptionShift> getExcShifts() {return excShifts;}
    public void clearExcShifts() {excShifts.clear();}

    // Returns true or false if the user is signed up to work on a given day and time
    public boolean isWorking(Date date, String time)
    {
        String day = (new SimpleDateFormat("EEEE", Locale.US)).format(date); // the day of the week spelled out completely
        for (ExceptionShift es: this.excShifts)
            if(es.getDate().equals(date) && es.getTime().equals(time))
                return es.isGoing();
        for (Shift shift: this.shifts)
            if(shift.getDay().equals(day) && shift.getTime().equals(time)) return true;
        return false;
    }

    public FirebaseUser getFBUser() {
        return fbuser;
    }
    public void GetFBUser(FirebaseUser _fbuser) {
        this.fbuser = _fbuser;
    }


}
