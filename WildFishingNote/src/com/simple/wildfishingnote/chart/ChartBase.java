
package com.simple.wildfishingnote.chart;

import android.support.v4.app.FragmentActivity;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineDataSet;
import com.simple.wildfishingnote.R;

/**
 * Baseclass of all Activities of the Demo Application.
 * 
 * @author Philipp Jahoda
 */
public abstract class ChartBase extends FragmentActivity {
    
    protected String[] mMonths = new String[] {
            "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"
    };
    
    protected void setLineDataSetProperty(LineDataSet lds, int color) {
        lds.setColor(color);
        lds.setLineWidth(2.5f);
        lds.setCircleColor(color);
        lds.setCircleSize(5f);
        lds.setFillColor(color);
        lds.setDrawCubic(true);
        lds.setDrawValues(true);
        lds.setValueTextSize(14f);
        lds.setValueTextColor(color);

        lds.setAxisDependency(YAxis.AxisDependency.LEFT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }
}
