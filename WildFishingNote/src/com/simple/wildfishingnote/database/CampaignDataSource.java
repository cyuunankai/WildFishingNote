package com.simple.wildfishingnote.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.simple.wildfishingnote.bean.Campaign;

public class CampaignDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {WildFishingContract.Campaigns._ID,
            WildFishingContract.Campaigns.COL_NAME_START_TIME,
            WildFishingContract.Campaigns.COL_NAME_END_TIME,
            WildFishingContract.Campaigns.COL_NAME_SUMMARY};

    public CampaignDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Campaign addCampaign(Campaign campaign) {
        ContentValues values = new ContentValues();
        values.put(WildFishingContract.Campaigns.COL_NAME_START_TIME, campaign.getStartTime());
        values.put(WildFishingContract.Campaigns.COL_NAME_END_TIME, campaign.getEndTime());
        values.put(WildFishingContract.Campaigns.COL_NAME_SUMMARY, campaign.getSummary());

        // Insert the new row, returning the primary key value of the new row
        long insertId = database.insert(WildFishingContract.Campaigns.TABLE_NAME, null,
                values);
        Cursor cursor = database.query(WildFishingContract.Campaigns.TABLE_NAME,
                allColumns, WildFishingContract.Campaigns._ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Campaign newCampaign = cursorToCampaign(cursor);
        cursor.close();
        return newCampaign;
    }

    public void deleteCampaign(Campaign campagin) {
        long id = campagin.getId();
        System.out.println("Campaign deleted with id: " + id);
        database.delete(WildFishingContract.Campaigns.TABLE_NAME, WildFishingContract.Campaigns._ID
                + " = " + id, null);
    }

    public List<Campaign> getAllCampagins() {
        List<Campaign> campaignList = new ArrayList<Campaign>();

        Cursor cursor = database.query(WildFishingContract.Campaigns.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Campaign campaign = cursorToCampaign(cursor);
            campaignList.add(campaign);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return campaignList;
    }

    private Campaign cursorToCampaign(Cursor cursor) {
        Campaign campaign = new Campaign();
        campaign.setId(cursor.getLong(0));
        campaign.setStartTime(cursor.getString(1));
        campaign.setEndTime(cursor.getString(2));
        campaign.setSummary(cursor.getString(3));
        return campaign;
    }
}
