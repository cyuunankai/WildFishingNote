package com.simple.wildfishingnote.chart.fragments;
import android.support.v4.app.Fragment;


public class Month6Fragment extends SimpleFragment {

    public static Fragment newInstance() {
        return new Month6Fragment();
    }

    private static String CURRENT_MONTH = "06";

    @Override
    public String getMonth() {
        return CURRENT_MONTH;
    }
}
