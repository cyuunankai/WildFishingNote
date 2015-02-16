package com.simple.wildfishingnote.tabs;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Point;
import com.simple.wildfishingnote.campaign.place.PlaceDetailActivity;
import com.simple.wildfishingnote.sectionedlistview.SectionListAdapter;
import com.simple.wildfishingnote.sectionedlistview.SectionListItem;
import com.simple.wildfishingnote.sectionedlistview.SectionListView;

public class MainTab1Fragment extends Fragment {

    private StandardArrayAdapter arrayAdapter;
    private SectionListAdapter sectionAdapter;
    private SectionListView listView;
    
    private View tab1View;
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
	    
        tab1View = inflater.inflate(R.layout.activity_main_tab1, container, false);
        
        List<SectionListItem> list = new ArrayList<SectionListItem>();
        for(int i =0; i< 20; i++){
            String section = "A";
            if(i > 10){
                section = "B";
            }
            Point p = new Point();
            p.setId(String.valueOf(i));
            p.setDepth(" xxxxxxxxx" + i);
            
            SectionListItem sli = new SectionListItem(p, section);
            list.add(sli);
        }
        
        
        arrayAdapter = new StandardArrayAdapter(getActivity(), list);
        sectionAdapter = new SectionListAdapter(inflater, arrayAdapter);
        listView = (SectionListView)tab1View.findViewById(R.id.section_list_view);
        listView.setAdapter(sectionAdapter);
        
        
        
        return tab1View;
    }
	
	public class StandardArrayAdapter extends ArrayAdapter<SectionListItem> {

        private final List<SectionListItem> list;
        private final Activity context;

        public StandardArrayAdapter(Activity context, List<SectionListItem> list) {
            super(context, R.layout.activity_place_listview_each_item, list);
            this.context = context;
            this.list = list;
        }

        class ViewHolder {
            protected TextView example_text_view;
            protected TextView example_text_view1;
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
                convertView = context.getLayoutInflater().inflate(R.layout.section_list_view_each_item, null);
                viewHolder.example_text_view = (TextView)convertView.findViewById(R.id.example_text_view);
                viewHolder.example_text_view1 = (TextView)convertView.findViewById(R.id.example_text_view1);
                viewHolder.example_text_view.setTag(list.get(position).item);//// 保存bean值到UI tag (响应事件从这个UI tag取值)
                convertView.setTag(viewHolder);
                
                // 添加事件
                //// 内容单击事件->详细页面
                addContentLayoutOnClickListener(viewHolder, convertView);
            }

            // 设置bean值到UI
            viewHolder.example_text_view.setText(((Point)list.get(position).item).getId());
            viewHolder.example_text_view1.setText(((Point)list.get(position).item).getDepth());
            viewHolder.example_text_view.setTag(list.get(position).item);//// 保存bean值到UI tag (响应事件从这个UI tag取值)

            return convertView;
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
                    Point element = (Point)viewHolder.example_text_view.getTag();
                    Intent intent = new Intent(context, PlaceDetailActivity.class);
                }
            });
        }
        

        public int getCount() {
            return list.size();
        }

        

    }

}
