package com.simple.wildfishingnote.common;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.animation.hover.HoverSwitch;
import com.simple.wildfishingnote.tabs.MainTab1Fragment;
import com.simple.wildfishingnote.tabs.MainTab2Fragment;
import com.simple.wildfishingnote.tabs.MainTab3Fragment;
import com.simple.wildfishingnote.tabs.MainTab4Fragment;
import com.simple.wildfishingnote.utils.StringUtils;

public class Common {

	public static boolean optionsItemSelectedHandler(MenuItem item, Context context, android.support.v4.app.FragmentManager fm, final HoverSwitch hoverSwitch) {
		boolean ret = false;
		
	   	int id = item.getItemId();
        if (id == R.id.action_main) {
        	// Create fragment and give it an argument specifying the article it should show
        	MainTab1Fragment newFragment = new MainTab1Fragment();
//        	Bundle args = new Bundle();
//        	args.putInt(ArticleFragment.ARG_POSITION, position);
//        	newFragment.setArguments(args);

        	FragmentTransaction transaction = fm.beginTransaction();

        	// Replace whatever is in the fragment_container view with this fragment,
        	// and add the transaction to the back stack so the user can navigate back
        	transaction.replace(R.id.fragment_container, newFragment);
//        	transaction.addToBackStack(null);

        	// Commit the transaction
        	transaction.commit();
        	
        	hoverSwitch.execRootImageClickZeroSecondDuration(true);
        	hoverSwitch.showRoot();
        	
        	
            return true;
        } else if (id == R.id.action_statistics) {
        	// Create fragment and give it an argument specifying the article it should show
        	MainTab2Fragment newFragment = new MainTab2Fragment();

        	FragmentTransaction transaction = fm.beginTransaction();

        	transaction.replace(R.id.fragment_container, newFragment);

        	// Commit the transaction
        	transaction.commit();

        	hoverSwitch.hideRootAndItems();
            return true;
        } else if (id == R.id.action_guide) {
        	// Create fragment and give it an argument specifying the article it should show
        	MainTab3Fragment newFragment = new MainTab3Fragment();

        	FragmentTransaction transaction = fm.beginTransaction();

        	transaction.replace(R.id.fragment_container, newFragment);

        	// Commit the transaction
        	transaction.commit();

        	hoverSwitch.hideRootAndItems();
            return true;
        } else if (id == R.id.action_add_today) {
        	// Create fragment and give it an argument specifying the article it should show
        	MainTab4Fragment newFragment = new MainTab4Fragment();

        	FragmentTransaction transaction = fm.beginTransaction();

        	transaction.replace(R.id.fragment_container, newFragment);

        	// Commit the transaction
        	transaction.commit();

        	hoverSwitch.hideRootAndItems();
            return true;
        }
        return ret;
	}
	
    public static void initCampaignPrefernce(Activity activity) {
        
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("campaign_operation_mode", null); //add edit
        editor.putString("campaign_id", null);
        editor.putString("campaign_start_time", null);
        editor.putString("campaign_end_time", null);
        editor.putString("campaign_summary", null);
        editor.putString("campaign_place_id", null);
        editor.putString("campaign_point_id_list", null);
        editor.putString("campaign_fish_result_pic_list", null);
        editor.putString("campaign_fish_result_statistics_list", null);
        
        editor.putString("btn_click", "false");
        editor.commit();
    }
    
    public static void initHistoryDate(Context context) {
        
        SharedPreferences sharedPref = context.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        editor.putString("history_date", year + Constant.DASH + StringUtils.leftPadTwo(month + 1) + Constant.DASH + StringUtils.leftPadTwo(day)); 
        editor.commit();
    }
    
    public static void setHistoryPrefernce(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }
    
    public static String getHistoryPrefernceString(Context context, String key) {
        
        SharedPreferences sharedPref = context.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }
    
    public static void setCampaignPrefernce(Activity activity, String key, String value) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }
    
    public static String getCampaignPrefernceString(Activity activity, String key) {
        
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }
    
    public static List<String> getCampaignPrefernceStrList(Activity activity, String key) {
        List<String> list = new ArrayList<String>();

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String strList = sharedPref.getString(key, "");
        if ("".equals(strList)) {
            return list;
        }
        
        Type strListType = new TypeToken<List<String>>() {}.getType();
        list = new Gson().fromJson(strList, strListType);
        
        return list;
    }

    
}
