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
import com.simple.wildfishingnote.bean.LureMethod;
import com.simple.wildfishingnote.database.CampaignDataSource;

public class ShowAllLureMethodActivity extends ActionBarActivity implements LureMethodDialogFragment.NoticeDialogListener {

    private CampaignDataSource dataSource;
    private LureMethodArrayAdapter adapter;
    private String globalLureMethodId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_lure_method);
        
        dataSource = new CampaignDataSource(this);
        dataSource.open();
        
        initLureMethodListView();
    }
    
    /**
     * 【打窝方法dialog】画面[OK]按钮按下（添加/更新）
     */
    @Override
	public void onLMDialogPositiveClick(DialogFragment dialog, String name, String detail) {
		if ("".equals(globalLureMethodId)) {
			addLureMethod(name, detail);
		} else {
		    updateLureMethod(name, detail);
		}

		initLureMethodListView();
	}

	
    
    /**
     * [添加]按钮按下
     */
    public void buttonAddLureMethodClick(View v){
    	globalLureMethodId = "";
    	
    	LureMethodDialogFragment dialog = new LureMethodDialogFragment("", "");
        dialog.show(getSupportFragmentManager(), "lureMethodDialog");
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
     * 初始化打窝方法listview
     */
    private void initLureMethodListView() {
        List<LureMethod> list = dataSource.getAllLureMethods();
        
        ListView listView = (ListView) findViewById(R.id.listViewLureMethod);
        adapter = new LureMethodArrayAdapter(this, list);
        listView.setAdapter(adapter);
    }
    
    /**
     * 添加打窝方法
     */
    private void addLureMethod(String name, String detail) {
		LureMethod obj = new LureMethod();
        obj.setName(name);
        obj.setDetail(detail);
        dataSource.addLureMethod(obj);
	}

    /**
     * 更新打窝方法
     */
    private void updateLureMethod(String name, String detail) {
        LureMethod obj = new LureMethod();
        obj.setName(name);
        obj.setDetail(detail);
        obj.setId(globalLureMethodId);
        dataSource.updateLureMethod(obj);
    }
    
    
    
    public class LureMethodArrayAdapter extends ArrayAdapter<LureMethod> {

        private final List<LureMethod> list;
        private final Activity context;
        protected Object mActionMode;

        public LureMethodArrayAdapter(Activity context, List<LureMethod> list) {
            super(context, R.layout.activity_lure_method_listview_each_item, list);
            this.context = context;
            this.list = list;
        }

        class ViewHolder {
            protected TextView textLureMethodTitle;
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
                convertView = context.getLayoutInflater().inflate(R.layout.activity_lure_method_listview_each_item, null);
                viewHolder.textLureMethodTitle = (TextView)convertView.findViewById(R.id.textViewLureMethodTitle);
                viewHolder.textLureMethodTitle.setTag(list.get(position));//// 保存bean值到UI tag (响应事件从这个UI tag取值)
                convertView.setTag(viewHolder);
                
                // 添加事件
                //// 内容长按事件->编辑页面，删除
                addContentLayoutOnLongClickListener(viewHolder, convertView);
            }

            // 设置bean值到UI
            viewHolder.textLureMethodTitle.setText(list.get(position).getName());

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

                    final LureMethod element = (LureMethod)viewHolder.textLureMethodTitle.getTag();

                    PopupMenu popup = new PopupMenu(context, paramView);
                    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            final String lureMethodId = element.getId();
                            switch (item.getItemId()) {
                                case R.id.edit:
                                	editLureMethod(lureMethodId);
                                    return true;
                                case R.id.delete:
                                    deleteLureMethod(lureMethodId);
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
         * 编辑打窝方法
         */
        private void editLureMethod(final String lureMethodId) {
        	globalLureMethodId = lureMethodId;
        	LureMethod obj = dataSource.getLureMethodById(lureMethodId);
        	LureMethodDialogFragment dialog = new LureMethodDialogFragment(obj.getName(), obj.getDetail());
            dialog.show(getSupportFragmentManager(), "lureMethodDialog");
        }
        
        /**
         * 删除打窝方法
         */
        private void deleteLureMethod(final String lureMethodId) {
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            adb.setTitle("删除?");
            adb.setMessage("确定删除打窝方法");
            adb.setNegativeButton(R.string.cancel, null);
            adb.setPositiveButton(R.string.confirm, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dataSource.deleteLureMethod(lureMethodId);
                    
                    int deleteIndex = 0;
                    for (int i = 0; i < list.size(); i++) {
                        if (lureMethodId.equals(list.get(i).getId())) {
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
