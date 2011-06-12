 package com.risotto.view.wizard;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.risotto.R;

public class HowOftenSchedule extends Activity implements OnClickListener,TimePickerDialog.OnTimeSetListener {
	
	public static final String LOG_TAG = "com.risotto.view.wizard.HowOftenSchedule";
	
	private static final int ADD_TIME_BUTTON_INDEX = 0;
	private static final int TIME_DISPLAY_INDEX = 1;
	private static final int AM_PM_INDEX = 2;
	
	static final int TIME_DIALOG_ID = 0;
	
	private int setHour;
	private int setMinute;
	private int rowSelected;
	
	private LinearLayout container;
	
	private RadioButton am,pm;
	
	private static int numTimerRows = 0;
	
	private static final int ROW_NUMBER = 1;
	
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
		
		//Intent intent = getIntent();

		setContentView(R.layout.wizard_how_often_schedule);
		
		//set listener for adding time
		Button addTimeSlot = (Button)findViewById(R.id.wizard_how_often_schedule_add_time_button);
		addTimeSlot.setOnClickListener(this);
		
		addTimerRow();
	}

	public void onClick(View v) {
		LinearLayout timerRow = (LinearLayout)v.getParent();
		switch(v.getId()) {
			case R.id.wizard_how_often_schedule_time_display:
				Log.d(LOG_TAG,"display time picker");
				rowSelected = (Integer)timerRow.getTag();
				Log.d(LOG_TAG,"row: " + rowSelected);
				showDialog(TIME_DIALOG_ID);
				break;
			case R.id.wizard_how_often_schedule_am:
				Log.d(LOG_TAG,"am picked");
				rowSelected = (Integer)timerRow.getTag();
				Log.d(LOG_TAG,"row: " + rowSelected);
				break;
			case R.id.wizard_how_often_schedule_pm:
				Log.d(LOG_TAG,"PM picked.");
				rowSelected = (Integer)timerRow.getTag();
				Log.d(LOG_TAG,"row: " + rowSelected);
				break;
			case R.id.wizard_how_often_schedule_add_time_button:
				Log.d(LOG_TAG,"Adding timer.");
				addTimerRow();
				break;
			case R.id.wizard_how_often_schedule_remove_time_button:
				Log.d(LOG_TAG,"Removing timer.");
				rowSelected = (Integer)timerRow.getTag();
				Log.d(LOG_TAG,"row: " + rowSelected);
				removeTimerRow();
			default:
				
		}
	}
	
	
	/**
	 * Logic for adding another timer row to the view
	 */
	private void addTimerRow() {
		//get the layout for the second half of the screen in this activity
		container = (LinearLayout)findViewById(R.id.wizard_how_often_schedule_timer_container);
		
		//read the timer row layout, which is a LinearLayout containing a LinearLayout 
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout row = (LinearLayout)inflater.inflate(R.layout.wizard_how_often_schedule_timer_row, null);
		
		//add the row - which is one view group specified in the wizard_how_often_schedule_timer_rows file
		container.addView(row);
		numTimerRows++;
		
		Log.d(LOG_TAG,"number of children for container: " + container.getChildCount());
		
		//get the newly added row 
		LinearLayout newTimerRow = (LinearLayout)container.getChildAt(numTimerRows);
		
		//set Hint & listener for time display
		TextView time = (TextView)newTimerRow.getChildAt(TIME_DISPLAY_INDEX);
		time.setHint("Click here...");
		time.setOnClickListener(this);
		
		//set listener for removing time
		Button removeTimeSlot = (Button)newTimerRow.getChildAt(ADD_TIME_BUTTON_INDEX);
		removeTimeSlot.setOnClickListener(this);
		
		//set listener for setting AM / PM
		RadioGroup rg = (RadioGroup)newTimerRow.getChildAt(AM_PM_INDEX);
		rg.setOnClickListener(this);
		
		//tag the setTimeRow with the row number so we know where to update the display
		newTimerRow.setTag(numTimerRows);
		Log.d(LOG_TAG,"numTimerRows: " + numTimerRows);
		Log.d(LOG_TAG,"addTimerRow: specRow tag: " + newTimerRow.getTag());

	}
	
	private void removeTimerRow() {
		Log.d(LOG_TAG,"Removing timer from row: " + rowSelected);
		container.removeViewAt(rowSelected);
		numTimerRows--;
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
    	
    	LinearLayout row = (LinearLayout)container.getChildAt(rowSelected);
    	
    	TextView time = (TextView)row.getChildAt(TIME_DISPLAY_INDEX);
    	RadioGroup rg = (RadioGroup)row.getChildAt(AM_PM_INDEX);
		am = (RadioButton)rg.getChildAt(0);
		pm = (RadioButton)rg.getChildAt(1);
    	
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
  
		time.setText(
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
