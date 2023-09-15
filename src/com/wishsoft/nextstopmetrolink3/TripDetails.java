package com.wishsoft.nextstopmetrolink3;


//for the station names to be listed
//used for the data adapter for the listview
public class TripDetails {

	public boolean bikeCar;
	public String tripTitle;
	public String tripDetails;
	//public int 	  routeName;//will be a three digit val
	public String routeName;//changed to string to resolve issues with 153(A761)
	public TripDetails(){
		super();
		
	
	}
	
	public TripDetails(String title, String detail, boolean bikeCar, String routeName)
	{
		super();
		this.bikeCar=bikeCar;
		this.tripDetails=detail;
		this.tripTitle=title;
		this.routeName = routeName;
	}
}
