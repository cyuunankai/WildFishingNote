package com.simple.wildfishingnote.chart.fragments;
import android.support.v4.app.Fragment;


public class Month10Fragment extends SimpleFragment {

    public static Fragment newInstance() {
        return new Month10Fragment();
    }

    private static String CURRENT_MONTH = "10";

    @Override
    public String getMonth() {
        return CURRENT_MONTH;
    }
}
