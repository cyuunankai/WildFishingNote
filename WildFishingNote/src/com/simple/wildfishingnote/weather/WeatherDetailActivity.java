package com.simple.wildfishingnote.weather;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.Hourly;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.database.WeatherDataSource;
import com.simple.wildfishingnote.utils.StringUtils;

public class WeatherDetailActivity extends ActionBarActivity {

    public static final String ID = "id";
    private WeatherDataSource dataSource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_detail);
		
		dataSource = new WeatherDataSource(this);
        dataSource.open();
		
		Intent intent = getIntent();
		String weatherId = intent.getStringExtra(ID);
		initWeatherDetail(weatherId);
	}

    private void initWeatherDetail(String weatherId) {
        List<Hourly> list = dataSource.getWeathersHourly(weatherId);
        
        ListView listView = (ListView) findViewById(R.id.weather_detail_list_view);
        View header = getLayoutInflater().inflate(R.layout.weather_hourly_list_view_header, null);
        listView.addHeaderView(header);
        WeatherHourlyArrayAdapter adapter = new WeatherHourlyArrayAdapter(this, list);
        listView.setAdapter(adapter);
    }
    
    public class WeatherHourlyArrayAdapter extends ArrayAdapter<Hourly> {

        private final List<Hourly> list;
        private final Activity context;
        protected Object mActionMode;

        public WeatherHourlyArrayAdapter(Activity context, List<Hourly> list) {
            super(context, R.layout.weather_hourly_list_view_each_item, list);
            this.context = context;
            this.list = list;
        }
        
        class ViewHolder {
            protected TextView timeTextView;
            protected TextView tempCTextView;
            protected TextView windDirDegreeTextView;
            protected TextView windSpeedKmphTextView;
            protected TextView pressureTextView;
            protected TextView weatherNameTextView;
//            protected TextView cloudCoverTextView;
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
                convertView = context.getLayoutInflater().inflate(R.layout.weather_hourly_list_view_each_item, null);
                viewHolder.timeTextView = (TextView)convertView.findViewById(R.id.timeTextView);
                viewHolder.tempCTextView = (TextView)convertView.findViewById(R.id.tempCTextView);
                viewHolder.windDirDegreeTextView = (TextView)convertView.findViewById(R.id.windDirDegreeTextView);
                viewHolder.windSpeedKmphTextView = (TextView)convertView.findViewById(R.id.windSpeedKmphTextView);
                viewHolder.pressureTextView = (TextView)convertView.findViewById(R.id.pressureTextView);
                viewHolder.weatherNameTextView = (TextView)convertView.findViewById(R.id.weatherNameTextView);
//                viewHolder.cloudCoverTextView = (TextView)convertView.findViewById(R.id.cloudCoverTextView);
                viewHolder.timeTextView.setTag(list.get(position));//// 保存bean值到UI tag (响应事件从这个UI tag取值)
                convertView.setTag(viewHolder);
            }

            // 设置bean值到UI
            viewHolder.timeTextView.setText(StringUtils.convertToTimeFormat(list.get(position).getTime()));
            viewHolder.tempCTextView.setText(list.get(position).getTempC() + Constant.TEMP_C_SYMBOL);
            viewHolder.windDirDegreeTextView.setText(list.get(position).getWinddirDegree());
            viewHolder.windSpeedKmphTextView.setText(list.get(position).getWindspeedKmph());
            viewHolder.pressureTextView.setText(list.get(position).getPressure());
            viewHolder.weatherNameTextView.setText(list.get(position).getWeatherName());
//            viewHolder.cloudCoverTextView.setText(list.get(position).getCloudcover());
            
            viewHolder.timeTextView.setTag(list.get(position));//// 保存bean值到UI tag (响应事件从这个UI tag取值)

            return convertView;
        }

        public int getCount() {
            return list.size();
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
