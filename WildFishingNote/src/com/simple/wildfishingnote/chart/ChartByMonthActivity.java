
package com.simple.wildfishingnote.chart;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.simple.wildfishingnote.R;

public class ChartByMonthActivity extends ChartBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chart_by_month);
        
        ListView lv = (ListView) findViewById(R.id.listView1);

        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

        list.add(new LineChartItem(generateDataLine("obtainedBigFish"), getApplicationContext()));
        list.add(new LineChartItem(generateDataLine("escapedBigFish"), getApplicationContext()));
        list.add(new LineChartItem(generateDataLine("noFish"), getApplicationContext()));
        list.add(new LineChartItem(generateDataLine("fishCount"), getApplicationContext()));

        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
        lv.setAdapter(cda);
    }

    /** adapter that supports 1 different item types */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {
        
        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }
        
        @Override
        public int getItemViewType(int position) {           
            // return the views type
            return getItem(position).getItemType();
        }
        
        @Override
        public int getViewTypeCount() {
            return 1; // we have 3 different item-types
        }
    }
    
    /**
     * generates a random ChartData object with just one DataSet
     * 
     * @return
     */
    private LineData generateDataLine(String flag) {
        if ("obtainedBigFish".equals(flag)) {
            return generateObtainedBigFish();
        } else if ("escapedBigFish".equals(flag)) {
            return generateEscapedBigFish();
        } else if ("noFish".equals(flag)) {
            return generateNoFish();
        } else if ("fishCount".equals(flag)) {
            return generateFishCount();
        } else {
            return null;
        }
    }

    private LineData generateObtainedBigFish() {
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        e1.add(new Entry(4, 0));
        e1.add(new Entry(6, 1));
        e1.add(new Entry(5, 2));
        e1.add(new Entry(7, 3));
        e1.add(new Entry(7, 4));
        e1.add(new Entry(10, 5));
        
        LineDataSet d1 = new LineDataSet(e1, "总次数");
        d1.setLineWidth(2.5f);
        d1.setCircleSize(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);
        
        ArrayList<Entry> e2 = new ArrayList<Entry>();
        e2.add(new Entry(0, 0));
        e2.add(new Entry(1, 1));
        e2.add(new Entry(3, 2));
        e2.add(new Entry(4, 3));
        e2.add(new Entry(1, 4));
        e2.add(new Entry(5, 5));

        LineDataSet d2 = new LineDataSet(e2, "钓获大鱼次数");
        d2.setLineWidth(2.5f);
        d2.setCircleSize(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        d2.setDrawValues(false);
        
        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        sets.add(d2);
        
        LineData cd = new LineData(mYears, sets);
        return cd;
    }
    
    private LineData generateEscapedBigFish() {
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        e1.add(new Entry(4, 0));
        e1.add(new Entry(6, 1));
        e1.add(new Entry(5, 2));
        e1.add(new Entry(7, 3));
        e1.add(new Entry(7, 4));
        e1.add(new Entry(10, 5));
        
        LineDataSet d1 = new LineDataSet(e1, "总次数");
        d1.setLineWidth(2.5f);
        d1.setCircleSize(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);
        
        ArrayList<Entry> e2 = new ArrayList<Entry>();
        e2.add(new Entry(0, 0));
        e2.add(new Entry(1, 1));
        e2.add(new Entry(0, 2));
        e2.add(new Entry(0, 3));
        e2.add(new Entry(3, 4));
        e2.add(new Entry(1, 5));
        
        LineDataSet d2 = new LineDataSet(e2, "跑大鱼次数");
        d2.setLineWidth(2.5f);
        d2.setCircleSize(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        d2.setDrawValues(false);
        
        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        sets.add(d2);
        
        LineData cd = new LineData(mYears, sets);
        return cd;
    }
    
    
    private LineData generateNoFish() {
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        e1.add(new Entry(4, 0));
        e1.add(new Entry(6, 1));
        e1.add(new Entry(5, 2));
        e1.add(new Entry(7, 3));
        e1.add(new Entry(7, 4));
        e1.add(new Entry(10, 5));
        
        LineDataSet d1 = new LineDataSet(e1, "总次数");
        d1.setLineWidth(2.5f);
        d1.setCircleSize(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);
        
        ArrayList<Entry> e2 = new ArrayList<Entry>();
        e2.add(new Entry(0, 0));
        e2.add(new Entry(1, 1));
        e2.add(new Entry(3, 2));
        e2.add(new Entry(4, 3));
        e2.add(new Entry(1, 4));
        e2.add(new Entry(5, 5));
        
        LineDataSet d2 = new LineDataSet(e2, "空军次数");
        d2.setLineWidth(2.5f);
        d2.setCircleSize(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        d2.setDrawValues(false);
        
        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        sets.add(d2);
        
        LineData cd = new LineData(mYears, sets);
        return cd;
    }
    
    private LineData generateFishCount() {
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        e1.add(new Entry(4, 0));
        e1.add(new Entry(6, 1));
        e1.add(new Entry(5, 2));
        e1.add(new Entry(7, 3));
        e1.add(new Entry(7, 4));
        e1.add(new Entry(10, 5));
        
        LineDataSet d1 = new LineDataSet(e1, "总次数");
        d1.setLineWidth(2.5f);
        d1.setCircleSize(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);
        
        ArrayList<Entry> e2 = new ArrayList<Entry>();
        e2.add(new Entry(0, 0));
        e2.add(new Entry(1, 1));
        e2.add(new Entry(3, 2));
        e2.add(new Entry(4, 3));
        e2.add(new Entry(1, 4));
        e2.add(new Entry(5, 5));
        
        LineDataSet d2 = new LineDataSet(e2, "10条以上次数");
        d2.setLineWidth(2.5f);
        d2.setCircleSize(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        d2.setDrawValues(false);
        
        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        sets.add(d2);
        
        LineData cd = new LineData(mYears, sets);
        return cd;
    }
    
    private String[] mYears = new String[] {
            "2008", "2009", "2010", "2011", "2012", "2013"
    };
    
}
