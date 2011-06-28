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
import com.risotto.model.Prescription;

public class TakeItEveryDay extends Activity implements OnClickListener {
	
	public static final String LOG_TAG = "com.risotto.view.wizard.TakeItEveryDay";
	private HashMap<String,Object> wizardData = new HashMap<String,Object>();
	
	Prescription prep;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
		setContentView(R.layout.wizard_gen_question);
		
		try {
			  this.wizardData = WizardData.getData(getIntent().getExtras());
			  prep = (Prescription)wizardData.get(WizardData.PRESCRIPTION);
		  } catch (Exception e) {
			  Log.d(LOG_TAG,"No data found in intent.");
		}
		
		TextView question = (TextView) this.findViewById(R.id.wizard_gen_question_layout_question_text);
		question.setText(R.string.wizard_take_it_every_day_question);
		
		Button yes = (Button) this.findViewById(R.id.button_wizard_gen_question_layout_answer_one);
		yes.setOnClickListener(this);
		yes.setText(R.string.wizard_take_it_every_day_yes);
		
		Button no = (Button) this.findViewById(R.id.button_wizard_gen_question_layout_answer_two);
		no.setOnClickListener(this);
		no.setText(R.string.wizard_take_it_every_day_no);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * 
	 * Method called when button is clicked.
	 * 
	 */
	public void onClick(View v) {
		Intent nextQuestion = new Intent();		
		switch(v.getId()) {
			case R.id.button_wizard_gen_question_layout_answer_one:
				Log.d(LOG_TAG,"take every day");
				prep.setDoseType(Prescription.DOSE_TYPE_EVERY_DAY);
		    	nextQuestion.setClass(getApplicationContext(), HowOftenSchedule.class);
		    	nextQuestion.putExtra(WizardData.CONTENTS, wizardData);
				startActivity(nextQuestion);
				break;
			case R.id.button_wizard_gen_question_layout_answer_two:
				Log.d(LOG_TAG,"not taken every day");
				prep.setDoseType(Prescription.DOSE_TYPE_EVERY_DAY_OF_WEEK);
				nextQuestion.setClass(getApplicationContext(), WhatDaysTake.class);
				nextQuestion.putExtra(WizardData.CONTENTS, wizardData);
				startActivity(nextQuestion);
				//launch how often will you take this
				break;
			
		}
	}
	
}

