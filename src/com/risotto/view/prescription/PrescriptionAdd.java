package com.risotto.view.prescription;

import android.app.Activity;
import android.content.ContentValues;
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
import com.risotto.model.Drug;
import com.risotto.model.Patient;
import com.risotto.model.Prescription;
import com.risotto.storage.StorageProvider;

public class PrescriptionAdd extends Activity implements OnClickListener, OnItemSelectedListener {
	
	public static final String LOG_TAG = "com.risotto.view.prescription.PrescriptionAdd";
	
	private static final String NEW_PATIENT = "New Patient";
	private static final String NEW_DRUG = "New Drug";
	
	private Patient patient;
	private Drug drug;
	
	private Spinner patientSpinner,drugSpinner;
	
	private static final String[] PATIENT_PROJECTION = {
		StorageProvider.PatientColumns._ID,
		StorageProvider.PatientColumns.PATIENT_FIRST_NAME,
		StorageProvider.PatientColumns.PATIENT_LAST_NAME
	};
	
	private static final String[] DRUG_PROJECTION = {
		StorageProvider.DrugColumns._ID,
		StorageProvider.DrugColumns.DRUG_BRAND_NAME,
		StorageProvider.DrugColumns.DRUG_STRENGTH,
		StorageProvider.DrugColumns.DRUG_STRENGTH_LABEL
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  
		  Patient patient;
		  Drug drug;
		  
		  setContentView(R.layout.prescription_add_layout);
		  
		  //code for filling the spinner UI object with the available patients
		  Cursor patientCursor = this.getContentResolver().query(
				  StorageProvider.PatientColumns.CONTENT_URI, 
				  PATIENT_PROJECTION,
				  null, 
				  null,
				  null);
		  
		  patientSpinner = (Spinner) this.findViewById(R.id.prescription_add_patient_spinner);
		  ArrayAdapter<CharSequence> patientAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		  patientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		  patientSpinner.setAdapter(patientAdapter);
		  
		  patientAdapter.add(NEW_PATIENT);
		  
		  while(patientCursor.moveToNext()) {
			  patient = Patient.fromCursor(patientCursor);
			  CharSequence firstName = patient.getFirstName();
			  CharSequence lastName = patient.getLastName();
			  CharSequence fullName = firstName + " " + lastName;
			  patientAdapter.add(fullName);
		  }
		  
		  patientSpinner.setOnItemSelectedListener(this);
		  patientCursor.close();
		  
		  EditText firstName = (EditText)findViewById(R.id.prescription_add_patient_first_name);
		  firstName.setHint(R.string.prescription_add_patient_first_name_hint);
		  
		  EditText lastName = (EditText)findViewById(R.id.prescription_add_patient_last_name);
		  lastName.setHint(R.string.prescription_add_patient_last_name_hint);
		  
		  //code for filling spinner UI object with available drugs
		  Cursor drugCursor = this.getContentResolver().query(
				  StorageProvider.DrugColumns.CONTENT_URI, 
				  DRUG_PROJECTION, 
				  null, 
				  null, 
				  null);
		  
		  drugSpinner = (Spinner) this.findViewById(R.id.prescription_add_drug_spinner);
		  ArrayAdapter<CharSequence> drugAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		  drugAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		  drugSpinner.setAdapter(drugAdapter);
		  
		  drugAdapter.add(NEW_DRUG);
		  
		  while(drugCursor.moveToNext()) {
			  drug = Drug.fromCursor(drugCursor);
			  drugAdapter.add(drug.getBrandName() + " - " + drug.getPrintableStrength());
		  }
		  
		  drugSpinner.setOnItemSelectedListener(this);
		  drugCursor.close();
		  
		  
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int viewPosition, long rowId) {
		// TODO Auto-generated method stub
		Log.d(LOG_TAG, "in onItemSelected. ");
		Log.d(LOG_TAG, "viewPosition: " + viewPosition);
		Log.d(LOG_TAG, "rowId: " + rowId);
		Log.d(LOG_TAG, "parent: " + parent.getId()); 
		Log.d(LOG_TAG, "id for patientSpinner: " + this.patientSpinner.getId());
		
		if(parent.getId() == R.id.prescription_add_patient_spinner) {
			if(0 == viewPosition) {
				//new patient was selected
			}
			else {
				//attempt to find patient in DB
				
				CharSequence fullName = (CharSequence)parent.getItemAtPosition(viewPosition);
				
				String[] name = ((String)fullName).split(" ");
				String firstName = name[0].trim();
				String lastName = "";
				if(name.length > 1) {
					lastName = name[1].trim();
				}
				
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
				fName.setText(firstName);
				fName.setEnabled(false);
				
				EditText lName = (EditText) findViewById(R.id.prescription_add_patient_last_name);
				lName.setText(lastName);
				lName.setEnabled(false);
				
				patient = Patient.fromCursor(patientCursor);
				
				patientCursor.close();
			}
		} else {
			if(0 == viewPosition) {
				//new patient was selected
			} else {
				CharSequence drugDisplayString = (CharSequence)parent.getItemAtPosition(viewPosition);
				
				EditText dName = (EditText) findViewById(R.id.prescription_add_drug_brand_name);
				dName.setText(drugDisplayString);
				dName.setEnabled(false);
				
				String[] name = ((String)drugDisplayString).split("-");
				String brandName = name[0].trim();
				
				String where = "drugs.brand_name='" + brandName + "'";
				
				Log.d(LOG_TAG, "where = " + where);
				
				Cursor drugCursor = getContentResolver().query(
						StorageProvider.DrugColumns.CONTENT_URI, 
						DRUG_PROJECTION,
						where, 
						null,
						null);
				drugCursor.moveToFirst();
				drug = Drug.fromCursor(drugCursor);
				drugCursor.close();
			}
			
		}
		
		
			

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	public void onClick(View v) {
		Prescription newPrep = new Prescription(patient,drug,Prescription.DOSE_TYPE_EVERY_DAY);
		ContentValues cv = newPrep.toContentValues(getApplicationContext());
		Uri prescriptionUri = this.getContentResolver().insert(StorageProvider.PrescriptionColumns.CONTENT_URI, cv);
		Log.d(LOG_TAG,"finished adding drug; uri = " + prescriptionUri);

	}

}
