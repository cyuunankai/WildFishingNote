package com.simple.wildfishingnote.campaign.point;

import java.util.List;

import org.holoeverywhere.widget.Toast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Bait;
import com.simple.wildfishingnote.bean.LureMethod;
import com.simple.wildfishingnote.bean.Point;
import com.simple.wildfishingnote.bean.RodLength;
import com.simple.wildfishingnote.database.CampaignDataSource;

public class AddPointActivity extends ActionBarActivity implements RodLengthDialogFragment.NoticeDialogListener,
                                                                   LureMethodDialogFragment.NoticeDialogListener,
                                                                   BaitDialogFragment.NoticeDialogListener {

    public static final String POINT_ID = "point_id";
    String globalPointId;
    private CampaignDataSource dataSource;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point);
        
        dataSource = new CampaignDataSource(this);
        dataSource.open();
        
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
     * [添加竿长]按钮按下
     * @param v
     */
    public void addPointShowAddRodLengthBtnClick(View v){
        RodLengthDialogFragment dialog = new RodLengthDialogFragment();
        dialog.show(getSupportFragmentManager(), "rodLengthDialog");
    }
    
    /**
     * [添加打窝]按钮按下
     * @param v
     */
    public void addPointShowAddLureMethodBtnClick(View v){
        LureMethodDialogFragment dialog = new LureMethodDialogFragment();
        dialog.show(getSupportFragmentManager(), "lureMethodDialog");
    }
    
    /**
     * [添加饵料]按钮按下
     * @param v
     */
    public void addPointShowAddBaitBtnClick(View v){
        BaitDialogFragment dialog = new BaitDialogFragment();
        dialog.show(getSupportFragmentManager(), "baitDialog");
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
    }

    @Override
    public void onRLDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
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
    }

    @Override
    public void onLMDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
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
    }

    @Override
    public void onBaitDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
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
        List<RodLength> list = dataSource.getAllRodLengths();
        
        RodLength[] arr = convertRodLengthListToArr(list);
        
        Spinner s = (Spinner) findViewById(R.id.addPointRodLengthSpinner);
        ArrayAdapter<RodLength> spinnerArrayAdapter = new ArrayAdapter<RodLength>(this,
                android.R.layout.simple_spinner_item, arr);
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
        List<LureMethod> list = dataSource.getAllLureMethods();
        
        LureMethod[] arr = convertLureMethodListToArr(list);
        
        Spinner s = (Spinner) findViewById(R.id.addPointLureMethodSpinner);
        ArrayAdapter<LureMethod> spinnerArrayAdapter = new ArrayAdapter<LureMethod>(this,
                android.R.layout.simple_spinner_item, arr);
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
        List<Bait> list = dataSource.getAllBaits();
        
        Bait[] arr = convertBaitListToArr(list);
        
        Spinner s = (Spinner) findViewById(R.id.addPointBaitSpinner);
        ArrayAdapter<Bait> spinnerArrayAdapter = new ArrayAdapter<Bait>(this,
                android.R.layout.simple_spinner_item, arr);
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

     @Override
     protected void onResume() {
       dataSource.open();
       super.onResume();
     }

     @Override
     protected void onPause() {
       dataSource.close();
       super.onPause();
     }

}
