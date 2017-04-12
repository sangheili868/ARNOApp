package com.cs40333.cmaheu.arnoapp;

/**
 * Created by chris_000 on 4/12/2017.
 */

public class RegularShift extends Shift {
    String day;

    public RegularShift(String day, String time, int userID) {
        this.day = day;
        this.time = time;
        this.userID = userID;
    }

    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }

}
