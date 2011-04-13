package com.risotto.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.Enumeration;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.util.Log;

import com.risotto.storage.StorageProvider;

public class Drug {
	
	public static final String LOG_TAG = "RISOTTO_DRUG";
	
	// Required Fields
	private String brandName;
	private Vector<DrugDetails> drugDetails;
	
	// Optional Fields
	private String genericName;
	private String manufacturer;
	
	// Unique id used for storage references
	private int _id;
	private static final int INVALID_ID = -1;
	
	public Drug(String brandName, DrugDetails drugDetails) {
		this(INVALID_ID, brandName, drugDetails);
	}
	
	public Drug(String brandName, int type, int strength, String strengthLabel) {
		this(brandName, new DrugDetails(type, strength, strengthLabel));
	}
	
	private Drug(int _id, String brandName, DrugDetails drugDetails) {
		this._id = _id;
		this.brandName = brandName;
		// Call for an add to the vector
		this.addDrugDetails(drugDetails);
	}
	
	private Drug(int _id, String brandName, Vector<DrugDetails> drugDetails) {
		this._id = _id;
		this.brandName = brandName;
		this.drugDetails = drugDetails;
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
	
	/*
	 * Some helper functions for the view.
	 */
	public String getPrintableStrengths() {
		String returnString = "";
		
		Enumeration<DrugDetails> drugDetailsEnum = this.getDrugDetails().elements();
		
		do {
			DrugDetails nextDetails = drugDetailsEnum.nextElement();
			returnString+= nextDetails.getStrength() + "" + nextDetails.getStrengthLabel();
			
			if (drugDetailsEnum.hasMoreElements()) {
				returnString+= ", ";
			}
		} while(drugDetailsEnum.hasMoreElements());
		
		return returnString;
	}
	
	public int storeDrug(Context context) {
		// Store the drug...
		Uri drugUri = context.getApplicationContext().getContentResolver().insert(StorageProvider.DrugColumns.CONTENT_URI, this.toContentValues());
		// Parse off the row number from the resulting URI
		int drugId = Integer.parseInt(drugUri.getPathSegments().get(1));
		// Return the row number
		// TODO Should we return the URI instead?
		return drugId;	
	}
	
	public ContentValues toContentValues() {
		// Create a new 'ContentValues' to store our values
		ContentValues drugValues = new ContentValues();
		
		/**
		 * STORE ALL REQUIRED FIELDS.
		 */
		// Store the brand name
		drugValues.put(StorageProvider.DrugColumns.DRUG_BRAND_NAME, this.getBrandName());
		
		// Store the details vector
		Log.d(LOG_TAG, "Attempting to store the details vector...");
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ObjectOutputStream o = new ObjectOutputStream(b);
			o.writeObject(this.getDrugDetails());
			o.flush();
			// Place the vector in the blob
			drugValues.put(StorageProvider.DrugColumns.DRUG_DETAILS, b.toByteArray());
			// Close the stream
			o.close();
			
		} catch (IOException e) {
			Log.d(LOG_TAG, "Exception while trying to serialize the details vector...");
			e.printStackTrace();
		}
		
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
			
		return drugValues;
	}
	
	public static Drug fromCursor(Cursor cursor, Context context) throws CursorIndexOutOfBoundsException {

		Log.d(LOG_TAG, "Entering from cursor...");
		
		try {
			// Create the drug object
			Drug newDrug = null;
			
			/**
			 * GET THE REQUIRED FIELDS.
			 */
			// Get the ID
			int _id = cursor.getInt(cursor.getColumnIndex(StorageProvider.DrugColumns._ID));
			// Get the Brand Name
			String brandName = cursor.getString(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_BRAND_NAME));

			// Get the DrugDetails vector
			Log.d(LOG_TAG, "Attempting to get the details vector...");
			Vector<DrugDetails> drugDetails = null;
			
			byte[] detailsArray = cursor.getBlob(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_DETAILS));
			
			try {
				ByteArrayInputStream bb = new ByteArrayInputStream(detailsArray);
				ObjectInputStream oo = new ObjectInputStream(bb);
				drugDetails = (Vector<DrugDetails>)oo.readObject();
				oo.close();
			} catch (StreamCorruptedException e) {
				Log.d(LOG_TAG, "SteamCorruptedExcpetion while getting the details vector...");
				e.printStackTrace();
			} catch (OptionalDataException e) {
				Log.d(LOG_TAG, "OptionalDataException while getting the details vector...");
				e.printStackTrace();
			} catch (IOException e) {
				Log.d(LOG_TAG, "IOException while getting the details vector...");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				Log.d(LOG_TAG, "ClassNotFoundException while getting the details vector...");
				e.printStackTrace();
			}
			
			// Instantiate the drug object
			newDrug = new Drug(_id, brandName, drugDetails);
			
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
			
			// Return the new drug!
			return newDrug;
			
		} catch (CursorIndexOutOfBoundsException cioobe) {
			cioobe.printStackTrace();
			throw cioobe;
		}
	}

}
