package com.simple.wildfishingnote.tabs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.simple.wildfishingnote.R;
import com.simple.wildfishingnote.bean.LocationData;
import com.simple.wildfishingnote.bean.Weather;
import com.simple.wildfishingnote.bean.WeatherAndLocation;
import com.simple.wildfishingnote.common.Constant;
import com.simple.wildfishingnote.database.WeatherDataSource;
import com.simple.wildfishingnote.datetimepicker.DatePickerFragment;
import com.simple.wildfishingnote.utils.StringUtils;
import com.simple.wildfishingnote.weather.LocalWeather;
import com.simple.wildfishingnote.weather.LocationSearch;
import com.simple.wildfishingnote.weather.WeatherNoticeActivity;
import com.simple.wildfishingnote.weather.schdule.AlarmManagerBroadcastReceiver;
import com.simple.wildfishingnote.weather.service.HistoryWeatherService;

public class MainTab2Fragment extends Fragment implements OnClickListener {
    
    private View tab2View;
//    private WeatherDataSource dataSource;
    
    LocationManager locationManager;
    Location location;
    LocationListener locationListener;
    boolean network_enabled=false;
    
    TextView weatherTextView;
    TextView geoTextView;
    
    private AlarmManagerBroadcastReceiver alarm;
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
	    
	    tab2View = inflater.inflate(R.layout.activity_main_tab2, container, false);
        
        alarm = new AlarmManagerBroadcastReceiver();
        
        setStartSchduleBtn();
        setShowWeatherBtn();
        setStartBtn();
        setEndBtn();
        setImportBtn();
//      WildFishingDatabase wfd = new WildFishingDatabase(getApplicationContext());
//      wfd.addWeather(new WeatherAndLocation());
        
        // for debug (debug cannot get location by GPS,network... etc)
        String qLocation = "41.73,123.47";
        new RetrieveWeatherTask().execute(qLocation);
//        registLocationListener();

        setDateBtnText();
        
        return tab2View;
    }

    
	
	@Override
    public void onClick(View v) {
        BootstrapButton b = (BootstrapButton)v;
        switch (v.getId()) {
            case R.id.startSchduleBtn:
                alarm.SetAlarm(getActivity());
                break;
            case R.id.showWeatherBtn:
                showWeather();
                break;
            case R.id.startBtn:
                showStartDatePickerDialog(b);
                break;
            case R.id.endBtn:
                showEndDatePickerDialog(b);
                break;
            case R.id.importBtn:
                importHistoryWeather();
                break;
            
        }
    }
	
	private void setStartSchduleBtn() {
        BootstrapButton btn = (BootstrapButton)tab2View.findViewById(R.id.startSchduleBtn);
        btn.setOnClickListener(this);
    }
	
	private void setShowWeatherBtn() {
        BootstrapButton btn = (BootstrapButton)tab2View.findViewById(R.id.showWeatherBtn);
        btn.setOnClickListener(this);
    }
	
	private void setStartBtn() {
        BootstrapButton btn = (BootstrapButton)tab2View.findViewById(R.id.startBtn);
        btn.setOnClickListener(this);
    }
	
	private void setEndBtn() {
        BootstrapButton btn = (BootstrapButton)tab2View.findViewById(R.id.endBtn);
        btn.setOnClickListener(this);
    }
	
	private void setImportBtn() {
        BootstrapButton btn = (BootstrapButton)tab2View.findViewById(R.id.importBtn);
        btn.setOnClickListener(this);
    }

    
	private void showWeather() {
        WeatherDataSource dataSource = new WeatherDataSource(getActivity());
        dataSource.open();
        Toast.makeText(getActivity(), dataSource.getWeathers(), Toast.LENGTH_SHORT).show();
        dataSource.close();
        
    }
    
    private void setDateBtnText(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        
        BootstrapButton startDateBtn = (BootstrapButton)tab2View.findViewById(R.id.startBtn);
        startDateBtn.setText(year + Constant.DASH +StringUtils.leftPadTwo(month + 1) + Constant.DASH + StringUtils.leftPadTwo(day));
        
        BootstrapButton endDateBtn = (BootstrapButton)tab2View.findViewById(R.id.endBtn);
        endDateBtn.setText(year + Constant.DASH +StringUtils.leftPadTwo(month + 1) + Constant.DASH + StringUtils.leftPadTwo(day));
    }
    
    private void showStartDatePickerDialog(BootstrapButton b) {
        
        DialogFragment newFragment = new DatePickerFragment(b);
        newFragment.show(getActivity().getSupportFragmentManager(), "startDatePicker");
    }
    
    private void showEndDatePickerDialog(BootstrapButton b) {
        
        DialogFragment newFragment = new DatePickerFragment(b);
        newFragment.show(getActivity().getSupportFragmentManager(), "endDatePicker");
    }
    
    private void importHistoryWeather() {
        BootstrapButton startDateBtn = (BootstrapButton)tab2View.findViewById(R.id.startBtn);
        BootstrapButton endDateBtn = (BootstrapButton)tab2View.findViewById(R.id.endBtn);
        String startDateStr = startDateBtn.getText().toString();
        String endDateStr = endDateBtn.getText().toString();
        
        Intent i = new Intent(getActivity(), HistoryWeatherService.class);
        i.putExtra("startDateStr", startDateStr);
        i.putExtra("endDateStr", endDateStr);
        getActivity().startService(i);
    }
    
    public void noficate(View view){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity())
                .setSmallIcon(R.drawable.abc_ab_bottom_solid_dark_holo)
                .setContentTitle("My notification")
                .setContentText("Hello World!");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(getActivity(), WeatherNoticeActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(WeatherNoticeActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
            (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());

    }
    
    public void registLocationListener() {
        
        boolean network_enabled=false;

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            network_enabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "network is not enable ",
                    Toast.LENGTH_SHORT).show();
        }

        // don't start listeners if no provider is enabled
        if (!network_enabled)
            return;
        Toast.makeText(getActivity(), "is ok ", Toast.LENGTH_SHORT).show();
        location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        // 位置监听器
        locationListener = new LocationListener() {

                 // 当位置改变时触发
                 @Override
                 public void onLocationChanged(Location location) {
                     Toast.makeText(
                             getActivity(),
                             "onLocationChanged", Toast.LENGTH_SHORT).show();
                     updateLocation(location);
                 }

                 // Provider失效时触发
                 @Override
                 public void onProviderDisabled(String arg0) {

                 }

                 // Provider可用时触发
                 @Override
                 public void onProviderEnabled(String arg0) {
                 }

                 // Provider状态改变时触发
                 @Override
                 public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
                 }
         };

         // 500毫秒更新一次，忽略位置变化
         locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, locationListener);
    }
    
    
    
    private void updateLocation(Location paramLocation) {
        if (paramLocation != null) {
            // 得到地理位置
            String qLocation = Double.toString(paramLocation.getLatitude()) + "," + Double.toString(paramLocation.getLongitude());
            // 获取天气和城市名
            new RetrieveWeatherTask().execute(qLocation);
            // 删除location监听
            locationManager.removeUpdates(locationListener);
        } else {
            Toast.makeText(
                    getActivity(),
                    "cannot get location  ", Toast.LENGTH_SHORT).show();
        }
    }
    
    
    private String getSystemDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(new Date());
    }
    
    // 异步任务，获取天气和地区信息
    class RetrieveWeatherTask extends AsyncTask<String, Void, WeatherAndLocation> {

        public static final boolean LOGD = true;

        protected WeatherAndLocation doInBackground(String... locations) {
            WeatherAndLocation wal = new WeatherAndLocation();
            
            //get weather
            LocalWeather lw = new LocalWeather(true);
            String query = (lw.new Params(Constant.FREE_API_KEY)).setQ(locations[0]).setDate(getSystemDate()).getQueryString(LocalWeather.Params.class);
            Weather weatherData = lw.callAPI(query);
            
            //get location
            LocationSearch ls = new LocationSearch(true);
            query = (ls.new Params(Constant.FREE_API_KEY)).setQuery(locations[0]).getQueryString(LocationSearch.Params.class);
            LocationData locationData = ls.callAPI(query);
            
            wal.setWeatherData(weatherData);
            wal.setLocationData(locationData);
            
            return wal;
        }
        
        protected void onPostExecute(WeatherAndLocation wal) {
            WeatherDataSource dataSource = new WeatherDataSource(getActivity());
            dataSource.open();
            dataSource.addWeatherData(wal);
            dataSource.close();
        }

     
    }
    

}
