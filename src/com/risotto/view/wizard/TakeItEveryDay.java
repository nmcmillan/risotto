package com.risotto.view.wizard;

import com.risotto.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TakeItEveryDay extends Activity implements OnClickListener {
	public static final String LOG_TAG = "com.risotto.view.wizard.TakeItEveryDay";
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
		
		Intent intent = getIntent();

		setContentView(R.layout.wizard_take_it_every_day);
		
		Button yes = (Button) this.findViewById(R.id.button_wizard_choice_take_every_day_yes);
		yes.setOnClickListener(this);
		
		Button no = (Button) this.findViewById(R.id.button_wizard_choice_take_every_day_no);
		no.setOnClickListener(this);
		
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * 
	 * Method called when button is clicked.
	 * 
	 */
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.button_wizard_choice_take_every_day_yes:
				Log.d(LOG_TAG,"spec_time");
				//launch will you take it every day
				break;
			case R.id.button_wizard_choice_take_every_day_no:
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

