package com.simple.wildfishingnote.chart.fragments;
import android.support.v4.app.Fragment;


public class Month2Fragment extends SimpleFragment {

    public static Fragment newInstance() {
        return new Month2Fragment();
    }

    private static String CURRENT_MONTH = "02";

    @Override
    public String getMonth() {
        return CURRENT_MONTH;
    }
    
}
