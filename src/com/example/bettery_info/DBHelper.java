package com.example.bettery_info;
import java.util.Calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME="battery";
    private static final String BATTERY_TABLE_NAME = "battery";
    private static final String BATTERY_TABLE_CREATE =
                "CREATE TABLE " + BATTERY_TABLE_NAME + " (_id INTEGER PRIMARY KEY, percent INTEGER, volt INTEGER, current INTEGER, temp INTEGER, time DATETIME);";

	  public DBHelper(Context context, String name, CursorFactory factory, int version) {
		  super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	  
	 public void insertData(int percent, int volt, int current, int temp, Calendar time){
		 
	 }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BATTERY_TABLE_CREATE);
    }
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}
}
