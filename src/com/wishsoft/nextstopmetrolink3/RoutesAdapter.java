package com.wishsoft.nextstopmetrolink3;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



public class RoutesAdapter extends ArrayAdapter<RouteDetails>{

	Context context; 
    int layoutResourceId;    
    RouteDetails data[] = null;
    
    public RoutesAdapter(Context context, int layoutResourceId, RouteDetails[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RouteHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new RouteHolder();
            holder.routeName = (TextView)row.findViewById(R.id.routeName);
            
            row.setTag(holder);
        }
        else
        {
            holder = (RouteHolder)row.getTag();
        }
        
        RouteDetails routeDetails = data[position];
        holder.routeName.setText(routeDetails.routeName);
        holder.routeName.setBackgroundColor(Color.parseColor(routeDetails.color));
        
        
        return row;
    }
    
    static class RouteHolder
    {
        //TextView color;
        TextView routeName;
    }
    
}
