package com.wishsoft.nextstopmetrolink3;

import java.util.LinkedHashMap;

import android.os.Parcel;
import android.os.Parcelable;

public class NextStopObject implements Parcelable{

	//private List<String> trains;
	private LinkedHashMap<String,String> trains;
	private String stopID;
	private String note;
	private String title;
	private String routeID;	//Anaheim Metrolink Station...
	//private RouteDetailsClass[] rcdArray;
	
	
	public NextStopObject()	//for non-parcel
	{
		trains = new LinkedHashMap<String,String>(); //new ArrayList<String>();
		stopID = null;
		note = null;
		title = null;
		setRouteID(null);
		//setRcdArray(null);
	}
	private NextStopObject(Parcel in)
	{
		//trains = new ArrayList<String>();
		readFromParcel(in);
	}
	
	private void readFromParcel(Parcel in)	
	{
		title = in.readString();
		note = in.readString();
		stopID = in.readString();
		trains = new LinkedHashMap<String,String>();//new ArrayList<String>();
		in.readMap(trains,null);
		//in.readList(trains, null);
		setRouteID(in.readString());
		
		//setRcdArray((RouteDetailsClass[]) in.readParcelableArray(
         //       com.example.nextstopmetrolink2.RouteDetailsClass.class.getClassLoader()));
	}
	
	public  LinkedHashMap<String,String>/*List<String>*/ Trains()
	{
		return trains;
	}
	public void SetTrains(LinkedHashMap<String,String> possibleStops)//List<String> possibleStops)
	{
		if(trains==null)
			trains = new LinkedHashMap<String,String>();//new ArrayList<String>();
		trains = possibleStops;
	}
	public String StopID()
	{
		return stopID;
	}
	public void SetStopID(String value)
	{
		stopID = value;
	}
	public void SetNote(String value)
	{
		note = value;
	}
	public String Note()
	{
		return note;
	}
	public void SetTitle(String value)
	{
		title = value;
	}
	public String Title()
	{
		return title;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	

	public static final Parcelable.Creator<NextStopObject> CREATOR = new Parcelable.Creator<NextStopObject>() {
		
		public NextStopObject createFromParcel(Parcel in)
		{
			return new NextStopObject(in);
		}

		@Override
		public NextStopObject[] newArray(int size) {
			// TODO Auto-generated method stub
			return new NextStopObject[size];
		}
	};
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		
		// TODO Auto-generated method stub
		Parcel out = (Parcel)arg0;
		
		out.writeString(title);
		out.writeString(note);
		out.writeString(stopID);
		out.writeMap(trains);//.writeList(trains);
		//out.writeParcelableArray(rcdArray, 0);
		out.writeString(routeID);
	}
	public String getRouteID() {
		return routeID;
	}
	public void setRouteID(String routeID) {
		this.routeID = routeID;
	}
//	public RouteDetailsClass[] getRcdArray() {
//		return rcdArray;
//	}
//	public void setRcdArray(RouteDetailsClass[] rcdArray) {
//		if(rcdArray!=null)
//			this.rcdArray = rcdArray;
//	}
}
