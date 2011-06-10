package com.risotto.model.event;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.util.Log;

import com.risotto.storage.StorageProvider;

public class NotificationEvent extends Event {
	
	private static final String LOG_TAG = "RISOTTO_NOTIFICATION_EVENT";
	
	private NotificationEventType eventType;
	private int prescriptionId;
	
	/*
	 * Drug notification base events:
	 * - Taken: The user has accepted the dialog and "taken" medication
	 * - Delay: The user has delayed the current medication alarm for a later date
	 * - Dismiss: The user has closed the drug reminder notification dialog
	 * - Skip: The user has chosen to skip this dosage
	 */
	public enum NotificationEventType {
		TAKEN,
		DELAY,
		DISMISS,
		SKIP,
		OTHER,
		NONE
	}
	
	public NotificationEvent(long timestamp, int prescriptionId, NotificationEventType eventType) {
		this(INVALID_ID, timestamp, prescriptionId, eventType);
	}
	
	private NotificationEvent(int _id, long timestamp, int prescriptionId, NotificationEventType eventType) {
		super(_id, timestamp);
		this.prescriptionId = prescriptionId;
		this.eventType = eventType;
	}
	
	public String toString() {
		
		String returnString =
			"Notification Event \n" +
			"Timestamp: " + new Date(timestamp).toString() + "\n" +
			"Event Type: " + eventType.name() + "\n" +
			"Prescription ID: " + prescriptionId;
		
		return returnString;
	}
	
	public static NotificationEvent fromCursor(Cursor notificationCursor)  throws CursorIndexOutOfBoundsException {
		
		Log.d(LOG_TAG, "Entering from cursor...");
		
		try {
			// Create the drug object
			NotificationEvent notificationEvent = null;
			
			// Get the ID
			int _id = notificationCursor.getInt(notificationCursor.getColumnIndex(StorageProvider.NotificationEventColumns._ID));
			
			// Get the timestamp
			long timestamp = INVALID_ID;
			if ( ! notificationCursor.isNull(notificationCursor.getColumnIndex(StorageProvider.NotificationEventColumns.NOTIFICATION_EVENTS_TIMESTAMP))) {
				timestamp = notificationCursor.getLong(notificationCursor.getColumnIndex(StorageProvider.NotificationEventColumns.NOTIFICATION_EVENTS_TIMESTAMP));
			}
			
			// Get the prescription id
			int prescriptionId = INVALID_ID;
			if ( ! notificationCursor.isNull(notificationCursor.getColumnIndex(StorageProvider.NotificationEventColumns.NOTIFICATION_EVENTS_PRESCRIPTION))) {
				prescriptionId = notificationCursor.getInt(notificationCursor.getColumnIndex(StorageProvider.NotificationEventColumns.NOTIFICATION_EVENTS_PRESCRIPTION));
			}
			
			// Get the event type
			int eventType = NotificationEventType.NONE.ordinal();
			NotificationEventType enumEventType = NotificationEventType.NONE;
			if ( ! notificationCursor.isNull(notificationCursor.getColumnIndex(StorageProvider.NotificationEventColumns.NOTIFICATION_EVENTS_EVENT_TYPE))) {
				eventType = notificationCursor.getInt(notificationCursor.getColumnIndex(StorageProvider.NotificationEventColumns.NOTIFICATION_EVENTS_EVENT_TYPE));
				// Convert the event type into the enum type
				enumEventType = NotificationEventType.values()[eventType];
			}
			
			// Instantiate the system event object
			notificationEvent = new NotificationEvent(_id, timestamp, prescriptionId, enumEventType);
			
			// Return the object
			return notificationEvent;
			
		} catch (CursorIndexOutOfBoundsException cioobe) {
			cioobe.printStackTrace();
			throw cioobe;
		}
	
	}
	
	public static NotificationEvent fromContentValues(ContentValues notificationContentValues) {
		NotificationEvent notificationEvent;
		
		// Get the ID
		int cvId = notificationContentValues.getAsInteger(StorageProvider.NotificationEventColumns._ID);
		
		// Get the timestamp
		long cvTimestamp = notificationContentValues.getAsLong(StorageProvider.NotificationEventColumns.NOTIFICATION_EVENTS_TIMESTAMP);
		
		// Get the prescription FK
		int cvPrescriptionId = notificationContentValues.getAsInteger(StorageProvider.NotificationEventColumns.NOTIFICATION_EVENTS_PRESCRIPTION);
		
		// Get the event type
		int cvEventType = notificationContentValues.getAsInteger(StorageProvider.NotificationEventColumns.NOTIFICATION_EVENTS_EVENT_TYPE);
		
		// Convert the  event int
		NotificationEventType enumEventType = NotificationEventType.values()[cvEventType];
		
		// Create the object
		notificationEvent = new NotificationEvent(cvId, cvTimestamp, cvPrescriptionId, enumEventType);
		
		// Return the object
		return notificationEvent;
	}
			
	public ContentValues toContentValues() {
		ContentValues notificationContentValues = new ContentValues();
		
		// Set the ID
		if (this._id != INVALID_ID) {
			notificationContentValues.put(StorageProvider.NotificationEventColumns._ID, this._id);
		}
		
		// Set the timestamp (required)
		notificationContentValues.put(StorageProvider.NotificationEventColumns.NOTIFICATION_EVENTS_TIMESTAMP, this.timestamp);
		
		// Set the prescription FK (required)
		notificationContentValues.put(StorageProvider.NotificationEventColumns.NOTIFICATION_EVENTS_PRESCRIPTION, this.prescriptionId);
		
		// Set the event type (required)
		notificationContentValues.put(StorageProvider.NotificationEventColumns.NOTIFICATION_EVENTS_EVENT_TYPE, this.eventType.ordinal());
		
		return notificationContentValues;
	}
	
	protected static void storeNotificationEvent(Context context, NotificationEvent notificationEvent) {
		ContentValues notificationContentValues = notificationEvent.toContentValues();
		context.getContentResolver().insert(StorageProvider.NotificationEventColumns.CONTENT_URI, notificationContentValues);
	}
	
}
