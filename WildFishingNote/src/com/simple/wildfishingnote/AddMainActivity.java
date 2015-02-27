package com.simple.wildfishingnote;

import org.apache.commons.lang.StringUtils;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.simple.wildfishingnote.campaigntabs.Tab1Fragment;
import com.simple.wildfishingnote.campaigntabs.Tab2Fragment;
import com.simple.wildfishingnote.campaigntabs.Tab3Fragment;
import com.simple.wildfishingnote.campaigntabs.Tab4Fragment;
import com.simple.wildfishingnote.campaigntabs.Tab5Fragment;
import com.simple.wildfishingnote.common.Common;
import com.simple.wildfishingnote.database.CampaignDataSource;

public class AddMainActivity extends ActionBarActivity {
	
    public static String CAMPAIGN_ID = "campaign_id";
    
    private MyFragmentStatePagerAdapter mMyFragmentStatePagerAdapter;
    private ViewPager mViewPager;
    private ActionBar bar;
    private CampaignDataSource dataSourceCampaign;
    private Handler mHandler;
    private SharedPreferences sharedPref;
    private String mode;

    public ActionBar getActionBarReference() {
        return bar;
    }
    
    public CampaignDataSource getCampaignDataSource() {
        return dataSourceCampaign;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 肖像模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_add_main);
        
        Intent intent = getIntent();
        String historyDate = intent.getStringExtra(CalendarDailogActivity.HISTORY_DATE);
        String campaginId = intent.getStringExtra(CAMPAIGN_ID);
        
        dataSourceCampaign = new CampaignDataSource(this);
        dataSourceCampaign.open();
        
        initPreference(campaginId);
        initViewPager();
        initActionBar(savedInstanceState);
    }

    private void initActionBar(Bundle savedInstanceState) {
        // 添加tab
        // 设定tab选中监听器
        bar = getActionBar();
        addTabAndSetTabListener(bar);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayShowTitleEnabled(false);
        
        // 初始化手势横向滑动监听器
        initViewPagerPageChangeListener(bar);
        
        if (savedInstanceState != null) {
            // 初始化选中第一个tab
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }
    }

    private void initViewPager() {
        mMyFragmentStatePagerAdapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        
        // 设定适配器
        mViewPager.setAdapter(mMyFragmentStatePagerAdapter);
        
        if (mode.equals("add")) {
            disableViewPagerScroll();
        }
    }

    private void initPreference(String campaginId) {
        Common.initCampaignPrefernce(this);
        if (StringUtils.isNotBlank(campaginId)) {
            Common.setCampaignPrefernce(this, "campaign_operation_mode", "edit");
            Common.setCampaignPrefernce(this, "campaign_id", campaginId);
        } else {
            Common.setCampaignPrefernce(this, "campaign_operation_mode", "add");
        }
        
        // 新规时，编辑用
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        mode = sharedPref.getString("campaign_operation_mode", "");
        mHandler = new Handler();
    }

    private void disableViewPagerScroll() {
        mViewPager.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View arg0, MotionEvent arg1) {
                    return true;
            }
        });
    }
   
    /**
     * 添加tab
     * 设定tab选中监听器
     */
    private void addTabAndSetTabListener(ActionBar bar) {
        
        ActionBar.TabListener tabListener = getTabListener();
        bar.addTab(bar.newTab().setText(R.string.time).setTabListener(tabListener));
        bar.addTab(bar.newTab().setText(R.string.place).setTabListener(tabListener));
        bar.addTab(bar.newTab().setText(R.string.point).setTabListener(tabListener));
        bar.addTab(bar.newTab().setText(R.string.results).setTabListener(tabListener));
        String mode = Common.getCampaignPrefernceString(this, "campaign_operation_mode");
        if (mode.equals("add")) {
            bar.addTab(bar.newTab().setText(R.string.confirm).setTabListener(tabListener));
        }
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
                
                if (mode.equals("edit")) {
                    // 编辑
                    mViewPager.setCurrentItem(tab.getPosition());
                } else {
                    // 新规
                    String btnClick = sharedPref.getString("btn_click", "");
                    if(btnClick.equals("false")){
                        // 点击tab
                        mHandler.postAtFrontOfQueue(new Runnable() {
                            @Override
                            public void run() {
                                bar.setSelectedNavigationItem(0);
                                mViewPager.setCurrentItem(0);
                            }
                        });
                    }else{
                        // 点击下一步按钮
                        mViewPager.setCurrentItem(tab.getPosition());
                    }
                    
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("btn_click", "false");
                    editor.commit();   
                }

            }

            @Override
            public void onTabUnselected(Tab tab,
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
            int tabCount = 5;
            String mode = sharedPref.getString("campaign_operation_mode", "");
            if(mode.equals("edit")) {
                tabCount = 4;
            }
            return tabCount;
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
