package com.wishsoft.nextstopmetrolink3;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncAlert2 extends AsyncTask<URL, Integer, Long>{

    String CONSUMER_KEY = "pyabDlJ9xfY7XJhJkPtkcQ";
    String CONSUMER_SECRET = "h7FlK7AMyyXkYBvtAL4w9I9rC6Er6kZ1iXgFQPmWTw";
    String accessToken = "269448541-DeTXN0wxlzEeKfewu9PT1JnDsGT52cmwGYrIOX4Q";
    String tokenSecret = "v6H7Vt0dJG99h4B5p5XatGEwTqN9VbNTf3t1PNcavOA";
    
	@Override
     protected Long doInBackground(URL... arg0) {
         
         long totalSize = 0;
         
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
			
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			String yesterday = getYesterdayDateString();
			//Log.d("Date", "Yesterday's date = "+ cal.getTime());
			
			    Query query = new Query("from:metrolink -filter:retweets -exclude_replies:true");
			    query.setSince(yesterday);
			    query.setCount(5);
			    
			    //QueryResult result;
			    //do {
			    	
			    	Paging paging = new Paging(1, 20);
			    	List<twitter4j.Status> tweets = twitter.getUserTimeline("metrolink");
			        //result = twitter.search(query);
			        //https://api.twitter.com/1.1/statuses/user_timeline.json?count=35&screen_name=
			        //Metrolink&exclude_replies=1
			        //List<twitter4j.Status> tweets = result.getTweets();
			        int count=0;
			        for (twitter4j.Status tweet : tweets) {
			        	if(count++>20)
			        		break;
			            //Log.d("Tweet " + count,"@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
			            //tweet.getCreatedAt();
			        }
			   // } while ((query = result.nextQuery()) != null);
			    
			} catch (TwitterException te) {
			    te.printStackTrace();
			    //Log.d("Tweet", "Failed to search tweets: " + te.getMessage());
			   
}

 		
         return totalSize;
     }
	private String getYesterdayDateString() {
		//2011-01-01
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);    
        return dateFormat.format(cal.getTime());
}
     
	 protected void onProgressUpdate(Integer... progress) {
        // setProgressPercent(progress[0]);
     }
	
     protected void onPostExecute(Long result) {
       
    	 
     }

	 
	 
	 
}
