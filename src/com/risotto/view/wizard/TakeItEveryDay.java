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

public class TakeItEveryDay extends Activity implements OnClickListener {
	
	//the name of these strings will correspond to the answer that was clicked in this UI
	//they will reference the activity that is to be started next based on the answer
	
	public static final String LOG_TAG = "com.risotto.view.wizard.TakeItEveryDay";
	private HashMap<String,Object> wizardData = new HashMap<String,Object>();
	
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
		nextQuestion.putExtra(WizardData.CONTENTS, wizardData);
		switch(v.getId()) {
			case R.id.button_wizard_gen_question_layout_answer_one:
				Log.d(LOG_TAG,"take every day");
		    	nextQuestion.setClass(getApplicationContext(), HowOftenSchedule.class);
				startActivity(nextQuestion);
				break;
			case R.id.button_wizard_gen_question_layout_answer_two:
				Log.d(LOG_TAG,"not taken every day");
				nextQuestion.setClass(getApplicationContext(), WhatDaysTake.class);
				startActivity(nextQuestion);
				//launch how often will you take this
				break;
			
		}
	}
	
}

