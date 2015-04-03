package com.simple.wildfishingnote.weather.schdule;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.PowerManager;

import com.simple.wildfishingnote.utils.LogUtil;
import com.simple.wildfishingnote.weather.service.SaveWeatherAndLocationService;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    LocationManager locationManager;
    Location location;
    LocationListener locationListener;
    boolean network_enabled = false;
    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        LogUtil.appendLog("AlarmManagerBroadcastReceiver -> onReceive");
        
        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        // Acquire the lock
        wl.acquire();
        mContext = context;
        Intent i = new Intent(context, SaveWeatherAndLocationService.class);
        context.startService(i);

        // Release the lock
        wl.release();

        // CancelAlarm(context);

    }

    public void setAlarm(Context context) {
        // Calendar calendar = Calendar.getInstance();
        //
        // calendar.set(Calendar.HOUR_OF_DAY, 22); // For 20 AM
        // calendar.set(Calendar.MINUTE, 0);
        // calendar.set(Calendar.SECOND, 0);
        //
        // AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        // PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        // // per day 1 AM
        // am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
        // AlarmManager.INTERVAL_DAY, pi);

        // for debug
        LogUtil.appendLog("AlarmManagerBroadcastReceiver -> setAlarm");
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 1, intent, 0);
        // After after 30 seconds
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 15, pi);
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
