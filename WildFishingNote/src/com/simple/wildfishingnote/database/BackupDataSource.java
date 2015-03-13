package com.simple.wildfishingnote.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.simple.wildfishingnote.bean.Backup;

public class BackupDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    
    private String[] backAllColumns = {WildFishingContract.Backups._ID,
            WildFishingContract.Backups.COLUMN_NAME_PATH,
            WildFishingContract.Backups.COLUMN_NAME_TIME};
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    
    // 备份
    private static final String SQL_CREATE_BACKUPS =
            "CREATE TABLE " + WildFishingContract.Backups.TABLE_NAME + " (" +
                    WildFishingContract.Backups._ID + " INTEGER PRIMARY KEY," +
                    WildFishingContract.Backups.COLUMN_NAME_PATH + TEXT_TYPE +  COMMA_SEP +
                    WildFishingContract.Backups.COLUMN_NAME_TIME + TEXT_TYPE +  
            " )";

    public BackupDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
    
    
    
    public void createTable() {
        database.execSQL("DROP TABLE IF EXISTS " + WildFishingContract.Backups.TABLE_NAME);
        database.execSQL(SQL_CREATE_BACKUPS);
    }

    public void addBackup(Backup backup) {
        String backupId = getBackupByPath(backup.getPath());
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WildFishingContract.Backups.COLUMN_NAME_PATH, backup.getPath());
        values.put(WildFishingContract.Backups.COLUMN_NAME_TIME, backup.getTime());
        
        if(backupId == null){
            database.insert(
                    WildFishingContract.Backups.TABLE_NAME,
                    null,
                    values);
        }else{
            String selection = WildFishingContract.Backups._ID + " = ? ";
            String[] selelectionArgs = { String.valueOf(backupId) };
            database.update(WildFishingContract.Backups.TABLE_NAME, values, selection, selelectionArgs);
        }
    }
    
    public String getBackupByPath(String path) {
        Cursor cursor = database.query(WildFishingContract.Backups.TABLE_NAME,
                backAllColumns, "path = '" + path + "'", null, null, null, WildFishingContract.Backups.COLUMN_NAME_TIME + " DESC");

        Backup obj = null;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            obj = cursorToBackup(cursor);
            cursor.moveToNext();
            break;
        }
        // make sure to close the cursor
        cursor.close();

        if (obj == null) {
            return null;
        } else {
            return obj.getId();
        }
    }

    public List<Backup> getBackups() {
        List<Backup> retList = new ArrayList<Backup>();

        Cursor cursor = database.query(WildFishingContract.Backups.TABLE_NAME,
                backAllColumns, null, null, null, null, WildFishingContract.Backups.COLUMN_NAME_TIME + " DESC");

        int wantToFetchRecord = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (wantToFetchRecord == 1) {
                break;
            }
            Backup obj = cursorToBackup(cursor);
            retList.add(obj);
            cursor.moveToNext();
            wantToFetchRecord++;
        }
        // make sure to close the cursor
        cursor.close();
        return retList;
    }
    
    
    private Backup cursorToBackup(Cursor cursor) {
        Backup obj = new Backup();
        obj.setId(cursor.getString(0));
        obj.setPath(cursor.getString(1));
        obj.setTime(cursor.getString(2));

        return obj;
    }

}
