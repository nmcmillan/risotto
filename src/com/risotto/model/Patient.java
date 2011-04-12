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
	private int gender;
	private String firstName;
	private String lastName;
	
	// Optional Fields
	private Hashtable<Integer, Integer> relations;
	
	// Relation constants
	public static final int RELATION_MOTHER = 0;
	public static final int RELATION_FATHER = 1;
	public static final int RELATION_DAUGHTER = 2;
	public static final int RELATION_SON = 3;
	public static final int RELATION_GRANDFATHER = 4;
	public static final int RELATION_GRANDMOTHER = 5;
	public static final int RELATION_GRANDSON = 6;
	public static final int RELATION_GRANDDAUGHTER = 7;
	public static final int RELATION_AUNT = 8;
	public static final int RELATION_UNCLE = 9;
	public static final int RELATION_COUSIN = 10;
	public static final int RELATION_OTHER = 11;
	
	// Gender constants
	public static final int GENDER_MALE = 0;
	public static final int GENDER_FEMALE = 0;
	public static final int GENDER_OTHER = 0;
	
	// Unique id used for storage references
	private int _id;
	private static final int INVALID_ID = -1;
	
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
	
	public Hashtable<Integer, Integer> getRelations() {
		return this.relations;
	}
	
	private void setRelations(Hashtable<Integer, Integer> relations) {
		this.relations = relations;
	}

	public boolean addRelation(Context appContext, int relationType, int relationId) {
		boolean returnValue = true;
		
		if ( this.relations == null ) {
			// The hash table has not been initialized, create it here
			this.relations = new Hashtable<Integer, Integer>();
		}
		
		// Check to see if that relation ID exists in the database...
		Cursor patientCursor = appContext.getApplicationContext().getContentResolver().query(Uri.withAppendedPath(com.risotto.storage.StorageProvider.PatientColumns.CONTENT_URI, String.valueOf(relationId)), null, null, null, null);
		
		if (patientCursor == null) {
			// No patient exists, cannot enter this relation...
			returnValue = false;
		} else {
			// A patient exists, add the relation to our data...
			// Note: This will overwrite a relationship to the patient indicated by relation id
			relations.put(relationId, relationType);
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

	public void set_id(int _id) {
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
		// Store the last name
		patientValues.put(StorageProvider.PatientColumns.PATIENT_LAST_NAME, this.getLastName());
		// Store the value gender
		patientValues.put(StorageProvider.PatientColumns.PATIENT_GENDER, this.getGender());
		
		/**
		 * STORE ANY OPTIONAL FIELDS.
		 */
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
				Log.e("RISOTTO_PATIENT", "Could not write the relations object.");
				e.printStackTrace();
			} 
		}
		
		// Return the 'ContentValue' object
		return patientValues;
	}
	
	public static Patient fromCursor(Cursor cursor) throws CursorIndexOutOfBoundsException {
		
		try {
			// Create the Patient object
			Patient newPatient = null;
			
			/**
			 * GET THE REQUIRED FIELDS.
			 */
			int _id = cursor.getInt(cursor.getColumnIndex(StorageProvider.PatientColumns._ID));
			String firstName = cursor.getString(cursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_FIRST_NAME));
			String lastName = cursor.getString(cursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_LAST_NAME));
			int gender = cursor.getInt(cursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_GENDER));
			
			// Instant. the patient
			newPatient = new Patient(_id, firstName, lastName, gender);
			
			/**
			 * GET THE OPTIONAL FIELDS.
			 */
			if ( !cursor.isNull(cursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_RELATIONS))) {
				byte[] byteRelations = cursor.getBlob(cursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_RELATIONS));
				
				try {
					ByteArrayInputStream bis = new ByteArrayInputStream(byteRelations);           
					ObjectInputStream ois = new ObjectInputStream(bis);   
					@SuppressWarnings("unchecked")
					Hashtable<Integer, Integer> byteHash = (Hashtable<Integer, Integer>)ois.readObject(); 
					newPatient.setRelations(byteHash);
					// Close the stream.
					ois.close();
				} catch (Exception e) {
					Log.e("RISOTTO_PATIENT", "Could not parse the relations object.");
					e.printStackTrace();
				}
			}
			
			// Return the new Patient object.
			return newPatient;
		} catch (CursorIndexOutOfBoundsException cioobe) {
			throw cioobe;
		}

	}
	
}
