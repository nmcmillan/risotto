package com.risotto.view.drug;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.risotto.R;
import com.risotto.model.Drug;
import com.risotto.model.Patient;
import com.risotto.storage.StorageProvider;
import com.risotto.view.wizard.EnterDrugDetails;
import com.risotto.view.wizard.WizardData;

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
	private HashMap<String,Object> wizardData = new HashMap<String,Object>();
	
	//Declare a PROJECTION - a 'filter' to the content provider to only
	//return specific fields during a query
	private static String[] PROJECTION = { 
		StorageProvider.DrugColumns._ID,
		StorageProvider.DrugColumns.DRUG_BRAND_NAME,
	};
	
	private EditText drugNameEditText;
	private Drug.FORM dForm;
	private Drug newDrug;
	private Patient newPatient;
	private boolean inWizard = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(LOG_TAG,"onCreate called");
		
		setContentView(R.layout.drug_add_layout);
		
		/**
		 * check if drug object was added to intent
		 *	- we're in the wizard
		 *	- a drug object with only the type (OTC / prep) is in the data, extract
		 *
		 * check if patient object was added to intent
		 * 	- we came from patient add
		 * 	- a patient object is in intent, pull it out
		 */	
		try {
			  this.wizardData = WizardData.getData(getIntent().getExtras());
			  inWizard = true;
			  if(wizardData.containsKey(WizardData.DRUG)) {
					newDrug = (Drug)wizardData.get(WizardData.DRUG);
					Log.d(LOG_TAG, newDrug.getType().toString());
			  } else
					newDrug = new Drug("");
				
			  if(wizardData.containsKey(WizardData.PATIENT)) {
					newPatient = (Patient)wizardData.get(WizardData.PATIENT);
					Log.d(LOG_TAG, newPatient.getFirstName());
			  }
		  } catch (IllegalArgumentException ie) {
			  Log.d(LOG_TAG,"No data found in intent.");
		}
		
		
		drugNameEditText = (EditText) this.findViewById(R.id.drug_add_layout_brand_name_field);
		
		Spinner spinner = (Spinner) this.findViewById(R.id.drug_add_layout_form_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.drug_types_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		
		Button b = (Button) this.findViewById(R.id.button_drug_add_layout_next);
		b.setOnClickListener(this);
		if(inWizard)
			b.setText("Next...");
	}
	
	public void onClick(View v) {
		String enteredName = drugNameEditText.getText().toString().trim();
		if(0 == enteredName.length()) {
			//name is incorrect - display dialog telling user
			new AlertDialog.Builder(this)
		    .setTitle("Drug Name")
		    .setMessage("The drug name can't be empty, please try again!")
		    .setPositiveButton("Okay", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) {} })
		    .show();
			this.drugNameEditText.requestFocus();
		}
		else {
			if(inWizard) {
				newDrug.setBrandName(enteredName);
				newDrug.setForm(dForm);
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), EnterDrugDetails.class);
				wizardData.put(WizardData.DRUG, newDrug);
				intent.putExtra(WizardData.CONTENTS, wizardData);
				startActivity(intent);
			} else {
				newDrug = new Drug(enteredName);
				newDrug.setForm(dForm);
				ContentValues cv = newDrug.toContentValues();
				Uri newDrugUri = this.getContentResolver().insert(StorageProvider.DrugColumns.CONTENT_URI, cv);
				Log.d(LOG_TAG,"finished adding drug; uri = " + newDrugUri);
				finish();
			}
			
		}
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		Log.d(LOG_TAG,"item selected: " + parent.getItemAtPosition(position));
		String form = (String)parent.getItemAtPosition(position);
		Log.d(LOG_TAG, Drug.FORM.CAPSULES.toString());
		
		if(form.equalsIgnoreCase(Drug.FORM.CAPSULES.toString()))
			dForm = Drug.FORM.CAPSULES;
		else if(form.equalsIgnoreCase(Drug.FORM.TABLETS.toString()))
			dForm = Drug.FORM.TABLETS;
		else if(form.equalsIgnoreCase(Drug.FORM.POWDERS.toString()))
			dForm = Drug.FORM.POWDERS;
		else if(form.equalsIgnoreCase(Drug.FORM.DROPS.toString()))
			dForm = Drug.FORM.DROPS;
		else if(form.equalsIgnoreCase(Drug.FORM.LIQUIDS.toString()))
			dForm = Drug.FORM.LIQUIDS;
		else if(form.equalsIgnoreCase(Drug.FORM.SPRAY.toString()))
			dForm = Drug.FORM.SPRAY;
		else if(form.equalsIgnoreCase(Drug.FORM.SKIN.toString()))
			dForm = Drug.FORM.SKIN;
		else if(form.equalsIgnoreCase(Drug.FORM.SUPPOSITORIES.toString()))
			dForm = Drug.FORM.SUPPOSITORIES;
		else if(form.equalsIgnoreCase(Drug.FORM.OTHER.toString()))
			dForm = Drug.FORM.OTHER;
		else
			throw new IllegalArgumentException("Unknown drug form given.: " + form);
		
		Log.d(LOG_TAG, "form: " + dForm.toString());
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {}
}
