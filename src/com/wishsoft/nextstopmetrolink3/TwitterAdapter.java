package com.wishsoft.nextstopmetrolink3;



import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TwitterAdapter extends ArrayAdapter<TwitterResultsClass>{

	Context context; 
    int layoutResourceId;    
    TwitterResultsClass data[] = null;
    
    public TwitterAdapter(Context context, int layoutResourceId, TwitterResultsClass[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    
    @SuppressWarnings("deprecation")
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TweetHolder holder = null;
       // Log.d("RoutesStopAdapter","getView");
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new TweetHolder();
            holder.tweetTime= (TextView)row.findViewById(R.id.twittertime);
            holder.tweetText = (TextView)row.findViewById(R.id.tweet);
            //Log.d("RoutesStopAdapter", holder.stopName + "," + holder.stopTime);
            row.setTag(holder);
        }
        else
        {
            holder = (TweetHolder)row.getTag();
        }
        
        TwitterResultsClass routeDetails = data[position];
        holder.tweetTime.setText(routeDetails.time.toString());
        holder.tweetText.setText(routeDetails.tweet);
        
        //Log.d("RoutesStopAdapter", holder.stopName + "," + holder.stopTime);
        return row;
    }
    
    static class TweetHolder
    {
        TextView tweetTime;
        TextView tweetText;
    }
}

