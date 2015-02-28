package com.simple.wildfishingnote.campaign.point;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.widget.Toast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.animation.hover.HoverSwitch;
import com.simple.wildfishingnote.bean.Bait;
import com.simple.wildfishingnote.bean.LureMethod;
import com.simple.wildfishingnote.bean.Point;
import com.simple.wildfishingnote.bean.RodLength;
import com.simple.wildfishingnote.bean.animation.hover.ImageIntentBean;
import com.simple.wildfishingnote.bean.animation.hover.ImageSrcIntent;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.database.CampaignDataSource;

public class AddPointActivity extends ActionBarActivity implements RodLengthDialogFragment.NoticeDialogListener,
                                                                   LureMethodDialogFragment.NoticeDialogListener,
                                                                   BaitDialogFragment.NoticeDialogListener {

    public static final String POINT_ID = "point_id";
    String globalPointId;
    private CampaignDataSource dataSource;
    
    private final static int IMAGE = R.drawable.ic_launcher;
    private final static int ROOT_IMAGE = R.drawable.ic_launcher;
    HoverSwitch hoverSwitch = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point);
        
        dataSource = new CampaignDataSource(this);
        dataSource.open();
        
        initHoverSwith();
        
        initAddOrEdit();
    }

    /**
     * [保存]按钮按下
     */
    public void onAddPointButtonSaveClickListener(View v) {
        
        if (checkIsFail()) {
            return;
        }
        
        Point Point = buildPoint();
        Point updatedPoint = savePoint(Point);

        Intent intent = getIntent();
        intent.putExtra(POINT_ID, updatedPoint.getId());
        setResult(RESULT_OK, intent);
        finish();
    }
    
    /**
     * [查看竿长]按钮按下
     * @param v
     */
    public void addPointDispAllRodLenghtBtnClick(View v){
        Intent intent = new Intent(this, ShowAllRodLengthActivity.class);
        startActivityForResult(intent, Constant.REQUEST_CODE_SHOW_ALL_ROD_LENGTH);
    }
    
    /**
     * 监听所有onActivityResult事件
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        if (requestCode == Constant.REQUEST_CODE_SHOW_ALL_ROD_LENGTH && resultCode == Activity.RESULT_OK) {
            // 查看竿长返回
            initRodLengthSpinner();
        } else if (requestCode == Constant.REQUEST_CODE_SHOW_ALL_LURE_METHOD && resultCode == Activity.RESULT_OK) {
            // 查看打窝方法返回
            initLureMethodSpinner();
        } else if (requestCode == Constant.REQUEST_CODE_SHOW_ALL_BAIT && resultCode == Activity.RESULT_OK) {
            // 查看饵料返回
            initBaitSpinner();
        }
    }
    
    /**
     * 【添加竿长】画面[OK]按钮按下
     */
    @Override
    public void onRLDialogPositiveClick(DialogFragment dialog, String rodLength) {
        // User touched the dialog's positive button
        RodLength rl = new RodLength();
        rl.setName(rodLength);
        RodLength newRl = dataSource.addRodLength(rl);
        
        initRodLengthSpinner();
        setRodLengthSpinnerSelection(newRl.getId());
        
        hoverSwitch.execRootImageClick(true);
    }
    
    /**
     *  【添加打窝】画面[OK]按钮按下
     */
    @Override
    public void onLMDialogPositiveClick(DialogFragment dialog, String name, String detail) {
        // User touched the dialog's positive button
        LureMethod lm = new LureMethod();
        lm.setName(name);
        lm.setDetail(detail);
        LureMethod newLm = dataSource.addLureMethod(lm);
        
        initLureMethodSpinner();
        setLureMethodSpinnerSelection(newLm.getId());
        
        hoverSwitch.execRootImageClick(true);
    }
    
    /**
     *  【添加饵料】画面[OK]按钮按下
     */
    @Override
    public void onBaitDialogPositiveClick(DialogFragment dialog, String name, String detail) {
        // User touched the dialog's positive button
        Bait b = new Bait();
        b.setName(name);
        b.setDetail(detail);
        Bait newBait = dataSource.addBait(b);
        
        initBaitSpinner();
        setBaitSpinnerSelection(newBait.getId());
        
        hoverSwitch.execRootImageClick(true);
    }
    
    /**
     * 初始化新规或编辑状态
     */
    private void initAddOrEdit() {
        initRodLengthSpinner();
        initLureMethodSpinner();
        initBaitSpinner();
        
        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.getExtras().containsKey(POINT_ID)) {
            globalPointId = intent.getStringExtra(POINT_ID);
            
            initEdit();
        } else {
            globalPointId = "";
        }
    }

    /**
     * 初始化编辑状态
     */
    private void initEdit() {
        Point point = dataSource.getPointById(globalPointId);
        BootstrapEditText editTextDepth = (BootstrapEditText)findViewById(R.id.addPointDepthEditText);
        editTextDepth.setText(point.getDepth());
        setRodLengthSpinnerSelection(point.getRodLengthId());
        setLureMethodSpinnerSelection(point.getLureMethodId());
        setBaitSpinnerSelection(point.getBaitId());
    }
    
    private void initRodLengthSpinner() {
        dataSource.open();
        List<RodLength> list = dataSource.getAllRodLengths();
        
        RodLength[] arr = convertRodLengthListToArr(list);
        
        Spinner s = (Spinner) findViewById(R.id.addPointRodLengthSpinner);
        ArrayAdapter<RodLength> spinnerArrayAdapter = new ArrayAdapter<RodLength>(this,
                android.R.layout.simple_spinner_item, arr);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);  
    }

    private RodLength[] convertRodLengthListToArr(List<RodLength> list) {
        RodLength[] arr = new RodLength[list.size()];
        int index = 0;
        for(RodLength obj : list){
            arr[index] = obj;
            index++;
        }
        return arr;
    }
    
    private void setRodLengthSpinnerSelection(String selectedId) {
        List<RodLength> list = dataSource.getAllRodLengths();
        
        int selectedIndex = getRodLengthSelectedIndex(selectedId, list);
        
        Spinner s = (Spinner) findViewById(R.id.addPointRodLengthSpinner);
        s.setSelection(selectedIndex);
    }

    private int getRodLengthSelectedIndex(String selectedId, List<RodLength> list) {
        int selectedIndex = 0;
        for (RodLength obj : list){
            if(selectedId.equals(obj.getId())) {
                break;
            }
            selectedIndex++;
        }
        return selectedIndex;
    }
    
    private void initLureMethodSpinner() {        
        dataSource.open();
        List<LureMethod> list = dataSource.getAllLureMethods();
        
        LureMethod[] arr = convertLureMethodListToArr(list);
        
        Spinner s = (Spinner) findViewById(R.id.addPointLureMethodSpinner);
        ArrayAdapter<LureMethod> spinnerArrayAdapter = new ArrayAdapter<LureMethod>(this,
                android.R.layout.simple_spinner_item, arr);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);  

    }
    
    private LureMethod[] convertLureMethodListToArr(List<LureMethod> list) {
        LureMethod[] arr = new LureMethod[list.size()];
        int index = 0;
        for(LureMethod obj : list){
            arr[index] = obj;
            index++;
        }
        return arr;
    }
    
    private void setLureMethodSpinnerSelection(String selectedId) {
        List<LureMethod> list = dataSource.getAllLureMethods();
        
        int selectedIndex = getLureMethodSelectedIndex(selectedId, list);
        
        Spinner s = (Spinner) findViewById(R.id.addPointLureMethodSpinner);
        s.setSelection(selectedIndex);
    }
    
    private int getLureMethodSelectedIndex(String selectedId, List<LureMethod> list) {
        int selectedIndex = 0;
        for (LureMethod obj : list){
            if(selectedId.equals(obj.getId())) {
                break;
            }
            selectedIndex++;
        }
        return selectedIndex;
    }

    private void initBaitSpinner() {
        dataSource.open();
        
        List<Bait> list = dataSource.getAllBaits();
        
        Bait[] arr = convertBaitListToArr(list);
        
        Spinner s = (Spinner) findViewById(R.id.addPointBaitSpinner);
        ArrayAdapter<Bait> spinnerArrayAdapter = new ArrayAdapter<Bait>(this,
                android.R.layout.simple_spinner_item, arr);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);  
    }
    
    private Bait[] convertBaitListToArr(List<Bait> list) {
        Bait[] arr = new Bait[list.size()];
        int index = 0;
        for(Bait obj : list){
            arr[index] = obj;
            index++;
        }
        return arr;
    }
    
    private void setBaitSpinnerSelection(String selectedId) {
        List<Bait> list = dataSource.getAllBaits();
        
        int selectedIndex = getBaitSelectedIndex(selectedId, list);
        
        Spinner s = (Spinner) findViewById(R.id.addPointBaitSpinner);
        s.setSelection(selectedIndex);
    }
    
    private int getBaitSelectedIndex(String selectedId, List<Bait> list) {
        int selectedIndex = 0;
        for (Bait obj : list){
            if(selectedId.equals(obj.getId())) {
                break;
            }
            selectedIndex++;
        }
        return selectedIndex;
    }
    
    /**
     * check
     */
     private boolean checkIsFail() {
         boolean ret = false;
         
         Spinner spinnerRodLength = (Spinner) findViewById(R.id.addPointRodLengthSpinner);
         Spinner spinnerLureMethod = (Spinner) findViewById(R.id.addPointLureMethodSpinner);
         Spinner spinnerBait = (Spinner) findViewById(R.id.addPointBaitSpinner);
         BootstrapEditText editTextDepth = (BootstrapEditText) findViewById(R.id.addPointDepthEditText);
         
         String selectedRodLengthId = ((RodLength)spinnerRodLength.getSelectedItem()).getId();
         String selectedLureMethodId = ((LureMethod)spinnerLureMethod.getSelectedItem()).getId();
         String selectedBaitId = ((Bait)spinnerBait.getSelectedItem()).getId();
         
         if ("".equals(selectedRodLengthId)) {
             Toast.makeText(this, "请选择竿长!", Toast.LENGTH_SHORT).show();
             ret = true;
         }
         
         if ("".equals(selectedLureMethodId)) {
             Toast.makeText(this, "请选择打窝!", Toast.LENGTH_SHORT).show();
             ret = true;
         }
         
         if ("".equals(selectedBaitId)) {
             Toast.makeText(this, "请选择饵料!", Toast.LENGTH_SHORT).show();
             ret = true;
         }
         
         if ("".equals(editTextDepth.getText().toString())) {
             Toast.makeText(this, "请输入水深!", Toast.LENGTH_SHORT).show();
             ret = true;
         }

         return ret;
     }
     
     /**
      * 根据画面值构建Point
      * @return
      */
     private Point buildPoint() {
         Spinner spinnerRodLength = (Spinner) findViewById(R.id.addPointRodLengthSpinner);
         Spinner spinnerLureMethod = (Spinner) findViewById(R.id.addPointLureMethodSpinner);
         Spinner spinnerBait = (Spinner) findViewById(R.id.addPointBaitSpinner);
         BootstrapEditText editTextDepth = (BootstrapEditText) findViewById(R.id.addPointDepthEditText);
         
         String selectedRodLengthId = ((RodLength)spinnerRodLength.getSelectedItem()).getId();
         String selectedLureMethodId = ((LureMethod)spinnerLureMethod.getSelectedItem()).getId();
         String selectedBaitId = ((Bait)spinnerBait.getSelectedItem()).getId();
         
         Point p = new Point();
         p.setId(globalPointId);
         p.setRodLengthId(selectedRodLengthId);
         p.setDepth(editTextDepth.getText().toString());
         p.setLureMethodId(selectedLureMethodId);
         p.setBaitId(selectedBaitId);
         
         return p;
     }

     /**
      * 保存Point
      */
     private Point savePoint(Point Point) {
         Point updatedPoint;
         if ("".equals(Point.getId())) {
             updatedPoint = dataSource.addPoint(Point);
         } else {
             updatedPoint = dataSource.updatePoint(Point);
         }
         return updatedPoint;
     }
     
     private void initHoverSwith() {
 		ViewGroup parent = (ViewGroup) findViewById(R.id.rootLayout);
 		List<ImageSrcIntent> itemImageSrcIntentList = getInitialItemImageSrcAndIntentList();
 		hoverSwitch = new HoverSwitch();
 		hoverSwitch.init(parent, ROOT_IMAGE, itemImageSrcIntentList, this, 150);
 	
 		
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
 		
 		// 至下向上
 		ImageSrcIntent imageSrcIntent = new ImageSrcIntent();
 		imageSrcIntent.setItemButtonSrc(IMAGE);
 		imageSrcIntent.setItemTextSrc(IMAGE);
 		imageSrcIntent.setInvokeIntentOnClick("BaitDialogFragment");
 		imageSrcIntentList.add(imageSrcIntent);
 		
 		imageSrcIntent = new ImageSrcIntent();
 		imageSrcIntent.setItemButtonSrc(IMAGE);
 		imageSrcIntent.setItemTextSrc(IMAGE);
 		imageSrcIntent.setInvokeIntentOnClick("LureMethodDialogFragment");
 		imageSrcIntentList.add(imageSrcIntent);
 		
 		imageSrcIntent = new ImageSrcIntent();
 		imageSrcIntent.setItemButtonSrc(IMAGE);
 		imageSrcIntent.setItemTextSrc(IMAGE);
 		imageSrcIntent.setInvokeIntentOnClick("RodLengthDialogFragment");
 		imageSrcIntentList.add(imageSrcIntent);
 		
 		imageSrcIntent = new ImageSrcIntent();
 		imageSrcIntent.setItemButtonSrc(IMAGE);
 		imageSrcIntent.setItemTextSrc(IMAGE);
 		imageSrcIntent.setInvokeIntentOnClick("ShowAllBaitActivity");
 		imageSrcIntentList.add(imageSrcIntent);
 		
 		imageSrcIntent = new ImageSrcIntent();
 		imageSrcIntent.setItemButtonSrc(IMAGE);
 		imageSrcIntent.setItemTextSrc(IMAGE);
 		imageSrcIntent.setInvokeIntentOnClick("ShowAllLureMethodActivity");
 		imageSrcIntentList.add(imageSrcIntent);
 		
 		imageSrcIntent = new ImageSrcIntent();
 		imageSrcIntent.setItemButtonSrc(IMAGE);
 		imageSrcIntent.setItemTextSrc(IMAGE);
 		imageSrcIntent.setInvokeIntentOnClick("ShowAllRodLengthActivity");
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
 					if ("RodLengthDialogFragment".equals(intentName)) {
 						RodLengthDialogFragment dialog = new RodLengthDialogFragment("");
 				        dialog.show(getSupportFragmentManager(), "rodLengthDialog");
 					} else if ("LureMethodDialogFragment".equals(intentName)) {
 						LureMethodDialogFragment dialog = new LureMethodDialogFragment("","");
 				        dialog.show(getSupportFragmentManager(), "lureMethodDialog");
 					} else if ("BaitDialogFragment".equals(intentName)) {
 						BaitDialogFragment dialog = new BaitDialogFragment("", "");
 				        dialog.show(getSupportFragmentManager(), "baitDialog");
 					} else if ("ShowAllRodLengthActivity".equals(intentName)){
 						Intent intent = new Intent(getApplicationContext(), ShowAllRodLengthActivity.class);
 				        startActivityForResult(intent, Constant.REQUEST_CODE_SHOW_ALL_ROD_LENGTH);
 					} else if ("ShowAllLureMethodActivity".equals(intentName)){
 						Intent intent = new Intent(getApplicationContext(), ShowAllLureMethodActivity.class);
 				        startActivityForResult(intent, Constant.REQUEST_CODE_SHOW_ALL_LURE_METHOD);
 					} else if ("ShowAllBaitActivity".equals(intentName)){
 						Intent intent = new Intent(getApplicationContext(), ShowAllBaitActivity.class);
 				        startActivityForResult(intent, Constant.REQUEST_CODE_SHOW_ALL_BAIT);
 					}
 					
 				}
 			});
 		}
 	}

     @Override
     protected void onResume() {
       dataSource.open();
       super.onResume();
       hoverSwitch.execRootImageClick(true);
     }

     @Override
     protected void onPause() {
       dataSource.close();
       super.onPause();
     }

}
