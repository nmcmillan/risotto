package com.risotto.view.wizard;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.risotto.R;
import com.risotto.model.Drug;
import com.risotto.view.drug.DrugAdd;

public class OverCounterOrPrescription extends Activity implements OnClickListener {

	public static final String LOG_TAG = "com.risotto.view.wizard.OverCounterOrPrescriptiong";
	
	private HashMap<String,Object> wizardData = new HashMap<String,Object>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.wizard_gen_question);
		
		try {
			  this.wizardData = WizardData.getData(getIntent().getExtras());
		  } catch (Exception e) {
			  Log.d(LOG_TAG,"No data found in intent.");
			  e.printStackTrace();
		}
		
		Button me = (Button)findViewById(R.id.button_wizard_gen_question_layout_answer_one);
		me.setText(R.string.wizard_over_counter_or_prescription_otc);
		me.setTextSize(30);
		me.setOnClickListener(this);
		
		Button other = (Button)findViewById(R.id.button_wizard_gen_question_layout_answer_two);
		other.setText(R.string.wizard_over_counter_or_prescription_prep);
		other.setTextSize(30);
		other.setOnClickListener(this);
		
		TextView question = (TextView)findViewById(R.id.wizard_gen_question_layout_question_text);
		question.setText(R.string.wizard_over_counter_or_prescription_question);
		question.setTextSize(30);
		
		
	}

	public void onClick(View v) {
		
		Drug drug = new Drug("");
		
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), DrugAdd.class);
		
		switch(v.getId()) {
			case R.id.button_wizard_gen_question_layout_answer_one:
				Log.d(LOG_TAG,"selected otc");
				drug.setType(Drug.TYPE.OVER_THE_COUNTER);
				break;
			case R.id.button_wizard_gen_question_layout_answer_two:
				Log.d(LOG_TAG,"selected prep");
				drug.setType(Drug.TYPE.PRESCRIPTION);
				break;
			default:
				break;
		
		}
		
		wizardData.put(WizardData.DRUG, drug);
		intent.putExtra(WizardData.CONTENTS, wizardData);
		startActivity(intent);
		
	}
	
}
