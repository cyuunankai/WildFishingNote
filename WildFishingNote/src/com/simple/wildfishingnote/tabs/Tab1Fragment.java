package com.simple.wildfishingnote.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.simple.wildfishingnote.R;

public class Tab1Fragment extends Fragment {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_tab1, container, false);
        
        initListView(view);
        
        return view;
    }
	
	private void initListView(View view) {
		ListView lv = (ListView) view.findViewById(R.id.listView1);

        String[] data = { "第1讲", "第2讲", "第3讲", "第4讲", "第5讲", "第6讲", "第7讲", "第8讲","第8讲","第10讲" };

        lv.setAdapter(new ArrayAdapter(view.getContext(),
                android.R.layout.simple_list_item_1, data));

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Toast.makeText(view.getContext(),
                        "您点击的是：" + ((TextView) view).getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        lv.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int position, long id){
                Toast.makeText(view.getContext(),
                        "您长按的是："+((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
	}
	
	

}
