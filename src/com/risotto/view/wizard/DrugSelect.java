package com.risotto.view.wizard;

import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.risotto.R;
import com.risotto.model.Drug;
import com.risotto.storage.StorageProvider;
import com.risotto.view.drug.DrugAdd;

public class DrugSelect extends ListActivity implements View.OnClickListener {
	public static final String LOG_TAG = "com.risotto.view.wizard.DrugSelect";
	
	private HashMap<String,Object> wizardData = new HashMap<String,Object>();
	
	private static String[] DRUG_PROJECTION = {
		StorageProvider.DrugColumns._ID,
		StorageProvider.DrugColumns.DRUG_BRAND_NAME,
		StorageProvider.DrugColumns.DRUG_FORM,
	};
		
	private static String[] VIEW_DB_COLUMN_MAPPING = {
		StorageProvider.DrugColumns.DRUG_BRAND_NAME,
		StorageProvider.DrugColumns.DRUG_FORM,
	};
	private static int[] VIEW_ID_MAPPING = {
		R.id.wizard_drug_select_list_view_brand_name,
		R.id.wizard_drug_select_list_view_form
	};
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  
		  setContentView(R.layout.wizard_drug_select);
		  
		  Cursor drugCursor = getContentResolver().query(
				  StorageProvider.DrugColumns.CONTENT_URI, DRUG_PROJECTION, null, null, null);
		  
		  try {
			  this.wizardData = WizardData.getData(getIntent().getExtras());
		  } catch (Exception e) {
			  Log.d(LOG_TAG,"No data found in intent.");
		  }
		  
		  startManagingCursor(drugCursor);

		  Log.d(LOG_TAG,"count " + drugCursor.getCount());
		  
		  Button addDrug = (Button)findViewById(R.id.button_wizard_drug_select_add_new_drug);
		  addDrug.setOnClickListener(this);
		  
		  SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				  this,
				  R.layout.wizard_drug_select_list_view,
				  drugCursor,
				  VIEW_DB_COLUMN_MAPPING,
				  VIEW_ID_MAPPING);
		  
		 setListAdapter(adapter);
		  
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d(LOG_TAG,"on resume");
		
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d(LOG_TAG,"on start");
	}
	
	
	/**
	 * When user selects a patient from the list, it needs to be added to the intent.
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), EnterDrugDetails.class);
		
		Uri drugUri = StorageProvider.DrugColumns.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
		
		Cursor dCursor = getContentResolver().query(
				drugUri, 
				DRUG_PROJECTION, 
				null, 
				null, 
				null);
		
		if(null != dCursor) {
			dCursor.moveToFirst();
			Drug drug = Drug.fromCursor(dCursor);
			wizardData.put(WizardData.DRUG, drug);
			intent.putExtra(WizardData.CONTENTS,wizardData);
		}
		else {
			Log.d(LOG_TAG,"Couldn't find patient in database.");
		}
		dCursor.close();
		
		startActivity(intent);
	}

	public void onClick(View v) {
		//only one button in this class, so no need to check view id
		
		Intent intent = new Intent();
		intent.putExtra(WizardData.CONTENTS, wizardData);
		intent.setClass(getApplicationContext(), DrugAdd.class);
		startActivity(intent);
		
	}
}