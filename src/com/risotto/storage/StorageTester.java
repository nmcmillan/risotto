package com.risotto.storage;

import java.util.Calendar;
import java.util.Enumeration;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.risotto.model.Drug;
import com.risotto.model.DrugDetails;
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
			
			DrugDetails drugDetails = new DrugDetails(DrugDetails.TYPE_PRE, 200, "mg");
			DrugDetails drugDetails1 = new DrugDetails(DrugDetails.TYPE_PRE, 400, "mg");
			DrugDetails drugDetails2 = new DrugDetails(DrugDetails.TYPE_PRE, 10, "ml");
			DrugDetails drugDetails3 = new DrugDetails(DrugDetails.TYPE_PRE, 50, "ml");
			
			Drug tylenol = new Drug("Tylenol", drugDetails);
			tylenol.addDrugDetails(drugDetails1);
			
			Drug vicadin = new Drug("Vicadin", drugDetails2);
			
			Drug crazyPills = new Drug("Crazy Pills", drugDetails3);
			
			Patient georgeBush = new Patient("George", "Bush", Patient.GENDER_MALE);
			Patient billClinton = new Patient("Bill", "Clinton", Patient.GENDER_MALE);
			Patient bObama = new Patient("Barak", "Obama", Patient.GENDER_MALE);
			
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
			Drug newDrug = Drug.fromCursor(drugCursor, context);
			log("Drug ID: " + newDrug.get_id());
			log("Drug Name: " + newDrug.getBrandName());
			log("Drug Strengths: " + newDrug.getPrintableStrengths());
			
			Enumeration<DrugDetails> myEnum = newDrug.getDrugDetails().elements();
			
			while ( myEnum.hasMoreElements() ) {
				DrugDetails newDetails = myEnum.nextElement();
				log("Detail Type: " + newDetails.getType());
				log("Details Strength: " + newDetails.getStrength() + newDetails.getStrengthLabel());
			}
			
		} while (drugCursor.moveToNext());
		
		drugCursor.close();
		
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
