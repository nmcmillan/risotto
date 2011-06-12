package com.risotto.model.event;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.util.Log;

import com.risotto.storage.StorageProvider;

public class SystemEvent extends Event {
	
	private static final String LOG_TAG = "RISOTTO_SYSTEM_EVENT";
	
	private SystemEventType eventType;
	private SystemEventSubtype eventSubtype;
	private byte[] eventData;
	
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
	
	public SystemEvent(long timestamp, SystemEventType eventType, SystemEventSubtype eventSubtype, byte[] eventData) {
		this(INVALID_ID, timestamp, eventType, eventSubtype, eventData);
	}
	
	private SystemEvent(int _id, long timestamp, SystemEventType eventType, SystemEventSubtype eventSubtype, byte[] eventData) {
		super(_id, timestamp);
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
	
	public static SystemEvent fromCursor(Cursor systemCursor)  throws CursorIndexOutOfBoundsException {
		
		Log.d(LOG_TAG, "Entering from cursor...");
		
		try {
			// Create the system event object
			SystemEvent systemEvent = null;
			
			// Get the ID
			int _id = systemCursor.getInt(systemCursor.getColumnIndex(StorageProvider.SystemEventColumns._ID));
			
			// Get the timestamp
			long timestamp = INVALID_ID;
			if ( ! systemCursor.isNull(systemCursor.getColumnIndex(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_TIMESTAMP))) {
				timestamp = systemCursor.getLong(systemCursor.getColumnIndex(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_TIMESTAMP));
			}

			// Get the event type
			int eventType = SystemEventType.NONE.ordinal();
			SystemEventType enumEventType = SystemEventType.NONE;
			if ( ! systemCursor.isNull(systemCursor.getColumnIndex(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_EVENT_TYPE))) {
				eventType = systemCursor.getInt(systemCursor.getColumnIndex(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_EVENT_TYPE));
				// Convert the event type into the enum type
				enumEventType = SystemEventType.values()[eventType];
			}
			
			// Get the event subtype
			int eventSubtype = SystemEventSubtype.NONE.ordinal();
			SystemEventSubtype enumEventSubtype = SystemEventSubtype.NONE;
			if ( ! systemCursor.isNull(systemCursor.getColumnIndex(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_EVENT_SUBTYPE))) {
				eventSubtype = systemCursor.getInt(systemCursor.getColumnIndex(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_EVENT_SUBTYPE));
				// Convert the event type into the enum type
				enumEventSubtype = SystemEventSubtype.values()[eventSubtype];
			}
			
			// Get the data
			byte[] data = null;
			if ( ! systemCursor.isNull(systemCursor.getColumnIndex(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_EVENT_DATA))) {
				data = systemCursor.getBlob(systemCursor.getColumnIndex(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_EVENT_DATA));
			}
			
			// Instantiate the system event object
			systemEvent = new SystemEvent(_id, timestamp, enumEventType, enumEventSubtype, data);
			
			// Return the object
			return systemEvent;
			
		} catch (CursorIndexOutOfBoundsException cioobe) {
			cioobe.printStackTrace();
			throw cioobe;
		}
	
	}
	
	public static SystemEvent fromContentValues(ContentValues systemContentValues) {
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
	
	public ContentValues toContentValues() {
		ContentValues systemContentValues = new ContentValues();
		
		// Set the ID
		if (this._id != INVALID_ID) {
			systemContentValues.put(StorageProvider.SystemEventColumns._ID, this._id);
		}
		
		// Set the timestamp (required)
		systemContentValues.put(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_TIMESTAMP, this.timestamp);
		
		// Set the event type (required)
		systemContentValues.put(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_EVENT_TYPE, this.eventType.ordinal());
		
		// Set the event subtype (required)
		systemContentValues.put(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_EVENT_SUBTYPE, this.eventSubtype.ordinal());
		
		// Set the event data if it's not null
		if ( this.eventData != null) {
			systemContentValues.put(StorageProvider.SystemEventColumns.SYSTEM_EVENTS_EVENT_DATA, this.eventData);
		}
		
		return systemContentValues;		
	}
	
	protected static void storeSystemEvent(Context context, SystemEvent systemEvent) {
		ContentValues systemContentValues = systemEvent.toContentValues();
		context.getContentResolver().insert(StorageProvider.SystemEventColumns.CONTENT_URI, systemContentValues);
	}
	
}