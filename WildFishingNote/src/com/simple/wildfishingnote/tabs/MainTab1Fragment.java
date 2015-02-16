package com.simple.wildfishingnote.tabs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.sectionedlistview.SectionListAdapter;
import com.simple.wildfishingnote.sectionedlistview.SectionListItem;
import com.simple.wildfishingnote.sectionedlistview.SectionListView;

public class MainTab1Fragment extends Fragment {
    
    SectionListItem[] exampleArray = { // Comment to prevent re-format
            new SectionListItem("Test 1 - A", "A"), //
                    new SectionListItem("Test 2 - A", "A"), //
                    new SectionListItem("Test 3 - A", "A"), //
                    new SectionListItem("Test 4 - A", "A"), //
                    new SectionListItem("Test 5 - A", "A"), //
                    new SectionListItem("Test 6 - B", "B"), //
                    new SectionListItem("Test 7 - B", "B"), //
                    new SectionListItem("Test 8 - B", "B"), //
                    new SectionListItem("Test 9 - Long", "Long section"), //
                    new SectionListItem("Test 10 - Long", "Long section"), //
                    new SectionListItem("Test 11 - Long", "Long section"), //
                    new SectionListItem("Test 12 - Long", "Long section"), //
                    new SectionListItem("Test 13 - Long", "Long section"), //
                    new SectionListItem("Test 14 - A again", "A"), //
                    new SectionListItem("Test 15 - A again", "A"), //
                    new SectionListItem("Test 16 - A again", "A"), //
                    new SectionListItem("Test 17 - B again", "B"), //
                    new SectionListItem("Test 18 - B again", "B"), //
                    new SectionListItem("Test 19 - B again", "B"), //
                    new SectionListItem("Test 20 - B again", "B"), //
                    new SectionListItem("Test 21 - B again", "B"), //
                    new SectionListItem("Test 22 - B again", "B"), //
                    new SectionListItem("Test 23 - C", "C"), //
                    new SectionListItem("Test 24 - C", "C"), //
                    new SectionListItem("Test 25 - C", "C"), //
                    new SectionListItem("Test 26 - C", "C"), //
            };
    
    private StandardArrayAdapter arrayAdapter;

    private SectionListAdapter sectionAdapter;

    private SectionListView listView;
    
    private View tab1View;
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
	    
        tab1View = inflater.inflate(R.layout.activity_main_tab1, container, false);

        arrayAdapter = new StandardArrayAdapter(getActivity(), R.id.example_text_view, exampleArray);
        sectionAdapter = new SectionListAdapter(inflater, arrayAdapter);
        listView = (SectionListView)tab1View.findViewById(R.id.section_list_view);
        listView.setAdapter(sectionAdapter);
        
        return tab1View;
    }
	
	private class StandardArrayAdapter extends ArrayAdapter<SectionListItem> {

        private final SectionListItem[] items;

        public StandardArrayAdapter(final Context context,
                final int textViewResourceId, final SectionListItem[] items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(final int position, final View convertView,
                final ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                final LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.example_list_view, null);
            }
            final SectionListItem currentItem = items[position];
            if (currentItem != null) {
                final TextView textView = (TextView) view
                        .findViewById(R.id.example_text_view);
                if (textView != null) {
                    textView.setText(currentItem.item.toString());
                }
            }
            return view;
        }
    }

}
