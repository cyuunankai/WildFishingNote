package com.simple.wildfishingnote.tabs;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.simple.wildfishingnote.AddMainActivity;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Place;
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
		    super(context, R.layout.activity_listview_each_item_place, list);
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
		      view = inflator.inflate(R.layout.activity_listview_each_item_place, null);
		      final ViewHolder viewHolder = new ViewHolder();
		      
		      viewHolder.textPlaceTitle = (TextView) view.findViewById(R.id.textViewPlaceTitle);
		      viewHolder.radio = (RadioButton) view.findViewById(R.id.placeRadio);
		      viewHolder.radio.setOnClickListener(new OnClickListener() {

		            @Override
		            public void onClick(View v) {

		                if(mSelectedRB != null){
		                    mSelectedRB.setChecked(false);
		                }

		                mSelectedRB = (RadioButton)v;
//		                setSelectText(list.get ( position ).getText ( ));
		            }
		        });
		      
		      view.setTag(viewHolder);
		      viewHolder.radio.setTag(list.get(position));
		      
		      LinearLayout ll = (LinearLayout)view.findViewById(R.id.col1Layout);
//		      ll.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					Point element = (Point) viewHolder.checkbox.getTag();
//					Intent intent = new Intent(context, PointDetailActivity.class);
//					intent.putExtra(PointDetailActivity.ROD_LENGTH_NAME, element.getRodLengthName());
//					intent.putExtra(PointDetailActivity.DEPTH, element.getDepth());
//					intent.putExtra(PointDetailActivity.LURE_METHOD_NAME, element.getLureMethodName());
//					intent.putExtra(PointDetailActivity.BAIT, element.getBaitName());
//					context.startActivity(intent);
//				}
//		      });
		      
//		      ll.setOnLongClickListener(new View.OnLongClickListener() {
//				
//				@Override
//				public boolean onLongClick(View paramView) {
//
//					final Point element = (Point) viewHolder.checkbox
//			                  .getTag();
//					
//					PopupMenu popup = new PopupMenu(context, paramView);
//					popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//						
//						@Override
//						public boolean onMenuItemClick(MenuItem item) {
//							final String pointId = element.getId();
//							switch (item.getItemId()) {
//					        case R.id.edit:
//					        	
//					            return true;
//					        case R.id.delete:
//					        	
//					        	//db.deletePoint(placeId);
//					        	
//					        	AlertDialog.Builder adb=new AlertDialog.Builder(context);
//					            adb.setTitle("Delete?");
//					            adb.setMessage("Are you sure you want to delete ");
//					            adb.setNegativeButton("Cancel", null);
//					            adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
//					                public void onClick(DialogInterface dialog, int which) {
//					                	int deleteIndex = 0;
//							        	for(int i=0; i<list.size(); i++){
//					                    	if(pointId.equals(list.get(i).getId())){
//					                    		deleteIndex = i;
//					                    		break;
//					                    	}
//					                    }
//							        	list.remove(deleteIndex);
//					                    adapter.notifyDataSetChanged();
//					                }});
//					            adb.show();
//					            return true;
//					        default:
//					            return false;
//					    }
//
//						}
//					});
//					
//				    MenuInflater inflater = popup.getMenuInflater();
//				    inflater.inflate(R.menu.select_point_context_menu, popup.getMenu());
//				    popup.show();
//
//			        return true;
//
//				}
//			});
		      
		    } else {
		      view = convertView;
		      ((ViewHolder) view.getTag()).radio.setTag(list.get(position));
		    }
		    ViewHolder holder = (ViewHolder) view.getTag();
		    holder.textPlaceTitle.setText(list.get(position).getTitle());
		    holder.radio.setChecked(list.get(position).isSelected());
		    return view;
		  }
		  
		  public List<String> getSelectedPointIdList() {
			  List<String> retList = new ArrayList<String>();
			  for(Place p : list){
				  if(p.isSelected()){
					  retList.add(p.getId());
				  }
			  }
		      
			  return retList;
		  }

	} 

}
