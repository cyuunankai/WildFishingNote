package com.simple.wildfishingnote;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.simple.wildfishingnote.tabs.Tab1Fragment;
import com.simple.wildfishingnote.tabs.Tab2Fragment;
import com.simple.wildfishingnote.tabs.Tab3Fragment;
import com.simple.wildfishingnote.tabs.Tab4Fragment;
import com.simple.wildfishingnote.tabs.Tab5Fragment;

public class AddMainActivity extends ActionBarActivity {

    private MyFragmentStatePagerAdapter mMyFragmentStatePagerAdapter;
    ViewPager mViewPager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 肖像模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_add_main);
        
        mMyFragmentStatePagerAdapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        
        // 设定适配器
        mViewPager.setAdapter(mMyFragmentStatePagerAdapter);

        // 添加tab
        // 设定tab选中监听器
        final ActionBar bar = getActionBar();
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
    
    /**
     * 添加tab
     * 设定tab选中监听器
     */
    private void addTabAndSetTabListener(ActionBar bar){
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        ActionBar.TabListener tabListener = getTabListener();
        bar.addTab(bar.newTab().setText(R.string.time).setTabListener(tabListener));
        bar.addTab(bar.newTab().setText(R.string.place).setTabListener(tabListener));
        bar.addTab(bar.newTab().setText(R.string.point).setTabListener(tabListener));
        bar.addTab(bar.newTab().setText(R.string.results).setTabListener(tabListener));
        bar.addTab(bar.newTab().setText(R.string.weather).setTabListener(tabListener));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	return super.onOptionsItemSelected(item);
    }
}
