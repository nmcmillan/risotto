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
 	
 	/**
 	 * Generates an empty Drug object where:
 	 *  - unitVolume = 0
 	 *  - strength = 0
 	 *  - genericName = ""
 	 */
 	public Drug() {
 		this(0,0,"");
 	}
	
 	/**
 	 * 
 	 * Creates a Drug object with the given input parameters set.
 	 * 
 	 * @param unitVolume 
 	 * @param strength
 	 * @param genericName
 	 */
	public Drug(int unitVolume, int strength, String genericName) {
		this.setUnitVolume(unitVolume);
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

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
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

	public void setUnitVolume(int unitVolume) {
		this.unitVolume = unitVolume;
	}

	public int getUnitVolume() {
		return unitVolume;
	}

}
