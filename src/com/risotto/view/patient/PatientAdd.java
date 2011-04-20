package com.risotto.view.patient;

import com.risotto.R;
import com.risotto.model.Drug;
import com.risotto.model.Patient;
import com.risotto.storage.StorageProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView.OnItemSelectedListener;

public class PatientAdd extends Activity implements OnClickListener, OnItemSelectedListener {
	
	public static final String ACTION_VIEW_ADD_PATIENT = "com.risotto.view.patient.AddPatientIntent";
	public static final String LOG_TAG = "com.risotto.view.patient.PatientAdd";
	
	private EditText patientFirstName;
	private EditText patientLastName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(LOG_TAG,"onCreate called");
		
		setContentView(R.layout.patient_add_layout);
		
		patientFirstName = (EditText) this.findViewById(R.id.patient_add_field_first_name);
		patientFirstName.setHint(R.string.patient_add_first_name);
		//drugNameText.setOnClickListener(this);
		
		patientLastName = (EditText) this.findViewById(R.id.patient_add_field_last_name);
		patientLastName.setHint(R.string.patient_add_last_name);
		//drugStrengthText.setOnClickListener(this);
		
		Button b = (Button) this.findViewById(R.id.patient_add_button_ok);
		b.setOnClickListener(this);
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

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
		}
		else {
			Patient patient = new Patient(firstName,lastName,Patient.GENDER_FEMALE);
			ContentValues cv = patient.toContentValues();
			this.getContentResolver().insert(StorageProvider.PatientColumns.CONTENT_URI, cv);
		}
		finish();

	}

}
