package com.simple.wildfishingnote.common;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

import com.simple.wildfishingnote.R;

public class CircleActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_circle);
        
        //设置标题栏中的不明确的进度条是否可以显示，当你需要表示处理中的时候设置为True,处理完毕后设置为false
        setProgressBarIndeterminateVisibility(true);
    }
}
