package com.wishsoft.nextstopmetrolink3;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TripDetailsAdapter extends ArrayAdapter<TripDetails>{

	Context context; 
    int layoutResourceId;    
    TripDetails data[] = null;

	public TripDetailsAdapter(Context context, int resource, TripDetails[] data) {
		//super(context, resource);
		 super(context, resource, data);
		this.context = context;
		this.layoutResourceId=resource;
		this.data=data;
	}
	
	  @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
		  //Log.d("TripDetailsAdapter", "Caled");
		  	View row = convertView;
	        TripHolder holder = null;
	        
	        if(row == null)
	        {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            
	            row = inflater.inflate(layoutResourceId, parent, false);
	            
	            holder = new TripHolder();
	            holder.title = (TextView)row.findViewById(R.id.tvTitle);
	            holder.description = (TextView)row.findViewById(R.id.tvDetails);
	            
	            row.setTag(holder);
	            
	        }
	        else
	        {
	            holder = (TripHolder)row.getTag();
	        }
	        
	        TripDetails stationDetails = data[position];
	        //Log.d("TripDetailsAdapter", "Setting details...");
	        holder.title.setText(stationDetails.tripTitle);//R.string.title);//stationDetails.tripTitle);
	        holder.description.setText(stationDetails.tripDetails);//R.string.description);//(stationDetails.tripDetails);
	        
	        ImageView bikeImage = (ImageView)row.findViewById(R.id.bikeimage);
	        bikeImage.setVisibility(View.VISIBLE);
	        if(!stationDetails.bikeCar)
	        {	
	        	bikeImage.setVisibility(View.GONE);
	        }
	        return row;
	    }
	    
	    static class TripHolder
	    {
	        TextView title;
	        TextView description;
	        boolean bikeCar;
	        
	    }
	    
	    

}
