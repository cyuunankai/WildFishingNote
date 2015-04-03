package com.simple.wildfishingnote.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.simple.wildfishingnote.common.Constant;

import android.os.Environment;

public class LogUtil {

    public static void appendLog(String text) {
        
        if(!Constant.ENABLE_LOG){
            return;
        }
        
        File logFile = new File(Environment.getExternalStorageDirectory() + "/wildFishingNote.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            // BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
