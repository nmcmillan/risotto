package com.risotto.view.drug;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView;

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

public class DrugAdd extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
	
	public static final String EDIT_DRUG_ACTION="com.risotto.action.EDIT_DRUG";
	
	private static final String LOG_TAG = "DrugAdd";
	
	//Declare a PROJECTION - a 'filter' to the content provider to only
	//return specific fields during a query
	private static String[] PROJECTION = { 
		StorageProvider.DrugColumns._ID,
		StorageProvider.DrugColumns.DRUG_BRAND_NAME,
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
		
		setContentView(R.layout.drug_add_layout);
		
		drugNameEditText = (EditText) this.findViewById(R.id.drug_add_field_name);
		drugNameEditText.setHint(R.string.drug_add_name);
		//drugNameText.setOnClickListener(this);
		
		drugStrengthEditText = (EditText) this.findViewById(R.id.drug_add_field_strength);
		drugStrengthEditText.setHint(R.string.drug_add_strength);
		//drugStrengthText.setOnClickListener(this);
		
		Spinner spinner = (Spinner) this.findViewById(R.id.drug_add_type_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.drug_types_array, android.R.layout.simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		Button b = (Button) this.findViewById(R.id.button_drug_edit_ok);
		b.setOnClickListener(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();		
	}
	
	public void onClick(View v) {
		//question, if we attach more than one item to the click listener, how do we distinguish?
		
		//will only be called when 'ok' is called because it's the only view attached to a clicklistener
		
		//called when button is clicked & when text field is clicked b/c setOnClickListener attached to it
		Log.d(LOG_TAG,"onClick");
		
		//TO DO: sanitize inputs to protect against SQL injection
		
		boolean validStrength = true;
		boolean validName = true;
		int enteredStrength = -1;
		
		String enteredName = drugNameEditText.getText().toString().trim();
		
		if(0 == enteredName.length()) {
			validName = false;
		}
		try {
			enteredStrength = Integer.parseInt(drugStrengthEditText.getEditableText().toString());
		} catch (NumberFormatException e) {
			validStrength = false;
		}
		if(!validStrength && !validName) {
			//both are incorrect, display appropriate message
			new AlertDialog.Builder(this)
		    .setTitle("Drug Info Incorrect")
		    .setMessage("Oops - the drug name can't be empty and the drug strength must be a number, try again!")
		    //don't do anything when the button is clicked
		    .setPositiveButton("Okay", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) {} })
		    .show();
		}
		else if(!validName) {
			//just name is incorrect
			new AlertDialog.Builder(this)
		    .setTitle("Drug Name")
		    .setMessage("The drug name can't be empty, please try again!")
		    .setPositiveButton("Okay", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) {} })
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
				Drug newDrug = new Drug(enteredName,Drug.TYPE.DEFAULT,enteredStrength,"");
				ContentValues cv = newDrug.toContentValues();
				Uri newDrugUri = this.getContentResolver().insert(StorageProvider.DrugColumns.CONTENT_URI, cv);
				Log.d(LOG_TAG,"finished adding drug; uri = " + newDrugUri);
		}
		
		finish();
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
