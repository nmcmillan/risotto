package com.risotto.view.wizard;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import com.risotto.R;
import com.risotto.storage.StorageProvider;

public class PatientSelect extends ListActivity {
	public static final String LOG_TAG = "com.risotto.view.wizard.PatientSelect";
	
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  
		  setContentView(R.layout.wizard_patient_select);
		  
		  Cursor patientCursor = getContentResolver().query(
				  StorageProvider.PatientColumns.CONTENT_URI, PATIENT_PROJECTION, null, null, null);
		  
		  startManagingCursor(patientCursor);
		  
		  Log.d(LOG_TAG,"count " + patientCursor.getCount());
		  
		  SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				  this,
				  R.layout.wizard_patient_select_list_view_layout,
				  patientCursor,
				  VIEW_DB_COLUMN_MAPPING,
				  VIEW_ID_MAPPING);
		  
		 setListAdapter(adapter);
		  
	}
		  
}
