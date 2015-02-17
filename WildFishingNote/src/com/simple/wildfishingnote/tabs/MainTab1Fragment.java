package com.simple.wildfishingnote.tabs;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.widget.Toast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Point;
import com.simple.wildfishingnote.campaign.place.PlaceDetailActivity;
import com.simple.wildfishingnote.flowtextview.FlowTextView;
import com.simple.wildfishingnote.sectionedlistview.SectionListAdapter;
import com.simple.wildfishingnote.sectionedlistview.SectionListItem;
import com.simple.wildfishingnote.sectionedlistview.SectionListView;

public class MainTab1Fragment extends Fragment implements OnClickListener {

    private StandardArrayAdapter arrayAdapter;
    private SectionListAdapter sectionAdapter;
    private SectionListView listView;
    
    private View tab1View;
    
    private static final float defaultFontSize = 20.0f;

    private FlowTextView flowTextView;
    private TextView dateTextView;
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
	    
        tab1View = inflater.inflate(R.layout.activity_main_tab1, container, false);
        
        setAddBtn1();
        setAddBtn();
        setDelBtn();
        setShowBtn();
        
        dateTextView = (TextView) tab1View.findViewById(R.id.dateTextView);
        flowTextView = (FlowTextView) tab1View.findViewById(R.id.ftv);
        Spanned html = Html.fromHtml("阿呆家附近啊东风大街噶较高的风景郭德纲风刀霜剑公司东风股份等施工水电费井冈山的风景哥电风扇高富帅递归算法归属感阿呆家附近啊东风大街噶较高的风景郭德纲风刀霜剑公司东风股份等施工水电费井冈山的风景哥电风扇高富帅递归算法归属感");
        flowTextView.setText(html);
        
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
        
        
//        arrayAdapter = new StandardArrayAdapter(getActivity(), list);
//        sectionAdapter = new SectionListAdapter(inflater, arrayAdapter);
//        listView = (SectionListView)tab1View.findViewById(R.id.section_list_view);
//        listView.setAdapter(sectionAdapter);
        
        
        
        return tab1View;
    }
	
	private void setAddBtn1() {
        Button buttonCampaignPlacePre = (Button)tab1View.findViewById(R.id.set);
        buttonCampaignPlacePre.setOnClickListener(this);
    }
	
    private void setAddBtn() {
        Button buttonCampaignPlacePre = (Button)tab1View.findViewById(R.id.addxx);
        buttonCampaignPlacePre.setOnClickListener(this);
    }
    
    private void setDelBtn() {
        Button buttonCampaignPlacePre = (Button)tab1View.findViewById(R.id.delxx);
        buttonCampaignPlacePre.setOnClickListener(this);
    }
    
    private void setShowBtn() {
        Button buttonCampaignPlacePre = (Button)tab1View.findViewById(R.id.showxx);
        buttonCampaignPlacePre.setOnClickListener(this);
    }
	
	@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set:
                set();
                
                break;
            case R.id.addxx:
                increaseFontSize();
                
                break;
            case R.id.delxx:
                decreaseFontSize();
                break;
            case R.id.showxx:
                show();
                break;
        }
    }
	
	private void set(){
	    EditText et = (EditText) tab1View.findViewById(R.id.setText);
        dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.valueOf(et.getText().toString()));
    }
	
	private void increaseFontSize(){
        float currentFontSize = flowTextView.getTextsize();
        flowTextView.setTextSize(currentFontSize + 1.0f);
    }

    private void decreaseFontSize(){
        float currentFontSize = flowTextView.getTextsize();
        flowTextView.setTextSize(currentFontSize - 1.0f);
    }

    private void show(){
        float currentFontSize = flowTextView.getTextsize();
        Toast.makeText(getActivity(), "" + currentFontSize, Toast.LENGTH_SHORT).show();
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
            protected ImageView thumbnail_view;
            protected TextView message_view;
            protected FlowTextView ftv;
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
                viewHolder.ftv = (FlowTextView)convertView.findViewById(R.id.ftv);
//                viewHolder.thumbnail_view = (ImageView)convertView.findViewById(R.id.thumbnail_view);
//                viewHolder.message_view = (TextView)convertView.findViewById(R.id.message_view);
//                viewHolder.message_view.setTag(list.get(position).item);//// 保存bean值到UI tag (响应事件从这个UI tag取值)
                convertView.setTag(viewHolder);
                
                // 添加事件
                //// 内容单击事件->详细页面
                addContentLayoutOnClickListener(viewHolder, convertView);
            }

            // 设置bean值到UI
            
//            viewHolder.message_view.setText(((Point)list.get(position).item).getId());
            
            String text = "阿呆家附近啊东风大街噶较高的风景郭德纲风刀霜剑公司东风股份等施工水电费井冈山的风景哥电风扇高富帅递归算法归属感";

//            Display display = getActivity().getWindowManager().getDefaultDisplay();
//            FlowTextHelper.tryFlowText(text, viewHolder.thumbnail_view, viewHolder.message_view, display);
            
            Spanned html = Html.fromHtml(text);
//            flowTextView.setText(html);
            viewHolder.ftv.setTextSize(25);
            viewHolder.ftv.invalidate();
            viewHolder.ftv.setText(html);
//            viewHolder.example_text_view1.setText(((Point)list.get(position).item).getDepth());
//            viewHolder.message_view.setTag(list.get(position).item);//// 保存bean值到UI tag (响应事件从这个UI tag取值)

            return convertView;
        }

        /**
         * 监听list item单击事件
         * @param viewHolder
         * @param contentLayout
         */
        private void addContentLayoutOnClickListener(final ViewHolder viewHolder, View convertView) {
            
            RelativeLayout contentLayout = (RelativeLayout)convertView.findViewById(R.id.col1Layout);
            contentLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Point element = (Point)viewHolder.message_view.getTag();
                    Intent intent = new Intent(context, PlaceDetailActivity.class);
                }
            });
        }
        

        public int getCount() {
            return list.size();
        }

        

    }

}
