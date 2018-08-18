package com.sunapp.sunnym.startupapp.Common;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Location;
import android.os.Environment;
import android.widget.Toast;

import com.sunapp.sunnym.startupapp.Model.LocationInfo;
import com.sunapp.sunnym.startupapp.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Constants {
    public static Location CurrentLocation;
    public static String FILE_NAME = "StartUpData.txt";
    public static Activity CurrentActivity;
    public static ArrayList<LocationInfo> locationInfo = new ArrayList<LocationInfo>();
    public static void ReplaceFragment(Fragment fragment,String sTag)
    {
        try {
            FragmentManager manager             = Constants.CurrentActivity.getFragmentManager();
            FragmentTransaction transaction     = manager.beginTransaction();
            transaction.replace(R.id.frameMain, fragment, sTag); // newInstance() is a static factory method.
            transaction.addToBackStack(fragment.getTag());
            transaction.commit();
        }
        catch   (Exception e)
        {
            Toast.makeText(Constants.CurrentActivity, e.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    public static String readFileAsString() {
        Constants.locationInfo.clear();
        StringBuilder stringBuilder = new StringBuilder();
        if(isExternalStorageReadable()) {
            Context context = Constants.CurrentActivity.getApplicationContext();
            String line;
            BufferedReader in = null;

            try {
                //ArrayList<EntryInfo> entryInfo = new ArrayList<EntryInfo>();
                in = new BufferedReader(new FileReader(new File(context.getFilesDir(), Constants.FILE_NAME)));
                while ((line = in.readLine()) != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(line);
                        LocationInfo obj1 = new LocationInfo();
                        obj1.setLocationName(jsonObj.getString("LocationName"));
                        obj1.setLongitude(jsonObj.getString("Longitude"));
                        obj1.setLatitude(jsonObj.getString("Latitude"));
                        Constants.locationInfo.add(obj1);
                    } catch (Exception e) {
                    }
                }
                //    stringBuilder.append(line);

            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }
        return stringBuilder.toString();
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}