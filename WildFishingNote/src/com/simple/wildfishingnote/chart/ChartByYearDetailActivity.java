
package com.simple.wildfishingnote.chart;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

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

public class ChartByYearDetailActivity extends ChartBase {

    private CombinedChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chart_by_year_detail);

        mChart = (CombinedChart) findViewById(R.id.chart1);
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
    }

    private LineData generateLineData() {


        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new BarEntry(5, 0));
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
        
        

        ArrayList<Entry> entries2 = new ArrayList<Entry>();
        entries2.add(new BarEntry(15, 0));
        entries2.add(new BarEntry(22, 30));
        
        LineDataSet set2 = new LineDataSet(entries, "温度");
        set2.setColor(ColorTemplate.VORDIPLOM_COLORS[3]);
        set2.setLineWidth(2.5f);
        set2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[3]);
        set2.setCircleSize(5f);
        set2.setFillColor(ColorTemplate.VORDIPLOM_COLORS[3]);
        set2.setDrawCubic(true);
        set2.setDrawValues(true);
        set2.setValueTextSize(10f);
        set2.setValueTextColor(ColorTemplate.VORDIPLOM_COLORS[3]);

        set2.setAxisDependency(YAxis.AxisDependency.LEFT);


        
        
        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(set);
        sets.add(set2);
        
        LineData cd = new LineData(mDays, sets);

        return cd;
    }
    
    private BarData generateBarData() {

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
