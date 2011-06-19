package com.risotto.view.wizard;

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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.wizard_gen_question_layout);
		
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
		
		Drug wizardDrug = new Drug("");
		
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), DrugAdd.class);
		
		switch(v.getId()) {
			case R.id.button_wizard_gen_question_layout_answer_one:
				Log.d(LOG_TAG,"selected otc");
				wizardDrug.setType(Drug.TYPE.OVER_THE_COUNTER);
				intent.putExtra(WizardData.DRUG, wizardDrug);
				startActivity(intent);
				break;
			case R.id.button_wizard_gen_question_layout_answer_two:
				Log.d(LOG_TAG,"selected prep");
				wizardDrug.setType(Drug.TYPE.PRESCRIPTION);
				intent.putExtra(WizardData.DRUG, wizardDrug);
				startActivity(intent);
				break;
			default:
				break;
		
		}
		
	}
	
}
