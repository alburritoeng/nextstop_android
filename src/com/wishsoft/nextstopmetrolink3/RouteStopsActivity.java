package com.wishsoft.nextstopmetrolink3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class RouteStopsActivity extends Activity implements OnItemClickListener{

	private RouteDetailsClass[] details;
	private NextStopObject objectPassedIn;
	private ListView listView1;
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.routesactivity);
	        //setTitle("Trains");
	        
	        AppData app = (AppData)this.getApplicationContext();
	        
	        
	        Bundle b = getIntent().getExtras();
		    objectPassedIn = b.getParcelable("data");
		    String routeLetters = app.getShortToRouteId().get(objectPassedIn.StopID());
		    details = app.getDB().getRouteDetailsForRoute(objectPassedIn.StopID());

		    setTitle(routeLetters + " " + objectPassedIn.StopID());
		    //Log.d("RouteStopsActivity", "OnCreate");
		    //as of 9/12/13 - I have the route info...just add it to the listview...you're done
		    RoutesStopsDetails stops_list[] = new RoutesStopsDetails[details.length];
		    for(int c = 0; c < details.length; c++)
		    {
		    	stops_list[c] = new RoutesStopsDetails(details[c]);
		    }

	        RoutesStopsAdapter adapter = new RoutesStopsAdapter(this, R.layout.routestopdetails, stops_list);
	        listView1 = (ListView)findViewById(R.id.listView1);

	        listView1.setAdapter(adapter);
	        listView1.setDividerHeight(1);	        
	 }
	 

	 
	 protected void onListItemClick(AdapterView<?> parent, View view, int position, long id) 
	 {
		 //Log.d("OnlistenItemClick", "Click");
	
		 
	 }



	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	 
}
