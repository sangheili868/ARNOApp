package com.cs40333.cmaheu.arnoapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Created by chris_000 on 4/12/2017.
 */

public class User {

    private int userID;
    private String username;
    private String password;
    private Vector<RegularShift> regShifts;
    private Vector<ExceptionShift> excShifts;

    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    public void addRegShift(RegularShift rs)
    {
        regShifts.add(rs);
    }
    public boolean deleteRegShift(RegularShift rs) {
        return regShifts.remove(rs);
    }
    public Vector<RegularShift> getRegShifts() {return regShifts;}
    public void clearRegShifts() {regShifts.clear();}

    public void addExcShift(ExceptionShift es)
    {
        excShifts.add(es);
    }
    public boolean deleteExcShift(ExceptionShift es) {
        return excShifts.remove(es);
    }
    public Vector<ExceptionShift> getExcShifts() {return excShifts;}
    public void clearExcShifts() {excShifts.clear();}

    // Returns true or false if the user is signed up to work on a given day and time
    public boolean isWorking(Date date, String time)
    {
        String day = (new SimpleDateFormat("EEEE")).format(date); // the day of the week spelled out completely
        for (ExceptionShift es: this.excShifts)
            if(es.getDate().equals(date) && es.getTime().equals(time))
                return es.isGoing();
        for (RegularShift rs: this.regShifts)
            if(rs.getDay().equals(day) && rs.getTime().equals(time)) return true;
        return false;
    }
    public boolean checkPassword(String attemptpw) {
        return this.password.equals(attemptpw);
    }

    public int getUserID() {
        return userID;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {return password; }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }


}
