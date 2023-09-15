package com.wishsoft.nextstopmetrolink3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class CalendarDays extends Activity implements OnClickListener{

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.daysofweek);

	    Button mon = (Button) findViewById(R.id.mondaybtn);
	    Button tue = (Button) findViewById(R.id.tuesdaybtn);
	    Button wed = (Button) findViewById(R.id.wednesdaybtn);
	    Button thurs = (Button) findViewById(R.id.thursdaybtn);
	    Button fri = (Button) findViewById(R.id.friday);
	    Button sat = (Button) findViewById(R.id.saturday);
	    Button sun = (Button) findViewById(R.id.sunday);
	    
	    mon.setOnClickListener(this);
	    tue.setOnClickListener(this);
	    wed.setOnClickListener(this);
	    thurs.setOnClickListener(this);
	    fri.setOnClickListener(this);
	    sat.setOnClickListener(this);
	    sun.setOnClickListener(this);
	    
	    SetColors(mon);
	    SetColors(tue);
	    SetColors(wed);
	    SetColors(thurs);
	    SetColors(fri);
	    SetColors(sat);
	    SetColors(sun);
	    
	    
	    
	}

	private void SetColors(Button btn)
	{
		 btn.setBackgroundColor(Color.BLACK);
		 btn.setTextColor(Color.WHITE);
	}
	@Override
	public void onClick(View v) {
		Intent ret = new Intent();
		switch (v.getId()) {
		case R.id.sunday:
	    	ret.putExtra("day", 1);
	        break;
	    case R.id.mondaybtn:
	    	ret.putExtra("day", 2);
	        break;
	    case R.id.tuesdaybtn:
	    	ret.putExtra("day", 3);
	       break;
	    case R.id.wednesdaybtn:
	    	ret.putExtra("day", 4);
	       break;
	    case R.id.thursdaybtn:
	    	ret.putExtra("day", 5);
	       break;
	    case R.id.friday:
	    	ret.putExtra("day", 6);
	       break;
	    case R.id.saturday:
	    	ret.putExtra("day", 7);
	       break;
	    default:
	    	ret.putExtra("day", 2);	//default week-day
	    	
	    }   
		this.setResult(RESULT_OK, ret);
        finish();
        return;
		
	}
}
