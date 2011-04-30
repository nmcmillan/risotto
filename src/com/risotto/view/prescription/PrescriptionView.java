package com.risotto.view.prescription;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.risotto.R;
import com.risotto.model.Prescription;
import com.risotto.storage.StorageProvider;

public class PrescriptionView extends ListActivity implements SimpleCursorAdapter.ViewBinder {
	
	public static final String ACTION_ADD_PRESCRIPTION = "com.risotto.view.prescription.PrescriptionAdd";
	
	private Cursor prepCursor;
	private SimpleCursorAdapter prepAdapter;
	
	private static final String[] PRESCRIPTION_PROJECTION = {
		//Prescription columns
		StorageProvider.PRESCRIPTIONS_TABLE_NAME + "." + StorageProvider.PrescriptionColumns._ID,
		StorageProvider.PrescriptionColumns.PRESCRIPTION_PATIENT,
		StorageProvider.PrescriptionColumns.PRESCRIPTION_DATE_EXPIRATION,
		
		//Patient columns
		//StorageProvider.PatientColumns._ID,
		StorageProvider.PatientColumns.PATIENT_FIRST_NAME,
		StorageProvider.PatientColumns.PATIENT_LAST_NAME,
		
		//Drug columns
		//StorageProvider.DrugColumns._ID,
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
	
	/**
	 * Method called when a user presses and holds on an item in the list - creates the menu.
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	  super.onCreateContextMenu(menu, v, menuInfo);
	  AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
	  Cursor pCursor = (Cursor) this.getListView().getItemAtPosition(info.position);
	  
	  String firstName = pCursor.getString(pCursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_FIRST_NAME));
	  String drugName = pCursor.getString(pCursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_BRAND_NAME));

	  menu.setHeaderTitle(firstName + "'s precription for " + drugName);
	  MenuInflater inflater = getMenuInflater();
	  inflater.inflate(R.layout.prescription_view_context_menu_layout, menu);
	}
	
	/**
	 * Method called when a user presses a button on the context menu.
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.prescription_view_context_menu_edit:
				return true;
			case R.id.prescription_view_context_menu_remove:
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
				Cursor pCursor = (Cursor)this.getListView().getItemAtPosition((int)info.position);
				
				//Log.d(LOG_TAG,"first: " + pCursor.getString(pCursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_FIRST_NAME)));
				//Log.d(LOG_TAG,"last: " + pCursor.getString(pCursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_LAST_NAME)));
				//Log.d(LOG_TAG,"drug: " + pCursor.getString(pCursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_BRAND_NAME)));
				//Log.d(LOG_TAG, "prescription_patient: " + pCursor.getString(pCursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_PATIENT)));
				//Log.d(LOG_TAG, "prescription_drug: " + pCursor.getString(pCursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DRUG)));
				//Log.d(LOG_TAG,"_id: " + pCursor.getInt(pCursor.getColumnIndex(StorageProvider.PrescriptionColumns._ID)));
				//Log.d(LOG_TAG,"_id : (3) " + _id);
				
				int _id = pCursor.getInt(pCursor.getColumnIndex(StorageProvider.PrescriptionColumns._ID));
				
				Uri prepUri = StorageProvider.PrescriptionColumns.CONTENT_URI.buildUpon().appendPath(String.valueOf(_id)).build();
				if(getContentResolver().delete(prepUri,null,null) > 0) {
					prepCursor.requery();
					return true;
				} else {
					return false;
				}
			default:
				return super.onContextItemSelected(item);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  
	  prepCursor = StorageProvider.prescriptionJoinQuery(PRESCRIPTION_PROJECTION);

	  if (null != prepCursor) {
			startManagingCursor(prepCursor);

			Log.d(LOG_TAG, "count: " + prepCursor.getCount());
			Log.d(LOG_TAG, "cursor column count: " + prepCursor.getColumnCount());
			
			prepAdapter = new SimpleCursorAdapter(
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
			
			prepAdapter.setViewBinder(this);
			setListAdapter(prepAdapter);
	  }
	  else {
		  Log.d(LOG_TAG,"prepCursor was null");
		  finish();
	  }
		
	  registerForContextMenu(getListView());
	  
	
	}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
		Log.d(LOG_TAG,"onListItemClick called");
	}
	

	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		if (columnIndex == cursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_FIRST_NAME)) {
			((TextView)view).setText(cursor.getString(columnIndex));
		} else if (columnIndex == cursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_LAST_NAME)) {
			//((TextView)view).setText(newDrug.getPrintableStrength());
			//((TextView)view).setTypeface(Typeface.create("null", Typeface.ITALIC));
			((TextView)view).setText(cursor.getString(columnIndex));
		} else if(columnIndex == cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_BRAND_NAME)) {
			((TextView)view).setText(cursor.getString(columnIndex));
		} else {
			Log.d(LOG_TAG,"column index passed in: " + columnIndex);
			Log.d(LOG_TAG,"column index drug brand name: " + StorageProvider.DrugColumns.DRUG_BRAND_NAME);
			return false;
		}
		return true;
	}
	
}
