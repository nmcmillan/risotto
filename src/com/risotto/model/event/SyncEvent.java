package com.risotto.model.event;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.util.Log;

import com.risotto.storage.StorageProvider;

public class SyncEvent extends Event {
	
	private static final String LOG_TAG = "RISOTTO_SYNC_EVENT";
	
	private SyncEventDirection syncDirection;
	private SyncEventType eventType;
	private SyncEventStatus syncStatus;
	private byte[] eventData;
	
	/*
	 * Sync events
	 */
	public enum SyncEventType {
		STANDARD,
		OTHER,
		NONE
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
	
	public SyncEvent(long timestamp, SyncEventDirection syncDirection, SyncEventType eventType, SyncEventStatus syncStatus, byte[] eventData) {
		this(INVALID_ID, timestamp, syncDirection, eventType, syncStatus, eventData);
	}
	
	private SyncEvent(int _id, long timestamp, SyncEventDirection syncDirection, SyncEventType eventType, SyncEventStatus syncStatus, byte[] eventData) {
		super(_id, timestamp);
		this.syncDirection = syncDirection;
		this.eventType = eventType;
		this.syncStatus = syncStatus;
		this.eventData = eventData;
	}
	
	public String toString() {
		
		boolean dataPresent = ( eventData == null) ? false : true;
		
		String returnString =
			"Sync Event \n" +
			"Timestamp: " + new Date(timestamp).toString() + "\n" +
			"Sync Direction: " + syncDirection.name() +"\n" +
			"Event Type: " + eventType.name() + "\n" +
			"Sync Status: " + syncStatus.name() + "\n" +
			"Sync Data? " + dataPresent + "";
		
		return returnString;
	}
	
	public static SyncEvent fromCursor(Cursor syncCursor)  throws CursorIndexOutOfBoundsException {
		
		Log.d(LOG_TAG, "Entering from cursor...");
		
		try {
			// Create the system event object
			SyncEvent syncEvent = null;
			
			// Get the ID
			int _id = syncCursor.getInt(syncCursor.getColumnIndex(StorageProvider.SyncEventColumns._ID));
			
			// Get the timestamp
			long timestamp = INVALID_ID;
			if ( ! syncCursor.isNull(syncCursor.getColumnIndex(StorageProvider.SyncEventColumns.SYNC_EVENTS_TIMESTAMP))) {
				timestamp = syncCursor.getLong(syncCursor.getColumnIndex(StorageProvider.SyncEventColumns.SYNC_EVENTS_TIMESTAMP));
			}
			
			// Get the sync direction
			int syncDirection = SyncEventDirection.NONE.ordinal();
			SyncEventDirection enumSyncEventDirection = SyncEventDirection.NONE;
			if ( ! syncCursor.isNull(syncCursor.getColumnIndex(StorageProvider.SyncEventColumns.SYNC_EVENTS_DIRECTION))) {
				syncDirection = syncCursor.getInt(syncCursor.getColumnIndex(StorageProvider.SyncEventColumns.SYNC_EVENTS_DIRECTION));
				// Convert the direction type into the enum type
				enumSyncEventDirection = SyncEventDirection.values()[syncDirection];
			}

			// Get the event type
			int eventType = SyncEventType.NONE.ordinal();
			SyncEventType enumEventType = SyncEventType.NONE;
			if ( ! syncCursor.isNull(syncCursor.getColumnIndex(StorageProvider.SyncEventColumns.SYNC_EVENTS_EVENT_TYPE))) {
				eventType = syncCursor.getInt(syncCursor.getColumnIndex(StorageProvider.SyncEventColumns.SYNC_EVENTS_EVENT_TYPE));
				// Convert the event type into the enum type
				enumEventType = SyncEventType.values()[eventType];
			}
			
			// Get the sync status
			int syncStatus = SyncEventStatus.NONE.ordinal();
			SyncEventStatus enumSyncEventStatus = SyncEventStatus.NONE;
			if ( ! syncCursor.isNull(syncCursor.getColumnIndex(StorageProvider.SyncEventColumns.SYNC_EVENTS_EVENT_STATUS))) {
				syncStatus = syncCursor.getInt(syncCursor.getColumnIndex(StorageProvider.SyncEventColumns.SYNC_EVENTS_EVENT_STATUS));
				// Convert the direction type into the enum type
				enumSyncEventStatus = SyncEventStatus.values()[syncStatus];
			}
			
			// Get the data
			byte[] data = null;
			if ( ! syncCursor.isNull(syncCursor.getColumnIndex(StorageProvider.SyncEventColumns.SYNC_EVENTS_EVENT_DATA))) {
				data = syncCursor.getBlob(syncCursor.getColumnIndex(StorageProvider.SyncEventColumns.SYNC_EVENTS_EVENT_DATA));
			}
			
			// Instantiate the system event object
			syncEvent = new SyncEvent(_id, timestamp, enumSyncEventDirection, enumEventType, enumSyncEventStatus, data);
			
			// Return the object
			return syncEvent;
			
		} catch (CursorIndexOutOfBoundsException cioobe) {
			cioobe.printStackTrace();
			throw cioobe;
		}
	
	}
	
	public ContentValues toContentValues() {
		ContentValues syncContentValues = new ContentValues();
		
		// Set the ID
		if (this._id != INVALID_ID) {
			syncContentValues.put(StorageProvider.SyncEventColumns._ID, this._id);
		}
		
		// Set the timestamp (required)
		syncContentValues.put(StorageProvider.SyncEventColumns.SYNC_EVENTS_TIMESTAMP, this.timestamp);
		
		// Set the event direction (required)
		syncContentValues.put(StorageProvider.SyncEventColumns.SYNC_EVENTS_DIRECTION, this.syncDirection.ordinal());
		
		// Set the event type (required)
		syncContentValues.put(StorageProvider.SyncEventColumns.SYNC_EVENTS_EVENT_TYPE, this.eventType.ordinal());
		
		// Set the sync status (required)
		syncContentValues.put(StorageProvider.SyncEventColumns.SYNC_EVENTS_EVENT_STATUS, this.syncStatus.ordinal());
		
		// Set the event data if it's not null
		if ( this.eventData != null) {
			syncContentValues.put(StorageProvider.SyncEventColumns.SYNC_EVENTS_EVENT_DATA, this.eventData);
		}
		
		return syncContentValues;		
	}
	
	protected static void storeSyncEvent(Context context, SyncEvent syncEvent) {
		ContentValues syncContentValues = syncEvent.toContentValues();
		context.getContentResolver().insert(StorageProvider.SyncEventColumns.CONTENT_URI, syncContentValues);
	}
	
}
