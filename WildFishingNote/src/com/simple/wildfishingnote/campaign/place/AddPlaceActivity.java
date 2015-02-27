package com.simple.wildfishingnote.campaign.place;

import org.holoeverywhere.widget.Toast;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.luminous.pick.Action;
import com.luminous.pick.GalleryAdapter;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Place;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.database.CampaignDataSource;
import com.simple.wildfishingnote.utils.FileUtil;

public class AddPlaceActivity extends ActionBarActivity {

    GridView gridGallery;
    Handler handler;
    GalleryAdapter adapter;

    ImageView imgSinglePick;
    BootstrapButton btnGalleryPick;

    String action;
    ViewSwitcher viewSwitcher;
    ImageLoader imageLoader;
    String single_path;
    
    public final static String PLACE_ID = "placeId"; 
    private CampaignDataSource dataSourceCampaign;
    String globalPlaceId;   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        
        dataSourceCampaign = new CampaignDataSource(this);
        dataSourceCampaign.open();
        
        initImageLoader();
        initSinglePickBtn();

        initAddOrEdit();
    }

    /**
     * 初始化新规或编辑状态
     */
    private void initAddOrEdit() {
        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.getExtras().containsKey(PLACE_ID)) {
            globalPlaceId = intent.getStringExtra(PLACE_ID);
            
            initEdit();
        } else {
            globalPlaceId = "";
        }
    }

    /**
     * 初始化编辑状态
     */
    private void initEdit() {
        Place place = dataSourceCampaign.getPlaceById(globalPlaceId);
        BootstrapEditText etTitle = (BootstrapEditText) findViewById(R.id.addPlaceTitle);
        BootstrapEditText etDetail = (BootstrapEditText) findViewById(R.id.addPlaceDetail);
        etTitle.setText(place.getTitle());
        etDetail.setText(place.getDetail());
        
        String directory = getApplicationContext().getFilesDir() + Constant.PLACE_IMAGE_PATH;
        single_path = directory + place.getFileName();
        imageLoader.displayImage("file://" + single_path, imgSinglePick);
    }
    
    /**
     * [保存]按钮按下
     */
    public void onAddPlaceButtonSaveClickListener(View v) {
        
//        if (checkIsFail()) {
//            return;
//        }
        
        Place place = buildPlace();
        Place updatedPlace = savePlace(place);

        Intent intent = getIntent();
        intent.putExtra("placeId", updatedPlace.getId());
        setResult(RESULT_OK, intent);
        finish();
    }
    
   /**
    * check
    */
    private boolean checkIsFail() {
        boolean ret = false;
        if ("".equals(single_path)) {
            Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
            ret = true;
        }

        return ret;
    }
    
    /**
     * 根据画面值构建place
     * @return
     */
    private Place buildPlace() {
        EditText etTitle = (EditText) findViewById(R.id.addPlaceTitle);
        EditText etDetail = (EditText) findViewById(R.id.addPlaceDetail);
        String title = etTitle.getText().toString();
        String detail = etDetail.getText().toString();
        
        String fileName = dealImageFile();
        
        Place place = new Place();
        place.setId(globalPlaceId);
        place.setTitle(title);
        place.setDetail(detail);
        place.setFileName(fileName);
        
        return place;
    }

    /**
     * 处理图片
     * @return
     */
    private String dealImageFile() {
        String fileName;
        
        String from = single_path;
        String directory = getApplicationContext().getFilesDir() + Constant.PLACE_IMAGE_PATH;
        if ("".equals(globalPlaceId)) {
            // 新规
            fileName = FileUtil.saveImageToInternalStorage(from, directory);
        } else {
            // 更新
            Place place = dataSourceCampaign.getPlaceById(globalPlaceId);
            String existFile = directory + place.getFileName();
            if (existFile.equals(single_path)) {
                // 图片没有变更
                fileName = place.getFileName();
            } else {
                // 图片变更
                FileUtil.deleteFile(existFile);
                fileName = FileUtil.saveImageToInternalStorage(from, directory);
            }
        }
        
        return fileName;
    }

    /**
     * 保存place
     */
    private Place savePlace(Place place) {
        Place updatedPlace;
        if ("".equals(place.getId())) {
            updatedPlace = dataSourceCampaign.addPlace(place);
        } else {
            updatedPlace = dataSourceCampaign.updatePlace(place);
        }
        return updatedPlace;
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    private void initSinglePickBtn() {

        handler = new Handler();
        gridGallery = (GridView) findViewById(R.id.addPlaceGridGallery);
        gridGallery.setFastScrollEnabled(true);
        adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
        adapter.setMultiplePick(false);
        gridGallery.setAdapter(adapter);

        viewSwitcher = (ViewSwitcher) findViewById(R.id.addPlaceViewSwitcher);
        viewSwitcher.setDisplayedChild(1);

        imgSinglePick = (ImageView) findViewById(R.id.addPlaceSinglePickImg);

        btnGalleryPick = (BootstrapButton) findViewById(R.id.addPlaceGalleryPickBtn);
        btnGalleryPick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                
                single_path = "";
                Intent i = new Intent(Action.ACTION_PICK);
                startActivityForResult(i, 100);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQUEST_CODE_PICK_SINGAL_IMAGE && resultCode == Activity.RESULT_OK) {
            adapter.clear();

            viewSwitcher.setDisplayedChild(1);
            single_path = data.getStringExtra("single_path");
            imageLoader.displayImage("file://" + single_path, imgSinglePick);

        }
    }
    
    @Override
    protected void onResume() {
      dataSourceCampaign.open();
      super.onResume();
    }

    @Override
    protected void onPause() {
      dataSourceCampaign.close();
      super.onPause();
    }

}
