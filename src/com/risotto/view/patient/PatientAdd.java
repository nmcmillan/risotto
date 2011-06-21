package com.risotto.view.patient;

import com.risotto.R;
import com.risotto.model.Drug;
import com.risotto.model.Patient;
import com.risotto.storage.StorageProvider;
import com.risotto.view.drug.DrugAdd;
import com.risotto.view.wizard.WizardData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView.OnItemSelectedListener;

public class PatientAdd extends Activity implements OnClickListener {
	
	public static final String ACTION_VIEW_ADD_PATIENT = "com.risotto.view.patient.AddPatientIntent";
	public static final String LOG_TAG = "com.risotto.view.patient.PatientAdd";
	
	private EditText patientFirstName;
	private EditText patientLastName;
	private boolean inWizard = false;
	private boolean goToNewDrug = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(LOG_TAG,"onCreate called");
		
		setContentView(R.layout.patient_add_layout);
		
		Bundle extras = getIntent().getExtras();
		
		if(extras != null && extras.containsKey(WizardData.CREATE_NEW_DRUG)) {
			inWizard = true;
			goToNewDrug = (Boolean) extras.get(WizardData.CREATE_NEW_DRUG);
		}
		
		patientFirstName = (EditText) this.findViewById(R.id.patient_add_field_first_name);
		
		patientLastName = (EditText) this.findViewById(R.id.patient_add_field_last_name);
		
		Button b = (Button) this.findViewById(R.id.patient_add_button_ok);
		b.setOnClickListener(this);
		if(inWizard)
			b.setText(R.string.patient_add_button_string_wizard);
		
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		String firstName = this.patientFirstName.getText().toString().trim();
		String lastName = this.patientLastName.getText().toString().trim();
		
		if(firstName == "") {
			new AlertDialog.Builder(this)
		    .setTitle("First Name needed!")
		    .setMessage("Oops - the first name can't be empty, please enter a name for the patient!")
		    //don't do anything when the button is clicked
		    .setPositiveButton("Okay", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) {} })
		    .show();
		} else {
			Patient patient = new Patient(firstName,lastName,Patient.GENDER.FEMALE);
			if(inWizard) {
				Intent intent = new Intent();
				intent.putExtra(WizardData.PATIENT, patient);
				//if goToNewDrug is true, that means there are no drugs
				//in the database & we need to go to OTC or prep
				//if it's false, go straight to drug selection screen
				if(goToNewDrug) 
					intent.setClass(getApplicationContext(), DrugAdd.class);
				//
				//else
				//	intent.setClass(getApplicationContext(), SelectPatient.class);
				
				startActivity(intent);
			} else {
				ContentValues cv = patient.toContentValues();
				this.getContentResolver().insert(StorageProvider.PatientColumns.CONTENT_URI, cv);
			}
			
		}
		finish();

	}

}
