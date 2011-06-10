package com.risotto.view.wizard;

import com.risotto.R;
import com.risotto.model.Prescription;
import com.risotto.storage.StorageProvider;
import com.risotto.view.prescription.PrescriptionView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * 
 *  
 * @author nick
 *
 */
public class WhenTakeIt extends Activity implements OnClickListener {

	//the name of these strings will correspond to the answer that was clicked in this UI
	//they will reference the activity that is to be started next based on the answer
	public static final String WIZARD_SPEC_TIMES = "com.risotto.view.wizard.TakeItEveryDay";
	public static final String WIZARD_SPEC_INTERVAL = "com.risotto.view.wizard.TakeItEveryInterval";
	
	public static final String LOG_TAG = "com.risotto.view.wizard.WhenTakeIt";
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
		
		Intent intent = getIntent();

		setContentView(R.layout.wizard_when_take_it);
		
		Button specTime = (Button) this.findViewById(R.id.button_wizard_choice_spec_time);
		specTime.setOnClickListener(this);
		
		Button specInt = (Button) this.findViewById(R.id.button_wizard_choice_spec_interval);
		specInt.setOnClickListener(this);
		
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * 
	 * Method called when button is clicked.
	 * 
	 */
	public void onClick(View v) {
		Intent nextQuestion = new Intent();
		switch(v.getId()) {
			case R.id.button_wizard_choice_spec_time:
				Log.d(LOG_TAG,"spec_time");
				//launch will you take it every day
				nextQuestion.setAction(WhenTakeIt.WIZARD_SPEC_TIMES);
		    	nextQuestion.setClass(getApplicationContext(), TakeItEveryDay.class);
				startActivity(nextQuestion);
				break;
			case R.id.button_wizard_choice_spec_interval:
				Log.d(LOG_TAG,"spec_interval");
				//launch how often will you take this
				break;
			
		}
		/*Prescription newPrep = new Prescription(patient,drug,Prescription.DOSE_TYPE_EVERY_DAY);
		ContentValues cv = newPrep.toContentValues(getApplicationContext());
		Uri prescriptionUri = this.getContentResolver().insert(StorageProvider.PrescriptionColumns.CONTENT_URI, cv);
		Log.d(LOG_TAG,"finished adding drug; uri = " + prescriptionUri);
		finish();*/
	}
	
}
