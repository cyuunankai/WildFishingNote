package com.simple.wildfishingnote.campaign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.simple.wildfishingnote.R;

public class PlaceDetailActivity extends ActionBarActivity {

	public static final String ID = "id";
	public static final String TITLE = "title";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_detail);
		
		Intent intent = getIntent();
		String id = intent.getStringExtra(this.ID);
		String title = intent.getStringExtra(this.TITLE);
		((TextView) findViewById(R.id.textViewPlaceTitle)).setText(title);
	}

}
