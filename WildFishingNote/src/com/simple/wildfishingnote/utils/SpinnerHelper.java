package com.simple.wildfishingnote.utils;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.simple.wildfishingnote.bean.Area;

public class SpinnerHelper {

    public static <T>int getSelectedIndex(String selectedId, List<T> list) {
        int selectedIndex = 0;
        for (T obj : list) {

            String objId = getObjectId(obj);

            if (selectedId.equals(objId)) {
                break;
            }
            selectedIndex++;
        }
        return selectedIndex;
    }
    
    public static <T> Spinner initSpinner(int position, T[] arr, View view, Activity activity, int id) {
        Spinner s = (Spinner)view.findViewById(id);
        ArrayAdapter<T> spinnerArrayAdapter = new ArrayAdapter<T>(activity,
                android.R.layout.simple_spinner_item, arr);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);
        // 防止页面加载后调用onItemSelected方法
        s.setSelection(position, false);
        return s;
    }

    private static <T>String getObjectId(T obj) {
        String objId = "";
        if (obj instanceof Area) {
            objId = ((Area)obj).getId();
        }
        return objId;
    }
}
