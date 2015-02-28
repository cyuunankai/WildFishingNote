package com.simple.wildfishingnote.tabs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.apache.commons.io.FileUtils;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.database.MySQLiteHelper;
import com.simple.wildfishingnote.utils.Msg;

public class MainTab4Fragment extends Fragment implements OnClickListener {
    
    private View tab4View;
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
	    tab4View = inflater.inflate(R.layout.activity_main_tab4, container, false);
	    
	    setBtns();
        
        return tab4View;
    }
	
	@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBackUp:
                backup();
                break;
            case R.id.buttonRestore:
                restore();
                break;
        }
    }
	
    private void restore() {
    	try {
	        restoreDatabase();
	        restorePlaceImageFiles();
	        restoreFishResultImageFiles();
        	Msg.show(getActivity(), "恢复成功");
    	} catch (IOException e) {
			Msg.show(getActivity(), "恢复失败");
		}
    }

	private void restoreDatabase() throws IOException {
//		try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "com.simple.wildfishingnote"
                        + "//databases//" + MySQLiteHelper.DATABASE_NAME;
                String backupDBPath = "backup.db"; // From SD directory.
                File backupDB = new File(data, currentDBPath);
                File currentDB = new File(sd, Constant.BACKUP_FOLDER_NAME + "/" + backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
//                Msg.show(getActivity(), "恢复数据库成功");

            }
//        } catch (Exception e) {
//        	Msg.show(getActivity(), "恢复数据库失败");
//        }
	}
    
    private void restorePlaceImageFiles() throws IOException {
//		try {

			String imagePath = getActivity().getFilesDir()
					+ Constant.PLACE_IMAGE_PATH;
			File sd = Environment.getExternalStorageDirectory();

			if (sd.canWrite()) {
				File source = new File(sd, Constant.BACKUP_FOLDER_NAME
						+ Constant.PLACE_IMAGE_PATH);
				File dest = new File(imagePath);
				
				FileUtils.deleteDirectory(dest);
				FileUtils.copyDirectory(source, dest);
//				Msg.show(getActivity(), "恢复钓位图片成功");
			}
//
//		} catch (IOException e) {
//			Msg.show(getActivity(), "恢复钓位图片失败");
//		}
	}
	
	private void restoreFishResultImageFiles() throws IOException {
//		try {

			String imagePath = getActivity().getFilesDir()
					+ Constant.FISH_RESULT_IMAGE_PATH;
			File sd = Environment.getExternalStorageDirectory();

			if (sd.canWrite()) {
				File source = new File(sd, Constant.BACKUP_FOLDER_NAME
						+ Constant.FISH_RESULT_IMAGE_PATH);
				File dest = new File(imagePath);
				
				FileUtils.deleteDirectory(dest);
				FileUtils.copyDirectory(source, dest);
//				Msg.show(getActivity(), "恢复渔获图片成功");
			}
//
//		} catch (IOException e) {
//			Msg.show(getActivity(), "恢复渔获图片失败");
//		}
	}

	private void backup() {
		try {
			backupDatabase();
			backupPlaceImageFiles();
			backupFishResultImageFiles();
			
			Msg.show(getActivity(), "备份成功");
		} catch (IOException e) {
			Msg.show(getActivity(), "备份成功失败");
		}
	}

	private void backupDatabase() throws IOException {
//		try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "com.simple.wildfishingnote"
                        + "//databases//" + MySQLiteHelper.DATABASE_NAME;
                String backupDBPath = "backup.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, Constant.BACKUP_FOLDER_NAME + "/" + backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
//                Msg.show(getActivity(), "备份数据库成功");

            }
//        } catch (Exception e) {
//        	Msg.show(getActivity(), "备份数据库失败");
//        }
	}
	
	private void backupPlaceImageFiles() throws IOException {
//		try {

			String imagePath = getActivity().getFilesDir()
					+ Constant.PLACE_IMAGE_PATH;
			File sd = Environment.getExternalStorageDirectory();

			if (sd.canWrite()) {
				File source = new File(imagePath);
				File dest = new File(sd, Constant.BACKUP_FOLDER_NAME
						+ Constant.PLACE_IMAGE_PATH);
				
				FileUtils.deleteDirectory(dest);
				FileUtils.copyDirectory(source, dest);
//				Msg.show(getActivity(), "备份钓位图片成功");
			}

//		} catch (IOException e) {
//			Msg.show(getActivity(), "备份钓位图片失败");
//		}
	}
	
	private void backupFishResultImageFiles() throws IOException {
//		try {

			String imagePath = getActivity().getFilesDir()
					+ Constant.FISH_RESULT_IMAGE_PATH;
			File sd = Environment.getExternalStorageDirectory();

			if (sd.canWrite()) {
				File source = new File(imagePath);
				File dest = new File(sd, Constant.BACKUP_FOLDER_NAME
						+ Constant.FISH_RESULT_IMAGE_PATH);
				
				FileUtils.deleteDirectory(dest);
				FileUtils.copyDirectory(source, dest);
//				Msg.show(getActivity(), "备份渔获图片成功");
			}
//
//		} catch (IOException e) {
//			Msg.show(getActivity(), "备份渔获图片失败");
//		}
	}
	
	private void setBtns() {
        BootstrapButton btn = (BootstrapButton)tab4View.findViewById(R.id.buttonBackUp);
        btn.setOnClickListener(this);
        
        btn = (BootstrapButton)tab4View.findViewById(R.id.buttonRestore);
        btn.setOnClickListener(this);
    }
    
}
