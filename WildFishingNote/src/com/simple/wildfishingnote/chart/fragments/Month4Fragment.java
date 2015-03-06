package com.simple.wildfishingnote.chart.fragments;
import android.support.v4.app.Fragment;


public class Month4Fragment extends SimpleFragment {

    public static Fragment newInstance() {
        return new Month4Fragment();
    }

    private static String CURRENT_MONTH = "04";

    @Override
    public String getMonth() {
        return CURRENT_MONTH;
    }
}
