package com.simple.wildfishingnote.chart.fragments;
import android.support.v4.app.Fragment;


public class Month7Fragment extends SimpleFragment {

    public static Fragment newInstance() {
        return new Month7Fragment();
    }

    private static String CURRENT_MONTH = "07";

    @Override
    public String getMonth() {
        return CURRENT_MONTH;
    }
}
