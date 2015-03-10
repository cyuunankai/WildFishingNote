package com.simple.wildfishingnote.tabs;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.FishCount;
import com.simple.wildfishingnote.bean.FishWeight;
import com.simple.wildfishingnote.chart.ChartByMonthActivity;
import com.simple.wildfishingnote.chart.ChartByYearActivity;
import com.simple.wildfishingnote.chart.fragments.SimpleChartDemo;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.database.CampaignDataSource;
import com.simple.wildfishingnote.utils.BusinessUtil;

public class MainTab3Fragment extends Fragment implements OnClickListener {

    private View tab3View;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tab3View = inflater.inflate(R.layout.activity_main_tab3, container, false);

        initSpinner();
        addBtnOnClickListener();

        return tab3View;
    }

    @Override
    public void onClick(View v) {
        Spinner spinnerFishWeight = (Spinner)tab3View.findViewById(R.id.spinnerFishWeight);
        String fishWeight = ((FishWeight)spinnerFishWeight.getSelectedItem()).getWeight();
        Spinner spinnerFishCount = (Spinner)tab3View.findViewById(R.id.spinnerFishCount);
        String fishCount = ((FishCount)spinnerFishCount.getSelectedItem()).getCount();
        
        switch (v.getId()) {
            case R.id.btnByYear:
                Spinner spinnerYear = (Spinner)tab3View.findViewById(R.id.spinnerYear);
                String year = spinnerYear.getSelectedItem().toString();

                Intent intentChartByYear = new Intent(getActivity(), ChartByYearActivity.class);
                intentChartByYear.putExtra(ChartByYearActivity.YEAR, year);
                intentChartByYear.putExtra(ChartByYearActivity.BIG_FISH_WEIGHT, fishWeight);
                intentChartByYear.putExtra(ChartByYearActivity.FISH_COUNT, fishCount);
                getActivity().startActivity(intentChartByYear);
                break;
            case R.id.btnByMonth:
                Intent intentChartByMonth = new Intent(getActivity(), ChartByMonthActivity.class);
                intentChartByMonth.putExtra(ChartByYearActivity.BIG_FISH_WEIGHT, fishWeight);
                intentChartByMonth.putExtra(ChartByYearActivity.FISH_COUNT, fishCount);
                getActivity().startActivity(intentChartByMonth);
                break;
            case R.id.btnByYearDetail:
                Spinner spinnerDetailYear = (Spinner)tab3View.findViewById(R.id.spinnerDetailYear);
                Spinner spinnerDetailMonth = (Spinner)tab3View.findViewById(R.id.spinnerDetailMonth);
                String detailYear = spinnerDetailYear.getSelectedItem().toString();
                String detailMonth = spinnerDetailMonth.getSelectedItem().toString();

                Intent intentChartByYearDetail = new Intent(getActivity(), SimpleChartDemo.class);
                intentChartByYearDetail.putExtra(SimpleChartDemo.YEAR_MONTH, detailYear + Constant.DASH + detailMonth);
                getActivity().startActivity(intentChartByYearDetail);
                break;
        }
    }

    private void addBtnOnClickListener() {
        BootstrapButton btnByYear = (BootstrapButton)tab3View.findViewById(R.id.btnByYear);
        btnByYear.setOnClickListener(this);

        BootstrapButton btnByMonth = (BootstrapButton)tab3View.findViewById(R.id.btnByMonth);
        btnByMonth.setOnClickListener(this);

        BootstrapButton btnByYearDetail = (BootstrapButton)tab3View.findViewById(R.id.btnByYearDetail);
        btnByYearDetail.setOnClickListener(this);

    }

    private void initSpinner() {
        initFishWeightSpinner(R.id.spinnerFishWeight);
        initFishCountSpinner(R.id.spinnerFishCount);
        initYearSpinner(R.id.spinnerYear);
        initMonthSpinner(R.id.spinnerMonth);
        initYearSpinner(R.id.spinnerDetailYear);
        initMonthSpinner(R.id.spinnerDetailMonth);
    }

    private void initFishWeightSpinner(int viewId) {

        List<FishWeight> list = new ArrayList<FishWeight>();
        for (int i = 0; i < Constant.SEARCH_FISH_WEIGHT.length; i++) {
            FishWeight fw = new FishWeight();
            fw.setWeight(String.valueOf(Constant.SEARCH_FISH_WEIGHT[i]));
            fw.setWeightName(BusinessUtil.getFishUnit(Constant.SEARCH_FISH_WEIGHT[i]));
            list.add(fw);
        }

        Spinner s = (Spinner)tab3View.findViewById(viewId);
        ArrayAdapter<FishWeight> spinnerArrayAdapter = new ArrayAdapter<FishWeight>(getActivity(),
                android.R.layout.simple_spinner_item, list.toArray(new FishWeight[list.size()]));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);

        int selectedIndex = 2;
        s.setSelection(selectedIndex);
    }
    
    private void initFishCountSpinner(int viewId) {

        List<FishCount> list = new ArrayList<FishCount>();
        for (int i = 0; i < Constant.SEARCH_FISH_COUNT.length; i++) {
            FishCount obj = new FishCount();
            obj.setCount(String.valueOf(Constant.SEARCH_FISH_COUNT[i]));
            obj.setCountName(Constant.SEARCH_FISH_COUNT[i] + "条");
            list.add(obj);
        }
        
        Spinner s = (Spinner)tab3View.findViewById(viewId);
        ArrayAdapter<FishCount> spinnerArrayAdapter = new ArrayAdapter<FishCount>(getActivity(),
                android.R.layout.simple_spinner_item, list.toArray(new FishCount[list.size()]));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);

        int selectedIndex = 1;
        s.setSelection(selectedIndex);
    }

    private void initYearSpinner(int viewId) {
        List<String> yearList = getYearList();

        String[] yearArr = yearList.toArray(new String[yearList.size()]);

        Spinner s = (Spinner)tab3View.findViewById(viewId);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, yearArr);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);
    }

    private List<String> getYearList() {
        CampaignDataSource dataSource = new CampaignDataSource(getActivity());
        dataSource.open();
        List<String> yearList = dataSource.getYearsList();
        dataSource.close();
        return yearList;
    }

    private void initMonthSpinner(int viewId) {
        Spinner s = (Spinner)tab3View.findViewById(viewId);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, Constant.MONTH_NAMES);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);
    }

}
