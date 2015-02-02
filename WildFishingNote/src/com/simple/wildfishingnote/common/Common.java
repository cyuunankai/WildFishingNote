package com.simple.wildfishingnote.common;

import android.content.Context;
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
}
