package com.simple.wildfishingnote.campaign.point;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Point;
import com.simple.wildfishingnote.database.CampaignDataSource;

public class PointDetailActivity extends ActionBarActivity {

    public static final String ID = "id";
    private CampaignDataSource dataSource;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_detail);
        
        dataSource = new CampaignDataSource(this);
        dataSource.open();
        
        Intent intent = getIntent();
        String pointId = intent.getStringExtra(ID);
        initPointDetail(pointId);
    }
    
    private void initPointDetail(String pointId) {
        Point point = dataSource.getPointById(pointId);
        
        BootstrapEditText etRodLengthName = (BootstrapEditText) findViewById(R.id.pointDetailRodLengthName);
        BootstrapEditText etLureMethodName = (BootstrapEditText) findViewById(R.id.pointDetailLureMethodName);
        BootstrapEditText etLureMethodDetail = (BootstrapEditText) findViewById(R.id.pointDetailLureMethodDetail);
        BootstrapEditText etBaitName = (BootstrapEditText) findViewById(R.id.pointDetailBaitName);
        BootstrapEditText etBaitDetail = (BootstrapEditText) findViewById(R.id.pointDetailBaitDetail);
        BootstrapEditText etDepth = (BootstrapEditText) findViewById(R.id.pointDetailDepth);
        
        etRodLengthName.setText(point.getRodLengthName());
        etLureMethodName.setText(point.getLureMethodName());
        etLureMethodDetail.setText(point.getLureMethodDetail());
        etBaitName.setText(point.getBaitName());
        etBaitDetail.setText(point.getBaitDetail());
        etDepth.setText(point.getDepth());
        
        etRodLengthName.setEnabled(false);
        etLureMethodName.setEnabled(false);
        etLureMethodDetail.setEnabled(false);
        etLureMethodName.setEnabled(false);
        etBaitName.setEnabled(false);
        etDepth.setEnabled(false);
    }
    
    @Override
    protected void onResume() {
      dataSource.open();
      super.onResume();
    }

    @Override
    protected void onPause() {
      dataSource.close();
      super.onPause();
    }

}
