package com.risotto.view.wizard;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
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

public class WhenTakeIt extends Activity implements OnClickListener {

	//the name of these strings will correspond to the answer that was clicked in this UI
	//they will reference the activity that is to be started next based on the answer
	public static final String WIZARD_SPEC_TIMES = "com.risotto.view.wizard.TakeItEveryDay";
	public static final String WIZARD_SPEC_INTERVAL = "com.risotto.view.wizard.TakeItEveryInterval";
	
	public static final String LOG_TAG = "com.risotto.view.wizard.WhenTakeIt";
	
	private Prescription prep;
	private Patient patient;
	private Drug drug;
	
	private HashMap<String,Object> wizardData = new HashMap<String,Object>();
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
		
		try {
			  this.wizardData = WizardData.getData(getIntent().getExtras());
		  } catch (Exception e) {
			  Log.d(LOG_TAG,"No data found in intent.");
		  }
		
		drug = (Drug)wizardData.get(WizardData.DRUG);
		patient = (Patient)wizardData.get(WizardData.PATIENT);
		
		//Print out what the information received so far is:
		Log.d(LOG_TAG,"Drug info: Name : " + drug.getBrandName());
		Log.d(LOG_TAG,"Drug info: Name : " + drug.getForm());
		
		Log.d(LOG_TAG,"Patient info: First Name : " + patient.getFirstName());
		Log.d(LOG_TAG,"Patient info: Last : " + patient.getLastName());

		setContentView(R.layout.wizard_gen_question);
		
		TextView question = (TextView) this.findViewById(R.id.wizard_gen_question_layout_question_text);
		question.setText(R.string.wizard_when_take_it_text);
		
		Button specTime = (Button) this.findViewById(R.id.button_wizard_gen_question_layout_answer_one);
		specTime.setOnClickListener(this);
		specTime.setText(R.string.wizard_when_take_it_spec_time);
		
		TextView suppTime = (TextView) this.findViewById(R.id.wizard_gen_question_layout_answer_one_supp_text);
		suppTime.setText(R.string.wizard_when_take_it_spec_time_support);
		
		Button specInt = (Button) this.findViewById(R.id.button_wizard_gen_question_layout_answer_two);
		specInt.setOnClickListener(this);
		specInt.setText(R.string.wizard_when_take_it_spec_interval);
		
		TextView suppInt = (TextView) this.findViewById(R.id.wizard_gen_question_layout_answer_two_supp_text);
		suppInt.setText(R.string.wizard_when_take_it_spec_interval_support);
		
	}
	
	public void onClick(View v) {
		Intent nextQuestion = new Intent();
		nextQuestion.putExtra(WizardData.CONTENTS, wizardData);
		switch(v.getId()) {
			case R.id.button_wizard_gen_question_layout_answer_one:
				Log.d(LOG_TAG,"spec_time");
				//launch will you take it every day
		    	nextQuestion.setClass(getApplicationContext(), TakeItEveryDay.class);
				startActivity(nextQuestion);
				break;
			case R.id.button_wizard_gen_question_layout_answer_two:
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
