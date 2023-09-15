package com.wishsoft.nextstopmetrolink3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import android.app.Application;
import android.graphics.Color;
import android.util.Log;

public class AppData extends Application {

	private static DBHelper db;
	private Dictionary<String, String> stopNameAndStopId;
	private Dictionary<String, String> stopIDAndStopName;
	
	private Dictionary<String, String> tripIdToShort;
	private Dictionary<String, String> shortToTripId;
	
	private Dictionary<String, String> shortToRouteId;
	
	private HashSet<String> bikeCars;
	
	
	public boolean containsBikeCar(String trainNum)
	{
		return bikeCars.contains(trainNum);
	}
	public DBHelper getDB()
	{
		return db;
	}
	public Dictionary<String, String> getStopIDAndStopName() {
		
		return stopIDAndStopName;
	}
	public void setStopIDAndStopName(String stopID, String stopName) {
		
		stopIDAndStopName.put(stopID, stopName);
	}
	public String getStopID(String stopName)
	{
		//Log.d("AppData", stopName);
		if(stopNameAndStopId.isEmpty())
		{
			//Log.d("AppData","its empty" );
			return null;
		}
		String d = stopNameAndStopId.get((String)stopName);
		//Log.d("AppData", d);
		return d;
	}
	public Dictionary<String, String> getStopNameAndStopId() {
	
		return stopNameAndStopId;
	}
	public void setStopNameAndStopId(String stopName, String stopID) {
		
		stopNameAndStopId.put(stopName, stopID);
	}
	public Dictionary<String, String> getTripIdToShort() {
		
		return tripIdToShort;
	}
	public void setTripIdToShort(String trip_id, String trip_short_name) {
		
		tripIdToShort.put(trip_id, trip_short_name);
	}
	public Dictionary<String, String> getShortToTripId() {
		
		return shortToTripId;
	}
	public void setShortToTripId(String trip_short_name, String trip_id) {
		
		shortToTripId.put(trip_short_name, trip_id);
	}
	public long getColorFromString(String color)
	{
		
		if(color.startsWith("0X"))
			color = color.substring(2);
		if(color.length() < 6)
			return 0;

		String ColorPlusTrans =  "FF" + color; 		
		return Long.parseLong(ColorPlusTrans, 16);//Integer.parseInt(ColorPlusTrans, 16);
	}

	public String getTwelveHourClockForTwentyFourHour(String time)
	{
		SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm:ss a");
	    SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm:ss");
	    Date date;
		try {
			date = parseFormat.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return time;
		}
	    return displayFormat.format(date);
	   
	}
	public void loadBikeCars()
	{
		List<String> list = Arrays.asList( "850", "804", "808", "803", "805", "807", "851", "858", "860", 
				"857", "859", "904", "909", "303", "609", "307", "311", "319", "327", 
				"335", "387", "302", "310", "318", "320", "322", "324", "328", 
				"386", "357", "369", "362", "368", "407", "408", "605", "683", 
				"607", "685", "687", "641", "643", "645", "682", "684", "602", 
				"640", "604", "642", "644", "661", "665", "663", "667", "660",
				"662", "666", "664", "850", "804", "808", "803", "805", "807", 
				"851", "858", "860", "857", "859");
		bikeCars = new HashSet<String>(list);
	 
	    
	}
	@Override
   public void onCreate() {

		//Log.d("AppData", "OnCreate dictionaries");
		try
		{
			db = new DBHelper(this);   
			db.createDataBase();      
			db.opendatabase();
			
		}catch(Exception e)
		{
			//Log.d("AppData", "OnCreate DBHelper issue...");
		}
		stopNameAndStopId = new Hashtable<String, String>();
		stopIDAndStopName = new Hashtable<String, String>();
		tripIdToShort = new Hashtable<String,String>();
		shortToTripId = new Hashtable<String,String>();
		shortToRouteId = new Hashtable<String,String>();
		
		db.fillStopNamesDictionary();
		db.createDictionaryWithTripIDAndShortName();
		db.fillTripDictionary();
		this.loadBikeCars();
		super.onCreate();
	}
	public Dictionary<String, String> getShortToRouteId() {
		return shortToRouteId;
	}
	public void setShortToRouteId(String trip_short_name, String line) {
		this.shortToRouteId.put(trip_short_name, line);
	}
		
}