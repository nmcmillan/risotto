package com.risotto.model;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import android.content.ContentValues;
import android.graphics.Color;

import com.risotto.storage.StorageProvider;

public class Drug {
	
	// Required Fields
	
	/**
	 * As of (3/22) we are thinking this is unnecessary
	 */
	//private int unitVolume;
	
	/**
	 * Medicare data: amount of active ingredient in the pill, not the size of the pill; we are saying that a drug (e.g. Advil)
	 * can have multiple strengths, but we don't want to have duplicate drug entries in the database; all strenths for a given drug
	 * will be captured in this array
	 */
	private int[] strength;	
	
	/**
	 * 
	 */
	private String genericName;
	
	// Unique id used for storage references
	private int _id;
	private static final int INVALID_ID = -1;
	
	private FORM form;
	
	// Optional Fields
	private String brandName;
	private String nickName;
	private String manufacturer;
	private Vector<Drug> interactions;
	private int drugId;
	private Color color;
	private int grossCost;
	private SHAPE shape;
	
	
	private SIZE size;
	
	// Over-the-counter drug.
	public static final byte TYPE_OTC = 0;
	// Prescription drug.
	public static final byte TYPE_PRE = 1;	

	// Possible solid shapes.
	public enum SHAPE {
		ROUND,
		OBLONG,
		OVAL,
		SQUARE,
		RECTANGLE,
		DIAMOND,
		THREE_SIDED,
		FIVE_SIDED,
		SIX_SIDED,
		SEVEN_SIDED,
		EIGHT_SIDED,
		OTHER,
		NONE
	}
	
	// The many forms which the drug can be.
 	public enum FORM {
		CAPSULES,
		TABLETS,
		POWDERS,
		DROPS,
		LIQUIDS,
		SPRAY,
		SKIN,
		SUPPOSITORIES,
		NONE,
		OTHER;
		
	}

 	// Possible sizes of solid drugs.
 	public enum SIZE {
 		SMALL,
 		MEDIUM,
 		LARGE,
 		NONE,
 		OTHER
 	}
 	
 	/**
 	 * Generates an empty Drug object where:
 	 *  - unitVolume = 0
 	 *  - strength[] = null
 	 *  - genericName = ""
 	 */
 	public Drug() {
 		this(0,null,"");
 	}
	
 	/**
 	 * 
 	 * Creates a Drug object with the given input parameters set.
 	 * 
 	 * @param unitVolume 
 	 * @param strength
 	 * @param genericName
 	 */
	public Drug(int unitVolume, int[] strength, String genericName) {
		this(INVALID_ID, unitVolume, strength, genericName);
	}
	
	private Drug(int _id, int unitVolume, int[] strength, String genericName) {
		this._id = _id;
		//this.setUnitVolume(unitVolume);
		this.strength = strength;
		this.genericName = genericName;
	}

	public String getMedicalName() {
		return genericName;
	}

	public void setMedicalName(String medicalName) {
		this.genericName = medicalName;
	}

	public String getCommonName() {
		return brandName;
	}

	public void setCommonName(String commonName) {
		this.brandName = commonName;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	private Vector<Drug> getInteractions() {
		return interactions;
	}

	private void setInteractions(Vector<Drug> interactions) {
		this.interactions = interactions;
	}

	public int[] getStrength() {
		return strength;
	}

	public void setStrength(int[] strength) {
		this.strength = strength;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getDrugId() {
		return drugId;
	}

	public void setDrugId(int drugId) {
		this.drugId = drugId;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getGrossCost() {
		return grossCost;
	}

	public void setGrossCost(int grossCost) {
		this.grossCost = grossCost;
	}

	public SHAPE getShape() {
		return shape;
	}

	public void setShape(SHAPE shape) {
		this.shape = shape;
	}

	public FORM getForm() {
		return form;
	}

	public void setForm(FORM form) {
		this.form = form;
	}

	public SIZE getSize() {
		return size;
	}

	public void setSize(SIZE size) {
		this.size = size;
	}

	/*public void setUnitVolume(int unitVolume) {
		this.unitVolume = unitVolume;
	}

	public int getUnitVolume() {
		return unitVolume;
	}*/
	
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}
	
	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(StorageProvider.DrugColumns.DRUG_NAME, this.genericName);
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(this.strength.length * 4);        
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(this.strength);
        byte[] array = byteBuffer.array();
        
        cv.put(StorageProvider.DrugColumns.DRUG_STRENGTH, array);

        return cv;
	}
	
	public static Drug fromContentValues(ContentValues cv) {
		// Declare a return object
		Drug returnDrug = null;
		
		// Declare required fields.
		int unitVolume = 0;
		int[] strength = null;
		String genericName = "";
		
		// Set required fields
		if (cv.containsKey(StorageProvider.DrugColumns.DRUG_NAME)) {
			genericName = cv.getAsString(StorageProvider.DrugColumns.DRUG_NAME);
		} else if (cv.containsKey(StorageProvider.DrugColumns.DRUG_STRENGTH)) {
			byte[] byteStrength = cv.getAsByteArray(StorageProvider.DrugColumns.DRUG_STRENGTH);
			/**
			 * Nick - Put the byte array back into an int array.
			 */
			
		}
		// Create the object with required fields
		returnDrug = new Drug(unitVolume, strength, genericName);
		
		// Check/set any non-required fields.
		
		
		return returnDrug;
	}

}
