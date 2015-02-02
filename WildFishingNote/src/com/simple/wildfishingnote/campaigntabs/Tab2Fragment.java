package com.simple.wildfishingnote.campaigntabs;

import java.util.List;

import org.holoeverywhere.widget.Toast;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.simple.wildfishingnote.AddMainActivity;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Place;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.database.CampaignDataSource;
import com.simple.wildfishingnote.place.AddPlaceActivity;
import com.simple.wildfishingnote.place.PlaceDetailActivity;

public class Tab2Fragment extends Fragment implements OnClickListener {
	private View tab2View;
	private CampaignDataSource dataSource;
	private PlaceArrayAdapter adapter;
	private AddMainActivity addMainActivity;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tab2View = inflater.inflate(R.layout.activity_tab2, container, false);
        addMainActivity = (AddMainActivity)getActivity();
        dataSource = addMainActivity.getCampaignDataSource();
		
		initPlaceListView(null);
		setAddPlaceBtn();
		setSavePlaceBtn();
		
        return tab2View;
    }

    /**
     * 监听所有onClick事件
     */
	@Override
    public void onClick(View v) {
        BootstrapButton b = (BootstrapButton)v;
        switch (v.getId()) {
            case R.id.buttonAddPlace:
                Intent intent = new Intent(getActivity(), AddPlaceActivity.class);
                startActivityForResult(intent, Constant.REQUEST_CODE_ADD_PLACE);
                break;
            case R.id.buttonSavePlace:
                if(addMainActivity.getCampaignId() == 0) {
                    Toast.makeText(getActivity(), "请先保存时间!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ListView listViewPlace = (ListView)tab2View.findViewById(R.id.listViewPlace);
                PlaceArrayAdapter adapter = (PlaceArrayAdapter)listViewPlace.getAdapter();
                Toast.makeText(getActivity(), adapter.getSelectedId(), Toast.LENGTH_SHORT).show();
                break;
        }
    }
	
	/**
	 * 监听所有onActivityResult事件
	 */
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // 添加钓点返回
        if (requestCode == Constant.REQUEST_CODE_ADD_PLACE && resultCode == Activity.RESULT_OK) {
            if (data.getExtras().containsKey(AddPlaceActivity.PLACE_ID)) {
                String placeId = data.getExtras().getString(AddPlaceActivity.PLACE_ID);
                initPlaceListView(placeId);
            }
        }
        
        // 编辑钓点返回
        if (requestCode == Constant.REQUEST_CODE_EDIT_PLACE && resultCode == Activity.RESULT_OK) {
            initPlaceListView(null);
        }
    }
	
	/**
	 * 初始化钓点listview
	 * @param placeId  是null，list中都不选中
	 *        placeId 不是null，list有选中值
	 */
	private void initPlaceListView(String placeId) {
	    dataSource.open();
        List<Place> list = dataSource.getPlacesForList();
        
        selectedPlaceById(placeId, list);
                
        ListView listView = (ListView) tab2View.findViewById(R.id.listViewPlace);
        adapter = new PlaceArrayAdapter(getActivity(), list);
        listView.setAdapter(adapter);
    }

	/**
	 * 根据ID选中钓点
	 */
    private void selectedPlaceById(String placeId, List<Place> list) {
        if (placeId != null) {
            for (int i = 0; i < list.size(); i++) {
                Place p = list.get(i);
                if (p.getId().equals(placeId)) {
                    p.setSelected(true);
                }
            }
        }
    }
    
    /**
     * 注册[保存]按钮事件
     */
    private void setSavePlaceBtn() {
        BootstrapButton savePlaceBtn = (BootstrapButton)tab2View.findViewById(R.id.buttonSavePlace);
        savePlaceBtn.setOnClickListener(this);
    }

    /**
     * 注册[添加]按钮事件
     */
    private void setAddPlaceBtn() {
        BootstrapButton addPlaceBtn = (BootstrapButton)tab2View.findViewById(R.id.buttonAddPlace);
        addPlaceBtn.setOnClickListener(this);
    }

	
    public class PlaceArrayAdapter extends ArrayAdapter<Place> {

        private final List<Place> list;
        private final Activity context;
        protected Object mActionMode;
        private RadioButton mSelectedRB;

        public PlaceArrayAdapter(Activity context, List<Place> list) {
            super(context, R.layout.activity_place_listview_each_item, list);
            this.context = context;
            this.list = list;
        }

        public int getCount() {
            return list.size();
        }

        class ViewHolder {
            protected TextView textPlaceTitle;
            protected RadioButton radio;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                LayoutInflater inflator = context.getLayoutInflater();
                view = inflator.inflate(R.layout.activity_place_listview_each_item, null);
                final ViewHolder viewHolder = new ViewHolder();

                viewHolder.textPlaceTitle = (TextView)view.findViewById(R.id.textViewPlaceTitle);
                viewHolder.radio = (RadioButton)view.findViewById(R.id.placeRadio);
                addRadioButtonOnCheckedChangeListener(viewHolder);

                view.setTag(viewHolder);
                viewHolder.radio.setTag(list.get(position));

                LinearLayout contentLayout = (LinearLayout)view.findViewById(R.id.col1Layout);
                addContentLayoutOnClickListener(viewHolder, contentLayout);
                addContentLayoutOnLongClickListener(viewHolder, contentLayout);

            } else {
                view = convertView;
                ((ViewHolder)view.getTag()).radio.setTag(list.get(position));
            }
            ViewHolder holder = (ViewHolder)view.getTag();
            holder.textPlaceTitle.setText(list.get(position).getTitle());
            holder.radio.setChecked(list.get(position).isSelected());
            return view;
        }

        /**
         * 监听list item长按事件
         * @param viewHolder
         * @param contentLayout
         */
        private void addContentLayoutOnLongClickListener(
                final ViewHolder viewHolder, LinearLayout contentLayout) {
            contentLayout.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View paramView) {

                    final Place element = (Place)viewHolder.radio.getTag();

                    PopupMenu popup = new PopupMenu(context, paramView);
                    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            final String placeId = element.getId();
                            switch (item.getItemId()) {
                                case R.id.edit:
                                    editPlace(placeId);
                                    return true;
                                case R.id.delete:

                                    // db.deletePoint(placeId);

                                    deletePlace(placeId);
                                    return true;
                                default:
                                    return false;
                            }

                        }
                    });
                        

                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.select_place_context_menu, popup.getMenu());
                    popup.show();

                    return true;

                }
            });
        }
        
        /**
         * 编辑钓位
         * @param placeId
         */
        private void editPlace(final String placeId) {
            Intent intent = new Intent(context, AddPlaceActivity.class);
            intent.putExtra(AddPlaceActivity.PLACE_ID, placeId);
            startActivityForResult(intent, Constant.REQUEST_CODE_EDIT_PLACE);
        }
        
        /**
         * 删除钓位
         * @param placeId
         */
        private void deletePlace(final String placeId) {
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            adb.setTitle("删除?");
            adb.setMessage("确定删除钓位");
            adb.setNegativeButton(R.string.cancel, null);
            adb.setPositiveButton(R.string.confirm, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dataSource.deletePlace(placeId);
                    
                    int deleteIndex = 0;
                    for (int i = 0; i < list.size(); i++) {
                        if (placeId.equals(list.get(i).getId())) {
                            deleteIndex = i;
                            break;
                        }
                    }
                    list.remove(deleteIndex);
                    adapter.notifyDataSetChanged();
                }
            });
            adb.show();
        }
    

        /**
         * 监听list item单击事件
         * @param viewHolder
         * @param contentLayout
         */
        private void addContentLayoutOnClickListener(
                final ViewHolder viewHolder, LinearLayout contentLayout) {
            contentLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Place element = (Place)viewHolder.radio.getTag();
                    Intent intent = new Intent(context, PlaceDetailActivity.class);
                    intent.putExtra(PlaceDetailActivity.ID, element.getId());
                    context.startActivity(intent);
                }
            });
        }

        /**
         * 监听raidobutton选中变更事件
         * @param viewHolder
         */
        private void addRadioButtonOnCheckedChangeListener(
                final ViewHolder viewHolder) {
            viewHolder.radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                        boolean isChecked) {
                    // 画面状态更新到bean中
                    Place element = (Place)viewHolder.radio.getTag();
                    element.setSelected(buttonView.isChecked());

                    // 取消前一次选中
                    if (mSelectedRB != null) {
                        mSelectedRB.setChecked(false);
                    }

                    mSelectedRB = (RadioButton)buttonView;
                }
            });
        }

        /**
         * 取得选中raidobutton对应的ID
         * @return
         */
        public String getSelectedId() {
            String retId = "";
            for (Place p : list) {
                if (p.isSelected()) {
                    retId = p.getId();
                    return retId;
                }
            }

            return retId;
        }

    }

}
