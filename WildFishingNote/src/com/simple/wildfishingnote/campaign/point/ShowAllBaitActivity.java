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
import com.simple.wildfishingnote.bean.Bait;
import com.simple.wildfishingnote.database.CampaignDataSource;

public class ShowAllBaitActivity extends ActionBarActivity implements BaitDialogFragment.NoticeDialogListener {

    private CampaignDataSource dataSource;
    private BaitArrayAdapter adapter;
    private String globalBaitId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_bait);
        
        dataSource = new CampaignDataSource(this);
        dataSource.open();
        
        initBaitListView();
    }
    
    /**
     * 【饵料dialog】画面[OK]按钮按下（添加/更新）
     */
    @Override
	public void onBaitDialogPositiveClick(DialogFragment dialog, String name, String detail) {
		if ("".equals(globalBaitId)) {
			addBait(name, detail);
		} else {
		    updateBait(name, detail);
		}

		initBaitListView();
	}

	
    
    /**
     * [添加]按钮按下
     */
    public void buttonAddBaitClick(View v){
    	globalBaitId = "";
    	
    	BaitDialogFragment dialog = new BaitDialogFragment("", "");
        dialog.show(getSupportFragmentManager(), "baitDialog");
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
     * 初始化饵料listview
     */
    private void initBaitListView() {
        List<Bait> list = dataSource.getAllBaits();
        
        ListView listView = (ListView) findViewById(R.id.listViewBait);
        adapter = new BaitArrayAdapter(this, list);
        listView.setAdapter(adapter);
    }
    
    /**
     * 添加饵料
     */
    private void addBait(String name, String detail) {
		Bait obj = new Bait();
        obj.setName(name);
        obj.setDetail(detail);
        dataSource.addBait(obj);
	}

    /**
     * 更新饵料
     */
    private void updateBait(String name, String detail) {
        Bait obj = new Bait();
        obj.setName(name);
        obj.setDetail(detail);
        obj.setId(globalBaitId);
        dataSource.updateBait(obj);
    }
    
    
    
    public class BaitArrayAdapter extends ArrayAdapter<Bait> {

        private final List<Bait> list;
        private final Activity context;
        protected Object mActionMode;

        public BaitArrayAdapter(Activity context, List<Bait> list) {
            super(context, R.layout.activity_bait_listview_each_item, list);
            this.context = context;
            this.list = list;
        }

        class ViewHolder {
            protected TextView textBaitTitle;
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
                convertView = context.getLayoutInflater().inflate(R.layout.activity_bait_listview_each_item, null);
                viewHolder.textBaitTitle = (TextView)convertView.findViewById(R.id.textViewBaitTitle);
                viewHolder.textBaitTitle.setTag(list.get(position));//// 保存bean值到UI tag (响应事件从这个UI tag取值)
                convertView.setTag(viewHolder);
                
                // 添加事件
                //// 内容长按事件->编辑页面，删除
                addContentLayoutOnLongClickListener(viewHolder, convertView);
            }

            // 设置bean值到UI
            viewHolder.textBaitTitle.setText(list.get(position).getName());

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

                    final Bait element = (Bait)viewHolder.textBaitTitle.getTag();

                    PopupMenu popup = new PopupMenu(context, paramView);
                    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            final String baitId = element.getId();
                            switch (item.getItemId()) {
                                case R.id.edit:
                                	editBait(baitId);
                                    return true;
                                case R.id.delete:
                                    deleteBait(baitId);
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
         * 编辑饵料
         */
        private void editBait(final String baitId) {
        	globalBaitId = baitId;
        	Bait obj = dataSource.getBaitById(baitId);
        	BaitDialogFragment dialog = new BaitDialogFragment(obj.getName(), obj.getDetail());
            dialog.show(getSupportFragmentManager(), "baitDialog");
        }
        
        /**
         * 删除饵料
         */
        private void deleteBait(final String baitId) {
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            adb.setTitle("删除?");
            adb.setMessage("确定删除饵料");
            adb.setNegativeButton(R.string.cancel, null);
            adb.setPositiveButton(R.string.confirm, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dataSource.deleteBait(baitId);
                    
                    int deleteIndex = 0;
                    for (int i = 0; i < list.size(); i++) {
                        if (baitId.equals(list.get(i).getId())) {
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
