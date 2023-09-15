package com.wishsoft.nextstopmetrolink3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBHelper extends SQLiteOpenHelper{

	private Context mContext;
	//copied the database file as-is from Next Stop Metrolink iOS version (NS_DB)
	@SuppressLint("SdCardPath")
	private static String DB_PATH = "/data/data/com.example.nextstopmetrolink2/databases/";
	private static String DB_NAME = "database1.sqlite";//the extension may be .sqlite or .db
	public SQLiteDatabase myDataBase;
	public AppData m_AppData;
	public DBHelper(Context context) throws IOException 
	{
		
		super(context,DB_NAME,null,1);
		
	    //super(context, DB_NAME, null, 1);// 1? its Database Version
	    if(android.os.Build.VERSION.SDK_INT >= 4.2){
	       DB_PATH = context.getApplicationInfo().dataDir + "/databases/";         
	    }
	    else
	    {
	    	//string t = context.getFilesDir().getPath();
	       //DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
	    	DB_PATH = context.getFilesDir().getPath() + context.getPackageName() + "/databases/";
	    }
	    this.mContext = context;
	    this.m_AppData = (AppData)mContext.getApplicationContext();

	}
	  
	public void createDataBase() throws IOException
	{
	    //If database not exists copy it from the assets

	    boolean mDataBaseExist = checkDataBase();
	    if(!mDataBaseExist)
	    {
	        this.getReadableDatabase();
	        this.close();
	        try 
	        {
	            //Copy the database from assests
	            copyDataBase();
	            
	        } 
	        catch (IOException mIOException) 
	        {
	            throw new Error("ErrorCopyingDataBase");
	        }
	    }
	}
    //Check that the database exists here: /data/data/your package/databases/Da Name
    private boolean checkDataBase()
    {
        File dbFile = new File(DB_PATH + DB_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

  //Copy the database from assets
    private void copyDataBase() throws IOException
    {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public void opendatabase() throws SQLException {
        //Open the database
        String mypath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
        

    }

    
    public synchronized void close() {
        if(myDataBase != null) {
            myDataBase.close();
        }
        super.close();
    }

    public String getServiceIdFromTripId(String tripId)
    {
        //service_id
        String q = "select service_id from trips where trip_id=\"" + tripId + "\"";
        Cursor c = null;
        try
        {
        	c = myDataBase.rawQuery(q, null);
        	if(c!=null)
        	{
        		if(c.moveToFirst())
        			return c.getString(c.getColumnIndex("service_id"));
        	}
        }
        catch(Exception e)
        {
        	
        	return null;
        }
        finally
    	{
    		if(c!=null && !c.isClosed())
        		c.close();
    	}
        return null;
    }
    private boolean doesTripRun(String serviceid, int day)	//checks if the service runs on this day
    {
        //day will be monday, tuesday, wednesday, etc
        //service_id should exist on the calendar table under service_id
        /*// Field descriptor #23 I
  public static final int SUNDAY = 1;
  
  // Field descriptor #23 I
  public static final int MONDAY = 2;
  
  // Field descriptor #23 I
  public static final int TUESDAY = 3;
  
  // Field descriptor #23 I
  public static final int WEDNESDAY = 4;
  
  // Field descriptor #23 I
  public static final int THURSDAY = 5;
  
  // Field descriptor #23 I
  public static final int FRIDAY = 6;
  
  // Field descriptor #23 I
  public static final int SATURDAY = 7;*/
        String q = "select * from calendar where service_id=\""+ serviceid + "\"";
        Cursor c = myDataBase.rawQuery(q, null);
        boolean retVal=false;
		if (c != null ) {
		    if  (c.moveToFirst()) {
		          
		        	  switch(day){
		        	  	case 2:
		        	  		if(c.getInt(c.getColumnIndex("monday")) == 1)
		        	  			retVal = true;
		        	  		break;
		        	  	case 3:
		        	  		if(c.getInt(c.getColumnIndex("tuesday")) == 1)
		        	  			retVal = true;//return true;
		        	  		break;
		        	  	case 4:
		        	  		if(c.getInt(c.getColumnIndex("wednesday")) == 1)
		        	  			retVal = true;//return true;
		        	  		break;
		        	  	case 5:
		        	  		if(c.getInt(c.getColumnIndex("thursday")) == 1)
		        	  			retVal = true;//return true;
		        	  		break;
		        	  	case 6:
		        	  		if(c.getInt(c.getColumnIndex("friday")) == 1)
		        	  			retVal = true;//return true;
		        	  		break;
		        	  	case 7:
		        	  		if(c.getInt(c.getColumnIndex("saturday")) == 1)
		        	  			retVal = true;//return true;
		        	  		break;
		        	  	case 1:
		        	  		if(c.getInt(c.getColumnIndex("sunday")) == 1)
		        	  			retVal = true;//return true;
		        	  		break;
		        	  	//default:
		        	  	//	return false;
		        	  		
		        	  }
		          }
		    }
		if(!c.isClosed())
			c.close();
        return retVal;
    }

	
    private boolean hasTrainAlreadyPassed(String trainTimeFound)
    {
    	Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	//SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
    	java.util.Date trainTime;// = cal.getTime());
    	try {
			 trainTime = sdf.parse(trainTimeFound);//(Date)parser.parse(trainTimeFound);
			 
			 String currentTime = sdf.format(cal.getTime());
		    	java.util.Date curTime = sdf.parse(currentTime);//Date.valueOf(currentTime);
		    	
		    	if (!trainTime.after(curTime)){
		           return true;
		        }
		    	
		} catch (java.text.ParseException e) {
			
			e.printStackTrace();
			return false;
		}
    	
	    
    	
    	return false;
    }

    private String getFriendNameForRouteID(String routeID)
    {
        String q = "select stop_name from stops where stop_id=\"" + routeID +  "\"";
        Cursor c = null;
        c = myDataBase.rawQuery(q,  null);
        String retVal=null;
        if(c!=null && c.moveToFirst())
        {
        	retVal = c.getString(c.getColumnIndex("stop_name"));
        	//return c.getString(c.getColumnIndex("stop_name"));
        }
        c.close();
        return retVal;
        
    }
    
    public RouteDetailsClass[] getRouteDetailsForRoute(String shortName)
	{
    	//Log.d("getRouteDetailsForRoute",shortName);
    	String s = this.m_AppData.getShortToTripId().get(shortName);
		String q1 = "select stop_id,stop_sequence,departure_time from stoptimes where trip_id=\"" + s + "\" order by departure_time";
		Cursor c = null;
		c = myDataBase.rawQuery(q1, null);
		
		RouteDetailsClass[] retVal = null;
		if (c != null ) {
		    if  (c.moveToFirst()) 
		    {
		    	//int c1 = c.getCount();
		    	retVal = new RouteDetailsClass[c.getCount()];
		    	int count=0;
		    	do{
		    		retVal[count] = new RouteDetailsClass();
		    		retVal[count].stopId = this.m_AppData.getStopIDAndStopName().get(c.getString(c.getColumnIndex("stop_id")));
		    		retVal[count].stopSequence = c.getString(c.getColumnIndex("stop_sequence"));
		    		retVal[count].departureTime =  m_AppData.getTwelveHourClockForTwentyFourHour(c.getString(c.getColumnIndex("departure_time")));
		    		count++;
		    	}while(c.moveToNext());
		    }
		}
		c.close();
		
		return retVal;
	}
    public String getLastStopFromShortName(String shortName)
    {
    	 String s = this.m_AppData.getShortToTripId().get(shortName);
    	String q1 = "select stop_sequence, stop_id from stoptimes where trip_id=\"" + s + "\"";
    	Cursor c=null;
    	int largestSequence =-1;
    	String stopID=null;
    	try
    	{
    		c = myDataBase.rawQuery(q1, null);
    		if (c != null ) {
    		    if  (c.moveToFirst()) {
    		    	do{
    		    		int stopSeq = c.getInt(c.getColumnIndex("stop_sequence"));
    		    		if(stopSeq>largestSequence)
    		    		{
    		    			largestSequence = stopSeq;
    		    			stopID = c.getString(c.getColumnIndex("stop_id"));
    		    		}
    		    	}while(c.moveToNext());
    		    	if(largestSequence>-1)
    		            return this.getFriendNameForRouteID(stopID);
    		    }
    		}
    	}
    	catch(Exception e)
    	{
    		
    	}
    	finally{
    		if(c!=null && !c.isClosed())
        		c.close();
    	}		
    	
    	return null;
    }
 
    
    public String getFirstStopForTripShortName(String shortName)
    {
    	String s = this.m_AppData.getShortToTripId().get(shortName);
        String q1 = "select stop_sequence, stop_id from stoptimes where trip_id=\""+ s + "\"";
        Cursor c=null;
    	int largestSequence =100000;
    	String stopID=null;
    	try
    	{
    		c = myDataBase.rawQuery(q1, null);
    		if (c != null ) {
    		    if  (c.moveToFirst()) {
    		    	do{
    		    		int stopSeq = c.getInt(c.getColumnIndex("stop_sequence"));
    		    		if(stopSeq<largestSequence)
    		    		{
    		    			largestSequence = stopSeq;
    		    			stopID = c.getString(c.getColumnIndex("stop_id"));
    		    		}
    		    	}while(c.moveToNext());
    		    	if(largestSequence	<100000)
    		            return this.getFriendNameForRouteID(stopID);
    		    }
    		}
    	}
    	catch(Exception e)
    	{
    		
    	}
    	finally{
    		if(c!=null && !c.isClosed())
        		c.close();
    	}		
    	
    	return null;
    }

    public void fillTripDictionary()
    {
    	String q1 = "select trip_short_name, route_id from trips";
    	Cursor c = null;
    	c = myDataBase.rawQuery(q1, null);
    	try
    	{
		if (c != null ) {
		    if  (c.moveToFirst()) {
		    	do{
		    		
		    		String line = c.getString(c.getColumnIndex("route_id"));//[rs stringForColumn:@"route_id"];
		            if(line.equals("Ventura County Line"))
		                this.m_AppData.setShortToRouteId(c.getString(c.getColumnIndex("trip_short_name")), "VC");//[shortToRouteId setObject:@"VC" forKey:[rs stringForColumn:@"trip_short_name"]];
		            else if(line.equals("San Bernardino Line"))
		                 this.m_AppData.setShortToRouteId(c.getString(c.getColumnIndex("trip_short_name")), "SB");//[shortToRouteId setObject:@"SB" forKey:[rs stringForColumn:@"trip_short_name"]];
		            else if(line.equals("Orange County Line"))
		                 this.m_AppData.setShortToRouteId(c.getString(c.getColumnIndex("trip_short_name")), "OC");//[shortToRouteId setObject:@"OC" forKey:[rs stringForColumn:@"trip_short_name"]];
		            else if(line.equals("Antelope Valley Line"))
		                 this.m_AppData.setShortToRouteId(c.getString(c.getColumnIndex("trip_short_name")), "ANT");//[shortToRouteId setObject:@"ANT" forKey:[rs stringForColumn:@"trip_short_name"]];
		            else if(line.equals("91 Line"))
		                 this.m_AppData.setShortToRouteId(c.getString(c.getColumnIndex("trip_short_name")), "91");//[shortToRouteId setObject:@"91" forKey:[rs stringForColumn:@"trip_short_name"]];
		            else if(line.equals("Burbank-Bob Hope Airport"))
		                 this.m_AppData.setShortToRouteId(c.getString(c.getColumnIndex("trip_short_name")), "Burbank");//[shortToRouteId setObject:@"Burbank" forKey:[rs stringForColumn:@"trip_short_name"]];
		            else if(line.equals("Burbank-Bob Hope Airport Metrolink Station"))
		                 this.m_AppData.setShortToRouteId(c.getString(c.getColumnIndex("trip_short_name")), "Burbank"); //[shortToRouteId setObject:@"Burbank" forKey:[rs stringForColumn:@"trip_short_name"]];
		            else if(line.equals("Inland Emp.-Orange Co. Line"))
		            	 this.m_AppData.setShortToRouteId(c.getString(c.getColumnIndex("trip_short_name")), "IEOC");//[shortToRouteId setObject:@"IEOC" forKey:[rs stringForColumn:@"trip_short_name"]];
		            else if(line.equals("Riverside Line"))
		            	 this.m_AppData.setShortToRouteId(c.getString(c.getColumnIndex("trip_short_name")), "RL");//[shortToRouteId setObject:@"RL" forKey:[rs stringForColumn:@"trip_short_name"]];
//		            else{
//		                NSLog(@"NOT IN SET = %@", [rs stringForColumn:@"trip_short_name"]);
//		            }
		            
		    	}while(c.moveToNext());
		    }
		}
    	}
		catch(Exception e)
		{
			
		}
		finally{
			c.close();
		}
    }
    /* NSString* q1 = [NSString stringWithFormat:@"select trip_short_name, route_id from trips"];
    shortToRouteId = [[NSMutableDictionary alloc] init];
    FMResultSet *rs = [db executeQuery:q1];
    while ([rs next])
    {
        //Venture County Line = VC
        //San Bernardino Line = SB
        //Orange County Line = OC
        //Antelope Valley Line = ANT
        //91 Line- 91
        //Burbank-Bob Hope Airport - Burbank
        //Inland Emp.-Orange Co. Line - IEOC
        //Riverside Line - RL
        NSString* line = [rs stringForColumn:@"route_id"];
        if([line isEqualToString:@"Ventura County Line"])
            [shortToRouteId setObject:@"VC" forKey:[rs stringForColumn:@"trip_short_name"]];
        else if([line isEqualToString:@"San Bernardino Line"])
            [shortToRouteId setObject:@"SB" forKey:[rs stringForColumn:@"trip_short_name"]];
        else if([line isEqualToString:@"Orange County Line"])
            [shortToRouteId setObject:@"OC" forKey:[rs stringForColumn:@"trip_short_name"]];
        else if([line isEqualToString:@"Antelope Valley Line"])
            [shortToRouteId setObject:@"ANT" forKey:[rs stringForColumn:@"trip_short_name"]];
        else if([line isEqualToString:@"91 Line"])
            [shortToRouteId setObject:@"91" forKey:[rs stringForColumn:@"trip_short_name"]];
        else if([line isEqualToString:@"Burbank-Bob Hope Airport"])
            [shortToRouteId setObject:@"Burbank" forKey:[rs stringForColumn:@"trip_short_name"]];
        else if([line isEqualToString:@"Burbank-Bob Hope Airport Metrolink Station"])
            [shortToRouteId setObject:@"Burbank" forKey:[rs stringForColumn:@"trip_short_name"]];
        else if([line isEqualToString:@"Inland Emp.-Orange Co. Line"])
            [shortToRouteId setObject:@"IEOC" forKey:[rs stringForColumn:@"trip_short_name"]];
        else if([line isEqualToString:@"Riverside Line"])
            [shortToRouteId setObject:@"RL" forKey:[rs stringForColumn:@"trip_short_name"]];
        else{
            NSLog(@"NOT IN SET = %@", [rs stringForColumn:@"trip_short_name"]);
        }
    }

     * 
     * */
    			
   
    public LinkedHashMap<String, String> getStopsForStopId(String stopId, boolean strip, int day)
    {
    	//Log.d("DBHelper", "Day is " + day);
    	int stopInt = Integer.parseInt(stopId);
    	LinkedHashMap<String,String> result = new LinkedHashMap<String,String>();
    	Cursor c = executeQuery("select distinct arrival_time, trip_id from stoptimes where stop_id=\"" + stopInt + "\" order by arrival_time");
    			  //String q1 = "select distinct arrival_time, trip_id from stoptimes where stop_id=\""+ stopId + "\" order by arrival_time";
    	
    	if(c!=null)
    	{
    		if(c.moveToFirst())
    		{
    			do{
    				String longRouteId = c.getString(c.getColumnIndex("trip_id"));
    		        String serviceID = this.getServiceIdFromTripId(longRouteId); 
    		        boolean routeAvailableToday = this.doesTripRun(serviceID, day);
    		        if(routeAvailableToday)
    		        {
    		        	String time = c.getString(c.getColumnIndex("arrival_time")); //[NSString stringWithFormat:@"%@", [rs stringForColumn:@"arrival_time"]];
    		            if(strip)
    		            {
    		                if(this.hasTrainAlreadyPassed(time)) //if( !([self hasTrainAlreadyPassed:time]))
    		                {
    		                	String res = this.m_AppData.getTripIdToShort().get(c.getString(c.getColumnIndex("trip_id"))) + "," + c.getString(c.getColumnIndex("arrival_time"));
    		                    result.put(res, res);
    		                	//result.add(res, res);
    		                   //[NSString stringWithFormat:@"%@,%@", [self.tripIdToShort objectForKey:[rs stringForColumn:@"trip_id"]], [rs stringForColumn:@"arrival_time"]];
    		                    //[results addObject:res];
    		                }
    		            }
    		            else
    		            {   
    		            	String res = this.m_AppData.getTripIdToShort().get(c.getString(c.getColumnIndex("trip_id"))) + "," + c.getString(c.getColumnIndex("arrival_time"));
		                    result.put(res,res);
    		                //String res = [NSString stringWithFormat:@"%@,%@", [self.tripIdToShort objectForKey:[rs stringForColumn:@"trip_id"]], [rs stringForColumn:@"arrival_time"]];
    		                //[results addObject:res];
    		            }
    		        }
    			}while(c.moveToNext());
    		}
    	}
    	
    	if(c!=null && !c.isClosed())
    		c.close();
    	return result;
    }

    public String getArrivalTimeForTrainNum(String trainNum)
    {
    	String tripID =  this.m_AppData.getShortToTripId().get(trainNum);
    	String q1 = "select arrival_time, stop_sequence from stoptimes where trip_id=\"" + tripID +  "\"";
    	int largest = 0;
    	String time = null;
    	Cursor c = null;
    	try
    	{
    		c = myDataBase.rawQuery(q1, null);
    		if (c != null ) {
    		    if  (c.moveToFirst()) {
    		          do {
    		          int seq = c.getInt(c.getColumnIndex("stop_sequence"));
    		          
    		          if(seq > largest)
    		          {
    		        	  time = c.getString(c.getColumnIndex("arrival_time"));
    		        	  largest = seq;
    		          }
    		          
    		          }while(c.moveToNext());
    		    }
    		}
    	}
    	catch(Exception e)
    	{
    		
    	}
    	finally
    	{
    		c.close();
    	}
    	return time;
    	
    }
    
   
    public LinkedHashMap<String, String>  getStopsForStopId(String stopId, boolean strip)
    {
    	
    	LinkedHashMap<String,String> results = new LinkedHashMap<String,String>();
    	if(myDataBase==null)
    		opendatabase();
    	String q1 = "select distinct arrival_time, trip_id from stoptimes where stop_id=\""+ stopId + "\" order by arrival_time";
    	//strip=true;
    	Calendar today = Calendar.getInstance();
    	//today.set(Calendar.DAY_OF_WEEK, 0); 

    	//the day is for determining if this route runs today
    	//int day = today get(0);
    	int day = today.get(Calendar.DAY_OF_WEEK); 
    	
    	Cursor c=null;
    	try
    	{
    		c = myDataBase.rawQuery(q1, null);
    		if (c != null ) {
    		    if  (c.moveToFirst()) {
    		          do {
    		        	  //String longRouteId = c.getString(c.getColumnIndex("trip_id"));
    		        	  String serviceID = this.getServiceIdFromTripId(c.getString(c.getColumnIndex("trip_id")));
    		        	  boolean routeAvailableToday = this.doesTripRun(serviceID, day);
    		        	 
    		        	  if(routeAvailableToday)
    		        	  {
    		        		String time = c.getString(c.getColumnIndex("arrival_time"));
    		        		String res = this.m_AppData.getTripIdToShort().get(c.getString(c.getColumnIndex("trip_id"))) + "," + c.getString(c.getColumnIndex("arrival_time"));
    		        		
    		        		if(strip)
    		        		{
    		        			
    		        			if(!this.hasTrainAlreadyPassed(time) )
    		        			{
    		        				
    		        				results.put(res, res);
    		        				//results.add(res);
    		        				//Log.d("DBHelper", "HasTrainPassed: " + time + " Added!");
    		        				}
    		        		}
    		        		else
    		        			results.put(res,res);//results.add(res);
    		        	  }
    		          }while(c.moveToNext());
    		    }
    		}
    	}
    	catch(Exception e)
    	{
    		
    	}
    	finally
    	{
    		if(c!=null && !c.isClosed())
        		c.close();
    	}
    	
    	
    	return results;
    }

    public void createDictionaryWithTripIDAndShortName()
    {
	    //tripIdToShort = [[NSMutableDictionary alloc]init];
	    //shortToTripId = [[NSMutableDictionary alloc] init];
	    String q1 = "select distinct trip_id, trip_short_name from trips";
	    
	    Cursor c=null;
    	try
    	{
    		c = myDataBase.rawQuery(q1, null);
    		if (c != null ) {
    		    if  (c.moveToFirst()) {
    		          do {
    		        	  String trip_id = c.getString(c.getColumnIndex("trip_id"));
    		        	  String trip_short_name = c.getString(c.getColumnIndex("trip_short_name"));
    		        	  this.m_AppData.setTripIdToShort(trip_id, trip_short_name); 
    		        	  this.m_AppData.setShortToTripId(trip_short_name, trip_id);
    		          }while (c.moveToNext());
    		    }
    		}
    	}
    	catch(Exception e)	 
    	{
    		
    	}
    	c.close();

    }
    
    private Cursor executeQuery(String q)
    {
    	//String q = "select stop_name, stop_id from stops";
    	Cursor c=null;
    	try
    	{
    		c = myDataBase.rawQuery(q, null);
    	}
    	catch(Exception e)
    	{
    		
    		return null;
    	}
    	finally
    	{
//    		if(c!=null && !c.isClosed())
//        		c.close();
    	}
    	return c;
    	
    }
    
    public void fillStopNamesDictionary()
    {
    	String query = "select stop_name, stop_id from stops";
    	Cursor c=null;
    	try
    	{
    		c = myDataBase.rawQuery(query, null);
    		if (c != null ) {
    		    if  (c.moveToFirst()) {
    		          do {
    		        	  String stopName = c.getString(c.getColumnIndex("stop_name"));
    		        	  String stopID = c.getString(c.getColumnIndex("stop_id"));
    		        	  this.m_AppData.setStopIDAndStopName(stopID, stopName);
    		        	  this.m_AppData.setStopNameAndStopId(stopName, stopID);
    		        	 
    		        	   
    		          }while (c.moveToNext());
    		    } 
    		}
    		
    	}
    	catch(Exception e)
    	{
    		
    	}
    	finally
    	{
    		if(c!=null && !c.isClosed())
        		c.close();
    	}
    }
    
    public Vector<PointsLongLat> getShapesForRoute(String routeID)
    {
    	Vector<PointsLongLat> points  = new Vector<PointsLongLat>();
    	//SortedSet<Double> sequence = new SortedSet<Double>();
    	Dictionary<Double, PointsLongLat> data = new Hashtable<Double, PointsLongLat>();
    	Cursor c = executeQuery("select distinct shape_id from trips where route_id=\"" + routeID +  "\"");
    	String ID = null;
        if(c!=null)
        {
        	if  (c.moveToFirst()) {
		        	  ID = c.getString(c.getColumnIndex("shape_id"));
		    }
        }
        if(ID!=null)
        {
            c = executeQuery("select * from shapes where shape_id=\""+ ID + "\" order by shape_pt_sequence");
            Vector<Double> sequence = new Vector<Double>();
            if(c!=null)
            {
            	if(c.moveToFirst())
            	{
            		do {
	            	PointsLongLat point = new PointsLongLat();
	            	point.latitude = c.getDouble(c.getColumnIndex("shape_pt_lat"));
	            	point.longitude = c.getDouble(c.getColumnIndex("shape_pt_lon"));
	            	double numStopSeq = c.getDouble(c.getColumnIndex("shape_pt_sequence"));
	            	data.put(numStopSeq, point);
	            	sequence.add(numStopSeq);
            		}while(c.moveToNext());
            	}
            	
            	Collections.sort(sequence);
            	for(Double d : sequence)
            	{
            		points.add(data.get(d));
            	}
            }
        }
        if(c!=null && !c.isClosed())
    		c.close();
		return points;
    }
    
    public String getColorForRoute(String routeName)
    {
    	String color = null;
    	Cursor c = executeQuery("select route_color from routes where route_id=\""+routeName+"\"");
    	if(c!=null)
    	{
    		if(c.moveToFirst())
        	{
    			String retVal = 
    			 c.getString(c.getColumnIndex("route_color"));
    			if(c!=null && !c.isClosed())
            		c.close();
    			return retVal;
        	}
    	}
    	
    	return color;
    }
    public Dictionary<String, PointsLongLat> getCoordinateForStopName(String stopName)
    {
    	Dictionary<String, PointsLongLat> res = new Hashtable<String, PointsLongLat>();
    	Cursor c = executeQuery("select stop_name, stop_lat, stop_lon from stops where stop_name=\""+stopName+"\"");
    	if(c!=null)
    	{
    		if(c.moveToFirst())
        	{
    			PointsLongLat point = new PointsLongLat(c.getDouble(c.getColumnIndex("stop_lat")), c.getDouble(c.getColumnIndex("stop_lon")));
    			res.put(c.getString(c.getColumnIndex("stop_name")), point);
        	}
    	}
    	if(c!=null && !c.isClosed())
    		c.close();
    	return res;
    }
    
    public Map<String, PointsLongLat> getStationsNearMe()
    {
    	Map<String, PointsLongLat> result = new Hashtable<String, PointsLongLat>();
    	Cursor c = executeQuery("select stop_name, stop_lat, stop_lon from stops");
    	if(c!=null)
    	{
    		if(c.moveToFirst())
    		{
    			do{
    				PointsLongLat point = new PointsLongLat(c.getDouble(c.getColumnIndex("stop_lat")), c.getDouble(c.getColumnIndex("stop_lon")));
        			result.put(c.getString(c.getColumnIndex("stop_name")), point);
    				//[longAndLat setObject:[rs stringForColumn:@"stop_lat"] forKey:[rs stringForColumn:@"stop_lon"]];
    		        //[stopIDDict setObject:longAndLat forKey:[rs stringForColumn:@"stop_name"]];
    			}while(c.moveToNext());
    		}
    		
    		if(c!=null && !c.isClosed())
        		c.close();
    	}
    	
    	return result;
    }
    /*
     * 
     * - (NSMutableDictionary*) getFiveStationsNear:(double) longitude andLat:(double)latitude
{
    NSMutableDictionary* stopIDDict = [[NSMutableDictionary alloc]init];
    NSString* q1 = [NSString stringWithFormat:@"select stop_name, stop_lat, stop_lon from stops "];
    
    FMResultSet *rs = [db executeQuery:q1];
    while ([rs next])
    {
       
        NSMutableDictionary* longAndLat = [[NSMutableDictionary alloc]init];
        [longAndLat setObject:[rs stringForColumn:@"stop_lat"] forKey:[rs stringForColumn:@"stop_lon"]];
        [stopIDDict setObject:longAndLat forKey:[rs stringForColumn:@"stop_name"]];
    }
    return stopIDDict;
}

     * - (NSMutableDictionary*) getCoordinateForStopName:(NSString*) stopName
{
    NSMutableDictionary* stopIDDict = [[NSMutableDictionary alloc]init];
    NSString* q1 = [NSString stringWithFormat:@"select stop_name, stop_lat, stop_lon from stops where stop_name=\"%@\"", stopName];
    
    FMResultSet *rs = [db executeQuery:q1];
    if ([rs next])
    {
        
        NSMutableString* longAndLat = [[NSMutableString alloc]init];
        [longAndLat setString:[NSString stringWithFormat:@"%@,%@", [rs stringForColumn:@"stop_lat"] ,[rs stringForColumn:@"stop_lon"]]];
        //[longAndLat setObject:[rs stringForColumn:@"stop_lat"] forKey:[rs stringForColumn:@"stop_lon"]];
        [stopIDDict setObject:longAndLat forKey:[rs stringForColumn:@"stop_name"]];
    }
    return stopIDDict;
}
     * - (NSString*) getColorForRoute:(NSString*) routeName
{
    //NSMutableArray * tempList = [[NSMutableArray alloc] init];
    NSString *q = [NSString stringWithFormat:@"select route_color from routes where route_id=\"%@\"", routeName];
    
    FMResultSet *rs = [db executeQuery:q];
    if ([rs next]) {
        
        return [rs stringForColumn:@"route_color"];
        //[tempList addObject:[rs stringForColumn:@"route_long_name"]];
    }
    return nil;
    //  return tempList;
}


     *- (NSArray*) getShapesForRoute:(NSString*)routeID
{
    //select unique shape_id from trips where route_id = routeID -- should return 2 results...use the first
    //then select * from shapes where shape_id = ....
    //return NSArray with the "shape_pt_lat","shape_pt_lon"
    //use that to plot the overlay
    NSString* ID;
    NSMutableArray * results = [[NSMutableArray alloc]init];
    //NSLog(@"%@", routeID);
    NSString *q = [NSString stringWithFormat:@"select distinct shape_id from trips where route_id=\"%@\"", routeID];
    FMResultSet *rs = [db executeQuery:q];
    if ([rs next])
    {
        //just get the first one...
        ID = [NSString stringWithFormat:@"%@", [rs stringForColumn:@"shape_id"]];
        
    }
    
    if(ID!=nil)
    {
        NSString *q1 = [NSString stringWithFormat:@"select * from shapes where shape_id=\"%@\" order by shape_pt_sequence", ID];
        FMResultSet *rs1 = [db executeQuery:q1];
        NSMutableArray* sequence = [[NSMutableArray alloc] init];
        NSMutableDictionary* data = [[NSMutableDictionary alloc]init];
        NSNumberFormatter * f = [[NSNumberFormatter alloc] init];
        [f setNumberStyle:NSNumberFormatterDecimalStyle];
        while([rs1 next])
        {
         
           
            
            NSString* seqStr = [NSString stringWithFormat:@"%@", [rs1 stringForColumn:@"shape_pt_sequence"]];
             NSNumber * numStopSeq = [f numberFromString:seqStr];
            [sequence addObject:numStopSeq];//[rs1 stringForColumn:@"shape_pt_sequence"]];
//            NSLog(@"routeID=%@, pointSeq =%@", [rs1 stringForColumn:@"shape_id"], [rs1 stringForColumn:@"shape_pt_sequence"]);
           NSString* point = [NSString stringWithFormat:@"%@,%@", [rs1 stringForColumn:@"shape_pt_lat"], [rs1 stringForColumn:@"shape_pt_lon"]];
//            [results addObject:point];
            [data setObject:point forKey:numStopSeq];
        }

        
        
           NSArray* sort = [sequence sortedArrayUsingSelector:@selector(compare:)];
        
        for(NSNumber *s in sort)
        {
            NSString * dat = [data objectForKey:s];
            //NSString* d = [NSString stringWithFormat:@"%@", dat];
            //NSLog(@"sequence = %@",dat );
            [results addObject:dat];
        }
    }
    
    NSArray* res = [results copy];
    return res;
}

     * */
    public HashSet<String> getLowestStopOrderForStop(String routeID)
    {
    	String[] results = null;
    	String q1 = "select distinct s.stop_id, s.stop_name, st.stop_sequence " +
    			"from stops s inner join stoptimes st on s.stop_id=st.stop_id inner " +
    			"join trips t on st.trip_id=t.trip_id inner join routes r " +
    			"on r.route_id=\""+ routeID +"\" where t.route_id=\""+ routeID + "\" and " +
    			"t.direction_id =\"1\" ORDER BY st.stop_sequence";
    	    
    	HashSet<String> stopSet = new HashSet<String>();
    	Dictionary<String,String> dict1 = new Hashtable<String,String>();
    	Cursor c = null;
    	try
    	{
    		c = myDataBase.rawQuery(q1, null);
    		if (c != null ) {
    		    if  (c.moveToFirst()) {
    		          do {
    		        	  String stopName = c.getString(c.getColumnIndex("stop_name"));
    		        	  stopSet.add(stopName);
    		        	  dict1.put(c.getString(c.getColumnIndex("stop_name")), c.getString(c.getColumnIndex("stop_sequence")));
    		          }while(c.moveToNext());
    		    }
    		}
    		//HashSet<String>
    	}
    	catch(Exception e)
    	{
    		
    	}
    	finally
    	{
    		if(c!=null && !c.isClosed())
        		c.close();
    	}
 	
    	return stopSet;
    }
    
    /*
     *- (NSArray*) getLowestStopOrderForStop:(NSString*) routeID
{
    //https://github.com/reedlauber/Next-Septa/blob/master/app/models/simplified_stop.rb
    NSString* q1 = [NSString stringWithFormat:@"select distinct s.stop_id, s.stop_name, st.stop_sequence from stops s inner join stoptimes st on s.stop_id=st.stop_id inner join trips t on st.trip_id=t.trip_id inner join routes r on r.route_id=\"%@\" where t.route_id=\"%@\" and t.direction_id =\"1\" ORDER BY st.stop_sequence", routeID, routeID];
    
    NSMutableArray *results = [NSMutableArray array];
    
    FMResultSet *rs = [db executeQuery:q1];
    NSMutableDictionary* dict1 = [[NSMutableDictionary alloc]init];
    NSMutableSet * setStopNames = [[NSMutableSet alloc] init];
    while ([rs next])
    {
       //if([rs objectForColumnName:@"stop_name"]!=nil && [rs stringForColumn:@"stop_name"]!=@"" &&
        if([rs objectForColumnName:@"stop_name"] !=nil && ![[rs stringForColumn:@"stop_name"] isEqual:@""] &&
        ![setStopNames containsObject:[rs stringForColumn:@"stop_name"]])
        {
            [setStopNames addObject:[rs stringForColumn:@"stop_name"]];
            [results addObject:[rs resultDictionary]];
            //[dict1 setObject:[rs stringForColumn:@"stop_name"] forKey:[NSNumber numberWithInt:[rs intForColumn:@"stop_sequence"]]];
            [dict1 setObject:[NSNumber numberWithInt:[rs intForColumn:@"stop_sequence"]] forKey:[rs stringForColumn:@"stop_name"]];
        }
        
    }
    NSMutableArray* ar1 = [[NSMutableArray alloc]init];
    for(id ob in dict1)
    {

        [ar1 addObject:ob];
    }
    
    NSArray* sorted = [ar1 sortedArrayUsingSelector:@selector(compare:)];
    NSMutableArray* stopids = [[NSMutableArray alloc] init];
    for(id i in sorted)
    {
        [stopids addObject:i];//[dict1 objectForKey:i]];
       // NSLog(@"Stop ---- %@", i);//[dict1 objectForKey:i]);

    }
    return stopids;
    }
     * */
    //method for getting list of stops for a route
    public HashSet<String> getAllStationNames()
    {
    	//Log.d("GetLowestStopOrder", "Top");
    	HashSet<String> result = new HashSet<String>();
    	//String[] results = null;

//    	String query = "select distinct s.stop_id, s.stop_name, st.stop_sequence from stops s " +
//    			"inner join stoptimes st on s.stop_id=st.stop_id inner join trips t on st.trip_id=t.trip_id " +
//    			"inner join routes r on r.route_id=\""+ routeID + "\""+
//    					"where t.route_id=\""+ routeID + "\"and t.direction_id =\"1\" " +
//    			"ORDER BY st.stop_sequence";
    	//Log.d("DBHelper", "getLowest... isOpen:  " + myDataBase.isOpen());
    	String query = "select distinct stop_name from stops";
    	//Log.d("getLowestQuery", query);
    	//myDataBase = SQLiteDatabase.openDatabase(path, factory, flags)
    	Cursor c = null;
    	try
    	{
    	c = myDataBase.rawQuery(query, null);
    	//Log.d("getLowestQuery","got a cursor");
    	
		if (c != null ) {
		    if  (c.moveToFirst()) {
		          do {
		        	  String stopName = c.getString(c.getColumnIndex("stop_name"));
		        	  //Log.d("getLowestQuery", stopName);
		        	  result.add(stopName);
		          }while (c.moveToNext());
		    } 
		}
    	}
    	catch(Exception e)
    	{
    		//Log.d("Select", e.getMessage());
    	}
    	finally
    	{
    		if(c!=null && !c.isClosed())
        		c.close();
    	}
    	

    	//Log.d("Select", "Made it to end");
    	return result;
    }
    
    
    @Override
    public void onCreate(SQLiteDatabase db) {
       // Log.v("OnCreate","On create Called:"+db.getPath());
    }
    
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	}



