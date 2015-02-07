package com.simple.wildfishingnote.campaign.point;

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
import com.simple.wildfishingnote.bean.RodLength;
import com.simple.wildfishingnote.database.CampaignDataSource;

public class ShowAllRodLengthActivity extends ActionBarActivity implements RodLengthDialogFragment.NoticeDialogListener {

    private CampaignDataSource dataSource;
    private RodLengthArrayAdapter adapter;
    private String globalRodLengthId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_rod_length);
        
        dataSource = new CampaignDataSource(this);
        dataSource.open();
        
        initPointListView();
    }
    
    /**
     * 【竿长dialog】画面[OK]按钮按下（添加/更新）
     */
    @Override
	public void onRLDialogPositiveClick(DialogFragment dialog, String rodLength) {
		if ("".equals(globalRodLengthId)) {
			addRodLength(rodLength);
		} else {
			updateRodLength(rodLength);
		}

		initPointListView();
	}

	
    
    /**
     * [添加]按钮按下
     */
    public void buttonAddRodLengthClick(View v){
    	globalRodLengthId = "";
    	
    	RodLengthDialogFragment dialog = new RodLengthDialogFragment("");
        dialog.show(getSupportFragmentManager(), "rodLengthDialog");
    }
    
    /**
     * [返回]按钮按下
     */
    public void buttonBackToAddPointClick(View v){
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
     * 初始化竿长listview
     */
    private void initPointListView() {
        List<RodLength> list = dataSource.getAllRodLengths();
        
        ListView listView = (ListView) findViewById(R.id.listViewRodLength);
        adapter = new RodLengthArrayAdapter(this, list);
        listView.setAdapter(adapter);
    }
    
    /**
     * 添加竿长
     */
    private void addRodLength(String rodLength) {
		RodLength rl = new RodLength();
        rl.setName(rodLength);
        dataSource.addRodLength(rl);
	}

    /**
     * 更新竿长
     */
    private void updateRodLength(String rodLength) {
        RodLength rl = new RodLength();
        rl.setId(globalRodLengthId);
        rl.setName(rodLength);
        dataSource.updateRodLength(rl);
    }
    
    
    
    public class RodLengthArrayAdapter extends ArrayAdapter<RodLength> {

        private final List<RodLength> list;
        private final Activity context;
        protected Object mActionMode;

        public RodLengthArrayAdapter(Activity context, List<RodLength> list) {
            super(context, R.layout.activity_rod_length_listview_each_item, list);
            this.context = context;
            this.list = list;
        }

        class ViewHolder {
            protected TextView textRodLengthTitle;
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
                convertView = context.getLayoutInflater().inflate(R.layout.activity_rod_length_listview_each_item, null);
                viewHolder.textRodLengthTitle = (TextView)convertView.findViewById(R.id.textViewRodLengthTitle);
                viewHolder.textRodLengthTitle.setTag(list.get(position));//// 保存bean值到UI tag (响应事件从这个UI tag取值)
                convertView.setTag(viewHolder);
                
                // 添加事件
                //// 内容长按事件->编辑页面，删除
                addContentLayoutOnLongClickListener(viewHolder, convertView);
            }

            // 设置bean值到UI
            viewHolder.textRodLengthTitle.setText(list.get(position).getName() + "米");

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

                    final RodLength element = (RodLength)viewHolder.textRodLengthTitle.getTag();

                    PopupMenu popup = new PopupMenu(context, paramView);
                    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            final String rodLengthId = element.getId();
                            switch (item.getItemId()) {
                                case R.id.edit:
                                	editRodLength(rodLengthId);
                                    return true;
                                case R.id.delete:
                                    deleteRodLength(rodLengthId);
                                    return true;
                                default:
                                    return false;
                            }

                        }
                    });
                        

                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.show_all_rod_length_menu, popup.getMenu());
                    popup.show();

                    return true;

                }
            });
        }
        
        /**
         * 编辑竿长
         */
        private void editRodLength(final String rodLengthId) {
        	globalRodLengthId = rodLengthId;
        	RodLength rl = dataSource.getRodLengthById(rodLengthId);
        	RodLengthDialogFragment dialog = new RodLengthDialogFragment(rl.getName());
            dialog.show(getSupportFragmentManager(), "rodLengthDialog");
        }
        
        /**
         * 删除竿长
         */
        private void deleteRodLength(final String rodLengthId) {
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            adb.setTitle("删除?");
            adb.setMessage("确定删除竿长");
            adb.setNegativeButton(R.string.cancel, null);
            adb.setPositiveButton(R.string.confirm, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dataSource.deleteRodLength(rodLengthId);
                    
                    int deleteIndex = 0;
                    for (int i = 0; i < list.size(); i++) {
                        if (rodLengthId.equals(list.get(i).getId())) {
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
