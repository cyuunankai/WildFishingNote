package com.simple.wildfishingnote;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.simple.wildfishingnote.common.Common;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.utils.DateUtil;

public class CalendarDailogActivity extends FragmentActivity {

	private CaldroidFragment caldroidFragment;
	private CaldroidFragment dialogCaldroidFragment;
	
//	private void setCustomResourceForDates() {
//		Calendar cal = Calendar.getInstance();
//
//		// Min date is last 7 days
//		cal.add(Calendar.DATE, -18);
//		Date blueDate = cal.getTime();
//
//		// Max date is next 7 days
//		cal = Calendar.getInstance();
//		cal.add(Calendar.DATE, 16);
//		Date greenDate = cal.getTime();
//
//	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_dailog);
		final SimpleDateFormat formatter = new SimpleDateFormat(DateUtil.DATE_FORMAT_YYYY_MM_DD_HYPHEN);
		
		// Setup caldroid fragment
		// **** If you want normal CaldroidFragment, use below line ****
		caldroidFragment = new CaldroidFragment();

		// //////////////////////////////////////////////////////////////////////
		// **** This is to show customized fragment. If you want customized
		// version, uncomment below line ****
//				 caldroidFragment = new CaldroidSampleCustomFragment();

		// Setup arguments

		// If Activity is created after rotation
		if (savedInstanceState != null) {
			caldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
		}
		// If activity is created from fresh
		else {
			Bundle args = new Bundle();
			String historyDate = Common.getHistoryPrefernceString(getApplicationContext(), "history_date");
			String[] arr = historyDate.split(Constant.DASH);
			
			args.putInt(CaldroidFragment.YEAR, Integer.parseInt(arr[0]));
			args.putInt(CaldroidFragment.MONTH, Integer.parseInt(arr[1]));
			
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
			
			args.putString(CaldroidFragment.SELECTED_DATES, historyDate);

			// Uncomment this to customize startDayOfWeek
			// args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
			// CaldroidFragment.TUESDAY); // Tuesday
			caldroidFragment.setArguments(args);
		}

//		setCustomResourceForDates();

		// Attach to the activity
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendar1, caldroidFragment);
		t.commit();
				
		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
//				Toast.makeText(getApplicationContext(), formatter.format(date),
//						Toast.LENGTH_SHORT).show();
				
				String selectedDate = formatter.format(date);
				Common.setHistoryPrefernce(getApplicationContext(), "history_date", selectedDate);
				
				Intent intent = new Intent(getApplicationContext(), AddMainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				intent.putExtra(AddMainActivity.HISTORY_DATE, selectedDate);
				startActivityForResult(intent, Constant.REQUEST_CODE_ADD_CAMPAIGN);
			}

			@Override
			public void onChangeMonth(int month, int year) {
//				String text = "month: " + month + " year: " + year;
//				Toast.makeText(getApplicationContext(), text,
//						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLongClickDate(Date date, View view) {
//				Toast.makeText(getApplicationContext(),
//						"Long click " + formatter.format(date),
//						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onCaldroidViewCreated() {
				
			}

		};
		
		// Setup Caldroid
		caldroidFragment.setCaldroidListener(listener);

	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

	/**
	 * Save current states of the Caldroid here
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (caldroidFragment != null) {
			caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
		}
		
		if (dialogCaldroidFragment != null) {
			dialogCaldroidFragment.saveStatesToKey(outState,
					"DIALOG_CALDROID_SAVED_STATE");
		}
	}
}
