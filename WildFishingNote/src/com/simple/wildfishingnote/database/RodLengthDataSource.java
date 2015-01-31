package com.simple.wildfishingnote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.simple.wildfishingnote.bean.RodLength;

/**
 * 竿长
 * 
 * @author ly
 * 
 */
public class RodLengthDataSource {

	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { WildFishingContract.RodLengths._ID,
			WildFishingContract.RodLengths.COLUMN_NAME_NAME };

	public RodLengthDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

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
				allColumns, // The columns to return
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

	public Cursor getRodLengthForPinner() {

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

		return c;
	}

	private RodLength cursorToRodLength(Cursor cursor) {
		RodLength obj = new RodLength();
		obj.setId(cursor.getString(0));
		obj.setName(cursor.getString(1));
		return obj;
	}
}
