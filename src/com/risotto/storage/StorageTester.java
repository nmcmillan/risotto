package com.risotto.storage;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.risotto.model.Drug;
import com.risotto.model.Patient;
import com.risotto.model.Prescription;
import com.risotto.model.event.Event;
import com.risotto.model.event.NotificationEvent;
import com.risotto.model.event.NotificationEvent.NotificationEventType;
import com.risotto.model.event.SyncEvent;
import com.risotto.model.event.SyncEvent.SyncEventDirection;
import com.risotto.model.event.SyncEvent.SyncEventStatus;
import com.risotto.model.event.SyncEvent.SyncEventType;
import com.risotto.model.event.SystemEvent;
import com.risotto.model.event.SystemEvent.SystemEventSubtype;
import com.risotto.model.event.SystemEvent.SystemEventType;

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
			
			Prescription newPrescription = new Prescription(georgeBush, tylenol, Prescription.DOSE_TYPE_EVERY_DAY, 2, 3);
			log(newPrescription.toString());
			
			Prescription anotherPrescription = new Prescription(billClinton, vicadin, Prescription.DOSE_TYPE_EVERY_HOUR, 2, 3);
			anotherPrescription.addTimeEveryDay("6:15");
			anotherPrescription.addTimeEveryDay("19:48");
			anotherPrescription.addTimeEveryDay("19:50");
			anotherPrescription.addTimeEveryDay("19:52");
			log(anotherPrescription.toString());
			
			Prescription morePrescription = new Prescription(bObama, crazyPills, Prescription.DOSE_TYPE_EVERY_HOUR_DAY_OF_WEEK , 2, 3);
			morePrescription.addTimeSpecificDay(Calendar.TUESDAY, "7:00");
			morePrescription.addTimeSpecificDay(Calendar.MONDAY, "19:51");
			log(morePrescription.toString());
			
			log("Attempting to store the prescriptions...");
			context.getContentResolver().insert(StorageProvider.PrescriptionColumns.CONTENT_URI, newPrescription.toContentValues(context));
			context.getContentResolver().insert(StorageProvider.PrescriptionColumns.CONTENT_URI, anotherPrescription.toContentValues(context));
			context.getContentResolver().insert(StorageProvider.PrescriptionColumns.CONTENT_URI, morePrescription.toContentValues(context));
			
			log("Added some entries to the databases.");			
		}
		
		myCursor.close();
		
		Cursor notificationEventsCursor = context.getContentResolver().query(StorageProvider.NotificationEventColumns.CONTENT_URI, null, null, null, null);
		
		// If there are no entries in the notification table...
		if (!notificationEventsCursor.moveToFirst()) {
			//...fill some out.
			
			Event.logNotificationEvent(context, 1, NotificationEventType.TAKEN);
			Event.logNotificationEvent(context, new Date().getTime(), 2, NotificationEventType.DISMISS);
			Event.logNotificationEvent(context, System.currentTimeMillis(), 2, NotificationEventType.SKIP);
			
		}
		
		notificationEventsCursor.close();
		
		Cursor systemEventsCursor = context.getContentResolver().query(StorageProvider.NotificationEventColumns.CONTENT_URI, null, null, null, null);
		
		// If there are no entries in the notification table...
		if (!notificationEventsCursor.moveToFirst()) {
			//...fill some out.
		
			byte[] myArray = { (byte)0, (byte)1, (byte)2, (byte)3 };	
			
			Event.logSystemEvent(context, SystemEventType.ADD, SystemEventSubtype.DRUG);
			Event.logSystemEvent(context, SystemEventType.REMOVE, SystemEventSubtype.PATIENT, myArray);
			Event.logSystemEvent(context, new Date().getTime(), SystemEventType.MODIFY, SystemEventSubtype.SCHEDULE, myArray);
			Event.logSystemEvent(context, System.currentTimeMillis(), SystemEventType.ADD, SystemEventSubtype.PRESCRIPTION, myArray);
		}
		
		systemEventsCursor.close();
		
		Cursor syncEventsCursor = context.getContentResolver().query(StorageProvider.NotificationEventColumns.CONTENT_URI, null, null, null, null);
		
		// If there are no entries in the notification table...
		if (!notificationEventsCursor.moveToFirst()) {
			//...fill some out.
			
			byte[] myArray = { (byte)0, (byte)1, (byte)2, (byte)3 };
			
			Event.logSyncEvent(context, SyncEventDirection.IN, SyncEventType.STANDARD, SyncEventStatus.SUCCESSFUL);
			Event.logSyncEvent(context, SyncEventDirection.IN, SyncEventType.STANDARD, SyncEventStatus.FAILED, myArray );
			Event.logSyncEvent(context, System.currentTimeMillis(), SyncEventDirection.IN, SyncEventType.STANDARD, SyncEventStatus.DELAYED, myArray );
			
		}
		
		syncEventsCursor.close();
		
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
				log(newPrescription.toString());
			} while (prescriptionCursor.moveToNext());
		
		} else {
			log("Prescription cursor is null.");
		}
		
		prescriptionCursor.close();
		
		
		// Notification Events
		Cursor notificationEventsCursor = context.getApplicationContext().getContentResolver().query(StorageProvider.NotificationEventColumns.CONTENT_URI, null, null, null, null);		
		
		if ( notificationEventsCursor != null ) {
		
			log("Notification events cursor is not null.");
			
			notificationEventsCursor.moveToFirst();
			
			log("Notification events cursor count: " + notificationEventsCursor.getCount());
			
			log("Notification events cursor columns: " + notificationEventsCursor.getColumnCount());
			
			String[] colNames = notificationEventsCursor.getColumnNames();
			
			for ( String name : colNames) {
				log("Column Name: " + name);
			}
			
			do {
				NotificationEvent notificationEvent = NotificationEvent.fromCursor(notificationEventsCursor);
				log(notificationEvent.toString());
			} while (notificationEventsCursor.moveToNext());
		
		} else {
			log("Notification events cursor is null.");
		}
		
		notificationEventsCursor.close();
		
		// System Events
		Cursor systemEventsCursor = context.getApplicationContext().getContentResolver().query(StorageProvider.SystemEventColumns.CONTENT_URI, null, null, null, null);		
		
		if ( systemEventsCursor != null ) {
		
			log("System events cursor is not null.");
			
			systemEventsCursor.moveToFirst();
			
			log("System events cursor count: " + systemEventsCursor.getCount());
			
			log("System events cursor columns: " + systemEventsCursor.getColumnCount());
			
			String[] colNames = systemEventsCursor.getColumnNames();
			
			for ( String name : colNames) {
				log("Column Name: " + name);
			}
			
			do {
				SystemEvent systemEvent = SystemEvent.fromCursor(systemEventsCursor);
				log(systemEvent.toString());
			} while (systemEventsCursor.moveToNext());
		
		} else {
			log("System events cursor is null.");
		}
		
		systemEventsCursor.close();
		
		// Sync Events
		Cursor syncEventsCursor = context.getApplicationContext().getContentResolver().query(StorageProvider.SyncEventColumns.CONTENT_URI, null, null, null, null);		
		
		if ( syncEventsCursor != null ) {
		
			log("Sync events cursor is not null.");
			
			syncEventsCursor.moveToFirst();
			
			log("Sync events cursor count: " + syncEventsCursor.getCount());
			
			log("Sync events cursor columns: " + syncEventsCursor.getColumnCount());
			
			String[] colNames = syncEventsCursor.getColumnNames();
			
			for ( String name : colNames) {
				log("Column Name: " + name);
			}
			
			do {
				SyncEvent syncEvent = SyncEvent.fromCursor(syncEventsCursor);
				log(syncEvent.toString());
			} while (syncEventsCursor.moveToNext());
		
		} else {
			log("Sync events cursor is null.");
		}
		
		syncEventsCursor.close();
		
		
		
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
