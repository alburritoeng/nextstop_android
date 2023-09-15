package com.wishsoft.nextstopmetrolink3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RoutesActivity extends BaseActivity {

    private ListView listView1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routesactivity);
		setTitle("Metrolink Lines");
		RouteDetails route_data[] = new RouteDetails[]
	    {
	    	new RouteDetails("#00A0C4","91 Line"),
	    	new RouteDetails("#009987","Antelope Valley Line"),
	       // new RouteDetails("#F9D616","Burbank-Bob Hope Airport"),
	        new RouteDetails("#CE007C","Inland Emp.-Orange Co. Line"),
	        new RouteDetails("#E66A1F","Orange County Line"),
	        new RouteDetails("#6D28AA","Riverside Line"),
	        new RouteDetails("#AF1E2D","San Bernardino Line"),
	        new RouteDetails("#F9D616","Ventura County Line")//,
	    	//new RouteDetails("#3A5FCD","All Routes Map")
	    };
		
		RoutesAdapter adapter = new RoutesAdapter(this, 
	                R.layout.routeslistview_item_row, route_data);
	        
	    
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
        listView1.setDividerHeight(0);
        
        
	}
   
	protected void onListItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		AppData app = (AppData) this.getApplicationContext();
	
    	RouteDetails rd = (RouteDetails)listView1.getItemAtPosition(position);
        showToast(rd.routeName);
        
		Intent myIntent = new Intent(this, TripsForRouteActivity.class);
		myIntent.putExtra("RouteID", rd.routeName);
		startActivity(myIntent);
		
	}

}
