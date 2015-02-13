package com.simple.wildfishingnote.common;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.animation.hover.HoverSwitch;
import com.simple.wildfishingnote.tabs.MainTab1Fragment;
import com.simple.wildfishingnote.tabs.MainTab2Fragment;
import com.simple.wildfishingnote.tabs.MainTab3Fragment;
import com.simple.wildfishingnote.tabs.MainTab4Fragment;

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
        editor.putString("campaign_operation_mode", ""); //add edit
        editor.putString("campaign_id", "");
        editor.putString("campaign_start_time", "");
        editor.putString("campaign_end_time", "");
        editor.putString("campaign_summary", "");
        editor.putString("campaign_place_id", "");
        editor.putStringSet("campaign_point_ids", null);
        editor.putString("campaign_fish_result_obj", null);
        
        editor.putString("btn_click", "false");
        editor.commit();
    }
    
    public static void setCampaignPrefernce(Activity activity, String key, String value) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }
    
    public static void setCampaignPrefernce(Activity activity, String key, Set<String> value) {
        
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }
    
    public static String getCampaignPrefernceString(Activity activity, String key) {
        
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }
    
    public static Set<String> getCampaignPrefernceSet(Activity activity, String key) {
        
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getStringSet(key, new HashSet<String>());
    }
    
    
}
