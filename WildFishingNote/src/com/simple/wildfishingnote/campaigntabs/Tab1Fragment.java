package com.simple.wildfishingnote.campaigntabs;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.simple.wildfishingnote.AddMainActivity;
import com.simple.wildfishingnote.CalendarDailogActivity;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Campaign;
import com.simple.wildfishingnote.database.CampaignDataSource;
import com.simple.wildfishingnote.datetimepicker.DatePickerFragment;
import com.simple.wildfishingnote.datetimepicker.TimePickerFragment;
import com.simple.wildfishingnote.utils.StringUtils;

public class Tab1Fragment extends Fragment implements OnClickListener {

    private View tab1View;
    private CampaignDataSource dataSource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        dataSource = ((AddMainActivity)getActivity()).getCampaignDataSource();
        
        // Inflate the layout for this fragment
        tab1View = inflater.inflate(R.layout.activity_tab1, container, false);
        setStartDateBtn();
        setStartTimeBtn();
        setEndDateBtn();
        setEndTimeBtn();
        setSaveCampaignBtn();
        
        Intent intent = ((AddMainActivity)getActivity()).getIntent();
        if (intent == null) {
            return tab1View;
        }
        String historyDate = intent.getStringExtra(CalendarDailogActivity.HISTORY_DATE);

        // TextView tv = (TextView)view.findViewById(R.id.textView2);
        // tv.setText(historyDate);
        
        

        return tab1View;
    }

    private void setSaveCampaignBtn() {
        BootstrapButton saveCampaignBtn = (BootstrapButton)tab1View.findViewById(R.id.buttonSaveCampagin);
        saveCampaignBtn.setOnClickListener(this);
    }

    private void setStartDateBtn() {
        BootstrapButton dateBtn = (BootstrapButton)tab1View.findViewById(R.id.addCampaignStartDate);
        dateBtn.setOnClickListener(this);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        dateBtn.setText(year + "-" + StringUtils.leftPadTwo(month + 1) + "-" + StringUtils.leftPadTwo(day));
    }

    private void setStartTimeBtn() {
        BootstrapButton timeBtn = (BootstrapButton)tab1View.findViewById(R.id.addCampaignStartTime);
        timeBtn.setOnClickListener(this);

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        timeBtn.setText(StringUtils.leftPadTwo(hour) + ":" + StringUtils.leftPadTwo(minute));
    }
    
    private void setEndDateBtn() {
        BootstrapButton dateBtn = (BootstrapButton)tab1View.findViewById(R.id.addCampaignEndDate);
        dateBtn.setOnClickListener(this);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        dateBtn.setText(year + "-" + StringUtils.leftPadTwo(month + 1) + "-" + StringUtils.leftPadTwo(day));
    }

    private void setEndTimeBtn() {
        BootstrapButton timeBtn = (BootstrapButton)tab1View.findViewById(R.id.addCampaignEndTime);
        timeBtn.setOnClickListener(this);

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        timeBtn.setText(StringUtils.leftPadTwo(hour) + ":" + StringUtils.leftPadTwo(minute));
    }

    @Override
    public void onClick(View v) {
        BootstrapButton b = (BootstrapButton)v;
        switch (v.getId()) {
            case R.id.addCampaignStartDate:
                DialogFragment startDateFragment = new DatePickerFragment(b);
                startDateFragment.show(getActivity().getSupportFragmentManager(), "startDatePicker");
                break;
            case R.id.addCampaignStartTime:
                DialogFragment startTimeFragment = new TimePickerFragment(b);
                startTimeFragment.show(getActivity().getSupportFragmentManager(), "startTimePicker");
                break;
            case R.id.addCampaignEndDate:
                DialogFragment endDateFragment = new DatePickerFragment(b);
                endDateFragment.show(getActivity().getSupportFragmentManager(), "endDatePicker");
                break;
            case R.id.addCampaignEndTime:
                DialogFragment endTimeFragment = new TimePickerFragment(b);
                endTimeFragment.show(getActivity().getSupportFragmentManager(), "endTimePicker");
                break;
            case R.id.buttonSaveCampagin:
                BootstrapButton startDateBtn = (BootstrapButton)tab1View.findViewById(R.id.addCampaignStartDate);
                BootstrapButton startTimeBtn = (BootstrapButton)tab1View.findViewById(R.id.addCampaignStartTime);
                BootstrapButton endDateBtn = (BootstrapButton)tab1View.findViewById(R.id.addCampaignEndDate);
                BootstrapButton endTimeBtn = (BootstrapButton)tab1View.findViewById(R.id.addCampaignEndTime);
                BootstrapEditText summaryEditText = (BootstrapEditText)tab1View.findViewById(R.id.tab1_summary);

                String startDate = startDateBtn.getText().toString();
                String startTime = startTimeBtn.getText().toString();
                String endDate = endDateBtn.getText().toString();
                String endTime = endTimeBtn.getText().toString();

                Campaign campaign = new Campaign();
                campaign.setStartTime(startDate + " " + startTime);
                campaign.setEndTime(endDate + " " + endTime);
                campaign.setSummary(summaryEditText.getText().toString());

                campaign = dataSource.addCampaign(campaign);
                ((AddMainActivity)getActivity()).setCampaignId(campaign.getId());
//                campaignId = campaign.getId();
                break;
        }
    }

}
