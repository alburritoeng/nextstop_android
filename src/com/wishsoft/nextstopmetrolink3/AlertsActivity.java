package com.wishsoft.nextstopmetrolink3;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class AlertsActivity extends BaseActivity implements OnClickListener{

	 // Instance Variables
    private AlertsActivity twitterActivity = null;
    private ListView twitterListView = null;
    //Vector<TwitterResultsClass> twitter_list;
    //TwitterResultsClass twitter_list[] = null;
    List<TwitterResultsClass> twitter_list = null;
    
    @Override
	protected void onCreate(Bundle savedInstanceState)  
	{
		super.onCreate(savedInstanceState);
		// we're going to need this later by the other threads
		twitterActivity = this;
		setContentView(R.layout.twitterlist);
		twitterListView = (ListView)findViewById(R.id.twitterlistview);
		Button refreshBtn = (Button)findViewById(R.id.refresh);
		refreshBtn.setOnClickListener(this);
		
		updateAdapter();

	}
	
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    
	public void updateAdapter()
	{
		if(!haveNetworkConnection())
		{
			TwitterResultsClass[] t = new TwitterResultsClass[1];
			t[0] = new TwitterResultsClass();
        	t[0].time = "No Internet Connection found";
        	t[0].tweet = "Must be connected to internet in order to retrieve Alerts";
        	TwitterAdapter adapter = new TwitterAdapter(twitterActivity, R.layout.twitter, t);
	       	 twitterListView.setAdapter(adapter);
	       	 twitterListView.setDividerHeight(1);
	       	 twitterListView.invalidate();
		}
		else
		{
			TwitterResultsClass[] t = new TwitterResultsClass[1];
			t[0] = new TwitterResultsClass();
        	t[0].time = "Loading...";
        	t[0].tweet = "Retrieving Metrolink alerts";
        	TwitterAdapter adapter = new TwitterAdapter(twitterActivity, R.layout.twitter, t);
	       	 twitterListView.setAdapter(adapter);
	       	 twitterListView.setDividerHeight(1);
	       	 twitterListView.invalidate();
	     	AsyncAlert n = new AsyncAlert();
			try
			{	
				n.execute();//fake URL...never used...
			}
			catch(Exception e)
			{
				//Log.d("Alerts..", e.getMessage());
			}
		}
	
	}
	
	private class AsyncAlert extends AsyncTask<URL, Integer, Long>{

		//Vector<TwitterResultsClass> twitter_list;
		@Override
	     protected Long doInBackground(URL... arg0) {
	         
	         long totalSize = 0;
	         //twitter_list= new Vector<TwitterResultsClass>();
			 	final String TWITTER_CONSUMER_KEY = "pyabDlJ9xfY7XJhJkPtkcQ";
				final String TWITTER_SECRET_KEY = "h7FlK7AMyyXkYBvtAL4w9I9rC6Er6kZ1iXgFQPmWTw";
				final String TWITTER_ACCESS_TOKEN = "269448541-DeTXN0wxlzEeKfewu9PT1JnDsGT52cmwGYrIOX4Q";
				final String TWITTER_ACCESS_TOKEN_SECRET = "v6H7Vt0dJG99h4B5p5XatGEwTqN9VbNTf3t1PNcavOA";
				try {
				ConfigurationBuilder cb = new ConfigurationBuilder();
				cb.setDebugEnabled(true)
				    .setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
				    .setOAuthConsumerSecret(TWITTER_SECRET_KEY)
				    .setOAuthAccessToken(TWITTER_ACCESS_TOKEN)
				    .setOAuthAccessTokenSecret(TWITTER_ACCESS_TOKEN_SECRET);
				TwitterFactory tf = new TwitterFactory(cb.build());
				Twitter twitter = tf.getInstance();

				
		    	List<twitter4j.Status> tweets = twitter.getUserTimeline("metrolink");
		    	//twitter_list = new TwitterResultsClass[tweets.size()];
		    	twitter_list = new ArrayList<TwitterResultsClass>();
			    //Log.d("Tweets count = ", Integer.toString(tweets.size()) );
			        int count=0;
			        for (twitter4j.Status tweet : tweets) {
			        	if(tweet.getText().contains(("@")))
			        		continue;
			        			
			        	if(count>20)
			        		break;
			        	TwitterResultsClass t = new TwitterResultsClass();
//			        	String originalString = "2010-07-14 09:00:02";
//			        	Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(originalString);
//			        	String newString = new SimpleDateFormat("H:mm").format(date); // 9:00
			        	//Fri Sept 20 22:58:06 PDT 2013
			        	//"E M d HH::mm::ss z y"
			        	 SimpleDateFormat dt = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
			             try {
							Date date = dt.parse(tweet.getCreatedAt().toString());
							String newTimeFormat = new SimpleDateFormat("EEE MMM d hh:mm:ss a").format(date);
							t.time = newTimeFormat;//"E M d HH::mm::ss z y";//date.toString();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							t.time = tweet.getCreatedAt().toString();
						}
			        	//t.time = tweet.getCreatedAt().toString();
			        	t.tweet = tweet.getText();
			        	twitter_list.add(t);
			        	//twitter_list[count] = t;
			        	count++;   
			        }
			 
				} catch (TwitterException te) {
				    te.printStackTrace();
				    //Log.d("Tweet", "Failed to search tweets: " + te.getMessage());
				   
	}

	 return totalSize;
	}

	 protected void onProgressUpdate(Integer... progress) {
        
     }
	
     protected void onPostExecute(Long result) {
    	 //Log.d("Twitter", "OnPostExecute");
    	 TwitterResultsClass list[] = null;
    	 if(twitter_list==null)
    		 return;
    	 int len = twitter_list.size();
    	 if(len==0)
    		 return;
    	 list = new TwitterResultsClass[len];
    	 int i =0;
    	 for(TwitterResultsClass t : twitter_list)
    	 {
    		list[i] = t;
    		i++;
    	 }
    	 TwitterAdapter adapter = new TwitterAdapter(twitterActivity, R.layout.twitter, list);
    	 twitterListView.setAdapter(adapter);
    	 twitterListView.setDividerHeight(1);
    	 twitterListView.invalidate();
    	 
     }
     

	 }

	@Override
	public void onClick(View v) {
		
		twitterListView.setAdapter(null);	
		updateAdapter();
		
	}

	
}
