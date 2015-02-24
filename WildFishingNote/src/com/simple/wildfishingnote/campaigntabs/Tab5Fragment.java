package com.simple.wildfishingnote.campaigntabs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luminous.pick.CustomGallery;
import com.luminous.pick.GalleryAdapter;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.simple.wildfishingnote.AddMainActivity;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Campaign;
import com.simple.wildfishingnote.bean.Place;
import com.simple.wildfishingnote.bean.Point;
import com.simple.wildfishingnote.bean.RelayCamapignStatisticsResult;
import com.simple.wildfishingnote.common.Common;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.database.CampaignDataSource;
import com.simple.wildfishingnote.moresimple.NestedListView;
import com.simple.wildfishingnote.utils.FileUtil;

public class Tab5Fragment extends Fragment implements OnClickListener {
    
    private View tab5View;
    private CampaignDataSource dataSource;
    private AddMainActivity addMainActivity;
    private LayoutInflater mInflater;
    private StatisticsArrayAdapter arrayAdapter;
    
    
    private GridView gridGallery;
    private GalleryAdapter galleryAdapter;
    private ImageLoader imageLoader;
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
	    mInflater = inflater;
	    tab5View = inflater.inflate(R.layout.activity_tab5, container, false);
	    addMainActivity = (AddMainActivity)getActivity();
        dataSource = addMainActivity.getCampaignDataSource();
        
	    return tab5View;
    }
	
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            dataSource.open();
            
            setPreBtn();
            setSaveAllBtn();
            setOperationBtnVisibility();
            
            initViewByPreference();
        }
    }

    private void initViewByPreference() {
        Campaign campaign = buildCampaign();
        Place place = dataSource.getPlaceById(campaign.getPlaceId());

        TextView summaryTextView = (TextView) tab5View.findViewById(R.id.summaryTextView);
        TextView startTimeTextView = (TextView) tab5View.findViewById(R.id.startTimeTextView);
        TextView endTimeTextView = (TextView) tab5View.findViewById(R.id.endTimeTextView);
        TextView placeTextView = (TextView) tab5View.findViewById(R.id.placeTextView);
        TextView pointTextView = (TextView) tab5View.findViewById(R.id.pointTextView);
        
        summaryTextView.setText(campaign.getSummary());
        startTimeTextView.setText(campaign.getStartTime());
        endTimeTextView.setText(campaign.getEndTime());
        placeTextView.setText(place.getTitle());
        
        initPointByPreference(campaign, pointTextView);
        
        initStatisticsByPreference(campaign);
        
        initImageByPreference(campaign);
    }

    private void initImageByPreference(Campaign campaign) {
        initImageLoader();
        initImage(campaign.getPicList());
    }

    private void initStatisticsByPreference(Campaign campaign) {
        NestedListView listView = (NestedListView) tab5View.findViewById(R.id.confirmListViewResult);
        View header = mInflater.inflate(R.layout.activity_fish_result_listview_header, null);
        listView.addHeaderView(header);
        arrayAdapter = new StatisticsArrayAdapter(getActivity(), campaign.getStatisticsList());
        listView.setAdapter(arrayAdapter);
    }

    private void initPointByPreference(Campaign campaign, TextView pointTextView) {
        List<Point> pointList = dataSource.getPointsByIds(campaign.getPointIdList());
        String pointsStr = "";
        int pointNameIdentify = 1;
        for (Point point : pointList) {
            pointsStr += "钓点" + String.valueOf(pointNameIdentify) + Constant.NEW_LINE;
            pointsStr += "水深: " + point.getDepth() + "米   竿长: " + point.getRodLengthName() + "米" + Constant.NEW_LINE;
            pointsStr += "打窝:" + point.getLureMethodName() + Constant.NEW_LINE;
            pointsStr += "饵料:" + point.getBaitName() + Constant.NEW_LINE;
            
            pointNameIdentify ++;
        }
        pointTextView.setText(pointsStr);
    }
    
    /**
     * 监听所有onClick事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCampaignWeatherPre:
                Common.setCampaignPrefernce(getActivity(), "btn_click", "true");
                ((AddMainActivity)getActivity()).getActionBarReference().setSelectedNavigationItem(3);
                break;
            case R.id.buttonSaveAll:
            	
                Campaign campaign = buildCampaign();
                dataSource.addAllData(campaign);
                
                Intent intent = getActivity().getIntent();
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
                break;
        }
    }
    
    private Campaign buildCampaign() {
        String startTime = Common.getCampaignPrefernceString(getActivity(), "campaign_start_time");
        String endTime = Common.getCampaignPrefernceString(getActivity(), "campaign_end_time");
        String summary = Common.getCampaignPrefernceString(getActivity(), "campaign_summary");
        
        String placeId = Common.getCampaignPrefernceString(getActivity(), "campaign_place_id");
        
        List<String> pointIdList = Common.getCampaignPrefernceStrList(getActivity(), "campaign_point_id_list");
        
        String fishResultStatisticsListStr = Common.getCampaignPrefernceString(getActivity(), "campaign_fish_result_statistics_list");
        Type statisticsListType = new TypeToken<List<RelayCamapignStatisticsResult>>() {}.getType();
        List<RelayCamapignStatisticsResult> statisticsList = new Gson().fromJson(fishResultStatisticsListStr, statisticsListType);
        
        List<String> sdCardPicList = new ArrayList<String>();
        List<String> picList = Common.getCampaignPrefernceStrList(getActivity(), "campaign_fish_result_pic_list");
        for(String from : picList){
        	String to = dealImageFile(from);
        	sdCardPicList.add(to);
        }
        
        Campaign campaign = new Campaign();
        campaign.setSummary(summary);
        campaign.setStartTime(startTime);
        campaign.setEndTime(endTime);
        campaign.setPlaceId(placeId);
        campaign.setPointIdList(pointIdList);
        campaign.setPicList(sdCardPicList);
        campaign.setStatisticsList(statisticsList);
        
        return campaign;
    }
    
	
    /**
     * 处理图片
     * @return
     */
    private String dealImageFile(String from) {
        String fileName;
        
        String directory = getActivity().getApplicationContext().getFilesDir() + Constant.FISH_RESULT_IMAGE_PATH;
        fileName = FileUtil.saveImageToInternalStorage(from, directory);
        
        return fileName;
    }
    
    private void setOperationBtnVisibility() {
        LinearLayout buttonsLayoutCampaignWeatherEdit = (LinearLayout)tab5View.findViewById(R.id.buttonsLayoutCampaignWeatherEdit);
        LinearLayout buttonsLayoutCampaignWeatherAdd = (LinearLayout)tab5View.findViewById(R.id.buttonsLayoutCampaignWeatherAdd);
        String mode = Common.getCampaignPrefernceString(getActivity(), "campaign_operation_mode");
        if (mode.equals("add")) {
            buttonsLayoutCampaignWeatherAdd.setVisibility(View.VISIBLE);
            buttonsLayoutCampaignWeatherEdit.setVisibility(View.INVISIBLE);
        } else if (mode.equals("edit")) {
            buttonsLayoutCampaignWeatherAdd.setVisibility(View.INVISIBLE);
            buttonsLayoutCampaignWeatherEdit.setVisibility(View.VISIBLE);
        }
    }
    
    /**
     * 注册[上一步]按钮事件
     */
    private void setPreBtn() {
        BootstrapButton preBtn = (BootstrapButton)tab5View.findViewById(R.id.buttonCampaignWeatherPre);
        preBtn.setOnClickListener(this);
    }
    
    /**
     * 注册[保存]按钮事件
     */
    private void setSaveAllBtn() {
        BootstrapButton saveAllBtn = (BootstrapButton)tab5View.findViewById(R.id.buttonSaveAll);
        saveAllBtn.setOnClickListener(this);
    }
    
    
    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                getActivity()).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }
    
    private void initImage(List<String> picList) {

        gridGallery = (GridView) tab5View.findViewById(R.id.confrimGridGallery);
        gridGallery.setFastScrollEnabled(true);
        galleryAdapter = new GalleryAdapter(getActivity(), imageLoader);
        galleryAdapter.setMultiplePick(false);
        gridGallery.setAdapter(galleryAdapter);

        ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();
        for (String string : picList) {
            CustomGallery item = new CustomGallery();
            item.sdcardPath = string;

            dataT.add(item);
        }

        galleryAdapter.addAll(dataT);
    }

    
    
    
    
    /**
     * 统计listview适配器
     */
    public class StatisticsArrayAdapter extends ArrayAdapter<RelayCamapignStatisticsResult> {

        private List<RelayCamapignStatisticsResult> list;
        private Activity context;
        protected Object mActionMode;

        public StatisticsArrayAdapter(Activity context, List<RelayCamapignStatisticsResult> list) {
            super(context, R.layout.activity_fish_result_listview_each_item, list);
            this.context = context;
            this.list = list;
        }

        class ViewHolder {
            protected TextView textPointName;
            protected TextView textFishTypeName;
            protected TextView textWeight;
            protected TextView textCount;
            protected TextView textHookFlagName;
        }

        /**
         * convertView -> viewHolder -> UI
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            
            if (convertView == null) {
                // 添加UI到convertView
                convertView = context.getLayoutInflater().inflate(R.layout.activity_fish_result_listview_each_item, null);
                viewHolder.textPointName = (TextView)convertView.findViewById(R.id.textViewPointEachItem);
                viewHolder.textFishTypeName = (TextView)convertView.findViewById(R.id.textViewFishTypeEachItem);
                viewHolder.textWeight = (TextView)convertView.findViewById(R.id.textViewWeightEachItem);
                viewHolder.textCount = (TextView)convertView.findViewById(R.id.textViewCountEachItem);
                viewHolder.textHookFlagName = (TextView)convertView.findViewById(R.id.textViewHookFlagEachItem);
                viewHolder.textPointName.setTag(list.get(position));//// 保存bean值到UI tag (响应事件从这个UI tag取值)
                convertView.setTag(viewHolder);
            }

            // 设置bean值到UI
            viewHolder.textPointName.setText(list.get(position).getPointName());
            viewHolder.textFishTypeName.setText(list.get(position).getFishTypeName());
            viewHolder.textWeight.setText(list.get(position).getWeight());
            viewHolder.textCount.setText(list.get(position).getCount());
            viewHolder.textHookFlagName.setText(list.get(position).getHookFlagName());
            
            viewHolder.textPointName.setTag(list.get(position));//// 保存bean值到UI tag (响应事件从这个UI tag取值)

            return convertView;
        }

        public int getCount() {
            return list.size();
        }

    }

}
