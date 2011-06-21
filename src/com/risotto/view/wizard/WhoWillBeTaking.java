package com.risotto.view.wizard;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.risotto.R;
import com.risotto.storage.StorageProvider;
import com.risotto.view.patient.PatientAdd;

public class WhoWillBeTaking extends Activity implements OnClickListener {

	public static final String LOG_TAG = "com.risotto.view.wizard.WhoWillBeTaking";
	
	public static final String ACTION_WIZARD_NEW_DRUG = "com.risotto.view.wizard.WhoWillBeTaking.newDrug";
	
	private static String[] DRUG_PROJECTION = {
		StorageProvider.DrugColumns._ID,
		StorageProvider.DrugColumns.DRUG_BRAND_NAME,
		StorageProvider.DrugColumns.DRUG_STRENGTH,
		StorageProvider.DrugColumns.DRUG_STRENGTH_LABEL
	};
	
	private static String[] PATIENT_PROJECTION = {
		StorageProvider.PatientColumns._ID,
		StorageProvider.PatientColumns.PATIENT_FIRST_NAME,
		StorageProvider.PatientColumns.PATIENT_LAST_NAME
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.wizard_gen_question_layout);
		
		Button me = (Button)findViewById(R.id.button_wizard_gen_question_layout_answer_one);
		me.setText(R.string.wizard_who_will_be_taking_me);
		me.setTextSize(30);
		me.setOnClickListener(this);
		
		Button other = (Button)findViewById(R.id.button_wizard_gen_question_layout_answer_two);
		other.setText(R.string.wizard_who_will_be_taking_other);
		other.setTextSize(30);
		other.setOnClickListener(this);
		
		TextView question = (TextView)findViewById(R.id.wizard_gen_question_layout_question_text);
		question.setText(R.string.wizard_who_will_be_taking_question);
		question.setTextSize(30);
		
		
	}

	public void onClick(View v) {
		
		//Check if the drug database has any drugs in it
		Cursor drugCursor = this.getContentResolver().query(
				StorageProvider.DrugColumns.CONTENT_URI, DRUG_PROJECTION, null, null, null);
		boolean goToNewDrug = true;
		
		Cursor patientCursor = this.getContentResolver().query(
				StorageProvider.PatientColumns.CONTENT_URI, PATIENT_PROJECTION, null, null, null);
		boolean goToNewPatient = true;
	
		//returned at least one result, so don't create new drug
		if(drugCursor != null && drugCursor.getCount() > 0) {
			goToNewDrug = false;
		}
		//check if we need to go to patient add if user selects it's for someone else
		//and there are no patients in database
		if(patientCursor != null && patientCursor.getCount() > 0) {
			goToNewPatient = false;
		}
		
		Intent intent = new Intent();
		
		intent.putExtra(WizardData.CREATE_NEW_DRUG, goToNewDrug);
		
		switch(v.getId()) {
			case R.id.button_wizard_gen_question_layout_answer_one:
				Log.d(LOG_TAG,"selected me.");
				if(goToNewDrug) {
					//launch new drug activity
					Log.d(LOG_TAG,"new drug - select OTC or prep.");
					intent.setClass(getApplicationContext(), OverCounterOrPrescription.class);
					startActivity(intent);
				} else {
					//launch select drug activity
					Log.d(LOG_TAG,"select drug from list.");
				}
				break;
			case R.id.button_wizard_gen_question_layout_answer_two:
				Log.d(LOG_TAG,"selected other");
				if(goToNewPatient) {
					Log.d(LOG_TAG,"new patient.");
					intent.setClass(getApplicationContext(), PatientAdd.class);
					startActivity(intent);
				} else {
					//launch select patient activity
					Log.d(LOG_TAG,"select patient from list.");
					intent.setClass(getApplicationContext(), PatientSelect.class);
					startActivity(intent);
				}
				break;
			default:
				break;
		
		}
		
	}
	
}
