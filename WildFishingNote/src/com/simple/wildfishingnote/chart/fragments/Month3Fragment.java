package com.simple.wildfishingnote.chart.fragments;
import android.support.v4.app.Fragment;


public class Month3Fragment extends SimpleFragment {

    public static Fragment newInstance() {
        return new Month3Fragment();
    }

    private static String CURRENT_MONTH = "03";

    @Override
    public String getMonth() {
        return CURRENT_MONTH;
    }

}
