package com.simple.wildfishingnote.campaigntabs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
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
import com.simple.wildfishingnote.bean.RelayResultStatistics;
import com.simple.wildfishingnote.bean.RodLength;
import com.simple.wildfishingnote.campaign.place.AddPlaceActivity;
import com.simple.wildfishingnote.campaigntabs.Tab2Fragment.PlaceArrayAdapter;
import com.simple.wildfishingnote.common.Common;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.database.CampaignDataSource;

public class Tab4Fragment extends Fragment implements OnClickListener {
    
    GridView gridGallery;
    Handler handler;
    GalleryAdapter adapter;

    ImageView imgSinglePick;
    Button btnGalleryPick;
    BootstrapButton btnGalleryPickMul;

    String action;
    ViewSwitcher viewSwitcher;
    ImageLoader imageLoader;
    
    private CampaignDataSource dataSource;
    private View tab4View;
    private AddMainActivity addMainActivity;
    private StatisticsArrayAdapter arrayAdapter;
    private List<RelayResultStatistics> statisticsList;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tab4View = inflater.inflate(R.layout.activity_tab4, container, false);
        addMainActivity = (AddMainActivity)getActivity();
        dataSource = addMainActivity.getCampaignDataSource();
        
        initPointSpinner();
        initFishTypeSpinner();
        initRadioGroup();
        setAddBtn();
        
        statisticsList = new ArrayList<RelayResultStatistics>();
        ListView listView = (ListView) tab4View.findViewById(R.id.listViewResult);
    	arrayAdapter = new StatisticsArrayAdapter(getActivity(), statisticsList);
        listView.setAdapter(adapter);
        
        // 图片
        initImageLoader();
        initMultiPickBtn();
        
        return tab4View;
    }
    
    /**
     * 监听所有onClick事件
     */
	@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addResultAddBtn:
            	Spinner pointSpinner = (Spinner) tab4View.findViewById(R.id.addResultPointSpinner);
            	Spinner fishTypeSpinner = (Spinner) tab4View.findViewById(R.id.addResultFishTypeSpinner);
            	BootstrapEditText weightEditText = (BootstrapEditText)tab4View.findViewById(R.id.addResultWeightEditText);
            	BootstrapEditText countEditText = (BootstrapEditText)tab4View.findViewById(R.id.addResultCountEditText);
            	RadioGroup radioGroup = (RadioGroup)tab4View.findViewById(R.id.radioGroup);
            	RadioButton rb = (RadioButton)tab4View.findViewById(R.id.addResultInRadio);
            	
            	String pointId = ((Point)pointSpinner.getSelectedItem()).getId();
            	String pointName = pointSpinner.getSelectedItem().toString();
            	String fishTypeId = ((FishType)fishTypeSpinner.getSelectedItem()).getId();
            	String fishTypeName = ((FishType)fishTypeSpinner.getSelectedItem()).getName();
            	String weight = weightEditText.getText().toString();
            	String count = countEditText.getText().toString();
            	String hookFlag = "";
            	int radioButtonId = radioGroup.getCheckedRadioButtonId();
            	if(radioButtonId == R.id.addResultInRadio){
            		hookFlag = "in";
            	} else if(radioButtonId == R.id.addResultOutRadio){
            		hookFlag = "out";
            	}
            	
            	RelayResultStatistics rrs = new RelayResultStatistics();
            	rrs.setPointId(pointId);
            	rrs.setPointName(pointName);
            	rrs.setFishTypeId(fishTypeId);
            	rrs.setFishTypeName(fishTypeName);
            	rrs.setWeight(weight);
            	rrs.setCount(count);
            	rrs.setHookFlag(hookFlag);
            	int id = 1;
            	if(statisticsList.size() > 0){
            		id = Integer.parseInt(statisticsList.get(statisticsList.size() - 1).getId()) + 1;
            	}
            	rrs.setId(String.valueOf(id));
            	statisticsList.add(rrs);
            	arrayAdapter.notifyDataSetChanged();
            	
                break;
            case R.id.buttonCampaignResultPre:
                Common.setCampaignPrefernce(getActivity(), "btn_click", "true");
                ((AddMainActivity)getActivity()).getActionBarReference().setSelectedNavigationItem(2);
                break;
            case R.id.buttonCampaignResultNext:
                break;
        }
    }
	
    /**
     * 注册[添加]按钮事件
     */
    private void setAddBtn() {
        BootstrapButton addBtn = (BootstrapButton)tab4View.findViewById(R.id.addResultAddBtn);
        addBtn.setOnClickListener(this);
    }

    private void initRadioGroup() {
        RadioGroup rg = (RadioGroup)tab4View.findViewById(R.id.radioGroup);
        rg.check(R.id.addResultInRadio);
    }
    
    public void initPointSpinner() {
        dataSource.open();
        Set<String> pointIds = Common.getCampaignPrefernceSet(getActivity(), "campaign_point_ids");
        List<Point> list = dataSource.getPointsById(new ArrayList<String>(pointIds));
        
        Point[] arr = list.toArray(new Point[list.size()]);
        
        Spinner s = (Spinner) tab4View.findViewById(R.id.addResultPointSpinner);
        ArrayAdapter<Point> spinnerArrayAdapter = new ArrayAdapter<Point>(getActivity(),
                android.R.layout.simple_spinner_item, arr);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);  
    }
    
    private void initFishTypeSpinner() {
        dataSource.open();
        List<FishType> list = dataSource.getAllFileTypes();
        
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
        adapter = new GalleryAdapter(getActivity(), imageLoader);
        adapter.setMultiplePick(false);
        gridGallery.setAdapter(adapter);

        viewSwitcher = (ViewSwitcher) tab4View.findViewById(R.id.addResultViewSwitcher);
        viewSwitcher.setDisplayedChild(0);

        btnGalleryPickMul = (BootstrapButton) tab4View.findViewById(R.id.addResultGalleryPickBtn);
        btnGalleryPickMul.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            String[] all_path = data.getStringArrayExtra("all_path");

            ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

            for (String string : all_path) {
                CustomGallery item = new CustomGallery();
                item.sdcardPath = string;

                dataT.add(item);
            }

            viewSwitcher.setDisplayedChild(0);
            adapter.addAll(dataT);
        }
    }
    
    
    
    public class StatisticsArrayAdapter extends ArrayAdapter<RelayResultStatistics> {

        private final List<RelayResultStatistics> list;
        private final Activity context;
        protected Object mActionMode;
        private RadioButton mSelectedRB;

        public StatisticsArrayAdapter(Activity context, List<RelayResultStatistics> list) {
            super(context, R.layout.activity_fish_result_listview_each_item, list);
            this.context = context;
            this.list = list;
        }
        
//        /**
//         * 取得选中raidobutton对应的ID
//         * @return
//         */
//        public String getSelectedId() {
//            String retId = "";
//            for (RelayResultStatistics p : list) {
//                if (p.isSelected()) {
//                    retId = p.getId();
//                    return retId;
//                }
//            }
//
//            return retId;
//        }

        class ViewHolder {
            protected TextView textPointName;
            protected TextView textFishTypeName;
            protected TextView textWeight;
            protected TextView textCount;
            protected TextView textHookFlag;
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
                viewHolder.textHookFlag = (TextView)convertView.findViewById(R.id.textViewHookFlagEachItem);
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
            viewHolder.textHookFlag.setText(list.get(position).getHookFlag());

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

                    final RelayResultStatistics element = (RelayResultStatistics)viewHolder.textPointName.getTag();

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
        private void editStatistics(final RelayResultStatistics element) {
        	Spinner pointSpinner = (Spinner) tab4View.findViewById(R.id.addResultPointSpinner);
        	Spinner fishTypeSpinner = (Spinner) tab4View.findViewById(R.id.addResultFishTypeSpinner);
        	BootstrapEditText weight = (BootstrapEditText)tab4View.findViewById(R.id.addResultWeightEditText);
        	BootstrapEditText count = (BootstrapEditText)tab4View.findViewById(R.id.addResultCountEditText);
        	RadioGroup rg = (RadioGroup)tab4View.findViewById(R.id.radioGroup);
        	
        	dataSource.open();
        	Set<String> pointIds = Common.getCampaignPrefernceSet(getActivity(), "campaign_point_ids");
        	List<Point> pointList = dataSource.getPointsById(new ArrayList<String>(pointIds));
        	int pointSelectedIndex = getPointSelectedIndex(element.getPointId(), pointList);
        	pointSpinner.setSelection(pointSelectedIndex);
        	
        	List<FishType> fishTypeList = dataSource.getAllFileTypes();
        	int fishTypeSelectedIndex = getFishTypeSelectedIndex(element.getFishTypeId(), fishTypeList);
        	fishTypeSpinner.setSelection(fishTypeSelectedIndex);
        	
        	weight.setText(element.getWeight());
        	count.setText(element.getCount());
        	if(element.getHookFlag().equals("in")){
        		rg.check(R.id.addResultInRadio);
        	}else{
        		rg.check(R.id.addResultOutRadio);
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
         * 删除统计信息
         */
        private void deleteStatistics(final RelayResultStatistics element) {
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
                }
            });
            adb.show();
        }
        

        public int getCount() {
            return list.size();
        }

        

    }

}
