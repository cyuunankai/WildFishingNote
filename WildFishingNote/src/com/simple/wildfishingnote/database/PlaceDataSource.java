package com.simple.wildfishingnote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.simple.wildfishingnote.bean.Place;

public class PlaceDataSource {

	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { WildFishingContract.Places._ID,
			WildFishingContract.Places.COLUMN_NAME_TITLE,
			WildFishingContract.Places.COLUMN_NAME_DETAIL,
			WildFishingContract.Places.COLUMN_NAME_FILE_NAME };

	public PlaceDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

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
				allColumns, WildFishingContract.Places._ID + " = " + placeId,
				null, null, null, null);
		cursor.moveToFirst();
		Place place = cursorToPlace(cursor);
		cursor.close();

		return place;
	}

	public Cursor getPlaceForPinner() {

		String[] projection = { WildFishingContract.Places._ID,
				WildFishingContract.Places.COLUMN_NAME_TITLE };

		Cursor c = database.query(WildFishingContract.Places.TABLE_NAME, // The
																			// table
																			// to
																			// query
				projection, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);

		return c;
	}

	private Place cursorToPlace(Cursor cursor) {
		Place place = new Place();
		place.setId(cursor.getString(0));
		place.setTitle(cursor.getString(1));
		place.setDetail(cursor.getString(2));
		place.setFileName(cursor.getString(3));
		return place;
	}
}
