package com.simple.wildfishingnote.chart.fragments;
import android.support.v4.app.Fragment;


public class Month1Fragment extends SimpleFragment {

    public static Fragment newInstance() {
        return new Month1Fragment();
    }

    private static String CURRENT_MONTH = "01";

    @Override
    public String getMonth() {
        return CURRENT_MONTH;
    }
}
