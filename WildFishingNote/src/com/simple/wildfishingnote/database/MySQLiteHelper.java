package com.simple.wildfishingnote.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "WildFishingDatabase.db";
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
    // 区域
    private static final String SQL_CREATE_AREAS =
        "CREATE TABLE " + WildFishingContract.Areas.TABLE_NAME + " (" +
                WildFishingContract.Areas._ID + " INTEGER PRIMARY KEY," +
                WildFishingContract.Areas.COLUMN_NAME_TITLE + TEXT_TYPE +  
        " )"; 
    
    // 钓位
    private static final String SQL_CREATE_PLACES =
        "CREATE TABLE " + WildFishingContract.Places.TABLE_NAME + " (" +
        		WildFishingContract.Places._ID + " INTEGER PRIMARY KEY," +
        		WildFishingContract.Places.COLUMN_NAME_AREA_ID + INTEGER_TYPE +  COMMA_SEP +
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
    
    // 天气
    private static final String SQL_CREATE_WEATHERS =
            "CREATE TABLE " + WildFishingContract.Weathers.TABLE_NAME + " (" +
                    WildFishingContract.Weathers._ID + " INTEGER PRIMARY KEY," +
                    WildFishingContract.Weathers.COLUMN_NAME_DATE + TEXT_TYPE + " UNIQUE" + COMMA_SEP +
                    WildFishingContract.Weathers.COLUMN_NAME_REGION + TEXT_TYPE + COMMA_SEP +
                    WildFishingContract.Weathers.COLUMN_NAME_MIN_TEMP_C + TEXT_TYPE + COMMA_SEP +
                    WildFishingContract.Weathers.COLUMN_NAME_MAX_TEMP_C + TEXT_TYPE + COMMA_SEP +
                    WildFishingContract.Weathers.COLUMN_NAME_SUNRISE + TEXT_TYPE + COMMA_SEP +
                    WildFishingContract.Weathers.COLUMN_NAME_SUNSET + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_WEATHERS_HOURLY =
            "CREATE TABLE " + WildFishingContract.WeathersHourly.TABLE_NAME + " (" +
                    WildFishingContract.WeathersHourly._ID + " INTEGER PRIMARY KEY," +
                    WildFishingContract.WeathersHourly.COLUMN_NAME_WEATHER_ID + TEXT_TYPE + COMMA_SEP +
                    WildFishingContract.WeathersHourly.COLUMN_NAME_TIME + TEXT_TYPE + COMMA_SEP +
                    WildFishingContract.WeathersHourly.COLUMN_NAME_TEMP_C + TEXT_TYPE + COMMA_SEP +
                    WildFishingContract.WeathersHourly.COLUMN_NAME_WIND_SPEED_KMPH + TEXT_TYPE + COMMA_SEP +
                    WildFishingContract.WeathersHourly.COLUMN_NAME_WIND_DIR_DEGREE + TEXT_TYPE + COMMA_SEP +
                    WildFishingContract.WeathersHourly.COLUMN_NAME_PRESSURE + TEXT_TYPE + COMMA_SEP +
                    WildFishingContract.WeathersHourly.COLUMN_NAME_CLOUD_COVER + TEXT_TYPE + COMMA_SEP +
                    WildFishingContract.WeathersHourly.COLUMN_NAME_WEATHER_CODE + TEXT_TYPE + COMMA_SEP + "FOREIGN KEY("
                    + WildFishingContract.WeathersHourly.COLUMN_NAME_WEATHER_ID
                    + ") REFERENCES "
                    + WildFishingContract.Weathers.TABLE_NAME
                    + "(" + WildFishingContract.Weathers._ID + ")" +
                    " )";
    
    // 备份
    private static final String SQL_CREATE_BACKUPS =
            "CREATE TABLE " + WildFishingContract.Backups.TABLE_NAME + " (" +
                    WildFishingContract.Backups._ID + " INTEGER PRIMARY KEY," +
                    WildFishingContract.Backups.COLUMN_NAME_PATH + TEXT_TYPE +  COMMA_SEP +
                    WildFishingContract.Backups.COLUMN_NAME_TIME + TEXT_TYPE +  
            " )";


    public MySQLiteHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
      database.execSQL(SQL_CREATE_CAMPAINGS);
      database.execSQL(SQL_CREATE_AREAS);
      database.execSQL(SQL_CREATE_PLACES);
      database.execSQL(SQL_CREATE_POINTS);
      database.execSQL(SQL_CREATE_RELAY_CAMPAIGN_POINTS);
      database.execSQL(SQL_CREATE_ROD_LENGTHS);
      database.execSQL(SQL_CREATE_LURE_METHODS);
      database.execSQL(SQL_CREATE_BAITS);
      database.execSQL(SQL_CREATE_RESULTS);
      database.execSQL(SQL_CREATE_RELAY_RESULT_STATISTICS);
      database.execSQL(SQL_CREATE_FISH_TYPE);
      database.execSQL(SQL_CREATE_WEATHERS);
      database.execSQL(SQL_CREATE_WEATHERS_HOURLY);
      database.execSQL(SQL_CREATE_BACKUPS);
      
      String initRodLengthSql = "insert into " + WildFishingContract.RodLengths.TABLE_NAME + "("+WildFishingContract.RodLengths.COLUMN_NAME_NAME +") values ('3.6')";
      database.execSQL(initRodLengthSql);
      initRodLengthSql = "insert into " + WildFishingContract.RodLengths.TABLE_NAME + "("+WildFishingContract.RodLengths.COLUMN_NAME_NAME +") values ('4.5')";
      database.execSQL(initRodLengthSql);
      initRodLengthSql = "insert into " + WildFishingContract.RodLengths.TABLE_NAME + "("+WildFishingContract.RodLengths.COLUMN_NAME_NAME +") values ('5.4')";
      database.execSQL(initRodLengthSql);
      initRodLengthSql = "insert into " + WildFishingContract.RodLengths.TABLE_NAME + "("+WildFishingContract.RodLengths.COLUMN_NAME_NAME +") values ('6.3')";
      database.execSQL(initRodLengthSql);
      initRodLengthSql = "insert into " + WildFishingContract.RodLengths.TABLE_NAME + "("+WildFishingContract.RodLengths.COLUMN_NAME_NAME +") values ('7.2')";
      database.execSQL(initRodLengthSql);
      
      String initLureMethodSql = "insert into " + WildFishingContract.LureMethods.TABLE_NAME + "("+WildFishingContract.LureMethods.COLUMN_NAME_NAME +") values ('老鬼诱鱼香精+酒泡苞米茬子')";
      database.execSQL(initLureMethodSql);
      initLureMethodSql = "insert into " + WildFishingContract.LureMethods.TABLE_NAME + "("+WildFishingContract.LureMethods.COLUMN_NAME_NAME +") values ('牛B颗粒(红虫蚯蚓)')";
      database.execSQL(initLureMethodSql);
      initLureMethodSql = "insert into " + WildFishingContract.LureMethods.TABLE_NAME + "("+WildFishingContract.LureMethods.COLUMN_NAME_NAME +") values ('牛B颗粒(红虫蚯蚓)+蒸玉米面+牛B鲤+曲酒泡玉米(20粒)')";
      database.execSQL(initLureMethodSql);
      
      String initBaitSql = "insert into " + WildFishingContract.Baits.TABLE_NAME + "("+WildFishingContract.Baits.COLUMN_NAME_NAME +") values ('火鲤+老鬼2#鲤+101薯香膏')";
      database.execSQL(initBaitSql);
      initBaitSql = "insert into " + WildFishingContract.Baits.TABLE_NAME + "("+WildFishingContract.Baits.COLUMN_NAME_NAME +") values ('老鬼野钓九一八')";
      database.execSQL(initBaitSql);
      initBaitSql = "insert into " + WildFishingContract.Baits.TABLE_NAME + "("+WildFishingContract.Baits.COLUMN_NAME_NAME +") values ('下钩(牛B鲤+曲酒泡玉米) 上钩(火鲤+老鬼2#鲤+101薯香膏)')";
      database.execSQL(initBaitSql);
      
      String initFishTypeSql = "insert into " + WildFishingContract.FishType.TABLE_NAME + "("+WildFishingContract.FishType.COLUMN_NAME_NAME +") values ('鲫鱼')";
      database.execSQL(initFishTypeSql);
      initFishTypeSql = "insert into " + WildFishingContract.FishType.TABLE_NAME + "("+WildFishingContract.FishType.COLUMN_NAME_NAME +") values ('鲤鱼')";
      database.execSQL(initFishTypeSql);
      initFishTypeSql = "insert into " + WildFishingContract.FishType.TABLE_NAME + "("+WildFishingContract.FishType.COLUMN_NAME_NAME +") values ('草鱼')";
      database.execSQL(initFishTypeSql);
      initFishTypeSql = "insert into " + WildFishingContract.FishType.TABLE_NAME + "("+WildFishingContract.FishType.COLUMN_NAME_NAME +") values ('青鱼')";
      database.execSQL(initFishTypeSql);
      initFishTypeSql = "insert into " + WildFishingContract.FishType.TABLE_NAME + "("+WildFishingContract.FishType.COLUMN_NAME_NAME +") values ('鲢鱼')";
      database.execSQL(initFishTypeSql);
      initFishTypeSql = "insert into " + WildFishingContract.FishType.TABLE_NAME + "("+WildFishingContract.FishType.COLUMN_NAME_NAME +") values ('白条')";
      database.execSQL(initFishTypeSql);
      initFishTypeSql = "insert into " + WildFishingContract.FishType.TABLE_NAME + "("+WildFishingContract.FishType.COLUMN_NAME_NAME +") values ('葫芦片子')";
      database.execSQL(initFishTypeSql);
      initFishTypeSql = "insert into " + WildFishingContract.FishType.TABLE_NAME + "("+WildFishingContract.FishType.COLUMN_NAME_NAME +") values ('船钉子')";
      database.execSQL(initFishTypeSql);
      initFishTypeSql = "insert into " + WildFishingContract.FishType.TABLE_NAME + "("+WildFishingContract.FishType.COLUMN_NAME_NAME +") values ('狗鱼')";
      database.execSQL(initFishTypeSql);
      initFishTypeSql = "insert into " + WildFishingContract.FishType.TABLE_NAME + "("+WildFishingContract.FishType.COLUMN_NAME_NAME +") values ('老头鱼')";
      database.execSQL(initFishTypeSql);
      initFishTypeSql = "insert into " + WildFishingContract.FishType.TABLE_NAME + "("+WildFishingContract.FishType.COLUMN_NAME_NAME +") values ('黑鱼')";
      database.execSQL(initFishTypeSql);
      initFishTypeSql = "insert into " + WildFishingContract.FishType.TABLE_NAME + "("+WildFishingContract.FishType.COLUMN_NAME_NAME +") values ('其它')";
      database.execSQL(initFishTypeSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      Log.w(MySQLiteHelper.class.getName(),
          "Upgrading database from version " + oldVersion + " to "
              + newVersion + ", which will destroy all old data");
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.Campaigns.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.Areas.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.Places.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.Points.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.RelayCampaignPoints.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.RodLengths.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.LureMethods.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.Baits.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.RelayCampaignImageResults.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.RelayCamapignStatisticsResults.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.FishType.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.Weathers.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.WeathersHourly.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.Backups.TABLE_NAME);
      onCreate(db);
    }

  } 
