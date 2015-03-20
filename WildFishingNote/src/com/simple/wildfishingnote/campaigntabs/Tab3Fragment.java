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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.gson.Gson;
import com.simple.wildfishingnote.AddMainActivity;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Point;
import com.simple.wildfishingnote.campaign.point.AddPointActivity;
import com.simple.wildfishingnote.campaign.point.PointDetailActivity;
import com.simple.wildfishingnote.common.Common;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.database.CampaignDataSource;

public class Tab3Fragment extends Fragment implements OnClickListener {
    
    private View tab3View;
    private CampaignDataSource dataSource;
    private PointArrayAdapter adapter;
    private AddMainActivity addMainActivity;
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
	    // Inflate the layout for this fragment
        tab3View = inflater.inflate(R.layout.activity_tab3, container, false);

        addMainActivity = (AddMainActivity)getActivity();
        dataSource = addMainActivity.getCampaignDataSource();
        
        return tab3View;
    }
	
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            dataSource.open();
            
            initPointListViewByDbOrPreference();
            setAddPointBtn();
            setSavePointBtn();
            setPreBtn();
            setNextBtn();
            setOperationBtnVisibility();
        }
    }

    private void initPointListViewByDbOrPreference() {
        List<String> pointIdList = null;
        String mode = Common.getCampaignPrefernceString(getActivity(), "campaign_operation_mode");
        if(mode.equals("edit")){
            String campaignId = Common.getCampaignPrefernceString(getActivity(), "campaign_id");
            pointIdList = dataSource.getPointIdListByCampaignId(campaignId);
        }else{
            pointIdList = Common.getCampaignPrefernceStrList(getActivity(), "campaign_point_id_list");
        }
        initPointListView(pointIdList);
    }

	/**
     * 监听所有onClick事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case Constant.ADD_POINT_ID:
                Intent intent = new Intent(getActivity(), AddPointActivity.class);
                startActivityForResult(intent, Constant.REQUEST_CODE_ADD_POINT);
                break;
            case R.id.buttonSavePoint:
                List<String> selectedPointIds = getSelectedPointIds();
                
                if (checkIsFail(selectedPointIds)) {
                    return;
                }
                
                addRelayCampaignPoint(selectedPointIds);
                break;
            case R.id.buttonCampaignPointPre:
                Common.setCampaignPrefernce(getActivity(), "btn_click", "true");
                ((AddMainActivity)getActivity()).getActionBarReference().setSelectedNavigationItem(1);
                break;
            case R.id.buttonCampaignPointNext:
                setCampaignPointToPreference();
                break;
        }
    }
    
    private void setOperationBtnVisibility() {
        LinearLayout buttonsLayoutCmapginPointEdit = (LinearLayout)tab3View.findViewById(R.id.buttonsLayoutCampaignPointEdit);
        LinearLayout buttonsLayoutCmapginPointAdd = (LinearLayout)tab3View.findViewById(R.id.buttonsLayoutCampaignPointAdd);
        String mode = Common.getCampaignPrefernceString(getActivity(), "campaign_operation_mode");
        if (mode.equals("add")) {
            buttonsLayoutCmapginPointAdd.setVisibility(View.VISIBLE);
            buttonsLayoutCmapginPointEdit.setVisibility(View.INVISIBLE);
        } else if (mode.equals("edit")) {
            buttonsLayoutCmapginPointAdd.setVisibility(View.INVISIBLE);
            buttonsLayoutCmapginPointEdit.setVisibility(View.VISIBLE);
        }
    }

    private void setCampaignPointToPreference() {
        List<String> selectedPointIds = getSelectedPointIds();
        
        if (checkIsFail(selectedPointIds)) {
            return;
        }
        
        Common.setCampaignPrefernce(getActivity(), "campaign_point_id_list", new Gson().toJson(selectedPointIds));
        Common.setCampaignPrefernce(getActivity(), "btn_click", "true");
        ((AddMainActivity)getActivity()).getActionBarReference().setSelectedNavigationItem(3);
    }
    
    /**
     * 监听所有onActivityResult事件
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 添加钓点返回
        if (requestCode == Constant.REQUEST_CODE_ADD_POINT && resultCode == Activity.RESULT_OK) {
            if (data.getExtras().containsKey(AddPointActivity.POINT_ID)) {
                String pointId = data.getExtras().getString(AddPointActivity.POINT_ID);
                List<String> selectedPointIds = getSelectedPointIds();
                selectedPointIds.add(pointId);
                initPointListView(selectedPointIds);
            }
        }
        
        // 编辑钓点返回
        if (requestCode == Constant.REQUEST_CODE_EDIT_POINT && resultCode == Activity.RESULT_OK) {
            initPointListView(null);
        }
    }
    
    /**
     * 取得选中的钓点
     */
    private List<String> getSelectedPointIds() {
        return adapter.getSelectedIds();
    }
    
    /**
     * 更新前check
     */
    private boolean checkIsFail(List<String> selectedPointIds) {
        boolean ret = false;
//        if (addMainActivity.getCampaignId() == 0) {
//            Toast.makeText(getActivity(), "请先保存时间!", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        
//        Campaign c = dataSource.getCampaignById(String.valueOf(addMainActivity.getCampaignId()));
//        if("".equals(getPlaceId())){
//            Toast.makeText(getActivity(), "请先保存钓位!", Toast.LENGTH_SHORT).show();
//            return true;
//        }

        if (selectedPointIds.size() == 0) {
            Toast.makeText(getActivity(), "请选择钓点!", Toast.LENGTH_SHORT).show();
            return true;
        }
        
        return ret;
    }
    
    /**
     * 更新钓位
     */
    private void addRelayCampaignPoint(List<String> selectedPointIds) {
        String campaignId = Common.getCampaignPrefernceString(getActivity(), "campaign_id");
        dataSource.updateRelayCampaignPoint(campaignId, selectedPointIds);
        
        Intent intent = getActivity().getIntent();
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
    
    /**
     * 初始化钓点listview
     * @param PointId  是null，list中都不选中
     *        PointId 不是null，list有选中值
     */
    private void initPointListView(List<String> pointIdList) {
        dataSource.open();
        List<Point> list = dataSource.getAllPoints();
        
        selectedPointById(pointIdList, list);
                
        ListView listView = (ListView) tab3View.findViewById(R.id.listViewPoint);
        adapter = new PointArrayAdapter(getActivity(), list);
        listView.setAdapter(adapter);
    }

    /**
     * 根据ID选中钓点
     */
    private void selectedPointById(List<String> selectedPointIdList, List<Point> allList) {
        if (selectedPointIdList != null) {
            for (int i = 0; i < allList.size(); i++) {
                Point p = allList.get(i);
                if (selectedPointIdList.contains(p.getId())) {
                    p.setSelected(true);
                }
            }
        }
    }
    
    /**
     * 注册[保存]按钮事件
     */
    private void setSavePointBtn() {
        BootstrapButton savePointBtn = (BootstrapButton)tab3View.findViewById(R.id.buttonSavePoint);
        savePointBtn.setOnClickListener(this);
    }

    /**
     * 注册[添加]按钮事件
     */
    private void setAddPointBtn() {
        ViewGroup parent = (ViewGroup) tab3View.findViewById(R.id.addPointRootLayout);
        ImageView imageView = new ImageView(getActivity());
        imageView.setId(Constant.ADD_POINT_ID);
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
        BootstrapButton buttonCampaignPointPre = (BootstrapButton)tab3View.findViewById(R.id.buttonCampaignPointPre);
        buttonCampaignPointPre.setOnClickListener(this);
    }
    
    /**
     * 注册[下一步]按钮事件
     */
    private void setNextBtn() {
        BootstrapButton buttonCampaignPointNext = (BootstrapButton)tab3View.findViewById(R.id.buttonCampaignPointNext);
        buttonCampaignPointNext.setOnClickListener(this);
    }
    
    public class PointArrayAdapter extends ArrayAdapter<Point> {

        private final List<Point> list;
        private final Activity context;
        protected Object mActionMode;

        public PointArrayAdapter(Activity context, List<Point> list) {
            super(context, R.layout.activity_point_listview_each_item, list);
            this.context = context;
            this.list = list;
        }
        
        /**
         * 取得选中check对应的ids
         * @return
         */
        public List<String> getSelectedIds() {
            List<String> retIds = new ArrayList<String>();

            for (Point p : list) {
                if (p.isSelected()) {
                    retIds.add(p.getId());
                }
            }

            return retIds;
        }

        class ViewHolder {
            protected TextView textViewDepth;
            protected TextView textViewRodLength;
            protected TextView textViewLureMethod;
            protected TextView textViewBait;
            protected CheckBox check;
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
                convertView = context.getLayoutInflater().inflate(R.layout.activity_point_listview_each_item, null);
                viewHolder.textViewDepth = (TextView)convertView.findViewById(R.id.textViewDepthEachItem);
                viewHolder.textViewRodLength = (TextView)convertView.findViewById(R.id.textViewRodLengthEachItem);
                viewHolder.textViewLureMethod = (TextView)convertView.findViewById(R.id.textViewLureMethodEachItem);
                viewHolder.textViewBait = (TextView)convertView.findViewById(R.id.textViewBaitEachItem);
                viewHolder.check = (CheckBox)convertView.findViewById(R.id.checkEachItem);
                viewHolder.check.setTag(list.get(position));//// 保存bean值到UI tag (响应事件从这个UI tag取值)
                convertView.setTag(viewHolder);
                
                // 添加事件
                //// check事件
                addCheckBoxOnCheckedChangeListener(viewHolder);
                //// 内容单击事件->详细页面
                addContentLayoutOnClickListener(viewHolder, convertView);
                //// 内容长按事件->编辑页面，删除
                addContentLayoutOnLongClickListener(viewHolder, convertView);
            }

            // 设置bean值到UI
            viewHolder.textViewDepth.setText(list.get(position).getDepth() + "米");
            viewHolder.textViewRodLength.setText(list.get(position).getRodLengthName() + "米");
            viewHolder.textViewLureMethod.setText(list.get(position).getLureMethodName());
            viewHolder.textViewBait.setText(list.get(position).getBaitName());
            viewHolder.check.setChecked(list.get(position).isSelected());
            
            viewHolder.check.setTag(list.get(position));//// 保存bean值到UI tag (响应事件从这个UI tag取值)

            return convertView;
        }

        /**
         * 监听raidobutton选中变更事件
         * @param viewHolder
         */
        private void addCheckBoxOnCheckedChangeListener(final ViewHolder viewHolder) {
            viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // 画面状态更新到bean中
                    Point element = (Point)viewHolder.check.getTag();
                    element.setSelected(buttonView.isChecked());
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

                    final Point element = (Point)viewHolder.check.getTag();

                    PopupMenu popup = new PopupMenu(context, paramView);
                    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            final String pointId = element.getId();
                            switch (item.getItemId()) {
                                case R.id.edit:
                                    editPoint(pointId);
                                    return true;
                                case R.id.delete:
                                    deletePoint(pointId);
                                    return true;
                                default:
                                    return false;
                            }

                        }
                    });
                        

                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.select_point_context_menu, popup.getMenu());
                    popup.show();

                    return true;

                }
            });
        }
        
        /**
         * 编辑钓点
         * @param PointId
         */
        private void editPoint(final String pointId) {
            Intent intent = new Intent(context, AddPointActivity.class);
            intent.putExtra(AddPointActivity.POINT_ID, pointId);
            startActivityForResult(intent, Constant.REQUEST_CODE_EDIT_POINT);
        }
        
        /**
         * 删除钓点
         * @param PointId
         */
        private void deletePoint(final String pointId) {
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            adb.setTitle("删除?");
            adb.setMessage("确定删除钓点");
            adb.setNegativeButton(R.string.cancel, null);
            adb.setPositiveButton(R.string.confirm, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dataSource.deletePoint(pointId);
                    
                    int deleteIndex = 0;
                    for (int i = 0; i < list.size(); i++) {
                        if (pointId.equals(list.get(i).getId())) {
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
                    Point element = (Point)viewHolder.check.getTag();
                    Intent intent = new Intent(context, PointDetailActivity.class);
                    intent.putExtra(PointDetailActivity.ID, element.getId());
                    context.startActivity(intent);
                }
            });
        }
        

        public int getCount() {
            return list.size();
        }

        @Override
        public int getViewTypeCount() {                 
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

    }

}
