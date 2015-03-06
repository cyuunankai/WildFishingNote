package com.simple.wildfishingnote.chart.fragments;
import android.support.v4.app.Fragment;


public class Month11Fragment extends SimpleFragment {

    public static Fragment newInstance() {
        return new Month11Fragment();
    }

    private static String CURRENT_MONTH = "11";

    @Override
    public String getMonth() {
        return CURRENT_MONTH;
    }
}
