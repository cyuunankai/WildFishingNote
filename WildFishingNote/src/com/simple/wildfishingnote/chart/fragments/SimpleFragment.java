package com.simple.wildfishingnote.chart.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.utils.DateUtil;

public abstract class SimpleFragment extends Fragment {
    
    public SimpleFragment() {
        
    }
    
    private CombinedChart mChart;
//    private TextView tvYearMonth;
    private final static float AXIS_MAX_VALUE = 40.0f;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_chart_by_year_detail, container, false);
        
        String queryYearMonth = getActivity().getIntent().getStringExtra(SimpleChartDemo.YEAR_MONTH);
        String year = queryYearMonth.split(Constant.DASH)[0];
        
        String currentYearMonth = year + Constant.DASH + getMonth();
        
        
//        tvYearMonth = (TextView) v.findViewById(R.id.tvYearMonth);
//        tvYearMonth.setText(DateUtil.getReadableYearMonth(currentYearMonth));
        
        mChart = (CombinedChart) v.findViewById(R.id.chart1);
        mChart.setDescription(DateUtil.getReadableYearMonth(currentYearMonth));
        mChart.setDrawGridBackground(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMaxValue(AXIS_MAX_VALUE);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaxValue(AXIS_MAX_VALUE);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTH_SIDED);

        String[] daysInMonth = DateUtil.getDaysInMonth(currentYearMonth);
        CombinedData data = new CombinedData(daysInMonth);

        data.setData(generateLineData(currentYearMonth, daysInMonth));
        data.setData(generateBarData(currentYearMonth, daysInMonth));

        mChart.setData(data);
        mChart.invalidate();
        
        return v;
    }
    
    public abstract String getMonth();

    protected LineData generateLineData(String yearMonth, String[] daysInMonth) {
        HashMap<String, String[]> dateTempHash = ((SimpleChartDemo)getActivity()).dateTempcHash;
        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();
        for (int i = 0; i < daysInMonth.length; i++) {
            String key = yearMonth + Constant.DASH + com.simple.wildfishingnote.utils.StringUtils.leftPadTwo(Integer.parseInt(daysInMonth[i]));
            String[] tempcArr = dateTempHash.get(key);
            if (tempcArr != null){
                int minTempc = Integer.parseInt(tempcArr[0]);
                int maxTempc = Integer.parseInt(tempcArr[1]);
				if (i % 2 == 0) {
					entries.add(new BarEntry(minTempc, i));
				} else {
					entries.add(new BarEntry(maxTempc, i));
				}
            }
            
        }
//        entries.add(new BarEntry(5, 0));
//        entries.add(new BarEntry(8, 1));
//        entries.add(new BarEntry(6, 2));
//        entries.add(new BarEntry(10, 3));
//        entries.add(new BarEntry(5, 4));
//        entries.add(new BarEntry(9, 5));
//        entries.add(new BarEntry(10, 6));
//        entries.add(new BarEntry(15, 7));
//        entries.add(new BarEntry(10, 8));
//        entries.add(new BarEntry(20, 9));
//        entries.add(new BarEntry(7, 10));
//        entries.add(new BarEntry(13, 11));
//        entries.add(new BarEntry(6, 12));
//        entries.add(new BarEntry(12, 13));
//        entries.add(new BarEntry(9, 14));
//        entries.add(new BarEntry(5, 15));
//        entries.add(new BarEntry(8, 16));
//        entries.add(new BarEntry(6, 17));
//        entries.add(new BarEntry(10, 18));
//        entries.add(new BarEntry(5, 19));
//        entries.add(new BarEntry(9, 20));
//        entries.add(new BarEntry(10, 21));
//        entries.add(new BarEntry(15, 22));
//        entries.add(new BarEntry(10, 23));
//        entries.add(new BarEntry(20, 24));
//        entries.add(new BarEntry(7, 25));
//        entries.add(new BarEntry(13, 26));
//        entries.add(new BarEntry(6, 27));
//        entries.add(new BarEntry(12, 28));
//        entries.add(new BarEntry(9, 29));
//        entries.add(new BarEntry(12, 30));


        LineDataSet set = new LineDataSet(entries, "温度");
        set.setColor(ColorTemplate.COLORFUL_COLORS[3]);
        set.setLineWidth(2.5f);
        set.setCircleColor(ColorTemplate.COLORFUL_COLORS[3]);
        set.setCircleSize(5f);
        set.setFillColor(ColorTemplate.COLORFUL_COLORS[3]);
        set.setDrawCubic(true);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(ColorTemplate.COLORFUL_COLORS[3]);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        d.addDataSet(set);

        return d;
    }
    
    protected BarData generateBarData(String yearMonth, String[] daysInMonth) {

        HashMap<String, String> dateFishResultHash = ((SimpleChartDemo)getActivity()).dateFishResultHash;
        BarData d = new BarData();

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        for (int i = 0; i < daysInMonth.length; i++) {
            String key = yearMonth + Constant.DASH + StringUtils.leftPad(daysInMonth[i], 2);
            String weight = dateFishResultHash.get(key);
            if (weight != null) {
                entries.add(new BarEntry(Float.valueOf(weight), i));
            }
        }
        

       
//        entries.add(new BarEntry(30, 0));
//        entries.add(new BarEntry(0.6f, 1));
//        entries.add(new BarEntry(0, 2));
//        entries.add(new BarEntry(2.6f, 3));
//        entries.add(new BarEntry(0.3f, 4));
//        entries.add(new BarEntry(0, 5));
//        entries.add(new BarEntry(1.4f, 6));
//        entries.add(new BarEntry(0, 7));
//        entries.add(new BarEntry(0, 8));
//        entries.add(new BarEntry(0, 9));
////        entries.add(new BarEntry(0, 10));
////        entries.add(new BarEntry(0, 11));
////        entries.add(new BarEntry(0, 12));
////        entries.add(new BarEntry(0, 13));
//        entries.add(new BarEntry(0, 14));
//        entries.add(new BarEntry(30, 15));
//        entries.add(new BarEntry(0.6f, 16));
//        entries.add(new BarEntry(0, 17));
//        entries.add(new BarEntry(2.6f, 18));
//        entries.add(new BarEntry(0.3f, 19));
//        entries.add(new BarEntry(0, 20));
//        entries.add(new BarEntry(1.4f, 21));
////        entries.add(new BarEntry(0, 22));
////        entries.add(new BarEntry(0, 23));
//        entries.add(new BarEntry(0, 24));
//        entries.add(new BarEntry(0, 25));
//        entries.add(new BarEntry(0, 26));
//        entries.add(new BarEntry(0, 27));
//        entries.add(new BarEntry(0, 28));
//        entries.add(new BarEntry(0, 29));
//        entries.add(new BarEntry(0, 30));
        
        
        BarDataSet set = new BarDataSet(entries, "渔获重量");
        set.setColor(Color.rgb(60, 220, 78));
        set.setValueTextColor(Color.rgb(60, 220, 78));
        set.setValueTextSize(10f);
        d.addDataSet(set);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        return d;
    }

//    protected String[] mDays = new String[] {
//            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"
//    };
}
