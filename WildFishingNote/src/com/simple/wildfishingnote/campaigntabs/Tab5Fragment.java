package com.simple.wildfishingnote.campaigntabs;

import java.lang.reflect.Type;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.RelayResultStatistics;
import com.simple.wildfishingnote.common.Common;

public class Tab5Fragment extends Fragment {
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_tab5, container, false);
    }
	
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            String fishResultObjStr = Common.getCampaignPrefernceString(getActivity(), "campaign_fish_result_obj");
            Type type = new TypeToken<List<RelayResultStatistics>>() {}.getType();
            List<RelayResultStatistics> statisticsList = new Gson().fromJson(fishResultObjStr, type);
            for (RelayResultStatistics rrs : statisticsList) {

            }
        }
    }

}
