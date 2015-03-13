package com.simple.wildfishingnote.tabs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.simple.wildfishingnote.AddMainActivity;
import com.simple.wildfishingnote.MainActivity;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.CampaignSummary;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.database.CampaignDataSource;
import com.simple.wildfishingnote.flowtextview.FlowTextView;
import com.simple.wildfishingnote.sectionedlistview.SectionListAdapter;
import com.simple.wildfishingnote.sectionedlistview.SectionListItem;
import com.simple.wildfishingnote.sectionedlistview.SectionListView;

public class MainTab1Fragment extends Fragment {

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    
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
        
        initImageLoaderConfig();
        
        initSectionedListView();
        
        
        
        return tab1View;
    }

    private void initImageLoaderConfig() {
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        options = new DisplayImageOptions.Builder()
        .showImageOnLoading(R.drawable.ic_stub)
        .showImageForEmptyUri(R.drawable.ic_empty)
        .showImageOnFail(R.drawable.ic_error)
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .considerExifParams(true)
        .displayer(new RoundedBitmapDisplayer(20))
        .build();
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
//        if (requestCode == Constant.REQUEST_CODE_ADD_CAMPAIGN && resultCode == Activity.RESULT_OK) {
            dataSource.open();
            initSectionedListView();
//        } else if (requestCode == Constant.REQUEST_CODE_EDIT_CAMPAIGN && resultCode == Activity.RESULT_OK) {
//        }
        
    }
	
	public class StandardArrayAdapter extends ArrayAdapter<SectionListItem> {
	    
	    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

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
            public ImageView imageView;
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
                //// 内容长按事件->删除
                addContentLayoutOnLongClickListener(viewHolder, convertView);
            }
            CampaignSummary csObj = (CampaignSummary)list.get(position).item;
            
            // 设置bean值到UI
            SpannableString spanString = new SpannableString(csObj.getTitle());
            spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
            viewHolder.titleTextView.setText(spanString);
            
            viewHolder.dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8.0f);
            viewHolder.dateTextView.setText(csObj.getDate());
            
            String summary = csObj.getSummary();
            Spanned html = Html.fromHtml(summary);
            viewHolder.summaryFlowTextView.setText(html);
            viewHolder.summaryFlowTextView.setTextSize(24.0f);
            
            String directory = getActivity().getApplicationContext().getFilesDir() + Constant.FISH_RESULT_IMAGE_PATH + csObj.getId() + "/";
            final String filePath = directory + csObj.getImagePath();
            if (StringUtils.isNotBlank(csObj.getImagePath())) {
                imageLoader.displayImage("file://" + filePath, viewHolder.imageView, options, animateFirstListener);
            } else {
                viewHolder.imageView.getLayoutParams().width = 0;
                viewHolder.imageView.getLayoutParams().height = 200;
            }
            viewHolder.titleTextView.setTag(list.get(position).item);//// 保存bean值到UI tag (响应事件从这个UI tag取值)

            return convertView;
        }

        /**
         * 监听list item单击事件(text以外部分)
         * @param viewHolder
         * @param contentLayout
         */
        private void addContentLayoutOnClickListener(final ViewHolder viewHolder, View convertView) {
            
            LinearLayout contentLayout = (LinearLayout)convertView.findViewById(R.id.col1Layout);
            contentLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                	doCampaignEdit(viewHolder);
                }

                
            });
        }
        
        /**
         * 监听list item长按事件(text部分以外)
         * @param viewHolder
         * @param contentLayout
         */
        private void addContentLayoutOnLongClickListener(final ViewHolder viewHolder, View convertView) {
            
            LinearLayout contentLayout = (LinearLayout)convertView.findViewById(R.id.col1Layout);
            contentLayout.setOnLongClickListener(deleteClickListener(viewHolder));
        }

        private OnLongClickListener deleteClickListener(final ViewHolder viewHolder) {
            return new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View paramView) {

                    final CampaignSummary element = (CampaignSummary)viewHolder.titleTextView.getTag();

                    PopupMenu popup = new PopupMenu(context, paramView);
                    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            final String campaignId = element.getId();
                            switch (item.getItemId()) {
                                case R.id.delete:

                                    deleteCampaign(campaignId);
                                    return true;
                                default:
                                    return false;
                            }

                        }
                    });
                        

                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.delete_menu, popup.getMenu());
                    popup.show();

                    return true;

                }
            };
        }
        
        private void doCampaignEdit(final ViewHolder viewHolder) {
            CampaignSummary element = (CampaignSummary)viewHolder.titleTextView.getTag();
            Intent intent = new Intent(context, AddMainActivity.class);
            intent.putExtra(AddMainActivity.CAMPAIGN_ID, element.getId());
            startActivityForResult(intent, Constant.REQUEST_CODE_EDIT_CAMPAIGN);
        }
        
        /**
         * 删除campaign
         */
        private void deleteCampaign(final String campaignId) {
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            adb.setTitle("删除?");
            adb.setMessage("确定删除记录");
            adb.setNegativeButton(R.string.cancel, null);
            adb.setPositiveButton(R.string.confirm, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dataSource.deleteCampaign(campaignId);
                    
                    int deleteIndex = 0;
                    for (int i = 0; i < list.size(); i++) {
                        if (campaignId.equals(((CampaignSummary)list.get(i).item).getId())) {
                            deleteIndex = i;
                            break;
                        }
                    }
                    list.remove(deleteIndex);
                    arrayAdapter.notifyDataSetChanged();
                }
            });
            adb.show();
        }
        

        public int getCount() {
            return list.size();
        }

        

    }
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

}
