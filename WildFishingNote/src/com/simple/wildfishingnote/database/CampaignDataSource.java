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
import com.simple.wildfishingnote.utils.FileUtil;

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

    public void deleteCampaign(Campaign campagin) {
        database.delete(WildFishingContract.Campaigns.TABLE_NAME, WildFishingContract.Campaigns._ID
                + " = " + campagin.getId(), null);
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
        return campaign;
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
    

}
