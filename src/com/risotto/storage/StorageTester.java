package com.risotto.storage;

import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.risotto.model.Drug;
import com.risotto.model.Patient;
import com.risotto.model.Prescription;

public class StorageTester {
	
	private static final String LOG_TAG = "RISOTTO_STORAGE_TEST";

	public static void runTest(Context context) {
		insertTest(context);
		updateTest(context);
		deleteTest(context);
		queryTest(context);
	}
	
	private static void insertTest(Context context) {
		log("Starting the instert test...");
		
		Cursor myCursor = context.getContentResolver().query(StorageProvider.PrescriptionColumns.CONTENT_URI, null, null, null, null);
		
		// If there are no entries in the prescription table...
		if (!myCursor.moveToFirst()) {
			//...fill some out.
			
			log("Adding some static data to the database...");
			
			
			Drug tylenol = new Drug("Tylenol", Drug.TYPE.OVER_THE_COUNTER, 400, "mg");
			
			Drug vicadin = new Drug("Vicadin", Drug.TYPE.PRESCRIPTION, 500, "mg");
			
			Drug crazyPills = new Drug("Crazy Pills", Drug.TYPE.PRESCRIPTION, 1000, "ml");
			
			Patient georgeBush = new Patient("George", "Bush", Patient.GENDER.MALE);
			Patient billClinton = new Patient("Bill", "Clinton", Patient.GENDER.MALE);
			Patient bObama = new Patient("Barak", "Obama", Patient.GENDER.MALE);
			
			Prescription newPrescription = new Prescription(georgeBush, tylenol, 1, 2, 3);
			newPrescription.addDay(Calendar.MONDAY);
			newPrescription.addDay(Calendar.SATURDAY);
			newPrescription.setScheduled(true);
			
			Prescription anotherPrescription = new Prescription(billClinton, vicadin, 1, 2, 3);
			anotherPrescription.addDay(Calendar.TUESDAY);
			anotherPrescription.addDay(Calendar.THURSDAY);
			anotherPrescription.setScheduled(true);
			
			Prescription morePrescription = new Prescription(bObama, crazyPills, 1, 2, 3);
			
			log("Attempting to store the prescription...");
			context.getContentResolver().insert(StorageProvider.PrescriptionColumns.CONTENT_URI, newPrescription.toContentValues(context));
			context.getContentResolver().insert(StorageProvider.PrescriptionColumns.CONTENT_URI, anotherPrescription.toContentValues(context));
			context.getContentResolver().insert(StorageProvider.PrescriptionColumns.CONTENT_URI, morePrescription.toContentValues(context));
			
			log("Added some entries to the databases.");
		}
		
		myCursor.close();
		
		log("Insert test complete.");
	}
	
	private static void updateTest(Context context) {
		log("Starting the update test...");
		
		log("Update test complete.");
	}
	
	private static void queryTest(Context context) {
		log("Starting the query test...");
		
		Cursor drugCursor = context.getApplicationContext().getContentResolver().query(StorageProvider.DrugColumns.CONTENT_URI, null, null, null, null);
		
		drugCursor.moveToFirst();
		
		do {
			Drug newDrug = Drug.fromCursor(drugCursor);
			log("Drug ID: " + newDrug.get_id());
			log("Drug Name: " + newDrug.getBrandName());
			log("Drug Strengths: " + newDrug.getPrintableStrength());
			
		} while (drugCursor.moveToNext());
		
		drugCursor.close();
		
		Cursor prescriptionCursor = StorageProvider.prescriptionJoinQuery(null);		
		
		if ( prescriptionCursor != null ) {
		
			log("Prescription cursor is not null.");
			
			prescriptionCursor.moveToFirst();
			
			log("Prescritpion cursor count: " + prescriptionCursor.getCount());
			
			log("Prescription cursor columns: " + prescriptionCursor.getColumnCount());
			
			String[] colNames = prescriptionCursor.getColumnNames();
			
			for ( String name : colNames) {
				log("Column Name: " + name);
			}
			
			do {
				Prescription newPrescription = Prescription.fromCursor(prescriptionCursor, context);
				log("Prescription ID: " + newPrescription.get_id());
				log("Prescription Patient: " + newPrescription.getPatient().getFirstName());
				log("Prescription Drug: " + newPrescription.getDrug().getBrandName());
				log("Prescription Dose Type: " + newPrescription.getDoseType());
			} while (prescriptionCursor.moveToNext());
		
		} else {
			log("Prescription cursor is null.");
		}
		
		prescriptionCursor.close();
		
		log("Query test complete.");
	}
	
	private static void deleteTest(Context context) {
		log("Starting the delete test...");
		
		log("Delete test complete.");
	}
	
	private static void log(String message) {
		Log.d(LOG_TAG, message);
	}
	
}
