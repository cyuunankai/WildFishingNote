package com.simple.wildfishingnote.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FileUtil {

    /**
     * 将图片保存到指定路径,并返回图片路径
     * @param fromImagePath
     * @param toDirectory
     * @return
     */
    public static String saveImageToInternalStorage(String fromImagePath, String toDirectory){
        Bitmap outputImage = BitmapFactory.decodeFile(fromImagePath);
        
        String fileName = Long.toString(System.currentTimeMillis()) + ".jpg";
        
        File storagePath = new File(toDirectory); 
        storagePath.mkdirs(); 

        File myImage = new File(storagePath, fileName);

        try { 
            FileOutputStream out = new FileOutputStream(myImage); 
            outputImage.compress(Bitmap.CompressFormat.JPEG, 60, out); 
            out.flush();    
            out.close();
        } catch (Exception e) { 
            fileName = null;
        }
        
        return fileName;
    }
    
	public static void deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}
	}
}
