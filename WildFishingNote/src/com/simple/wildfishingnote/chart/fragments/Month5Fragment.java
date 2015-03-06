package com.simple.wildfishingnote.chart.fragments;
import android.support.v4.app.Fragment;


public class Month5Fragment extends SimpleFragment {

    public static Fragment newInstance() {
        return new Month5Fragment();
    }

    private static String CURRENT_MONTH = "05";

    @Override
    public String getMonth() {
        return CURRENT_MONTH;
    }
}
