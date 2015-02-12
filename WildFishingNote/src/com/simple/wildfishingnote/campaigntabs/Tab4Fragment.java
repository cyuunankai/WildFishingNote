package com.simple.wildfishingnote.campaigntabs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.ViewSwitcher;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.luminous.pick.Action;
import com.luminous.pick.CustomGallery;
import com.luminous.pick.GalleryAdapter;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.simple.wildfishingnote.AddMainActivity;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.FishType;
import com.simple.wildfishingnote.bean.Point;
import com.simple.wildfishingnote.common.Common;
import com.simple.wildfishingnote.database.CampaignDataSource;

public class Tab4Fragment extends Fragment {
    
    GridView gridGallery;
    Handler handler;
    GalleryAdapter adapter;

    ImageView imgSinglePick;
    Button btnGalleryPick;
    BootstrapButton btnGalleryPickMul;

    String action;
    ViewSwitcher viewSwitcher;
    ImageLoader imageLoader;
    
    private CampaignDataSource dataSource;
    private View tab4View;
    private AddMainActivity addMainActivity;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tab4View = inflater.inflate(R.layout.activity_tab4, container, false);
        addMainActivity = (AddMainActivity)getActivity();
        dataSource = addMainActivity.getCampaignDataSource();
        
        initPointSpinner();
        initFishTypeSpinner();
        initRadioGroup();
        
        // 图片
        initImageLoader();
        initMultiPickBtn();
        
        return tab4View;
    }

    private void initRadioGroup() {
        RadioGroup rg = (RadioGroup)tab4View.findViewById(R.id.radioGroup);
        rg.check(R.id.addResultInRadio);
    }
    
    public void initPointSpinner() {
        dataSource.open();
        Set<String> pointIds = Common.getCampaignPrefernceSet(getActivity(), "campaign_point_ids");
        List<Point> list = dataSource.getPointsById(new ArrayList<String>(pointIds));
        
        Point[] arr = list.toArray(new Point[list.size()]);
        
        Spinner s = (Spinner) tab4View.findViewById(R.id.addResultPointSpinner);
        ArrayAdapter<Point> spinnerArrayAdapter = new ArrayAdapter<Point>(getActivity(),
                android.R.layout.simple_spinner_item, arr);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);  
    }
    
    private void initFishTypeSpinner() {
        dataSource.open();
        List<FishType> list = dataSource.getAllFileTypes();
        
        FishType[] arr = list.toArray(new FishType[list.size()]);
        
        Spinner s = (Spinner) tab4View.findViewById(R.id.addResultFishTypeSpinner);
        ArrayAdapter<FishType> spinnerArrayAdapter = new ArrayAdapter<FishType>(getActivity(),
                android.R.layout.simple_spinner_item, arr);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);  
    }

    
    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                getActivity()).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    private void initMultiPickBtn() {

        handler = new Handler();
        gridGallery = (GridView) tab4View.findViewById(R.id.addResultGridGallery);
        gridGallery.setFastScrollEnabled(true);
        adapter = new GalleryAdapter(getActivity(), imageLoader);
        adapter.setMultiplePick(false);
        gridGallery.setAdapter(adapter);

        viewSwitcher = (ViewSwitcher) tab4View.findViewById(R.id.addResultViewSwitcher);
        viewSwitcher.setDisplayedChild(0);

        btnGalleryPickMul = (BootstrapButton) tab4View.findViewById(R.id.addResultGalleryPickBtn);
        btnGalleryPickMul.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            String[] all_path = data.getStringArrayExtra("all_path");

            ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

            for (String string : all_path) {
                CustomGallery item = new CustomGallery();
                item.sdcardPath = string;

                dataT.add(item);
            }

            viewSwitcher.setDisplayedChild(0);
            adapter.addAll(dataT);
        }
    }

}
