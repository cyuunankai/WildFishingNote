package com.simple.wildfishingnote.campaigntabs;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.widget.Toast;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.simple.wildfishingnote.AddMainActivity;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Area;
import com.simple.wildfishingnote.bean.Campaign;
import com.simple.wildfishingnote.bean.Place;
import com.simple.wildfishingnote.campaign.place.AddPlaceActivity;
import com.simple.wildfishingnote.campaign.place.PlaceDetailActivity;
import com.simple.wildfishingnote.common.Common;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.database.CampaignDataSource;

public class Tab2Fragment extends Fragment implements OnClickListener {
	private View tab2View;
	private CampaignDataSource dataSource;
	private PlaceArrayAdapter adapter;
	private AddMainActivity addMainActivity;
	String mPlaceId = null;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tab2View = inflater.inflate(R.layout.activity_tab2, container, false);
        addMainActivity = (AddMainActivity)getActivity();
        dataSource = addMainActivity.getCampaignDataSource();
        
        return tab2View;
    }
	
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            dataSource.open();
            
            initPlaceListViewByDbOrPreference();
            setAddPlaceBtn();
            setSavePlaceBtn();
            setPreBtn();
            setNextBtn();
            setOperationBtnVisibility();
        }
    }
    
    /**
     * 监听所有onClick事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case Constant.ADD_PLACE_ID:
                Intent intent = new Intent(getActivity(), AddPlaceActivity.class);
                startActivityForResult(intent, Constant.REQUEST_CODE_ADD_PLACE);
                break;
            case R.id.buttonSavePlace:
                String selectedPlaceId = getSelectedPlaceId();
                String selectedAreaId = getSelectedAreaId();
                
                if (!checkIsFail(selectedPlaceId, selectedAreaId)) {
                    updateCampaign(selectedPlaceId, selectedAreaId);
                }
                
                break;
            case R.id.buttonCampaignPlacePre:
                Common.setCampaignPrefernce(getActivity(), "btn_click", "true");
                ((AddMainActivity)getActivity()).getActionBarReference().setSelectedNavigationItem(0);
                break;
            case R.id.buttonCampaignPlaceNext:
                setCampaignPlaceToPreference();
                break;
        }
    }
    
    /**
     * 监听所有onActivityResult事件
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 添加/编辑钓位返回
        if ((requestCode == Constant.REQUEST_CODE_ADD_PLACE || requestCode == Constant.REQUEST_CODE_EDIT_PLACE) && resultCode == Activity.RESULT_OK) {
            if (data.getExtras().containsKey(AddPlaceActivity.PLACE_ID)) {
                String placeId = data.getExtras().getString(AddPlaceActivity.PLACE_ID);
                String areaId = data.getExtras().getString(AddPlaceActivity.AREA_ID);
                
                initAreaSpinner(areaId);
                    
                mPlaceId = placeId;
            }
        }
    }
    
    private void initPlaceListViewByDbOrPreference() {
        mPlaceId = null;
        String areaId = null;
        String mode = Common.getCampaignPrefernceString(getActivity(), "campaign_operation_mode");
        if(mode.equals("edit")){
            String campaignId = Common.getCampaignPrefernceString(getActivity(), "campaign_id");
            Campaign campaign = dataSource.getCampaignById(campaignId);
            mPlaceId = campaign.getPlaceId();
            areaId = dataSource.getPlaceById(mPlaceId).getAreaId();
        }else{
            mPlaceId = Common.getCampaignPrefernceString(getActivity(), "campaign_place_id");
            areaId = Common.getCampaignPrefernceString(getActivity(), "campaign_area_id");
            if ("".equals(mPlaceId)) {
                mPlaceId = null;
            }
            if ("".equals(areaId)) {
                areaId = null;
            }
        }
        
        initAreaSpinner(areaId);
        
        initPlaceListView(areaId, mPlaceId);
    }
    
    private void initAreaSpinner(String areaId) {
        dataSource.open();
        List<Area> areaList = dataSource.getAllAreas();
        if(areaId != null){
            int position = getAreaSelectedIndex(areaId, areaList) + 1;
            initAreaSpinner(position, areaList);
        } else {
            initAreaSpinner(0, areaList);
        }
    }
    
    private void initAreaSpinner(int position, List<Area> list) {
    	
        addDefaultValueToAreaSpinner(list);
        
        Area[] arr = list.toArray(new Area[list.size()]);
        
        Spinner s = (Spinner) tab2View.findViewById(R.id.areaSpinner);
        ArrayAdapter<Area> spinnerArrayAdapter = new ArrayAdapter<Area>(getActivity(),
                android.R.layout.simple_spinner_item, arr);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);  
        // 防止页面加载后调用onItemSelected方法
        s.setSelection(position, false);
        
        addOnItemSelectedListener(s);
    }

    private void addDefaultValueToAreaSpinner(List<Area> list) {
        Area obj = new Area();
        obj.setId("0");
        obj.setTitle("请选择");
        list.add(0, obj);
    }

    private void addOnItemSelectedListener(Spinner s) {
        s.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int positon, long id) {
				String areaId = ((Area)parent.getItemAtPosition(positon)).getId();
				onAreaSpinnerChange(areaId);
				initPlaceListView(areaId, mPlaceId);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
        	
		});
    }

    private void setCampaignPlaceToPreference() {
        String selectedPlaceId = getSelectedPlaceId();
        String selectedAreaId = getSelectedAreaId();
        
        if (!checkIsFail(selectedPlaceId, selectedAreaId)) {
            Common.setCampaignPrefernce(getActivity(), "campaign_place_id", selectedPlaceId);
            Common.setCampaignPrefernce(getActivity(), "campaign_area_id", selectedAreaId);
            Common.setCampaignPrefernce(getActivity(), "btn_click", "true");
            ((AddMainActivity)getActivity()).getActionBarReference().setSelectedNavigationItem(2);
        }
        
    }
    
    private void setOperationBtnVisibility() {
        LinearLayout buttonsLayoutCampaignPlaceAdd = (LinearLayout)tab2View.findViewById(R.id.buttonsLayoutCampaignPlaceAdd);
        LinearLayout buttonsLayoutCampaignPlaceEdit = (LinearLayout)tab2View.findViewById(R.id.buttonsLayoutCampaignPlaceEdit);
        String mode = Common.getCampaignPrefernceString(getActivity(), "campaign_operation_mode");
        if (mode.equals("add")) {
            buttonsLayoutCampaignPlaceAdd.setVisibility(View.VISIBLE);
            buttonsLayoutCampaignPlaceEdit.setVisibility(View.INVISIBLE);
        } else if (mode.equals("edit")) {
            buttonsLayoutCampaignPlaceAdd.setVisibility(View.INVISIBLE);
            buttonsLayoutCampaignPlaceEdit.setVisibility(View.VISIBLE);
        }
    }
    
	/**
     * 取得选中的钓位
     */
    private String getSelectedPlaceId() {
        ListView listViewPlace = (ListView)tab2View.findViewById(R.id.listViewPlace);
        PlaceArrayAdapter adapter = (PlaceArrayAdapter)listViewPlace.getAdapter();
        return adapter.getSelectedId();
    }
    
    private int getAreaSelectedIndex(String selectedId, List<Area> list) {
        int selectedIndex = 0;
        for (Area obj : list){
            if(selectedId.equals(obj.getId())) {
                break;
            }
            selectedIndex++;
        }
        return selectedIndex;
    }
    
//    private void setSelectedAreaById(String areaId){
//        List<Area> list = dataSource.getAllAreas();
//        int index = getAreaSelectedIndex(areaId, list);
//        index = index + 1;
//        if(areaId == null){
//            index = 0;
//        }
//        Spinner s = (Spinner) tab2View.findViewById(R.id.areaSpinner);
//        s.setSelection(index);
//    }
    
	/**
     * 取得选中的区域
     */
    private String getSelectedAreaId() {
    	Spinner s = (Spinner) tab2View.findViewById(R.id.areaSpinner);
        return ((Area)s.getSelectedItem()).getId();
    }
    
    /**
     * 更新前check
     */
    private boolean checkIsFail(String selectedPlaceId, String selectedAreaId) {
        boolean ret = false;

        if ("".equals(selectedAreaId)) {
            Toast.makeText(getActivity(), "请选择区域!", Toast.LENGTH_SHORT).show();
            return true;
        }
        
        if ("".equals(selectedPlaceId)) {
            Toast.makeText(getActivity(), "请选择钓位!", Toast.LENGTH_SHORT).show();
            return true;
        }
        
        return ret;
    }
    
	/**
	 * 更新钓位
	 */
    private void updateCampaign(String selectedPlaceId, String selectedAreaId) {
        String campaignId = Common.getCampaignPrefernceString(getActivity(), "campaign_id");
        dataSource.updatePlaceAndArea(campaignId, selectedPlaceId, selectedAreaId);

        Intent intent = getActivity().getIntent();
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
	
	
	
	/**
	 * 初始化钓位listview
	 * @param placeId  是null，list中都不选中
	 *        placeId 不是null，list有选中值
	 */
	private void initPlaceListView(String areaId, String placeId) {
	    dataSource.open();
	    List<Place> list = new ArrayList<Place>();
	    if(areaId != null){
	    	list = dataSource.getPlacesForList(areaId);
	    }
        
        selectedPlaceById(placeId, list);
                
        ListView listView = (ListView) tab2View.findViewById(R.id.listViewPlace);
        adapter = new PlaceArrayAdapter(getActivity(), list);
        listView.setAdapter(adapter);
    }
	
	private void onAreaSpinnerChange(String areaId) {
		dataSource.open();
        List<Place> list = dataSource.getPlacesForList(areaId);
        
        ListView listView = (ListView) tab2View.findViewById(R.id.listViewPlace);
        adapter = new PlaceArrayAdapter(getActivity(), list);
        listView.setAdapter(adapter);
	}

	/**
	 * 根据ID选中钓位
	 */
    private void selectedPlaceById(String placeId, List<Place> list) {
        if (placeId != null) {
            for (int i = 0; i < list.size(); i++) {
                Place p = list.get(i);
                if (p.getId().equals(placeId)) {
                    p.setSelected(true);
                }
            }
        }
    }
    
    /**
     * 注册[保存]按钮事件
     */
    private void setSavePlaceBtn() {
        BootstrapButton savePlaceBtn = (BootstrapButton)tab2View.findViewById(R.id.buttonSavePlace);
        savePlaceBtn.setOnClickListener(this);
    }

    /**
     * 注册[添加]按钮事件
     */
    private void setAddPlaceBtn() {
        ViewGroup parent = (ViewGroup) tab2View.findViewById(R.id.addPlaceRootLayout);
        ImageView imageView = new ImageView(getActivity());
        imageView.setId(Constant.ADD_PLACE_ID);
        imageView.setImageResource(R.drawable.ic_launcher);
        
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.bottomMargin = 150;
        params.rightMargin = 10;
        imageView.setLayoutParams(params);
        parent.addView(imageView);
        
        imageView.setOnClickListener(this);
    }
    
    /**
     * 注册[上一步]按钮事件
     */
    private void setPreBtn() {
        BootstrapButton buttonCampaignPlacePre = (BootstrapButton)tab2View.findViewById(R.id.buttonCampaignPlacePre);
        buttonCampaignPlacePre.setOnClickListener(this);
    }
    
    /**
     * 注册[下一步]按钮事件
     */
    private void setNextBtn() {
        BootstrapButton buttonCampaignPlaceNext = (BootstrapButton)tab2View.findViewById(R.id.buttonCampaignPlaceNext);
        buttonCampaignPlaceNext.setOnClickListener(this);
    }

	
    public class PlaceArrayAdapter extends ArrayAdapter<Place> {

        private final List<Place> list;
        private final Activity context;
        protected Object mActionMode;
        private RadioButton mSelectedRB;

        public PlaceArrayAdapter(Activity context, List<Place> list) {
            super(context, R.layout.activity_place_listview_each_item, list);
            this.context = context;
            this.list = list;
        }
        
        /**
         * 取得选中raidobutton对应的ID
         * @return
         */
        public String getSelectedId() {
            String retId = "";
            for (Place p : list) {
                if (p.isSelected()) {
                    retId = p.getId();
                    return retId;
                }
            }

            return retId;
        }

        class ViewHolder {
            protected TextView textPlaceTitle;
            protected RadioButton radio;
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
                convertView = context.getLayoutInflater().inflate(R.layout.activity_place_listview_each_item, null);
                viewHolder.textPlaceTitle = (TextView)convertView.findViewById(R.id.textViewPlaceTitle);
                viewHolder.radio = (RadioButton)convertView.findViewById(R.id.placeRadio);
                viewHolder.radio.setTag(list.get(position));//// 保存bean值到UI tag (响应事件从这个UI tag取值)
                convertView.setTag(viewHolder);
                
                // 添加事件
                //// radio事件
                addRadioButtonOnCheckedChangeListener(viewHolder);
                //// 内容单击事件->详细页面
                addContentLayoutOnClickListener(viewHolder, convertView);
                //// 内容长按事件->编辑页面，删除
                addContentLayoutOnLongClickListener(viewHolder, convertView);
            }

            // 设置bean值到UI
            viewHolder.textPlaceTitle.setText(list.get(position).getTitle());
            viewHolder.radio.setChecked(list.get(position).isSelected());
            
            viewHolder.radio.setTag(list.get(position));//// 保存bean值到UI tag (响应事件从这个UI tag取值)

            return convertView;
        }

        /**
         * 监听raidobutton选中变更事件
         * @param viewHolder
         */
        private void addRadioButtonOnCheckedChangeListener(
                final ViewHolder viewHolder) {
            viewHolder.radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                        boolean isChecked) {
                    // 画面状态更新到bean中
                    Place element = (Place)viewHolder.radio.getTag();
                    element.setSelected(buttonView.isChecked());

                    // 取消前一次选中
                    if (mSelectedRB != null) {
                        mSelectedRB.setChecked(false);
                    }

                    mSelectedRB = (RadioButton)buttonView;
                }
            });
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

                    final Place element = (Place)viewHolder.radio.getTag();

                    PopupMenu popup = new PopupMenu(context, paramView);
                    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            final String placeId = element.getId();
                            switch (item.getItemId()) {
                                case R.id.edit:
                                    editPlace(placeId);
                                    return true;
                                case R.id.delete:

                                    // db.deletePoint(placeId);

                                    deletePlace(placeId);
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
         * 编辑钓位
         * @param placeId
         */
        private void editPlace(final String placeId) {
            Intent intent = new Intent(context, AddPlaceActivity.class);
            intent.putExtra(AddPlaceActivity.PLACE_ID, placeId);
            startActivityForResult(intent, Constant.REQUEST_CODE_EDIT_PLACE);
        }
        
        /**
         * 删除钓位
         * @param placeId
         */
        private void deletePlace(final String placeId) {
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            adb.setTitle("删除?");
            adb.setMessage("确定删除钓位");
            adb.setNegativeButton(R.string.cancel, null);
            adb.setPositiveButton(R.string.confirm, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dataSource.deletePlace(placeId);
                    
                    int deleteIndex = 0;
                    for (int i = 0; i < list.size(); i++) {
                        if (placeId.equals(list.get(i).getId())) {
                            deleteIndex = i;
                            break;
                        }
                    }
                    list.remove(deleteIndex);
                    adapter.notifyDataSetChanged();
                }
            });
            adb.show();
        }
    

        /**
         * 监听list item单击事件
         * @param viewHolder
         * @param contentLayout
         */
        private void addContentLayoutOnClickListener(final ViewHolder viewHolder, View convertView) {
            
            LinearLayout contentLayout = (LinearLayout)convertView.findViewById(R.id.col1Layout);
            contentLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Place element = (Place)viewHolder.radio.getTag();
                    Intent intent = new Intent(context, PlaceDetailActivity.class);
                    intent.putExtra(PlaceDetailActivity.ID, element.getId());
                    context.startActivity(intent);
                }
            });
        }
        

        public int getCount() {
            return list.size();
        }
        
        @Override
        public int getViewTypeCount() {      
        	if(list.size() == 0){
        		return 1;
        	}else{
        		return list.size();
        	}
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

    }

}
