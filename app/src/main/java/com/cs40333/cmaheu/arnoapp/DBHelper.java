package com.cs40333.cmaheu.arnoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chris_000 on 4/12/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public SQLiteDatabase db;
    public static String DATABASE_NAME = "arno.db";
    public static int DATABASE_VERSION = 1;
    public static String TABLE_USERS = "Users";
    public static String TABLE_REG_SHIFTS = "RegShifts";
    public static String TABLE_EXC_SHIFTS = "ExcShifts";
    public static String COL_USER_ID = "user_ID";
    public static String COL_USERNAME = "username";
    public static String COL_PASSWORD = "password";
    public static String COL_DAY = "day";
    public static String COL_TIME = "time";
    public static String COL_DATE = "date";
    public static String COL_GOING = "going";


    public DBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " ( " +
                COL_USERNAME + " TEXT," +
                COL_PASSWORD + " TEXT," +
                COL_USER_ID + " INTEGER PRIMARY KEY )");
        db.execSQL("CREATE TABLE " + TABLE_REG_SHIFTS + " ( " +
                COL_DAY + " TEXT," +
                COL_TIME + " TEXT," +
                COL_USER_ID + " INTEGER PRIMARY KEY )");
        db.execSQL("CREATE TABLE " + TABLE_EXC_SHIFTS + " ( " +
                COL_DATE + " DATE," +
                COL_TIME + " TEXT," +
                COL_GOING + " BOOLEAN," +
                COL_USER_ID + " INTEGER PRIMARY KEY )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE if exists " + TABLE_USERS );
        db.execSQL("DROP TABLE if exists " + TABLE_REG_SHIFTS );
        db.execSQL("DROP TABLE if exists " + TABLE_EXC_SHIFTS );
        onCreate(db);
    }

    public void insertUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USER_ID, user.getUserID());
        contentValues.put(COL_USERNAME, user.getUsername());
        contentValues.put(COL_PASSWORD, user.getPassword());
        long ret = db.insert(TABLE_USERS, null, contentValues );

        for (RegularShift rs: user.getRegShifts()) this.insertRegShift(rs);
        for (ExceptionShift es: user.getExcShifts()) this.insertExcShift(es);

        if (ret > -1) {
            System.out.println("Successfully inserted");
        } else {
            System.out.println("Insert Unsuccessful");
        }

        db.close();
    }

    public void insertRegShift(RegularShift rs) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USER_ID, rs.getUserID());
        contentValues.put(COL_DAY, rs.getDay());
        contentValues.put(COL_TIME, rs.getTime());
        long ret = db.insert(TABLE_REG_SHIFTS, null, contentValues );

        if (ret > -1) {
            System.out.println("Successfully inserted");
        } else {
            System.out.println("Insert Unsuccessful");
        }

        // delete exception shifts that are the same day

        db.close();
    }

    public void insertExcShift(ExceptionShift es) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USER_ID, es.getUserID());
        contentValues.put(COL_DATE, es.getDate().toString());
        contentValues.put(COL_TIME, es.getTime());
        contentValues.put(COL_GOING, es.isGoing());
        long ret = db.insert(TABLE_EXC_SHIFTS, null, contentValues );

        if (ret > -1) {
            System.out.println("Successfully inserted");
        } else {
            System.out.println("Insert Unsuccessful");
        }

        db.close();
    }

    public void deleteUser(int userID) {
        db = getWritableDatabase();
        db.delete(TABLE_USERS, " " + COL_USER_ID + " = ?", new String[]{Integer.toString(userID)});
        db.delete(TABLE_REG_SHIFTS, " " + COL_USER_ID + " = ?", new String[]{Integer.toString(userID)});
        db.delete(TABLE_EXC_SHIFTS, " " + COL_USER_ID + " = ?", new String[]{Integer.toString(userID)});
        db.close();
    }

    public void deleteRegShift(RegularShift rs) {
        db = getWritableDatabase();
        db.delete(TABLE_REG_SHIFTS, " " + COL_USER_ID + " = ? and "+
                COL_DAY+" = ? and "+COL_TIME+" = ?", new String[]{
                        Integer.toString(rs.getUserID()),
                        rs.getDay(), rs.getTime()
        });
        db.close();

        // delete exception shifts that are the same day
    }

    public void deleteExcShift(ExceptionShift es) {
        db = getWritableDatabase();
        db.delete(TABLE_EXC_SHIFTS, " " + COL_USER_ID + " = ? and "+
                COL_DATE+" = ? and "+COL_TIME+" = ?", new String[]{
                        Integer.toString(es.getUserID()),
                        es.getDate().toString(), es.getTime()
                });
        db.close();
    }


    // TODO: Add get functions

}
