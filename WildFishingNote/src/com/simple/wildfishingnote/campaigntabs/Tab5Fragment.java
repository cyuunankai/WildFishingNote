package com.simple.wildfishingnote.campaigntabs;

import java.lang.reflect.Type;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simple.wildfishingnote.AddMainActivity;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.RelayResultStatistics;
import com.simple.wildfishingnote.common.Common;
import com.simple.wildfishingnote.database.CampaignDataSource;

public class Tab5Fragment extends Fragment implements OnClickListener {
    
    private View tab5View;
    private CampaignDataSource dataSource;
    private AddMainActivity addMainActivity;
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
	    tab5View = inflater.inflate(R.layout.activity_tab5, container, false);
	    return tab5View;
    }
	
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            setPreBtn();
            setSaveAllBtn();
            setOperationBtnVisibility();
            
        }
    }
    
    /**
     * 监听所有onClick事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCampaignWeatherPre:
                Common.setCampaignPrefernce(getActivity(), "btn_click", "true");
                ((AddMainActivity)getActivity()).getActionBarReference().setSelectedNavigationItem(3);
                break;
            case R.id.buttonSaveAll:
                String fishResultObjStr = Common.getCampaignPrefernceString(getActivity(), "campaign_fish_result_obj");
                Type type = new TypeToken<List<RelayResultStatistics>>() {}.getType();
                List<RelayResultStatistics> statisticsList = new Gson().fromJson(fishResultObjStr, type);
                for (RelayResultStatistics rrs : statisticsList) {

                }
                
                break;
        }
    }
    
    private void setOperationBtnVisibility() {
        LinearLayout buttonsLayoutCampaignWeatherEdit = (LinearLayout)tab5View.findViewById(R.id.buttonsLayoutCampaignWeatherEdit);
        LinearLayout buttonsLayoutCampaignWeatherAdd = (LinearLayout)tab5View.findViewById(R.id.buttonsLayoutCampaignWeatherAdd);
        String mode = Common.getCampaignPrefernceString(getActivity(), "campaign_operation_mode");
        if (mode.equals("add")) {
            buttonsLayoutCampaignWeatherAdd.setVisibility(View.VISIBLE);
            buttonsLayoutCampaignWeatherEdit.setVisibility(View.INVISIBLE);
        } else if (mode.equals("edit")) {
            buttonsLayoutCampaignWeatherAdd.setVisibility(View.INVISIBLE);
            buttonsLayoutCampaignWeatherEdit.setVisibility(View.VISIBLE);
        }
    }
    
    /**
     * 注册[上一步]按钮事件
     */
    private void setPreBtn() {
        BootstrapButton preBtn = (BootstrapButton)tab5View.findViewById(R.id.buttonCampaignWeatherPre);
        preBtn.setOnClickListener(this);
    }
    
    /**
     * 注册[保存]按钮事件
     */
    private void setSaveAllBtn() {
        BootstrapButton saveAllBtn = (BootstrapButton)tab5View.findViewById(R.id.buttonSaveAll);
        saveAllBtn.setOnClickListener(this);
    }

}
