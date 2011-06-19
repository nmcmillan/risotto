package com.risotto.view.wizard;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.risotto.R;
import com.risotto.model.Drug;

public class EnterDrugDetails extends Activity implements OnClickListener,OnItemSelectedListener{

	public static final String LOG_TAG = "com.risotto.view.wizard.EnterDrugDetails";
	
	private static final String[] DRUG_LABEL_POSITIONS = { "Pills","mL","mg","oz","Tablespoons" }; 
	
	private Drug newDrug;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		Bundle extras = getIntent().getExtras();
		newDrug = (Drug)extras.getSerializable(WizardData.DRUG);
		
		setContentView(R.layout.wizard_drug_details_layout);
		
		Spinner spinner = (Spinner) this.findViewById(R.id.wizard_enter_drug_details_layout_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.drug_form_label_array, android.R.layout.simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		
		if(newDrug.getForm() == Drug.FORM.CAPSULES ||
		   newDrug.getForm() == Drug.FORM.TABLETS) {
			
		}
		
		Button next = (Button) this.findViewById(R.id.button_enter_drug_details_layout_next);
		next.setOnClickListener(this);
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

	public void onClick(View v) {
		
	}
	
}
