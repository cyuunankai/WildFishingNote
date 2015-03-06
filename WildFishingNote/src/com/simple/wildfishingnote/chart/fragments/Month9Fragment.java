package com.simple.wildfishingnote.chart.fragments;
import android.support.v4.app.Fragment;


public class Month9Fragment extends SimpleFragment {

    public static Fragment newInstance() {
        return new Month9Fragment();
    }

    private static String CURRENT_MONTH = "09";

    @Override
    public String getMonth() {
        return CURRENT_MONTH;
    }
}
