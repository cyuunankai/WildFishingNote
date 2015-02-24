package com.simple.wildfishingnote.tabs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simple.wildfishingnote.MainActivity;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.CampaignSummary;
import com.simple.wildfishingnote.campaign.place.PlaceDetailActivity;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.database.CampaignDataSource;
import com.simple.wildfishingnote.flowtextview.FlowTextView;
import com.simple.wildfishingnote.sectionedlistview.SectionListAdapter;
import com.simple.wildfishingnote.sectionedlistview.SectionListItem;
import com.simple.wildfishingnote.sectionedlistview.SectionListView;

public class MainTab1Fragment extends Fragment {

    private StandardArrayAdapter arrayAdapter;
    private SectionListAdapter sectionAdapter;
    private SectionListView listView;
    private LayoutInflater mInflater;
    
    private View tab1View;
    private CampaignDataSource dataSource;
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
	    mInflater = inflater;
        tab1View = inflater.inflate(R.layout.activity_main_tab1, container, false);
        dataSource = ((MainActivity)getActivity()).getCampaignDataSource();
        dataSource.open();
        
        initSectionedListView();
        
        return tab1View;
    }

    private void initSectionedListView() {
        List<SectionListItem> list = new ArrayList<SectionListItem>();
        List<CampaignSummary> csList = dataSource.getAllCampaignSummarys();
        for(CampaignSummary cs : csList){
            SectionListItem sli = new SectionListItem(cs, cs.getDate().substring(0, 7));
            list.add(sli);
        }

        arrayAdapter = new StandardArrayAdapter(getActivity(), list);
        sectionAdapter = new SectionListAdapter(mInflater, arrayAdapter);
        listView = (SectionListView)tab1View.findViewById(R.id.section_list_view);
        listView.setAdapter(sectionAdapter);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_CODE_ADD_CAMPAIGN && resultCode == Activity.RESULT_OK) {
            dataSource.open();
            initSectionedListView();
        }
        
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
            SpannableString spanString = new SpannableString(((CampaignSummary)list.get(position).item).getTitle());
            spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
            viewHolder.titleTextView.setText(spanString);
            
            viewHolder.dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8.0f);
            viewHolder.dateTextView.setText(((CampaignSummary)list.get(position).item).getDate());
            
            String summary = ((CampaignSummary)list.get(position).item).getSummary();
            Spanned html = Html.fromHtml(summary);
            viewHolder.summaryFlowTextView.setText(html);
            viewHolder.summaryFlowTextView.setTextSize(24.0f);
            
            
            String filePath = ((CampaignSummary)list.get(position).item).getImagePath();
            if (StringUtils.isNotBlank(filePath)) {
            	Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                viewHolder.imageView.setImageBitmap(bitmap);
                viewHolder.imageView.getLayoutParams().width = 100;
                viewHolder.imageView.getLayoutParams().height = 180;
            } else {
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
                	CampaignSummary element = (CampaignSummary)viewHolder.titleTextView.getTag();
                    Intent intent = new Intent(context, PlaceDetailActivity.class);
                }
            });
        }
        

        public int getCount() {
            return list.size();
        }

        

    }

}
