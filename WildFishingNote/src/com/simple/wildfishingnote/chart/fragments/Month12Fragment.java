package com.simple.wildfishingnote.chart.fragments;
import android.support.v4.app.Fragment;


public class Month12Fragment extends SimpleFragment {

    public static Fragment newInstance() {
        return new Month12Fragment();
    }

    private static String CURRENT_MONTH = "12";

    @Override
    public String getMonth() {
        return CURRENT_MONTH;
    }
}
