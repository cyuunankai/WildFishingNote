package com.simple.wildfishingnote.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WildFishingDatabase.db";
    private static final int DATABASE_VERSION = 1;
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    // campaigns
    private static final String SQL_CREATE_CAMPAINGS =
            "CREATE TABLE " + WildFishingContract.Campaigns.TABLE_NAME + " (" +
                    WildFishingContract.Campaigns._ID + " INTEGER PRIMARY KEY," +
                    WildFishingContract.Campaigns.COL_NAME_START_TIME + TEXT_TYPE +  COMMA_SEP +
                    WildFishingContract.Campaigns.COL_NAME_END_TIME + TEXT_TYPE + COMMA_SEP +
                    WildFishingContract.Campaigns.COL_NAME_SUMMARY + TEXT_TYPE +  
            " )"; 

    public MySQLiteHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
      database.execSQL(SQL_CREATE_CAMPAINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      Log.w(MySQLiteHelper.class.getName(),
          "Upgrading database from version " + oldVersion + " to "
              + newVersion + ", which will destroy all old data");
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.Campaigns.TABLE_NAME);
      onCreate(db);
    }

  } 
