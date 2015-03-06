package com.simple.wildfishingnote.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.chart.ChartByMonthActivity;
import com.simple.wildfishingnote.chart.ChartByYearActivity;
import com.simple.wildfishingnote.chart.ChartByYearDetailActivity;
import com.simple.wildfishingnote.chart.fragments.SimpleChartDemo;

public class MainTab3Fragment extends Fragment implements OnClickListener {
    
    private View tab3View;
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
	    tab3View = inflater.inflate(R.layout.activity_main_tab3, container, false);
	    addBtnOnClickListener();
	    
        return tab3View;
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
                Intent intentChartByYearDetail1 = new Intent(getActivity(), SimpleChartDemo.class);
                intentChartByYearDetail1.putExtra(SimpleChartDemo.YEAR_MONTH, "2015-01");
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
