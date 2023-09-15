package com.wishsoft.nextstopmetrolink3;

public class PointsLongLat {

	public double longitude;
	public double latitude;
	public PointsLongLat()
	{
		longitude=0.0;
		latitude =0.0;
	}
	public PointsLongLat(String latStr, String longStr)
	{
		longitude = Double.parseDouble(longStr);
		latitude = Double.parseDouble(latStr);
	}
	public PointsLongLat(double latDouble, double longDouble)
	{
		longitude = longDouble;
		latitude = latDouble;
	}
}
