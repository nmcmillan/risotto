package com.risotto.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;

import com.risotto.storage.StorageProvider;

public class DrugDetails {

	// Required Fields
	private int type;
	private int strength;
	private String strengthLabel;
	private int drugId = INVALID_ID;

	// Optional Fields
	private String nickName;
	private FORM form;
	private int color = -1;
	private SHAPE shape;
	private SIZE size;

	// Unique id used for storage references
	private int _id;
	private static final int INVALID_ID = -1;

	/*
	 * TYPES
	 */
	// Over-the-counter drug.
	public static final int TYPE_OTC = 0;
	// Prescription drug.
	public static final int TYPE_PRE = 1;

	// Possible solid shapes.
	public enum SHAPE {
		ROUND, OBLONG, OVAL, SQUARE, RECTANGLE, DIAMOND, THREE_SIDED, FIVE_SIDED, SIX_SIDED, SEVEN_SIDED, EIGHT_SIDED, OTHER, NONE
	}

	// The many forms which the drug can be.
	public enum FORM {
		CAPSULES, TABLETS, POWDERS, DROPS, LIQUIDS, SPRAY, SKIN, SUPPOSITORIES, NONE, OTHER
	}

	// Possible sizes of solid drugs.
	public enum SIZE {
		SMALL, MEDIUM, LARGE, NONE, OTHER
	}

	public DrugDetails(byte type, int strength, String strengthLabel) {
		this(INVALID_ID, type, strength, strengthLabel);
	}

	private DrugDetails(int _id, int type, int strength, String strengthLabel) {
		this._id = _id;
		this.type = type;
		this.strength = strength;
		this.strengthLabel = strengthLabel;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
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

	private void set_id(int _id) {
		this._id = _id;
	}
	
	public int getDrugId() {
		return drugId;
	}

	public void setDrugId(int drugId) {
		this.drugId = drugId;
	}

	private static SIZE toSizeOrdinal(int size) {
		// TODO Implement
		return null;
	}

	private static FORM toFormOrdinal(int form) {
		// TODO return null
		return null;
	}

	private static SHAPE toShapeOrdinal(int shape) {
		// TODO Implement
		return null;
	}

	public static DrugDetails fromCursor(Cursor cursor) throws CursorIndexOutOfBoundsException {
		try {
			// Create the new drugDetails object. 
			DrugDetails drugDetails = null;
			
			/**
			 * GET THE REQUIRED FIELDS.
			 */
			// ID
			int _id = cursor.getInt(cursor.getColumnIndex(StorageProvider.DrugDetailColumns._ID));
			// Type
			int type = cursor.getInt(cursor.getColumnIndex(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_TYPE));
			// Strength
			int strength = cursor.getInt(cursor.getColumnIndex(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_TYPE));
			// Strength Label
			String strengthLabel = cursor.getString(cursor.getColumnIndex(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_STRENGTH_LABEL));
				
			/**
			 * CREATE THE OBJECT.
			 */
			drugDetails = new DrugDetails(_id, type, strength, strengthLabel);
			
			/**
			 * GET THE OPTIONAL FIELDS.
			 */
			// Nick Name
			if( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_NICK_NAME)) ) {
				drugDetails.setNickName( cursor.getString( cursor.getColumnIndex(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_NICK_NAME)) );
			}
			
			// Form
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_FORM))) {
				int formInt = cursor.getInt(cursor.getColumnIndex(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_FORM));
				// TODO Turn this int back into the enum value.
			}
			
			// Color
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_COLOR))) {
				drugDetails.setColor(cursor.getInt(cursor.getColumnIndex(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_COLOR)));
			}
			
			// Shape
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_SHAPE))) {
				int shapeId = cursor.getInt(cursor.getColumnIndex(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_SHAPE));
				// TODO Turn this int back into the enum value.
			}
			
			// Size
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_SIZE))) {
				int sizeId = cursor.getInt(cursor.getColumnIndex(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_SIZE));
				// TODO Turn this int back into the enum value.
			}
			
			// Return the new DrugDetails object.
			return drugDetails;
			
		} catch (CursorIndexOutOfBoundsException cioobe) {
			throw cioobe;
		}
	}

	public ContentValues toContentValues() {
		// Create a new 'ContentValues' to store our values
		ContentValues drugDetailsValues = new ContentValues();

		/**
		 * STORE ALL REQUIRED FIELDS.
		 */
		// Type
		drugDetailsValues.put(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_TYPE, this.getType());
		// Strength
		drugDetailsValues.put(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_STRENGTH, this.getStrength());
		// Strength Label
		drugDetailsValues.put(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_STRENGTH_LABEL, this.getStrengthLabel());
		// Drug Reference
		drugDetailsValues.put(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG, this.getDrugId());
		
		/**
		 * STORE ANY OPTIONAL FIELDS.
		 */
		// Nick Name
		if ( !this.getNickName().equalsIgnoreCase("")) {
			drugDetailsValues.put(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_NICK_NAME, this.getNickName());
		}
		// Form
		if ( this.getForm() != null) {
			drugDetailsValues.put(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_FORM, this.getForm().ordinal());
		}
		// Color
		if ( this.getColor() != -1 ) {
			drugDetailsValues.put(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_COLOR, this.getColor());
		}
		// Shape
		if ( this.getShape() != null) {
			drugDetailsValues.put(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_SHAPE, this.getShape().ordinal());
		}
		// Size
		if ( this.getSize() != null ) {
			drugDetailsValues.put(StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_SIZE, this.getSize().ordinal());
		}
		
		// Return the 'ContentValue' object
		return drugDetailsValues;		
	}
}
