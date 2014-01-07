package com.example.bettery_info;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class InfoSaveLoader {
	public static long insertInfo(Context context, int percent, int volt, int current, int temp, int time){
		DBHelper mDbHelper = new DBHelper(context);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBHelper.PERCENT, percent);
		values.put(DBHelper.VOLT, volt);
		values.put(DBHelper.CURRENT, current);
		values.put(DBHelper.TEMP, temp);
		values.put(DBHelper.TIME, time);
		long newRowId;
		newRowId = db.insert(
		         DBHelper.TABLE_NAME,
		         null,
		         values);
		return newRowId;
	}
	public static Cursor loadInfo(Context context,int how){
		DBHelper mDbHelper = new DBHelper(context);
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
		    DBHelper.INFO_ID,
		    DBHelper.PERCENT,
		    DBHelper.VOLT,
		    DBHelper.CURRENT,
		    DBHelper.TEMP,
		    DBHelper.TIME
		    };
		String sortOrder =
			    DBHelper.TIME + " DESC";
		
		Cursor c = db.query(
			    DBHelper.TABLE_NAME,  // The table to query
			    projection,                               // The columns to return
			    null,                                // The columns for the WHERE clause
			    null,                            // The values for the WHERE clause
			    null,                                     // don't group the rows
			    null,                                     // don't filter by row groups
			    sortOrder,                                 // The sort order
			    String.valueOf(how)
			    );
		return c;
	}
}
