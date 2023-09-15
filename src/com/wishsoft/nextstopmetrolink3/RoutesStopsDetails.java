package com.wishsoft.nextstopmetrolink3;

public class RoutesStopsDetails {

		public String stopTime;
	    public String stopName;
	    public RoutesStopsDetails(){
	        super();
	    }
	    
	    public RoutesStopsDetails(String stopTime, String stopName) {
	        super();
	        this.stopName = stopName;
	        this.stopTime = stopTime;
	    }

		public RoutesStopsDetails(RouteDetailsClass routeDetailsClass) {
			// TODO Auto-generated constructor stub
			
			super();
			this.stopName = routeDetailsClass.stopId;
			this.stopTime = routeDetailsClass.departureTime;
		}
	};