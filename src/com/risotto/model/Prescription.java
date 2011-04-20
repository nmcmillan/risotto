package com.risotto.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
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
	private boolean scheduled = false;

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

	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}

	public boolean isScheduled() {
		return scheduled;
	}
	
	public boolean addTimeEveryDay(String time) {
		boolean returnValue = false;
		
		return returnValue;
	}
	
	public boolean addTimeSpecificDay( int dayOfWeek, String time ) {
		boolean returnValue = false;
		
		return returnValue;
	}
	
	public boolean removeTimeEveryDay(String time) {
		boolean returnValue = false;
		
		return returnValue;
	}
	
	public boolean removeTimeSpecificDay( int dayOfWeek, String time ) {
		boolean returnValue = false;
		
		return returnValue;
	}
	
	public boolean clearAllTimes() {
		boolean returnValue = false;
		
		return returnValue;
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
		
		return returnArray;
	}
	
	private Vector<String> dayVectorFromBytes(byte[] dayBytes) {
		Vector<String> dayVector = null;
		
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
		
		return dayVector;
	}

	public void addDay(int dayOfWeek) {
		if (!this.daysOfWeek.contains((Integer) dayOfWeek)) {
			this.daysOfWeek.add((Integer) dayOfWeek);
		}
	}

	public void removeDay(int dayOfWeek) {
		this.daysOfWeek.remove((Integer) dayOfWeek);
	}

	public Vector<Integer> getAllDays() {
		return this.daysOfWeek;
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
		
		// Store the days of the week that this prescription is needed
		if (!this.daysOfWeek.isEmpty()) {
			// Sort the vector in ascending order.
			Collections.sort(this.daysOfWeek);
			// Trim the vector
			this.daysOfWeek.trimToSize();
			// Get an enumeration
			Enumeration<Integer> daysEnum = this.daysOfWeek.elements();

			while (daysEnum.hasMoreElements()) {
				// Get the value from the enum (integer representation of the
				// day of the week)
				Integer value = daysEnum.nextElement();
				/**
				 * For the time being, just store a boolean flag to the days.
				 * Eventually the scheduled "times" will be stored in the
				 * columns.
				 */
				// TODO Figure out what we want to store in this structure.
				cv.put(dayToColumnName(value), 1);
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

		// Get the scheduled boolean.
		int scheduledBoolean = cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_SCHEDULED));
		boolean scheduled = false;
		if (scheduledBoolean == Prescription.SCHEDULED) {
			scheduled = true;
		}
		// Instantiate the prescription object.
		newPrescription = new Prescription(_id, newPatient, newDrug, doseType);
		newPrescription.setScheduled(scheduled);
		
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
		// TODO Figure out what data we want to store in this data structure...
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SUNDAY))) {
			int sundayData = cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SUNDAY));
			newPrescription.addDay(columnNameToDay(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SUNDAY));
		}
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_MONDAY))) {
			int mondayData = cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_MONDAY));
			newPrescription.addDay(columnNameToDay(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_MONDAY));
		}
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_TUESDAY))) {
			int tuesdayData = cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_TUESDAY));
			newPrescription.addDay(columnNameToDay(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_TUESDAY));
		}
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_WEDNESDAY))) {
			int wednesdayData = cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_WEDNESDAY));
			newPrescription.addDay(columnNameToDay(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_WEDNESDAY));
		}
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_THURSDAY))) {
			int thursdayData = cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_THURSDAY));
			newPrescription.addDay(columnNameToDay(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_THURSDAY));
		}
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_FRIDAY))) {
			int fridayData = cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_FRIDAY));
			newPrescription.addDay(columnNameToDay(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_FRIDAY));
		}
		if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SATURDAY))) {
			int saturdayData = cursor.getInt(cursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SATURDAY));
			newPrescription.addDay(columnNameToDay(StorageProvider.PrescriptionColumns.PRESCRIPTION_DAY_SATURDAY));
		}

		return newPrescription;
	}
}
