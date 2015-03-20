package com.simple.wildfishingnote.backupandrestore;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Backup;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.common.DirectoryChooserDialog;
import com.simple.wildfishingnote.database.BackupDataSource;
import com.simple.wildfishingnote.database.MySQLiteHelper;
import com.simple.wildfishingnote.utils.DateUtil;
import com.simple.wildfishingnote.utils.Msg;

public class RestoreActivity extends ActionBarActivity {

    private BackupDataSource dataSource;
    private BackupArrayAdapter adapter;
    private TextView tvRestorePath;
    private BootstrapButton buttonDirectoryChooser;
    private Context mContext;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore);
        try{
        mContext = this;
        dataSource = new BackupDataSource(this);
        dataSource.open();
        
        initListView();
        
        setBtns();
        }
        catch(Exception e){
            Msg.showLongTime(this, e.getMessage());
        }
        
    }

    private void setBtns() {
        
        buttonDirectoryChooser = (BootstrapButton) findViewById(R.id.buttonDirectoryChooser);
        buttonDirectoryChooser.setEnabled(false);
        
        buttonDirectoryChooser.setOnClickListener(new OnClickListener() {
            private String m_chosenDir = "";
            private boolean m_newFolderEnabled = true;

            @Override
            public void onClick(View v) {
                // Create DirectoryChooserDialog and register a callback
                DirectoryChooserDialog directoryChooserDialog =
                        new DirectoryChooserDialog(RestoreActivity.this,
                                new DirectoryChooserDialog.ChosenDirectoryListener() {
                                    @Override
                                    public void onChosenDir(String chosenDir) {
                                        m_chosenDir = chosenDir;
                                        tvRestorePath = (TextView) findViewById(R.id.tvRestorePath);
                                        tvRestorePath.setText(chosenDir);
                                    }
                                });
                // Toggle new folder button enabling
                directoryChooserDialog.setNewFolderEnabled(m_newFolderEnabled);
                // Load directory chooser dialog for initial 'm_chosenDir' directory.
                // The registered callback will be called upon final directory selection.
                directoryChooserDialog.chooseDirectory(m_chosenDir);
                m_newFolderEnabled = !m_newFolderEnabled;
            }
        });
        
        BootstrapButton buttonCancel = (BootstrapButton) findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        
        
        BootstrapButton buttonRestore = (BootstrapButton) findViewById(R.id.buttonRestore);
        buttonRestore.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                String restoreDir = adapter.getSelectedRestorePath();
                if(restoreDir.equals("指定路径")){
                    restoreDir = tvRestorePath.getText().toString();
                }
                new RestoreTask().execute(restoreDir);
            }
        });
    }
    
    private void initListView() {

        List<Backup> list = dataSource.getBackups();
        Backup obj = new Backup();
        obj.setPath("指定路径");
        obj.setTime("");
        obj.setSelected(false);
        list.add(obj);
        
        selectedRestorePathByIndex(0, list);
        
        ListView listView = (ListView) findViewById(R.id.listViewRestore);
        adapter = new BackupArrayAdapter(this, list);
        listView.setAdapter(adapter);
    }
    
    private void selectedRestorePathByIndex(int index, List<Backup> list) {
        Backup obj = list.get(index);
        obj.setSelected(true);
    }
    
    private String restore(String directory) {
        String ret = "恢复失败";
        try {
            restoreDatabase(directory);
            restorePlaceImageFiles(directory);
            restoreFishResultImageFiles(directory);
            ret = "恢复成功";
        } catch (IOException e) {
            ret = "恢复失败" + e.getMessage();
        }
        
        return ret;
    }

    private void restoreDatabase(String directory) throws IOException {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        if (sd.canWrite()) {
            String srcFileStr = "//data//" + "com.simple.wildfishingnote"
                    + "//databases//" + MySQLiteHelper.DATABASE_NAME;
            String destFileStr = MySQLiteHelper.DATABASE_NAME;

            File srcFile = new File(directory, destFileStr);
            File destFile = new File(data, srcFileStr);
            FileUtils.copyFile(srcFile, destFile);
        }
    }
    
    private void restorePlaceImageFiles(String directory) throws IOException {

        String imagePath = this.getFilesDir()
                + Constant.PLACE_IMAGE_PATH;
        File sd = Environment.getExternalStorageDirectory();

        if (sd.canWrite()) {
            File source = new File(directory, Constant.PLACE_IMAGE_PATH);
            File dest = new File(imagePath);

            FileUtils.deleteDirectory(dest);
            FileUtils.copyDirectory(source, dest);
        }

    }
    
    private void restoreFishResultImageFiles(String directory) throws IOException {

        String imagePath = this.getFilesDir()
                + Constant.FISH_RESULT_IMAGE_PATH;
        File sd = Environment.getExternalStorageDirectory();

        if (sd.canWrite()) {
            File source = new File(directory, Constant.FISH_RESULT_IMAGE_PATH);
            File dest = new File(imagePath);

            FileUtils.deleteDirectory(dest);
            FileUtils.copyDirectory(source, dest);
        }
    }
    
    class RestoreTask extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(mContext);
            this.dialog.setMessage("数据恢复中...");
            this.dialog.show();
        }

        protected String doInBackground(String... directory) {
            // Msg.showLongTime(mActivity, "备份中");

            String ret = restore(directory[0]);
            return ret;
        }
        
        protected void onPostExecute(String msg) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Msg.showLongTime(mContext, msg);
        }
    }
    
    public class BackupArrayAdapter extends ArrayAdapter<Backup> {

        private final List<Backup> list;
        private final Activity context;
        protected Object mActionMode;
        private RadioButton mSelectedRB;

        public BackupArrayAdapter(Activity context, List<Backup> list) {
            super(context, R.layout.activity_restore_listview_each_item, list);
            this.context = context;
            this.list = list;
        }
        
        /**
         * 取得选中raidobutton对应的ID
         * @return
         */
        public String getSelectedRestorePath() {
            String ret = "";
            for (Backup p : list) {
                if (p.isSelected()) {
                    ret = p.getPath();
                    return ret;
                }
            }

            return ret;
        }

        class ViewHolder {
            protected TextView textViewPath;
            protected TextView textViewTime;
            protected RadioButton restoreRadio;
        }

        /**
         * convertView -> viewHolder -> UI
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            
            if (convertView == null) {
                // 添加UI到convertView
                convertView = context.getLayoutInflater().inflate(R.layout.activity_restore_listview_each_item, null);
                viewHolder.textViewPath = (TextView)convertView.findViewById(R.id.textViewPath);
                viewHolder.textViewTime = (TextView)convertView.findViewById(R.id.textViewTime);
                viewHolder.restoreRadio = (RadioButton)convertView.findViewById(R.id.restoreRadio);
                viewHolder.restoreRadio.setTag(list.get(position));//// 保存bean值到UI tag (响应事件从这个UI tag取值)
                convertView.setTag(viewHolder);
                
                // 添加事件
                //// radio事件
                addRadioButtonOnCheckedChangeListener(viewHolder);
            }

            // 设置bean值到UI
            viewHolder.textViewPath.setText(list.get(position).getPath());
            viewHolder.textViewTime.setText(list.get(position).getTime());
            viewHolder.restoreRadio.setChecked(list.get(position).isSelected());
            
            viewHolder.restoreRadio.setTag(list.get(position));//// 保存bean值到UI tag (响应事件从这个UI tag取值)

            return convertView;
        }

        /**
         * 监听raidobutton选中变更事件
         * @param viewHolder
         */
        private void addRadioButtonOnCheckedChangeListener(
                final ViewHolder viewHolder) {
            viewHolder.restoreRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                        boolean isChecked) {
                    // 画面状态更新到bean中
                    Backup element = (Backup)viewHolder.restoreRadio.getTag();
                    element.setSelected(buttonView.isChecked());

                    // 取消前一次选中
                    if (mSelectedRB != null) {
                        mSelectedRB.setChecked(false);
                    }

                    mSelectedRB = (RadioButton)buttonView;
                    
                    if (element.getPath().equals("指定路径")) {
                        buttonDirectoryChooser.setEnabled(true);
                    } else {
                        buttonDirectoryChooser.setEnabled(false);
                    }
                }
            });
        }


        public int getCount() {
            return list.size();
        }
        
        @Override
        public int getViewTypeCount() {                 
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

    }
    
    @Override
    protected void onResume() {
      dataSource.open();
      super.onResume();
    }

    @Override
    protected void onPause() {
      dataSource.close();
      super.onPause();
    }
}
