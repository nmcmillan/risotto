package com.risotto.model;

import java.util.Date;

import com.risotto.storage.StorageProvider;

import android.content.ContentValues;
import android.content.Context;

public class Event {
	
	protected int _id;
	protected static final int INVALID_ID = -1;
	protected long timestamp;
	
	private Event() {
		// Private default constructor.
	}
	
	public class NotificationEvent extends Event {
		private NotificationEventType eventType;
		private int prescriptionId;
		
		public NotificationEvent(long timestamp, int prescriptionId, NotificationEventType eventType) {
			this(INVALID_ID, timestamp, prescriptionId, eventType);
		}
		
		private NotificationEvent(int _id, long timestamp, int prescriptionId, NotificationEventType eventType) {
			this._id = _id;
			this.timestamp = timestamp;
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
		
		public NotificationEvent fromContentValues(ContentValues notificationContentValues) {
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
				
		public ContentValues toContentValues(NotificationEvent notificationEvent) {
			ContentValues notificationContentValues = new ContentValues();
			
			// Set the ID
			if (notificationEvent._id != INVALID_ID) {
				notificationContentValues.put(StorageProvider.NotificationEventColumns._ID, notificationEvent._id);
			}
			
			// Set the timestamp (required)
			notificationContentValues.put(StorageProvider.NotificationEventColumns.NOTIFICATION_EVENTS_TIMESTAMP, notificationEvent.timestamp);
			
			// Set the prescription FK (required)
			notificationContentValues.put(StorageProvider.NotificationEventColumns.NOTIFICATION_EVENTS_PRESCRIPTION, notificationEvent.prescriptionId);
			
			// Set the event type (required)
			notificationContentValues.put(StorageProvider.NotificationEventColumns.NOTIFICATION_EVENTS_EVENT_TYPE, notificationEvent.eventType.ordinal());
			
			return notificationContentValues;
		}
		
	}
	
	public class SystemEvent extends Event {
		private SystemEventType eventType;
		private SystemEventSubtype eventSubtype;
		private byte[] eventData;
		
		public SystemEvent(long timestamp, SystemEventType eventType, SystemEventSubtype eventSubtype, byte[] eventData) {
			this(INVALID_ID, timestamp, eventType, eventSubtype, eventData);
		}
		
		private SystemEvent(int _id, long timestamp, SystemEventType eventType, SystemEventSubtype eventSubtype, byte[] eventData) {
			this._id = INVALID_ID;
			this.timestamp = timestamp;
			this.eventType = eventType;
			this.eventSubtype = eventSubtype;
			this.eventData = eventData;
		}
		
		public String toString() {
			
			boolean dataPresent = ( eventData == null) ? false : true;
			
			String returnString =
				"System Event \n" +
				"Timestamp: " + new Date(timestamp).toString() + "\n" +
				"Event Type: " + eventType.name() + "\n" +
				"Event Subtype: " + eventSubtype.name() + "\n" +
				"Data? " + dataPresent + ""; 
			return returnString;
		}
		
		public SystemEvent fromContentValues(ContentValues systemContentValues) {
			SystemEvent systemEvent;
			
			// Get the ID
			int cvId = systemContentValues.getAsInteger(StorageProvider.SystemEventColumns._ID);
			
			// Get the timestamp
			long cvTimestamp = systemContentValues.getAsLong(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_TIMESTAMP);
			
			// Get the event type
			int cvEventType = systemContentValues.getAsInteger(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_EVENT_TYPE);
			
			// Convert the  event int
			SystemEventType enumEventType = SystemEventType.values()[cvEventType];
			
			// Get the event subtype
			int cvEventSubtype = systemContentValues.getAsInteger(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_EVENT_SUBTYPE);
			
			// Convert the  event int
			SystemEventSubtype enumEventSubtype = SystemEventSubtype.values()[cvEventSubtype];
			
			// Get the event data
			byte[] cvEventData = systemContentValues.getAsByteArray(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_EVENT_DATA);
			
			// Create the object
			systemEvent = new SystemEvent(cvId, cvTimestamp, enumEventType, enumEventSubtype, cvEventData);
			
			return systemEvent;
		}
		
		public ContentValues toContentValues(SystemEvent systemEvent) {
			ContentValues systemContentValues = new ContentValues();
			
			// Set the ID
			if (systemEvent._id != INVALID_ID) {
				systemContentValues.put(StorageProvider.SystemEventColumns._ID, systemEvent._id);
			}
			
			// Set the timestamp (required)
			systemContentValues.put(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_TIMESTAMP, systemEvent.timestamp);
			
			// Set the event type (required)
			systemContentValues.put(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_EVENT_TYPE, systemEvent.eventType.ordinal());
			
			// Set the event subtype (required)
			systemContentValues.put(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_EVENT_SUBTYPE, systemEvent.eventSubtype.ordinal());
			
			// Set the event data if it's not null
			if ( systemEvent.eventData != null) {
				systemContentValues.put(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_EVENT_DATA, systemEvent.eventData);
			}
			
			return systemContentValues;		
		}
		
	}
	
	private class SyncEvent extends Event {
		private SyncEventDirection direction;
		private int syncEventForeignTable;
		private int syncEventForeignKey;
		
	}
	
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
	
	/*
	 * Basic system event types dealing with model data:
	 * - Add: Data has been added to the device's DB/ContentProvider
	 * - Remove: Data has been removed from the device's DB/ContentProvider
	 * - Modify: Data has been changed on the device's DB/ContentProvider
	 */
	public enum SystemEventType {
		ADD,
		REMOVE,
		MODIFY,
		OTHER,
		NONE
	}
	
	/*
	 * System event subtypes: 
	 * - Patient: Information has changed in the Patient DB/ContentProvider
	 * - Drug: Information has changed in the Drug DB/ContentProvider
	 * - Prescription: Information has changed in the Prescription DB/ContentProvider
	 * - Schedule: Information has changed in the Schedule DB/ContentProvider 
	 */
	public enum SystemEventSubtype {
		PATIENT,
		DRUG,
		PRESCRIPTION,
		SCHEDULE,
		OTHER,
		NONE
	}
	
	/*
	 * Sync events
	 */
	public enum SyncEventType {
		
	}
	
	/*
	 * The direction of the sync event:
	 * - In: Server-side push to device
	 * - Out: Device push to server
	 */
	public enum SyncEventDirection {
		IN,
		OUT,
		OTHER,
		NONE
	}
	
	/*
	 * The status of the sync event:
	 * - Successful: all of the data was transferred
	 * - Failed: some/all of the data was not transferred
	 * - Interrupted: the sync was interrupted; server/device connection, battery pull, etc.
	 * - Delayed: Not sure when this case would be needed, no data connection?
	 */
	public enum SyncEventStatus {
		SUCCESSFUL,
		FAILED,
		INTERRUPTED,
		DELAYED,
		OTHER,
		NONE
	}
	
	
	/*
	 * Notification Event Methods.
	 */
	// Log with a prescription foreign key with the current time
	public static void logNotificationEvent(Context context, int prescriptionId, NotificationEventType eventType) {
		logNotificationEvent(context, System.currentTimeMillis(), prescriptionId, eventType);
	}
	
	// Log with a prescription object (will read Prescription._id) with the current time
	public static void logNotificationEvent(Context context, Prescription prescription, NotificationEventType eventType) {
		logNotificationEvent(context, System.currentTimeMillis(), prescription, eventType);
	}
	
	// Log with a prescription foreign key with the given timestamp
	public static void logNotificationEvent(Context context, Date timestamp, int prescriptionId, NotificationEventType eventType) {
		logNotificationEvent(context, timestamp.getTime(), prescriptionId, eventType);
	}
	
	// Log with a prescription object with the given timestamp
	public static void logNotificationEvent(Context context, Date timestamp, Prescription prescription, NotificationEventType eventType) {
		logNotificationEvent(context, timestamp.getTime(), prescription, eventType);
	}
	
	// Log with a prescription foreign key with the given long timestamp
	public static void logNotificationEvent(Context context, long timestamp, int prescriptionId, NotificationEventType eventType) {
		NotificationEvent event = new Event().new NotificationEvent(timestamp, prescriptionId, eventType);
		storeNotificationEvent(context, event);
	}
	
	// Log with a prescription object with the given long timestamp
	public static void logNotificationEvent(Context context, long timestamp, Prescription prescription, NotificationEventType eventType) {
		NotificationEvent event = new Event().new NotificationEvent(timestamp, prescription.get_id(), eventType);
		storeNotificationEvent(context, event);
	}
	
	// Private method to wrap content provider calls
	private static void storeNotificationEvent(Context context, NotificationEvent notificationEvent) {
		ContentValues notificationContentValues = notificationEvent.toContentValues(notificationEvent);
		context.getContentResolver().insert(StorageProvider.NotificationEventColumns.CONTENT_URI, notificationContentValues);
	}
	
	/*
	 * System Event Methods.
	 */
	// Log without any data at the current time
	public static void logSystemEvent(Context context, SystemEventType eventType, SystemEventSubtype eventSubtype) {
		logSystemEvent(context, System.currentTimeMillis(), eventType, eventSubtype, null);
	}
	
	// Log with data at the current time
	public static void logSystemEvent(Context context, SystemEventType eventType, SystemEventSubtype eventSubtype, byte[] eventData) {
		logSystemEvent(context, System.currentTimeMillis(), eventType, eventSubtype, eventData);
	}
	
	// Log without any data with the given Date timestamp
	public static void logSystemEvent(Context context, Date timestamp, SystemEventType eventType, SystemEventSubtype eventSubtype) {
		logSystemEvent(context, timestamp.getTime(), eventType, eventSubtype, null);
	}
	
	// Log with given data and given Date timestamp
	public static void logSystemEvent(Context context, Date timestamp, SystemEventType eventType, SystemEventSubtype eventSubtype, byte[] eventData) {
		logSystemEvent(context, timestamp.getTime(), eventType, eventSubtype, eventData);
	}
	
	// Log without any data with the given long timestamp
	public static void logSystemEvent(Context context, long timestamp, SystemEventType eventType, SystemEventSubtype eventSubtype) {
		SystemEvent event = new Event().new SystemEvent(timestamp, eventType, eventSubtype, null);
		storeSystemEvent(context, event);
	}
	
	// Log with data and the given long timestamp
	public static void logSystemEvent(Context context, long timestamp, SystemEventType eventType, SystemEventSubtype eventSubtype, byte[] eventData) {
		SystemEvent event = new Event().new SystemEvent(timestamp, eventType, eventSubtype, eventData);
		storeSystemEvent(context, event);
	}
	
	// Private method to wrap content provider calls
	private static void storeSystemEvent(Context context, SystemEvent systemEvent) {
		ContentValues systemContentValues = systemEvent.toContentValues(systemEvent);
		context.getContentResolver().insert(StorageProvider.SystemEventColumns.CONTENT_URI, systemContentValues);
	}
	
	/*
	 * Sync Event Methods.
	 */
	public static void logSyncEvent() {
		
	}

}
