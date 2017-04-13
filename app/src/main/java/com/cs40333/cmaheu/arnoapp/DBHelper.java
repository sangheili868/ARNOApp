package com.cs40333.cmaheu.arnoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Created by chris_000 on 4/12/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public SQLiteDatabase db;
    public static String DATABASE_NAME = "arno.db";
    public static int DATABASE_VERSION = 1;
    public static String TABLE_USERS = "Users";
    public static String TABLE_SHIFTS = "Shifts";
    public static String TABLE_EXC_SHIFTS = "ExcShifts";
    public static String COL_USER_ID = "user_ID";
    public static String COL_USERNAME = "username";
    public static String COL_PASSWORD = "password";
    public static String COL_DAY = "day";
    public static String COL_TIME = "time";
    public static String COL_DATE = "date";
    public static String COL_GOING = "going";
    public static String COL_COUNT = "count";


    public DBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " ( " +
                COL_USERNAME + " TEXT," +
                COL_PASSWORD + " TEXT," +
                COL_USER_ID + " INTEGER PRIMARY KEY )");
        db.execSQL("CREATE TABLE " + TABLE_SHIFTS + " ( " +
                COL_DAY + " TEXT," +
                COL_TIME + " TEXT," +
                COL_USER_ID + " INTEGER PRIMARY KEY )");
        db.execSQL("CREATE TABLE " + TABLE_EXC_SHIFTS + " ( " +
                COL_DATE + " DATE," +
                COL_DAY + " DATE," +
                COL_TIME + " TEXT," +
                COL_GOING + " BOOLEAN," +
                COL_USER_ID + " INTEGER PRIMARY KEY )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE if exists " + TABLE_USERS );
        db.execSQL("DROP TABLE if exists " + TABLE_SHIFTS );
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

        for (Shift shift: user.getShifts()) this.insertShift(shift);
        for (ExceptionShift es: user.getExcShifts()) this.insertExcShift(es);

        if (ret > -1) {
            System.out.println("Successfully inserted");
        } else {
            System.out.println("Insert Unsuccessful");
        }

        db.close();
    }

    public void insertShift(Shift shift) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USER_ID, shift.getUserID());
        contentValues.put(COL_DAY, shift.getDay());
        contentValues.put(COL_TIME, shift.getTime());
        long ret = db.insert(TABLE_SHIFTS, null, contentValues );

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
        contentValues.put(COL_DAY, es.getDay());
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
        db.delete(TABLE_SHIFTS, " " + COL_USER_ID + " = ?", new String[]{Integer.toString(userID)});
        db.delete(TABLE_EXC_SHIFTS, " " + COL_USER_ID + " = ?", new String[]{Integer.toString(userID)});
        db.close();
    }

    public void deleteShift(Shift rs) {
        db = getWritableDatabase();
        db.delete(TABLE_SHIFTS, " " + COL_USER_ID + " = ? and "+
                COL_DAY+" = ? and "+COL_TIME+" = ?", new String[]{
                        Integer.toString(rs.getUserID()),
                        rs.getDay(), rs.getTime()
        });
        db.delete(TABLE_EXC_SHIFTS, " " + COL_USER_ID + " = ? and "+
                COL_DAY+" = ? and "+COL_TIME+" = ?", new String[]{
                Integer.toString(rs.getUserID()),
                rs.getDay(), rs.getTime()
        });
        db.close();
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

    public User getUser(String username, String password)
    {
        db = getReadableDatabase();


        // Get userID
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + "WHERE "+
                COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?", new String[]
                {username, password}
        );
        User myUser=null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    myUser = new User(
                            cursor.getInt(cursor.getColumnIndex(COL_USER_ID)),
                            username, password);
                } while (cursor.moveToNext());
            }
        }

        // Get regular shifts
        myUser.setShifts(getShiftsByID(myUser.getUserID()));

        // Get exception shifts
        myUser.setExcShifts(getExcShiftsByID(myUser.getUserID()));

        cursor.close();
        if(myUser == null) throw new IndexOutOfBoundsException();
        else return myUser;
    }

    public Vector<Shift> getShiftsByID(int userID)
    {
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SHIFTS + "WHERE "+
                COL_USER_ID + " = ?", new String[] {Integer.toString(userID)}
        );
        Vector<Shift> myShifts = new Vector<Shift>();

        if (cursor != null ) {
            if (cursor.moveToFirst()) {
                do {
                    myShifts.add(new Shift(
                            cursor.getString(cursor.getColumnIndex(COL_DAY)),
                            cursor.getString(cursor.getColumnIndex(COL_TIME)),
                            userID
                    ));
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        return myShifts;
    }

    public Vector<ExceptionShift> getExcShiftsByID(int userID)
    {
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXC_SHIFTS + "WHERE "+
                COL_USER_ID + " = ?", new String[] {Integer.toString(userID)}
        );
        Vector<ExceptionShift> myShifts = new Vector<ExceptionShift>();

        if (cursor != null ) {
            if (cursor.moveToFirst()) {
                do {
                    myShifts.add(new ExceptionShift(
                            new Date(cursor.getLong(cursor.getColumnIndex(COL_DATE))),
                            cursor.getString(cursor.getColumnIndex(COL_TIME)),
                            userID,
                            (cursor.getInt(cursor.getColumnIndex(COL_GOING)) > 0)
                    ));
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        return myShifts;
    }

    // Returns the number of people who will attend a given shift
    public int countShiftsForDate(Date mydate, String time)
    {
        db = getReadableDatabase();
        String day = (new SimpleDateFormat("EEEE")).format(mydate);
        int shiftCount=0;

        // Count regular volunteers
        Cursor cursor = db.rawQuery("SELECT COUNT(*) AS "+COL_COUNT+" FROM " + TABLE_SHIFTS + "WHERE "+
                COL_DAY + " = ? AND " + COL_TIME + " = ? ", new String[] {day, time}
        );
        if (cursor != null ) {
            if (cursor.moveToFirst()) {
                do {
                    shiftCount += cursor.getInt(cursor.getColumnIndex(COL_COUNT));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        // Count exception volunteers
        cursor = db.rawQuery("SELECT COUNT(*) AS "+COL_COUNT+" FROM " + TABLE_EXC_SHIFTS + "WHERE "+
                COL_DATE + " = ? AND "+COL_TIME + " = ? AND " +COL_GOING+" = 1", new String[] {mydate.toString(), time}
        );
        if (cursor != null ) {
            if (cursor.moveToFirst()) {
                do {
                    shiftCount += cursor.getInt(cursor.getColumnIndex(COL_COUNT));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        // Remove regular volunteers who opted out
        cursor = db.rawQuery("SELECT COUNT(*) AS "+COL_COUNT+" FROM " + TABLE_EXC_SHIFTS + "WHERE "+
                COL_DATE + " = ? AND "+COL_TIME + " = ? AND "+COL_GOING+" = 0", new String[] {mydate.toString(),time}
        );
        if (cursor != null ) {
            if (cursor.moveToFirst()) {
                do {
                    shiftCount -= cursor.getInt(cursor.getColumnIndex(COL_COUNT));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        return shiftCount;

    }

}
