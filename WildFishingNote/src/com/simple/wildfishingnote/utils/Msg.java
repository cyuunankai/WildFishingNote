package com.simple.wildfishingnote.utils;

import org.holoeverywhere.widget.Toast;

import android.content.Context;

public class Msg {
    public static void show(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
