package com.risotto.view.prescription;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.risotto.R;
import com.risotto.model.Drug;
import com.risotto.service.MainService;
import com.risotto.storage.StorageProvider;
import com.risotto.view.drug.DrugDetailsView;
import com.risotto.view.drug.DrugView;

public class PrescriptionView extends ListActivity implements SimpleCursorAdapter.ViewBinder {
	
	public static final String ACTION_ADD_PRESCRIPTION = "com.risotto.view.prescription.PrescriptionAdd";
	
	private static final String[] PRESCRIPTION_PROJECTION = {
		//Prescription columns
		StorageProvider.PrescriptionColumns._ID,
		StorageProvider.PrescriptionColumns.PRESCRIPTION_PATIENT,
		StorageProvider.PrescriptionColumns.PRESCRIPTION_DATE_EXPIRATION,
		
		//Patient columns
		StorageProvider.PatientColumns._ID,
		StorageProvider.PatientColumns.PATIENT_FIRST_NAME,
		StorageProvider.PatientColumns.PATIENT_LAST_NAME,
		
		//Drug columns
		StorageProvider.DrugColumns._ID,
		StorageProvider.DrugColumns.DRUG_BRAND_NAME,
		StorageProvider.DrugColumns.DRUG_FORM		
	};
	
	public static final String LOG_TAG = "com.risotto.view.prescription.PrescriptionView";
	
	/**
	 * Create a menu that will pop up when the user presses the Menu button.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.prescription_menu_layout, menu);
	    return true;
	}
	
	/**
	 * Handles actions when buttons from the menu created in 'onCreateOptionsMenu'
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.prescription_menu_add_prep:
	    	//send intent to add prescription
	    	Intent newPrepIntent = new Intent();
			newPrepIntent.setAction(PrescriptionView.ACTION_ADD_PRESCRIPTION);
			newPrepIntent.setClass(getApplicationContext(), PrescriptionAdd.class);
			startActivity(newPrepIntent);
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	  super.onCreateContextMenu(menu, v, menuInfo);
	  MenuInflater inflater = getMenuInflater();
	  inflater.inflate(R.layout.alarm_menu_context_layout, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.alarm_view_context_menu_edit:
			System.out.println("Attempting to change notification");
			//stbm.modify(1, Content.STATUS_BAR, "This is a test.");
			//nots[0] = "New";
			//onContentChanged();
			((ArrayAdapter<String>)(this.getListAdapter())).notifyDataSetChanged();
			return true;
		case R.id.alarm_view_context_menu_remove:
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  
	  Cursor prepCursor = StorageProvider.prescriptionJoinQuery(PRESCRIPTION_PROJECTION);

	  if (null != prepCursor) {
			startManagingCursor(prepCursor);

			Log.d(LOG_TAG, "count: " + prepCursor.getCount());
			Log.d(LOG_TAG, "cursor column count: " + prepCursor.getColumnCount());
			
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(
					this, // context
					R.layout.prescription_list_view, // layout
					prepCursor, // cursor
					new String[] { 
						StorageProvider.PatientColumns.PATIENT_FIRST_NAME,
						StorageProvider.PatientColumns.PATIENT_LAST_NAME,
						StorageProvider.DrugColumns.DRUG_BRAND_NAME},
					new int[] { R.id.prescription_list_view_patient_first_name,
							R.id.prescription_list_view_patient_last_name,
							R.id.prescription_list_view_drug_name}); // mapping
							
			adapter.setViewBinder(this);
			setListAdapter(adapter);
	  }
	  else {
		  Log.d(LOG_TAG,"prepCursor was null");
	  }
		
	  registerForContextMenu(getListView());
	  
	
	}

	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		//Log.d(LOG_TAG, "cursor index: " + cursor.getPosition());
		//Drug newDrug = Drug.fromCursor(cursor);

		if (columnIndex == cursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_FIRST_NAME)) {
			((TextView)view).setText(cursor.getString(columnIndex));
		} else if (columnIndex == cursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_LAST_NAME)) {
			//((TextView)view).setText(newDrug.getPrintableStrength());
			//((TextView)view).setTypeface(Typeface.create("null", Typeface.ITALIC));
			((TextView)view).setText(cursor.getString(columnIndex));
		} else if(columnIndex == cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_BRAND_NAME)) {
			Log.d(LOG_TAG,"brand name");
			((TextView)view).setText(cursor.getString(columnIndex));
		} else {
			Log.d(LOG_TAG,"column index passed in: " + columnIndex);
			Log.d(LOG_TAG,"column index drug brand name: " + StorageProvider.DrugColumns.DRUG_BRAND_NAME);
			return false;
		}
		return true;
	}
	
}
