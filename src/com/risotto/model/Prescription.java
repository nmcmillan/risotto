package com.risotto.model;

import java.util.Date;

public class Prescription {
	
	// Required fields
	private Doseage dose;
	private Patient patient;
	private Drug drug;
	private DOSE_TYPE doseType;
	private int doseSize;
	private int totalUnits;
	
	// Optional Fields
	private Date filled;
	private String drName;
	private int prescripID;
	private int cost;
	private int numDaysSupplied;
	private int numRefills;
	private Date expiration;

	// Possible doseage types.
	public enum DOSE_TYPE {
		EVERY_DAY_OF_WEEK,
		EVERY_HOUR,
		EVERY_HOUR_DAY_OF_WEEK,
		EVERY_DAY
	}
	
	public Prescription(Patient patient, Drug drug, DOSE_TYPE doseType,
			int doseSize, int totalUnits) {
		this.patient = patient;
		this.drug = drug;
		this.doseType = doseType;
		this.doseSize = doseSize;
		this.totalUnits = totalUnits;
	}

	public Doseage getDose() {
		return dose;
	}

	public void setDose(Doseage dose) {
		this.dose = dose;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Drug getDrug() {
		return drug;
	}

	public void setDrug(Drug drug) {
		this.drug = drug;
	}

	public DOSE_TYPE getDoseType() {
		return doseType;
	}

	public void setDoseType(DOSE_TYPE doseType) {
		this.doseType = doseType;
	}

	public int getDoseSize() {
		return doseSize;
	}

	public void setDoseSize(int doseSize) {
		this.doseSize = doseSize;
	}

	public int getTotalUnits() {
		return totalUnits;
	}

	public void setTotalUnits(int totalUnits) {
		this.totalUnits = totalUnits;
	}

	public Date getFilled() {
		return filled;
	}

	public void setFilled(Date filled) {
		this.filled = filled;
	}

	public String getDrName() {
		return drName;
	}

	public void setDrName(String drName) {
		this.drName = drName;
	}

	public int getPrescripID() {
		return prescripID;
	}

	public void setPrescripID(int prescripID) {
		this.prescripID = prescripID;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getNumDaysSupplied() {
		return numDaysSupplied;
	}

	public void setNumDaysSupplied(int numDaysSupplied) {
		this.numDaysSupplied = numDaysSupplied;
	}

	public int getNumRefills() {
		return numRefills;
	}

	public void setNumRefills(int numRefills) {
		this.numRefills = numRefills;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}	

}
