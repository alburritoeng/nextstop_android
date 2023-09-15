package com.wishsoft.nextstopmetrolink3;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StationAdapter  extends ArrayAdapter<StationDetails>{

	Context context; 
    int layoutResourceId;    
    StationDetails data[] = null;
    
    public StationAdapter(Context context, int layoutResourceId, StationDetails[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        StationHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new StationHolder();
            holder.stationName = (TextView)row.findViewById(R.id.stationname);
            
            row.setTag(holder);
        }
        else
        {
            holder = (StationHolder)row.getTag();
        }
        
        StationDetails stationDetails = data[position];
        holder.stationName.setText(stationDetails.stationName);
        //Log.d("StationDetailsAdapter", "Setting details...");
        
        
        return row;
    }
    
    static class StationHolder
    {
        TextView stationName;
    }
    
}

