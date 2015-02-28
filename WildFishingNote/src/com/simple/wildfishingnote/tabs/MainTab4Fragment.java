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
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.database.MySQLiteHelper;

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
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "com.simple.wildfishingnote"
                        + "//databases//" + MySQLiteHelper.DATABASE_NAME;
                String backupDBPath = "backup.db"; // From SD directory.
                File backupDB = new File(data, currentDBPath);
                File currentDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getActivity(), "Import Successful!",
                        Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {

            Toast.makeText(getActivity(), "Import Failed!", Toast.LENGTH_SHORT)
                    .show();

        }
    }

    private void backup() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "com.simple.wildfishingnote"
                        + "//databases//" + MySQLiteHelper.DATABASE_NAME;
                String backupDBPath = "backup.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getActivity(), "Backup Successful!",
                        Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {

            Toast.makeText(getActivity(), "Backup Failed!", Toast.LENGTH_SHORT)
                    .show();

        }
    }
    
    private void backupImageFiles() {
        File source = new File("H:\\work-temp\\file");
        File dest = new File("H:\\work-temp\\file2");
        try {
            FileUtils.copyDirectory(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private void setBtns() {
        BootstrapButton btn = (BootstrapButton)tab4View.findViewById(R.id.buttonBackUp);
        btn.setOnClickListener(this);
        
        btn = (BootstrapButton)tab4View.findViewById(R.id.buttonRestore);
        btn.setOnClickListener(this);
    }
    
}
