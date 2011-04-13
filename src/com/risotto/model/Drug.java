package com.risotto.model;

import java.util.ListIterator;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.util.Log;
import android.net.Uri;
import android.util.Log;

import com.risotto.storage.StorageProvider;

public class Drug {
	
	public static final String LOG_TAG = "RISOTTO_DRUG";
	
	// Required Fields
	private String brandName;
	
	// Optional Fields
	private String genericName;
	private String manufacturer;
	
	// Possible drug details
	private Vector<DrugDetails> drugDetails;
	
	// Unique id used for storage references
	private int _id;
	private static final int INVALID_ID = -1;
 	
 	public Drug() {
 		this("");
 	}
	
	public Drug(String brandName) {
		this(INVALID_ID, brandName);
	}
	
	public Drug(String brandName, DrugDetails drugDetails) {
		this(INVALID_ID, brandName, drugDetails);
	}
	
	private Drug(int _id, String brandName) {
		this._id = _id;
		this.brandName = brandName;
	}
	
	private Drug(int _id, String brandName, DrugDetails drugDetails) {
		this._id = _id;
		this.brandName = brandName;
		// Call for an add to the vector
		this.addDrugDetails(drugDetails);
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}
	
	public boolean addDrugDetails(DrugDetails drugDetail) {
		if (this.drugDetails == null) {
			this.drugDetails = new Vector<DrugDetails>();
		}
		
		// TODO Make sure that we are not adding duplicates.
		
		return this.drugDetails.add(drugDetail);
	}
	
	public boolean removeDrugDetails(DrugDetails drugDetail) {
		if (this.drugDetails == null) {
			return false;
		} else {
			// TODO Do some checking to make sure that we are removing the correct detail based on the id.
			return this.drugDetails.remove(drugDetail);
		}
	}
	
	public Vector<DrugDetails> getDrugDetails() {
		return this.drugDetails;
	}
	
	public int storeDrugAndDetails(Context context) {
		// Create a new 'ContentValues' to store our values
		ContentValues drugValues = new ContentValues();
		
		/**
		 * STORE ALL REQUIRED FIELDS.
		 */
		drugValues.put(StorageProvider.DrugColumns.DRUG_BRAND_NAME, this.getBrandName());
		
		/**
		 * STORE ANY OPTIONAL FIELDS.
		 */
		// Store the generic name if not empty
		if( this.getGenericName() != null && !this.getGenericName().equalsIgnoreCase("") ){
			drugValues.put(StorageProvider.DrugColumns.DRUG_GENERIC_NAME, this.getGenericName());
		}
		// Store the manufacturer if not empty
		if ( this.getManufacturer() != null && !this.getManufacturer().equalsIgnoreCase("") ) {
			drugValues.put(StorageProvider.DrugColumns.DRUG_MANUFACTURER, this.getManufacturer());
		}
		
		// TODO Store drug interactions.
			
		/**
		 * STORE THE DRUG.
		 */
		// Store the drug, because why the hell not...
		Uri drugUri = context.getApplicationContext().getContentResolver().insert(StorageProvider.DrugColumns.CONTENT_URI, drugValues);
		int drugId = Integer.parseInt(drugUri.getPathSegments().get(1));
		
		if ( this.getDrugDetails() != null) {
			// Get an iterator on the drug details
			ListIterator<DrugDetails> detailList = this.getDrugDetails().listIterator();
			
			/**
			 * For each of the drug details, set the drug id and store the details.
			 */
			while (detailList.hasNext()) {
				// Get the next DrugDetails object
				DrugDetails currDetails = detailList.next();
				// Store the Drug object reference.
				currDetails.setDrugId(drugId);
				// Convert the DrugDetails to ContentValues and store them
				context.getApplicationContext().getContentResolver().insert(StorageProvider.DrugDetailColumns.CONTENT_URI, currDetails.toContentValues());
			}
		}
		
		return drugId;
	}
	
	public static Drug fromCursor(Cursor cursor, Context context) throws CursorIndexOutOfBoundsException {

		Log.d(LOG_TAG, "Entering from cursor...");
		
		try {
			// Create the drug object
			Drug newDrug = null;
			
			/**
			 * GET THE REQUIRED FIELDS.
			 */
			int _id = cursor.getInt(cursor.getColumnIndex(StorageProvider.DrugColumns._ID));
			String brandName = cursor.getString(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_BRAND_NAME));

			Log.d(LOG_TAG, "Got required fields: " + _id + " " + brandName);
			
			// Inst. the drug
			newDrug = new Drug(_id, brandName);
			
			/**
			 * GET THE OPTIONAL FIELDS.
			 */
			// Get the generic name.
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_GENERIC_NAME))) {
				newDrug.setGenericName(cursor.getString(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_GENERIC_NAME)));
			}
		
			// Get the manufacturer
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_MANUFACTURER))) {
				newDrug.setManufacturer(cursor.getString(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_MANUFACTURER)));
			}
			
			// Get any interactions
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_INTERACTIONS))) {
				// TODO Get all interactions.
			}
			
			Log.d(LOG_TAG, "About to look for any drug details...");
			// Get all of the DrugDetails associated with this drug
			String whereClause = StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG + "=" + "'" + newDrug.get_id() + "'";
			Cursor detailsCursor = context.getApplicationContext().getContentResolver().query(StorageProvider.DrugDetailColumns.CONTENT_URI, null, whereClause, null, null);
			Log.d(LOG_TAG, "Completed the search for drug details...");
			
			if ( detailsCursor != null && detailsCursor.getCount() != 0 ) {
				Log.d(LOG_TAG, "There are drug details for this drug!");
				Log.d(LOG_TAG, "Number of entries in the details cursor: " + detailsCursor.getCount());
				
				// Move the cursor to the start
				detailsCursor.moveToFirst();
				
				do {
					// Add the drug details object to the drug class.
					newDrug.addDrugDetails(DrugDetails.fromCursor(detailsCursor));
				}  while (detailsCursor.moveToNext());
				
			}
			
			// Close and release the cursor.
			detailsCursor.close();
					
			Log.d(LOG_TAG, "Returning the new drug.");
			
			// Return the new drug!
			return newDrug;
			
		} catch (CursorIndexOutOfBoundsException cioobe) {
			cioobe.printStackTrace();
			throw cioobe;
		}
	}

}
