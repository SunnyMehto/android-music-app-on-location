package com.sunapp.sunnym.startupapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sunapp.sunnym.startupapp.Common.Constants;
import com.sunapp.sunnym.startupapp.Model.LocationInfo;
import com.sunapp.sunnym.startupapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragAdd_location.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragAdd_location#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragAdd_location extends Fragment implements LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragAdd_location.
     */
    // TODO: Rename and change types and number of parameters
    public static FragAdd_location newInstance(String param1, String param2) {
        FragAdd_location fragment = new FragAdd_location();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragAdd_location() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    TextView txtLocationName,txtLocationCoordinate;
    Button btnGetLocation,btnAddLocation;
    View view;
    LocationManager locationManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_frag_add_location, container, false);

        txtLocationName = (TextView)view.findViewById(R.id.txtLocationName);
        //txtUserName.setText("Sunny");
        txtLocationCoordinate = (TextView)view.findViewById(R.id.txtLocationCoordinate);
        //txtPassword.setText("Mehto");
        btnGetLocation    = (Button)view.findViewById(R.id.btnGetLocation);
        btnAddLocation    = (Button)view.findViewById(R.id.btnAddLocation);

        locationManager = (LocationManager) Constants.CurrentActivity.getSystemService(Context.LOCATION_SERVICE);

        try {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    3000,   // 3 sec
                    10, this);
        }
        catch(SecurityException ex) {
            Toast.makeText(Constants.CurrentActivity.getBaseContext(), "Kindly Enable GPS Location.", Toast.LENGTH_LONG).show();
        }


        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtLocationCoordinate.setText(str);
            }
        });

        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    SetValue();
                    resetView();
                }
            }
        });

        return view;
    }
    private void resetView()
    {
        txtLocationName.setText("");
        txtLocationCoordinate.setText("");
    }

    private void SetValue()
    {
        LocationInfo objLocationInfo = new LocationInfo();
        objLocationInfo.setLocationName(txtLocationName.getText().toString());
        objLocationInfo.setLatitude(String.valueOf(dLatitude));
        objLocationInfo.setLongitude(String.valueOf(dLongitude));

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("LocationName", objLocationInfo.getLocationName());
            jsonObject.put("Longitude", objLocationInfo.getLongitude());
            jsonObject.put("Latitude", objLocationInfo.getLatitude());

            writeStringAsFile(jsonObject.toString(), Constants.FILE_NAME);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void writeStringAsFile(final String fileContents, String fileName) {
        if(isExternalStorageWritable() ) {
            Context context = Constants.CurrentActivity.getApplicationContext();
            try {
                File file = new File(context.getFilesDir(),fileName);
                if(file.exists())
                {
                    FileWriter logWriter = new FileWriter(file,true);
                    BufferedWriter out = new BufferedWriter(logWriter);
                    out.write(fileContents);////<---HERE IS THE CHANGE
                    out.newLine();
                    out.close();
                }
                else
                {
                    FileWriter out = new FileWriter(new File(context.getFilesDir(), fileName));
                    out.write(fileContents);
                    out.close();
                }
            } catch (IOException e) {

            }
        }
    }

    private boolean validate()
    {
        boolean retVal = true;
        if(txtLocationName.getText() == "")
        {
            retVal = false;
            Toast.makeText(Constants.CurrentActivity.getBaseContext(), "Enter Location Name.", Toast.LENGTH_SHORT).show();
        }
        else if(str == "")
        {
            retVal = false;
            Toast.makeText(Constants.CurrentActivity.getBaseContext(), "Wait... let the location detect.", Toast.LENGTH_SHORT).show();
        }

        return retVal;
    }

    Double dLongitude = 0.0;
    Double dLatitude = 0.0;
    String str = "";
    /************* Called after each 3 sec **********/
    @Override
    public void onLocationChanged(Location location) {

        str = "Latitude: "+location.getLatitude()+"|"+
              "Longitude: "+location.getLongitude();

        dLongitude = location.getLongitude();
        dLatitude = location.getLatitude();

        Toast.makeText(Constants.CurrentActivity.getBaseContext(), str, Toast.LENGTH_LONG).show();
        txtLocationCoordinate.setText(str);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderDisabled(String provider) {

        /******** Called when User off Gps *********/

        Toast.makeText(Constants.CurrentActivity.getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

        /******** Called when User on Gps  *********/

        Toast.makeText(Constants.CurrentActivity.getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
