/**
 * 
 */
package com.risotto.view;

import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.risotto.R;
import com.risotto.model.Drug;
import com.risotto.storage.StorageProvider;

/**
 * @author nick
 *
 */
public class DrugDetailsView extends Activity {
	
	public static final String LOG_TAG = "DrugDetailsView";
	private Drug editDrug = null;
	
	private static String[] PROJECTION = {
		StorageProvider.DrugColumns._ID,
		StorageProvider.DrugColumns.DRUG_BRAND_NAME,
		StorageProvider.DrugColumns.DRUG_STRENGTH,
	};

	/* 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG,"onCreate called successfully");
		
		setContentView(R.layout.drug_details_layout);
		
		String drugID = getIntent().getStringExtra(DrugView.DRUG_DETAILS_DB_ID);
		
		Log.d(LOG_TAG,"Drug id: " + drugID);
		
		Uri drugUri = StorageProvider.DrugColumns.CONTENT_URI.buildUpon().appendPath(drugID).build();
		
		Cursor dCursor = this.getContentResolver().query(drugUri, PROJECTION, null, null, null);
		
		dCursor.moveToFirst();
		
		try {
			editDrug = Drug.fromCursor(dCursor);
		} catch (Exception e) {
			Log.d(LOG_TAG,"Exception thrown:");
			e.printStackTrace();
		}
		
		TextView dNameView = (TextView) this.findViewById(R.id.drug_details_drug_name);
		dNameView.setText(editDrug.getMedicalName());
		
		TextView dStrengthView = (TextView) this.findViewById(R.id.drug_details_drug_strength);
		Vector<String> strength = editDrug.getStrength();
		ListIterator<String> li = strength.listIterator();
		//can do this because for a drug to be in the DB, it must have at least one strength
		try {
			String strenString = li.next() + " mg ";
			while(li.hasNext()) {
				strenString += ", " + li.next() + " mg ";
			}
			dStrengthView.setText(strenString);
			dStrengthView.setTypeface(Typeface.create("null", Typeface.ITALIC));
		} catch(NoSuchElementException e) {
			Log.d(LOG_TAG,"No elements in strength vector.");
		}
		
	}

	/* 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/* 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	/* 
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	/* 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/* 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	/*
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
