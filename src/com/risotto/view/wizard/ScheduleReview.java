package com.risotto.view.wizard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.risotto.R;
import com.risotto.model.Drug;
import com.risotto.model.Patient;
import com.risotto.model.Prescription;

public class ScheduleReview extends Activity implements OnClickListener {
	
	public static final String LOG_TAG = "com.risotto.view.wizard.ScheduleReview";
	
	private HashMap<String,Object> wizardData = new HashMap<String,Object>();
	private Drug drug;
	private Patient patient;
	private Prescription prep;
	private ArrayList<Calendar> times;
	
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.wizard_schedule_review);
		
		try {
			  this.wizardData = WizardData.getData(getIntent().getExtras());
		} catch (Exception e) {
			  Log.d(LOG_TAG,"No data found in intent.");
		}
		
		//TIMES COMES OUT AS AN ARRAYLIST
		//WHAT THE FUCK IS GOING ON?
		
		try {
			drug = (Drug) wizardData.get(WizardData.DRUG);
			patient = (Patient) wizardData.get(WizardData.PATIENT);		
			times = (ArrayList<Calendar>) wizardData.get(WizardData.TIMES);
			prep = (Prescription) wizardData.get(WizardData.PRESCRIPTION);
		} catch (ClassCastException e) {
			e.printStackTrace();
			
		}
		Log.d(LOG_TAG,"class: " + wizardData.get(WizardData.TIMES).getClass());
		
		TextView who = (TextView) this.findViewById(R.id.wizard_schedule_review_who);
		who.setText(patient.getFirstName() + patient.getLastName());
		
		TextView what = (TextView) this.findViewById(R.id.wizard_schedule_review_what);
		what.setText(drug.getBrandName());
		
		TextView when = (TextView) this.findViewById(R.id.wizard_schedule_review_when);
		Iterator<Calendar> iterator = times.iterator();
		Calendar time;
		String list = "";
		String AM_PM = "";
		int hour;
		while(iterator.hasNext()) {
			time = iterator.next();
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
			
			list =  list + hour + ":" + pad(time.get(Calendar.MINUTE)) + " " + AM_PM + ",";
		}
		when.setText(list);	
		
		Button schedule = (Button) this.findViewById(R.id.button_wizard_schedule_review_schedule);
		schedule.setOnClickListener(this);
	}
	
	//Adds a 0 into the tens place if it would otherwise be empty
    private static String pad(int c) {
    	if (c >= 10)
    		return String.valueOf(c);
    	else
    		return "0" + String.valueOf(c);
    }

	public void onClick(View v) {
		
	}

}
