package com.simple.wildfishingnote.tabs;

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
import com.simple.wildfishingnote.bean.RodLength;
import com.simple.wildfishingnote.chart.ChartByMonthActivity;
import com.simple.wildfishingnote.chart.ChartByYearActivity;
import com.simple.wildfishingnote.chart.ChartByYearDetailActivity;
import com.simple.wildfishingnote.chart.fragments.SimpleChartDemo;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.database.CampaignDataSource;

public class MainTab3Fragment extends Fragment implements OnClickListener {
    
    private View tab3View;
    private String[] mMonths = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
	    tab3View = inflater.inflate(R.layout.activity_main_tab3, container, false);
	    
	    initSpinner();  
        
	    addBtnOnClickListener();
	    
        return tab3View;
    }

    private void initSpinner() {
        initYearSpinner();
        initMonthSpinner();
    }

    private void initYearSpinner() {
        CampaignDataSource dataSource = new CampaignDataSource(getActivity());
        dataSource.open();
        List<String> yearList = dataSource.getYearsList();
        dataSource.close();
	    
        String[] yearArr = yearList.toArray(new String[yearList.size()]);
        
        Spinner s = (Spinner) tab3View.findViewById(R.id.spinnerYear);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, yearArr);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);
    }
    
    private void initMonthSpinner() {
        Spinner s = (Spinner) tab3View.findViewById(R.id.spinnerMonth);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, mMonths);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnByYear:
                Intent intentChartByYear = new Intent(getActivity(), ChartByYearActivity.class);
                getActivity().startActivity(intentChartByYear);
                break;
            case R.id.btnByMonth:
                Intent intentChartByMonth = new Intent(getActivity(), ChartByMonthActivity.class);
                getActivity().startActivity(intentChartByMonth);
                break;
            case R.id.btnByYearDetail:
                Intent intentChartByYearDetail = new Intent(getActivity(), ChartByYearDetailActivity.class);
                getActivity().startActivity(intentChartByYearDetail);
                break;
            case R.id.btnByYearDetail1:
                Spinner spinnerYear = (Spinner) tab3View.findViewById(R.id.spinnerYear);
                Spinner spinnerMonth = (Spinner) tab3View.findViewById(R.id.spinnerMonth);
                String year = spinnerYear.getSelectedItem().toString();
                String month = spinnerMonth.getSelectedItem().toString();
                
                Intent intentChartByYearDetail1 = new Intent(getActivity(), SimpleChartDemo.class);
                intentChartByYearDetail1.putExtra(SimpleChartDemo.YEAR_MONTH, year + Constant.DASH + month);
                getActivity().startActivity(intentChartByYearDetail1);
                break;
        }
    }
    
    private void addBtnOnClickListener() {
        BootstrapButton btnByYear = (BootstrapButton) tab3View.findViewById(R.id.btnByYear);
        btnByYear.setOnClickListener(this);
        
        BootstrapButton btnByMonth = (BootstrapButton) tab3View.findViewById(R.id.btnByMonth);
        btnByMonth.setOnClickListener(this);
        
        BootstrapButton btnByYearDetail = (BootstrapButton) tab3View.findViewById(R.id.btnByYearDetail);
        btnByYearDetail.setOnClickListener(this);
        
        BootstrapButton btnByYearDetail1 = (BootstrapButton) tab3View.findViewById(R.id.btnByYearDetail1);
        btnByYearDetail1.setOnClickListener(this);
    }

}
