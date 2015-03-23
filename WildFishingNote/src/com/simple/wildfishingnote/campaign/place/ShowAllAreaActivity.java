package com.simple.wildfishingnote.campaign.place;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Area;
import com.simple.wildfishingnote.database.CampaignDataSource;

public class ShowAllAreaActivity extends ActionBarActivity implements AreaDialogFragment.NoticeDialogListener {

    private CampaignDataSource dataSource;
    private AreaArrayAdapter adapter;
    private String globalAreaId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_area);
        
        dataSource = new CampaignDataSource(this);
        dataSource.open();
        
        initAreaListView();
    }
    
    /**
     * 【区域dialog】画面[OK]按钮按下（添加/更新）
     */
    @Override
	public void onAreaDialogPositiveClick(DialogFragment dialog, String title) {
		if ("".equals(globalAreaId)) {
			addArea(title);
		} else {
			updateArea(title);
		}

		initAreaListView();
	}

	
    
    /**
     * [添加]按钮按下
     */
    public void buttonAddAreaClick(View v){
    	globalAreaId = "";
    	
    	AreaDialogFragment dialog = new AreaDialogFragment("");
        dialog.show(getSupportFragmentManager(), "areaDialog");
    }
    
    /**
     * [返回]按钮按下
     */
    public void buttonBackToAddPlaceClick(View v){
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }
    
    /**
     * 监听系统返回
     */
    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 初始化区域listview
     */
    private void initAreaListView() {
        List<Area> list = dataSource.getAllAreas();
        
        ListView listView = (ListView) findViewById(R.id.listViewArea);
        adapter = new AreaArrayAdapter(this, list);
        listView.setAdapter(adapter);
    }
    
    /**
     * 添加区域
     */
    private void addArea(String title) {
		Area area = new Area();
		area.setTitle(title);
        dataSource.addArea(area);
	}

    /**
     * 更新区域
     */
    private void updateArea(String title) {
        Area area = new Area();
        area.setId(globalAreaId);
        area.setTitle(title);
        dataSource.updateArea(area);
    }
    
    
    
    public class AreaArrayAdapter extends ArrayAdapter<Area> {

        private final List<Area> list;
        private final Activity context;
        protected Object mActionMode;

        public AreaArrayAdapter(Activity context, List<Area> list) {
            super(context, R.layout.activity_area_listview_each_item, list);
            this.context = context;
            this.list = list;
        }

        class ViewHolder {
            protected TextView textAreaTitle;
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
                convertView = context.getLayoutInflater().inflate(R.layout.activity_area_listview_each_item, null);
                viewHolder.textAreaTitle = (TextView)convertView.findViewById(R.id.textViewAreaTitle);
                viewHolder.textAreaTitle.setTag(list.get(position));//// 保存bean值到UI tag (响应事件从这个UI tag取值)
                convertView.setTag(viewHolder);
                
                // 添加事件
                //// 内容长按事件->编辑页面，删除
                addContentLayoutOnLongClickListener(viewHolder, convertView);
            }

            // 设置bean值到UI
            viewHolder.textAreaTitle.setText(list.get(position).getTitle());

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

                    final Area element = (Area)viewHolder.textAreaTitle.getTag();

                    PopupMenu popup = new PopupMenu(context, paramView);
                    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            final String AreaId = element.getId();
                            switch (item.getItemId()) {
                                case R.id.edit:
                                	editArea(AreaId);
                                    return true;
                                case R.id.delete:
                                    deleteArea(AreaId);
                                    return true;
                                default:
                                    return false;
                            }

                        }
                    });
                        

                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.edit_and_delete_menu, popup.getMenu());
                    popup.show();

                    return true;

                }
            });
        }
        
        /**
         * 编辑区域
         */
        private void editArea(final String areaId) {
        	globalAreaId = areaId;
        	Area obj = dataSource.getAreaById(areaId);
        	AreaDialogFragment dialog = new AreaDialogFragment(obj.getTitle());
            dialog.show(getSupportFragmentManager(), "areaDialog");
        }
        
        /**
         * 删除区域
         */
        private void deleteArea(final String areaId) {
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            adb.setTitle("删除?");
            adb.setMessage("确定删除区域");
            adb.setNegativeButton(R.string.cancel, null);
            adb.setPositiveButton(R.string.confirm, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dataSource.deleteArea(areaId);
                    
                    int deleteIndex = 0;
                    for (int i = 0; i < list.size(); i++) {
                        if (areaId.equals(list.get(i).getId())) {
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
        

        public int getCount() {
            return list.size();
        }

        

    }
    
    @Override
    public void onResume() {
      dataSource.open();
      super.onResume();
    }

    @Override
    public void onPause() {
      dataSource.close();
      super.onPause();
    }


}
