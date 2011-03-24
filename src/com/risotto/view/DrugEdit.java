package com.risotto.view;

import android.R;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.risotto.storage.StorageProvider;

/**
 * Activity that will edit the details of a drug or add a new drug.
 * If it's editing an existing drugs, the fields will be populated with the
 * details of that drug.
 * 
 * @author nick
 *
 */

public class DrugEdit extends Activity implements View.OnClickListener {
	
	public static final String EDIT_DRUG_ACTION="com.risotto.action.EDIT_DRUG";
	
	//Declare a PROJECTION - a 'filter' to the content provider to only
	//return specific fields during a query
	private static String[] PROJECTION = { 
		StorageProvider.DrugColumns.DRUG_NAME,
		StorageProvider.DrugColumns.DRUG_STRENGTH,
	};
	
	//Return type of queries
	private Cursor mCursor;
	
	//EditText field - will probably need one of these for each drug attribute
	private EditText drugNameText, drugStrengthText;
	
	//Uri for accessing DB
	private Uri drugUri;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//setContentView(R.layout.);
	}
	
	@Override
	public void onPause() {
		
	}
	
	public void onClick(View v) {
		
	}
}
