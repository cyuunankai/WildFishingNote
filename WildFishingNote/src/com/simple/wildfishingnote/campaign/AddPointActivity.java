package com.simple.wildfishingnote.campaign;

import org.holoeverywhere.widget.Toast;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Spinner;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Bait;
import com.simple.wildfishingnote.bean.LureMethod;
import com.simple.wildfishingnote.bean.Point;
import com.simple.wildfishingnote.bean.RodLength;
import com.simple.wildfishingnote.common.Common;
import com.simple.wildfishingnote.database.CampaignDataSource;
import com.simple.wildfishingnote.database.WildFishingContract;

public class AddPointActivity extends ActionBarActivity implements RodLengthDialogFragment.NoticeDialogListener,
                                                                   LureMethodDialogFragment.NoticeDialogListener,
                                                                   BaitDialogFragment.NoticeDialogListener {

    public static final String POINT_ID = "point_id";
    String globalPointId;
    String placeId;
    private CampaignDataSource dataSource;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point);
        
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
        intent.putExtra("PointId", updatedPoint.getId());
        setResult(RESULT_OK, intent);
        finish();
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
        
        if ("".equals(placeId)) {
            Toast.makeText(this, "请先选择钓位!", Toast.LENGTH_SHORT).show();
            ret = true;
        }
        
        if (spinnerRodLength.getSelectedItemId() == 0) {
            Toast.makeText(this, "请选择竿长!", Toast.LENGTH_SHORT).show();
            ret = true;
        }
        
        if (spinnerLureMethod.getSelectedItemId() == 0) {
            Toast.makeText(this, "请选择打窝!", Toast.LENGTH_SHORT).show();
            ret = true;
        }
        
        if (spinnerBait.getSelectedItemId() == 0) {
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
        
        Point p = new Point();
        p.setId(globalPointId);
        p.setPlaceId(placeId);
        p.setRodLengthId(String.valueOf(spinnerRodLength.getSelectedItemId()));
        p.setDepth(editTextDepth.getText().toString());
        p.setLureMethodId(String.valueOf(spinnerLureMethod.getSelectedItemId()));
        p.setBaitId(String.valueOf(spinnerBait.getSelectedItemId()));
        
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
    
    public void addPointShowAddRodLengthBtnClick(View v){
        RodLengthDialogFragment dialog = new RodLengthDialogFragment();
        dialog.show(getSupportFragmentManager(), "rodLengthDialog");
    }
    
    public void addPointShowAddLureMethodBtnClick(View v){
        LureMethodDialogFragment dialog = new LureMethodDialogFragment();
        dialog.show(getSupportFragmentManager(), "lureMethodDialog");
    }
    
    public void addPointShowAddBaitBtnClick(View v){
        BaitDialogFragment dialog = new BaitDialogFragment();
        dialog.show(getSupportFragmentManager(), "baitDialog");
    }
    
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
        Cursor c = dataSource.getRodLengthForPinner();
        Spinner s = (Spinner) findViewById(R.id.addPointRodLengthSpinner);
        String[] from = new String[] { WildFishingContract.RodLengths.COLUMN_NAME_NAME };
        Common.initSpinner(this, s, c, from);
        c.close();
    }
    
    private void setRodLengthSpinnerSelection(String selectedId) {
        Cursor c = dataSource.getRodLengthForPinner();
        Spinner s = (Spinner) findViewById(R.id.addPointRodLengthSpinner);
        
        // 设定选中项目
        String colNameId = WildFishingContract.RodLengths._ID;
        Common.setSpinnerSelection(selectedId, s, c, colNameId);
        c.close();
    }
    
    private void initLureMethodSpinner() {
        Cursor c = dataSource.getLureMethodForPinner();
        Spinner s = (Spinner) findViewById(R.id.addPointLureMethodSpinner);
        String[] from = new String[] { WildFishingContract.LureMethods.COLUMN_NAME_NAME };
        Common.initSpinner(this, s, c, from);
        c.close();
    }
    
    private void setLureMethodSpinnerSelection(String selectedId) {
        Cursor c = dataSource.getLureMethodForPinner();
        Spinner s = (Spinner) findViewById(R.id.addPointLureMethodSpinner);
        
        // 设定选中项目
        String colNameId = WildFishingContract.LureMethods._ID;
        Common.setSpinnerSelection(selectedId, s, c, colNameId);
        c.close();
    }

    private void initBaitSpinner() {
        Cursor c = dataSource.getBaitForPinner();
        Spinner s = (Spinner) findViewById(R.id.addPointBaitSpinner);
        String[] from = new String[] { WildFishingContract.Baits.COLUMN_NAME_NAME };
        Common.initSpinner(this, s, c, from);
        c.close();
    }
    
    private void setBaitSpinnerSelection(String selectedId) {
        Cursor c = dataSource.getBaitForPinner();
        Spinner s = (Spinner) findViewById(R.id.addPointBaitSpinner);
        
        // 设定选中项目
        String colNameId = WildFishingContract.Baits._ID;
        Common.setSpinnerSelection(selectedId, s, c, colNameId);
        c.close();
    }


}
