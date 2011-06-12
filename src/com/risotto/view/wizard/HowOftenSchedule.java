 package com.risotto.view.wizard;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

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
	
	private static final int AM_INDEX = 0;
	private static final int PM_INDEX = 1;
	
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
		
		Button scheduleIt = (Button)findViewById(R.id.wizard_how_often_schedule_schedule_it_button);
		scheduleIt.setOnClickListener(this);
		
		addTimerRow();
	}

	public void onClick(View v) {
		LinearLayout timerRow = (LinearLayout)v.getParent();
		switch(v.getId()) {
			case R.id.wizard_how_often_schedule_time_display:
				Log.d(LOG_TAG,"display time picker");
				rowSelected = (Integer)timerRow.getTag();
				Log.d(LOG_TAG,"row selected: " + rowSelected);
				showDialog(TIME_DIALOG_ID);
				break;
			case R.id.wizard_how_often_schedule_am:
				Log.d(LOG_TAG,"am picked");
				rowSelected = (Integer)timerRow.getTag();
				Log.d(LOG_TAG,"row selected: " + rowSelected);
				break;
			case R.id.wizard_how_often_schedule_pm:
				Log.d(LOG_TAG,"PM picked.");
				rowSelected = (Integer)timerRow.getTag();
				Log.d(LOG_TAG,"row selected: " + rowSelected);
				break;
			case R.id.wizard_how_often_schedule_add_time_button:
				Log.d(LOG_TAG,"Adding timer.");
				addTimerRow();
				break;
			case R.id.wizard_how_often_schedule_remove_time_button:
				Log.d(LOG_TAG,"Removing timer.");
				rowSelected = (Integer)timerRow.getTag();
				Log.d(LOG_TAG,"row selected: " + rowSelected);
				removeTimerRow();
				break;
			case R.id.wizard_how_often_schedule_schedule_it_button:
				Log.d(LOG_TAG,"go to review of schedule");
				Iterator<Calendar> it = getTimes().iterator();
				Calendar time;
				String AM_PM;
				int hour;
				while(it.hasNext()) {
					time = it.next();
					if(time.get(Calendar.AM_PM) == Calendar.AM)
						AM_PM = "AM";
					else
						AM_PM = "PM";
					
					if(time.get(Calendar.HOUR) == 0) 
						hour = 12;
					else
						hour = time.get(Calendar.HOUR);
					Log.d(LOG_TAG,"Time: " + hour +
							":" + time.get(Calendar.MINUTE) + " " + AM_PM);
				}
				//pull all schedule info out of view & add it to intent
				break;
			default:
				
		}
	}
	
	private Vector<Calendar> getTimes() {
		Vector<Calendar> times = new Vector<Calendar>();
		
		while(numTimerRows > 0) {
			
			LinearLayout row = (LinearLayout)container.getChildAt(numTimerRows);
			
			//get the time as a string
			String time = ((TextView)row.getChildAt(TIME_DISPLAY_INDEX)).getText().toString();
			Log.d(LOG_TAG,"time: " + time);
			
			//get the radio group, then the AM button and see if it's checked
			boolean isAM = ((RadioButton)((RadioGroup)row.getChildAt(AM_PM_INDEX)).getChildAt(AM_INDEX)).isChecked();
			
			//time is following format --> hh:mm,
			//get the hour
			int hour = Integer.valueOf(time.split(":")[0]);
			int minute = Integer.valueOf(time.split(":")[1]);
			
			Calendar timer = Calendar.getInstance();
			if(isAM) {
				timer.set(Calendar.AM_PM, Calendar.AM);
				Log.d(LOG_TAG,"set AM");
			}
			else {
				timer.set(Calendar.AM_PM, Calendar.PM);
				Log.d(LOG_TAG,"set PM");
			}
			
			timer.set(Calendar.HOUR, hour);
			timer.set(Calendar.MINUTE, minute);
			
			times.add(timer);
			
			numTimerRows--;
		}
		return times;
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
		
		//increment the number of rows in the view
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
		
		//tag the newTimerRow with the row number so we know where to update the display
		newTimerRow.setTag(numTimerRows);
		Log.d(LOG_TAG,"numTimerRows: " + numTimerRows);
		Log.d(LOG_TAG,"addTimerRow: specRow tag: " + newTimerRow.getTag());

	}
	
	/**
	 * Removes row from UI, readjusts the tag for all rows following the row removed
	 * to keep sync
	 */
	private void removeTimerRow() {
		Log.d(LOG_TAG,"Removing timer from row: " + rowSelected);
		int rowNum = rowSelected;
		container.removeViewAt(rowSelected);
		numTimerRows--;
		while(rowNum <= numTimerRows){
			int oldTag = (Integer)container.getChildAt(rowNum).getTag();
			Log.d(LOG_TAG,"changing tag for rowNum: " + rowNum);
			Log.d(LOG_TAG,"oldTag: " + oldTag);
			Log.d(LOG_TAG,"newTag: " + (oldTag - 1));
			container.getChildAt(rowNum).setTag(oldTag - 1);
			rowNum++;
		}
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
    	
    	Log.d(LOG_TAG,"updateDisplay for row: " + rowSelected);
    	
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
