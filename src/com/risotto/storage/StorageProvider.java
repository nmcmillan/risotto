package com.risotto.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StorageProvider {
	
    protected static final String LOG_TAG = "RisottoProvider";

    protected static final String DATABASE_NAME = "risotto.db";
    protected static final int DATABASE_VERSION = 2;
    protected static final String DRUGS_TABLE_NAME = "drugs";
    protected static final String PATIENTS_TABLE_NAME = "patients";
    protected static final String PRESCRIPTIONS_TABLE_NAME = "prescriptions";
    protected static final String SCHEDULES_TABLE_NAME = "schedules";

    // Column names - do these belong here?
    private static final String _ID = "_id";
    // DRUGS Table
    private static final String DRUG_NAME = "name";
    private static final String DRUG_UNIT_VOLUME = "unit_volume";
    private static final String DRUG_STRENGTH = "strength";
    // PATIENTS Table
    private static final String PATIENT_FIRST_NAME = "first_name";
    private static final String PATIENT_LAST_NAME = "last_name";
    private static final String PATIENT_GENDER = "gender";
    // PRESCRIPTIONS Table
    private static final String PRESCRIPTION_DOSE_TYPE = "dose_type";
    private static final String PRESCRIPTION_DOSE_SIZE = "dose_size";
    private static final String PRESCRIPTION_TOTAL_UNITS = "total_units";
    private static final String PRESCRIPTION_PATIENT = "patient";
    private static final String PRESCRIPTION_DRUG = "drug";
	// SCHEDULES Table
    private static final String SCHEDULES_NEXT_TIME = "next_time";
    private static final String SCHEDULES_DELAY = "delay";
    private static final String SCHEDULES_PRESCRIPTION = "prescription";
    
    private StorageDatabaseHelper dbHelper;
	
	public StorageProvider(Context context) {
		dbHelper = new StorageDatabaseHelper(context);
	}
	
	public SQLiteDatabase getDatabase() {
		return dbHelper.getWritableDatabase();
	}
    
	private class StorageDatabaseHelper extends SQLiteOpenHelper {

		public StorageDatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(LOG_TAG, "Creating the SQLite DB...");
			this.createDrugsTable(db);
			this.createPatientsTable(db);
			this.createPrescriptionsTable(db);
			this.createSchedulesTable(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(LOG_TAG, "Dropping all tables from version " + oldVersion + " to migrate to version " + newVersion);
			db.execSQL("DROP TABLE IF EXISTS " + DRUGS_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + PATIENTS_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + PRESCRIPTIONS_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + SCHEDULES_TABLE_NAME);
            onCreate(db);
		}
		
		
		private void createDrugsTable(SQLiteDatabase db) {		
			Log.d(LOG_TAG, "Creating the " + DRUGS_TABLE_NAME + " table...");
			db.execSQL("CREATE TABLE " + DRUGS_TABLE_NAME + " ("
					+ _ID + " INTEGER PRIMARY KEY,"
					+ DRUG_NAME + " TEXT,"
					+ DRUG_UNIT_VOLUME + " INTEGER,"
					// Add a label for unit volume
					+ DRUG_STRENGTH + " INTEGER"
					// Add a label for strength
					+ ");");	
		}
		
		private void createPatientsTable(SQLiteDatabase db) {
			Log.d(LOG_TAG, "Creating the " + PATIENTS_TABLE_NAME + " table...");
			db.execSQL("CREATE TABLE " + PATIENTS_TABLE_NAME + " ("
					+ _ID + " INTEGER PRIMARY KEY,"
					+ PATIENT_FIRST_NAME + " TEXT,"
					+ PATIENT_LAST_NAME + " TEXT,"
					+ PATIENT_GENDER + " TEXT"
					+ ");");
		}
		
		private void createPrescriptionsTable(SQLiteDatabase db) {
			Log.d(LOG_TAG, "Creating the " + PRESCRIPTIONS_TABLE_NAME + " table...");
			db.execSQL("CREATE TABLE " + PRESCRIPTIONS_TABLE_NAME + " ("
					+ _ID + " INTEGER PRIMARY KEY,"
					// Patient needs to be a FK
					+ PRESCRIPTION_PATIENT + " INTEGER,"
					// Drug needs to be a FK
					+ PRESCRIPTION_DRUG + " INTEGER,"
					+ PRESCRIPTION_DOSE_TYPE + " INTEGER,"
					+ PRESCRIPTION_DOSE_SIZE + " INTEGER,"
					+ PRESCRIPTION_TOTAL_UNITS + " INTEGER"
					+ ");");
		}
		
		private void createSchedulesTable(SQLiteDatabase db) {
			Log.d(LOG_TAG, "Creating the " + SCHEDULES_TABLE_NAME + " table...");
			db.execSQL("CREATE TABLE " + SCHEDULES_TABLE_NAME + " ("
					+ _ID + " INTEGER PRIMARY KEY,"
					+ SCHEDULES_NEXT_TIME + " INTEGER,"
					+ SCHEDULES_DELAY + " INTEGER,"
					// Prescription needs to be a FK
					+ SCHEDULES_PRESCRIPTION + " INTEGER"
					+ ");");
		}
	}
}
