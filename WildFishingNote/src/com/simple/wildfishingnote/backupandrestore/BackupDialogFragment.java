package com.simple.wildfishingnote.backupandrestore;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.common.DirectoryChooserDialog;
import com.simple.wildfishingnote.database.MySQLiteHelper;
import com.simple.wildfishingnote.utils.Msg;

public class BackupDialogFragment extends DialogFragment {
	private Activity mActivity;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    mActivity = getActivity();
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    final View mView = inflater.inflate(R.layout.dialog_backup, null);
	    
	    BootstrapButton dirChooserButton = (BootstrapButton)mView.findViewById(R.id.buttonDirectoryChooser);
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
                        TextView tv = (TextView) mView.findViewById(R.id.tvBackupPath);
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
	    
	    String defaultDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Constant.BACKUP_FOLDER_NAME;
	    TextView tvBackupPath = (TextView)mView.findViewById(R.id.tvBackupPath);
	    tvBackupPath.setText(defaultDir);
	    
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(mView)
	    .setTitle("备份数据")
	    // Add action buttons
	           .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   TextView tvBackupPath = (TextView)mView.findViewById(R.id.tvBackupPath);
	                   tvBackupPath.getText().toString();
	               }
	           })
	           .setNegativeButton("取消", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   BackupDialogFragment.this.getDialog().cancel();
	               }
	           });      
	    
	    final AlertDialog dialog = builder.create();
        dialog.show();
        //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView tvBackupPath = (TextView)mView.findViewById(R.id.tvBackupPath);
                new BackupTask().execute(tvBackupPath.getText().toString());

                Boolean wantToCloseDialog = false;
                // Do stuff, possibly set wantToCloseDialog to true then...
                if (wantToCloseDialog)
                    dialog.dismiss();
                // else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set
                // cancellable to false.
            }
        });
	    return dialog;
	}
	
	
	   private String backup(String directory) {
	       String ret = "备份失败";
	        try {
	            backupDatabase(directory);
	            backupPlaceImageFiles(directory);
	            backupFishResultImageFiles(directory);
	            
	            ret = "备份成功";
	        } catch (IOException e) {
//	            ret = "备份失败" + e.getMessage();
	        }
	        
	        return ret;
	    }

	    private void backupDatabase(String directory) throws IOException {
	        File sd = Environment.getExternalStorageDirectory();
	        File data = Environment.getDataDirectory();

	        if (sd.canWrite()) {
	            
	            String srcFileStr = "//data//" + "com.simple.wildfishingnote"
                      + "//databases//" + MySQLiteHelper.DATABASE_NAME;
                String destFileStr = MySQLiteHelper.DATABASE_NAME;
                
                File srcFile = new File(data, srcFileStr);
                File destFile = new File(directory, destFileStr);
                FileUtils.copyFile(srcFile, destFile);
	        }

	    }
	    
	    private void backupPlaceImageFiles(String directory) throws IOException {

	        String imagePath = mActivity.getFilesDir()
	                + Constant.PLACE_IMAGE_PATH;
	        File sd = Environment.getExternalStorageDirectory();

	        if (sd.canWrite()) {
	            File source = new File(imagePath);
	            File dest = new File(directory, Constant.PLACE_IMAGE_PATH);

	            FileUtils.deleteDirectory(dest);
	            FileUtils.copyDirectory(source, dest);
	        }

	    }
	    
	    private void backupFishResultImageFiles(String directory) throws IOException {
	        String imagePath = mActivity.getFilesDir()
	                + Constant.FISH_RESULT_IMAGE_PATH;
	        File sd = Environment.getExternalStorageDirectory();

	        if (sd.canWrite()) {
	            File source = new File(imagePath);
	            File dest = new File(directory, Constant.FISH_RESULT_IMAGE_PATH);

	            FileUtils.deleteDirectory(dest);
	            FileUtils.copyDirectory(source, dest);
	        }
	    }
	    
	    class BackupTask extends AsyncTask<String, Void, String> {

            public static final boolean LOGD = true;

            protected String doInBackground(String... directory) {
//                Msg.showLongTime(mActivity, "备份中");
                String ret = backup(directory[0]);
                return ret;
            }
            
            protected void onPostExecute(String msg) {
                Msg.showLongTime(mActivity, msg);
            }
        }

}
