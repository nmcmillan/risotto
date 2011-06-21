package com.risotto.view.wizard;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.risotto.R;
import com.risotto.model.Drug;
import com.risotto.model.Prescription;

public class EnterDrugDetails extends Activity implements OnClickListener,OnItemSelectedListener{

	public static final String LOG_TAG = "com.risotto.view.wizard.EnterDrugDetails";
	
	private static final String[] DRUG_LABEL_POSITIONS = { "Pills","mL","mg","oz","Tablespoons" }; 
	
	private Drug newDrug;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		newDrug = (Drug)extras.getSerializable(WizardData.DRUG);
		
		setContentView(R.layout.wizard_drug_details_layout);
		
		Spinner spinner = (Spinner) this.findViewById(R.id.wizard_enter_drug_details_layout_spinner);
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
		
		if(newDrug.getForm().equals(Drug.FORM.CAPSULES) ||
		   newDrug.getForm().equals(Drug.FORM.TABLETS)) {
			spinner.setSelection(0);
		} else if(newDrug.getForm().equals(Drug.FORM.LIQUIDS))
			spinner.setSelection(4);
		else
			spinner.setSelection(5);
		
		Button next = (Button) this.findViewById(R.id.button_enter_drug_details_layout_next);
		next.setOnClickListener(this);
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		String form = (String)parent.getItemAtPosition(position);
		
		if(form.equalsIgnoreCase(Drug.LABEL.MG.toString()))
			newDrug.setStrengthLabel("mg");
		else if(form.equalsIgnoreCase(Drug.LABEL.ML.toString()))
			newDrug.setStrengthLabel("mL");
		else if(form.equalsIgnoreCase(Drug.LABEL.OZ.toString()))
			newDrug.setStrengthLabel("oz");
		else if(form.equalsIgnoreCase(Drug.LABEL.PILLS.toString()))
			newDrug.setStrengthLabel("pills");
		else if(form.equalsIgnoreCase(Drug.LABEL.TABLESPOONS.toString()))
			newDrug.setStrengthLabel("tablespoons");
		else
			newDrug.setStrengthLabel("");
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

	public void onClick(View v) {
		Log.d(LOG_TAG,"in onclick");
		
		
		
	}
	
}
