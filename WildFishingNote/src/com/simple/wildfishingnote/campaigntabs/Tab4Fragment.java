package com.simple.wildfishingnote.campaigntabs;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luminous.pick.Action;
import com.luminous.pick.CustomGallery;
import com.luminous.pick.GalleryAdapter;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.simple.wildfishingnote.AddMainActivity;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.FishType;
import com.simple.wildfishingnote.bean.Point;
import com.simple.wildfishingnote.bean.RelayCamapignStatisticsResult;
import com.simple.wildfishingnote.common.Common;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.database.CampaignDataSource;
import com.simple.wildfishingnote.utils.FileUtil;

public class Tab4Fragment extends Fragment implements OnClickListener {
    
    GridView gridGallery;
    Handler handler;
    GalleryAdapter galleryAdapter;

    ImageView imgSinglePick;
    Button btnGalleryPick;
    BootstrapButton btnGalleryPickMul;

    String action;
    ImageLoader imageLoader;
    
    private CampaignDataSource dataSource;
    private View tab4View;
    private AddMainActivity addMainActivity;
    private StatisticsArrayAdapter arrayAdapter;
    private List<RelayCamapignStatisticsResult> statisticsList;
    private String statisticsId = "";
    private LayoutInflater mInflater;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = inflater;
        tab4View = inflater.inflate(R.layout.activity_tab4, container, false);
        addMainActivity = (AddMainActivity)getActivity();
        dataSource = addMainActivity.getCampaignDataSource();

        return tab4View;
    }
    
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            dataSource.open();

            initPointSpinner();
            initFishTypeSpinner();
            initRadioGroup();
            setAddBtn();
            setSaveResultBtn();
            setPreBtn();
            setNextBtn();
            setOperationBtnVisibility();
            
            initListView();
            
            // 图片
            initImageLoader();
            initMultiPickBtn();
            initImage();

        }
    }

    private void initListView() {
        
        String mode = Common.getCampaignPrefernceString(getActivity(), "campaign_operation_mode");
        if (mode.equals("edit")) {
            String campaignId = Common.getCampaignPrefernceString(getActivity(), "campaign_id");
            statisticsList = dataSource.getRelayCamapignStatisticsResultList(campaignId);
        } else {
            String fishResultStatisticsListStr = Common.getCampaignPrefernceString(getActivity(), "campaign_fish_result_statistics_list");
            if (!"".equals(fishResultStatisticsListStr)) {
                Type statisticsListType = new TypeToken<List<RelayCamapignStatisticsResult>>() {}.getType();
                statisticsList = new Gson().fromJson(fishResultStatisticsListStr, statisticsListType);
            } else {
                statisticsList = new ArrayList<RelayCamapignStatisticsResult>();
            }
        }
        
        

        ListView listView = (ListView)tab4View.findViewById(R.id.listViewResult);
        if (listView.getHeaderViewsCount() == 0) {
            View header = mInflater.inflate(R.layout.activity_fish_result_listview_header, null);
            listView.addHeaderView(header);
        }
        arrayAdapter = new StatisticsArrayAdapter(getActivity(), statisticsList);
        listView.setAdapter(arrayAdapter);
    }
    
    /**
     * 监听所有onClick事件
     */
	@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addResultAddBtn:
            	refreshStatisticsListView();
            	
                break;
            case R.id.buttonSaveResult:
                update();
                
                break;
            case R.id.buttonCampaignResultPre:
                Common.setCampaignPrefernce(getActivity(), "btn_click", "true");
                ((AddMainActivity)getActivity()).getActionBarReference().setSelectedNavigationItem(2);
                break;
            case R.id.buttonCampaignResultNext:
                List<String> picList = new ArrayList<String>();
                ArrayList<CustomGallery> allT = galleryAdapter.getSelected();
                for (CustomGallery cg : allT) {
                    picList.add(cg.sdcardPath);
                }
                Common.setCampaignPrefernce(getActivity(), "campaign_fish_result_pic_list", new Gson().toJson(picList));
                Common.setCampaignPrefernce(getActivity(), "campaign_fish_result_statistics_list", new Gson().toJson(statisticsList));
                Common.setCampaignPrefernce(getActivity(), "btn_click", "true");
                ((AddMainActivity)getActivity()).getActionBarReference().setSelectedNavigationItem(4);
                break;
        }
    }

    private void update() {
        String campaignId = Common.getCampaignPrefernceString(getActivity(), "campaign_id");
        
        List<String> picList = new ArrayList<String>();
        
        dealImageFile(picList, campaignId);
        dataSource.updateRelayCamapignStatisticsAndImageResult(campaignId, statisticsList, picList);
        
        Intent intent = getActivity().getIntent();
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

	private void dealImageFile(List<String> picList, String campaignId) {
		ArrayList<CustomGallery> allT = galleryAdapter.getSelected();
		
		String toDirectory = getActivity().getApplicationContext().getFilesDir() + Constant.FISH_RESULT_IMAGE_PATH + campaignId + "/";
        try {
        	boolean isImageNotChange = false;
        	for (CustomGallery cg : allT) {
        		if(cg.sdcardPath.startsWith("/data")){
        			isImageNotChange = true;
        			break;
        		}
        	}
        	if(!isImageNotChange){
        		FileUtils.deleteDirectory(new File(toDirectory));
        	}
			
		} catch (IOException e) {
		}
        
        for (CustomGallery cg : allT) {
        	String to = FileUtil.saveImageToInternalStorage(cg.sdcardPath, toDirectory);
            picList.add(to);
        }
	}

	
	private void setOperationBtnVisibility() {
        LinearLayout buttonsLayoutCampaignResultEdit = (LinearLayout)tab4View.findViewById(R.id.buttonsLayoutCampaignResultEdit);
        LinearLayout buttonsLayoutCampaignResultAdd = (LinearLayout)tab4View.findViewById(R.id.buttonsLayoutCampaignResultAdd);
        String mode = Common.getCampaignPrefernceString(getActivity(), "campaign_operation_mode");
        if (mode.equals("add")) {
            buttonsLayoutCampaignResultAdd.setVisibility(View.VISIBLE);
            buttonsLayoutCampaignResultEdit.setVisibility(View.INVISIBLE);
        } else if (mode.equals("edit")) {
            buttonsLayoutCampaignResultAdd.setVisibility(View.INVISIBLE);
            buttonsLayoutCampaignResultEdit.setVisibility(View.VISIBLE);
        }
    }

    private void refreshStatisticsListView() {
        Spinner pointSpinner = (Spinner) tab4View.findViewById(R.id.addResultPointSpinner);
        Spinner fishTypeSpinner = (Spinner) tab4View.findViewById(R.id.addResultFishTypeSpinner);
        BootstrapEditText weightEditText = (BootstrapEditText)tab4View.findViewById(R.id.addResultWeightEditText);
        BootstrapEditText countEditText = (BootstrapEditText)tab4View.findViewById(R.id.addResultCountEditText);
        RadioGroup radioGroup = (RadioGroup)tab4View.findViewById(R.id.radioGroup);
        
        String pointId = ((Point)pointSpinner.getSelectedItem()).getId();
        String pointName = pointSpinner.getSelectedItem().toString();
        String fishTypeId = ((FishType)fishTypeSpinner.getSelectedItem()).getId();
        String fishTypeName = ((FishType)fishTypeSpinner.getSelectedItem()).getName();
        String weight = weightEditText.getText().toString();
        String count = countEditText.getText().toString();
        String hookFlag = "";
        String hookFlagName = "";
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        if(radioButtonId == R.id.addResultInRadio){
        	hookFlag = "in";
        	hookFlagName = getResources().getString(R.string.in);
        } else if(radioButtonId == R.id.addResultOutRadio){
            hookFlag = "out";
            hookFlagName = getResources().getString(R.string.out);
        }
        
        
        if ("".equals(statisticsId)) {
            // 添加
            RelayCamapignStatisticsResult rrs = buildStatistics(pointId, pointName, fishTypeId, fishTypeName, weight, count, hookFlag, hookFlagName);
            arrayAdapter.add(rrs);
        }else{
            // 更新
            buildStatistics(pointId, pointName, fishTypeId, fishTypeName, weight, count, hookFlag, hookFlagName);
            
            arrayAdapter.notifyDataSetChanged();
            statisticsId = "";
        }
    }

    private RelayCamapignStatisticsResult buildStatistics(String pointId, String pointName, String fishTypeId, String fishTypeName, String weight, String count, String hookFlag, String hookFlagName) {
        RelayCamapignStatisticsResult rrs = null;
        
        if ("".equals(statisticsId)) {
            // 添加
            rrs = new RelayCamapignStatisticsResult();
            
            int id = 1;
            if (statisticsList.size() > 0) {
                id = Integer.parseInt(statisticsList.get(statisticsList.size() - 1).getId()) + 1;
            }
            rrs.setId(String.valueOf(id));

        }else{
            // 更新
            rrs = statisticsList.get(getStatisticsSelectedIndex(statisticsId, statisticsList));
            
        }
        rrs.setPointId(pointId);
        rrs.setPointName(pointName);
        rrs.setFishTypeId(fishTypeId);
        rrs.setFishTypeName(fishTypeName);
        rrs.setWeight(weight);
        rrs.setCount(count);
        rrs.setHookFlag(hookFlag);
        rrs.setHookFlagName(hookFlagName);
        
        return rrs;
    }
    
	
	private int getStatisticsSelectedIndex(String selectedId, List<RelayCamapignStatisticsResult> list) {
        int selectedIndex = 0;
        for (RelayCamapignStatisticsResult obj : list){
            if(selectedId.equals(obj.getId())) {
                break;
            }
            selectedIndex++;
        }
        return selectedIndex;
    }
	
    /**
     * 注册[添加]按钮事件
     */
    private void setAddBtn() {
        BootstrapButton addBtn = (BootstrapButton)tab4View.findViewById(R.id.addResultAddBtn);
        addBtn.setOnClickListener(this);
    }
    
    /**
     * 注册[保存]按钮事件
     */
    private void setSaveResultBtn() {
        BootstrapButton btn = (BootstrapButton)tab4View.findViewById(R.id.buttonSaveResult);
        btn.setOnClickListener(this);
    }
    
    /**
     * 注册[上一步]按钮事件
     */
    private void setPreBtn() {
        BootstrapButton preBtn = (BootstrapButton)tab4View.findViewById(R.id.buttonCampaignResultPre);
        preBtn.setOnClickListener(this);
    }
    
    /**
     * 注册[下一步]按钮事件
     */
    private void setNextBtn() {
        BootstrapButton nextBtn = (BootstrapButton)tab4View.findViewById(R.id.buttonCampaignResultNext);
        nextBtn.setOnClickListener(this);
    }

    private void initRadioGroup() {
        RadioGroup rg = (RadioGroup)tab4View.findViewById(R.id.radioGroup);
        rg.check(R.id.addResultInRadio);
    }
    
    public void initPointSpinner() {
        dataSource.open();
        
        List<String> pointIds = null;
        String mode = Common.getCampaignPrefernceString(getActivity(), "campaign_operation_mode");
        if(mode.equals("edit")){
            String campaignId = Common.getCampaignPrefernceString(getActivity(), "campaign_id");
            pointIds = dataSource.getPointIdListByCampaignId(campaignId);
        }else{
            pointIds = Common.getCampaignPrefernceStrList(getActivity(), "campaign_point_id_list");
        }
        
        List<Point> list = dataSource.getPointsByIds(pointIds);
        
        Point[] arr = list.toArray(new Point[list.size()]);
        
        Spinner s = (Spinner) tab4View.findViewById(R.id.addResultPointSpinner);
        ArrayAdapter<Point> spinnerArrayAdapter = new ArrayAdapter<Point>(getActivity(),
                android.R.layout.simple_spinner_item, arr);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);  
    }
    
    private void initFishTypeSpinner() {
        dataSource.open();
        List<FishType> list = dataSource.getAllFishTypes();
        
        FishType[] arr = list.toArray(new FishType[list.size()]);
        
        Spinner s = (Spinner) tab4View.findViewById(R.id.addResultFishTypeSpinner);
        ArrayAdapter<FishType> spinnerArrayAdapter = new ArrayAdapter<FishType>(getActivity(),
                android.R.layout.simple_spinner_item, arr);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);  
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
    

    private void initMultiPickBtn() {

        handler = new Handler();
        gridGallery = (GridView) tab4View.findViewById(R.id.addResultGridGallery);
        gridGallery.setFastScrollEnabled(true);
        galleryAdapter = new GalleryAdapter(getActivity(), imageLoader);
        galleryAdapter.setMultiplePick(false);
        gridGallery.setAdapter(galleryAdapter);

        btnGalleryPickMul = (BootstrapButton) tab4View.findViewById(R.id.addResultGalleryPickBtn);
        btnGalleryPickMul.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, Constant.REQUEST_CODE_PICK_MULTIPLE_IMAGE);
            }
        });

    }
    
    private void initImage() {
    	String mode = Common.getCampaignPrefernceString(getActivity(), "campaign_operation_mode");
        if (mode.equals("edit")) {
            String campaignId = Common.getCampaignPrefernceString(getActivity(), "campaign_id");
            List<String> picList = dataSource.getFishResultPicList(campaignId);
            
            String directory = getActivity().getApplicationContext().getFilesDir() + Constant.FISH_RESULT_IMAGE_PATH + campaignId + "/";
            ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();
            
            for (String fileName : picList) {
                CustomGallery item = new CustomGallery();
                item.isSeleted = true;
                item.sdcardPath = directory + fileName;

                dataT.add(item);
            }

            galleryAdapter.addAll(dataT);
        } 
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQUEST_CODE_PICK_MULTIPLE_IMAGE && resultCode == Activity.RESULT_OK) {
            String[] all_path = data.getStringArrayExtra("all_path");

            ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

            for (String string : all_path) {
                CustomGallery item = new CustomGallery();
                item.isSeleted = true;
                item.sdcardPath = string;

                dataT.add(item);
            }

            galleryAdapter.addAll(dataT);
        }
    }
    
    private int getPointSelectedIndex(String selectedId, List<Point> list) {
        int selectedIndex = 0;
        for (Point obj : list){
            if(selectedId.equals(obj.getId())) {
                break;
            }
            selectedIndex++;
        }
        return selectedIndex;
    }
    
    private int getFishTypeSelectedIndex(String selectedId, List<FishType> list) {
        int selectedIndex = 0;
        for (FishType obj : list){
            if(selectedId.equals(obj.getId())) {
                break;
            }
            selectedIndex++;
        }
        return selectedIndex;
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
                
                // 添加事件
                //// 内容长按事件->编辑页面，删除
                addContentLayoutOnLongClickListener(viewHolder, convertView);
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

        /**
         * 监听list item长按事件
         * @param viewHolder
         * @param contentLayout
         */
        private void addContentLayoutOnLongClickListener(final ViewHolder viewHolder, View convertView) {
            
            LinearLayout contentLayout = (LinearLayout)convertView.findViewById(R.id.col1Layout);
            contentLayout.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View paramView) {

                    final RelayCamapignStatisticsResult element = (RelayCamapignStatisticsResult)viewHolder.textPointName.getTag();

                    PopupMenu popup = new PopupMenu(context, paramView);
                    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.edit:
                                	editStatistics(element);
                                    return true;
                                case R.id.delete:
                                	deleteStatistics(element);
                                    return true;
                                default:
                                    return false;
                            }

                        }
                    });
                        

                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.select_place_context_menu, popup.getMenu());
                    popup.show();

                    return true;

                }
            });
        }
        
        /**
         * 编辑统计信息
         */
        private void editStatistics(final RelayCamapignStatisticsResult element) {
        	Spinner pointSpinner = (Spinner) tab4View.findViewById(R.id.addResultPointSpinner);
        	Spinner fishTypeSpinner = (Spinner) tab4View.findViewById(R.id.addResultFishTypeSpinner);
        	BootstrapEditText weight = (BootstrapEditText)tab4View.findViewById(R.id.addResultWeightEditText);
        	BootstrapEditText count = (BootstrapEditText)tab4View.findViewById(R.id.addResultCountEditText);
        	RadioGroup rg = (RadioGroup)tab4View.findViewById(R.id.radioGroup);
        	
        	dataSource.open();
        	List<String> pointIds = Common.getCampaignPrefernceStrList(getActivity(), "campaign_point_id_list");
        	List<Point> pointList = dataSource.getPointsByIds(pointIds);
        	int pointSelectedIndex = getPointSelectedIndex(element.getPointId(), pointList);
        	pointSpinner.setSelection(pointSelectedIndex);
        	
        	List<FishType> fishTypeList = dataSource.getAllFishTypes();
        	int fishTypeSelectedIndex = getFishTypeSelectedIndex(element.getFishTypeId(), fishTypeList);
        	fishTypeSpinner.setSelection(fishTypeSelectedIndex);
        	
        	weight.setText(element.getWeight());
        	count.setText(element.getCount());
        	if(element.getHookFlag().equals("in")){
        		rg.check(R.id.addResultInRadio);
        	}else{
        		rg.check(R.id.addResultOutRadio);
        	}
        	
        	statisticsId = element.getId();
            
        }
        
        /**
         * 删除统计信息
         */
        private void deleteStatistics(final RelayCamapignStatisticsResult element) {
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            adb.setTitle("删除?");
            adb.setMessage("确定删除该记录");
            adb.setNegativeButton(R.string.cancel, null);
            adb.setPositiveButton(R.string.confirm, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    
                    int deleteIndex = 0;
                    for (int i = 0; i < list.size(); i++) {
                        if (element.getId().equals(list.get(i).getId())) {
                            deleteIndex = i;
                            break;
                        }
                    }
                    list.remove(deleteIndex);
                    arrayAdapter.notifyDataSetChanged();
                    
                    statisticsId = "";
                }
            });
            adb.show();
        }
        

        public int getCount() {
            return list.size();
        }

    }

}
