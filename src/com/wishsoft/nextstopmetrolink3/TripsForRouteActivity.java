package com.wishsoft.nextstopmetrolink3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

//this class is for the Map + ListView

public class TripsForRouteActivity extends android.support.v4.app.FragmentActivity implements OnInfoWindowClickListener
{	
	private ListView listView1;
	private GoogleMap googleMap;
	private AppData app = null;
	//private HashSet<String> allStops;
	private void loadroute(String routeID)
	{
        googleMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
         googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.setOnInfoWindowClickListener(this);
        Vector<PointsLongLat> pointsArr = app.getDB().getShapesForRoute(routeID);
        PolylineOptions p = new PolylineOptions();
        final LatLngBounds.Builder bc = new LatLngBounds.Builder();
        String color = app.getDB().getColorForRoute(routeID);
        long colorInt = app.getColorFromString(color);
      
        for(PointsLongLat p1 : pointsArr)
        {
        	LatLng l = new LatLng(p1.latitude, p1.longitude);
        	
        	
        	p.width(10);
        	p.add(l);
        	bc.include(l);
        	
        }p.color((int)colorInt);
        googleMap.addPolyline(p);
       

        googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0) {
                // Move camera.
            	googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 10));
                // Remove listener to prevent position reset on camera move.
            	googleMap.setOnCameraChangeListener(null);
            }
        });
      
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //setContentView(R.layout.tripsforroute); //extends Activity
	    setContentView(R.layout.tripsforroute_map);
	    //add the map and the listview right below it
	    //allStops=new HashSet<String>();
	    Bundle extras = getIntent().getExtras();
	    HashSet<String> details = null;
	    String routeID =null;
	    app = (AppData)this.getApplicationContext();
	    if (extras != null)
	    {
	        routeID = extras.getString("RouteID");
	        details = app.getDB().getLowestStopOrderForStop(routeID);
	        setTitle(routeID);
	        
	    }
	    else
	    {
	        //..oops!
	    }
	    
	    loadroute(routeID);
	    ArrayList<String> list = new ArrayList<String>(details);
		Collections.sort(list);
	    StationDetails stops_list[] = new StationDetails[list.size()];
	    
	    int c = 0;
	    for(String s : details)
	    {
	    	//piggy-back on this existing adapter....
	    	addStopMarkers(s);
	    	stops_list[c] = new StationDetails(s);//this takes two strings, stopname & stoptime...set them both the same
	    	c++;
	    }

	    StationAdapter adapter = new StationAdapter(this, R.layout.stationslistview_item, stops_list);
        listView1 = (ListView)findViewById(R.id.listView1);
        listView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onListItemClick(parent, view, position, id);
				///showToast(rd.routeName);
			}
		});
        listView1.setAdapter(adapter);
        listView1.setDividerHeight(1);	     
	    //stopSet
	}
	

	protected void onListItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		
		StationDetails rd = (StationDetails) listView1.getItemAtPosition(position);
		
		
		AppData app = (AppData) this.getApplicationContext();
		// stopID is a number
		// routeID is the station name "Buena Park Metrolink Station"
		String stopID = app.getStopID(rd.stationName);
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, String> possibleStops = app.getDB().getStopsForStopId(stopID, false);
		Intent myIntent = new Intent(this, TripsActivity.class);
//
		NextStopObject data = new NextStopObject();
//
		data.SetNote("");
		data.SetStopID(stopID);
		data.SetTrains(possibleStops);
		data.SetTitle("");
		data.setRouteID(rd.stationName);
		myIntent.putExtra("data", data);
		startActivity(myIntent);

	}
	private void addStopMarkers(String stopName)
	{
		String name = stopName;
		Dictionary<String,PointsLongLat> point = app.getDB().getCoordinateForStopName(stopName);
		PointsLongLat p1 = point.get(stopName);
		double longitude = p1.longitude;
		double latitude = p1.latitude;
		googleMap.addMarker(new MarkerOptions()
	 	                          .position(new LatLng(latitude, longitude))
	 	                          .title(name));
	}
	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub
		
		selectStop(marker.getTitle());
	}
	
	private void selectStop(String stationName)
	{
		//StationDetails rd = (StationDetails) listView1.getItemAtPosition(position);
		
		AppData app = (AppData) this.getApplicationContext();
		// stopID is a number
		// routeID is the station name "Buena Park Metrolink Station"
		String stopID = app.getStopID(stationName);
		LinkedHashMap<String, String> possibleStops = app.getDB().getStopsForStopId(stopID, false);
		Intent myIntent = new Intent(this, TripsActivity.class);
		NextStopObject data = new NextStopObject();

		data.SetNote("");
		data.SetStopID(stopID);
		data.SetTrains(possibleStops);
		data.SetTitle("");
		data.setRouteID(stationName);
		myIntent.putExtra("data", data);
		startActivity(myIntent);
	}
   
    

    
    
}
