package com.simple.wildfishingnote.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.simple.wildfishingnote.bean.Astronomy;
import com.simple.wildfishingnote.bean.Campaign;
import com.simple.wildfishingnote.bean.Hourly;
import com.simple.wildfishingnote.bean.LocationData;
import com.simple.wildfishingnote.bean.Weather;
import com.simple.wildfishingnote.bean.WeatherAndLocation;

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

    public List<Weather> getWeathers() {
        List<Weather> retList = new ArrayList<Weather>();

        Cursor cursor = database.query(WildFishingContract.Campaigns.TABLE_NAME,
                weatherAllColumns, null, null, null, null, null);

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
    
    private Weather cursorToWeather(Cursor cursor) {
        Weather obj = new Weather();
        obj.setId(cursor.getString(0));
        obj.setDate(cursor.getString(1));
        obj.setMintempC(cursor.getString(2));
        obj.setMaxtempC(cursor.getString(3));
        Astronomy astronomy = new Astronomy();
        astronomy.setSunrise(cursor.getString(4));
        astronomy.setSunset(cursor.getString(5));
        obj.setAstronomy(astronomy);
        return obj;
    }

}
