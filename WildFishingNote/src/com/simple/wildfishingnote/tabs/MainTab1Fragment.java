package com.simple.wildfishingnote.tabs;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Point;
import com.simple.wildfishingnote.campaign.place.PlaceDetailActivity;
import com.simple.wildfishingnote.flowtextview.FlowTextView;
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
        for(int i =1; i< 20; i++){
            String section = "2015-01";
            String date = "2015-01-" + i;
            String showImageFlag = "1";
            if(i > 10){
                section = "2015-02";
                date = "2015-02-" + i;
                showImageFlag = "0";
            }
            Point p = new Point();
            p.setId(String.valueOf(i));
            p.setDepth("title" + i);
            p.setRodLengthName(date);
            p.setLureMethodName("哦那都佛啊个网购耳光阿迪浓缩机嘎达给那都嘎达爱唯欧工二娃狗儿阿济格阿公阿二舅公二进宫而奥迪将公安违规阿拉斯蒂芬大驾光临大家阿斯顿飞骄傲就改为违规如果都是垃圾嘎达给解答搜狗价位给饥饿感");
            p.setRodLengthId(showImageFlag);
            
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
            super(context, R.layout.section_list_view_each_item, list);
            this.context = context;
            this.list = list;
        }

        class ViewHolder {
            
            protected TextView titleTextView;
            protected FlowTextView summaryFlowTextView;
            protected TextView dateTextView;
            protected ImageView imageView;
        }

        /**`
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
                viewHolder.titleTextView = (TextView)convertView.findViewById(R.id.titleTextView);
                viewHolder.summaryFlowTextView = (FlowTextView)convertView.findViewById(R.id.summaryFlowTextView);
                viewHolder.dateTextView = (TextView)convertView.findViewById(R.id.dateTextView);
                viewHolder.imageView = (ImageView)convertView.findViewById(R.id.imageView);
                viewHolder.titleTextView.setTag(list.get(position).item);//// 保存bean值到UI tag (响应事件从这个UI tag取值)
                convertView.setTag(viewHolder);
                
                // 添加事件
                //// 内容单击事件->详细页面
                addContentLayoutOnClickListener(viewHolder, convertView);
            }

            // 设置bean值到UI
            SpannableString spanString = new SpannableString(((Point)list.get(position).item).getDepth());
            spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
            viewHolder.titleTextView.setText(spanString);
            
            viewHolder.dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8.0f);
            viewHolder.dateTextView.setText(((Point)list.get(position).item).getRodLengthName());
            
            String summary = ((Point)list.get(position).item).getLureMethodName();
            Spanned html = Html.fromHtml(summary);
            viewHolder.summaryFlowTextView.setText(html);
            viewHolder.summaryFlowTextView.setTextSize(24.0f);
            
            
            
            if (((Point)list.get(position).item).getRodLengthId().equals("1")) {
//                viewHolder.imageView.setVisibility(View.INVISIBLE);
                viewHolder.imageView.setImageResource(R.drawable.ic_launcher);
                viewHolder.imageView.getLayoutParams().width = 120;
                viewHolder.imageView.getLayoutParams().height = 180;
            } else {
//                viewHolder.imageView.setVisibility(View.VISIBLE);
//                viewHolder.imageView.setImageResource(R.drawable.ic_launcher);
                viewHolder.imageView.getLayoutParams().width = 0;
                viewHolder.imageView.getLayoutParams().height = 180;
            }
            viewHolder.titleTextView.setTag(list.get(position).item);//// 保存bean值到UI tag (响应事件从这个UI tag取值)

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
                    Point element = (Point)viewHolder.titleTextView.getTag();
                    Intent intent = new Intent(context, PlaceDetailActivity.class);
                }
            });
        }
        

        public int getCount() {
            return list.size();
        }

        

    }

}
