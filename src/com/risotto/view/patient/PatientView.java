package com.risotto.view.patient;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
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
import android.widget.SimpleCursorAdapter;

import com.risotto.R;
import com.risotto.model.Patient;
import com.risotto.model.Prescription;
import com.risotto.storage.StorageProvider;

public class PatientView extends ListActivity implements SimpleCursorAdapter.ViewBinder {

	public static final String LOG_TAG = "com.risotto.view.drug.PatientView";
	public static final int MENU_ITEM_ADD_POSITION = Menu.FIRST;
	
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
		R.id.patient_list_view_first_name,
		R.id.patient_list_view_last_name
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  
		  //sets default state of what happens when keys are pressed
		  //that are not handled by the application;
		  //multiple options here, see the javadoc
		  setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
		  
		  //This method will display all of the drugs in the database
		  //	- will we expect something to be passed in through the intent?
		  Intent intent = getIntent();
		  
		  if(null == intent.getData()) {
			  intent.setData(StorageProvider.PatientColumns.CONTENT_URI);
		  }
		
		  getListView().setOnCreateContextMenuListener(this);
		  
		  Cursor cursor = this.getContentResolver().query(getIntent().getData(), PATIENT_PROJECTION, null, null, null);
		  
		  if(null != cursor) {
			  startManagingCursor(cursor);
			  
		  Log.d(LOG_TAG,"count: " + cursor.getCount());
		  Log.d(LOG_TAG,"cursor column count: " + cursor.getColumnCount());
		  
		  //note: the cursor originally points to a null row, needs to move before trying to print data
		  //cursor.moveToFirst();
		  
		  //Log.d(LOG_TAG,"cursor.toString(0)" + cursor.getString(1));
		  
		  //TextView drugListItem = (TextView)this.findViewById(R.layout.drug_list_item);
		  
		  SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				  this,						//context
			  R.layout.patient_list_item,	//layout
			  cursor,					//cursor
			  VIEW_DB_COLUMN_MAPPING, 
			  VIEW_ID_MAPPING); //mapping
		  
		  //adapter.setViewBinder(this);
			  setListAdapter(adapter);
		  }
		
		  registerForContextMenu(getListView());
	}
	
	/**
	 * This method is only called once and that's the first time the options
	 * menu is displayed.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		/*
		 * MenuInflater inflater = getMenuInflater();
		 * inflater.inflate(R.layout.drug_menu_layout, menu); return true;
		 */
		menu.add(Menu.NONE, // group id for doing batch changes
				MENU_ITEM_ADD_POSITION, // position
				Menu.NONE, // order, see getOrder()
				R.string.patient_view_menu_add) // name of button - link to XML
				.setIcon(android.R.drawable.ic_menu_add);

		return true;
	}
	
	/**
	 * Handles actions when buttons from the menu created in
	 * 'onCreateOptionsMenu'
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case MENU_ITEM_ADD_POSITION:
			Log.d(LOG_TAG, "MENU_ITEM_ADD clicked");
			Intent addIntent = new Intent();
			addIntent.setAction(PatientAdd.ACTION_VIEW_ADD_PATIENT);
			addIntent.setClass(getApplicationContext(), PatientAdd.class);
			startActivity(addIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		Patient patient = Patient.fromCursor(cursor);
		return true;
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
	  String lastName = pCursor.getString(pCursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_LAST_NAME)); 
	  

	  menu.setHeaderTitle(firstName + " " + lastName);
	  MenuInflater inflater = getMenuInflater();
	  inflater.inflate(R.layout.patient_view_context_menu_layout, menu);
	}
	
	/**
	 * Method called when a user presses a button on the context menu.
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.patient_view_context_menu_edit:
				return true;
			case R.id.patient_view_context_menu_remove:
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
				Cursor pCursor = (Cursor)this.getListView().getItemAtPosition((int)info.position);
				
				int _id = pCursor.getInt(pCursor.getColumnIndex(StorageProvider.PatientColumns._ID));
				String fName = pCursor.getString(pCursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_FIRST_NAME));
				
				Uri patientUri = StorageProvider.PatientColumns.CONTENT_URI.buildUpon().appendPath(String.valueOf(_id)).build();
				
				Log.d(LOG_TAG,"attempting to remove patient with db id of : " + _id);
				
				try{
					if(getContentResolver().delete(patientUri,null,null) > 0) {
						Log.d(LOG_TAG,"delete success.");
						return true;
					} else {
						Log.d(LOG_TAG,"delete fail.");
						return false;
					}
				} catch(SQLiteConstraintException foreignKey){
					//if this exception was thrown, that means this patient is scheduled for a prescription, get that information
					Log.d(LOG_TAG,"Attempting to delete patient who has assoicated prescription - caught exception.");
					Cursor prepCursor = getContentResolver().query(StorageProvider.PrescriptionColumns.CONTENT_URI, 
							Prescription.DEFAULT_PRESCRIPTION_PROJECTION, 
							"patient=?", 
							new String[] {String.valueOf(_id)}, 
							null);
					if(prepCursor.moveToFirst()){
						Prescription p = Prescription.fromCursor(prepCursor, getApplicationContext());
						new AlertDialog.Builder(this)
					    .setTitle("Scheduled prescription found for " + fName)
					    .setMessage(fName + " is scheduled to take " + p.getDrug().getBrandName() 
					    		+ ".  Please delete the scheduled prescription before deleting the user.")
					    //TODO: need to provide option to delete both from here
					    //don't do anything when the button is clicked
					    .setPositiveButton("Okay", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) {} })
					    .show();
					}else {
						Log.d(LOG_TAG,"Foreign key exception thrown, but no record found in prescription table for patient.");
						//TODO: what to do if this case encountered
					}
				}
			default:
				return super.onContextItemSelected(item);
		}
	}
	
}
