package com.simple.wildfishingnote.tabs;

import java.util.List;

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

import com.simple.wildfishingnote.AddMainActivity;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Place;
import com.simple.wildfishingnote.campaign.PlaceDetailActivity;
import com.simple.wildfishingnote.database.CampaignDataSource;

public class Tab2Fragment extends Fragment {
	private View tab2View;
	private CampaignDataSource dataSource;
	private PlaceArrayAdapter adapter;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		tab2View = inflater.inflate(R.layout.activity_tab2, container, false);
		dataSource = ((AddMainActivity)getActivity()).getCampaignDataSource();
		
		List<Place> list = dataSource.getPlacesForList();
		Place p = new Place();
		p.setId("1");
		p.setTitle("title");
		p.setDetail("detail");
		p.setFileName("d:/");
		list.add(p);
		p = new Place();
		p.setId("2");
		p.setTitle("title2");
		p.setDetail("detail2");
		p.setFileName("d:/2");
		list.add(p);
		
		ListView listView = (ListView) tab2View.findViewById(R.id.listViewPlace);
		adapter = new PlaceArrayAdapter(getActivity(), list);
		listView.setAdapter(adapter);
		
		
        return tab2View;
    }
	
	public class PlaceArrayAdapter extends ArrayAdapter<Place>
	 {

		  private final List<Place> list;
		  private final Activity context;
		  protected Object mActionMode;
		  private RadioButton mSelectedRB;
		  
		public PlaceArrayAdapter(Activity context, List<Place> list) {
		    super(context, R.layout.activity_place_listview_each_item, list);
		    this.context = context;
		    this.list = list;
		  }
		
		public int getCount() 
	    {
	        // return the number of records in cursor
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
		      
		      viewHolder.textPlaceTitle = (TextView) view.findViewById(R.id.textViewPlaceTitle);
		      viewHolder.radio = (RadioButton) view.findViewById(R.id.placeRadio);
		      addRadioButtonOnCheckedChangeListener(viewHolder);
		      
		      view.setTag(viewHolder);
		      viewHolder.radio.setTag(list.get(position));
		      
		      LinearLayout contentLayout = (LinearLayout)view.findViewById(R.id.col1Layout);
		      addContentLayoutOnClickListener(viewHolder, contentLayout);
		      addContentLayoutOnLongClickListener(viewHolder, contentLayout);
		      
		    } else {
		      view = convertView;
		      ((ViewHolder) view.getTag()).radio.setTag(list.get(position));
		    }
		    ViewHolder holder = (ViewHolder) view.getTag();
		    holder.textPlaceTitle.setText(list.get(position).getTitle());
		    holder.radio.setChecked(list.get(position).isSelected());
		    return view;
		  }

		private void addContentLayoutOnLongClickListener(
				final ViewHolder viewHolder, LinearLayout contentLayout) {
			contentLayout.setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View paramView) {

					final Place element = (Place) viewHolder.radio.getTag();
					
					PopupMenu popup = new PopupMenu(context, paramView);
					popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
						
						@Override
						public boolean onMenuItemClick(MenuItem item) {
							final String placeId = element.getId();
							switch (item.getItemId()) {
					        case R.id.edit:
					        	
					            return true;
					        case R.id.delete:
					        	
					        	//db.deletePoint(placeId);
					        	
					        	AlertDialog.Builder adb=new AlertDialog.Builder(context);
					            adb.setTitle("Delete?");
					            adb.setMessage("Are you sure you want to delete ");
					            adb.setNegativeButton("Cancel", null);
					            adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
					                public void onClick(DialogInterface dialog, int which) {
					                	int deleteIndex = 0;
							        	for(int i=0; i<list.size(); i++){
					                    	if(placeId.equals(list.get(i).getId())){
					                    		deleteIndex = i;
					                    		break;
					                    	}
					                    }
							        	list.remove(deleteIndex);
					                    adapter.notifyDataSetChanged();
					                }});
					            adb.show();
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

		private void addContentLayoutOnClickListener(
				final ViewHolder viewHolder, LinearLayout contentLayout) {
			contentLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Place element = (Place) viewHolder.radio.getTag();
					Intent intent = new Intent(context, PlaceDetailActivity.class);
					intent.putExtra(PlaceDetailActivity.ID, element.getId());
					intent.putExtra(PlaceDetailActivity.TITLE, element.getTitle());
					context.startActivity(intent);
				}
		      });
		}

		private void addRadioButtonOnCheckedChangeListener(
				final ViewHolder viewHolder) {
			viewHolder.radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

	            @Override
	            public void onCheckedChanged(CompoundButton buttonView,
	                boolean isChecked) {
	            	// 画面状态更新到bean中
	            	Place element = (Place) viewHolder.radio.getTag();
	            	element.setSelected(buttonView.isChecked());
	            	
	            	// 取消前一次选中
	            	if(mSelectedRB != null){
	                    mSelectedRB.setChecked(false);
	                }

	                mSelectedRB = (RadioButton)buttonView;
	            }
	          });
		}
		  
		  public String getSelectedId() {
			  String retId = "";
			  for(Place p : list){
				  if(p.isSelected()){
					  retId = p.getId();
					  return retId;
				  }
			  }
		      
			  return retId;
		  }

	} 

}
