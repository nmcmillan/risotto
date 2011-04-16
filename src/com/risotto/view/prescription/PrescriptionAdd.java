package com.risotto.view.prescription;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.risotto.R;
import com.risotto.storage.StorageProvider;

public class PrescriptionAdd extends Activity implements OnClickListener, OnItemSelectedListener {
	
	public static final String LOG_TAG = "com.risotto.view.prescription.PrescriptionAdd";
	
	private static final String NEW_PATIENT = "New Patient";
	
	private static final String[] PATIENT_PROJECTION = {
		StorageProvider.PatientColumns._ID,
		StorageProvider.PatientColumns.PATIENT_FIRST_NAME
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
		  
		  while(patientCursor.moveToNext()) {
			  CharSequence name = patientCursor.getString(patientCursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_FIRST_NAME));
			  Log.d(LOG_TAG,name.toString());
			  adapter.add(name);
		  }
		  
		  adapter.add(NEW_PATIENT);		  
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

	}

}
