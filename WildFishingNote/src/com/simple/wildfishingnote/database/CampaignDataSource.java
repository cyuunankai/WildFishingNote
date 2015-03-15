package com.simple.wildfishingnote.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.simple.wildfishingnote.bean.Bait;
import com.simple.wildfishingnote.bean.Campaign;
import com.simple.wildfishingnote.bean.CampaignSummary;
import com.simple.wildfishingnote.bean.FishType;
import com.simple.wildfishingnote.bean.LureMethod;
import com.simple.wildfishingnote.bean.Place;
import com.simple.wildfishingnote.bean.Point;
import com.simple.wildfishingnote.bean.RelayCamapignStatisticsResult;
import com.simple.wildfishingnote.bean.RelayCampaignImageResult;
import com.simple.wildfishingnote.bean.RelayCampaignPoint;
import com.simple.wildfishingnote.bean.RodLength;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.utils.BusinessUtil;

public class CampaignDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] campaignAllColumns = {WildFishingContract.Campaigns._ID,
            WildFishingContract.Campaigns.COL_NAME_START_TIME,
            WildFishingContract.Campaigns.COL_NAME_END_TIME,
            WildFishingContract.Campaigns.COL_NAME_SUMMARY,
            WildFishingContract.Campaigns.COL_NAME_PLACE_ID};
    
    private String[] placeAllColumns = { WildFishingContract.Places._ID,
			WildFishingContract.Places.COLUMN_NAME_TITLE,
			WildFishingContract.Places.COLUMN_NAME_DETAIL,
			WildFishingContract.Places.COLUMN_NAME_FILE_NAME };
    
    private String[] pointAllColumns = { WildFishingContract.Points._ID,
			WildFishingContract.Points.COLUMN_NAME_ROD_LENGTH_ID,
			WildFishingContract.Points.COLUMN_NAME_DEPTH,
			WildFishingContract.Points.COLUMN_NAME_LURE_METHOD_ID,
			WildFishingContract.Points.COLUMN_NAME_BAIT_ID};
    
    private String[] relayCampaignPointAllColumns = { WildFishingContract.RelayCampaignPoints._ID,
            WildFishingContract.RelayCampaignPoints.COLUMN_NAME_CAMPAIGN_ID,
            WildFishingContract.RelayCampaignPoints.COLUMN_NAME_POINT_ID };
    
    private String[] rodLengthAllColumns = { WildFishingContract.RodLengths._ID,
			WildFishingContract.RodLengths.COLUMN_NAME_NAME };
    
    private String[] lureMethodAllColumns = { WildFishingContract.LureMethods._ID,
			WildFishingContract.LureMethods.COLUMN_NAME_NAME,
			WildFishingContract.LureMethods.COLUMN_NAME_DETAIL };
    
    private String[] baitAllColumns = { WildFishingContract.Baits._ID,
			WildFishingContract.Baits.COLUMN_NAME_NAME,
			WildFishingContract.Baits.COLUMN_NAME_DETAIL };
    
    private String[] relayCampaignImageResultsAllColumns = { WildFishingContract.RelayCampaignImageResults._ID,
            WildFishingContract.RelayCampaignImageResults.COLUMN_NAME_CAMPAIGN_ID,
            WildFishingContract.RelayCampaignImageResults.COLUMN_NAME_FILE_PATH };
    
    private String[] relayCamapignStatisticsResultsAllColumns = { WildFishingContract.RelayCamapignStatisticsResults._ID,
            WildFishingContract.RelayCamapignStatisticsResults.COLUMN_NAME_CAMPAIGN_ID,
            WildFishingContract.RelayCamapignStatisticsResults.COLUMN_NAME_POINT_ID,
            WildFishingContract.RelayCamapignStatisticsResults.COLUMN_NAME_FISH_TYPE_ID,
            WildFishingContract.RelayCamapignStatisticsResults.COLUMN_NAME_WEIGHT,
            WildFishingContract.RelayCamapignStatisticsResults.COLUMN_NAME_COUNT,
            WildFishingContract.RelayCamapignStatisticsResults.COLUMN_NAME_HOOK_FLAG };

    public CampaignDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
    
    
    public List<CampaignSummary> getAllCampaignSummarys() {
        List<CampaignSummary> retList = new ArrayList<CampaignSummary>();
        
        HashMap<String, CampaignSummary> campaignIdHash = new HashMap<String, CampaignSummary>();
        HashMap<String, List<RelayCamapignStatisticsResult>> statisticsHash = new HashMap<String, List<RelayCamapignStatisticsResult>>();
        HashMap<String, List<RelayCampaignImageResult>> imageHash = new HashMap<String, List<RelayCampaignImageResult>>();
        
        StringBuffer  sb = new StringBuffer();
        sb.append("SELECT c._id,c.start_time, c.summary, ");
        sb.append(" rcsr.weight AS weight,rcsr.count AS count, rcsr.hook_flag AS hook_flag, ");
        sb.append(" rcir.file_path AS file_path ");
        sb.append("FROM campaigns c  ");
        sb.append("LEFT JOIN relay_campaign_statistics_results rcsr ON rcsr.campaign_id=c._id ");
        sb.append("LEFT JOIN relay_campaign_image_results rcir ON rcir.campaign_id=c._id ");
        sb.append("order by c.start_Time DESC ");

        
        Cursor c = database.rawQuery(sb.toString(), new String[]{});
        
        c.moveToFirst();
        while(!c.isAfterLast()){
            List<RelayCamapignStatisticsResult> rcsrList= null;
            List<RelayCampaignImageResult> rcirList= null;
            CampaignSummary obj = null;
            RelayCamapignStatisticsResult rcsr = null;
            RelayCampaignImageResult rcir = null;
            
            String campaignId = c.getString(0);
            String date = c.getString(1).split(Constant.SPACE)[0];
            String summary = c.getString(2);
            String weight = c.getString(3);
            String count = c.getString(4);
            String hookFlag = c.getString(5);
            
            String filePath = c.getString(6);
            
            if(!campaignIdHash.containsKey(campaignId)){
                obj = new CampaignSummary();
                obj.setId(campaignId);
                obj.setDate(date);
                obj.setSummary(summary);
                campaignIdHash.put(campaignId, obj);
            }
            
            if(statisticsHash.containsKey(campaignId)){
                rcsrList = statisticsHash.get(campaignId);
                rcsr = new RelayCamapignStatisticsResult();
                rcsr.setWeight(weight);
                rcsr.setCount(count);
                rcsr.setHookFlag(hookFlag);
                rcsrList.add(rcsr);
                
                statisticsHash.put(campaignId, rcsrList);
            }else{
                rcsrList = new ArrayList<RelayCamapignStatisticsResult>();
                rcsr = new RelayCamapignStatisticsResult();
                rcsr.setWeight(weight);
                rcsr.setCount(count);
                rcsr.setHookFlag(hookFlag);
                rcsrList.add(rcsr);
                
                statisticsHash.put(campaignId, rcsrList);
            }
            
            if(imageHash.containsKey(campaignId)){
                rcirList = imageHash.get(campaignId);
                rcir = new RelayCampaignImageResult();
                rcir.setFilePath(filePath);
                rcirList.add(rcir);
                
                imageHash.put(campaignId, rcirList);
            }else{
                rcirList = new ArrayList<RelayCampaignImageResult>();
                rcir = new RelayCampaignImageResult();
                rcir.setFilePath(filePath);
                rcirList.add(rcir);
                
                imageHash.put(campaignId, rcirList);
            }

            c.moveToNext();
        }
        c.close();
        
        
        ArrayList<CampaignSummary> tempList = new ArrayList<CampaignSummary>(campaignIdHash.values());
        for(CampaignSummary cc : tempList){
            CampaignSummary newObj = new CampaignSummary();
            
            String title = "";
            if(statisticsHash.containsKey(cc.getId())){
                title = BusinessUtil.getCampaignSummaryTitle(statisticsHash, cc);
            }else{
                title = "空军";
            }
            String imagePath = "";
            if(imageHash.containsKey(cc.getId())){
                imagePath = imageHash.get(cc.getId()).get(0).getFilePath();
            }
            
            newObj.setId(cc.getId());
            newObj.setDate(cc.getDate());
            newObj.setSummary(cc.getSummary());
            newObj.setTitle(title);
            newObj.setImagePath(imagePath);
            
            retList.add(newObj);
        }
        
        Collections.sort(retList, new Comparator<CampaignSummary>() {

            public int compare(CampaignSummary o1, CampaignSummary o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        
        return retList;
    }

    public void addAllData(Campaign campaign) {
        database.beginTransaction();
        try {
            // 1. 活动
            String campaignId = addCampaign(campaign);
            // 2. 活动_钓点关系表
            addRelayCampaignPoint(campaignId, campaign.getPointIdList());
            // 3. 活动渔获图片关系表
            addRelayCampaignImageResult(campaignId, campaign.getPicList());
            // 4. 渔获统计关系表
            addRelayCamapignStatisticsResults(campaignId, campaign.getStatisticsList());
            
            database.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            database.endTransaction();
        }

    }
    
    // 活动
    
    public String addCampaign(Campaign campaign) {
        ContentValues values = new ContentValues();
        values.put(WildFishingContract.Campaigns.COL_NAME_START_TIME, campaign.getStartTime());
        values.put(WildFishingContract.Campaigns.COL_NAME_END_TIME, campaign.getEndTime());
        values.put(WildFishingContract.Campaigns.COL_NAME_SUMMARY, campaign.getSummary());
        values.put(WildFishingContract.Campaigns.COL_NAME_PLACE_ID, campaign.getPlaceId());

        long insertId = database.insert(WildFishingContract.Campaigns.TABLE_NAME, null,
                values);
        return String.valueOf(insertId);
    }
    
    public Campaign updateCampaign(Campaign campaign) {

        ContentValues values = new ContentValues();
        values.put(WildFishingContract.Campaigns.COL_NAME_START_TIME, campaign.getStartTime());
        values.put(WildFishingContract.Campaigns.COL_NAME_END_TIME, campaign.getEndTime());
        values.put(WildFishingContract.Campaigns.COL_NAME_SUMMARY, campaign.getSummary());
        values.put(WildFishingContract.Campaigns.COL_NAME_PLACE_ID, campaign.getPlaceId());

        String selection = WildFishingContract.Campaigns._ID + " = ? ";
        String[] selelectionArgs = { String.valueOf(campaign.getId()) };

        database.update(WildFishingContract.Campaigns.TABLE_NAME, values,
                selection, selelectionArgs);

        return getCampaignById(String.valueOf(campaign.getId()));
    }

    public void deleteCampaign(String campaginId) {
        database.delete(WildFishingContract.Campaigns.TABLE_NAME, WildFishingContract.Campaigns._ID
                + " = " + campaginId, null);
    }
    
    public Campaign getCampaignById(String campaginId) {
        Cursor cursor = database.query(WildFishingContract.Campaigns.TABLE_NAME,
                campaignAllColumns, WildFishingContract.Campaigns._ID + " = " + campaginId,
                null, null, null, null);
        cursor.moveToFirst();
        Campaign campaign = cursorToCampaign(cursor);
        cursor.close();

        return campaign;
    }

    public List<Campaign> getAllCampagins() {
        List<Campaign> campaignList = new ArrayList<Campaign>();

        Cursor cursor = database.query(WildFishingContract.Campaigns.TABLE_NAME,
        		campaignAllColumns, null, null, null, null, null);

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
        campaign.setId(cursor.getString(0));
        campaign.setStartTime(cursor.getString(1));
        campaign.setEndTime(cursor.getString(2));
        campaign.setSummary(cursor.getString(3));
        campaign.setPlaceId(cursor.getString(4));
        return campaign;
    }
    
    public String getMaxCampaignId() {
    	
	    StringBuffer  sb = new StringBuffer();
        sb.append("SELECT max(_id) as max_id ");
        sb.append("FROM campaigns   ");

        Cursor c = database.rawQuery(sb.toString(), null);

		c.moveToFirst();
		int max = c.getInt(0) + 1;
		c.close();
		
		return String.valueOf(max);
	}
    
    // 钓位
    
    public Place addPlace(Place place) {

		ContentValues values = new ContentValues();
		values.put(WildFishingContract.Places.COLUMN_NAME_TITLE,
				place.getTitle());
		values.put(WildFishingContract.Places.COLUMN_NAME_DETAIL,
				place.getDetail());
		values.put(WildFishingContract.Places.COLUMN_NAME_FILE_NAME,
				place.getFileName());

		// Insert the new row, returning the primary key value of the new row
		long insertId = database.insert(WildFishingContract.Places.TABLE_NAME,
				null, values);

		return getPlaceById(String.valueOf(insertId));
	}

	public Place updatePlace(Place place) {

		ContentValues values = new ContentValues();
		values.put(WildFishingContract.Places.COLUMN_NAME_TITLE,
				place.getTitle());
		values.put(WildFishingContract.Places.COLUMN_NAME_DETAIL,
				place.getDetail());
		values.put(WildFishingContract.Places.COLUMN_NAME_FILE_NAME,
				place.getFileName());

		String selection = WildFishingContract.Places._ID + " = ? ";
		String[] selelectionArgs = { place.getId() };

		database.update(WildFishingContract.Places.TABLE_NAME, values,
				selection, selelectionArgs);

		return getPlaceById(place.getId());
	}

	public void deletePlace(String rowId) {

		String selection = WildFishingContract.Places._ID + " = ? ";
		String[] selelectionArgs = { rowId };

		database.delete(WildFishingContract.Places.TABLE_NAME, selection,
				selelectionArgs);
	}

	public Place getPlaceById(String placeId) {
		Cursor cursor = database.query(WildFishingContract.Places.TABLE_NAME,
				placeAllColumns, WildFishingContract.Places._ID + " = " + placeId,
				null, null, null, null);
		cursor.moveToFirst();
		Place place = cursorToPlace(cursor);
		cursor.close();

		return place;
	}

	public Cursor getPlaceForPinner() {

		String[] projection = { WildFishingContract.Places._ID,
				WildFishingContract.Places.COLUMN_NAME_TITLE };

		Cursor c = database.query(WildFishingContract.Places.TABLE_NAME, 
				projection, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);

		return c;
	}
	
	public List<Place> getPlacesForList() {
		List<Place> list = new ArrayList<Place>();
		
		Cursor c = database.query(WildFishingContract.Places.TABLE_NAME, 
				placeAllColumns, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);
		
		c.moveToFirst();
		while(!c.isAfterLast()){
			Place obj = new Place();
			obj.setId(c.getString(c.getColumnIndex(WildFishingContract.Places._ID)));
			obj.setTitle(c.getString(c.getColumnIndex(WildFishingContract.Places.COLUMN_NAME_TITLE)));
			obj.setDetail(c.getString(c.getColumnIndex(WildFishingContract.Places.COLUMN_NAME_DETAIL)));
			obj.setFileName(c.getString(c.getColumnIndex(WildFishingContract.Places.COLUMN_NAME_FILE_NAME)));
		
			list.add(obj);
			c.moveToNext();
		}
		c.close();
		
		return list;
	}

	private Place cursorToPlace(Cursor cursor) {
		Place place = new Place();
		place.setId(cursor.getString(0));
		place.setTitle(cursor.getString(1));
		place.setDetail(cursor.getString(2));
		place.setFileName(cursor.getString(3));
		return place;
	}
	
	public String getMaxPlaceId() {
    	
	    StringBuffer  sb = new StringBuffer();
        sb.append("SELECT max(_id) as max_id ");
        sb.append("FROM places   ");

        Cursor c = database.rawQuery(sb.toString(), null);

		c.moveToFirst();
		int max = c.getInt(0) + 1;
		c.close();
		
		return String.valueOf(max);
	}
	
	// 钓点
	
	public Point addPoint(Point point) {
    	
    	ContentValues values = new ContentValues();
    	values.put(WildFishingContract.Points.COLUMN_NAME_ROD_LENGTH_ID, point.getRodLengthId());
    	values.put(WildFishingContract.Points.COLUMN_NAME_DEPTH, point.getDepth());
    	values.put(WildFishingContract.Points.COLUMN_NAME_LURE_METHOD_ID, point.getLureMethodId());
    	values.put(WildFishingContract.Points.COLUMN_NAME_BAIT_ID, point.getBaitId());

    	// Insert the new row, returning the primary key value of the new row
    	long newRowId;
    	newRowId = database.insert(
    			 WildFishingContract.Points.TABLE_NAME,
    	         null,
    	         values);
    	
    	return getPointById(String.valueOf(newRowId));
	}
	
	public Point updatePoint(Point point) {
    	
    	ContentValues values = new ContentValues();
    	values.put(WildFishingContract.Points.COLUMN_NAME_ROD_LENGTH_ID, point.getRodLengthId());
    	values.put(WildFishingContract.Points.COLUMN_NAME_DEPTH, point.getDepth());
    	values.put(WildFishingContract.Points.COLUMN_NAME_LURE_METHOD_ID, point.getLureMethodId());
    	values.put(WildFishingContract.Points.COLUMN_NAME_BAIT_ID, point.getBaitId());
    	
    	String selection = WildFishingContract.Points._ID + " = ? ";
		String[] selelectionArgs = { point.getId() };

    	database.update(
    			 WildFishingContract.Points.TABLE_NAME,
    			 values,
    			 selection,
    			 selelectionArgs);
    	
    	return getPointById(point.getId());
	}
	
	public void deletePoint(String rowId) {
    	
    	String selection = WildFishingContract.Points._ID + " = ? ";
		String[] selelectionArgs = { rowId };

    	database.delete(
    			 WildFishingContract.Points.TABLE_NAME,
    			 selection,
    			 selelectionArgs);
	}

	public Point getPointById(String rowId) {
	    StringBuffer  sb = new StringBuffer();
        sb.append("SELECT p._id,p.depth, ");
        sb.append(" rl._id AS rl_id,rl.name AS rod_length_name, ");
        sb.append(" lm._id AS lm_id,lm.name AS lure_method_name,lm.detail AS lure_method_detail, ");
        sb.append(" b._id AS b_id,b.name AS bait_name, b.detail AS bait_detail ");
        sb.append("FROM points p  ");
        sb.append("INNER JOIN rod_lengths rl ON p.rod_length_id=rl._id ");
        sb.append("INNER JOIN lure_methods lm ON p.lure_method_id=lm._id ");
        sb.append("INNER JOIN baits b ON p.bait_id=b._id ");
        sb.append(" WHERE p._id=?");

        
        Cursor c = database.rawQuery(sb.toString(), new String[]{rowId});

		c.moveToFirst();
		Point point = cursorToPoint(c);
		c.close();
		
		return point;
	}
	
	public List<Point> getAllPoints() {
	        List<Point> list = new ArrayList<Point>();
	        
	        StringBuffer  sb = new StringBuffer();
	        sb.append("SELECT p._id,p.depth, ");
	        sb.append(" rl._id AS rl_id,rl.name AS rod_length_name, ");
	        sb.append(" lm._id AS lm_id,lm.name AS lure_method_name,lm.detail AS lure_method_detail, ");
	        sb.append(" b._id AS b_id,b.name AS bait_name, b.detail AS bait_detail ");
	        sb.append("FROM points p  ");
	        sb.append("INNER JOIN rod_lengths rl ON p.rod_length_id=rl._id ");
	        sb.append("INNER JOIN lure_methods lm ON p.lure_method_id=lm._id ");
	        sb.append("INNER JOIN baits b ON p.bait_id=b._id ");

	        
	        Cursor c = database.rawQuery(sb.toString(), new String[]{});
	        
	        c.moveToFirst();
	        while(!c.isAfterLast()){
	            Point point = cursorToPoint(c);
	        
	            list.add(point);
	            c.moveToNext();
	        }
	        c.close();
	        
	        return list;
	    }
	
	public List<Point> getPointsByIds(List<String> pointIds) {
        List<Point> list = new ArrayList<Point>();
        
        StringBuffer  sb = new StringBuffer();
        sb.append("SELECT p._id,p.depth, ");
        sb.append(" rl._id AS rl_id,rl.name AS rod_length_name, ");
        sb.append(" lm._id AS lm_id,lm.name AS lure_method_name,lm.detail AS lure_method_detail, ");
        sb.append(" b._id AS b_id,b.name AS bait_name, b.detail AS bait_detail ");
        sb.append("FROM points p  ");
        sb.append("INNER JOIN rod_lengths rl ON p.rod_length_id=rl._id ");
        sb.append("INNER JOIN lure_methods lm ON p.lure_method_id=lm._id ");
        sb.append("INNER JOIN baits b ON p.bait_id=b._id ");
        sb.append("WHERE p._id in ("+StringUtils.join(pointIds, ",")+") ");
        
        Cursor c = database.rawQuery(sb.toString(), new String[]{});
        
        c.moveToFirst();
        while(!c.isAfterLast()){
            Point point = cursorToPoint(c);
        
            list.add(point);
            c.moveToNext();
        }
        c.close();
        
        return list;
    }
	
	private Point cursorToPoint(Cursor c) {
		Point point = new Point();
		point.setId(c.getString(c.getColumnIndex(WildFishingContract.Points._ID)));
        point.setDepth(c.getString(c.getColumnIndex(WildFishingContract.Points.COLUMN_NAME_DEPTH)));
        point.setRodLengthId(c.getString(c.getColumnIndex("rl_id")));
        point.setRodLengthName(c.getString(c.getColumnIndex("rod_length_name")));
        point.setLureMethodId(c.getString(c.getColumnIndex("lm_id")));
        point.setLureMethodName(c.getString(c.getColumnIndex("lure_method_name")));
        point.setLureMethodDetail(c.getString(c.getColumnIndex("lure_method_detail")));
        point.setBaitId(c.getString(c.getColumnIndex("b_id")));
        point.setBaitName(c.getString(c.getColumnIndex("bait_name")));
        point.setBaitDetail(c.getString(c.getColumnIndex("bait_detail")));
		return point;
	}
	
	
    // 活动钓点关系
    
    public void addRelayCampaignPoint(String campaignId, List<String> pointIdList) {

        for (String pointId : pointIdList) {
            ContentValues values = new ContentValues();
            values.put(WildFishingContract.RelayCampaignPoints.COLUMN_NAME_CAMPAIGN_ID, campaignId);
            values.put(WildFishingContract.RelayCampaignPoints.COLUMN_NAME_POINT_ID, pointId);

            // Insert the new row, returning the primary key value of the new row
            database.insert(
                    WildFishingContract.RelayCampaignPoints.TABLE_NAME,
                    null,
                    values);
        }
    }
    
    public void updateRelayCampaignPoint(String campaignId, List<String> pointIdList) {
        
        deleteRelayCampaignPoint(campaignId);
        addRelayCampaignPoint(campaignId, pointIdList);
    }
    
    public void deleteRelayCampaignPoint(String campaignId) {
        
        String selection = WildFishingContract.RelayCampaignPoints._ID + " = ? ";
        String[] selelectionArgs = { campaignId };

        database.delete(
                 WildFishingContract.RelayCampaignPoints.TABLE_NAME,
                 selection,
                 selelectionArgs);
    }

    public List<String> getPointIdListByCampaignId(String campaignId) {
        List<String> retList = new ArrayList<String>();

        String selection = WildFishingContract.RelayCampaignPoints.COLUMN_NAME_CAMPAIGN_ID + " = ? ";
        String[] selelectionArgs = {campaignId};

        Cursor c = database.query(WildFishingContract.RelayCampaignPoints.TABLE_NAME,
                relayCampaignPointAllColumns, // The columns to return
                selection, // The columns for the WHERE clause
                selelectionArgs, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
                );

        c.moveToFirst();
        while (!c.isAfterLast()) {
            RelayCampaignPoint rcp = cursorToRelayCampaignPoint(c);
            retList.add(rcp.getPointId());

            c.moveToNext();
        }
        c.close();

        return retList;
    }

    private RelayCampaignPoint cursorToRelayCampaignPoint(Cursor c) {
        RelayCampaignPoint rcp = new RelayCampaignPoint();
        rcp.setId(c.getString(c.getColumnIndex(WildFishingContract.RelayCampaignPoints._ID)));
        rcp.setCampaignId(c.getString(c.getColumnIndex(WildFishingContract.RelayCampaignPoints.COLUMN_NAME_CAMPAIGN_ID)));
        rcp.setPointId(c.getString(c.getColumnIndex(WildFishingContract.RelayCampaignPoints.COLUMN_NAME_POINT_ID)));
        
        return rcp;
    }
	
	// 竿长
	public RodLength addRodLength(RodLength rl) {

		ContentValues values = new ContentValues();
		values.put(WildFishingContract.RodLengths.COLUMN_NAME_NAME,
				rl.getName());

		// Insert the new row, returning the primary key value of the new row
		long insertId = database.insert(
				WildFishingContract.RodLengths.TABLE_NAME, null, values);

		return getRodLengthById(String.valueOf(insertId));
	}

	public RodLength updateRodLength(RodLength rl) {

		ContentValues values = new ContentValues();
		values.put(WildFishingContract.RodLengths.COLUMN_NAME_NAME,
				rl.getName());

		String selection = WildFishingContract.RodLengths._ID + " = ? ";
		String[] selelectionArgs = { rl.getId() };

		database.update(WildFishingContract.RodLengths.TABLE_NAME, values,
				selection, selelectionArgs);

		return getRodLengthById(rl.getId());
	}

	public void deleteRodLength(String rowId) {

		String selection = WildFishingContract.RodLengths._ID + " = ? ";
		String[] selelectionArgs = { rowId };

		database.delete(WildFishingContract.RodLengths.TABLE_NAME, selection,
				selelectionArgs);
	}

	public RodLength getRodLengthById(String rowId) {
		
		String selection = WildFishingContract.RodLengths._ID + " = ? ";
		String[] selelectionArgs = { rowId };

		Cursor c = database.query(WildFishingContract.RodLengths.TABLE_NAME, 
				rodLengthAllColumns, // The columns to return
				selection, // The columns for the WHERE clause
				selelectionArgs, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);

		c.moveToFirst();
		RodLength rl = cursorToRodLength(c);
		c.close();

		return rl;
	}

	public List<RodLength> getAllRodLengths() {
	    List<RodLength> retList = new ArrayList<RodLength>();
	    
		String[] projection = { WildFishingContract.RodLengths._ID,
				WildFishingContract.RodLengths.COLUMN_NAME_NAME };

		Cursor c = database.query(WildFishingContract.RodLengths.TABLE_NAME, 
				projection, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);
		
		c.moveToFirst();
		while(!c.isAfterLast()){
		    RodLength rl = cursorToRodLength(c); 
		    retList.add(rl);
		    
		    c.moveToNext();
		}
		c.close();
		
		return retList;
	}

	private RodLength cursorToRodLength(Cursor cursor) {
		RodLength obj = new RodLength();
		obj.setId(cursor.getString(0));
		obj.setName(cursor.getString(1));
		return obj;
	}
	
	// 打窝
	public LureMethod addLureMethod(LureMethod lm) {

		ContentValues values = new ContentValues();
		values.put(WildFishingContract.LureMethods.COLUMN_NAME_NAME,
				lm.getName());
		values.put(WildFishingContract.LureMethods.COLUMN_NAME_DETAIL,
				lm.getDetail());

		// Insert the new row, returning the primary key value of the new row
		long newRowId = database.insert(
				WildFishingContract.LureMethods.TABLE_NAME, null, values);

		return getLureMethodById(String.valueOf(newRowId));
	}

	public LureMethod updateLureMethod(LureMethod rl) {
		ContentValues values = new ContentValues();
		values.put(WildFishingContract.LureMethods.COLUMN_NAME_NAME,
				rl.getName());
		values.put(WildFishingContract.LureMethods.COLUMN_NAME_DETAIL,
				rl.getDetail());

		String selection = WildFishingContract.LureMethods._ID + " = ? ";
		String[] selelectionArgs = { rl.getId() };

		database.update(WildFishingContract.LureMethods.TABLE_NAME, values,
				selection, selelectionArgs);

		return getLureMethodById(rl.getId());
	}

	public void deleteLureMethod(String rowId) {

		String selection = WildFishingContract.LureMethods._ID + " = ? ";
		String[] selelectionArgs = { rowId };

		database.delete(WildFishingContract.LureMethods.TABLE_NAME, selection,
				selelectionArgs);
	}

	public LureMethod getLureMethodById(String rowId) {
		String selection = WildFishingContract.LureMethods._ID + " = ? ";
		String[] selelectionArgs = { rowId };

		Cursor c = database.query(WildFishingContract.LureMethods.TABLE_NAME, 
				lureMethodAllColumns, // The columns to return
				selection, // The columns for the WHERE clause
				selelectionArgs, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);

		c.moveToFirst();
		LureMethod lm = cursorToLureMethod(c);
		c.close();

		return lm;
	}
	
    public List<LureMethod> getAllLureMethods() {
        List<LureMethod> retList = new ArrayList<LureMethod>();

        Cursor c = database.query(WildFishingContract.LureMethods.TABLE_NAME,
                lureMethodAllColumns, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
                );

        c.moveToFirst();
        while (!c.isAfterLast()) {
            LureMethod lm = cursorToLureMethod(c);
            retList.add(lm);

            c.moveToNext();
        }
        c.close();

        return retList;
    }

	private LureMethod cursorToLureMethod(Cursor c) {
		LureMethod lm = new LureMethod();
		lm.setId(c.getString(c
				.getColumnIndex(WildFishingContract.LureMethods._ID)));
		lm.setName(c.getString(c
				.getColumnIndex(WildFishingContract.LureMethods.COLUMN_NAME_NAME)));
		lm.setDetail(c.getString(c
				.getColumnIndex(WildFishingContract.LureMethods.COLUMN_NAME_DETAIL)));
		return lm;
	}
	
	// 饵料
	public Bait addBait(Bait bait) {

		ContentValues values = new ContentValues();
		values.put(WildFishingContract.Baits.COLUMN_NAME_NAME, bait.getName());
		values.put(WildFishingContract.Baits.COLUMN_NAME_DETAIL,
				bait.getDetail());

		// Insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = database.insert(WildFishingContract.Baits.TABLE_NAME, null,
				values);

		return getBaitById(String.valueOf(newRowId));
	}

	public Bait updateBait(Bait bait) {

		ContentValues values = new ContentValues();
		values.put(WildFishingContract.Baits.COLUMN_NAME_NAME, bait.getName());
		values.put(WildFishingContract.Baits.COLUMN_NAME_DETAIL,
				bait.getDetail());

		String selection = WildFishingContract.Baits._ID + " = ? ";
		String[] selelectionArgs = { bait.getId() };

		database.update(WildFishingContract.Baits.TABLE_NAME, values,
				selection, selelectionArgs);

		return getBaitById(bait.getId());
	}

	public void deleteBait(String rowId) {

		String selection = WildFishingContract.Baits._ID + " = ? ";
		String[] selelectionArgs = { rowId };

		database.delete(WildFishingContract.Baits.TABLE_NAME, selection,
				selelectionArgs);
	}

	public Bait getBaitById(String rowId) {
		String selection = WildFishingContract.Baits._ID + " = ? ";
		String[] selelectionArgs = { rowId };

		Cursor c = database.query(WildFishingContract.Baits.TABLE_NAME, 
				baitAllColumns, // The columns to return
				selection, // The columns for the WHERE clause
				selelectionArgs, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);

		c.moveToFirst();
		Bait bait = cursorToBait(c);
		c.close();

		return bait;
	}

    public List<Bait> getAllBaits() {
        List<Bait> retList = new ArrayList<Bait>();

        Cursor c = database.query(WildFishingContract.Baits.TABLE_NAME,
                baitAllColumns, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
                );

        c.moveToFirst();
        while (!c.isAfterLast()) {
            Bait obj = cursorToBait(c);
            retList.add(obj);

            c.moveToNext();
        }
        c.close();

        return retList;
    }

	private Bait cursorToBait(Cursor c) {
		Bait bait = new Bait();
		bait.setId(c.getString(c.getColumnIndex(WildFishingContract.Baits._ID)));
		bait.setName(c.getString(c
				.getColumnIndex(WildFishingContract.Baits.COLUMN_NAME_NAME)));
		bait.setDetail(c.getString(c
				.getColumnIndex(WildFishingContract.Baits.COLUMN_NAME_DETAIL)));
		return bait;
	}
	
	/**
	 * 添加活动渔获图片关系
	 */
	public void addRelayCampaignImageResult(String campaignId, List<String> picList) {

	    for(String path : picList){
	        ContentValues values = new ContentValues();
	        values.put(WildFishingContract.RelayCampaignImageResults.COLUMN_NAME_CAMPAIGN_ID, campaignId);
	        values.put(WildFishingContract.RelayCampaignImageResults.COLUMN_NAME_FILE_PATH, path);
	        
	        database.insert(WildFishingContract.RelayCampaignImageResults.TABLE_NAME, null,values);
	    }
	}
	
	public List<String> getFishResultPicList(String campaignId) {
        List<String> retList = new ArrayList<String>();

        Cursor c = database.query(WildFishingContract.RelayCampaignImageResults.TABLE_NAME,
        		relayCampaignImageResultsAllColumns, // The columns to return
        		WildFishingContract.RelayCampaignImageResults.COLUMN_NAME_CAMPAIGN_ID + " = " + campaignId , // The columns for the WHERE clause
        		null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
                );

        c.moveToFirst();
        while (!c.isAfterLast()) {
        	RelayCampaignImageResult obj = cursorToRelayCampaignImageResults(c);
            retList.add(obj.getFilePath());

            c.moveToNext();
        }
        c.close();

        return retList;
    }
	
	private RelayCampaignImageResult cursorToRelayCampaignImageResults(Cursor c) {
		RelayCampaignImageResult obj = new RelayCampaignImageResult();
		obj.setId(c.getString(0));
		obj.setCampaignId(c.getString(1));
		obj.setFilePath(c.getString(2));
		return obj;
	}
	
	public void deleteRelayCampaignImageResult(String campaignId) {

        String selection = WildFishingContract.RelayCampaignImageResults.COLUMN_NAME_CAMPAIGN_ID + " = ? ";
        String[] selelectionArgs = { campaignId };

        database.delete(WildFishingContract.RelayCampaignImageResults.TABLE_NAME, selection,
                selelectionArgs);
    }
	
	/**
     * 添加活动渔获统计关系
     */
    public void addRelayCamapignStatisticsResults(String campaignId, List<RelayCamapignStatisticsResult> rcsrList) {

        for(RelayCamapignStatisticsResult rrs : rcsrList){
            ContentValues values = new ContentValues();
            values.put(WildFishingContract.RelayCamapignStatisticsResults.COLUMN_NAME_CAMPAIGN_ID, campaignId);
            values.put(WildFishingContract.RelayCamapignStatisticsResults.COLUMN_NAME_POINT_ID, rrs.getPointId());
            values.put(WildFishingContract.RelayCamapignStatisticsResults.COLUMN_NAME_FISH_TYPE_ID, rrs.getFishTypeId());
            values.put(WildFishingContract.RelayCamapignStatisticsResults.COLUMN_NAME_WEIGHT, rrs.getWeight());
            values.put(WildFishingContract.RelayCamapignStatisticsResults.COLUMN_NAME_COUNT, rrs.getCount());
            values.put(WildFishingContract.RelayCamapignStatisticsResults.COLUMN_NAME_HOOK_FLAG, rrs.getHookFlag());
            
            database.insert(WildFishingContract.RelayCamapignStatisticsResults.TABLE_NAME, null,values);
        }
    }
	
    public List<RelayCamapignStatisticsResult> getRelayCamapignStatisticsResultList(String campaignId) {
        List<RelayCamapignStatisticsResult> retList = new ArrayList<RelayCamapignStatisticsResult>();
        
        StringBuffer  sb = new StringBuffer();
        sb.append("SELECT rcsr._id,rcsr.campaign_id, ");
        sb.append(" rcsr.weight,rcsr.count, ");
        sb.append(" rcsr.hook_flag,rl.name AS rod_length_name,p.depth, ");
        sb.append(" ft.name AS fish_type_name,rcsr.point_id, rcsr.fish_type_id ");
        sb.append("FROM relay_campaign_statistics_results rcsr  ");
        sb.append("INNER JOIN points p ON rcsr.point_id=p._id ");
        sb.append("INNER JOIN rod_lengths rl ON p.rod_length_id=rl._id ");
        sb.append("INNER JOIN fish_types ft ON rcsr.fish_type_id=ft._id ");
        sb.append(" WHERE rcsr.campaign_id=? ");
        sb.append(" order by rcsr._id ");

        
        Cursor c = database.rawQuery(sb.toString(), new String[]{campaignId});

        c.moveToFirst();
        while (!c.isAfterLast()) {
            RelayCamapignStatisticsResult obj = cursorToRelayCamapignStatisticsResult(c);
            retList.add(obj);

            c.moveToNext();
        }
        c.close();
        
        return retList;
    }
    
    public void deleteRelayCamapignStatisticsResult(String campaignId) {

        String selection = WildFishingContract.RelayCamapignStatisticsResults.COLUMN_NAME_CAMPAIGN_ID + " = ? ";
        String[] selelectionArgs = { campaignId };

        database.delete(WildFishingContract.RelayCamapignStatisticsResults.TABLE_NAME, selection,
                selelectionArgs);
    }
    
    public void updateRelayCamapignStatisticsAndImageResult(String campaignId, List<RelayCamapignStatisticsResult> rcsrList, List<String> picList) {

        database.beginTransaction();
        try {
            deleteRelayCamapignStatisticsResult(campaignId);
            addRelayCamapignStatisticsResults(campaignId, rcsrList);
            
            deleteRelayCampaignImageResult(campaignId);
            addRelayCampaignImageResult(campaignId, picList);
            
            database.setTransactionSuccessful();
        } catch (Exception ex) {

        } finally {
            database.endTransaction();
        }
    }
    
    private RelayCamapignStatisticsResult cursorToRelayCamapignStatisticsResult(Cursor c) {
        RelayCamapignStatisticsResult obj = new RelayCamapignStatisticsResult();
        obj.setId(c.getString(0));
        obj.setCampaignId(c.getString(1));
        obj.setWeight(c.getString(2));
        obj.setCount(c.getString(3));
        obj.setHookFlag(c.getString(4));
        obj.setHookFlagName(c.getString(4).equals("in") ? "入护" : "跑鱼");
        obj.setPointName("竿长" + c.getString(5) + "米" + " - 水深" + c.getString(6) + "米");
        obj.setFishTypeName(c.getString(7));
        obj.setPointId(c.getString(8));
        obj.setFishTypeId(c.getString(9));
        
        return obj;
    }
	
	// 鱼种
	public void addFishType(FishType fishType) {

        ContentValues values = new ContentValues();
        values.put(WildFishingContract.FishType.COLUMN_NAME_NAME, fishType.getName());

        database.insert(WildFishingContract.FishType.TABLE_NAME, null, values);
    }
	
	public List<FishType> getAllFishTypes() {
        List<FishType> retList = new ArrayList<FishType>();
        
        String []cols = { WildFishingContract.FishType._ID,
                WildFishingContract.FishType.COLUMN_NAME_NAME};
        
        Cursor c = database.query(WildFishingContract.FishType.TABLE_NAME,
                cols, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
                );

        c.moveToFirst();
        while (!c.isAfterLast()) {
            FishType obj = cursorToFishType(c);
            retList.add(obj);

            c.moveToNext();
        }
        c.close();

        return retList;
    }

    private FishType cursorToFishType(Cursor c) {
        FishType bait = new FishType();
        bait.setId(c.getString(c.getColumnIndex(WildFishingContract.FishType._ID)));
        bait.setName(c.getString(c
                .getColumnIndex(WildFishingContract.FishType.COLUMN_NAME_NAME)));
        return bait;
    }
    
    
    public HashMap<String, String> getFishWeightPerDay(String year) {
        HashMap<String, String> ret = new HashMap<String, String>();
        
        HashMap<String, CampaignSummary> dateHash = new HashMap<String, CampaignSummary>();
        HashMap<String, List<RelayCamapignStatisticsResult>> statisticsHash = new HashMap<String, List<RelayCamapignStatisticsResult>>();
        
        setDateAndStatisticsHashForChartYear(year, dateHash, statisticsHash);
        
        ArrayList<CampaignSummary> tempList = new ArrayList<CampaignSummary>(dateHash.values());
        for(CampaignSummary cc : tempList){
            if (statisticsHash.containsKey(cc.getDate())) {
                String weight = BusinessUtil.getFishWeight(statisticsHash, cc);
                ret.put(cc.getDate(), weight);
            } 
        }        
        
        return ret;
    }
    
    // chart by year
    public HashMap<String, List<String>> getChartByYearData(String year, String bigFishWeight, String fishCount) {
    	HashMap<String, List<String>> ret = new HashMap<String, List<String>>();
        
        HashMap<String, CampaignSummary> dateHash = new HashMap<String, CampaignSummary>();
        HashMap<String, List<RelayCamapignStatisticsResult>> statisticsHash = new HashMap<String, List<RelayCamapignStatisticsResult>>();
        
        setDateAndStatisticsHashForChartYear(year, dateHash, statisticsHash);
        
        List<String> campaignCountList = getCampaignCountList(dateHash);
        List<String> obtainedBigFishCountList = getObtainedBigFishCountList(bigFishWeight, dateHash, statisticsHash);
        List<String> escapedBigFishCountList = getEscapedBigFishCountList(bigFishWeight, dateHash, statisticsHash);
        List<String> notObtainedFishCountList = getNotObtainedFishCountList(dateHash, statisticsHash);
        List<String> fishCountMoreThanCountList = getFishCountMoreThanCountList(fishCount, dateHash, statisticsHash);
        
        ret.put(Constant.CAMPAIGN_COUNT, campaignCountList);
        ret.put(Constant.OBTAINED_FISH_BIGGER_THAN, obtainedBigFishCountList);
        ret.put(Constant.ESCAPED_FISH_BIGGER_THAN, escapedBigFishCountList);
        ret.put(Constant.NOT_OBTAINED_FISH, notObtainedFishCountList);
        ret.put(Constant.FISH_COUNT_MORE_THAN, fishCountMoreThanCountList);
        
        return ret;
    }


    private List<String> getCampaignCountList(HashMap<String, CampaignSummary> dateHash) {
        List<String> campaignCountList = new ArrayList<String>();
        ArrayList<CampaignSummary> tempList = new ArrayList<CampaignSummary>(dateHash.values());
		int[] monthCountArr = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		for (CampaignSummary cc : tempList) {
			String month = cc.getDate().split(Constant.DASH)[1];
			for (int i = 0; i < 12; i++) {
				if (Constant.MONTH_NAMES[i].equals(month)) {
					monthCountArr[i] += 1;
				}
			}

		}  
		
		for (int i = 0; i < 12; i++) {
		    campaignCountList.add(String.valueOf(monthCountArr[i]));
		}
		
		return campaignCountList;
    }

    private List<String> getObtainedBigFishCountList(String bigFishWeight, HashMap<String, CampaignSummary> dateHash, HashMap<String, List<RelayCamapignStatisticsResult>> statisticsHash) {
        List<String> obtainedBigFishCountList = new ArrayList<String>();
        
        ArrayList<CampaignSummary> tempList = new ArrayList<CampaignSummary>(dateHash.values());
        int[] monthCountArr = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for(CampaignSummary cc : tempList){
        	if(!statisticsHash.containsKey(cc.getDate())){
        		continue;
        	}
            int bigFishCount = BusinessUtil.getObatainedBigFishCount(statisticsHash, cc, bigFishWeight);
            if(bigFishCount == 0){
            	continue;
            }
            
            String month = cc.getDate().split(Constant.DASH)[1];
			for (int i = 0; i < 12; i++) {
				if (Constant.MONTH_NAMES[i].equals(month)) {
					monthCountArr[i] += bigFishCount;
				}
			}
        }
        
        for (int i = 0; i < 12; i++) {
            obtainedBigFishCountList.add(String.valueOf(monthCountArr[i]));
		}
        
        return obtainedBigFishCountList;
    }

    private List<String> getEscapedBigFishCountList(String bigFishWeight, HashMap<String, CampaignSummary> dateHash, HashMap<String, List<RelayCamapignStatisticsResult>> statisticsHash) {
        List<String> ret = new ArrayList<String>();
        ArrayList<CampaignSummary> tempList = new ArrayList<CampaignSummary>(dateHash.values());
        int[] monthCountArr = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for(CampaignSummary cc : tempList){
            if(!statisticsHash.containsKey(cc.getDate())){
                continue;
            }
            int bigFishCount = BusinessUtil.getEscapedBigFishCount(statisticsHash, cc, bigFishWeight);
            if(bigFishCount == 0){
                continue;
            }
            
            String month = cc.getDate().split(Constant.DASH)[1];
            for (int i = 0; i < 12; i++) {
                if (Constant.MONTH_NAMES[i].equals(month)) {
                    monthCountArr[i] += bigFishCount;
                }
            }
        }
        
        for (int i = 0; i < 12; i++) {
            ret.add(String.valueOf(monthCountArr[i]));
        }
        return ret;
    }


    private List<String> getNotObtainedFishCountList(HashMap<String, CampaignSummary> dateHash, HashMap<String, List<RelayCamapignStatisticsResult>> statisticsHash) {
        List<String> ret = new ArrayList<String>();
        ArrayList<CampaignSummary> tempList = new ArrayList<CampaignSummary>(dateHash.values());
        int[] monthCountArr = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for(CampaignSummary cc : tempList){
            if(statisticsHash.containsKey(cc.getDate())){
                continue;
            }
            
            
            String month = cc.getDate().split(Constant.DASH)[1];
            for (int i = 0; i < 12; i++) {
                if (Constant.MONTH_NAMES[i].equals(month)) {
                    monthCountArr[i] += 1;
                }
            }
        }
        
        for (int i = 0; i < 12; i++) {
            ret.add(String.valueOf(monthCountArr[i]));
        }
        return ret;
    }

    private List<String> getFishCountMoreThanCountList(String fishCount, HashMap<String, CampaignSummary> dateHash, HashMap<String, List<RelayCamapignStatisticsResult>> statisticsHash) {
        List<String> ret = new ArrayList<String>();
        ArrayList<CampaignSummary> tempList = new ArrayList<CampaignSummary>(dateHash.values());
        int[] monthCountArr = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (CampaignSummary cc : tempList) {
            if (!statisticsHash.containsKey(cc.getDate())) {
                continue;
            }
            int fishCountMoreThan = BusinessUtil.getFishCountMoreThan(statisticsHash, cc, fishCount);
            if (fishCountMoreThan == 0) {
                continue;
            }

            String month = cc.getDate().split(Constant.DASH)[1];
            for (int i = 0; i < 12; i++) {
                if (Constant.MONTH_NAMES[i].equals(month)) {
                    monthCountArr[i] += fishCountMoreThan;
                }
            }
        }

        for (int i = 0; i < 12; i++) {
            ret.add(String.valueOf(monthCountArr[i]));
        }
        return ret;
    }

	private void setDateAndStatisticsHashForChartYear(String year,
			HashMap<String, CampaignSummary> dateHash,
			HashMap<String, List<RelayCamapignStatisticsResult>> statisticsHash) {
		StringBuffer  sb = new StringBuffer();
        sb.append("SELECT c._id,c.start_time, ");
        sb.append(" rcsr.weight ,rcsr.count,rcsr.hook_flag ");
        sb.append("FROM campaigns c  ");
        sb.append("LEFT JOIN relay_campaign_statistics_results rcsr ON rcsr.campaign_id=c._id ");
        sb.append("WHERE c.start_time like '"+year+"%' ");
        sb.append("order by c.start_Time DESC ");

        
        Cursor c = database.rawQuery(sb.toString(), new String[]{});
        
        c.moveToFirst();
        while(!c.isAfterLast()){
            List<RelayCamapignStatisticsResult> rcsrList= null;
            CampaignSummary obj = null;
            RelayCamapignStatisticsResult rcsr = null;
            
            String campaignId = c.getString(0);
            String date = c.getString(1).split(Constant.SPACE)[0];
            String weight = c.getString(2);
            String count = c.getString(3);
            String hookFlag = c.getString(4);
            
            if(!dateHash.containsKey(date)){
                obj = new CampaignSummary();
                obj.setDate(date);
                dateHash.put(date, obj);
            }
            
            if(statisticsHash.containsKey(date)){
                rcsrList = statisticsHash.get(date);
                rcsr = new RelayCamapignStatisticsResult();
                rcsr.setWeight(weight);
                rcsr.setCount(count);
                rcsr.setHookFlag(hookFlag);
                rcsrList.add(rcsr);
                
                statisticsHash.put(campaignId, rcsrList);
            }else{
            	if(weight != null){
	                rcsrList = new ArrayList<RelayCamapignStatisticsResult>();
	                rcsr = new RelayCamapignStatisticsResult();
	                rcsr.setWeight(weight);
	                rcsr.setCount(count);
	                rcsr.setHookFlag(hookFlag);
	                rcsrList.add(rcsr);
	                
	                statisticsHash.put(date, rcsrList);
            	}
            }

            c.moveToNext();
        }
        c.close();
	}
	
	// chart by month
	
    public HashMap<String, HashMap<String, Integer>> getChartByMonthData(String month, String bigFishWeight, String fishCount) {
        HashMap<String, HashMap<String, Integer>> ret = new HashMap<String, HashMap<String, Integer>>();

        List<String> yearList = getYearsList();
        HashMap<String, CampaignSummary> dateHash = new HashMap<String, CampaignSummary>();
        HashMap<String, List<RelayCamapignStatisticsResult>> statisticsHash = new HashMap<String, List<RelayCamapignStatisticsResult>>();

        setDateAndStatisticsHashForChartMonth(month, dateHash, statisticsHash);

        HashMap<String, Integer> campaignCountHash = getCampaignCountHash(dateHash, yearList);
        HashMap<String, Integer> obtainedBigFishCountHash = getObtainedBigFishCountHash(bigFishWeight, dateHash, statisticsHash, yearList);
        HashMap<String, Integer> escapedBigFishCountHash = getEscapedBigFishCountHash(bigFishWeight, dateHash, statisticsHash, yearList);
        HashMap<String, Integer> notObtainedFishCountHash = getNotObtainedFishCountHash(dateHash, statisticsHash, yearList);
        HashMap<String, Integer> fishCountMoreThanCountHash = getFishCountMoreThanCountHash(fishCount, dateHash, statisticsHash, yearList);

        ret.put(Constant.CAMPAIGN_COUNT, campaignCountHash);
        ret.put(Constant.OBTAINED_FISH_BIGGER_THAN, obtainedBigFishCountHash);
        ret.put(Constant.ESCAPED_FISH_BIGGER_THAN, escapedBigFishCountHash);
        ret.put(Constant.NOT_OBTAINED_FISH, notObtainedFishCountHash);
        ret.put(Constant.FISH_COUNT_MORE_THAN, fishCountMoreThanCountHash);

        return ret;
    }

    private HashMap<String, Integer> getCampaignCountHash(HashMap<String, CampaignSummary> dateHash, List<String> yearList) {
        HashMap<String, Integer> ret = new HashMap<String, Integer>();
        initYearCountHash(yearList, ret);

        ArrayList<CampaignSummary> tempList = new ArrayList<CampaignSummary>(dateHash.values());
        for (CampaignSummary cc : tempList) {
            setYearCountHash(yearList, ret, cc);
        }

        return ret;
    }

    private HashMap<String, Integer> getObtainedBigFishCountHash(String bigFishWeight, HashMap<String, CampaignSummary> dateHash, HashMap<String, List<RelayCamapignStatisticsResult>> statisticsHash, List<String> yearList) {
        HashMap<String, Integer> ret = new HashMap<String, Integer>();
        initYearCountHash(yearList, ret);

        ArrayList<CampaignSummary> tempList = new ArrayList<CampaignSummary>(dateHash.values());
        for (CampaignSummary cc : tempList) {
            if (!statisticsHash.containsKey(cc.getDate())) {
                continue;
            }
            int bigFishCount = BusinessUtil.getObatainedBigFishCount(statisticsHash, cc, bigFishWeight);
            if (bigFishCount == 0) {
                continue;
            }

            setYearCountHash(yearList, ret, cc);
        }

        return ret;
    }

    private HashMap<String, Integer> getEscapedBigFishCountHash(String bigFishWeight, HashMap<String, CampaignSummary> dateHash, HashMap<String, List<RelayCamapignStatisticsResult>> statisticsHash, List<String> yearList) {
        HashMap<String, Integer> ret = new HashMap<String, Integer>();
        initYearCountHash(yearList, ret);

        ArrayList<CampaignSummary> tempList = new ArrayList<CampaignSummary>(dateHash.values());
        for (CampaignSummary cc : tempList) {
            if (!statisticsHash.containsKey(cc.getDate())) {
                continue;
            }
            int bigFishCount = BusinessUtil.getEscapedBigFishCount(statisticsHash, cc, bigFishWeight);
            if (bigFishCount == 0) {
                continue;
            }

            setYearCountHash(yearList, ret, cc);
        }

        return ret;
    }

    private HashMap<String, Integer> getNotObtainedFishCountHash(HashMap<String, CampaignSummary> dateHash, HashMap<String, List<RelayCamapignStatisticsResult>> statisticsHash, List<String> yearList) {
        HashMap<String, Integer> ret = new HashMap<String, Integer>();
        initYearCountHash(yearList, ret);

        ArrayList<CampaignSummary> tempList = new ArrayList<CampaignSummary>(dateHash.values());
        for (CampaignSummary cc : tempList) {
            if (statisticsHash.containsKey(cc.getDate())) {
                continue;
            }

            setYearCountHash(yearList, ret, cc);
        }
        return ret;
    }

    private HashMap<String, Integer> getFishCountMoreThanCountHash(String fishCount, HashMap<String, CampaignSummary> dateHash, HashMap<String, List<RelayCamapignStatisticsResult>> statisticsHash, List<String> yearList) {
        HashMap<String, Integer> ret = new HashMap<String, Integer>();
        initYearCountHash(yearList, ret);

        ArrayList<CampaignSummary> tempList = new ArrayList<CampaignSummary>(dateHash.values());
        for (CampaignSummary cc : tempList) {
            if (!statisticsHash.containsKey(cc.getDate())) {
                continue;
            }
            int fishCountMoreThan = BusinessUtil.getFishCountMoreThan(statisticsHash, cc, fishCount);
            if (fishCountMoreThan == 0) {
                continue;
            }

            setYearCountHash(yearList, ret, cc);
        }

        return ret;
    }

    private void initYearCountHash(List<String> yearList, HashMap<String, Integer> ret) {
        for (String year : yearList) {
            ret.put(year, 0);
        }
    }

    private void setYearCountHash(List<String> yearList, HashMap<String, Integer> ret, CampaignSummary cc) {
        String year = cc.getDate().split(Constant.DASH)[0];
        for (String s : yearList) {
            if (s.equals(year)) {
                if (ret.containsKey(year)) {
                    Integer count = ret.get(year) + 1;
                    ret.put(year, count);
                } else {
                    ret.put(year, 1);
                }
                break;
            }
        }
    }

    private void setDateAndStatisticsHashForChartMonth(String month,
            HashMap<String, CampaignSummary> dateHash,
            HashMap<String, List<RelayCamapignStatisticsResult>> statisticsHash) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT c._id,c.start_time, ");
        sb.append(" rcsr.weight ,rcsr.count,rcsr.hook_flag ");
        sb.append("FROM campaigns c  ");
        sb.append("LEFT JOIN relay_campaign_statistics_results rcsr ON rcsr.campaign_id=c._id ");
        sb.append("WHERE c.start_time like '%-" + month + "-%' ");
        sb.append("order by c.start_Time DESC ");

        Cursor c = database.rawQuery(sb.toString(), new String[]{});

        c.moveToFirst();
        while (!c.isAfterLast()) {
            List<RelayCamapignStatisticsResult> rcsrList = null;
            CampaignSummary obj = null;
            RelayCamapignStatisticsResult rcsr = null;

            String campaignId = c.getString(0);
            String date = c.getString(1).split(Constant.SPACE)[0];
            String weight = c.getString(2);
            String count = c.getString(3);
            String hookFlag = c.getString(4);

            if (!dateHash.containsKey(date)) {
                obj = new CampaignSummary();
                obj.setDate(date);
                dateHash.put(date, obj);
            }

            if (statisticsHash.containsKey(date)) {
                rcsrList = statisticsHash.get(date);
                rcsr = new RelayCamapignStatisticsResult();
                rcsr.setWeight(weight);
                rcsr.setCount(count);
                rcsr.setHookFlag(hookFlag);
                rcsrList.add(rcsr);

                statisticsHash.put(campaignId, rcsrList);
            } else {
                rcsrList = new ArrayList<RelayCamapignStatisticsResult>();
                rcsr = new RelayCamapignStatisticsResult();
                rcsr.setWeight(weight);
                rcsr.setCount(count);
                rcsr.setHookFlag(hookFlag);
                rcsrList.add(rcsr);

                statisticsHash.put(date, rcsrList);
            }

            c.moveToNext();
        }
        c.close();
    }
    
    public List<String> getYearsList() {
        List<String> ret = new ArrayList<String>();

        List<Campaign> camList = getAllCampagins();
        for (Campaign obj : camList) {
            String year = obj.getStartTime().substring(0, 4);
            if (!ret.contains(year)) {
                ret.add(year);
            }
        }
        
        Collections.sort(ret, new Comparator<String>() {

            public int compare(String o1, String o2) {
                return Integer.parseInt(o2) - Integer.parseInt(o1);
            }
        });
        
        return ret;
    }
    
}
