package com.risotto.view.prescription;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.risotto.R;
import com.risotto.storage.StorageProvider;

public class PrescriptionAdd extends Activity implements OnClickListener, OnItemSelectedListener {
	
	public static final String LOG_TAG = "com.risotto.view.prescription.PrescriptionAdd";
	
	private static final String NEW_PATIENT = "New Patient";
	
	private static final String[] PATIENT_PROJECTION = {
		StorageProvider.PatientColumns._ID,
		StorageProvider.PatientColumns.PATIENT_FIRST_NAME,
		StorageProvider.PatientColumns.PATIENT_LAST_NAME
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  
		  setContentView(R.layout.prescription_add_layout);
		  
		  Cursor patientCursor = this.getContentResolver().query(
				  StorageProvider.PatientColumns.CONTENT_URI, 
				  PATIENT_PROJECTION,
				  null, 
				  null,
				  null);
		  
		  Spinner spinner = (Spinner) this.findViewById(R.id.prescription_add_patient_spinner);
		  ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		  spinner.setAdapter(adapter);
		  
		  adapter.add(NEW_PATIENT);
		  
		  while(patientCursor.moveToNext()) {
			  CharSequence firstName = patientCursor.getString(patientCursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_FIRST_NAME)).trim();
			  CharSequence lastName = patientCursor.getString(patientCursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_LAST_NAME)).trim();
			  CharSequence fullName = firstName + " " + lastName;
			  adapter.add(fullName);
		  }
		  
		  spinner.setOnItemSelectedListener(this);
		  patientCursor.close();
		  
		  EditText firstName = (EditText)findViewById(R.id.prescription_add_patient_first_name);
		  firstName.setHint(R.string.prescription_add_patient_first_name_hint);
		  
		  EditText lastName = (EditText)findViewById(R.id.prescription_add_patient_last_name);
		  lastName.setHint(R.string.prescription_add_patient_last_name_hint);
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int viewPosition, long rowId) {
		// TODO Auto-generated method stub
		Log.d(LOG_TAG, "in onItemSelected. ");
		Log.d(LOG_TAG, "viewPosition: " + viewPosition);
		Log.d(LOG_TAG, "rowId: " + rowId);
		
		if(0 == viewPosition) {
			//new patient was selected
		}
		else {
			//attempt to find patient in DB
			
			CharSequence fullName = (CharSequence)parent.getItemAtPosition(viewPosition);
			
			String[] name = ((String)fullName).split(" ");
			String firstName = name[0].trim();
			String lastName = name[1].trim();
			
			//Uri patientUri = ContentUris.withAppendedId(StorageProvider.PatientColumns.CONTENT_URI, rowId);
			
			String where = "patients.first_name='" + firstName + "' AND patients.last_name='" + lastName + "'";
			
			Cursor patientCursor = getContentResolver().query(
					StorageProvider.PatientColumns.CONTENT_URI, 
					PATIENT_PROJECTION, 
					where, 
					null, 
					null);
			
			patientCursor.moveToFirst();
			
			EditText fName = (EditText) findViewById(R.id.prescription_add_patient_first_name);
			fName.setText(patientCursor.getString(patientCursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_FIRST_NAME)));
			fName.setEnabled(false);
			
			EditText lName = (EditText) findViewById(R.id.prescription_add_patient_last_name);
			lName.setText(patientCursor.getString(patientCursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_LAST_NAME)));
			lName.setEnabled(false);
		}
			

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
