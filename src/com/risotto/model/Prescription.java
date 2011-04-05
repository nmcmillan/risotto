package com.risotto.model;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

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
	private Vector<Integer> daysOfWeek;

	// Possible dosage types.
	// Ex: ONCE on any given day/days (M,W,F or T,Th)
	public static final int DOSE_TYPE_EVERY_DAY_OF_WEEK = 0;
	// Ex: Twice a day -or- every 12 hours
	public static final int DOSE_TYPE_EVERY_HOUR = 1;
	// Ex: Twice a day T,Th -or- Every 12hrs T,Th
	public static final int DOSE_TYPE_EVERY_HOUR_DAY_OF_WEEK = 2;
	// Ex: ONCE a day EVERY day
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
		this.daysOfWeek = new Vector<Integer>();
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
	
	public void addDay(int dayOfWeek) {
		if (!this.daysOfWeek.contains((Integer)dayOfWeek)) {
			this.daysOfWeek.add((Integer)dayOfWeek);
		}
	}
	
	public void removeDay(int dayOfWeek) {
		this.daysOfWeek.remove((Integer)dayOfWeek);
	}
	
	public Vector<Integer> getAllDays() {
		return this.daysOfWeek;
	}
	
	private static String dayToColumnName(int dayOfWeek) {
		switch(dayOfWeek) {
		case Calendar.SUNDAY:
			return StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SUNDAY;
		case Calendar.MONDAY:
			return StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_MONDAY;
		case Calendar.TUESDAY:
			return StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_TUESDAY;
		case Calendar.WEDNESDAY:
			return StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_WEDNESDAY;
		case Calendar.THURSDAY:
			return StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_THURSDAY;
		case Calendar.FRIDAY:
			return StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_FRIDAY;
		case Calendar.SATURDAY:
			return StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SATURDAY;
		default:
			return null;	
		}
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
		
		// Store the days of the week that this prescription is needed
		if(!this.daysOfWeek.isEmpty()) {
			// Sort the vector in ascending order.
			Collections.sort(this.daysOfWeek);
			// Trim the vector
			this.daysOfWeek.trimToSize();
			// Get an enumeration
			Enumeration<Integer> daysEnum = this.daysOfWeek.elements();
			
			while(daysEnum.hasMoreElements()) {
				// Get the value from the enum (integer representation of the day of the week)
				Integer value = daysEnum.nextElement();
				/**
				 * For the time being, just store a boolean flag to the days.
				 * Eventually the scheduled "times" will be stored in the columns.
				 */
				cv.put(dayToColumnName(value), 1);
			}
		}
		
		
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
