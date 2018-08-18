package com.sunapp.sunnym.startupapp;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.sunapp.sunnym.startupapp.Common.Constants;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by SunnyM on 12/29/2015.
 */
public class StartUpService extends Service implements LocationListener {

    public StartUpService() {

    }
    /************* Called after each 3 sec **********/
    @Override
    public void onLocationChanged(Location location) {
        Constants.CurrentLocation = location;
        String str  = "Latitude: "+location.getLatitude()+"|"+
                    "Longitude: "+location.getLongitude();

        Toast.makeText(getBaseContext(), str, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {

        /******** Called when User off Gps *********/

        Toast.makeText(getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

        /******** Called when User on Gps  *********/

        Toast.makeText(getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        startTimer();
        locationManager = (LocationManager) Constants.CurrentActivity.getSystemService(Context.LOCATION_SERVICE);

        try {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    3000,   // 3 sec
                    10, this);
        }
        catch(SecurityException ex) {
            Toast.makeText(Constants.CurrentActivity.getBaseContext(), "Kindly Enable GPS Location.", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();
    }

    @Override

    public void onDestroy() {
        stoptimertask();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

    }

    Timer timer;
    TimerTask timerTask;

    final Handler handler = new Handler();

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 1000, 4000); //
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    private LocationManager locationManager;
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        if(Constants.CurrentLocation != null)
                        {
                            for(int i=0;i<Constants.locationInfo.size();i++) {
                                String sLongitude    = Constants.locationInfo.get(i).getLongitude();
                                String sLatitude    = Constants.locationInfo.get(i).getLatitude();

                                Location location = new Location("");
                                location.setLatitude(Double.valueOf(sLatitude));
                                location.setLongitude(Double.valueOf(sLongitude));
                                if(Constants.CurrentLocation.distanceTo(location) < 30 ) {
                                    if (!isAppRunning(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH)) {
                                        try {
                                            Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            stoptimertask();
                                        }
                                        catch(Exception ex)
                                        {
                                            Toast.makeText(Constants.CurrentActivity, ex.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }

                        }
                    }
                });
            }
        };
    }

    private boolean isAppRunning(String packageName) {
        ActivityManager manager = (ActivityManager)Constants.CurrentActivity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (packageName.equals(service.service.getPackageName())) {
                return true;
            }
        }
        return false;
    }

}

