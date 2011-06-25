 package com.risotto.view.wizard;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.risotto.R;

public class HowOftenSchedule extends ListActivity implements OnClickListener,TimePickerDialog.OnTimeSetListener,SimpleAdapter.ViewBinder {
	
	public static final String LOG_TAG = "com.risotto.view.wizard.HowOftenSchedule";
	
	private static final int REMOVE_TIME_INDEX = 0;
	private static final int TIME_DISPLAY_INDEX = 1;
	private static final int AM_PM_INDEX = 2;
	
	private static final int AM_INDEX = 0;
	private static final int PM_INDEX = 1;
	
	static final int TIME_DIALOG_ID = 0;
	
	private int setHour;
	private int setMinute;
	
	private LinearLayout container;
	private HashMap<String,Object> wizardData = new HashMap<String,Object>();
	private RadioButton am,pm;
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
		
		//Intent intent = getIntent();

		setContentView(R.layout.wizard_how_often_schedule);
		try {
			  this.wizardData = WizardData.getData(getIntent().getExtras());
		  } catch (Exception e) {
			  Log.d(LOG_TAG,"No data found in intent.");
		}
		//set listener for adding time
		Button addTimeSlot = (Button)findViewById(R.id.wizard_how_often_schedule_add_time_button);
		addTimeSlot.setOnClickListener(this);
		
		Button scheduleIt = (Button)findViewById(R.id.wizard_how_often_schedule_schedule_it_button);
		scheduleIt.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.wizard_how_often_schedule_time_display:
				Log.d(LOG_TAG,"display time picker");
				container = (LinearLayout)v.getParent();
				showDialog(TIME_DIALOG_ID);
				break;
			case R.id.wizard_how_often_schedule_add_time_button:
				Log.d(LOG_TAG,"Adding timer.");
				addTimerRow();
				break;
			case R.id.button_wizard_how_often_schedule_remove_time:
				Log.d(LOG_TAG,"Removing timer.");
				LinearLayout timeRow = (LinearLayout)v.getParent();
				getListView().removeFooterView(timeRow);
				break;
			case R.id.wizard_how_often_schedule_schedule_it_button:
				Log.d(LOG_TAG,"go to review of schedule");
				Vector<Calendar> listOfTimes = getTimes();
				if(null == listOfTimes)
					break;
				
				/*Iterator<Calendar> it = listOfTimes.iterator();
				Calendar time;
				String AM_PM;
				int hour;
				while(it.hasNext()) {
					time = it.next();
					//Log.d(LOG_TAG,"am: " + time.get(Calendar.AM_PM));
					if(time.get(Calendar.AM_PM) == Calendar.AM) {
						AM_PM = "AM";
						//Log.d(LOG_TAG,"set am in list iteration");
					}
					else {
						AM_PM = "PM";
						//Log.d(LOG_TAG,"set pm in list iteration");
					}
					
					
					if(time.get(Calendar.HOUR_OF_DAY) == 0) 
						hour = 12;
					else if(time.get(Calendar.HOUR_OF_DAY) <= 12)
						hour = time.get(Calendar.HOUR_OF_DAY);
					else
						hour = (time.get(Calendar.HOUR_OF_DAY) - 12);
					
					Log.d(LOG_TAG,"Time: " + hour +
							":" + time.get(Calendar.MINUTE) + " " + AM_PM);
				}*/
				
				Intent intent = new Intent();
				Log.d(LOG_TAG,"listOfTimes: " + listOfTimes.getClass());
				
				//list of times goes into hashmap as a vector
				wizardData.put(WizardData.TIMES, listOfTimes);
				intent.putExtra(WizardData.CONTENTS, wizardData);
				intent.setClass(getApplicationContext(),ScheduleReview.class);
				startActivity(intent);
				break;
			default:
				
		}
	}
	
	private Vector<Calendar> getTimes() {
		Vector<Calendar> times = new Vector<Calendar>();
		
		ListView list = getListView();
		LinearLayout row;
		int count = list.getChildCount();
		
		Log.d(LOG_TAG,"count: " + count);
		int counter = 0;
		
		while(counter < count) {
			row = (LinearLayout) list.getChildAt(counter);
			//get the time as a string
			String time = ((TextView)row.getChildAt(TIME_DISPLAY_INDEX)).getText().toString();
			Resources res = getApplicationContext().getResources();
			
			if(time.equalsIgnoreCase(res.getString(R.string.wizard_how_often_schedule_hint_time_pick))) {
				new AlertDialog.Builder(this)
			    .setTitle("Time not entered")
			    .setMessage("Oops - you need to enter a time for all available slots.  If you have too many, just delete " +
			    		"the row by pressing the trash can!")
			    //don't do anything when the button is clicked
			    .setPositiveButton("Okay", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) {} })
			    .show();
				
				return null;
			}
			
			Log.d(LOG_TAG,"time: " + time);
			
			//get the radio group, then the AM button and see if it's checked
			boolean isAM = ((RadioButton)((RadioGroup)row.getChildAt(AM_PM_INDEX)).getChildAt(AM_INDEX)).isChecked();
			//time is following format --> hh:mm,
			//get the hour
			int hour = Integer.valueOf(time.split(":")[0]);
			int minute = Integer.valueOf(time.split(":")[1]);
			
			Calendar timer = Calendar.getInstance();
			
			Log.d(LOG_TAG,"hour: " + hour);
			Log.d(LOG_TAG,"minute: " + minute);
			Log.d(LOG_TAG,"isAM : " + isAM);
			
			//Calendar object is awful, the developer should be forced to write 
			//assembly for the rest of his god-forsaken life
			//tried to not use hour_of_day, but the damn object would keep switching AM / PM for no
			//good reason. asshat.
			
			//following values are set:
			//12AM = 0
			// 1 - 11 AM = 1-11
			// 12 - 11 PM = 12 - 23
			
			if(isAM) {
				if(hour == 12)
					timer.set(Calendar.HOUR_OF_DAY, 0);
				else
					timer.set(Calendar.HOUR_OF_DAY,hour);
			} else {
				if(hour == 12)
					timer.set(Calendar.HOUR_OF_DAY,12);
				else
					timer.set(Calendar.HOUR_OF_DAY, hour + 12);
			}
			timer.set(Calendar.MINUTE, minute);
			
			//Log.d(LOG_TAG,"am_pm: " + timer.get(Calendar.AM_PM));
			//Log.d(LOG_TAG,"in getTimes: time (AM_PM): " + timer.get(Calendar.AM_PM));
			
			times.add(timer);
			counter++;
		}
		
		return times;
	}
	
	/**
	 * Logic for adding another timer row to the view
	 */
	private void addTimerRow() {
		//timerRow = new HashMap<String,Object>();
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		container = (LinearLayout)inflater.inflate(R.layout.wizard_how_often_schedule_timer_row, null);
		Button deleteTime = (Button)container.getChildAt(REMOVE_TIME_INDEX);
		deleteTime.setOnClickListener(this);
		
		TextView timer = (TextView)container.getChildAt(TIME_DISPLAY_INDEX);
		timer.setOnClickListener(this);
		
		RadioGroup group = (RadioGroup)container.getChildAt(AM_PM_INDEX);
		group.setOnClickListener(this);
		
		/*timerRow.put(REMOVE_TIME_FIELD, deleteTime);
		timerRow.put(TIME_FIELD, timer);
		timerRow.put(AM_PM_FIELD, group);
		
		timerList.add(timerRow);*/
		
		getListView().addFooterView(container);
		
		Log.d(LOG_TAG,"Child count: " + getListView().getChildCount());
		
		onContentChanged();
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
    	
    	Log.d(LOG_TAG,"updateDisplay");
    	
    	TextView time = (TextView)container.getChildAt(TIME_DISPLAY_INDEX);
    	RadioGroup rg = (RadioGroup)container.getChildAt(AM_PM_INDEX);
		am = (RadioButton)rg.getChildAt(AM_INDEX);
		pm = (RadioButton)rg.getChildAt(PM_INDEX);
    	
    	if(setHour < 12) {
    		if(setHour == 0)
    			adjustHour = 12;
    		else 
	    		adjustHour = setHour;
    		
    		am.setChecked(true);
    	} 
    	else {
    		if(setHour == 12)
    			adjustHour = 12;
    		else
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

	public boolean setViewValue(View view, Object data,	String textRepresentation) {
		Log.d(LOG_TAG,"setViewValue: " + view.getContentDescription());
		Log.d(LOG_TAG,"setViewValue: " + data.toString());
		return true;
	}

}
