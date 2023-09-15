package com.wishsoft.nextstopmetrolink3;

//Class Name: RouteDetails
//Holds specifics of each Route
//Color as hex string, Title for Routes screen
 
public class RouteDetails 
{
	public String color;
    public String routeName;
    public RouteDetails(){
        super();
    }
    
    public RouteDetails(String color, String routeName) {
        super();
        this.color = color;
        this.routeName = routeName;
    }
};

