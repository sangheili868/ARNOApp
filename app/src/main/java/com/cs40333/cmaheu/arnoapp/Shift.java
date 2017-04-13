package com.cs40333.cmaheu.arnoapp;

/**
 * Created by chris_000 on 4/12/2017.
 */

public class Shift {
    int userID;
    String time;
    String day;


    public Shift(String day, String time, int userID) {
        this.day = day;
        this.time = time;
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }
    public String getTime() {
        return time;
    }
    public String getDay() {
        return day;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setDay(String day) {
        this.day = day;
    }

}
