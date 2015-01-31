package com.simple.wildfishingnote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.simple.wildfishingnote.bean.Bait;

/**
 * 饵料
 * 
 * @author ly
 * 
 */
public class BaitDataSource {

	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { WildFishingContract.Baits._ID,
			WildFishingContract.Baits.COLUMN_NAME_NAME,
			WildFishingContract.Baits.COLUMN_NAME_DETAIL };

	public BaitDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

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
				allColumns, // The columns to return
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

	public Cursor getBaitForPinner() {

		String[] projection = { WildFishingContract.Baits._ID,
				WildFishingContract.Baits.COLUMN_NAME_NAME };

		Cursor c = database.query(WildFishingContract.Baits.TABLE_NAME, 
				projection, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);

		return c;
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
}
