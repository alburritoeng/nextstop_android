package com.wishsoft.nextstopmetrolink3;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RoutesStopsAdapter extends ArrayAdapter<RoutesStopsDetails>{

	Context context; 
    int layoutResourceId;    
    RoutesStopsDetails data[] = null;
    
    public RoutesStopsAdapter(Context context, int layoutResourceId, RoutesStopsDetails[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RouteHolder holder = null;
       // Log.d("RoutesStopAdapter","getView");
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new RouteHolder();
            holder.stopName = (TextView)row.findViewById(R.id.tvStopName);
            holder.stopTime = (TextView)row.findViewById(R.id.tvStopTime);
            //Log.d("RoutesStopAdapter", holder.stopName + "," + holder.stopTime);
            row.setTag(holder);
        }
        else
        {
            holder = (RouteHolder)row.getTag();
        }
        
        RoutesStopsDetails routeDetails = data[position];
        holder.stopName.setText(routeDetails.stopName);
        holder.stopTime.setText(routeDetails.stopTime);
        
        //Log.d("RoutesStopAdapter", holder.stopName + "," + holder.stopTime);
        return row;
    }
    
    static class RouteHolder
    {
        TextView stopTime;
        TextView stopName;
    }
}
