package com.risotto.model;

import com.risotto.storage.StorageProvider;

import android.content.ContentValues;

public class Patient {
	
	// Required Fields
	private int gender;
	private String firstName;
	private String lastName;
	
	//Optional Fields
	private byte relations;
	
	public enum RELATION {
		MOTHER,
		FATHER,
		DAUGHTER,
		SON,
		GRANDFATHER,
		GRANDMOTHER,
		GRANDSON,
		GRANDDAUGHTER,
		AUNT,
		UNCLE,
		COUSIN,
		OTHER;
	}
	
	// Gender constants
	public static final int GENDER_MALE = 0;
	public static final int GENDER_FEMALE = 0;
	public static final int GENDER_OTHER = 0;
	
	// Unique id used for storage references
	private int _id;
	private static final int INVALID_ID = -1;
	
	@Deprecated
	public enum GENDER {
		MALE,
		FEMALE,
		OTHER;
	}
	
	public Patient(String firstName, String lastName, int gender) {
		this(INVALID_ID, firstName, lastName, gender);
	}

	private Patient(int _id, String firstName, String lastName, int gender) {
		this._id = _id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public byte getRelations() {
		return relations;
	}

	public void setRelations(byte relations) {
		this.relations = relations;
	}
	
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}
	
	public ContentValues toContentValues() {
		// Create a new 'ContentValues' to store our values
		ContentValues patientValues = new ContentValues();
		// Store the first name
		patientValues.put(StorageProvider.PatientColumns.PATIENT_FIRST_NAME, this.getFirstName());
		// Store the last name
		patientValues.put(StorageProvider.PatientColumns.PATIENT_LAST_NAME, this.getLastName());
		// Store the value gender
		patientValues.put(StorageProvider.PatientColumns.PATIENT_GENDER, this.getGender());
		// Return the 'ContentValue' object
		return patientValues;
	}

	public static Patient fromContentValues(ContentValues cv) {
		Patient newPatient = null;
		String firstName;
		String lastName;
		int gender;
		
		// Get the required fields
		firstName = cv.getAsString(StorageProvider.PatientColumns.PATIENT_FIRST_NAME);
		lastName = cv.getAsString(StorageProvider.PatientColumns.PATIENT_LAST_NAME);
		gender = cv.getAsInteger(StorageProvider.PatientColumns.PATIENT_GENDER);
		
		// Create the 'Patient' object with the required fields
		newPatient = new Patient(firstName, lastName, gender);
		
		// Get any optional fields

		// Return the 'Patient'
		return newPatient;
	}
	
}
