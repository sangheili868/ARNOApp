package com.cs40333.cmaheu.arnoapp;

import java.util.Date;

/**
 * Created by JoeS on 5/9/2017.
 */

public class Need {
    Date day;
    String time;
    int volunteers;

    public Need(Date day, String time, int volunteers){
        this.day = day;
        this.time = time;
        this.volunteers = volunteers;
    }

    public Date getDay(){return day;}
    public String getTime(){return time;}
    public int getVolunteers(){return volunteers;}
    public void setDay(Date day){this.day = day;}
    public void setTime(String time){this.time = time;}
    public void setVolunteers(int volunteers){this.volunteers = volunteers;}
}
