package com.risotto.view;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hello.R;
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
	private Cursor drugCursor;
	
	//EditText field - will probably need one of these for each drug attribute
	private EditText drugNameText;//, drugStrengthText;
	
	//Uri for accessing DB
	private Uri drugUri;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.drug_editor);
		
		//get the URI of the drug we're looking to edit
		//TO DO: the list menu must have to package this before
		//it sends off the intent
		drugUri = getIntent().getData();
		
		drugCursor = managedQuery(drugUri,PROJECTION,null,null,null);
		
		//find the objects that we want to display and set it to be run
		//when it is clicked, which will call the onClick()
		drugNameText = (EditText) this.findViewById(R.id.drug_edit_title);
		drugNameText.setOnClickListener(this);
		
		//drugStrengthText = (EditText) this.findViewById(R.id.)
		
		Button b = (Button) this.findViewById(R.id.button_drug_edit_ok);
		b.setOnClickListener(this);
		
	}
	
	@Override
	public void onPause() {
		
	}
	
	public void onClick(View v) {
		finish();
	}
}
