 package com.risotto.view.wizard;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.risotto.R;

public class HowOftenSchedule extends Activity implements OnClickListener,TimePickerDialog.OnTimeSetListener {
	
	public static final String LOG_TAG = "com.risotto.view.wizard.HowOftenSchedule";
	
	static final int TIME_DIALOG_ID = 0;
	
	private int setHour;
	private int setMinute;
	
	private TextView setTime;
	private RadioButton am,pm;
	
	private int numTimerRows = 0;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
		
		Intent intent = getIntent();

		setContentView(R.layout.wizard_how_often_schedule);
		
		/*setTime = (TextView) this.findViewById(R.id.wizard_how_often_schedule_hour_display);
		setTime.setHint(R.string.wizard_how_often_schedule_hint_time_pick);
		setTime.setOnClickListener(this);
		
		am = (RadioButton) this.findViewById(R.id.wizard_how_often_schedule_am);
		am.setOnClickListener(this);
		
		pm = (RadioButton) this.findViewById(R.id.wizard_how_often_schedule_pm);
		pm.setOnClickListener(this);
		
		Button addTime = (Button) this.findViewById(R.id.wizard_how_often_schedule_add_time_button);
		addTime.setOnClickListener(this);*/
		
		addTimerRow();
		addTimerRow();
		
	}

	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.wizard_how_often_schedule_time_display:
				Log.d(LOG_TAG,"display time picker");
				showDialog(TIME_DIALOG_ID);
				break;
			case R.id.wizard_how_often_schedule_am:
				Log.d(LOG_TAG,"am picked");
				break;
			case R.id.wizard_how_often_schedule_pm:
				Log.d(LOG_TAG,"PM picked.");
				break;
			case R.id.wizard_how_often_schedule_add_time_button:
				Log.d(LOG_TAG,"Add time.");
				addTimerRow();
				break;
			default:
				
		}
	}
	
	
	/**
	 * Logic for adding another timer row to the view
	 */
	private void addTimerRow() {
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		LinearLayout container = (LinearLayout)findViewById(R.id.wizard_how_often_schedule_timer_container);
		
		LinearLayout row = (LinearLayout)inflater.inflate(R.layout.wizard_how_often_schedule_timer_row, container);
		
		TextView time = (TextView)row.findViewById(R.id.wizard_how_often_schedule_time_display);
		time.setHint("Click here...");
		time.setOnClickListener(this);
		
		/*
		 * Bookmark:
		 * 	- multiple rows are displaying okay, but i've encountered an issue, the same id
		 * is used everytime a row is added, (time.getId()) prints the same thing every time, so
		 * I need a new way to distinguish them.
		 */
		
		Button addTimeSlot = (Button)row.findViewById(R.id.wizard_how_often_schedule_add_time_button);
		addTimeSlot.setOnClickListener(this);
		
		Log.d(LOG_TAG,"addTimerRow: set hint for view id: " + time.getId());
		Log.d(LOG_TAG,"row atts: " + row.getChildCount());
		
		
		
		
		//container.addView(row);
		//container.refreshDrawableState();
	}
	
	//Callback method executed when the user presses okay after
	//entering their time
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		Log.d(LOG_TAG,"hourOfDay: " + hourOfDay);
		setHour = hourOfDay;
		setMinute = minute;
		updateDisplay();
	}

    /**
     * Updates the view to display the time in 12 hour format.
     * Also sets the correct radio button for AM/PM
     */
    private void updateDisplay() {
    	int adjustHour;
    	if(setHour <= 12) {
    		if(setHour == 0) {
    			adjustHour = 12;
    			am.setChecked(true);
    		} else if(setHour == 12) {
    			adjustHour = setHour;
    			pm.setChecked(true);
    		} else {
	    		adjustHour = setHour;
	    		am.setChecked(true);
    		}
    	} 
    	else {
    		adjustHour = setHour - 12;
    		pm.setChecked(true);
    	}
  
		setTime.setText(
			new StringBuilder()
			.append(pad(adjustHour)).append(":")
			.append(pad(setMinute)));
    }

    //Adds a 0 into the tens place if it would otherwise be empty
    private static String pad(int c) {
    	if (c >= 10)
    		return String.valueOf(c);
    	else
    		return "0" + String.valueOf(c);
    }
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    	case TIME_DIALOG_ID:
	    		return new TimePickerDialog(this, this, setHour, setMinute, false);
	    }
	    return null;
	}

}
