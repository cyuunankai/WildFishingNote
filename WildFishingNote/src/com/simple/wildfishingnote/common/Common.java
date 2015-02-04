package com.simple.wildfishingnote.common;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.MenuItem;
import android.widget.Spinner;

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
	
	public static void initSpinner(Context context, Spinner s, Cursor c, String[] from){


        // create an array of the display item we want to bind our data to
        int[] to = new int[] { android.R.id.text1 };
        // create simple cursor adapter
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context,
                android.R.layout.simple_spinner_item, c, from, to, 0);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // get reference to our spinner
        s.setAdapter(adapter);
	}
	
    public static void setSpinnerSelection(String selectedId, Spinner s, Cursor c, String colNameId) {
        int position = 0;
        c.moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(colNameId)).equals(selectedId)) {
                break;
            }
            position++;
            c.moveToNext();
        }
        s.setSelection(position);
    }
}
