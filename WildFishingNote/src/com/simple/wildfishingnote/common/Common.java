package com.simple.wildfishingnote.common;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.simple.wildfishingnote.AddMainActivity;
import com.simple.wildfishingnote.GuideActivity;
import com.simple.wildfishingnote.MainActivity;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.StatisticsActivity;

public class Common {

	public static boolean optionsItemSelectedHandler(MenuItem item, Context context) {
		boolean ret = false;
		
		int id = item.getItemId();
        if (id == R.id.action_main) {
        	Intent intent = new Intent(context, MainActivity.class);
        	context.startActivity(intent);
            
            return true;
        } else if (id == R.id.action_statistics) {
        	Intent intent = new Intent(context, StatisticsActivity.class);
        	context.startActivity(intent);
            
            return true;
        } else if (id == R.id.action_guide) {
        	Intent intent = new Intent(context, GuideActivity.class);
        	context.startActivity(intent);
            
            return true;
        } else if (id == R.id.action_add_today) {
        	Intent intent = new Intent(context, AddMainActivity.class);
        	context.startActivity(intent);
            
            return true;
        } 
        return ret;
	}
}
