package com.risotto.storage;

import android.content.Context;
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
		
		String[] strengths = { "100", "200", "400" };
		Drug newDrug = new Drug(0, strengths, "Crazy Pills");
		
		Patient newPatient = new Patient("George", "Bush", Patient.GENDER_MALE);
		
		Prescription newPrescription = new Prescription(newPatient, newDrug, 1, 2, 3);
		
		log("Attempting to store the drug...");
		context.getContentResolver().insert(StorageProvider.DrugColumns.CONTENT_URI, newDrug.toContentValues());
		log("Attempting to store the patient...");
		context.getContentResolver().insert(StorageProvider.PatientColumns.CONTENT_URI, newPatient.toContentValues());
		log("Attempting to store the prescription...");
		context.getContentResolver().insert(StorageProvider.PrescriptionColumns.CONTENT_URI, newPrescription.toContentValues(context));
		
		
		log("Insert test complete.");
	}
	
	private static void updateTest(Context context) {
		log("Starting the update test...");
		
		log("Update test complete.");
	}
	
	private static void queryTest(Context context) {
		log("Starting the query test...");
		
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
