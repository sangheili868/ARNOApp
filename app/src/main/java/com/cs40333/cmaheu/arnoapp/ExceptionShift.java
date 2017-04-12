package com.cs40333.cmaheu.arnoapp;

import java.util.Date;

/**
 * Created by chris_000 on 4/12/2017.
 */

public class ExceptionShift extends Shift {
    Date date;
    boolean going;


    public ExceptionShift(Date date, String time, int userID, boolean going) {
        this.date = date;
        this.time = time;
        this.userID = userID;
        this.going  = going;
    }

    public Date getDate() {
        return date;
    }
    public boolean isGoing() {
        return going;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setGoing(boolean going) {
        this.going = going;
    }

}
