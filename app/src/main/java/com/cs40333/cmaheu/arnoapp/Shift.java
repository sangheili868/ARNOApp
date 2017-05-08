package com.cs40333.cmaheu.arnoapp;

/**
 * Created by chris_000 on 4/12/2017.
 */

public class Shift {
    String time;
    String day;

    public Shift()
    {}

    public Shift(String day, String time) {
        this.day = day;
        this.time = time;
    }

    public String getTime() {
        return time;
    }
    public String getDay() {
        return day;
    }
    public String getKey() {return day+"-"+time;}
    public void setTime(String time) {
        this.time = time;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public boolean equals(Shift other) {
        return other!=null &&
                this.day.equals(other.day) &&
                this.time.equals(other.time);
    }

}
