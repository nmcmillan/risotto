package com.risotto.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.util.Log;

import com.risotto.storage.StorageProvider;

public class Patient {
	
	// Required Fields
	private String firstName;
	
	// Extra Constructor Fields
	private String lastName = "NULL";
	private GENDER gender = GENDER.DEFAULT;
	
	// Optional Fields
	private Hashtable<Integer, String> relations;
	private int age = -1;
	
	// Relation constants
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
		OTHER,
		DEFAULT
	}
	
	// Gender constants
	public enum GENDER {
		MALE,
		FEMALE,
		OTHER,
		DEFAULT
	}
	
	// Unique id used for storage references
	private int _id;
	private static final int INVALID_ID = -1;
	
	// DEBUG: LOG_TAG
	private static final String LOG_TAG = "RISOTTO_PATIENT";
	
	public Patient(String firstName) {
		this(INVALID_ID, firstName);
	}
	
	private Patient(int _id, String firstName) {
		this._id = _id;
		this.firstName = firstName;
	}
	
	public Patient(String firstName, String lastName, GENDER gender) {
		this(INVALID_ID, firstName, lastName, gender);
	}

	private Patient(int _id, String firstName, String lastName, GENDER gender) {
		this._id = _id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public GENDER getGender() {
		return gender;
	}

	public void setGender(GENDER gender) {
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
	
	public Hashtable<Integer, String> getRelations() {
		return this.relations;
	}
	
	private void setRelations(Hashtable<Integer, String> relations) {
		this.relations = relations;
	}

	public boolean addRelation(Context context, GENDER relationType, int relationId) {
		boolean returnValue = true;
		
		if ( this.relations == null ) {
			// The hash table has not been initialized, create it here
			this.relations = new Hashtable<Integer, String>();
		}
		
		// Check to see if that relation ID exists in the database...
		Cursor patientCursor = context.getApplicationContext().getContentResolver().query(Uri.withAppendedPath(com.risotto.storage.StorageProvider.PatientColumns.CONTENT_URI, String.valueOf(relationId)), null, null, null, null);
		
		if (patientCursor == null) {
			// No patient exists, cannot enter this relation...
			returnValue = false;
		} else {
			// A patient exists, add the relation to our data...
			// Note: This will overwrite a relationship to the patient indicated by relation id
			relations.put(relationId, relationType.toString());
			returnValue = true;
		}
		
		return returnValue;
	}
	
	public boolean removeRelation(int relationId) {
		boolean returnValue = true;
		
		if (this.relations == null) {
			// Cannot remove a relation if there are none
			returnValue = false;
		} else {
			// Our hashtable is not null, we can remove this relation
			// Remove the id but check to see if it existed at all
			returnValue = this.relations.remove(relationId) == null ? false : true;	
		}
		
		return returnValue;
	}
	
	public int get_id() {
		return _id;
	}

	protected void set_id(int _id) {
		this._id = _id;
	}
	
	public ContentValues toContentValues() {
		// Create a new 'ContentValues' to store our values
		ContentValues patientValues = new ContentValues();
		
		/**
		 * STORE ALL REQUIRED FIELDS.
		 */
		// Store the first name
		patientValues.put(StorageProvider.PatientColumns.PATIENT_FIRST_NAME, this.getFirstName());

		/**
		 * STORE ANY OPTIONAL FIELDS.
		 */
		// Store the last name
		if ( this.getLastName() != null && this.getLastName().equals("NULL")) {
			patientValues.put(StorageProvider.PatientColumns.PATIENT_LAST_NAME, this.getLastName());
		}
		// Store the value gender
		if ( this.getGender() != null && this.getGender() != GENDER.DEFAULT) {
			patientValues.put(StorageProvider.PatientColumns.PATIENT_GENDER, this.getGender().toString());
		}
		
		// Store the age
		if ( this.getAge() != -1) {
			patientValues.put(StorageProvider.PatientColumns.PATIENT_AGE, this.getAge());
		}
		
		// Store the relations
		if (this.relations != null) {
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
				ObjectOutputStream oos = new ObjectOutputStream(bos); 
				// Serialize the hashtable
				oos.writeObject(relations);
				// Flush the stream
				oos.flush();
				// Create the byte array
				byte[] relationBytes = bos.toByteArray();
				// Store the byte array to the content values
				patientValues.put(StorageProvider.PatientColumns.PATIENT_RELATIONS, relationBytes);
				// Close the stream
				oos.close();
				
			} catch (IOException e) {
				Log.e(LOG_TAG, "Could not write the relations object.");
				e.printStackTrace();
			} 
		}
		
		// Return the 'ContentValue' object
		return patientValues;
	}
	
	public static Patient fromCursor(Cursor cursor) throws CursorIndexOutOfBoundsException {
		
		Log.d(LOG_TAG, "Entering fromCursor()");
		
		try {
			// Create the Patient object
			Patient newPatient = null;
			
			/**
			 * GET THE REQUIRED FIELDS.
			 */
			// Get the integer id.
			int _id = cursor.getInt(cursor.getColumnIndex(StorageProvider.PatientColumns._ID));
			
			// Get the first name.
			String firstName = "NULL";
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_FIRST_NAME))) {
				firstName = cursor.getString(cursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_FIRST_NAME));
			}
			
			// Instantiate the patient object
			newPatient = new Patient(_id, firstName);
			
			/**
			 * GET THE OPTIONAL FIELDS.
			 */
			// Get the last name.
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_LAST_NAME))) {
				String lastName = cursor.getString(cursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_LAST_NAME));
				newPatient.setLastName(lastName);
			}
			// Get the gender.
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_GENDER))) {
				String gender = cursor.getString(cursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_GENDER));
				newPatient.setGender(GENDER.valueOf(gender));
			}
			// Get the age.
			if ( ! cursor.isNull(cursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_AGE))) {
				newPatient.setAge(cursor.getInt(cursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_AGE)));
			}
			
			// Get the relations.
			if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_RELATIONS))) {
				byte[] byteRelations = cursor.getBlob(cursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_RELATIONS));
				
				try {
					ByteArrayInputStream bis = new ByteArrayInputStream(byteRelations);           
					ObjectInputStream ois = new ObjectInputStream(bis);   
					@SuppressWarnings("unchecked")
					Hashtable<Integer, String> byteHash = (Hashtable<Integer, String>)ois.readObject(); 
					newPatient.setRelations(byteHash);
					// Close the stream.
					ois.close();
				} catch (Exception e) {
					Log.e(LOG_TAG, "Could not parse the relations object.");
					e.printStackTrace();
				}
			}
			
			// Return the new Patient object.
			return newPatient;
		} catch (CursorIndexOutOfBoundsException cioobe) {
			Log.e(LOG_TAG, "Exception in Patient fromCursor()...");
			cioobe.printStackTrace();
			throw cioobe;
		}

	}
	
}
