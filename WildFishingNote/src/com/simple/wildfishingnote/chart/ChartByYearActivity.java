
package com.simple.wildfishingnote.chart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.database.CampaignDataSource;

public class ChartByYearActivity extends ChartBase {

	public final static String YEAR = "year";
    public final static String BIG_FISH = "big_fish";
    public final static String FISH_COUNT = "fish_count";
    private CampaignDataSource campaignDataSource;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chart_by_year);
        campaignDataSource = new CampaignDataSource(this);
		campaignDataSource.open();
        
		String year = getIntent().getStringExtra(YEAR);
        String bigFishWeight = getIntent().getStringExtra(BIG_FISH);
        String fishCount = getIntent().getStringExtra(FISH_COUNT);
        
        initListView(year, bigFishWeight, fishCount);
    }

	private void initListView(String year, String bigFishWeight, String fishCount) {
		ListView lv = (ListView) findViewById(R.id.listView1);

        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

        list.add(new LineChartItem(generateDataLine("obtainedbigFishWeight", year, bigFishWeight, fishCount), getApplicationContext()));
        list.add(new LineChartItem(generateDataLine("escapedbigFishWeight", year, bigFishWeight, fishCount), getApplicationContext()));
        list.add(new LineChartItem(generateDataLine("noFish", year, bigFishWeight, fishCount), getApplicationContext()));
        list.add(new LineChartItem(generateDataLine("fishCount", year, bigFishWeight, fishCount), getApplicationContext()));

        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
        lv.setAdapter(cda);
	}

    
    
    /**
     * generates a random ChartData object with just one DataSet
     * 
     * @return
     */
    private LineData generateDataLine(String flag, String year, String bigFishWeight, String fishCount) {
        if ("obtainedbigFishWeight".equals(flag)) {
            return generateObtainedbigFishWeight(year, bigFishWeight);
        } else if ("escapedbigFishWeight".equals(flag)) {
            return generateEscapedbigFish();
        } else if ("noFish".equals(flag)) {
            return generateNoFish();
        } else if ("fishCount".equals(flag)) {
            return generateFishCount();
        } else {
            return null;
        }
    }

    private LineData generateObtainedbigFishWeight(String year, String bigFishWeight) {
    	
    	List<String> campaignFishCountList = campaignDataSource.getCampaignCountPerMonth(year);
    	List<String> bigFishCountList = campaignDataSource.getObtainBigFishCountPerMonth(year, bigFishWeight);
    	
    	ArrayList<Entry> e1 = new ArrayList<Entry>();
    	int fishCountIndex = 0;
    	for(String s : campaignFishCountList){
    		e1.add(new Entry(Float.parseFloat(s), fishCountIndex));
    		fishCountIndex++;
    	}
//        e1.add(new Entry(0, 0));
//        e1.add(new Entry(0, 1));
//        e1.add(new Entry(0, 2));
//        e1.add(new Entry(3, 3));
//        e1.add(new Entry(8, 4));
//        e1.add(new Entry(10, 5));
//        e1.add(new Entry(5, 6));
//        e1.add(new Entry(6, 7));
//        e1.add(new Entry(10, 8));
//        e1.add(new Entry(12, 9));
//        e1.add(new Entry(2, 10));
//        e1.add(new Entry(0, 11));
        
        LineDataSet d1 = new LineDataSet(e1, "总次数");
        d1.setColor(ColorTemplate.COLORFUL_COLORS[3]);
        d1.setLineWidth(2.5f);
        d1.setCircleColor(ColorTemplate.COLORFUL_COLORS[3]);
        d1.setCircleSize(5f);
        d1.setFillColor(ColorTemplate.COLORFUL_COLORS[3]);
        d1.setDrawCubic(true);
        d1.setDrawValues(true);
        d1.setValueTextSize(14f);
        d1.setValueTextColor(ColorTemplate.COLORFUL_COLORS[3]);

        d1.setAxisDependency(YAxis.AxisDependency.LEFT);
        
        ArrayList<Entry> e2 = new ArrayList<Entry>();
        int obtainBigFishIndex = 0;
    	for(String s : bigFishCountList){
    		e2.add(new Entry(Float.parseFloat(s), obtainBigFishIndex));
    		obtainBigFishIndex++;
    	}
//        e2.add(new Entry(0, 0));
//        e2.add(new Entry(0, 1));
//        e2.add(new Entry(0, 2));
//        e2.add(new Entry(0, 3));
//        e2.add(new Entry(0, 4));
//        e2.add(new Entry(0, 5));
//        e2.add(new Entry(0, 6));
//        e2.add(new Entry(0, 7));
//        e2.add(new Entry(0, 8));
//        e2.add(new Entry(1, 9));
//        e2.add(new Entry(0, 10));
//        e2.add(new Entry(0, 11));

        LineDataSet d2 = new LineDataSet(e2, "钓获大鱼次数");
        d2.setColor(Color.rgb(60, 220, 78));
        d2.setLineWidth(2.5f);
        d2.setCircleColor(Color.rgb(60, 220, 78));
        d2.setCircleSize(5f);
        d2.setFillColor(Color.rgb(60, 220, 78));
        d2.setDrawCubic(true);
        d2.setDrawValues(true);
        d2.setValueTextSize(14f);
        d2.setValueTextColor(Color.rgb(60, 220, 78));

        d2.setAxisDependency(YAxis.AxisDependency.LEFT);
        
        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        sets.add(d2);
        
        LineData cd = new LineData(mMonths, sets);
        
        return cd;
    }
    
    private LineData generateEscapedbigFish() {
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        e1.add(new Entry(0, 0));
        e1.add(new Entry(0, 1));
        e1.add(new Entry(0, 2));
        e1.add(new Entry(3, 3));
        e1.add(new Entry(8, 4));
        e1.add(new Entry(10, 5));
        e1.add(new Entry(5, 6));
        e1.add(new Entry(6, 7));
        e1.add(new Entry(10, 8));
        e1.add(new Entry(12, 9));
        e1.add(new Entry(2, 10));
        e1.add(new Entry(0, 11));
        
        LineDataSet d1 = new LineDataSet(e1, "总次数");
        d1.setLineWidth(2.5f);
        d1.setCircleSize(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);
        
        ArrayList<Entry> e2 = new ArrayList<Entry>();
        e2.add(new Entry(0, 0));
        e2.add(new Entry(0, 1));
        e2.add(new Entry(0, 2));
        e2.add(new Entry(0, 3));
        e2.add(new Entry(1, 4));
        e2.add(new Entry(1, 5));
        e2.add(new Entry(0, 6));
        e2.add(new Entry(0, 7));
        e2.add(new Entry(0, 8));
        e2.add(new Entry(1, 9));
        e2.add(new Entry(0, 10));
        e2.add(new Entry(0, 11));
        
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
        
        LineData cd = new LineData(mMonths, sets);
        return cd;
    }
    
    
    private LineData generateNoFish() {
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        e1.add(new Entry(0, 0));
        e1.add(new Entry(0, 1));
        e1.add(new Entry(0, 2));
        e1.add(new Entry(3, 3));
        e1.add(new Entry(8, 4));
        e1.add(new Entry(10, 5));
        e1.add(new Entry(5, 6));
        e1.add(new Entry(6, 7));
        e1.add(new Entry(10, 8));
        e1.add(new Entry(12, 9));
        e1.add(new Entry(2, 10));
        e1.add(new Entry(0, 11));
        
        LineDataSet d1 = new LineDataSet(e1, "总次数");
        d1.setLineWidth(2.5f);
        d1.setCircleSize(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);
        
        ArrayList<Entry> e2 = new ArrayList<Entry>();
        e2.add(new Entry(0, 0));
        e2.add(new Entry(0, 1));
        e2.add(new Entry(0, 2));
        e2.add(new Entry(0, 3));
        e2.add(new Entry(1, 4));
        e2.add(new Entry(1, 5));
        e2.add(new Entry(0, 6));
        e2.add(new Entry(0, 7));
        e2.add(new Entry(0, 8));
        e2.add(new Entry(1, 9));
        e2.add(new Entry(0, 10));
        e2.add(new Entry(0, 11));
        
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
        
        LineData cd = new LineData(mMonths, sets);
        return cd;
    }
    
    private LineData generateFishCount() {
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        e1.add(new Entry(0, 0));
        e1.add(new Entry(0, 1));
        e1.add(new Entry(0, 2));
        e1.add(new Entry(3, 3));
        e1.add(new Entry(8, 4));
        e1.add(new Entry(10, 5));
        e1.add(new Entry(5, 6));
        e1.add(new Entry(6, 7));
        e1.add(new Entry(10, 8));
        e1.add(new Entry(12, 9));
        e1.add(new Entry(2, 10));
        e1.add(new Entry(0, 11));
        
        LineDataSet d1 = new LineDataSet(e1, "总次数");
        d1.setLineWidth(2.5f);
        d1.setCircleSize(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);
        
        ArrayList<Entry> e2 = new ArrayList<Entry>();
        e2.add(new Entry(0, 0));
        e2.add(new Entry(0, 1));
        e2.add(new Entry(0, 2));
        e2.add(new Entry(0, 3));
        e2.add(new Entry(1, 4));
        e2.add(new Entry(1, 5));
        e2.add(new Entry(0, 6));
        e2.add(new Entry(0, 7));
        e2.add(new Entry(0, 8));
        e2.add(new Entry(1, 9));
        e2.add(new Entry(0, 10));
        e2.add(new Entry(0, 11));
        
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
        
        LineData cd = new LineData(mMonths, sets);
        return cd;
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
    
    @Override
    protected void onResume() {
      campaignDataSource.open();
      super.onResume();
    }

    @Override
    protected void onPause() {
      campaignDataSource.close();
      super.onPause();
    }
    
}
