package com.risotto.model;

import java.io.Serializable;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.util.Log;

import com.risotto.storage.StorageProvider;

public class Drug implements Serializable{
	
	// Required Fields
	private String brandName;
	
	// Extra Constructor Fields
	private TYPE type = TYPE.DEFAULT;
	private int strength = -1;
	private String strengthLabel = "NULL";
	
	// Optional Fields
	private String compoundName = "NULL";
	private String manufacturer = "NULL";
	private Vector<Integer> interactions = null;
	private String nickName = "NULL";
	private FORM form = FORM.DEFAULT;
	private int color = -1;
	private SHAPE shape = SHAPE.DEFAULT;
	private SIZE size = SIZE.DEFAULT;
	
	// Unique id used for storage references
	private int _id;
	private static final int INVALID_ID = -1;
	
	// DEBUG: LOG_TAG
	private static final String LOG_TAG = "RISOTTO_DRUG";
	
	// Supported labels for notification
	public enum LABEL {
		PILLS,ML,MG,OZ,TABLESPOONS,OTHER
	}
	
	// Possible drug types.
	public enum TYPE {
		OVER_THE_COUNTER, PRESCRIPTION, DEFAULT
	}

	// Possible solid shapes.
	public enum SHAPE {
		ROUND, OBLONG, OVAL, SQUARE, RECTANGLE, DIAMOND, THREE_SIDED, FIVE_SIDED, SIX_SIDED, SEVEN_SIDED, EIGHT_SIDED, NONE, OTHER, DEFAULT
	}

	// The many forms which the drug can be.
	public enum FORM {
		CAPSULES, TABLETS, POWDERS, DROPS, LIQUIDS, SPRAY, SKIN, SUPPOSITORIES, NONE, OTHER, DEFAULT;
	
		public String toString() {
			String form = super.toString();
			form = form.toLowerCase();
			return (form.substring(0, 1).toUpperCase() + form.substring(1));
		}
	}

	//For UI
	/*public static final String Capsules = "Capsules";
	public static final String Tablets = "Tablets";
	public static final String Powders = "Capsules";
	public static final String Drops = "Drops";
	public static final String Liquids = "Liquids";
	public static final String Spray = "Spray";
	public static final String Skin = "Skin";
	public static final String Suppositories = "Suppositories";
	public static final String None = "None";
	public static final String Other = "Other";*/

	// Possible sizes of solid drugs.
	public enum SIZE {
		SMALL, MEDIUM, LARGE, NONE, OTHER, DEFAULT
	}
	
	public Drug(String brandName) {
		this(INVALID_ID, brandName);
	}
	
	private Drug(int _id, String brandName) {
		this._id = _id;
		this.brandName = brandName;
	}
	
	public Drug(String brandName, TYPE type, int strength, String strengthLabel) {
		this(INVALID_ID, brandName, type, strength, strengthLabel);
	}
	
	private Drug(int _id, String brandName, TYPE type, int strength, String strengthLabel) {
		this._id = _id;
		this.brandName = brandName;
		this.type = type;
		this.strength = strength;
		this.strengthLabel = strengthLabel;
	}

	public String getCompoundName() {
		return compoundName;
	}

	public void setCompoundName(String compoundName) {
		this.compoundName = compoundName;
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
	
	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public String getStrengthLabel() {
		return strengthLabel;
	}

	public void setStrengthLabel(String strengthLabel) {
		this.strengthLabel = strengthLabel;
	}

	public Vector<Integer> getInteractions() {
		return interactions;
	}

	public void setInteractions(Vector<Integer> interactions) {
		this.interactions = interactions;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public FORM getForm() {
		return form;
	}

	public void setForm(FORM form) {
		this.form = form;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public SHAPE getShape() {
		return shape;
	}

	public void setShape(SHAPE shape) {
		this.shape = shape;
	}

	public SIZE getSize() {
		return size;
	}

	public void setSize(SIZE size) {
		this.size = size;
	}
	
	public int get_id() {
		return _id;
	}

	protected void set_id(int _id) {
		this._id = _id;
	}
	
	/**
	 * Returns a string with a display friendly version of the drug strength.  Will return an empty string if both fields are 
	 * empty or if the strength field is empty
	 * 
	 * @return string formatted for pritning on screen
	 */
	public String getPrintableStrength() {
		int stren = this.getStrength();
		String label = this.getStrengthLabel();
		if(label.equalsIgnoreCase("null"))
			label = "";
		if(stren == -1)
			return "";
		else 
			return "" + stren + "" + label;
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
		
		/**
		 * STORE ANY OPTIONAL FIELDS.
		 */
		// Store the type
		if ( this.getType() != null && this.getType() != TYPE.DEFAULT ) {
			drugValues.put(StorageProvider.DrugColumns.DRUG_TYPE, this.getType().toString());
		}
		
		// Store the strength
		if ( this.getStrength() != -1 ) {
			drugValues.put(StorageProvider.DrugColumns.DRUG_STRENGTH, this.getStrength());
		}
		
		// Store the strength label
		if ( this.getStrengthLabel() != null && !this.getStrengthLabel().equals("NULL")) {
			drugValues.put(StorageProvider.DrugColumns.DRUG_STRENGTH_LABEL, this.getStrengthLabel());
		}
		
		// Store the compound name if not empty
		if( this.getCompoundName() != null && !this.getCompoundName().equals("NULL") ){
			drugValues.put(StorageProvider.DrugColumns.DRUG_COMPOUND_NAME, this.getCompoundName());
		}
		
		// Store the manufacturer if not empty
		if ( this.getManufacturer() != null && !this.getManufacturer().equals("NULL") ) {
			drugValues.put(StorageProvider.DrugColumns.DRUG_MANUFACTURER, this.getManufacturer());
		}
		
		// Store the nick name if not empty.
		if ( this.getNickName() != null && !this.getNickName().equals("NULL") ) {
			drugValues.put(StorageProvider.DrugColumns.DRUG_NICK_NAME, this.getNickName());
		}
		
		// Store the form
		if ( this.getForm() != null && !this.getForm().equals(FORM.DEFAULT)) {
			drugValues.put(StorageProvider.DrugColumns.DRUG_FORM, this.getForm().toString());
		}
		
		// Store the color
		if ( this.getColor() != -1 ) {
			drugValues.put(StorageProvider.DrugColumns.DRUG_COLOR, this.getColor());
		}
		
		// Store the shape
		if ( this.getShape() != null && !this.getShape().equals(SHAPE.DEFAULT)) {
			drugValues.put(StorageProvider.DrugColumns.DRUG_SHAPE, this.getShape().toString());
		}
		
		// Store the size
		if ( this.getSize() != null && !this.getSize().equals(SIZE.DEFAULT)) {
			drugValues.put(StorageProvider.DrugColumns.DRUG_SIZE, this.getSize().toString());
		}
		
		// TODO Store drug interactions.
			
		return drugValues;
	}
	
	public static Drug fromCursor(Cursor cursor) throws CursorIndexOutOfBoundsException {

		Log.d(LOG_TAG, "Entering from cursor...");
		
		try {
			// Create the drug object
			Drug newDrug = null;
			int _id = -1;
			/**
			 * GET THE REQUIRED FIELDS.
			 */
			// Get the ID
			if( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugColumns._ID))){
				_id = cursor.getInt(cursor.getColumnIndex(StorageProvider.DrugColumns._ID));
			}
			
			// Get the Brand Name
			String brandName = "NULL";
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_BRAND_NAME))) {
				brandName = cursor.getString(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_BRAND_NAME));
			}
			
			// Instantiate the drug object
			newDrug = new Drug(_id, brandName);
			
			/**
			 * GET THE OPTIONAL FIELDS.
			 */
			// Get the type
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_TYPE))) {
				TYPE type = TYPE.valueOf(cursor.getString(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_TYPE)));
				newDrug.setType(type);
			}
			// Get the strength
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_STRENGTH))) {
				int strength = cursor.getInt(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_STRENGTH));
				newDrug.setStrength(strength);
			}
			// Get the strength label
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_STRENGTH_LABEL))) {
				String strengthLabel = cursor.getString(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_STRENGTH_LABEL));
				newDrug.setStrengthLabel(strengthLabel);
			}
			
			// Get the compound name
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_COMPOUND_NAME))) {
				newDrug.setCompoundName(cursor.getString(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_COMPOUND_NAME)));
			}
		
			// Get the manufacturer
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_MANUFACTURER))) {
				newDrug.setManufacturer(cursor.getString(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_MANUFACTURER)));
			}
			
			// Get the nick name
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_NICK_NAME))) {
				newDrug.setNickName(cursor.getString(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_NICK_NAME)));
			}
			
			// Get the form
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_FORM))) {
				newDrug.setForm(FORM.valueOf(cursor.getString(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_FORM)).toUpperCase()));
			}
			
			// Get the color
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_COLOR))) {
				newDrug.setColor(cursor.getInt(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_COLOR)));
			}
			
			// Get the shape
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_SHAPE))) {
				newDrug.setShape(SHAPE.valueOf(cursor.getString(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_SHAPE))));
			}
			
			// Get the size
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_SIZE))) {
				newDrug.setSize(SIZE.valueOf(cursor.getString(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_SIZE))));
			}
			
			// Get any interactions
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_INTERACTIONS))) {
				// TODO Get all interactions.
			}
			
			Log.d(LOG_TAG,"finished parsing cursor data into drug object.");
			
			// Return the new drug!
			return newDrug;
			
		} catch (CursorIndexOutOfBoundsException cioobe) {
			cioobe.printStackTrace();
			throw cioobe;
		}
	}

}
