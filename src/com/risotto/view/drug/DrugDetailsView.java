/**
 * 
 */
package com.risotto.view.drug;

import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.risotto.R;
import com.risotto.model.Drug;
import com.risotto.service.MainService;
import com.risotto.storage.StorageProvider;

/**
 * @author nick
 *
 */
public class DrugDetailsView extends Activity {
	
	public static final String LOG_TAG = "DrugDetailsView";
	
	private String drugName;
	private String drugID;
	private Drug editDrug;
	private Uri drugUri;
	
	private static String[] PROJECTION = {
		StorageProvider.DrugColumns._ID,
		StorageProvider.DrugColumns.DRUG_BRAND_NAME,
		StorageProvider.DrugColumns.DRUG_STRENGTH,
		StorageProvider.DrugColumns.DRUG_STRENGTH_LABEL
	};
	
	public static final int MENU_ITEM_REMOVE_POSITION = Menu.FIRST;
	public static final int MENU_ITEM_EDIT_POSITION = Menu.FIRST + 1;

	/* 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG,"onCreate called successfully");
		
		setContentView(R.layout.drug_details_layout);
		
		drugID = getIntent().getStringExtra(DrugView.DRUG_DETAILS_DB_ID);
		drugName = getIntent().getStringExtra(DrugView.DRUG_DETAILS_BRAND_NAME);
		
		drugUri = StorageProvider.DrugColumns.CONTENT_URI.buildUpon().appendPath(drugID).build();
		
		Cursor dCursor = this.getContentResolver().query(drugUri, PROJECTION, null, null, null);
		
		dCursor.moveToFirst();
		
		editDrug = Drug.fromCursor(dCursor);
	
		TextView dNameView = (TextView) this.findViewById(R.id.drug_details_drug_name);
		dNameView.setText(drugName);
		
		TextView dStrengthView = (TextView) this.findViewById(R.id.drug_details_drug_strength);
		dStrengthView.setText(editDrug.getPrintableStrength());
		dStrengthView.setTypeface(Typeface.create("null", Typeface.ITALIC));
		
		dCursor.close();
	}

	/**
	 * Handles actions when buttons from the menu created in
	 * 'onCreateOptionsMenu'
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case MENU_ITEM_REMOVE_POSITION:
			//remove drug from database
			Log.d(LOG_TAG, "You clicked remove drug: " + editDrug.getBrandName());
			getContentResolver().delete(drugUri, null, null);
			finish();
			return true;
		case MENU_ITEM_EDIT_POSITION:
			Log.d(MainService.LOG_TAG, "You clicked edit drug: " + editDrug.getBrandName());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * This method is only called once and that's the first time the options
	 * menu is displayed.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		/*
		 * MenuInflater inflater = getMenuInflater();
		 * inflater.inflate(R.layout.drug_menu_layout, menu); return true;
		 */
		menu.add(Menu.NONE, // group id for doing batch changes
				MENU_ITEM_REMOVE_POSITION, // position
				Menu.NONE, // order, see getOrder()
				R.string.drug_add_menu_remove) // name of button - link to XML
				.setIcon(android.R.drawable.ic_menu_delete);
		menu.add(Menu.NONE,
				MENU_ITEM_EDIT_POSITION,
				Menu.NONE,
				R.string.drug_add_menu_edit)
				.setIcon(android.R.drawable.ic_menu_edit);

		return true;
	}

}
