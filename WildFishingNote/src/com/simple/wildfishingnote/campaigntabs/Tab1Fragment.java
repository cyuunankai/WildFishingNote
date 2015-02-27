package com.simple.wildfishingnote.campaigntabs;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.simple.wildfishingnote.AddMainActivity;
import com.simple.wildfishingnote.CalendarDailogActivity;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Campaign;
import com.simple.wildfishingnote.common.Common;
import com.simple.wildfishingnote.common.Constant;
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
        setCancelCampaignTimeBtn();
        setSaveCampaignTimeBtn();
        setCampaignTimeNext();
        
        setOperationBtnVisibility();
        
        initViewByDb();
        
        Intent intent = ((AddMainActivity)getActivity()).getIntent();
        if (intent == null) {
            return tab1View;
        }
        String historyDate = intent.getStringExtra(CalendarDailogActivity.HISTORY_DATE);
        
        return tab1View;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (getActivity() != null) {
             // 第一个tab特殊处理
                initViewByPreference();
            }
        }
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
            case R.id.buttonCancelCampaignTime:
                Intent intent = getActivity().getIntent();
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();

                break;
            case R.id.buttonSaveCampaginTime:
                updateCampaignTime();

                break;
            case R.id.buttonCampaginTimeNext:
                
                setCampaignTimeToPreference();
                Common.setCampaignPrefernce(getActivity(), "btn_click", "true");
                ((AddMainActivity)getActivity()).getActionBarReference().setSelectedNavigationItem(1);
                
                break;
        }
    }

    /**
     * [钓位画面]点上一步时处理
     */
    private void initViewByPreference() {
        String mode = Common.getCampaignPrefernceString(getActivity(), "campaign_operation_mode");
        if (mode.equals("add")) {
            String summary = Common.getCampaignPrefernceString(getActivity(), "campaign_summary");
            String startTime = Common.getCampaignPrefernceString(getActivity(), "campaign_start_time");
            String endTime = Common.getCampaignPrefernceString(getActivity(), "campaign_end_time");

            initView(summary, startTime, endTime);
        }
    }

    private void initView(String summary, String startTime, String endTime) {
        BootstrapButton startDateBtn = (BootstrapButton)tab1View.findViewById(R.id.addCampaignStartDate);
        BootstrapButton startTimeBtn = (BootstrapButton)tab1View.findViewById(R.id.addCampaignStartTime);
        BootstrapButton endDateBtn = (BootstrapButton)tab1View.findViewById(R.id.addCampaignEndDate);
        BootstrapButton endTimeBtn = (BootstrapButton)tab1View.findViewById(R.id.addCampaignEndTime);
        BootstrapEditText summaryEditText = (BootstrapEditText)tab1View.findViewById(R.id.tab1_summary);
        if (summary != null) {
            summaryEditText.setText(summary);
        }
        if (startTime != null) {
            startDateBtn.setText(startTime.split(Constant.SPACE)[0]);
            startTimeBtn.setText(startTime.split(Constant.SPACE)[1]);
        }
        if (endTime != null) {
            endDateBtn.setText(endTime.split(Constant.SPACE)[0]);
            endTimeBtn.setText(endTime.split(Constant.SPACE)[1]);
        }
    }
    
    private void initViewByDb() {
        String mode = Common.getCampaignPrefernceString(getActivity(), "campaign_operation_mode");
        if (mode.equals("edit")) {
            String campaignId = Common.getCampaignPrefernceString(getActivity(), "campaign_id");
            Campaign obj = dataSource.getCampaignById(campaignId);
            String summary = obj.getSummary();
            String startTime = obj.getStartTime();
            String endTime = obj.getEndTime();
            initView(summary, startTime, endTime);
        }
    }

    private void setOperationBtnVisibility() {
        LinearLayout layoutAdd = (LinearLayout)tab1View.findViewById(R.id.buttonsLayoutCampaignTimeAdd);
        LinearLayout layoutEdit = (LinearLayout)tab1View.findViewById(R.id.buttonsLayoutCampaignTimeEdit);
        String mode = Common.getCampaignPrefernceString(getActivity(), "campaign_operation_mode");
        if (mode.equals("add")) {
            layoutAdd.setVisibility(View.VISIBLE);
            layoutEdit.setVisibility(View.INVISIBLE);
        } else if (mode.equals("edit")) {
            layoutAdd.setVisibility(View.INVISIBLE);
            layoutEdit.setVisibility(View.VISIBLE);
        }
    }
    
    private void setCampaignTimeNext() {
        BootstrapButton nextBtn = (BootstrapButton)tab1View.findViewById(R.id.buttonCampaginTimeNext);
        nextBtn.setOnClickListener(this);
    }
    
    private void setCancelCampaignTimeBtn() {
        BootstrapButton btn = (BootstrapButton)tab1View.findViewById(R.id.buttonCancelCampaignTime);
        btn.setOnClickListener(this);
    }

    private void setSaveCampaignTimeBtn() {
        BootstrapButton saveBtn = (BootstrapButton)tab1View.findViewById(R.id.buttonSaveCampaginTime);
        saveBtn.setOnClickListener(this);
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

    

    private void setCampaignTimeToPreference() {
        Campaign campaign = setViewValueToBean(new Campaign());
        
        Common.setCampaignPrefernce(getActivity(), "campaign_start_time", campaign.getStartTime());
        Common.setCampaignPrefernce(getActivity(), "campaign_end_time", campaign.getEndTime());
        Common.setCampaignPrefernce(getActivity(), "campaign_summary", campaign.getSummary());
    }

    private void updateCampaignTime() {
        String campaignId = Common.getCampaignPrefernceString(getActivity(), "campaign_id");
        Campaign campaign = dataSource.getCampaignById(campaignId);
        campaign = setViewValueToBean(campaign);
        campaign.setId(campaignId);
        dataSource.updateCampaign(campaign);
        
        Intent intent = getActivity().getIntent();
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private Campaign setViewValueToBean(Campaign campaign) {
        BootstrapButton startDateBtn = (BootstrapButton)tab1View.findViewById(R.id.addCampaignStartDate);
        BootstrapButton startTimeBtn = (BootstrapButton)tab1View.findViewById(R.id.addCampaignStartTime);
        BootstrapButton endDateBtn = (BootstrapButton)tab1View.findViewById(R.id.addCampaignEndDate);
        BootstrapButton endTimeBtn = (BootstrapButton)tab1View.findViewById(R.id.addCampaignEndTime);
        BootstrapEditText summaryEditText = (BootstrapEditText)tab1View.findViewById(R.id.tab1_summary);

        String startDate = startDateBtn.getText().toString();
        String startTime = startTimeBtn.getText().toString();
        String endDate = endDateBtn.getText().toString();
        String endTime = endTimeBtn.getText().toString();

        campaign.setStartTime(startDate + Constant.SPACE + startTime);
        campaign.setEndTime(endDate + Constant.SPACE + endTime);
        campaign.setSummary(summaryEditText.getText().toString());
        return campaign;
    }

}
