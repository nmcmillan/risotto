package com.risotto.model.event;

import android.content.Context;

import com.risotto.model.Prescription;
import com.risotto.model.event.NotificationEvent.NotificationEventType;
import com.risotto.model.event.SyncEvent.SyncEventDirection;
import com.risotto.model.event.SyncEvent.SyncEventStatus;
import com.risotto.model.event.SyncEvent.SyncEventType;
import com.risotto.model.event.SystemEvent.SystemEventSubtype;
import com.risotto.model.event.SystemEvent.SystemEventType;

public class Event {
	
	protected int _id;
	protected static final int INVALID_ID = -1;
	protected long timestamp;
	
	protected Event(long timestamp) {
		this(INVALID_ID, timestamp);
	}
	
	protected Event(int _id, long timestamp) {
		this._id = _id;
		this.timestamp = timestamp;
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
	
	// Log with a prescription foreign key with the given long timestamp
	public static void logNotificationEvent(Context context, long timestamp, int prescriptionId, NotificationEventType eventType) {
		NotificationEvent event = new NotificationEvent(timestamp, prescriptionId, eventType);
		NotificationEvent.storeNotificationEvent(context, event);
	}
	
	// Log with a prescription object with the given long timestamp
	public static void logNotificationEvent(Context context, long timestamp, Prescription prescription, NotificationEventType eventType) {
		NotificationEvent event = new NotificationEvent(timestamp, prescription.get_id(), eventType);
		NotificationEvent.storeNotificationEvent(context, event);
	}
	
	/*
	 * System Event Methods.
	 */
	// Log without any data at the current time
	public static void logSystemEvent(Context context, SystemEventType eventType, SystemEventSubtype eventSubtype) {
		logSystemEvent(context, System.currentTimeMillis(), eventType, eventSubtype);
	}
	
	// Log with data at the current time
	public static void logSystemEvent(Context context, SystemEventType eventType, SystemEventSubtype eventSubtype, byte[] eventData) {
		logSystemEvent(context, System.currentTimeMillis(), eventType, eventSubtype, eventData);
	}
	
	// Log without any data with the given long timestamp
	public static void logSystemEvent(Context context, long timestamp, SystemEventType eventType, SystemEventSubtype eventSubtype) {
		SystemEvent event = new SystemEvent(timestamp, eventType, eventSubtype, null);
		SystemEvent.storeSystemEvent(context, event);
	}
	
	// Log with data and the given long timestamp
	public static void logSystemEvent(Context context, long timestamp, SystemEventType eventType, SystemEventSubtype eventSubtype, byte[] eventData) {
		SystemEvent event = new SystemEvent(timestamp, eventType, eventSubtype, eventData);
		SystemEvent.storeSystemEvent(context, event);
	}
	
	/*
	 * Sync Event Methods.
	 */
	// Log without any data at the current time
	public static void logSyncEvent(Context context, SyncEventDirection syncDirection, SyncEventType eventType, SyncEventStatus syncStatus) {
		SyncEvent.logSyncEvent(context, System.currentTimeMillis(), syncDirection, eventType, syncStatus);
	}
	
	// Log with data at the current time
	public static void logSyncEvent(Context context, SyncEventDirection syncDirection, SyncEventType eventType, SyncEventStatus syncStatus, byte[] eventData) {
		logSyncEvent(context, System.currentTimeMillis(), syncDirection, eventType, syncStatus, eventData);
	}
	
	// Log without any data with the given long timestamp
	public static void logSyncEvent(Context context, long timestamp, SyncEventDirection syncDirection, SyncEventType eventType, SyncEventStatus syncStatus) {
		SyncEvent event = new SyncEvent(timestamp, syncDirection, eventType, syncStatus, null);
		SyncEvent.storeSyncEvent(context, event);
	}
	
	// Log with data and the given long timestamp
	public static void logSyncEvent(Context context, long timestamp, SyncEventDirection syncDirection, SyncEventType eventType, SyncEventStatus syncStatus, byte[] eventData) {
		SyncEvent event = new SyncEvent(timestamp, syncDirection, eventType, syncStatus, eventData);
		SyncEvent.storeSyncEvent(context, event);
	}

}
