package com.simple.wildfishingnote;

import org.holoeverywhere.widget.Toast;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.simple.wildfishingnote.bean.Campaign;
import com.simple.wildfishingnote.database.CampaignDataSource;
import com.simple.wildfishingnote.tabs.Tab1Fragment;
import com.simple.wildfishingnote.tabs.Tab2Fragment;
import com.simple.wildfishingnote.tabs.Tab3Fragment;
import com.simple.wildfishingnote.tabs.Tab4Fragment;
import com.simple.wildfishingnote.tabs.Tab5Fragment;
import com.simple.wildfishingnote.tabs.Tab2Fragment.PlaceArrayAdapter;

public class AddMainActivity extends ActionBarActivity {

    private MyFragmentStatePagerAdapter mMyFragmentStatePagerAdapter;
    private ViewPager mViewPager;
    private ActionBar bar;
    private CampaignDataSource dataSourceCampaign;
    private long campaignId;
    
    public CampaignDataSource getCampaignDataSource(){
    	return dataSourceCampaign;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 肖像模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_add_main);
        
        campaignId = 0;
        
        dataSourceCampaign = new CampaignDataSource(this);
        dataSourceCampaign.open();
        
        Intent intent = getIntent();
        String historyDate = intent.getStringExtra(CalendarDailogActivity.HISTORY_DATE);
        
        mMyFragmentStatePagerAdapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        
        // 设定适配器
        mViewPager.setAdapter(mMyFragmentStatePagerAdapter);

        // 添加tab
        // 设定tab选中监听器
        bar = getActionBar();
        addTabAndSetTabListener(bar);
        
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayShowTitleEnabled(false);

        
        // 初始化手势横向滑动监听器
        initViewPagerPageChangeListener(bar);
        
        if (savedInstanceState != null) {
            // 初始化选中第一个tab
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }
    }
    
    public void onSaveBtnClickListener(View v) {
    	int currentTabIndex = bar.getSelectedNavigationIndex();
    	if(currentTabIndex == 0) {
	        BootstrapButton startDateBtn = (BootstrapButton) findViewById(R.id.startDate);
	        BootstrapButton startTimeBtn = (BootstrapButton) findViewById(R.id.startTime);
	        BootstrapButton endDateBtn = (BootstrapButton) findViewById(R.id.endDate);
	        BootstrapButton endTimeBtn = (BootstrapButton) findViewById(R.id.endTime);
	        BootstrapEditText summaryEditText = (BootstrapEditText)findViewById(R.id.tab1_summary);
	        
	        String startDate = startDateBtn.getText().toString();
	        String startTime = startTimeBtn.getText().toString();
	        String endDate = endDateBtn.getText().toString();
	        String endTime = endTimeBtn.getText().toString();
	        
	        Campaign campaign = new Campaign();
	        campaign.setStartTime(startDate + " " + startTime);
	        campaign.setEndTime(endDate + " " + endTime);
	        campaign.setSummary(summaryEditText.getText().toString());
	        
	        campaign = dataSourceCampaign.addCampaign(campaign);
	        campaignId = campaign.getId();
    	} else if(currentTabIndex == 1) {
    		ListView listViewPlace = (ListView)findViewById(R.id.listViewPlace);
    		PlaceArrayAdapter adapter = (PlaceArrayAdapter)listViewPlace.getAdapter();
    		Toast.makeText(this, adapter.getSelectedId(), Toast.LENGTH_SHORT).show();
    	}
        
    }
    
    /**
     * 添加tab
     * 设定tab选中监听器
     */
    private void addTabAndSetTabListener(ActionBar bar){
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        ActionBar.TabListener tabListener = getTabListener();
        bar.addTab(bar.newTab().setText(R.string.place).setTabListener(tabListener));
        bar.addTab(bar.newTab().setText(R.string.point).setTabListener(tabListener));
        bar.addTab(bar.newTab().setText(R.string.results).setTabListener(tabListener));
        bar.addTab(bar.newTab().setText(R.string.weather).setTabListener(tabListener));
        bar.addTab(bar.newTab().setText(R.string.time).setTabListener(tabListener));
    }
    
    /**
     * 点击tab时
     * ActionBar.TabListener -> onTabSelected -> ViewPager.setCurrentItem -> FragmentStatePagerAdapter.getItem被触发
     */
    private ActionBar.TabListener getTabListener() {
        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabReselected(Tab arg0,
                    android.app.FragmentTransaction arg1) {

            }

            @Override
            public void onTabSelected(Tab tab,
                    android.app.FragmentTransaction arg1) {
                
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(Tab arg0,
                    android.app.FragmentTransaction arg1) {

            }

        };
        
        return tabListener;
    }
    
    /**
     * 初始化手势横向滑动监听器
     */
    private void initViewPagerPageChangeListener(final ActionBar bar) {
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between pages, select the
                // corresponding tab.
                bar.setSelectedNavigationItem(position);
            }
        });
    }

    // Since this is an object collection, use a FragmentStatePagerAdapter,
    // and NOT a FragmentPagerAdapter.
    public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

        public MyFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            
            switch (i) {
            case 0:
                return new Tab1Fragment();
            case 1:
                return new Tab2Fragment();
            case 2:
                return new Tab3Fragment();
            case 3:
                return new Tab4Fragment();
            case 4:
                return new Tab5Fragment();

            }

            return null;
        }

        @Override
        public int getCount() {
            // tab数量
            return 5;
        }

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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	return super.onOptionsItemSelected(item);
    }
}
