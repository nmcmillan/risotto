package com.risotto.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.risotto.storage.StorageProvider;

public class Prescription {

	// Required fields
	private Patient patient;
	private Drug drug;
	private int doseType;
	
	// Extra Constructor Fields
	private int doseSize = -1;
	private int totalUnits = -1;

	// Optional Fields
	private Date filled;
	private String drName = "NULL";
	private int prescripID = -1;
	private int cost = -1;
	private int numDaysSupplied = -1;
	private int numRefills = -1;
	private Date expiration;
	private Vector<Integer> daysOfWeek;
	
	// Scheduled time vectors
	private Vector<String> sundayTimes;
	private Vector<String> mondayTimes;
	private Vector<String> tuesdayTimes;
	private Vector<String> wednesdayTimes;
	private Vector<String> thursdayTimes;
	private Vector<String> fridayTimes;
	private Vector<String> saturdayTimes;

	// Scheduling constants
	public static final int SCHEDULED = 1;
	public static final int NOT_SCHEDULED = 0;

	// Possible dosage types.
	// Ex: ONCE on any given day/days (M,W,F or T,Th)
	public static final int DOSE_TYPE_EVERY_DAY_OF_WEEK = 0;
	// Ex: Twice a day -or- every 12 hours
	public static final int DOSE_TYPE_EVERY_HOUR = 1;
	// Ex: Twice a day T,Th -or- Every 12hrs T,Th
	public static final int DOSE_TYPE_EVERY_HOUR_DAY_OF_WEEK = 2;
	// Ex: ONCE a day EVERY day
	public static final int DOSE_TYPE_EVERY_DAY = 3;

	// Unique ID for storage references
	private int _id;
	private static final int INVALID_ID = -1;

	// DEBUG: LOG_TAG
	private static final String LOG_TAG = "RISOTTO_PRESCRIPTION";

	public Prescription(Patient patient, Drug drug, int doseType) {
		this(INVALID_ID, patient, drug, doseType);
	}
	
	private Prescription(int _id, Patient patient, Drug drug, int doseType) {
		this._id = _id;
		this.patient = patient;
		this.drug = drug;
		this.daysOfWeek = new Vector<Integer>();
		this.initializeAllDayVectors();
	}
	
	public Prescription(Patient patient, Drug drug, int doseType, int doseSize,
			int totalUnits) {
		this(INVALID_ID, patient, drug, doseType, doseSize, totalUnits);
	}

	private Prescription(int _id, Patient patient, Drug drug, int doseType,
			int doseSize, int totalUnits) {
		this._id = _id;
		this.patient = patient;
		this.drug = drug;
		this.doseType = doseType;
		this.doseSize = doseSize;
		this.totalUnits = totalUnits;
		this.daysOfWeek = new Vector<Integer>();
		this.initializeAllDayVectors();
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

	protected void set_id(int _id) {
		this._id = _id;
	}

	public boolean isScheduled() {
		return this.areDaysScheduled();
	}
	
	public boolean addTimeEveryDay(String time) {
		boolean returnValue = false;
		
		this.sundayTimes.add(time);
		this.mondayTimes.add(time);
		this.tuesdayTimes.add(time);
		this.wednesdayTimes.add(time);
		this.thursdayTimes.add(time);
		this.fridayTimes.add(time);
		this.saturdayTimes.add(time);
		
		// Add all the days to the "scheduled" vector.
		this.addAllDays();
		
		return returnValue;
	}
	
	public boolean addTimeSpecificDay( int dayOfWeek, String time ) {
		boolean returnValue = true;
		
		switch(dayOfWeek) {
		case Calendar.SUNDAY:
			// See if this time already exists on that date...
			if ( !this.sundayTimes.contains(time) ) {
				//...it does not, add it.
				this.sundayTimes.add(time);
			}
			// Updated the schedule vector.
			this.addDay(Calendar.SUNDAY);
			break;
		case Calendar.MONDAY:
			// See if this time already exists on that date...
			if ( !this.mondayTimes.contains(time) ) {
				//...it does not, add it.
				this.mondayTimes.add(time);
			}
			// Updated the schedule vector.
			this.addDay(Calendar.MONDAY);
			break;
		case Calendar.TUESDAY:
			// See if this time already exists on that date...
			if ( !this.tuesdayTimes.contains(time) ) {
				//...it does not, add it.
				this.tuesdayTimes.add(time);
			}
			// Updated the schedule vector.
			this.addDay(Calendar.TUESDAY);
			break;
		case Calendar.WEDNESDAY:
			// See if this time already exists on that date...
			if ( !this.wednesdayTimes.contains(time) ) {
				//...it does not, add it.
				this.wednesdayTimes.add(time);
			}
			// Updated the schedule vector.
			this.addDay(Calendar.WEDNESDAY);
			break;
		case Calendar.THURSDAY:
			// See if this time already exists on that date...
			if ( !this.thursdayTimes.contains(time) ) {
				//...it does not, add it.
				this.thursdayTimes.add(time);
			}
			// Updated the schedule vector.
			this.addDay(Calendar.THURSDAY);
			break;
		case Calendar.FRIDAY:
			// See if this time already exists on that date...
			if ( !this.fridayTimes.contains(time) ) {
				//...it does not, add it.
				this.fridayTimes.add(time);
			}
			// Updated the schedule vector.
			this.addDay(Calendar.FRIDAY);
			break;
		case Calendar.SATURDAY:
			// See if this time already exists on that date...
			if ( !this.saturdayTimes.contains(time) ) {
				//...it does not, add it.
				this.saturdayTimes.add(time);
			}
			// Updated the schedule vector.
			this.addDay(Calendar.SATURDAY);
			break;
		default:
			Log.d(LOG_TAG, "Not sure how we got to the default case in addTimeSpecificDay()");
			break;
		}
		
		return returnValue;
	}
	
	public boolean removeTimeEveryDay(String time) {
		boolean returnValue = true;
		
		if ( !this.areDayVectorsEmpty() ) {
			// Remove the time from the day vector.
			this.sundayTimes.remove(time);
			// Check to see if the day vector is now empty...
			if ( this.sundayTimes.isEmpty()) {
				//...it is, remove that day from the schedule.
				this.removeDay(Calendar.SUNDAY);
			}
			// Remove the time from the day vector.
			this.mondayTimes.remove(time);
			// Check to see if the day vector is now empty...
			if ( this.mondayTimes.isEmpty()) {
				//...it is, remove that day from the schedule.
				this.removeDay(Calendar.MONDAY);
			}
			// Remove the time from the day vector.
			this.tuesdayTimes.remove(time);
			// Check to see if the day vector is now empty...
			if ( this.tuesdayTimes.isEmpty()) {
				//...it is, remove that day from the schedule.
				this.removeDay(Calendar.TUESDAY);
			}
			// Remove the time from the day vector.
			this.wednesdayTimes.remove(time);
			// Check to see if the day vector is now empty...
			if ( this.wednesdayTimes.isEmpty()) {
				//...it is, remove that day from the schedule.
				this.removeDay(Calendar.WEDNESDAY);
			}
			// Remove the time from the day vector.
			this.thursdayTimes.remove(time);
			// Check to see if the day vector is now empty...
			if ( this.thursdayTimes.isEmpty()) {
				//...it is, remove that day from the schedule.
				this.removeDay(Calendar.THURSDAY);
			}
			// Remove the time from the day vector.
			this.fridayTimes.remove(time);
			// Check to see if the day vector is now empty...
			if ( this.fridayTimes.isEmpty()) {
				//...it is, remove that day from the schedule.
				this.removeDay(Calendar.FRIDAY);
			}
			// Remove the time from the day vector.
			this.saturdayTimes.remove(time);
			// Check to see if the day vector is now empty...
			if ( this.saturdayTimes.isEmpty()) {
				//...it is, remove that day from the schedule.
				this.removeDay(Calendar.SATURDAY);
			}
		}
		
		return returnValue;
	}
	
	public boolean removeTimeSpecificDay( int dayOfWeek, String time ) {
		boolean returnValue = true;
		
		switch( dayOfWeek ) {
		case Calendar.SUNDAY:
			// Remove the time from the day vector.
			this.sundayTimes.remove(time);
			// Check to see if the day vector is now empty...
			if ( this.sundayTimes.isEmpty()) {
				//...it is, remove that day from the schedule.
				this.removeDay(Calendar.SUNDAY);
			}
			break;
		case Calendar.MONDAY:
			// Remove the time from the day vector.
			this.mondayTimes.remove(time);
			// Check to see if the day vector is now empty...
			if ( this.mondayTimes.isEmpty()) {
				//...it is, remove that day from the schedule.
				this.removeDay(Calendar.MONDAY);
			}
			break;
		case Calendar.TUESDAY:
			// Remove the time from the day vector.
			this.tuesdayTimes.remove(time);
			// Check to see if the day vector is now empty...
			if ( this.tuesdayTimes.isEmpty()) {
				//...it is, remove that day from the schedule.
				this.removeDay(Calendar.TUESDAY);
			}
			break;
		case Calendar.WEDNESDAY:
			// Remove the time from the day vector.
			this.wednesdayTimes.remove(time);
			// Check to see if the day vector is now empty...
			if ( this.wednesdayTimes.isEmpty()) {
				//...it is, remove that day from the schedule.
				this.removeDay(Calendar.WEDNESDAY);
			}
			break;
		case Calendar.THURSDAY:
			// Remove the time from the day vector.
			this.thursdayTimes.remove(time);
			// Check to see if the day vector is now empty...
			if ( this.thursdayTimes.isEmpty()) {
				//...it is, remove that day from the schedule.
				this.removeDay(Calendar.THURSDAY);
			}
			break;
		case Calendar.FRIDAY:
			// Remove the time from the day vector.
			this.fridayTimes.remove(time);
			// Check to see if the day vector is now empty...
			if ( this.fridayTimes.isEmpty()) {
				//...it is, remove that day from the schedule.
				this.removeDay(Calendar.FRIDAY);
			}
			break;
		case Calendar.SATURDAY:
			// Remove the time from the day vector.
			this.saturdayTimes.remove(time);
			// Check to see if the day vector is now empty...
			if ( this.saturdayTimes.isEmpty()) {
				//...it is, remove that day from the schedule.
				this.removeDay(Calendar.SATURDAY);
			}
			break;
		default:
			Log.d(LOG_TAG, "Not sure how we got to the default case in removeTimeSpecificDay()");
			break;
		}
		
		return returnValue;
	}
	
	public void clearAllTimes() {
		// Is this the best way to "clear" the values?
		
		// Clear any days that might be "scheduled"
		this.daysOfWeek = new Vector<Integer>();
		
		// Reinitialize all the vectors
		this.initializeAllDayVectors();
	}
	
	private boolean areDayVectorsEmpty() {
		
		if ( this.sundayTimes != null && !this.sundayTimes.isEmpty() 
				&& this.mondayTimes != null && !this.mondayTimes.isEmpty() 
				&& this.tuesdayTimes != null && !this.tuesdayTimes.isEmpty()
				&& this.wednesdayTimes != null && !this.wednesdayTimes.isEmpty()
				&& this.thursdayTimes != null && !this.thursdayTimes.isEmpty()
				&& this.fridayTimes != null && !this.fridayTimes.isEmpty()
				&& this.saturdayTimes != null && !this.saturdayTimes.isEmpty()) {
			return false;
		}
		
		return true;
	}
	
	private void initializeAllDayVectors() {
		this.sundayTimes = new Vector<String>();
		this.mondayTimes = new Vector<String>();
		this.tuesdayTimes = new Vector<String>();
		this.wednesdayTimes = new Vector<String>();
		this.thursdayTimes = new Vector<String>();
		this.fridayTimes = new Vector<String>();
		this.saturdayTimes = new Vector<String>();
	}
	
	private byte[] dayVectorToBytes(Vector<String> dayVector) {
		byte[] returnArray = null;
		
		Log.d(LOG_TAG, "Atempting to convert a day vector to bytes...");
		
		// Trim the vector to remove any unnecessary space.
		dayVector.trimToSize();
		
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
			ObjectOutputStream oos = new ObjectOutputStream(bos); 
			// Serialize the vector
			oos.writeObject(dayVector);
			// Flush the stream
			oos.flush();
			// Create the byte array
			byte[] relationBytes = bos.toByteArray();
			// Set the return value.
			returnArray = relationBytes;
			// Close the stream
			oos.close();
		} catch (IOException e) {
			Log.e(LOG_TAG, "Could not write the Vector<String> object to byte array.");
			e.printStackTrace();
		} 
		
		Log.d(LOG_TAG, "Returning after converting day vector to bytes...");
		
		return returnArray;
	}
	
	private Vector<String> dayVectorFromBytes(byte[] dayBytes) {
		Vector<String> dayVector = null;
		
		Log.d(LOG_TAG, "Attempting to parse day bytes into a vector...");
		
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(dayBytes);           
			ObjectInputStream ois = new ObjectInputStream(bis);   
			@SuppressWarnings("unchecked")
			// Read the vector
			Vector<String> tempVector = (Vector<String>)ois.readObject(); 
			// Set the return value
			dayVector = tempVector;
			// Close the stream.
			ois.close();
		} catch (Exception e) {
			Log.e(LOG_TAG, "Could not parse the Vector<String> object from byte array.");
			e.printStackTrace();
		}
		
		Log.d(LOG_TAG, "Returning after converting day bytes into a vector...");
		
		return dayVector;
	}

	private void addDay(int dayOfWeek) {
		if (!this.daysOfWeek.contains((Integer) dayOfWeek)) {
			this.daysOfWeek.add((Integer) dayOfWeek);
		}
	}

	private void removeDay(int dayOfWeek) {
		this.daysOfWeek.remove((Integer) dayOfWeek);
	}

	private Vector<Integer> getAllDays() {
		return this.daysOfWeek;
	}
	
	private boolean areDaysScheduled() {
		if ( this.daysOfWeek != null && !this.daysOfWeek.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	private void addAllDays() {
		this.addDay(Calendar.SUNDAY);
		this.addDay(Calendar.MONDAY);
		this.addDay(Calendar.TUESDAY);
		this.addDay(Calendar.WEDNESDAY);
		this.addDay(Calendar.THURSDAY);
		this.addDay(Calendar.FRIDAY);
		this.addDay(Calendar.SATURDAY);
	}

	private static String dayToColumnName(int dayOfWeek) {
		switch (dayOfWeek) {
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
	
	private static int columnNameToDay(String columnName) {
		int returnDate = -1;
		if (columnName.equals(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SUNDAY)) {
			returnDate = Calendar.SUNDAY;
		} else if (columnName.equals(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_MONDAY)) {
			returnDate = Calendar.MONDAY;
		} else if (columnName.equals(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_TUESDAY)) {
			returnDate = Calendar.TUESDAY;
		} else if (columnName.equals(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_WEDNESDAY)) {
			returnDate = Calendar.WEDNESDAY;
		} else if (columnName.equals(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_THURSDAY)) {
			returnDate = Calendar.THURSDAY;
		} else if (columnName.equals(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_FRIDAY)) {
			returnDate = Calendar.FRIDAY;
		} else if (columnName.equals(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SATURDAY)) {
			returnDate = Calendar.SATURDAY;
		}	
		return returnDate;
	}
	

	public static Vector<String> getScheduledDays(Cursor cursor) {
		Vector<String> returnVector = new Vector<String>();

		if (!cursor
				.isNull(cursor
						.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SUNDAY))) {
			Log.d(LOG_TAG, "Found data on Sunday.");
			returnVector
					.add(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SUNDAY);
		}

		if (!cursor
				.isNull(cursor
						.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_MONDAY))) {
			Log.d(LOG_TAG, "Found data on Monday.");
			returnVector
					.add(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_MONDAY);
		}

		if (!cursor
				.isNull(cursor
						.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_TUESDAY))) {
			Log.d(LOG_TAG, "Found data on Tuesday.");
			returnVector
					.add(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_TUESDAY);
		}

		if (!cursor
				.isNull(cursor
						.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_WEDNESDAY))) {
			Log.d(LOG_TAG, "Found data on Wednesday.");
			returnVector
					.add(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_WEDNESDAY);
		}

		if (!cursor
				.isNull(cursor
						.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_THURSDAY))) {
			Log.d(LOG_TAG, "Found data on Thursday.");
			returnVector
					.add(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_THURSDAY);
		}

		if (!cursor
				.isNull(cursor
						.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_FRIDAY))) {
			Log.d(LOG_TAG, "Found data on Friday.");
			returnVector
					.add(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_FRIDAY);
		}

		if (!cursor
				.isNull(cursor
						.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SATURDAY))) {
			Log.d(LOG_TAG, "Found data on Saturday.");
			returnVector
					.add(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SATURDAY);
		}

		// Trim the vector
		returnVector.trimToSize();

		// Return the array of string columns that contain valid times.
		return returnVector;
	}

	public ContentValues toContentValues(Context context) {
		ContentValues cv = new ContentValues();

		// Is this a new prescription? (check _id)
		boolean newPrescription = (this._id == INVALID_ID ? true : false);

		// Is this a new drug?
		boolean newDrug = (drug.get_id() == INVALID_ID ? true : false);

		// Is this a new patient?
		boolean newPatient = (patient.get_id() == INVALID_ID ? true : false);

		// If the drug is new, we need to store the drug
		if (newDrug) {
			// Store the drug
			int drugId = this.getDrug().storeDrug(context);
			// Store the new _id back to the drug object in memory.
			this.getDrug().set_id(drugId);
		}

		// If the patient is new, we need to store the patient
		if (newPatient) {
			// Store the patient.
			Uri patientUri = context.getContentResolver().insert(
					StorageProvider.PatientColumns.CONTENT_URI,
					this.getPatient().toContentValues());
			// Store the new _id back the patient object in memory.
			this.getPatient().set_id(
					Integer.parseInt(patientUri.getPathSegments().get(1)));
		}

		// If the prescription is not new, we need to provide the _id
		if (!newPrescription) {
			// Give the previous _id to the caller
			cv.put(StorageProvider.PrescriptionColumns._ID, "" + this.get_id());
		}

		/**
		 * STORE REQUIRED FIELDS
		 */
		// Store the patient id.
		cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_PATIENT, this
				.getPatient().get_id());
		// Store the drug id.
		cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_DRUG, this
				.getDrug().get_id());
		// Store the dose type.
		cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_DOSE_TYPE,
				this.getDoseType());
		// Store the scheduled flag.
		if (this.isScheduled()) {
			cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_SCHEDULED,
					SCHEDULED);
		} else {
			cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_SCHEDULED,
					NOT_SCHEDULED);
		}

		/**
		 * STORE OPTIONAL FIELDS
		 */
		// Store the dose size.
		if (this.getDoseSize() != -1) {
			cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_DOSE_SIZE,this.getDoseSize());
		}		
		// Store the total units.
		if (this.getTotalUnits() != -1) {
			cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_TOTAL_UNITS, this.getTotalUnits());
		}
		// Store the date filled.
		if (this.getFilled() != null) {
			cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_DATE_FILLED, this.getFilled().getTime());
		}
		// Store the Dr's name.
		if ( this.getDrName() != null && !this.getDrName().equalsIgnoreCase("NULL") ) {
			cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_DR_NAME, this.getDrName());
		}
		// Store the prescription id.
		if ( this.getPrescripID() != -1) {
			cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_UNIQUE_ID, this.getPrescripID());
		}
		// Store the prescription cost.
		if ( this.getCost() != -1) {
			cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_COST, this.getCost());
		}
		// Store the number of days supplied.
		if ( this.getNumDaysSupplied() != -1 ) {
			cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_NUM_DAYS_SUPPLIED, this.getNumDaysSupplied());
		}
		// Store the number of refills.
		if ( this.getNumRefills() != -1 ) {
			cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_NUM_REFILLS, this.getNumRefills());
		}
		// Store the expiration date.
		if ( this.getExpiration() != null) {
			cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_DATE_EXPIRATION, this.getExpiration().getTime());
		}
		
		// If there is schedule data...
		if ( this.isScheduled() ) {
			//...get an enum on the the schedule vector.
			Enumeration<Integer> daysEnum = this.daysOfWeek.elements();
			
			while (daysEnum.hasMoreElements()) {
				// Get the value from the enum (integer representation of the
				// day of the week)
				Integer value = daysEnum.nextElement();
				
				switch (value) {
				case Calendar.SUNDAY:
					// There is some data on sunday, add it to the content values
					Log.d(LOG_TAG, "Found some data on Sunday, converting it to add to the content values.");
					cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SUNDAY, this.dayVectorToBytes(this.sundayTimes));
					break;
				case Calendar.MONDAY:
					// There is some data on monday, add it to the content values
					Log.d(LOG_TAG, "Found some data on Monday, converting it to add to the content values.");
					cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_MONDAY, this.dayVectorToBytes(this.mondayTimes));
					break;
				case Calendar.TUESDAY:
					// There is some data on tuesday, add it to the content values
					Log.d(LOG_TAG, "Found some data on Tuesday, converting it to add to the content values.");
					cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_TUESDAY, this.dayVectorToBytes(this.tuesdayTimes));
					break;
				case Calendar.WEDNESDAY:
					// There is some data on wednesday, add it to the content values
					Log.d(LOG_TAG, "Found some data on Wednesday, converting it to add to the content values.");
					cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_WEDNESDAY, this.dayVectorToBytes(this.wednesdayTimes));
					break;
				case Calendar.THURSDAY:
					// There is some data on thursday, add it to the content values
					Log.d(LOG_TAG, "Found some data on Thursday, converting it to add to the content values.");
					cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_THURSDAY, this.dayVectorToBytes(this.thursdayTimes));
					break;
				case Calendar.FRIDAY:
					// There is some data on friday, add it to the content values
					Log.d(LOG_TAG, "Found some data on Friday, converting it to add to the content values.");
					cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_FRIDAY, this.dayVectorToBytes(this.fridayTimes));
					break;
				case Calendar.SATURDAY:
					// There is some data on saturday, add it to the content values
					Log.d(LOG_TAG, "Found some data on Saturday, converting it to add to the content values.");
					cv.put(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SATURDAY, this.dayVectorToBytes(this.saturdayTimes));
					break;
				default:
					// Not sure how we would get to this case, panic...
					Log.e(LOG_TAG, "Found some data on an unknown day in the toContentValues()");
					break;
				}
			}
			
		}

		// Return the 'ContentValues' to the caller
		return cv;

	}

	/**
	 * This function will take a cursor and parse the information found within returning 
	 * a Prescription object. If you are using this function you must have the following 
	 * columns in your cursor query projection:
	 * 
	 * PrescriptionColumns._ID
	 * PrescriptionColumns.PRESCRIPTION_PATIENT
	 * PrescriptionColumns.PRESCRIPTION_DRUG
	 * PrescriptionColumns.PRESCRIPTION_DOSE_TYPE
	 * 
	 * @param cursor the cursor to process
	 * @param context the application context (needed for ContentResolver queries)
	 * @return a populated Prescription object
	 */
	public static Prescription fromCursor(Cursor cursor, Context context) {
		// Create the new objects.
		Prescription newPrescription = null;
		Patient newPatient = null;
		Drug newDrug = null;
		
		/**
		 * GET THE REQUIRED FIELDS.
		 */
		int _id = cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns._ID));
		
		int patientId = cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_PATIENT));
		Log.d(LOG_TAG, "Patient ID: " + patientId);
		// Get the Patient object from the id
		Uri myPatient = ContentUris.withAppendedId(StorageProvider.PatientColumns.CONTENT_URI, patientId);
		Cursor patientCursor = context.getApplicationContext().getContentResolver().query(myPatient, null, null, null, null);
		patientCursor.moveToFirst();
		newPatient = Patient.fromCursor(patientCursor);
		patientCursor.close();
		
		int drugId = cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DRUG));
		Log.d(LOG_TAG, "Drug ID: " + drugId);
		// Get the drug object from the id
		Uri myDrug = ContentUris.withAppendedId(StorageProvider.DrugColumns.CONTENT_URI, drugId);
		Cursor drugCursor = context.getApplicationContext().getContentResolver().query(myDrug, null, null, null, null);
		drugCursor.moveToFirst();
		newDrug = Drug.fromCursor(drugCursor);
		drugCursor.close();
		
		// Get the dose type.
		int doseType = cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DOSE_TYPE));

		// Instantiate the prescription object.
		newPrescription = new Prescription(_id, newPatient, newDrug, doseType);
		
		/**
		 * GET THE OPTIONAL FIELDS.
		 */
		// Get the dose size.
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DOSE_SIZE))) {
			int doseSize = cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DOSE_SIZE));
			newPrescription.setDoseSize(doseSize);
		}	
		// Get the total units.
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_TOTAL_UNITS))) {
			int totalUnits = cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_TOTAL_UNITS));
			newPrescription.setTotalUnits(totalUnits);
		}
		// Get the date filled.
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DATE_FILLED))) {
			int dateFilledInt = cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DATE_FILLED));
			newPrescription.setFilled(new Date((long)dateFilledInt));
		}
		// Get the Dr's name.
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DR_NAME))) {
			newPrescription.setDrName(cursor.getString(cursor.getColumnIndex((StorageProvider.PrescriptionColumns.PRESCRIPTION_DR_NAME))));
		}
		// Get the prescription id.
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_UNIQUE_ID))) {
			newPrescription.setPrescripID(cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_UNIQUE_ID)));
		}
		// Get the prescription cost.
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_COST))) {
			newPrescription.setCost(cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_COST)));
		}
		// Get the number of days supplied.
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_NUM_DAYS_SUPPLIED))) {
			newPrescription.setNumDaysSupplied(cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_NUM_DAYS_SUPPLIED)));
		}
		// Get the number of refills.
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_NUM_REFILLS))) {
			newPrescription.setNumRefills(cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_NUM_REFILLS)));
		}
		// Get the expiration.
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DATE_EXPIRATION))) {
			int dateExpiredInt = cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DATE_EXPIRATION));
			newPrescription.setExpiration(new Date((long)dateExpiredInt));
		}
		// Get the scheduled days of the week.
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SUNDAY))) {
			Log.d(LOG_TAG, "Found cursor data on Sunday, attempting to parse and add it...");
			// Get the byte[] from the cursor
			byte[] sundayData = cursor.getBlob(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SUNDAY));
			// Parse and set the resulting vector of times
			newPrescription.sundayTimes = newPrescription.dayVectorFromBytes(sundayData);
			// Make sure the scheduling vector has been updated for this day
			newPrescription.addDay(columnNameToDay(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SUNDAY));
		}
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_MONDAY))) {
			Log.d(LOG_TAG, "Found cursor data on Monday, attempting to parse and add it...");
			byte[] mondayData = cursor.getBlob(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_MONDAY));
			newPrescription.mondayTimes = newPrescription.dayVectorFromBytes(mondayData);
			newPrescription.addDay(columnNameToDay(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_MONDAY));
		}
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_TUESDAY))) {
			Log.d(LOG_TAG, "Found cursor data on Tuesday, attempting to parse and add it...");
			byte[] tuesdayData = cursor.getBlob(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_TUESDAY));
			newPrescription.tuesdayTimes = newPrescription.dayVectorFromBytes(tuesdayData);
			newPrescription.addDay(columnNameToDay(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_TUESDAY));
		}
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_WEDNESDAY))) {
			Log.d(LOG_TAG, "Found cursor data on Wednesday, attempting to parse and add it...");
			byte[] wednesdayData = cursor.getBlob(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_WEDNESDAY));
			newPrescription.wednesdayTimes = newPrescription.dayVectorFromBytes(wednesdayData);
			newPrescription.addDay(columnNameToDay(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_WEDNESDAY));
		}
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_THURSDAY))) {
			Log.d(LOG_TAG, "Found cursor data on Thursday, attempting to parse and add it...");
			byte[] thursdayData = cursor.getBlob(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_THURSDAY));
			newPrescription.thursdayTimes = newPrescription.dayVectorFromBytes(thursdayData);
			newPrescription.addDay(columnNameToDay(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_THURSDAY));
		}
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_FRIDAY))) {
			Log.d(LOG_TAG, "Found cursor data on Friday, attempting to parse and add it...");
			byte[] fridayData = cursor.getBlob(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_FRIDAY));
			newPrescription.fridayTimes = newPrescription.dayVectorFromBytes(fridayData);
			newPrescription.addDay(columnNameToDay(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_FRIDAY));
		}
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SATURDAY))) {
			Log.d(LOG_TAG, "Found cursor data on Saturday, attempting to parse and add it...");
			byte[] saturdayData = cursor.getBlob(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SATURDAY));
			newPrescription.saturdayTimes = newPrescription.dayVectorFromBytes(saturdayData);
			newPrescription.addDay(columnNameToDay(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SATURDAY));
		}

		return newPrescription;
	}
	
	public String toString() {
		String returnString = "";
		
		returnString+= "Prescription ID: " + this.get_id() + "\n";
		returnString+= "Patient ID: " + this.getPatient().get_id() +"\n";
		returnString+= "Drug ID: " + this.getDrug().get_id() + "\n";
		returnString+= "Dose Type: " + this.getDoseType() + "\n";
		returnString+= "Dose Size: " + this.getDoseSize() + "\n";
		returnString+= "Total Units: " + this.getTotalUnits() + "\n";
		returnString+= "Scheduled: " + this.isScheduled() + "\n";
		
		if ( this.isScheduled() ) {
			
			Enumeration<Integer> dayEnum = this.getAllDays().elements();
			
			while(dayEnum.hasMoreElements()) {
				int day =  (Integer) dayEnum.nextElement();
				
				switch(day) {
				case Calendar.SUNDAY:
					// Sunday has some times scheduled...
					returnString+= "Schedule - Sunday: ";
					Enumeration<String> sundayEnum = this.sundayTimes.elements();
					do {
						returnString+= sundayEnum.nextElement();
						
						if (sundayEnum.hasMoreElements()) {
							returnString+= ", ";
						} else {
							returnString+="\n";
						}
					} while(sundayEnum.hasMoreElements());
					
					break;
				case Calendar.MONDAY:
					// Sunday has some times scheduled...
					returnString+= "Schedule - Monday: ";
					Enumeration<String> mondayEnum = this.mondayTimes.elements();
					do {
						returnString+= mondayEnum.nextElement();
						
						if (mondayEnum.hasMoreElements()) {
							returnString+= ", ";
						} else {
							returnString+="\n";
						}
					} while(mondayEnum.hasMoreElements());
					
					break;
				case Calendar.TUESDAY:
					// Sunday has some times scheduled...
					returnString+= "Schedule - Tuesday: ";
					Enumeration<String> tuesdayEnum = this.tuesdayTimes.elements();
					do {
						returnString+= tuesdayEnum.nextElement();
						
						if (tuesdayEnum.hasMoreElements()) {
							returnString+= ", ";
						} else {
							returnString+="\n";
						}
					} while(tuesdayEnum.hasMoreElements());
					
					break;
				case Calendar.WEDNESDAY:
					// Sunday has some times scheduled...
					returnString+= "Schedule - Wednesday: ";
					Enumeration<String> wednesdayEnum = this.wednesdayTimes.elements();
					do {
						returnString+= wednesdayEnum.nextElement();
						
						if (wednesdayEnum.hasMoreElements()) {
							returnString+= ", ";
						} else {
							returnString+="\n";
						}
					} while(wednesdayEnum.hasMoreElements());
					
					break;
				case Calendar.THURSDAY:
					// Sunday has some times scheduled...
					returnString+= "Schedule - Thursday: ";
					Enumeration<String> thursdayEnum = this.thursdayTimes.elements();
					do {
						returnString+= thursdayEnum.nextElement();
						
						if (thursdayEnum.hasMoreElements()) {
							returnString+= ", ";
						} else {
							returnString+="\n";
						}
					} while(thursdayEnum.hasMoreElements());
					
					break;
				case Calendar.FRIDAY:
					// Sunday has some times scheduled...
					returnString+= "Schedule - Friday: ";
					Enumeration<String> fridayEnum = this.fridayTimes.elements();
					do {
						returnString+= fridayEnum.nextElement();
						
						if (fridayEnum.hasMoreElements()) {
							returnString+= ", ";
						} else {
							returnString+="\n";
						}
					} while(fridayEnum.hasMoreElements());
					
					break;
				case Calendar.SATURDAY:
					// Sunday has some times scheduled...
					returnString+= "Schedule - Saturday: ";
					Enumeration<String> saturdayEnum = this.saturdayTimes.elements();
					do {
						returnString+= saturdayEnum.nextElement();
						
						if (saturdayEnum.hasMoreElements()) {
							returnString+= ", ";
						} else {
							returnString+="\n";
						}
					} while(saturdayEnum.hasMoreElements());
					
					break;
				default:
					Log.e(LOG_TAG, "Not sure why we are at the default case in the toString() method.");
					break;
				}
			}
		}
		
		return returnString;
	}
	
	
}
