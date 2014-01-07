package com.example.bettery_info;
import java.util.Calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class DBHelper extends SQLiteOpenHelper   implements BaseColumns{
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME="battery.db";
    public static final String TABLE_NAME = "battery";
    public static final String INFO_ID = "_id";
    public static final String PERCENT="percent";
    public static final String VOLT="volt";
    public static final String CURRENT="current";
    public static final String TEMP="temp";
    public static final String TIME="time";
    private static final String TABLE_CREATE =
                	"CREATE TABLE " + TABLE_NAME + " (" +
                	INFO_ID + " INTEGER PRIMARY KEY, " +
                	PERCENT + " INTEGER, " +
                	VOLT + " INTEGER, " + 
                	CURRENT + " INTEGER, " + 
                	TEMP + " INTEGER, " +
                	TIME + " DATETIME);";

	  public DBHelper(Context context) {
		  super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}
}
