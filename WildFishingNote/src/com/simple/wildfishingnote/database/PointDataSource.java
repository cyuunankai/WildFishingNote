package com.simple.wildfishingnote.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.simple.wildfishingnote.bean.Point;

/**
 * 钓点
 * @author ly
 *
 */
public class PointDataSource {

	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { WildFishingContract.Points._ID,
			WildFishingContract.Points.COLUMN_NAME_PLACE_ID,
			WildFishingContract.Points.COLUMN_NAME_ROD_LENGTH_ID,
			WildFishingContract.Points.COLUMN_NAME_DEPTH,
			WildFishingContract.Points.COLUMN_NAME_LURE_METHOD_ID,
			WildFishingContract.Points.COLUMN_NAME_BAIT_ID};

	public PointDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public Point addPoint(Point point) {
    	
    	ContentValues values = new ContentValues();
    	values.put(WildFishingContract.Points.COLUMN_NAME_PLACE_ID, point.getPlaceId());
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
    	values.put(WildFishingContract.Points.COLUMN_NAME_PLACE_ID, point.getPlaceId());
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
		String selection = WildFishingContract.Points._ID + " = ? ";
		String[] selelectionArgs = { rowId };

		Cursor c = database.query(WildFishingContract.Points.TABLE_NAME, // The table to query
				allColumns, // The columns to return
				selection, // The columns for the WHERE clause
				selelectionArgs, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);

		c.moveToFirst();
		Point point = cursorToPoint(c);
		c.close();
		
		return point;
	}
	
	public List<Point> getPointsForList(String placeId) {
		List<Point> list = new ArrayList<Point>();
		
		StringBuffer  sb = new StringBuffer();
		sb.append("SELECT p._id,p.depth,rl._id AS rl_id,rl.name AS rod_length_name,lm._id AS lm_id,lm.name AS lure_method_name,b._id AS b_id,b.name AS bait_name ");
		sb.append("FROM points p  ");
		sb.append("INNER JOIN rod_lengths rl ON p.rod_length_id=rl._id ");
		sb.append("INNER JOIN lure_methods lm ON p.lure_method_id=lm._id ");
		sb.append("INNER JOIN baits b ON p.bait_id=b._id ");
		sb.append(" WHERE p.place_id=?");

		
		Cursor c = database.rawQuery(sb.toString(), new String[]{String.valueOf(placeId)});
		
		c.moveToFirst();
		while(!c.isAfterLast()){
			Point point = new Point();
			point.setId(c.getString(c.getColumnIndex(WildFishingContract.Points._ID)));
			point.setDepth(c.getString(c.getColumnIndex(WildFishingContract.Points.COLUMN_NAME_DEPTH)));
			point.setRodLengthId(c.getString(c.getColumnIndex("rl_id")));
			point.setRodLengthName(c.getString(c.getColumnIndex("rod_length_name")));
			point.setLureMethodId(c.getString(c.getColumnIndex("lm_id")));
			point.setLureMethodName(c.getString(c.getColumnIndex("lure_method_name")));
			point.setBaitId(c.getString(c.getColumnIndex("b_id")));
			point.setBaitName(c.getString(c.getColumnIndex("bait_name")));
		
			list.add(point);
			c.moveToNext();
		}
		c.close();
		
		return list;
	}
	
	private Point cursorToPoint(Cursor c) {
		Point point = new Point();
		point.setId(c.getString(c.getColumnIndex(WildFishingContract.Points._ID)));
		point.setPlaceId(c.getString(c.getColumnIndex(WildFishingContract.Points.COLUMN_NAME_PLACE_ID)));
		point.setRodLengthId(c.getString(c.getColumnIndex(WildFishingContract.Points.COLUMN_NAME_ROD_LENGTH_ID)));
		point.setDepth(c.getString(c.getColumnIndex(WildFishingContract.Points.COLUMN_NAME_DEPTH)));
		point.setLureMethodId(c.getString(c.getColumnIndex(WildFishingContract.Points.COLUMN_NAME_LURE_METHOD_ID)));
		point.setBaitId(c.getString(c.getColumnIndex(WildFishingContract.Points.COLUMN_NAME_BAIT_ID)));
		return point;
	}
}
