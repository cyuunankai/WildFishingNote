package com.simple.wildfishingnote.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WildFishingDatabase.db";
    private static final int DATABASE_VERSION = 1;
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    // campaigns
    private static final String SQL_CREATE_CAMPAINGS =
            "CREATE TABLE " + WildFishingContract.Campaigns.TABLE_NAME + " (" +
                    WildFishingContract.Campaigns._ID + " INTEGER PRIMARY KEY," +
                    WildFishingContract.Campaigns.COL_NAME_START_TIME + TEXT_TYPE +  COMMA_SEP +
                    WildFishingContract.Campaigns.COL_NAME_END_TIME + TEXT_TYPE + COMMA_SEP +
                    WildFishingContract.Campaigns.COL_NAME_SUMMARY + TEXT_TYPE + COMMA_SEP +
                    WildFishingContract.Campaigns.COL_NAME_PLACE_ID + INTEGER_TYPE + 
            " )"; 
    
    // 钓位
    private static final String SQL_CREATE_PLACES =
        "CREATE TABLE " + WildFishingContract.Places.TABLE_NAME + " (" +
        		WildFishingContract.Places._ID + " INTEGER PRIMARY KEY," +
        		WildFishingContract.Places.COLUMN_NAME_TITLE + TEXT_TYPE +  COMMA_SEP +
        		WildFishingContract.Places.COLUMN_NAME_DETAIL + TEXT_TYPE + COMMA_SEP +
	            WildFishingContract.Places.COLUMN_NAME_FILE_NAME + TEXT_TYPE +  
        " )"; 
    
    // 钓点
    private static final String SQL_CREATE_POINTS =
        "CREATE TABLE " + WildFishingContract.Points.TABLE_NAME + " (" +
        		WildFishingContract.Points._ID + " INTEGER PRIMARY KEY," +
        		WildFishingContract.Points.COLUMN_NAME_PLACE_ID + INTEGER_TYPE +  COMMA_SEP +
        		WildFishingContract.Points.COLUMN_NAME_ROD_LENGTH_ID + INTEGER_TYPE +  COMMA_SEP +
        		WildFishingContract.Points.COLUMN_NAME_DEPTH + REAL_TYPE + COMMA_SEP +
	            WildFishingContract.Points.COLUMN_NAME_LURE_METHOD_ID + INTEGER_TYPE +  COMMA_SEP +
	            WildFishingContract.Points.COLUMN_NAME_BAIT_ID + INTEGER_TYPE +  
        " )";
    
    // relay_campaign_point
    private static final String SQL_CREATE_RELAY_CAMPAIGN_POINTS =
        "CREATE TABLE " + WildFishingContract.RelayCampaignPoints.TABLE_NAME + " (" +
                WildFishingContract.RelayCampaignPoints._ID + " INTEGER PRIMARY KEY," +
                WildFishingContract.RelayCampaignPoints.COLUMN_NAME_CAMPAIGN_ID + INTEGER_TYPE +  COMMA_SEP +
                WildFishingContract.RelayCampaignPoints.COLUMN_NAME_POINT_ID + INTEGER_TYPE +  
        " )";
    
    // 竿长
    private static final String SQL_CREATE_ROD_LENGTHS =
            "CREATE TABLE " + WildFishingContract.RodLengths.TABLE_NAME + " (" +
            		WildFishingContract.RodLengths._ID + " INTEGER PRIMARY KEY," +
            		WildFishingContract.RodLengths.COLUMN_NAME_NAME + TEXT_TYPE +  
            " )";
    // 打窝
    private static final String SQL_CREATE_LURE_METHODS =
            "CREATE TABLE " + WildFishingContract.LureMethods.TABLE_NAME + " (" +
            		WildFishingContract.LureMethods._ID + " INTEGER PRIMARY KEY," +
            		WildFishingContract.LureMethods.COLUMN_NAME_NAME + TEXT_TYPE +  COMMA_SEP +
            		WildFishingContract.LureMethods.COLUMN_NAME_DETAIL + TEXT_TYPE +  
            " )";
    
    // 饵料
    private static final String SQL_CREATE_BAITS =
            "CREATE TABLE " + WildFishingContract.Baits.TABLE_NAME + " (" +
            		WildFishingContract.Baits._ID + " INTEGER PRIMARY KEY," +
            		WildFishingContract.Baits.COLUMN_NAME_NAME + TEXT_TYPE +  COMMA_SEP +
            		WildFishingContract.Baits.COLUMN_NAME_DETAIL + TEXT_TYPE +  
            " )";
    
    // 活动渔获图片关系表
    private static final String SQL_CREATE_RESULTS =
            "CREATE TABLE " + WildFishingContract.RelayCampaignImageResults.TABLE_NAME + " (" +
            		WildFishingContract.RelayCampaignImageResults._ID + " INTEGER PRIMARY KEY," +
            		WildFishingContract.RelayCampaignImageResults.COLUMN_NAME_CAMPAIGN_ID + TEXT_TYPE +  COMMA_SEP +
            		WildFishingContract.RelayCampaignImageResults.COLUMN_NAME_FILE_PATH + TEXT_TYPE +  
            " )";
    
    // 活动渔获统计关系表
    private static final String SQL_CREATE_RELAY_RESULT_STATISTICS =
            "CREATE TABLE " + WildFishingContract.RelayCamapignStatisticsResults.TABLE_NAME + " (" +
            		WildFishingContract.RelayCamapignStatisticsResults._ID + " INTEGER PRIMARY KEY," +
            		WildFishingContract.RelayCamapignStatisticsResults.COLUMN_NAME_CAMPAIGN_ID + INTEGER_TYPE +  COMMA_SEP +
            		WildFishingContract.RelayCamapignStatisticsResults.COLUMN_NAME_POINT_ID + INTEGER_TYPE +  COMMA_SEP +
            		WildFishingContract.RelayCamapignStatisticsResults.COLUMN_NAME_FISH_TYPE_ID + INTEGER_TYPE +  COMMA_SEP +
            		WildFishingContract.RelayCamapignStatisticsResults.COLUMN_NAME_WEIGHT + REAL_TYPE +  COMMA_SEP +
            		WildFishingContract.RelayCamapignStatisticsResults.COLUMN_NAME_COUNT + INTEGER_TYPE +  COMMA_SEP +
            		WildFishingContract.RelayCamapignStatisticsResults.COLUMN_NAME_HOOK_FLAG + TEXT_TYPE +  
            " )";
    
    // 鱼种
    private static final String SQL_CREATE_FISH_TYPE =
            "CREATE TABLE " + WildFishingContract.FishType.TABLE_NAME + " (" +
            		WildFishingContract.FishType._ID + " INTEGER PRIMARY KEY," +
            		WildFishingContract.FishType.COLUMN_NAME_NAME + TEXT_TYPE +  
            " )";

    public MySQLiteHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
      database.execSQL(SQL_CREATE_CAMPAINGS);
      database.execSQL(SQL_CREATE_PLACES);
      database.execSQL(SQL_CREATE_POINTS);
      database.execSQL(SQL_CREATE_RELAY_CAMPAIGN_POINTS);
      database.execSQL(SQL_CREATE_ROD_LENGTHS);
      database.execSQL(SQL_CREATE_LURE_METHODS);
      database.execSQL(SQL_CREATE_BAITS);
      database.execSQL(SQL_CREATE_RESULTS);
      database.execSQL(SQL_CREATE_RELAY_RESULT_STATISTICS);
      database.execSQL(SQL_CREATE_FISH_TYPE);
      
      String initRodLengthSql = "insert into " + WildFishingContract.RodLengths.TABLE_NAME + "("+WildFishingContract.RodLengths.COLUMN_NAME_NAME +") values ('3.6')";
      database.execSQL(initRodLengthSql);
      initRodLengthSql = "insert into " + WildFishingContract.RodLengths.TABLE_NAME + "("+WildFishingContract.RodLengths.COLUMN_NAME_NAME +") values ('4.5')";
      database.execSQL(initRodLengthSql);
      
      String initLureMethodSql = "insert into " + WildFishingContract.LureMethods.TABLE_NAME + "("+WildFishingContract.LureMethods.COLUMN_NAME_NAME +") values ('苞米茬子')";
      database.execSQL(initLureMethodSql);
      initLureMethodSql = "insert into " + WildFishingContract.LureMethods.TABLE_NAME + "("+WildFishingContract.LureMethods.COLUMN_NAME_NAME +") values ('小米')";
      database.execSQL(initLureMethodSql);
      
      String initBaitSql = "insert into " + WildFishingContract.Baits.TABLE_NAME + "("+WildFishingContract.Baits.COLUMN_NAME_NAME +") values ('九一八')";
      database.execSQL(initBaitSql);
      initBaitSql = "insert into " + WildFishingContract.Baits.TABLE_NAME + "("+WildFishingContract.Baits.COLUMN_NAME_NAME +") values ('野钓7号')";
      database.execSQL(initBaitSql);
      
      String initFishTypeSql = "insert into " + WildFishingContract.FishType.TABLE_NAME + "("+WildFishingContract.FishType.COLUMN_NAME_NAME +") values ('鲤鱼')";
      database.execSQL(initFishTypeSql);
      initFishTypeSql = "insert into " + WildFishingContract.FishType.TABLE_NAME + "("+WildFishingContract.FishType.COLUMN_NAME_NAME +") values ('鲫鱼')";
      database.execSQL(initFishTypeSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      Log.w(MySQLiteHelper.class.getName(),
          "Upgrading database from version " + oldVersion + " to "
              + newVersion + ", which will destroy all old data");
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.Campaigns.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.Places.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.Points.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.RelayCampaignPoints.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.RodLengths.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.LureMethods.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.Baits.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.RelayCampaignImageResults.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.RelayCamapignStatisticsResults.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.FishType.TABLE_NAME);
      onCreate(db);
    }

  } 
