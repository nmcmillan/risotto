package com.risotto.model;

import com.risotto.storage.StorageProvider;

import android.content.ContentValues;
import android.database.Cursor;

public class Schedule {
	
	private int prescriptionId;
	private long firstTime;
	private int interval;
	private long nextTime;
	private int countRemain = -1;
	
	// SCHEDULING CONSTANTS
	public static final int MS_IN_ONE_SECOND = 1000;
	public static final int SECONDS_IN_ONE_MINUTE = 60;
	public static final int MINUTES_IN_ONE_HOUR = 60;
	public static final int HOURS_IN_ONE_DAY = 24;
	public static final int TWENTY_FOUR_HOURS_IN_MS = MS_IN_ONE_SECOND * SECONDS_IN_ONE_MINUTE * MINUTES_IN_ONE_HOUR * HOURS_IN_ONE_DAY;
	public static final int DAYS_IN_ONE_WEEK = 7;
	public static final int SEVEN_DAYS_IN_MS = TWENTY_FOUR_HOURS_IN_MS * DAYS_IN_ONE_WEEK;
	public static final int TWENTY_FOUR_HOURS_IN_HOURS = 24;
	public static final int SEVEN_DAYS_IN_DAYS = 7;
	public static final int ZERO = 0;
	public static final int DEFAULT_COUNT_REMAINING = -1;
	
	// Unique ID for storage references
	private int _id;
	private static final int INVALID_ID = -1;
	
	
	/**
	 * Creates a new schedule object.
	 * 
	 * @param prescriptionId - db id of the prescription that is being scheduled
	 * @param firstTime - the first time, as a long, that the drug must be taken
	 * @param interval - the interval for a given drug
	 * @param nextTime - the next time a drug should be taken
	 * @param countRemain - how many more times the schedule should be activated
	 */
	public Schedule(int prescriptionId, long firstTime, int interval, long nextTime, int countRemain) {
		this(INVALID_ID, prescriptionId, firstTime, interval, nextTime, countRemain);
	}
	
	public Schedule(int prescriptionId, long firstTime, int interval, long nextTime) {
		this(INVALID_ID, prescriptionId, firstTime, interval, nextTime, DEFAULT_COUNT_REMAINING);
	}
	
	public Schedule(int prescriptionId, long firstTime, int interval, int countRemain) {
		this(INVALID_ID, prescriptionId, firstTime, interval, (firstTime + interval), countRemain);
	}
	
	public Schedule(int prescriptionId, long firstTime, int interval) {
		this(INVALID_ID, prescriptionId, firstTime, interval, (firstTime + interval), DEFAULT_COUNT_REMAINING);
	}
	
	private Schedule(int _id, int prescriptionId, long firstTime, int interval, long nextTime, int countRemain) {
		this._id = _id;
		this.prescriptionId = prescriptionId;
		this.firstTime = firstTime;
		this.interval = interval;
		this.nextTime = nextTime;
		this.countRemain = countRemain;
	}
	
	public int getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(int prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public long getFirstTime() {
		return firstTime;
	}

	public void setFirstTime(long firstTime) {
		this.firstTime = firstTime;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public long getNextTime() {
		return nextTime;
	}

	public void setNextTime(long nextTime) {
		this.nextTime = nextTime;
	}

	public int getCountRemain() {
		return countRemain;
	}

	public void setCountRemain(int countRemain) {
		this.countRemain = countRemain;
	}

	public int get_id() {
		return _id;
	}

	protected void set_id(int _id) {
		this._id = _id;
	}

	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		
		/**
		 * STORE REQUIRED FIELDS
		 */
		// Store the prescription id.
		cv.put(StorageProvider.ScheduleColumns.SCHEDULES_PRESCRIPTION, this.getPrescriptionId());
		// Store the first scheduled time.
		cv.put(StorageProvider.ScheduleColumns.SCHEDULES_FIRST_TIME, this.getFirstTime());
		// Store the interval.
		cv.put(StorageProvider.ScheduleColumns.SCHEDULES_INTERVAL, this.getInterval());
		// Store the next time.
		cv.put(StorageProvider.ScheduleColumns.SCHEDULES_NEXT_TIME, this.getNextTime());
		// Store the count remaining.
		cv.put(StorageProvider.ScheduleColumns.SCHEDULES_COUNT_REMAIN, this.getCountRemain());

		return cv;
	}
	
	public static Schedule fromCursor(Cursor cursor) {
		// Create the new schedule.
		Schedule newSchedule = null;
		
		/**
		 * GET THE REQUIRED FIELDS.
		 */
		int _id = cursor.getInt(cursor.getColumnIndex(StorageProvider.ScheduleColumns._ID));
		
		int prescriptionId = cursor.getInt(cursor.getColumnIndex(StorageProvider.ScheduleColumns.SCHEDULES_PRESCRIPTION));
		
		long firstTime = cursor.getInt(cursor.getColumnIndex(StorageProvider.ScheduleColumns.SCHEDULES_FIRST_TIME));
		
		int interval = cursor.getInt(cursor.getColumnIndex(StorageProvider.ScheduleColumns.SCHEDULES_INTERVAL));
		
		long nextTime = cursor.getInt(cursor.getColumnIndex(StorageProvider.ScheduleColumns.SCHEDULES_NEXT_TIME));
		
		int countRemain = cursor.getInt(cursor.getColumnIndex(StorageProvider.ScheduleColumns.SCHEDULES_COUNT_REMAIN));
		
		// Instantiate the schedule object.
		newSchedule = new Schedule(_id, prescriptionId, firstTime, interval, nextTime, countRemain);
		
		return newSchedule;
	}
	
}
