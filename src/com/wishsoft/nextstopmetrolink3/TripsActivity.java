package com.wishsoft.nextstopmetrolink3;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class TripsActivity extends Activity implements OnItemClickListener, Preference.OnPreferenceClickListener
{
	
	private NextStopObject objectPassedIn;	//used to pass in data from previous activity
	private AppData appData;
	private ListView listView1;
	private LinkedHashMap<String, String> possibleStops;
	//private Map<String, String> ridesInDict;
	//private Button chooseDay;
	private String m_RouteID;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	   

	    
	    //ridesInDict = new Hashtable<String, String>();
	    setContentView(R.layout.tripsforroute_true);
	    appData = (AppData)this.getApplicationContext();
	    Bundle b = getIntent().getExtras();
	    objectPassedIn = b.getParcelable("data");
	    /*LinkedHashMap<String, String>*/ possibleStops = objectPassedIn.Trains();//new HashMap<String,String>();
	    m_RouteID = objectPassedIn.getRouteID();
	    setTitle(m_RouteID);

	     setListData();
	}
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	      MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.menu.choosedaymenu, menu);
	     
	      return super.onCreateOptionsMenu(menu);
	    }
	 
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	//Log.d("TripsActivity", "OnOptions");
	        switch (item.getItemId()) {
	        case R.id.calendar:
	        	//startActivityForResult(new Intent(Intent.ACTION_PICK).setDataAndType(null, CalendarActivity.MIME_TYPE), 100);
	             Intent calendarIntent = new Intent(this, CalendarDays.class);
	             calendarIntent.putExtra("day", 0);
	             startActivityForResult(calendarIntent, 0);
	                return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_OK) {
			int day2 = data.getIntExtra("day", 0);
			//Log.d("TripsActivity", "Day is " + day2);
			possibleStops.clear();
			String stopID = appData.getStopNameAndStopId().get(m_RouteID);
			possibleStops = appData.getDB().getStopsForStopId(stopID, false,day2);
			setListData();
		}
	}
	
	public boolean onPreferenceClick(Preference  preference) {
//		String key = preference.getKey();
//		if(key.equals("pick")) {
//			//Intent myIntent = new Intent(this, RouteStopsActivity.class);
//			//startActivityForResult(new Intent(Intent.ACTION_PICK).setDataAndType(null, CalendarActivity.MIME_TYPE), 100);
//		} else if(key.equals("about")) {
//			new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.app_name).setMessage("http://code.google.com/p/android-calendar-view/\n\nBy Chris Gao<chris@exina.net>").create().show();
//		}
		return true;
	}
	
//	private static class TripDetailsStaticClass
//	{
//		String title;
//		String description;
//		boolean image;
//	};
//	@SuppressWarnings("unused")
//	private TripDetailsStaticClass getTrip(String shortName)
//	{
//		TripDetailsStaticClass obj = new TripDetailsStaticClass();
//		
//		//08-03 13:38:11.383: D/TripsActivity(2761): 294200134 time: 07:36:00
//		Log.d("TripsActivity", shortName + " time: " + ridesInDict.get(shortName));
//		String titleHeading = appData.getDB().getLastStopFromShortName(shortName);
//		String time = ridesInDict.get(shortName);
//		String headingTo = titleHeading;
//		String trainNum = shortName;
//		String startingFrom = appData.getDB().getFirstStopForTripShortName(shortName);
//		String arrival_time = appData.getDB().getArrivalTimeForTrainNum(shortName);
//		
//		String ID = appData.getShortToRouteId().get(shortName);
//
//		String title = ID + " " + trainNum + " @" + time;
//		String description = "Headed to " +  headingTo +" from " + startingFrom + " scheduled to arrive in " + headingTo + " at " + arrival_time;
//		boolean showImage = true;
//		 if(appData.containsBikeCar(trainNum))
//		       showImage=true;
//		    else
//		        showImage=false;
//		 
//		 obj.title = title;
//		 obj.description = description;
//		 obj.image=showImage;
//		 return obj;
//	}
//	
	
	private void setListData()
	{
		//Log.d("TripsActivity 2", objectPassedIn.StopID());
		AppData app = (AppData)this.getApplicationContext();
		int size = possibleStops.size();//ridesInDict.size();//objectPassedIn.Trains().size();
		if(size==0)
			size=1;
		TripDetails trip_list[] = new TripDetails[size];
		listView1 = (ListView)findViewById(R.id.listView14);
		TextView tx = (TextView)findViewById(R.id.info);
		tx.setText("**All** stops "+objectPassedIn.getRouteID());
		int i = 0;
		if(possibleStops.size()>0)
		{
		for(Entry<String, String> entry : possibleStops.entrySet()){// ridesInDict.entrySet()){
            
        	String key = entry.getKey();
        	String[] splitVal = entry.getValue().split(",");
        	String shortName =  splitVal[0];//key;

        	String titleHeading = appData.getDB().getLastStopFromShortName(shortName);
			String time=null;
			time = app.getTwelveHourClockForTwentyFourHour(splitVal[1]);//splitVal[1]);//ridesInDict.get(shortName);
			String headingTo = titleHeading;
			String trainNum = shortName;
			String startingFrom = appData.getDB().getFirstStopForTripShortName(shortName);
			String arrival_time = app.getTwelveHourClockForTwentyFourHour(appData.getDB().getArrivalTimeForTrainNum(shortName));
			
			String ID = appData.getShortToRouteId().get(shortName);
   
			String title = ID + " " + trainNum + " @" + time;
			String description = "Headed to " +  headingTo +" from " + startingFrom + " scheduled to arrive in " + headingTo + " at " + arrival_time;
			boolean showImage = true;
			 if(appData.containsBikeCar(trainNum))
			       showImage=true;
			    else
			        showImage=false;
			 
			trip_list[i] = new TripDetails(title, description, showImage, trainNum);//Integer.parseInt(trainNum));
			i++;
			
		}
		}
		else
		{
			//add a message...
			//ridesInDict.put("No more trains today", "Please select a different station");
			trip_list[i] = new TripDetails("No more trains today", "Please select a different station", false, "");
		}
		TripDetailsAdapter adapter = new TripDetailsAdapter(this, R.layout.stopdetail, trip_list);
		listView1.setAdapter(adapter);
		 listView1.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position,
	                    long id) {
	            	onListItemClick(parent, view, position, id);

	            }
	        });
	        
		
		
	}
	 protected void onListItemClick(AdapterView<?> parent, View view, int position, long id) 
	 {
		 
		 
		TripDetails rd = (TripDetails)listView1.getItemAtPosition(position);
		AppData app = (AppData)this.getApplicationContext();
		
		//int stopID = rd.routeName;
		if(rd.tripTitle =="No more trains today")
		{
			return;
		}
		Intent myIntent = new Intent(this, RouteStopsActivity.class);
		NextStopObject data = new NextStopObject();
		data.SetNote("");
		data.SetStopID(rd.routeName);//String.valueOf(stopID));
		myIntent.putExtra("data", data);
		startActivity(myIntent);
	 }
	 
    
	public NextStopObject getObjectPassedIn() {
		return objectPassedIn;
	}
	public void setObjectPassedIn(NextStopObject objectPassedIn) {
		this.objectPassedIn = objectPassedIn;
	}


//	public Button getChooseDay() {
//		return chooseDay;
//	}
//
//
//	public void setChooseDay(Button chooseDay) {
//		this.chooseDay = chooseDay;
//	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
}
