package com.simple.wildfishingnote.campaign.point;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.RodLength;
import com.simple.wildfishingnote.database.CampaignDataSource;

public class ShowAllRodLengthActivity extends ActionBarActivity implements RodLengthDialogFragment.NoticeDialogListener {

    private CampaignDataSource dataSource;
    private String globalRodLengthId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_rod_length);
        
        dataSource = new CampaignDataSource(this);
        dataSource.open();
        
        initPointListView();
        addListViewOnItemLongClickListener();
    }
    
    /**
     * 【更新竿长】画面[OK]按钮按下
     */
    @Override
    public void onRLDialogPositiveClick(DialogFragment dialog, String rodLength) {
        updateRodLength(rodLength);
        initPointListView();
    }
    
    /**
     * [返回]按钮按下
     */
    public void buttonBackToAddPoint(View v){
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
     * 监听listView 
     */
    private void addListViewOnItemLongClickListener() {
        
        ListView listView = (ListView) findViewById(R.id.listViewRodLength);
        
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int position, long id){
                
                List<RodLength> list = dataSource.getAllRodLengths();
                final RodLength rl = list.get(position);
                globalRodLengthId = rl.getId();
                
                PopupMenu popup = new PopupMenu(getApplicationContext(), view);
                popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        
                        switch (item.getItemId()) {
                            case R.id.edit:
                                RodLengthDialogFragment dialog = new RodLengthDialogFragment(rl.getName());
                                dialog.show(getSupportFragmentManager(), "editRodLengthDialog");
                                return true;
                            case R.id.delete:
                                
                                return true;
                            default:
                                return false;
                        }

                    }
                });
                    

                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.show_all_rod_length_menu, popup.getMenu());
                popup.show();
                
                return false;
            }
        });
    }
    

    
    /**
     * 初始化竿长listview
     */
    private void initPointListView() {
        List<RodLength> list = dataSource.getAllRodLengths();
        
        ListView listView = (ListView) findViewById(R.id.listViewRodLength);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
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
