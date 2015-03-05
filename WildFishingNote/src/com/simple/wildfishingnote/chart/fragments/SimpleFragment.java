package com.simple.wildfishingnote.chart.fragments;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

public abstract class SimpleFragment extends Fragment {
    
    private Typeface tf;
    
    public SimpleFragment() {
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new BarEntry(5, 0));
        entries.add(new BarEntry(8, 1));
        entries.add(new BarEntry(6, 2));
        entries.add(new BarEntry(10, 3));
        entries.add(new BarEntry(5, 4));
        entries.add(new BarEntry(9, 5));
        entries.add(new BarEntry(10, 6));
        entries.add(new BarEntry(15, 7));
        entries.add(new BarEntry(10, 8));
        entries.add(new BarEntry(20, 9));
        entries.add(new BarEntry(7, 10));
        entries.add(new BarEntry(13, 11));
        entries.add(new BarEntry(6, 12));
        entries.add(new BarEntry(12, 13));
        entries.add(new BarEntry(9, 14));
        entries.add(new BarEntry(5, 15));
        entries.add(new BarEntry(8, 16));
        entries.add(new BarEntry(6, 17));
        entries.add(new BarEntry(10, 18));
        entries.add(new BarEntry(5, 19));
        entries.add(new BarEntry(9, 20));
        entries.add(new BarEntry(10, 21));
        entries.add(new BarEntry(15, 22));
        entries.add(new BarEntry(10, 23));
        entries.add(new BarEntry(20, 24));
        entries.add(new BarEntry(7, 25));
        entries.add(new BarEntry(13, 26));
        entries.add(new BarEntry(6, 27));
        entries.add(new BarEntry(12, 28));
        entries.add(new BarEntry(9, 29));
        entries.add(new BarEntry(12, 30));


        LineDataSet set = new LineDataSet(entries, "温度");
        set.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        set.setLineWidth(2.5f);
        set.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        set.setCircleSize(5f);
        set.setFillColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        set.setDrawCubic(true);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(ColorTemplate.VORDIPLOM_COLORS[2]);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        d.addDataSet(set);

        return d;
    }
    
    protected BarData generateBarData() {

        BarData d = new BarData();

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(30, 0));
        entries.add(new BarEntry(0.6f, 1));
        entries.add(new BarEntry(0, 2));
        entries.add(new BarEntry(2.6f, 3));
        entries.add(new BarEntry(0.3f, 4));
        entries.add(new BarEntry(0, 5));
        entries.add(new BarEntry(1.4f, 6));
        entries.add(new BarEntry(0, 7));
        entries.add(new BarEntry(0, 8));
        entries.add(new BarEntry(0, 9));
//        entries.add(new BarEntry(0, 10));
//        entries.add(new BarEntry(0, 11));
//        entries.add(new BarEntry(0, 12));
//        entries.add(new BarEntry(0, 13));
        entries.add(new BarEntry(0, 14));
        entries.add(new BarEntry(30, 15));
        entries.add(new BarEntry(0.6f, 16));
        entries.add(new BarEntry(0, 17));
        entries.add(new BarEntry(2.6f, 18));
        entries.add(new BarEntry(0.3f, 19));
        entries.add(new BarEntry(0, 20));
        entries.add(new BarEntry(1.4f, 21));
//        entries.add(new BarEntry(0, 22));
//        entries.add(new BarEntry(0, 23));
        entries.add(new BarEntry(0, 24));
        entries.add(new BarEntry(0, 25));
        entries.add(new BarEntry(0, 26));
        entries.add(new BarEntry(0, 27));
        entries.add(new BarEntry(0, 28));
        entries.add(new BarEntry(0, 29));
        entries.add(new BarEntry(0, 30));
        
        
        BarDataSet set = new BarDataSet(entries, "渔获重量");
        set.setColor(Color.rgb(60, 220, 78));
        set.setValueTextColor(Color.rgb(60, 220, 78));
        set.setValueTextSize(10f);
        d.addDataSet(set);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        return d;
    }

    protected String[] mDays = new String[] {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"
    };
}
