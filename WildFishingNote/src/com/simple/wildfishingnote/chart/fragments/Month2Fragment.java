package com.simple.wildfishingnote.chart.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.simple.wildfishingnote.R;


public class Month2Fragment extends SimpleFragment {

    public static Fragment newInstance() {
        return new Month2Fragment();
    }

    private CombinedChart mChart;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_chart_by_year_detail, container, false);
        
        mChart = (CombinedChart) v.findViewById(R.id.chart1);
        mChart.setDescription("");
        mChart.setDrawGridBackground(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTH_SIDED);

        CombinedData data = new CombinedData(mDays);

        data.setData(generateLineData());
        data.setData(generateBarData());

        mChart.setData(data);
        mChart.invalidate();
        
        return v;
    }
}
