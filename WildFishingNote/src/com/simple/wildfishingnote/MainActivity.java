package com.simple.wildfishingnote;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.simple.wildfishingnote.animation.hover.HoverSwitch;
import com.simple.wildfishingnote.bean.animation.hover.ImageIntentBean;
import com.simple.wildfishingnote.bean.animation.hover.ImageSrcIntent;
import com.simple.wildfishingnote.common.Common;

public class MainActivity extends ActionBarActivity {

	private final static int IMAGE = R.drawable.ic_launcher;
    private final static int ROOT_IMAGE = R.drawable.ic_launcher;
    HoverSwitch hoverSwitch = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initCustomActionBar();
        
        initHoverSwith();
    }

	private void initCustomActionBar() {
		final ActionBar bar = getActionBar();
        
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM);
        
        ViewGroup view = (ViewGroup)LayoutInflater.from(this)
                .inflate(R.layout.activity_top_action_bar, null);
        
        bar.setCustomView(view,
                    new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                            ActionBar.LayoutParams.WRAP_CONTENT,
                            Gravity.CENTER_VERTICAL | Gravity.RIGHT));
	}

	private void initHoverSwith() {
		ViewGroup parent = (ViewGroup) findViewById(R.id.rootLayout);
		List<ImageSrcIntent> itemImageSrcIntentList = getInitialItemImageSrcAndIntentList();
		hoverSwitch = new HoverSwitch();
		hoverSwitch.init(parent, ROOT_IMAGE, itemImageSrcIntentList, this);
	
		
		List<ImageIntentBean> itemBtnImageIntentList = hoverSwitch.getItemBtnImageList();
		addItemClickListener(itemBtnImageIntentList);
		List<ImageIntentBean> itemTextImageIntentList = hoverSwitch.getItemTextImageList();
		addItemClickListener(itemTextImageIntentList);
	}
    
    /**
	 * get initial item image src and intent
	 * @return
	 */
	private List<ImageSrcIntent> getInitialItemImageSrcAndIntentList(){
		List<ImageSrcIntent> imageSrcIntentList = new ArrayList<ImageSrcIntent>();
		
		ImageSrcIntent imageSrcIntent = new ImageSrcIntent();
		imageSrcIntent.setItemButtonSrc(IMAGE);
		imageSrcIntent.setItemTextSrc(IMAGE);
		imageSrcIntent.setIntentClass(AddMainActivity.class);
		imageSrcIntentList.add(imageSrcIntent);
		
		imageSrcIntent = new ImageSrcIntent();
		imageSrcIntent.setItemButtonSrc(IMAGE);
		imageSrcIntent.setItemTextSrc(IMAGE);
		imageSrcIntent.setIntentClass(HistoryActivity.class);
		imageSrcIntentList.add(imageSrcIntent);
		
		imageSrcIntent = new ImageSrcIntent();
		imageSrcIntent.setItemButtonSrc(IMAGE);
		imageSrcIntent.setItemTextSrc(IMAGE);
		imageSrcIntent.setIntentClass(HistoryActivity.class);
		imageSrcIntentList.add(imageSrcIntent);
		
		return imageSrcIntentList;
	}
	
	/**
	 * add item click listener
	 */
	private void addItemClickListener(List<ImageIntentBean> imageIntentList){
		for(ImageIntentBean imageIntent : imageIntentList){
			ImageView imageView = imageIntent.getImageView();
			final Class intentClass = imageIntent.getIntentClass();
			imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(getApplicationContext(), intentClass);
					startActivity(intent);
				}
			});
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		hoverSwitch.execRootImageClick(true);
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    	if (Common.optionsItemSelectedHandler(item, this)) {
    		return true;
    	}
    	
    	return super.onOptionsItemSelected(item);
    }

	
}
