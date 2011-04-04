package com.risotto.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.risotto.R;
import com.risotto.model.Drug;
import com.risotto.storage.StorageProvider;

/**
 * Activity that will edit the details of a drug or add a new drug.
 * If it's editing an existing drugs, the fields will be populated with the
 * details of that drug.
 * 
 * @author nick
 *
 */

public class DrugAdd extends Activity implements View.OnClickListener {
	
	public static final String EDIT_DRUG_ACTION="com.risotto.action.EDIT_DRUG";
	
	private static final String LOG_TAG = "DrugAdd";
	
	//Declare a PROJECTION - a 'filter' to the content provider to only
	//return specific fields during a query
	private static String[] PROJECTION = { 
		StorageProvider.DrugColumns._ID,
		StorageProvider.DrugColumns.DRUG_NAME,
		StorageProvider.DrugColumns.DRUG_STRENGTH,
	};
	
	//Return type of queries
	private Cursor drugCursor;
	
	//EditText field - will probably need one of these for each drug attribute
	private EditText drugNameEditText;
	private EditText drugStrengthEditText;
	
	//Uri for accessing DB
	private Uri drugUri;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(LOG_TAG,"onCreate called");
		
		setContentView(R.layout.drug_editor);
	
		//get the URI of the drug we're looking to edit
		//TO DO: the list menu must have to package this before
		//it sends off the intent
		drugUri = getIntent().getData();
		drugCursor = managedQuery(drugUri,PROJECTION,null,null,null);
		
		//find the objects that we want to display and set it to be run
		//when it is clicked, which will call the onClick()
		
		//do we need to dynamically add EditText boxes as needed?
		
		drugNameEditText = (EditText) this.findViewById(R.id.drug_add_field_name);
		if(null == this.drugNameEditText)
			Log.d(LOG_TAG,"drugNameEditText is null");
		else
			drugNameEditText.setHint("Drug name...");
		//drugNameText.setOnClickListener(this);
		
		drugStrengthEditText = (EditText) this.findViewById(R.id.drug_add_field_strength);
		drugStrengthEditText.setHint("Drug strength...");
		//drugStrengthText.setOnClickListener(this);
		
		Button b = (Button) this.findViewById(R.id.button_drug_edit_ok);
		b.setOnClickListener(this);
		
		
		
	}
	
	@Override
	public void onPause() {
		super.onPause();		
	}
	
	public void onClick(View v) {
		//question, if we attach more than one item to the click listener, how do we distinguish?
		
		//this will only be called when 'ok' is called because it's the only view attached to a clicklistener
		
		//called when button is clicked & when text field is clicked b/c setOnClickListener attached to it
		//NOTE: if an activity overtakes this one (phone call, user presses home button) any saving will need
		//		to be done in onPause() --> this is already being done
		Log.d(LOG_TAG,"onClick");
		
		//TO DO: sanitize inputs to protect against SQL injection
		
		boolean validStrength = true;
		boolean validName = true;
		String enteredStrength = "";
		
		String enteredName = drugNameEditText.getText().toString().trim();
		
		if(0 == enteredName.length()) {
			validName = false;
		}
		try {
			int x = Integer.parseInt(drugStrengthEditText.getEditableText().toString());
			enteredStrength = drugStrengthEditText.getEditableText().toString(); 		
		} catch (NumberFormatException e) {
			validStrength = false;
		}
		if(!validStrength && !validName) {
			//both are incorrect, display appropriate message
			new AlertDialog.Builder(this)
		    .setTitle("Drug Info Incorrect")
		    .setMessage("Whoops - the drug name can't be empty and the drug strength must be a number, try again!")
		    //don't do anything when the button is clicked
		    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {}
			})
		    .show();
		}
		else if(!validName) {
			//just name is incorrect
			new AlertDialog.Builder(this)
		    .setTitle("Drug Name")
		    .setMessage("The drug name can't be empty, please try again!")
		    //don't do anything when the button is clicked
		    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {}
			})
		    .show();
			this.drugNameEditText.requestFocus();
		}
		else if(!validStrength) {
			//just strength is incorrect
			new AlertDialog.Builder(this)
		    .setTitle("Drug Strength")
		    .setMessage("It doesn't appear the drug strength you entered is applicable, try entering it again!")
		    //don't do anything when the button is clicked
		    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {}
			})
		    .show();
			this.drugStrengthEditText.requestFocus();
		}
		else {
			//Basic info checks done, now search for drug to see if it's in database
			//Cursor storedDrugs = this.getContentResolver().query(StorageProvider.DrugColumns.CONTENT_URI, PROJECTION, null, null, null);
			String whereClause = StorageProvider.DrugColumns.DRUG_NAME + "=" + "'" + enteredName + "'";
		
			//First run a query to see if the drug is already in the database.
			Cursor dCursor = this.getContentResolver().query(
						StorageProvider.DrugColumns.CONTENT_URI, 
						PROJECTION, 
						whereClause, 
						null, 
						null);
			
			if(dCursor.getCount() > 0) {
				dCursor.moveToFirst();
				Drug existingDrug = Drug.fromCursor(dCursor);
				existingDrug.addStrength(enteredStrength);
				int id = existingDrug.get_id();
				Uri uri = StorageProvider.DrugColumns.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
				ContentValues cv = existingDrug.toContentValues();
				this.getContentResolver().update(uri, cv, null, null);
			}
			else {
				Drug newDrug = new Drug(0,enteredStrength,enteredName);
				ContentValues cv = newDrug.toContentValues();
				Uri newDrugUri = this.getContentResolver().insert(StorageProvider.DrugColumns.CONTENT_URI, cv);
				Log.d(LOG_TAG,"finished adding drug; uri = " + newDrugUri);
			}
			
			/*Code for iterating over cursor
			while(dCursor.moveToNext()) {
				Drug d = Drug.fromCursor(dCursor);
				Log.d(LOG_TAG,d.getMedicalName());
			}*/
			
			finish();
		}
		
		
		
	}
}
