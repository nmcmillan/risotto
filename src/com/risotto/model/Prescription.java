package com.risotto.model;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.risotto.storage.StorageProvider;

public class Prescription {
	
	// Required fields
	private Dosage dose;
	private Patient patient;
	private Drug drug;
	private int doseType;
	private int doseSize;
	private int totalUnits;
	// Unique ID for storage references
	private int _id;
	private static final int INVALID_ID = -1;
	
	// Optional Fields
	private Date filled;
	private String drName;
	private int prescripID;
	private int cost;
	private int numDaysSupplied;
	private int numRefills;
	private Date expiration;

	// Possible dosage types.
	public static final int DOSE_TYPE_EVERY_DAY_OF_WEEK = 0;
	public static final int DOSE_TYPE_EVERY_HOUR = 1;
	public static final int DOSE_TYPE_EVERY_HOUR_DAY_OF_WEEK = 2;
	public static final int DOSE_TYPE_EVERY_DAY = 3;
	
	@Deprecated
	public enum DOSE_TYPE {
		EVERY_DAY_OF_WEEK,
		EVERY_HOUR,
		EVERY_HOUR_DAY_OF_WEEK,
		EVERY_DAY
	}
	
	public Prescription(Patient patient, Drug drug, int doseType, int doseSize, int totalUnits) {
		this(INVALID_ID, patient, drug, doseType, doseSize, totalUnits);
	}
	
	private Prescription(int _id, Patient patient, Drug drug, int doseType, int doseSize, int totalUnits) {
		this._id = _id;
		this.patient = patient;
		this.drug = drug;
		this.doseType = doseType;
		this.doseSize = doseSize;
		this.totalUnits = totalUnits;
	}

	public Dosage getDose() {
		return dose;
	}

	public void setDose(Dosage dose) {
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

	public int getDoseType() {
		return doseType;
	}

	public void setDoseType(int doseType) {
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
	
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}
	
	public ContentValues toContentValues(Context context) {
		ContentValues cv = new ContentValues();
		
		// Is this a new prescription? (check _id)
		boolean newPrescription = ( this._id == INVALID_ID ? true : false );
		
		// Is this a new drug?
		boolean newDrug = ( drug.get_id() == INVALID_ID ? true : false );
		
		// Is this a new patient?
		boolean newPatient = ( patient.get_id() == INVALID_ID ? true : false );
		
		// If the drug is new, we need to store the drug
		if (newDrug) {
			// Store the drug
			Uri drugUri = context.getContentResolver().insert(StorageProvider.DrugColumns.CONTENT_URI, this.getDrug().toContentValues());
			// Store the new _id back to the drug object in memory.
			this.getDrug().set_id(Integer.parseInt(drugUri.getPathSegments().get(1)));	
		}
		
		// If the patient is new, we need to store the patient
		if (newPatient) {
			// Store the patient.
			Uri patientUri = context.getContentResolver().insert(StorageProvider.PatientColumns.CONTENT_URI, this.getPatient().toContentValues());
			// Store the new _id back the patient object in memory.
			this.getPatient().set_id(Integer.parseInt(patientUri.getPathSegments().get(1)));
		}
		
		// If the prescription is not new, we need to provide the _id
		if (!newPrescription) {
			// Give the previous _id to the caller
			cv.put(StorageProvider.PrescriptionColumns._ID, "" + this.get_id());
		}
		
		// Store all the required fields
		cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_PATIENT, this.getPatient().get_id());
		cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_DRUG, this.getDrug().get_id());
		cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_DOSE_TYPE, this.getDoseType());
		cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_DOSE_SIZE, this.getDoseSize());
		cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_TOTAL_UNITS, this.getTotalUnits());
		
		// Store any optional fields that may be present
		
		// Return the 'ContentValues' to the caller
		return cv;
		
	}
	
	public static Prescription fromContentValues(ContentValues cv, Context context) {
		Prescription newPrescription = null;
		Patient newPatient;
		Drug newDrug;
		
		return newPrescription;
	}

}
