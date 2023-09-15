package com.wishsoft.nextstopmetrolink3;

import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import android.app.Dialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NearLocationMapActivity extends  android.support.v4.app.FragmentActivity implements OnClickListener, android.location.LocationListener, OnMarkerClickListener, OnInfoWindowClickListener{

	private ListView listView1;
	private GoogleMap googleMap;
	private AppData app = null;
	private Button clickdistanceBtn;
	private Button pickdayBtn;
	private Location myLocation;
	private int distance = 11266;//6400;	//this is 6400 meters = ~4miles, 11265.4=7miles
	private LatLngBounds.Builder bc = new LatLngBounds.Builder();
	private HashSet<String> stopNames;
	private LinkedHashMap<String, String> possibleStops ;
	private Map<String, PointsLongLat> results;
	private Vector<Marker> markerArray;
	private String currentlySelectedStop;
	private int currentDay;
	
	 private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
     private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
	public Location getLocation() {
	    try {
	    	boolean canGetLocation;
	    	LocationManager locationManager;// = (LocationManager) getSystemService(LOCATION_SERVICE);

	        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

	        // getting GPS status
	        boolean isGPSEnabled = locationManager
	                .isProviderEnabled(LocationManager.GPS_PROVIDER);

	        // getting network status
	        boolean isNetworkEnabled = locationManager
	                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

	       // if (!isGPSEnabled && !isNetworkEnabled) {
	            // no network provider is enabled
	        //} else {
	        	canGetLocation = true;
	            if (isNetworkEnabled) {
	                locationManager.requestLocationUpdates(
	                        LocationManager.NETWORK_PROVIDER,
	                        MIN_TIME_BW_UPDATES,
	                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
	                Log.d("Network", "Network Enabled");
	                if (locationManager != null) {
	                	myLocation = locationManager
	                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	                    if (myLocation != null) {
	                        double latitude = myLocation.getLatitude();
	                        double longitude = myLocation.getLongitude();
	                    }
	                }
	            }
	            // if GPS Enabled get lat/long using GPS Services
	            if (isGPSEnabled) {
	                if (myLocation == null) {
	                    locationManager.requestLocationUpdates(
	                            LocationManager.GPS_PROVIDER,
	                            MIN_TIME_BW_UPDATES,
	                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
	                    Log.d("GPS", "GPS Enabled");
	                    if (locationManager != null) {
	                    	myLocation = locationManager
	                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
	                        if (myLocation != null) {
	                            double latitude = myLocation.getLatitude();
	                            double longitude = myLocation.getLongitude();
	                        }
	                    }
	                }
	            }
	        //}

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return myLocation;
	}
	
	
	private void addMyLocationOnMap()
	{ try
		 {
		 int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

		
	        // Showing status
	        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

	            int requestCode = 10;
	            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
	            dialog.show();

	        }else { // Google Play Services are available

	            // Getting reference to the SupportMapFragment of activity_main.xml
	            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

	            // Getting GoogleMap object from the fragment
	            googleMap = fm.getMap();

	            // Enabling MyLocation Layer of Google Map
	            googleMap.setMyLocationEnabled(true);

	            // Getting LocationManager object from System Service LOCATION_SERVICE
	            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

	            // Creating a criteria object to retrieve provider
	            Criteria criteria = new Criteria();

	            // Getting the name of the best provider
	            String provider = locationManager.getBestProvider(criteria, true);

	            // getting GPS status
		        boolean isGPSEnabled = locationManager
		                .isProviderEnabled(LocationManager.GPS_PROVIDER);

		        // getting network status
		        boolean isNetworkEnabled = locationManager
		                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		        if(!isGPSEnabled || !isNetworkEnabled )
		        {
		        	Toast.makeText(NearLocationMapActivity.this, "Please enable Location Services",
		  		    Toast.LENGTH_LONG).show();
		        	return;
		        }
	            // Getting Current Location
	            //Location location = locationManager.getLastKnownLocation(provider);
	            myLocation = getLocation();//locationManager.getLastKnownLocation(provider);
	            if(myLocation!=null){
	                onLocationChanged(myLocation);
	            }
	            
	           // locationManager.requestLocationUpdates(minTime, minDistance, criteria, intent)
	            locationManager.requestLocationUpdates(provider, 20000, 1000, this );
	            String name = "You";
	            //myLocation.setLatitude(34.1114);
	            //myLocation.setLongitude(-117.3825);
	            if(myLocation!=null)
	            {
	    		double longitude = myLocation.getLongitude();
	    		double latitude = myLocation.getLatitude();
	    		googleMap.addMarker(new MarkerOptions()
	    	 	                          .position(new LatLng(longitude, latitude))
	    	 	                          .title(name));
	            }
	    		
	        }
		 }
		 catch(Exception e)
		 {
			 Log.d("NearLoc", e.getMessage());
		 }
	}
	  @Override
	    public void onLocationChanged(Location location) {

	        //TextView tvLocation = (TextView) findViewById(R.id.tv_location);

	        // Getting latitude of the current location
	        double latitude = location.getLatitude();

	        // Getting longitude of the current location
	        double longitude = location.getLongitude();

	        // Creating a LatLng object for the current location
	        LatLng latLng = new LatLng(latitude, longitude);
	        bc.include(latLng);
	        // Showing the current location in Google Map
	        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
	        updateGoogleMapCamera();
	        // Zoom in the Google Map
	        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

	        // Setting latitude and longitude in the TextView tv_location
	        //tvLocation.setText("Latitude:" +  latitude  + ", Longitude:"+ longitude );

	    }

	 
	    public void onProviderDisabled(final String provider) {
	    }

	    public void onProviderEnabled(final String provider) {
	    }

	    public void onMyLocationButtonClick()
	    {
	    	
	    }
	    public void onStatusChanged(final String arg0, final int arg1, final Bundle arg2) {
	    	//Log.d("NearLocationMapActivity", "Staus Changed");
	    }
	    
	    private void addStationsNearMe(int dist)
	    {
	    	if(markerArray==null)
	    		markerArray=new Vector<Marker>();
	    	else
	    	{
	    		for(Marker m : markerArray)
	    		{
	    			m.remove();
	    		}
	    		markerArray.clear();
	    		bc = new LatLngBounds.Builder();
	    	}
	    	//int count=0;
//	    	Map<String, PointsLongLat> results = app.getDB().getStationsNearMe();
	    	if(results.size() >0)
	    		stopNames.clear();
	    	//myLocation
	    	//Log.d("NearLocationActivity", "MyLocation: " + myLocation.getLatitude() + "," + myLocation.getLongitude());
	    	for (Entry<String, PointsLongLat> entry : results.entrySet()) {
	    		
	    		//get the location
	    		//check if it is within X-Miles
	    		//add the marker if it is
	    		//update the map camera
	    		String name = entry.getKey();
	    		PointsLongLat pll = entry.getValue();
	    		
	    		Location loc = new Location(name);
	    		loc.setLatitude(pll.latitude);
	    		loc.setLongitude(pll.longitude);
	    		
	    		//boolean setCamera = false;
	    		float val=0;
	    		if(myLocation==null)
	    		{
	    			val = 0;
	    		}
	    		else
	    			val = myLocation.distanceTo(loc);
	    		stopNames.add(name);
	    		if(val <  dist || dist==0)
	    		{
	    			//count++;
	    			//Log.d("NearLocationMapActivity", name + " " + loc.getLatitude() + "," + loc.getLongitude()+ " is near by " + val);
	    			double longitude = loc.getLongitude();
		    		double latitude = loc.getLatitude();
		    		LatLng l = new LatLng(latitude, longitude);
		    		bc.include(l);
		    		//setCamera=true;
		    		Marker m = googleMap.addMarker(new MarkerOptions()
		    	 	                          .position(new LatLng(latitude, longitude))
		    	 	                          .title(name));
		    		markerArray.add(m);
	    		}
	    		
	    		
	    	}
	
	    	updateGoogleMapCamera();
	    	googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));	
	    }
	    
	    
	private void updateGoogleMapCamera()
	{
		 googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {

             @Override
             public void onCameraChange(CameraPosition arg0) {
                 // Move camera.
            	 
             	googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 10));
                 // Remove listener to prevent position reset on camera move.
             	//googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
             	googleMap.setOnCameraChangeListener(null);
             }
         });
		
	}

	
	private void selectStop(String stopName)
	{
		String selectedStop = app.getStopIDAndStopName().get(currentlySelectedStop);
		if(selectedStop.equals(stopName))
			return;
		possibleStops.clear();
		String stopID = app.getStopNameAndStopId().get(stopName);
		currentlySelectedStop = stopID;
		possibleStops = app.getDB().getStopsForStopId(stopID, false, currentDay);
		setListData(stopID);
	}
	private void selectFirstEntry()
	{
		if(stopNames.size()==0)
			return;
		Calendar today = Calendar.getInstance();
    	
    	currentDay = today.get(Calendar.DAY_OF_WEEK); 
    	setDayTextOnButton(currentDay);
		possibleStops.clear();
		String stopID = app.getStopID((String)stopNames.toArray()[0]);
		
		possibleStops = app.getDB().getStopsForStopId(stopID, false);
		currentlySelectedStop=stopID;
		setListData(stopID);
	}
	 private void CheckEnableGPS(){
		    String provider = Settings.Secure.getString(getContentResolver(),
		      Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		       if(!provider.equals("")){
		           //GPS Enabled
		        //Toast.makeText(AndroidEnableGPS.this, "GPS Enabled: " + provider,
		          //Toast.LENGTH_LONG).show();
		       }else{
		        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
		           startActivity(intent);
		       }

		   }
	public void onCreate(Bundle savedInstanceState) {
	  
		//get location... find
		super.onCreate(savedInstanceState);
		//CheckEnableGPS();
		
		
	    setContentView(R.layout.nearlocation_map_listview);
	    
	    stopNames = new HashSet<String>();
	    
	    clickdistanceBtn=(Button)findViewById(R.id.clickdistance);
	    
		pickdayBtn=(Button)findViewById(R.id.clickday);
		
		clickdistanceBtn.setOnClickListener(this);
		
		pickdayBtn.setOnClickListener(this);
		
	    googleMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
	    
	    googleMap.getUiSettings().setZoomControlsEnabled(false);
	    
	   // googleMap.setOnInfoWindowClickListener(getInfoWindowClickListener());
	    googleMap.setOnInfoWindowClickListener(this);
	    
	    googleMap.setOnMarkerClickListener(this);
	    
	    app = (AppData)this.getApplicationContext();
	    
	    results = app.getDB().getStationsNearMe();
	    
	    addMyLocationOnMap();
	    
	    addStationsNearMe(distance);
	    
	    //at this point, after all this google map setup, we should have a list of stop names 
	    possibleStops = new LinkedHashMap<String, String>();
	    
	    selectFirstEntry();
	    
	}

	protected void onListItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		TripDetails rd = (TripDetails)listView1.getItemAtPosition(position);
		//AppData app = (AppData)this.getApplicationContext();
		//int stopID = rd.routeName;
		Intent myIntent = new Intent(this, RouteStopsActivity.class);
		NextStopObject data = new NextStopObject();
		data.SetNote("");
		data.SetStopID(rd.routeName);//String.valueOf(stopID));
		myIntent.putExtra("data", data);
		startActivity(myIntent);
	}
	
	private void setListData(String routeID )
	{
		AppData app = (AppData)this.getApplicationContext();
		int size = possibleStops.size();//ridesInDict.size();
		if(size==0)
			size=1;
		TripDetails trip_list[] = new TripDetails[size];
		listView1 = (ListView)findViewById(R.id.listView1);
		TextView tx = (TextView)findViewById(R.id.info);
		int i = 0;
		String selectedStop = app.getStopIDAndStopName().get(routeID);
		tx.setText(selectedStop);
		if(possibleStops.size()>0)
		{
		for(Entry<String, String> entry : possibleStops.entrySet()){
            
        	//String key = entry.getKey();
        	String[] splitVal = entry.getValue().split(",");
        	String shortName =  splitVal[0];

			String titleHeading = app.getDB().getLastStopFromShortName(shortName);
			String time=null;
			//int c1 = splitVal.length;
			time = app.getTwelveHourClockForTwentyFourHour(splitVal[1]);//ridesInDict.get(shortName);
			String headingTo = titleHeading;
			String trainNum = shortName;
			String startingFrom = app.getDB().getFirstStopForTripShortName(shortName);
			String arrival_time = app.getTwelveHourClockForTwentyFourHour(app.getDB().getArrivalTimeForTrainNum(shortName));
			
			String ID = app.getShortToRouteId().get(shortName);
   
			String title = ID + " " + trainNum + " @" + time;
			String description = "Headed to " +  headingTo +" from " + startingFrom + " scheduled to arrive in " + headingTo + " at " + arrival_time;
			boolean showImage = true;
			 if(app.containsBikeCar(trainNum))
			       showImage=true;
			    else
			        showImage=false;
			 
			trip_list[i] = new TripDetails(title, description, showImage, trainNum);//Integer.parseInt(trainNum));
			i++;
			
		}
		}
		else
		{
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
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	      MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.menu.routes, menu);

	      return super.onCreateOptionsMenu(menu);
	    }
	 
	    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
                
            case R.id.lines:
          	  //if(this.getClass()!=RoutesActivity.class)
          	  //startActivity(new Intent(this, RoutesActivity.class));
              
          	  //if(this.getClass()!=RoutesActivity.class)
          	  {
          		  Intent intent1 = new Intent(this, RoutesActivity.class);
          		  intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
          		  startActivity(intent1);
          	  }
              break;
          	case R.id.near:
              //stopService(new Intent(this, UpdaterService.class));
              break;
            case R.id.alerts:
            {
      		  Intent intent1 = new Intent(this, AlertsActivity.class);
      		  intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
      		  startActivity(intent1);
            }
              break;
            case R.id.stations:
          	  //sif(this.getClass()!=StationActivity.class)
          	  {
          		  Intent intent1 = new Intent(this, StationActivity.class);
          		  intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
          		  startActivity(intent1);
          	  }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
	@Override
	public boolean onMarkerClick(Marker marker) {
		
		this.selectStop(marker.getTitle());
		return false;
	}
	@Override
	public void onInfoWindowClick(Marker marker) {
		
		this.selectStop(marker.getTitle());
		
	}
	@Override
	public void onClick(View v) {
		
		//toggle depending....
		switch (v.getId()) {
		case R.id.clickdistance:
			 
			String buttonText = clickdistanceBtn.getText().toString(); 
			if (buttonText.equals(getResources().getText(R.string.sevenmiles)))
			{
				addStationsNearMe(24140);
				clickdistanceBtn.setText(R.string.fifteenmiles);
			}
			else if (buttonText.equals(getResources().getText(R.string.fifteenmiles)))
			{
				addStationsNearMe(0);
				clickdistanceBtn.setText(R.string.alltrains);
			}
			else
			{
				addStationsNearMe(11265);
				clickdistanceBtn.setText(R.string.sevenmiles);
			}
			break;
		case R.id.clickday:
			Intent calendarIntent = new Intent(this, CalendarDays.class);
            calendarIntent.putExtra("day", 0);
            startActivityForResult(calendarIntent, 0); 
           
			break;
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_OK) {
			int day2 = data.getIntExtra("day", 0);
			//Log.d("TripsActivity", "Day is " + day2);
			currentDay = day2;
			setDayTextOnButton(currentDay);
			possibleStops.clear();
			//String stopID = app.getStopNameAndStopId().get(currentlySelectedStop);
			possibleStops = app.getDB().getStopsForStopId(currentlySelectedStop, false,day2);
			setListData(currentlySelectedStop);
		}
	}
	
	private void setDayTextOnButton(int day)
	{
		switch(day)
		{
		
		case 1:
			pickdayBtn.setText(R.string.sunday);
			break;
		case 2:
			pickdayBtn.setText(R.string.monday);
			break;
		case 3:
			pickdayBtn.setText(R.string.tuesday);
			break;
		case 4:
			pickdayBtn.setText(R.string.wednesday);
			break;
		case 5:
			pickdayBtn.setText(R.string.thursday);
			break;
		case 6:
			pickdayBtn.setText(R.string.friday);
			break;
		case 7:
			pickdayBtn.setText(R.string.saturday);
			break;
		}
	}

	
	
}
