package com.simple.wildfishingnote.database;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.simple.wildfishingnote.bean.Hourly;
import com.simple.wildfishingnote.bean.LocationData;
import com.simple.wildfishingnote.bean.Weather;
import com.simple.wildfishingnote.bean.WeatherAndLocation;

public class WeatherDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

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
            long weatherId = addWeather(wal);
            addWeatherHourly(wal.getWeatherData(), weatherId);
            database.setTransactionSuccessful();
        } catch (Exception e) {

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

    public String getWeathers() {
        String[] projection = {
                WildFishingContract.Weathers._ID,
                WildFishingContract.Weathers.COLUMN_NAME_DATE
        };

        String sortOrder =
                WildFishingContract.Weathers._ID + " DESC";

        Cursor c = database.query(
                WildFishingContract.Weathers.TABLE_NAME, // The table to query
                projection, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                sortOrder // The sort order
                );

        c.moveToFirst();
        return c.getString(0) + " : " + c.getString(1);
    }

}
