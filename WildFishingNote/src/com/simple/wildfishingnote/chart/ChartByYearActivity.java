
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
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.database.CampaignDataSource;
import com.simple.wildfishingnote.utils.BusinessUtil;

public class ChartByYearActivity extends ChartBase {

	public final static String YEAR = "year";
    public final static String BIG_FISH_WEIGHT = "big_fish_weight";
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
        String bigFishWeight = getIntent().getStringExtra(BIG_FISH_WEIGHT);
        String fishCount = getIntent().getStringExtra(FISH_COUNT);
        
        initListView(year, bigFishWeight, fishCount);
    }

	private void initListView(String year, String bigFishWeight, String fishCount) {
	    HashMap<String, List<String>> chartByYearData = campaignDataSource.getChartByYearData(year, bigFishWeight, fishCount);
	    
		ListView lv = (ListView) findViewById(R.id.listView1);

        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

        
        list.add(new LineChartItem(generateObtainedbigFish(chartByYearData, bigFishWeight), getApplicationContext()));
        list.add(new LineChartItem(generateEscapedBigFish(chartByYearData, bigFishWeight), getApplicationContext()));
        list.add(new LineChartItem(generateNotObtainFish(chartByYearData), getApplicationContext()));
        list.add(new LineChartItem(generateFishCountMoreThan(chartByYearData, fishCount), getApplicationContext()));

        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
        lv.setAdapter(cda);
	}


    private LineData generateObtainedbigFish(HashMap<String, List<String>> chartByYearData, String bigFishWeight) {
    	
    	List<String> campaignFishCountList = chartByYearData.get(Constant.CAMPAIGN_COUNT);
    	List<String> bigFishCountList = chartByYearData.get(Constant.OBTAINED_FISH_BIGGER_THAN);
    	
    	ArrayList<Entry> e1 = new ArrayList<Entry>();
    	int fishCountIndex = 0;
    	for(String s : campaignFishCountList){
    		e1.add(new Entry(Float.parseFloat(s), fishCountIndex));
    		fishCountIndex++;
    	}
        
        LineDataSet d1 = new LineDataSet(e1, "总次数");
        setLineDataSetProperty(d1, ColorTemplate.COLORFUL_COLORS[3]);
        
        ArrayList<Entry> e2 = new ArrayList<Entry>();
        int obtainBigFishIndex = 0;
    	for(String s : bigFishCountList){
    		e2.add(new Entry(Float.parseFloat(s), obtainBigFishIndex));
    		obtainBigFishIndex++;
    	}

        LineDataSet d2 = new LineDataSet(e2, "钓获大鱼次数("+ BusinessUtil.getFishUnit(Integer.parseInt(bigFishWeight))+"以上)");
        setLineDataSetProperty(d2, Color.rgb(60, 220, 78));
        
        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        sets.add(d2);
        
        LineData cd = new LineData(mMonths, sets);
        
        return cd;
    }

    
    
    private LineData generateEscapedBigFish(HashMap<String, List<String>> chartByYearData, String bigFishWeight) {
        List<String> campaignFishCountList = chartByYearData.get(Constant.CAMPAIGN_COUNT);
        List<String> bigFishCountList = chartByYearData.get(Constant.ESCAPED_FISH_BIGGER_THAN);
        
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        int fishCountIndex = 0;
        for(String s : campaignFishCountList){
            e1.add(new Entry(Float.parseFloat(s), fishCountIndex));
            fishCountIndex++;
        }
        
        LineDataSet d1 = new LineDataSet(e1, "总次数");
        setLineDataSetProperty(d1, ColorTemplate.COLORFUL_COLORS[3]);
        
        ArrayList<Entry> e2 = new ArrayList<Entry>();
        int escapeBigFishIndex = 0;
        for(String s : bigFishCountList){
            e2.add(new Entry(Float.parseFloat(s), escapeBigFishIndex));
            escapeBigFishIndex++;
        }

        LineDataSet d2 = new LineDataSet(e2, "跑大鱼次数("+ BusinessUtil.getFishUnit(Integer.parseInt(bigFishWeight))+"以上)");
        setLineDataSetProperty(d2, Color.rgb(60, 220, 78));
        
        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        sets.add(d2);
        
        LineData cd = new LineData(mMonths, sets);
        
        return cd;
    }
    
    
    private LineData generateNotObtainFish(HashMap<String, List<String>> chartByYearData) {
        List<String> campaignFishCountList = chartByYearData.get(Constant.CAMPAIGN_COUNT);
        List<String> notObtainFishCountList = chartByYearData.get(Constant.NOT_OBTAINED_FISH);
        
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        int fishCountIndex = 0;
        for(String s : campaignFishCountList){
            e1.add(new Entry(Float.parseFloat(s), fishCountIndex));
            fishCountIndex++;
        }
        
        LineDataSet d1 = new LineDataSet(e1, "总次数");
        setLineDataSetProperty(d1, ColorTemplate.COLORFUL_COLORS[3]);
        
        ArrayList<Entry> e2 = new ArrayList<Entry>();
        int notObtainFishCountIndex = 0;
        for(String s : notObtainFishCountList){
            e2.add(new Entry(Float.parseFloat(s), notObtainFishCountIndex));
            notObtainFishCountIndex++;
        }

        LineDataSet d2 = new LineDataSet(e2, "空军次数");
        setLineDataSetProperty(d2, Color.rgb(60, 220, 78));
        
        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        sets.add(d2);
        
        LineData cd = new LineData(mMonths, sets);
        
        return cd;
    }
    
    private LineData generateFishCountMoreThan(HashMap<String, List<String>> chartByYearData, String fishCount) {
        List<String> campaignFishCountList = chartByYearData.get(Constant.CAMPAIGN_COUNT);
        List<String> fishCountMoreThanCountList = chartByYearData.get(Constant.FISH_COUNT_MORE_THAN);
        
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        int fishCountIndex = 0;
        for(String s : campaignFishCountList){
            e1.add(new Entry(Float.parseFloat(s), fishCountIndex));
            fishCountIndex++;
        }
        
        LineDataSet d1 = new LineDataSet(e1, "总次数");
        setLineDataSetProperty(d1, ColorTemplate.COLORFUL_COLORS[3]);
        
        ArrayList<Entry> e2 = new ArrayList<Entry>();
        int fishCountMoreThanIndex = 0;
        for(String s : fishCountMoreThanCountList){
            e2.add(new Entry(Float.parseFloat(s), fishCountMoreThanIndex));
            fishCountMoreThanIndex++;
        }

        LineDataSet d2 = new LineDataSet(e2, fishCount + "条以上次数");
        setLineDataSetProperty(d2, Color.rgb(60, 220, 78));
        
        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        sets.add(d2);
        
        LineData cd = new LineData(mMonths, sets);
        
        return cd;
    }
    
    private void setLineDataSetProperty(LineDataSet lds, int color) {
        lds.setColor(color);
        lds.setLineWidth(2.5f);
        lds.setCircleColor(color);
        lds.setCircleSize(5f);
        lds.setFillColor(color);
        lds.setDrawCubic(true);
        lds.setDrawValues(true);
        lds.setValueTextSize(14f);
        lds.setValueTextColor(color);

        lds.setAxisDependency(YAxis.AxisDependency.LEFT);
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
