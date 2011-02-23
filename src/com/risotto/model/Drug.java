package com.risotto.model;

import java.util.Vector;

public class Drug {
	
	private String medicalName;
	private String commonName;
	private String manufacturer;
	public static final byte TYPE_OTC = 0;
	public static final byte TYPE_PRE = 1;
	private Vector<Drug> interactions;
	private int drugId;
	
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
		this.medicalName = medName;
		this.commonName = comName;
		this.manufacturer = manu;
	}

	public String getMedicalName() {
		return medicalName;
	}

	public void setMedicalName(String medicalName) {
		this.medicalName = medicalName;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
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
