package com.sunapp.sunnym.startupapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sunapp.sunnym.startupapp.Model.LocationInfo;
import com.sunapp.sunnym.startupapp.R;

import java.util.ArrayList;

/**
 * Created by SunnyM on 12/29/2015.
 */
public class LocationListAdapter extends BaseAdapter {
    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList<LocationInfo> data;
    private static LayoutInflater inflater=null;
    public Resources res;
    private LocationInfo objLocationInfo;

    /*************  CustomAdapter Constructor *****************/
    public LocationListAdapter(Activity a, ArrayList<LocationInfo> d) {

        /********** Take passed values **********/
        activity = a;
        data=d;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView txtLocationName;
        public TextView txtLocationCoordinate;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.locationlistitem, null);
            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder                          = new ViewHolder();
            holder.txtLocationName          =(TextView)vi.findViewById(R.id.txtiLocationName);
            holder.txtLocationCoordinate    = (TextView) vi.findViewById(R.id.txtiLocationCoordinate);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.txtLocationName.setText("No Data");
            holder.txtLocationCoordinate.setText("");
        }
        else
        {
            objLocationInfo = ( LocationInfo ) data.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.txtLocationName.setText(objLocationInfo.getLocationName());
            String sLocationText = "Latitude : " + objLocationInfo.getLatitude() + " , " +
                                    "Longitude : " + objLocationInfo.getLongitude();
            holder.txtLocationCoordinate.setText(sLocationText);
        }
        return vi;
    }
}
