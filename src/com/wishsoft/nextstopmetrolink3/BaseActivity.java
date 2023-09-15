package com.wishsoft.nextstopmetrolink3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
 
public class BaseActivity extends Activity 
{
	DBHelper db;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		
		try
	      {
			  AppData app = (AppData)this.getApplicationContext();
	    	  db = app.getDB();       
	      }
	      catch(Exception e)
	      {
	      	System.out.println("DB...issue...Something went wrong \n" + e.getMessage());
	      }
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.routes, menu);
      
      
      try
      {
    	  db = new DBHelper(this);
    	  db.createDataBase();
    	 
      }
      catch(Exception e)
      {
      	System.out.println("DB...issue...Something went wrong \n" + e.getMessage());
      }
     //return true; 
      return super.onCreateOptionsMenu(menu);
    }
    
	 // Called when an options item is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId()) 
    	{
    	
    	case R.id.lines:
    	  //if(this.getClass()!=RoutesActivity.class)
    	  //startActivity(new Intent(this, RoutesActivity.class));
    	  if(this.getClass()!=RoutesActivity.class)
    	  {
    		  Intent intent1 = new Intent(this, RoutesActivity.class);
    		  intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    		  startActivity(intent1);
    	  }
        break;
    	case R.id.near:
        //stopService(new Intent(this, UpdaterService.class));
    		  //if(this.getClass()!=NearLocationMapActivity.class)
        	  {
        		  Intent intent1 = new Intent(this, NearLocationMapActivity.class);
        		  intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        		  startActivity(intent1);
        	  }
        break;
      case R.id.alerts:
      {
		  Intent intent1 = new Intent(this, AlertsActivity.class);
		  intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		  startActivity(intent1);
	  }
        break;
      case R.id.stations:
    	  if(this.getClass()!=StationActivity.class)
    	  {
    		  Intent intent1 = new Intent(this, StationActivity.class);
    		  intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    		  startActivity(intent1);
    	  }
          break;
      }

      return true;
    }

    public void showToast(Object object)
    {
    	Context context = getApplicationContext();
		CharSequence text = object.toString();//"Hello toast!";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
    	
    }
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long arg) {
		// TODO Auto-generated method stub
		
	}

}
