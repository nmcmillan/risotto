package com.risotto.view.wizard;

import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.risotto.R;
import com.risotto.model.Patient;
import com.risotto.storage.StorageProvider;
import com.risotto.view.patient.PatientAdd;

public class PatientSelect extends ListActivity implements View.OnClickListener {
	public static final String LOG_TAG = "com.risotto.view.wizard.PatientSelect";
	
	private HashMap<String,Object> wizardData = new HashMap<String,Object>();
	
	private static String[] PATIENT_PROJECTION = {
		StorageProvider.PatientColumns._ID,
		StorageProvider.PatientColumns.PATIENT_FIRST_NAME,
		StorageProvider.PatientColumns.PATIENT_LAST_NAME,
	};
		
	private static String[] VIEW_DB_COLUMN_MAPPING = {
		StorageProvider.PatientColumns.PATIENT_FIRST_NAME,
		StorageProvider.PatientColumns.PATIENT_LAST_NAME
	};
	private static int[] VIEW_ID_MAPPING = {
		R.id.wizard_patient_select_list_view_first_name,
		R.id.wizard_patient_select_list_view_last_name
	};
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  
		  setContentView(R.layout.wizard_patient_select);
		  
		  try {
			  this.wizardData = WizardData.getData(getIntent().getExtras());
		  } catch (Exception e) {
			  Log.d(LOG_TAG,"No data found in intent.");
		  }
		  
		  Cursor patientCursor = getContentResolver().query(
				  StorageProvider.PatientColumns.CONTENT_URI, PATIENT_PROJECTION, null, null, null);
		  
		  startManagingCursor(patientCursor);

		  Log.d(LOG_TAG,"count " + patientCursor.getCount());
		  
		  Button addPatient = (Button)findViewById(R.id.button_wizard_patient_select_add_new_patient);
		  addPatient.setOnClickListener(this);
		  
		  SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				  this,
				  R.layout.wizard_patient_select_list_view,
				  patientCursor,
				  VIEW_DB_COLUMN_MAPPING,
				  VIEW_ID_MAPPING);
		  
		 setListAdapter(adapter);
		  
	}
	
	/**
	 * When user selects a patient from the list, it needs to be added to the intent.
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent();
		
		Uri patientUri = StorageProvider.PatientColumns.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
		
		Cursor pCursor = getContentResolver().query(
				patientUri, 
				PATIENT_PROJECTION, 
				null, 
				null, 
				null);
		
		if(null != pCursor) {
			pCursor.moveToFirst();
			Patient patient = Patient.fromCursor(pCursor);
			wizardData.put(WizardData.PATIENT, patient);
			intent.putExtra(WizardData.CONTENTS,wizardData);
		}
		else {
			Log.d(LOG_TAG,"Couldn't find patient in database.");
		}
		pCursor.close();
		
		if((Boolean)wizardData.get(WizardData.CREATE_NEW_DRUG))
			intent.setClass(getApplicationContext(), OverCounterOrPrescription.class);
		else
			intent.setClass(getApplicationContext(), DrugSelect.class);
		
		startActivity(intent);
	}

	public void onClick(View v) {
		//only one button in this class, so no need to check view id
		
		Intent intent = new Intent();
		intent.putExtra(WizardData.CONTENTS, wizardData);
		intent.setClass(getApplicationContext(), PatientAdd.class);
		startActivity(intent);
		
	}
		  
}
