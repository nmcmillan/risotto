package com.risotto.view.wizard;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.risotto.R;
import com.risotto.model.Drug;
import com.risotto.model.Patient;
import com.risotto.model.Prescription;

public class EnterDrugDetails extends Activity implements OnClickListener,OnItemSelectedListener{

	public static final String LOG_TAG = "com.risotto.view.wizard.EnterDrugDetails";
	
	private static final String[] DRUG_LABEL_POSITIONS = { "Pills","mL","mg","oz","Tablespoons" };
	private HashMap<String,Object> wizardData = new HashMap<String,Object>();
	
	private Drug drug;
	private Patient patient;
	private Prescription prep;
	
	private TextView sizeOneDose;
	private TextView totalQuantity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.wizard_enter_drug_details);
		
		try {
			  this.wizardData = WizardData.getData(getIntent().getExtras());
		  } catch (Exception e) {
			  Log.d(LOG_TAG,"No data found in intent.");
		  }
		
		drug = (Drug)wizardData.get(WizardData.DRUG);
		patient = (Patient)wizardData.get(WizardData.PATIENT);
		
		//Print out what the information received so far is:
		Log.d(LOG_TAG,"Drug info: Name : " + drug.getBrandName());
		Log.d(LOG_TAG,"Drug info: Form : " + drug.getForm());
		Log.d(LOG_TAG,"Drug info: Type : " + drug.getType().toString());
		
		Log.d(LOG_TAG,"Patient info: First Name : " + patient.getFirstName());
		Log.d(LOG_TAG,"Patient info: Last : " + patient.getLastName());
		
		Spinner spinner = (Spinner) this.findViewById(R.id.wizard_enter_drug_details_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.drug_form_label_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		
		/** 
		 * Order of array 
		 * Pills - 0
		 * mL - 1
		 * mg - 2
		 * oz - 3
		 * Tablespoons - 4
		 * other - 5
		 * 
		 * Taken from strings.xml (drug label array)
		 */
		
		if(drug.getForm().equals(Drug.FORM.CAPSULES) ||
		   drug.getForm().equals(Drug.FORM.TABLETS)) {
			spinner.setSelection(0);
		} else if(drug.getForm().equals(Drug.FORM.LIQUIDS))
			spinner.setSelection(4);
		else
			spinner.setSelection(5);
		
		Button next = (Button) this.findViewById(R.id.button_enter_drug_details_next);
		next.setOnClickListener(this);
		
		sizeOneDose = (TextView) this.findViewById(R.id.wizard_enter_drug_details_dose_size);
		totalQuantity = (TextView) this.findViewById(R.id.wizard_enter_drug_details_total_quantity);
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		String form = (String)parent.getItemAtPosition(position);
		
		if(form.equalsIgnoreCase(Drug.LABEL.MG.toString()))
			drug.setStrengthLabel("mg");
		else if(form.equalsIgnoreCase(Drug.LABEL.ML.toString()))
			drug.setStrengthLabel("mL");
		else if(form.equalsIgnoreCase(Drug.LABEL.OZ.toString()))
			drug.setStrengthLabel("oz");
		else if(form.equalsIgnoreCase(Drug.LABEL.PILLS.toString()))
			drug.setStrengthLabel("pills");
		else if(form.equalsIgnoreCase(Drug.LABEL.TABLESPOONS.toString()))
			drug.setStrengthLabel("tablespoons");
		else
			drug.setStrengthLabel("");
	}

	public void onClick(View v) {
		Log.d(LOG_TAG,"in onclick");
		
		if(sizeOneDose.getText().toString().trim().equals("")) {
			new AlertDialog.Builder(this)
		    .setTitle("Dose size empty!")
		    .setMessage("Oops - the dose size can't be empty!")
		    //don't do anything when the button is clicked
		    .setPositiveButton("Okay", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) {} })
		    .show();
		} else if (totalQuantity.getText().toString().trim().equals("") && drug.getType().equals(Drug.TYPE.PRESCRIPTION)){
			new AlertDialog.Builder(this)
		    .setTitle("Total quantity empty!")
		    .setMessage("Oops - the total quantity of the drug can't be empty!")
		    //don't do anything when the button is clicked
		    .setPositiveButton("Okay", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) {} })
		    .show();
		} else {
			int size = -1;
			int total = -1;
			if (totalQuantity.getText().toString().trim().equals("") && drug.getType().equals(Drug.TYPE.OVER_THE_COUNTER)){
				new AlertDialog.Builder(this)
			    .setTitle("Cancel Reminder")
			    .setMessage("Since you haven't entered a total quanity, we won't know when to stop the reminders, so you'll have to cancel them manually.")
			    //don't do anything when the button is clicked
			    .setPositiveButton("Okay", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) {} })
			    .show();
				total = 0;
			} else {
				total = Integer.parseInt(totalQuantity.getText().toString().trim());
			}
			
			size = Integer.parseInt(sizeOneDose.getText().toString().trim());
			
			prep = new Prescription(patient,drug,Prescription.DOSE_TYPE_OTHER,size,total);
			wizardData.put(WizardData.PRESCRIPTION, prep);
			
			Intent intent = new Intent();
			intent.putExtra(WizardData.CONTENTS, wizardData);
			intent.setClass(getApplicationContext(), WhenTakeIt.class);
			startActivity(intent);
		}
		
				
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
