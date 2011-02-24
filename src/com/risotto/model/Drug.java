package com.risotto.model;

import java.util.Vector;

import android.graphics.Color;

public class Drug {
	
	// Required Fields
	private int unitVolume;
	private int strength;	
	private String genericName;
	
	// Optional Fields
	private String brandName;
	private String nickName;
	private String manufacturer;
	private Vector<Drug> interactions;
	private int drugId;
	private Color color;
	private int grossCost;
	private SHAPE shape;
	private FORM form;
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
	
	
	public Drug() {
		this("","","");
	}
	
	public Drug(String medName) {
		this(medName,"","");
	}
	
	public Drug(String medName, String comName) {
		this(medName,comName,"");
	}
	
	public Drug(String medName, String comName, String manu) {
		this.genericName = medName;
		this.brandName = comName;
		this.manufacturer = manu;
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
	
	

}
