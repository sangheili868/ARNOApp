package com.cs40333.cmaheu.arnoapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by chris_000 on 4/12/2017.
 */

public class ExceptionShift extends Shift {
    private Date date;
    private boolean going;

    public ExceptionShift() {};
    public ExceptionShift(Date date, String time, boolean going) {
        super((new SimpleDateFormat("EEEE", Locale.US)).format(date),time);
        this.date = date;
        this.going  = going;
    }
    public boolean equals(ExceptionShift other) {
        return other!=null &&
                (this.going == other.going) &&
                this.date.equals(other.date) &&
                this.time.equals(other.time);
    }

    public Date getDate() {
        return date;
    }
    public boolean isGoing() {
        return going;
    }
    @Override
    public String getKey() {return date.toString()+"-"+time+"-"+going;};
    public void setDate(Date date) {
        this.date = date;
        this.day = (new SimpleDateFormat("EEEE", Locale.US)).format(date);
    }
    public void setGoing(boolean going) {
        this.going = going;
    }

}
