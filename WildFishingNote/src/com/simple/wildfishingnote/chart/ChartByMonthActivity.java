package com.simple.wildfishingnote.chart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
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
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.database.CampaignDataSource;
import com.simple.wildfishingnote.utils.BusinessUtil;

public class ChartByMonthActivity extends ChartBase {

    public final static int LINE_1_COLOR = ColorTemplate.COLORFUL_COLORS[3];
    public final static int LINE_2_COLOR = ColorTemplate.COLORFUL_COLORS[4];
    public final static String MONTH = "month";
    public final static String BIG_FISH_WEIGHT = "big_fish_weight";
    public final static String FISH_COUNT = "fish_count";
    private CampaignDataSource campaignDataSource;
    private List<String> yearList;
    private String[] years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chart_by_month);

        campaignDataSource = new CampaignDataSource(this);
        campaignDataSource.open();

        yearList = campaignDataSource.getYearsList();
        years = yearList.toArray(new String[yearList.size()]);

        String month = getIntent().getStringExtra(MONTH);
        String bigFishWeight = getIntent().getStringExtra(BIG_FISH_WEIGHT);
        String fishCount = getIntent().getStringExtra(FISH_COUNT);

        initListView(month, bigFishWeight, fishCount);
    }

    private void initListView(String month, String bigFishWeight, String fishCount) {
        HashMap<String, HashMap<String, Integer>> chartByMonthData = campaignDataSource.getChartByMonthData(month, bigFishWeight, fishCount);

        ListView lv = (ListView)findViewById(R.id.listView1);

        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

        list.add(new LineChartItem(generateObtainedbigFish(chartByMonthData, bigFishWeight), getApplicationContext()));
        list.add(new LineChartItem(generateEscapedBigFish(chartByMonthData, bigFishWeight), getApplicationContext()));
        list.add(new LineChartItem(generateNotObtainFish(chartByMonthData), getApplicationContext()));
        list.add(new LineChartItem(generateFishCountMoreThan(chartByMonthData, fishCount), getApplicationContext()));

        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
        lv.setAdapter(cda);
    }

    private LineData generateObtainedbigFish(HashMap<String, HashMap<String, Integer>> chartByMonthData, String bigFishWeight) {

        HashMap<String, Integer> campaignFishCountHash = chartByMonthData.get(Constant.CAMPAIGN_COUNT);
        HashMap<String, Integer> bigFishCountHash = chartByMonthData.get(Constant.OBTAINED_FISH_BIGGER_THAN);

        ArrayList<Entry> e1 = new ArrayList<Entry>();
        int fishCountIndex = 0;
        for (String s : yearList) {
            e1.add(new Entry(campaignFishCountHash.get(s), fishCountIndex));
            fishCountIndex++;
        }

        LineDataSet d1 = new LineDataSet(e1, "总次数");
        setLineDataSetProperty(d1, LINE_1_COLOR);

        ArrayList<Entry> e2 = new ArrayList<Entry>();
        int obtainBigFishIndex = 0;
        for (String s : yearList) {
            e2.add(new Entry(bigFishCountHash.get(s), obtainBigFishIndex));
            obtainBigFishIndex++;
        }

        LineDataSet d2 = new LineDataSet(e2, "钓获大鱼次数(" + BusinessUtil.getFishUnit(Integer.parseInt(bigFishWeight)) + "以上)");
        setLineDataSetProperty(d2, LINE_2_COLOR);

        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(years, sets);

        return cd;
    }

    private LineData generateEscapedBigFish(HashMap<String, HashMap<String, Integer>> chartByMonthData, String bigFishWeight) {
        HashMap<String, Integer> campaignFishCountHash = chartByMonthData.get(Constant.CAMPAIGN_COUNT);
        HashMap<String, Integer> bigFishCountHash = chartByMonthData.get(Constant.ESCAPED_FISH_BIGGER_THAN);

        ArrayList<Entry> e1 = new ArrayList<Entry>();
        int fishCountIndex = 0;
        for (String s : yearList) {
            e1.add(new Entry(campaignFishCountHash.get(s), fishCountIndex));
            fishCountIndex++;
        }

        LineDataSet d1 = new LineDataSet(e1, "总次数");
        setLineDataSetProperty(d1, LINE_1_COLOR);

        ArrayList<Entry> e2 = new ArrayList<Entry>();
        int escapeBigFishIndex = 0;
        for (String s : yearList) {
            e2.add(new Entry(bigFishCountHash.get(s), escapeBigFishIndex));
            escapeBigFishIndex++;
        }

        LineDataSet d2 = new LineDataSet(e2, "跑大鱼次数(" + BusinessUtil.getFishUnit(Integer.parseInt(bigFishWeight)) + "以上)");
        setLineDataSetProperty(d2, LINE_2_COLOR);

        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(years, sets);

        return cd;
    }

    private LineData generateNotObtainFish(HashMap<String, HashMap<String, Integer>> chartByMonthData) {
        HashMap<String, Integer> campaignFishCountHash = chartByMonthData.get(Constant.CAMPAIGN_COUNT);
        HashMap<String, Integer> notObtainFishCountHash = chartByMonthData.get(Constant.NOT_OBTAINED_FISH);

        ArrayList<Entry> e1 = new ArrayList<Entry>();
        int fishCountIndex = 0;
        for (String s : yearList) {
            e1.add(new Entry(campaignFishCountHash.get(s), fishCountIndex));
            fishCountIndex++;
        }

        LineDataSet d1 = new LineDataSet(e1, "总次数");
        setLineDataSetProperty(d1, LINE_1_COLOR);

        ArrayList<Entry> e2 = new ArrayList<Entry>();
        int notObtainFishCountIndex = 0;
        for (String s : yearList) {
            e2.add(new Entry(notObtainFishCountHash.get(s), notObtainFishCountIndex));
            notObtainFishCountIndex++;
        }

        LineDataSet d2 = new LineDataSet(e2, "空军次数");
        setLineDataSetProperty(d2, LINE_2_COLOR);

        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(years, sets);

        return cd;
    }

    private LineData generateFishCountMoreThan(HashMap<String, HashMap<String, Integer>> chartByMonthData, String fishCount) {
        HashMap<String, Integer> campaignFishCountHash = chartByMonthData.get(Constant.CAMPAIGN_COUNT);
        HashMap<String, Integer> fishCountMoreThanCountHash = chartByMonthData.get(Constant.FISH_COUNT_MORE_THAN);

        ArrayList<Entry> e1 = new ArrayList<Entry>();
        int fishCountIndex = 0;
        for (String s : yearList) {
            e1.add(new Entry(campaignFishCountHash.get(s), fishCountIndex));
            fishCountIndex++;
        }

        LineDataSet d1 = new LineDataSet(e1, "总次数");
        setLineDataSetProperty(d1, LINE_1_COLOR);

        ArrayList<Entry> e2 = new ArrayList<Entry>();
        int fishCountMoreThanIndex = 0;
        for (String s : yearList) {
            e2.add(new Entry(fishCountMoreThanCountHash.get(s), fishCountMoreThanIndex));
            fishCountMoreThanIndex++;
        }

        LineDataSet d2 = new LineDataSet(e2, fishCount + "条以上次数");
        setLineDataSetProperty(d2, LINE_2_COLOR);

        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(years, sets);

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
