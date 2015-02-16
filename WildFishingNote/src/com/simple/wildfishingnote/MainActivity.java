package com.simple.wildfishingnote;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simple.wildfishingnote.animation.hover.HoverSwitch;
import com.simple.wildfishingnote.bean.animation.hover.ImageIntentBean;
import com.simple.wildfishingnote.bean.animation.hover.ImageSrcIntent;
import com.simple.wildfishingnote.common.Common;
import com.simple.wildfishingnote.sectionedlistview.SectionListAdapter;
import com.simple.wildfishingnote.sectionedlistview.SectionListItem;
import com.simple.wildfishingnote.sectionedlistview.SectionListView;
import com.simple.wildfishingnote.tabs.MainTab1Fragment;

public class MainActivity extends ActionBarActivity {

	private final static int IMAGE = R.drawable.ic_launcher;
    private final static int ROOT_IMAGE = R.drawable.ic_launcher;
    HoverSwitch hoverSwitch = null;
    
//    private int selectedMenuItemId = 0;
    
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initCustomActionBar();
        
        initHoverSwith();
        
        initMainTab1Fm(savedInstanceState);

        
    }


	private void initMainTab1Fm(Bundle savedInstanceState) {
		// Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of ExampleFragment
            MainTab1Fragment firstFragment = new MainTab1Fragment();
            
            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());
            
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
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
		hoverSwitch.init(parent, ROOT_IMAGE, itemImageSrcIntentList, this, 10);
	
		
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
		imageSrcIntent.setInvokeIntentOnClick("CalendarDailogActivity");
		imageSrcIntentList.add(imageSrcIntent);
		
		imageSrcIntent = new ImageSrcIntent();
		imageSrcIntent.setItemButtonSrc(IMAGE);
		imageSrcIntent.setItemTextSrc(IMAGE);
		imageSrcIntent.setInvokeIntentOnClick("AddMainActivity");
		imageSrcIntentList.add(imageSrcIntent);
		
		imageSrcIntent = new ImageSrcIntent();
		imageSrcIntent.setItemButtonSrc(IMAGE);
		imageSrcIntent.setItemTextSrc(IMAGE);
		imageSrcIntent.setInvokeIntentOnClick("AddMainActivity");
		imageSrcIntentList.add(imageSrcIntent);
		
		return imageSrcIntentList;
	}
	
	/**
	 * add item click listener
	 */
	private void addItemClickListener(List<ImageIntentBean> imageIntentList){
		for(ImageIntentBean imageIntent : imageIntentList){
			ImageView imageView = imageIntent.getImageView();
			final String intentName = imageIntent.getInvokeIntentOnClick();
			imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					if ("CalendarDailogActivity".equals(intentName)) {
						Intent intent = new Intent(getApplicationContext(), CalendarDailogActivity.class);
						startActivity(intent);
					} else if ("AddMainActivity".equals(intentName)) {
						Intent intent = new Intent(getApplicationContext(), AddMainActivity.class);
						startActivity(intent);
					} else if ("AddMainActivity".equals(intentName)) {
						Intent intent = new Intent(getApplicationContext(), AddMainActivity.class);
						startActivity(intent);
					}
					
				}
			});
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
//		if (selectedMenuItemId == 0 || selectedMenuItemId == R.id.action_main) {
			hoverSwitch.execRootImageClick(true);
//			hoverSwitch.showRoot();
//		} else {
//			hoverSwitch.hideRootAndItems();
//		}
		
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
//    	selectedMenuItemId = item.getItemId();
		if (Common.optionsItemSelectedHandler(item, this,
				getSupportFragmentManager(), hoverSwitch)) {
			return true;
		}

    	return super.onOptionsItemSelected(item);
    }

}
