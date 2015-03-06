package com.simple.wildfishingnote.chart.fragments;
import android.support.v4.app.Fragment;


public class Month8Fragment extends SimpleFragment {

    public static Fragment newInstance() {
        return new Month8Fragment();
    }

    private static String CURRENT_MONTH = "08";

    @Override
    public String getMonth() {
        return CURRENT_MONTH;
    }
}
