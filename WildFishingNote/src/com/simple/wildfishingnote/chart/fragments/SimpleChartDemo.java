
package com.simple.wildfishingnote.chart.fragments;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Weather;
import com.simple.wildfishingnote.chart.ChartBase;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.database.CampaignDataSource;
import com.simple.wildfishingnote.database.WeatherDataSource;

/**
 * Demonstrates how to keep your charts straight forward, simple and beautiful with the MPAndroidChart library.
 * 
 * @author Philipp Jahoda
 */
public class SimpleChartDemo extends ChartBase {

    public static String YEAR_MONTH = "year_month";
    private WeatherDataSource dataSource;
    private CampaignDataSource campaignDataSource;
    public HashMap<String, String[]> dateTempcHash;
    public HashMap<String, String> dateFishResultHash;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_awesomedesign);
        
        String yearMonth = getIntent().getStringExtra(YEAR_MONTH);
        initDbValue(yearMonth);
        

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        
        PageAdapter a = new PageAdapter(getSupportFragmentManager());
        pager.setAdapter(a);
        
        setViewPagerCurrentItem(yearMonth, pager);
    }



    private void setViewPagerCurrentItem(String yearMonth, ViewPager pager) {
        String month = yearMonth.split(Constant.DASH)[1];
        int currentItemIndex = Integer.parseInt(month) - 1;
        pager.setCurrentItem(currentItemIndex);
    }
    
    

    private void initDbValue(String yearMonth) {
        String year = yearMonth.split(Constant.DASH)[0];
        dataSource = new WeatherDataSource(this);
        dataSource.open();
        
        dateTempcHash = new HashMap<String, String[]>();
        List<Weather> list = dataSource.getWeathersByYear(year);
        for(Weather obj : list){
            String[] arr = {obj.getMintempC(), obj.getMaxtempC()};
            dateTempcHash.put(obj.getDate(), arr);
        }
        
        campaignDataSource = new CampaignDataSource(this);
        campaignDataSource.open();
        dateFishResultHash = campaignDataSource.getFishWeightPerDay(year);
    }
       
    private class PageAdapter extends FragmentPagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm); 
        }

        @Override
        public Fragment getItem(int pos) {  
            Fragment f = null;
            
            switch(pos) {
            case 0:
                f = Month1Fragment.newInstance();
                break;
            case 1:
                f = Month2Fragment.newInstance();
                break;
            case 2:
                f = Month3Fragment.newInstance();
                break;
            case 3:
                f = Month4Fragment.newInstance();
                break;
            case 4:
                f = Month5Fragment.newInstance();
                break;
            case 5:
                f = Month6Fragment.newInstance();
                break;
            case 6:
                f = Month7Fragment.newInstance();
                break;
            case 7:
                f = Month8Fragment.newInstance();
                break;
            case 8:
                f = Month9Fragment.newInstance();
                break;
            case 9:
                f = Month10Fragment.newInstance();
                break;
            case 10:
                f = Month11Fragment.newInstance();
                break;
            case 11:
                f = Month12Fragment.newInstance();
                break;
            }

            return f;
        }

        @Override
        public int getCount() {
            return 12;
        }       
    }
    
    @Override
    protected void onResume() {
      dataSource.open();
      campaignDataSource.open();
      super.onResume();
    }

    @Override
    protected void onPause() {
      dataSource.close();
      campaignDataSource.close();
      super.onPause();
    }
    
}
