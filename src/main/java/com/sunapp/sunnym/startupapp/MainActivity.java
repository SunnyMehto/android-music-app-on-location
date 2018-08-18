package com.sunapp.sunnym.startupapp;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sunapp.sunnym.startupapp.Common.Constants;
import com.sunapp.sunnym.startupapp.Fragments.FragAdd_location;
import com.sunapp.sunnym.startupapp.Fragments.FragLocationList;
import com.sunapp.sunnym.startupapp.Fragments.FragWelcome;
import com.sunapp.sunnym.startupapp.Model.LocationInfo;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity  implements LocationListener,FragWelcome.OnFragmentInteractionListener,
        FragAdd_location.OnFragmentInteractionListener,
        FragLocationList.OnFragmentInteractionListener{
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Constants.CurrentActivity = this;
        Constants.ReplaceFragment(new FragWelcome(),"FRAG_WELCOME");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* locationManager = (LocationManager) Constants.CurrentActivity.getSystemService(Context.LOCATION_SERVICE);

        try {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    3000,   // 3 sec
                    10, this);
        }
        catch(SecurityException ex) {
            Toast.makeText(Constants.CurrentActivity.getBaseContext(), "Kindly Enable GPS Location.", Toast.LENGTH_LONG).show();
        }*/

                /* CAL METHOD requestLocationUpdates */

        // Parameters :
        //   First(provider)    :  the name of the provider with which to register
        //   Second(minTime)    :  the minimum time interval for notifications,
        //                         in milliseconds. This field is only used as a hint
        //                         to conserve power, and actual time between location
        //                         updates may be greater or lesser than this value.
        //   Third(minDistance) :  the minimum distance interval for notifications, in meters
        //   Fourth(listener)   :  a {#link LocationListener} whose onLocationChanged(Location)
        //                         method will be called for each location update

        /*try {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    3000,   // 3 sec
                    10, this);
        }
        catch(SecurityException ex) {
            Toast.makeText(getBaseContext(), "Kindly Enable GPS Location.", Toast.LENGTH_LONG).show();
        }*/
        /********* After registration onLocationChanged method  ********/
        /********* called periodically after each 3 sec ***********/
    }

    /************* Called after each 3 sec **********/
    @Override
    public void onLocationChanged(Location location) {
        Constants.CurrentLocation = location;
        String str = "Latitude: "+location.getLatitude()+"|"+
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_location) {
            Constants.ReplaceFragment(new FragAdd_location(),"FRAG_LOCATION_ADD");
        }
        else if (id == R.id.action_location_list) {
            Constants.ReplaceFragment(new FragLocationList(),"FRAG_LOCATION_LIST");
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
