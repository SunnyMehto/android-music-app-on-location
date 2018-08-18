package com.sunapp.sunnym.startupapp.Fragments;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.sunapp.sunnym.startupapp.Common.Constants;
import com.sunapp.sunnym.startupapp.R;
import com.sunapp.sunnym.startupapp.StartUpService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragWelcome.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragWelcome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragWelcome extends Fragment {
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
     * @return A new instance of fragment FragWelcome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragWelcome newInstance(String param1, String param2) {
        FragWelcome fragment = new FragWelcome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragWelcome() {
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
    private View view;
    Button btnStartService;
    Button btnStopService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_frag_welcome, container, false);

        btnStartService    = (Button)view.findViewById(R.id.btnStartService);
        btnStopService    = (Button)view.findViewById(R.id.btnStopService);


        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             try {
                 if (!isMyServiceRunning(StartUpService.class)) {
                     Constants.CurrentActivity.startService(new Intent(Constants.CurrentActivity, StartUpService.class));
                 } else {
                     Toast.makeText(Constants.CurrentActivity.getBaseContext(), "App Service already started.", Toast.LENGTH_LONG).show();
                 }
                 //startTimer();
             }catch (Exception ex)
             {
                 Toast.makeText(Constants.CurrentActivity.getBaseContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
             }
            }
        });
        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.CurrentActivity.stopService(new Intent(Constants.CurrentActivity, StartUpService.class));
            }
        });
        Constants.readFileAsString();
        return view;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager)Constants.CurrentActivity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    Timer timer;
    TimerTask timerTask;

    final Handler handler = new Handler();

    public void startTimer() {
/*        timer = new Timer();
        initializeTimerTask();
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 1000, 4000); //*/
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
                        Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
                        startActivity(intent);
                    }
                }
            }

        }

    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
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
