package com.simple.wildfishingnote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.simple.wildfishingnote.bean.LureMethod;

/**
 * 打窝
 * 
 * @author ly
 * 
 */
public class LureMethodDataSource {

	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { WildFishingContract.LureMethods._ID,
			WildFishingContract.LureMethods.COLUMN_NAME_NAME,
			WildFishingContract.LureMethods.COLUMN_NAME_DETAIL };

	public LureMethodDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

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
				allColumns, // The columns to return
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

	public Cursor getLureMethodForPinner() {

		String[] projection = { WildFishingContract.LureMethods._ID,
				WildFishingContract.LureMethods.COLUMN_NAME_NAME };

		Cursor c = database.query(WildFishingContract.LureMethods.TABLE_NAME, 
				projection, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);

		return c;
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

}
