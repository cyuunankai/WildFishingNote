package com.simple.wildfishingnote.campaign;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Place;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.database.CampaignDataSource;

public class PlaceDetailActivity extends ActionBarActivity {

    private CampaignDataSource dataSourceCampaign;
    
	public static final String ID = "id";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_detail);
		
		dataSourceCampaign = new CampaignDataSource(this);
        dataSourceCampaign.open();
		
		Intent intent = getIntent();
		String placeId = intent.getStringExtra(ID);
		initPlaceDetail(placeId);
	}

    private void initPlaceDetail(String placeId) {
        Place place = dataSourceCampaign.getPlaceById(placeId);
        
        BootstrapEditText etTextTitle = (BootstrapEditText) findViewById(R.id.placeDetailTitle);
        BootstrapEditText etTextDetail = (BootstrapEditText) findViewById(R.id.placeDetailDetail);
        
        etTextTitle.setText(place.getTitle());
        etTextDetail.setText(place.getDetail());
        
        etTextTitle.setEnabled(false);
        etTextDetail.setEnabled(false);
        
		ImageView imageView = ((ImageView) findViewById(R.id.placeDetailImage));
		String path = getApplicationContext().getFilesDir() + Constant.PLACE_IMAGE_PATH;
		imageView.setImageBitmap(BitmapFactory.decodeFile(path + place.getFileName()));
    }
	
	@Override
    protected void onResume() {
      dataSourceCampaign.open();
      super.onResume();
    }

    @Override
    protected void onPause() {
      dataSourceCampaign.close();
      super.onPause();
    }

}
