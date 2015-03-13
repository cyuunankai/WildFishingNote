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
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.simple.wildfishingnote.MainActivity;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Bait;
import com.simple.wildfishingnote.bean.FishType;
import com.simple.wildfishingnote.bean.LureMethod;
import com.simple.wildfishingnote.bean.RodLength;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.common.DirectoryChooserDialog;
import com.simple.wildfishingnote.database.BackupDataSource;
import com.simple.wildfishingnote.database.CampaignDataSource;
import com.simple.wildfishingnote.database.MySQLiteHelper;
import com.simple.wildfishingnote.utils.Msg;

public class MainTab4Fragment extends Fragment implements OnClickListener {
    
    private View tab4View;
    private CampaignDataSource dataSource;
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
	    tab4View = inflater.inflate(R.layout.activity_main_tab4, container, false);
	    dataSource = ((MainActivity)getActivity()).getCampaignDataSource();
        dataSource.open();
        
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
            case R.id.buttonImport:
            	importData();
                break;
            case R.id.buttonCreateBackupTable:
                createTable();
                break;
        }
    }
	
    private void createTable() {
        BackupDataSource bds = new BackupDataSource(getActivity());
        bds.open();
        bds.createTable();
        bds.close();
    }
	
	private void importData(){
		RodLength rl = new RodLength();
		rl.setName("5.4");
		dataSource.addRodLength(rl);
		
		rl = new RodLength();
		rl.setName("6.3");
		dataSource.addRodLength(rl);
		
		rl = new RodLength();
		rl.setName("7.2");
		dataSource.addRodLength(rl);
		
		LureMethod lm = new LureMethod();
		lm.setName("老鬼诱鱼香精+酒泡苞米茬子");
		dataSource.addLureMethod(lm);
		
		lm = new LureMethod();
		lm.setName("牛B颗粒(红虫蚯蚓)");
		dataSource.addLureMethod(lm);
		
		lm = new LureMethod();
		lm.setName("牛B颗粒(红虫蚯蚓)+蒸玉米面+牛B鲤+曲酒泡玉米(20粒)");
		dataSource.addLureMethod(lm);
		
		Bait bait = new Bait();
		bait.setName("火鲤+老鬼2#鲤+101薯香膏");
		dataSource.addBait(bait);
		
		bait = new Bait();
		bait.setName("老鬼野钓九一八");
		dataSource.addBait(bait);
		
		bait = new Bait();
		bait.setName("下钩(牛B鲤+曲酒泡玉米) 上钩(火鲤+老鬼2#鲤+101薯香膏)");
		dataSource.addBait(bait);
		
		FishType fishType = new FishType();
		fishType.setName("草鱼");
		dataSource.addFishType(fishType);
		
		fishType = new FishType();
		fishType.setName("青鱼");
		dataSource.addFishType(fishType);
		
		fishType = new FishType();
		fishType.setName("鲢鱼");
		dataSource.addFishType(fishType);
		
		fishType = new FishType();
		fishType.setName("白条");
		dataSource.addFishType(fishType);
		
		fishType = new FishType();
		fishType.setName("葫芦片子");
		dataSource.addFishType(fishType);
		
		fishType = new FishType();
		fishType.setName("船钉子");
		dataSource.addFishType(fishType);
		
		fishType = new FishType();
		fishType.setName("狗鱼");
		dataSource.addFishType(fishType);
		
		fishType = new FishType();
		fishType.setName("老头鱼");
		dataSource.addFishType(fishType);
		
		fishType = new FishType();
		fishType.setName("黑鱼");
		dataSource.addFishType(fishType);
		
		fishType = new FishType();
		fishType.setName("其它");
		dataSource.addFishType(fishType);
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
        }

    }
	
    private void backupPlaceImageFiles() throws IOException {

        String imagePath = getActivity().getFilesDir()
                + Constant.PLACE_IMAGE_PATH;
        File sd = Environment.getExternalStorageDirectory();

        if (sd.canWrite()) {
            File source = new File(imagePath);
            File dest = new File(sd, Constant.BACKUP_FOLDER_NAME
                    + Constant.PLACE_IMAGE_PATH);

            FileUtils.deleteDirectory(dest);
            FileUtils.copyDirectory(source, dest);
        }

    }
	
    private void backupFishResultImageFiles() throws IOException {
        String imagePath = getActivity().getFilesDir()
                + Constant.FISH_RESULT_IMAGE_PATH;
        File sd = Environment.getExternalStorageDirectory();

        if (sd.canWrite()) {
            File source = new File(imagePath);
            File dest = new File(sd, Constant.BACKUP_FOLDER_NAME
                    + Constant.FISH_RESULT_IMAGE_PATH);

            FileUtils.deleteDirectory(dest);
            FileUtils.copyDirectory(source, dest);
        }
    }
	
	private void setBtns() {
	    BootstrapButton btn = (BootstrapButton)tab4View.findViewById(R.id.buttonCreateBackupTable);
        btn.setOnClickListener(this);
	    
        btn = (BootstrapButton)tab4View.findViewById(R.id.buttonBackUp);
        btn.setOnClickListener(this);
        
        btn = (BootstrapButton)tab4View.findViewById(R.id.buttonRestore);
        btn.setOnClickListener(this);
        
        btn = (BootstrapButton)tab4View.findViewById(R.id.buttonImport);
        btn.setOnClickListener(this);
        
        BootstrapButton dirChooserButton = (BootstrapButton)tab4View.findViewById(R.id.buttonDirectoryChooser);
        
        dirChooserButton.setOnClickListener(new OnClickListener() 
        {
            private String m_chosenDir = "";
            private boolean m_newFolderEnabled = true;

            @Override
            public void onClick(View v) 
            {
                // Create DirectoryChooserDialog and register a callback 
                DirectoryChooserDialog directoryChooserDialog = 
                new DirectoryChooserDialog(getActivity(), 
                    new DirectoryChooserDialog.ChosenDirectoryListener() 
                {
                    @Override
                    public void onChosenDir(String chosenDir) 
                    {
                        m_chosenDir = chosenDir;
                        TextView tv = (TextView) tab4View.findViewById(R.id.tvBackupPath);
                        tv.setText(chosenDir);
                    }
                }); 
                // Toggle new folder button enabling
                directoryChooserDialog.setNewFolderEnabled(m_newFolderEnabled);
                // Load directory chooser dialog for initial 'm_chosenDir' directory.
                // The registered callback will be called upon final directory selection.
                directoryChooserDialog.chooseDirectory(m_chosenDir);
                m_newFolderEnabled = ! m_newFolderEnabled;
            }
        });
    }
    
}
