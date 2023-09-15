package com.wishsoft.nextstopmetrolink3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class StationActivity extends BaseActivity implements
		OnItemClickListener {

	HashSet<String> stationsSet = new HashSet<String>();

	private ListView listView1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stations);

		stationsSet = db.getAllStationNames();
		StationDetails station_list[] = new StationDetails[stationsSet.size()];

		ArrayList<String> list = new ArrayList<String>(stationsSet);
		Collections.sort(list);
		for (int c = 0; c < list.size(); c++) {
			station_list[c] = new StationDetails(list.get(c));
		}

		StationAdapter adapter = new StationAdapter(this,
				R.layout.stationslistview_item, station_list);

		listView1 = (ListView) findViewById(R.id.listView2);
		listView1.setOnItemClickListener(this);
		listView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onListItemClick(parent, view, position, id);

			}
		});

		listView1.setAdapter(adapter);
		listView1.setDividerHeight(1);
	}

	protected void onListItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		StationDetails rd = (StationDetails) listView1.getItemAtPosition(position);
		
		
		AppData app = (AppData) this.getApplicationContext();
		// stopID is a number
		// routeID is the station name "Buena Park Metrolink Station"
		String stopID = app.getStopID(rd.stationName);
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, String> possibleStops = db.getStopsForStopId(stopID, false);
		Intent myIntent = new Intent(this, TripsActivity.class);

		NextStopObject data = new NextStopObject();
		data.SetNote("");
		data.SetStopID(stopID);
		data.SetTrains(possibleStops);
		data.SetTitle("");
		data.setRouteID(rd.stationName);
		myIntent.putExtra("data", data);
		startActivity(myIntent);

	}

}
