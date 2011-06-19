package com.risotto.view.wizard;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.risotto.R;
import com.risotto.storage.StorageProvider;
import com.risotto.view.drug.DrugAdd;

public class WhoWillBeTaking extends Activity implements OnClickListener {

	public static final String LOG_TAG = "com.risotto.view.wizard.WhoWillBeTaking";
	public static final String DATA_NEW_DRUG_NEEDED = "WhoWillBeTaking_NeedNewDrug";
	
	public static final String ACTION_WIZARD_NEW_DRUG = "com.risotto.view.wizard.WhoWillBeTaking.newDrug";
	
	private static String[] DRUG_PROJECTION = {
		StorageProvider.DrugColumns._ID,
		StorageProvider.DrugColumns.DRUG_BRAND_NAME,
		StorageProvider.DrugColumns.DRUG_STRENGTH,
		StorageProvider.DrugColumns.DRUG_STRENGTH_LABEL};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.wizard_gen_question_layout);
		
		Button me = (Button)findViewById(R.id.button_wizard_gen_question_layout_answer_one);
		me.setText(R.string.wizard_who_will_be_taking_me);
		me.setTextSize(30);
		me.setOnClickListener(this);
		
		Button other = (Button)findViewById(R.id.button_wizard_gen_question_layout_answer_two);
		other.setText(R.string.wizard_who_will_be_taking_other);
		other.setTextSize(30);
		other.setOnClickListener(this);
		
		TextView question = (TextView)findViewById(R.id.wizard_gen_question_layout_question_text);
		question.setText(R.string.wizard_who_will_be_taking_question);
		question.setTextSize(30);
		
		
	}

	public void onClick(View v) {
		
		//Check if the drug database has any drugs in it
		Cursor drugCursor = this.getContentResolver().query(
				StorageProvider.DrugColumns.CONTENT_URI, DRUG_PROJECTION, null, null, null);
		boolean goToNewDrug = true;
	
		//returned at least one result, so don't create new drug
		if(drugCursor != null && drugCursor.getCount() > 0) {
			goToNewDrug = false;
		}
		
		Intent intent = new Intent();
		
		intent.putExtra(DATA_NEW_DRUG_NEEDED, goToNewDrug);
		
		switch(v.getId()) {
			case R.id.button_wizard_gen_question_layout_answer_one:
				Log.d(LOG_TAG,"selected me.");
				if(goToNewDrug) {
					//launch new drug activity
					Log.d(LOG_TAG,"new drug.");
					intent.setClass(getApplicationContext(), OverCounterOrPrescription.class);
					startActivity(intent);
				} else {
					//launch select drug activity
					Log.d(LOG_TAG,"select drug.");
				}
				break;
			case R.id.button_wizard_gen_question_layout_answer_two:
				Log.d(LOG_TAG,"selected other");
				//pass goToNewDrug to select other activity
				break;
			default:
				break;
		
		}
		
	}
	
}
