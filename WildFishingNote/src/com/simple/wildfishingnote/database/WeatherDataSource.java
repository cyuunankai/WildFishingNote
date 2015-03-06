package com.simple.wildfishingnote.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.simple.wildfishingnote.bean.Astronomy;
import com.simple.wildfishingnote.bean.Hourly;
import com.simple.wildfishingnote.bean.LocationData;
import com.simple.wildfishingnote.bean.Weather;
import com.simple.wildfishingnote.bean.WeatherAndLocation;
import com.simple.wildfishingnote.utils.BusinessUtil;

public class WeatherDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    
    private String[] weatherAllColumns = {WildFishingContract.Weathers._ID,
            WildFishingContract.Weathers.COLUMN_NAME_DATE,
            WildFishingContract.Weathers.COLUMN_NAME_MIN_TEMP_C,
            WildFishingContract.Weathers.COLUMN_NAME_MAX_TEMP_C,
            WildFishingContract.Weathers.COLUMN_NAME_SUNRISE,
            WildFishingContract.Weathers.COLUMN_NAME_SUNSET,
            WildFishingContract.Weathers.COLUMN_NAME_REGION};
    
    private String[] weatherHourlyAllColumns = {WildFishingContract.WeathersHourly._ID,
            WildFishingContract.WeathersHourly.COLUMN_NAME_CLOUD_COVER,
            WildFishingContract.WeathersHourly.COLUMN_NAME_PRESSURE,
            WildFishingContract.WeathersHourly.COLUMN_NAME_TEMP_C,
            WildFishingContract.WeathersHourly.COLUMN_NAME_TIME,
            WildFishingContract.WeathersHourly.COLUMN_NAME_WEATHER_CODE,
            WildFishingContract.WeathersHourly.COLUMN_NAME_WIND_DIR_DEGREE,
            WildFishingContract.WeathersHourly.COLUMN_NAME_WIND_SPEED_KMPH,
            WildFishingContract.WeathersHourly.COLUMN_NAME_WEATHER_ID};

    public WeatherDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addWeatherData(WeatherAndLocation wal) {
        database.beginTransaction();
        try {
            if (isNotExist(wal.getWeatherData().getDate())) {
                long weatherId = addWeather(wal);
                addWeatherHourly(wal.getWeatherData(), weatherId);
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i("", "");
        } finally {
            database.endTransaction();
        }
    }

    public long addWeather(WeatherAndLocation wal) {

        Weather weather = wal.getWeatherData();
        LocationData location = wal.getLocationData();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WildFishingContract.Weathers.COLUMN_NAME_DATE, weather.getDate());
        values.put(WildFishingContract.Weathers.COLUMN_NAME_REGION, location.getRegion());
        values.put(WildFishingContract.Weathers.COLUMN_NAME_MIN_TEMP_C, weather.getMintempC());
        values.put(WildFishingContract.Weathers.COLUMN_NAME_MAX_TEMP_C, weather.getMaxtempC());
        values.put(WildFishingContract.Weathers.COLUMN_NAME_SUNRISE, weather.getAstronomy().getSunrise());
        values.put(WildFishingContract.Weathers.COLUMN_NAME_SUNSET, weather.getAstronomy().getSunset());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = database.insert(
                WildFishingContract.Weathers.TABLE_NAME,
                null,
                values);

        return newRowId;
    }

    public void addWeatherHourly(Weather weather, long weatherId) {

        List<Hourly> hList = weather.getHourlyList();
        for (Hourly h : hList) {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(WildFishingContract.WeathersHourly.COLUMN_NAME_WEATHER_ID, weatherId);
            values.put(WildFishingContract.WeathersHourly.COLUMN_NAME_TIME, h.getTime());
            values.put(WildFishingContract.WeathersHourly.COLUMN_NAME_TEMP_C, h.getTempC());
            values.put(WildFishingContract.WeathersHourly.COLUMN_NAME_WIND_SPEED_KMPH, h.getWindspeedKmph());
            values.put(WildFishingContract.WeathersHourly.COLUMN_NAME_WIND_DIR_DEGREE, h.getWinddirDegree());
            values.put(WildFishingContract.WeathersHourly.COLUMN_NAME_PRESSURE, h.getPressure());
            values.put(WildFishingContract.WeathersHourly.COLUMN_NAME_CLOUD_COVER, h.getCloudcover());
            values.put(WildFishingContract.WeathersHourly.COLUMN_NAME_WEATHER_CODE, h.getWeatherCode());

            database.insert(
                    WildFishingContract.WeathersHourly.TABLE_NAME,
                    null,
                    values);
        }
    }
    
    public boolean isNotExist(String date) {
        Cursor cursor = database.query(WildFishingContract.Weathers.TABLE_NAME,
                weatherAllColumns, WildFishingContract.Weathers.COLUMN_NAME_DATE + " = '" + date +"'", null, null, null, null);
        
        cursor.moveToFirst();
        Weather obj = cursorToWeather(cursor);
        cursor.close();
        
        return  obj.getId() == null;
    }

    public List<Weather> getWeathers() {
        List<Weather> retList = new ArrayList<Weather>();

        Cursor cursor = database.query(WildFishingContract.Weathers.TABLE_NAME,
                weatherAllColumns, null, null, null, null, WildFishingContract.Weathers.COLUMN_NAME_DATE + " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Weather obj = cursorToWeather(cursor);
            retList.add(obj);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return retList;
    }
    
    public List<Weather> getWeathersByYear(String year) {
        List<Weather> retList = new ArrayList<Weather>();

        Cursor cursor = database.query(WildFishingContract.Weathers.TABLE_NAME,
                weatherAllColumns, WildFishingContract.Weathers.COLUMN_NAME_DATE + " like '"+year+"%'", null, null, null, WildFishingContract.Weathers.COLUMN_NAME_DATE + " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Weather obj = cursorToWeather(cursor);
            retList.add(obj);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return retList;
    }
    
    public List<Hourly> getWeathersHourly(String weatherId) {
        List<Hourly> retList = new ArrayList<Hourly>();

        Cursor cursor = database.query(WildFishingContract.WeathersHourly.TABLE_NAME,
                weatherHourlyAllColumns, WildFishingContract.WeathersHourly.COLUMN_NAME_WEATHER_ID + " = '" + weatherId +"'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Hourly obj = cursorToWeathersHourly(cursor);
            retList.add(obj);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return retList;
    }
    
    private Weather cursorToWeather(Cursor cursor) {
        Weather obj = new Weather();
        if (!cursor.isAfterLast()) {
            obj.setId(cursor.getString(0));
            obj.setDate(cursor.getString(1));
            obj.setMintempC(cursor.getString(2));
            obj.setMaxtempC(cursor.getString(3));
            Astronomy astronomy = new Astronomy();
            astronomy.setSunrise(cursor.getString(4));
            astronomy.setSunset(cursor.getString(5));
            obj.setAstronomy(astronomy);
        }

        return obj;
    }
    
    private Hourly cursorToWeathersHourly(Cursor cursor) {
        Hourly obj = new Hourly();
        if (!cursor.isAfterLast()) {
            obj.setId(cursor.getString(0));
            obj.setCloudcover(cursor.getString(1));
            obj.setPressure(cursor.getString(2));
            obj.setTempC(cursor.getString(3));
            obj.setTime(cursor.getString(4));
            obj.setWeatherCode(cursor.getString(5));
            obj.setWeatherName(BusinessUtil.getWeatherName(cursor.getString(5)));
            obj.setWinddirDegree(BusinessUtil.getWindDirDegreeName(cursor.getString(6)));
            obj.setWindspeedKmph(BusinessUtil.getWindspeedName(cursor.getString(7)));
            obj.setWeatherId(cursor.getString(8));
        }

        return obj;
    }

}
