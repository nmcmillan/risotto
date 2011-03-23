package com.risotto.storage;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

public class StorageProvider extends ContentProvider {
	
    protected static final String LOG_TAG = "StorageProvider";
    
    public static final String AUTHORITY = StorageProvider.class.getPackage().getName() + ".provider";
    
    private static final String DATABASE_NAME = "risotto.db";
    private static final int DATABASE_VERSION = 2;
    private static final String DRUGS_TABLE_NAME = "drugs";
    private static final String PATIENTS_TABLE_NAME = "patients";
    private static final String PRESCRIPTIONS_TABLE_NAME = "prescriptions";
    private static final String SCHEDULES_TABLE_NAME = "schedules";
    
    // URI Matching ID's
    private static final int URI_TYPE_DRUGS = 0;
    private static final int URI_TYPE_DRUG_ID = 1;
    private static final int URI_TYPE_PATIENTS = 2;
    private static final int URI_TYPE_PATIENT_ID = 3;
    private static final int URI_TYPE_PRESCRIPTIONS = 4;
    private static final int URI_TYPE_PRESCRIPTION_ID = 5;
    private static final int URI_TYPE_SCHEDULES = 6;
    private static final int URI_TYPE_SCHEDULE_ID  = 7;
    
	// Set up the URI matcher
	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sUriMatcher.addURI(AUTHORITY, "drugs", URI_TYPE_DRUGS);
		sUriMatcher.addURI(AUTHORITY, "drugs/#", URI_TYPE_DRUG_ID);
		sUriMatcher.addURI(AUTHORITY, "patients", URI_TYPE_PATIENTS);
		sUriMatcher.addURI(AUTHORITY, "patients/#", URI_TYPE_PATIENT_ID);
		sUriMatcher.addURI(AUTHORITY, "prescriptions", URI_TYPE_PRESCRIPTIONS);
		sUriMatcher.addURI(AUTHORITY, "prescriptions/#", URI_TYPE_PRESCRIPTION_ID);
		sUriMatcher.addURI(AUTHORITY, "schedules", URI_TYPE_SCHEDULES);
		sUriMatcher.addURI(AUTHORITY, "schedules/#", URI_TYPE_SCHEDULE_ID);
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
					+ DrugColumns._ID + " INTEGER PRIMARY KEY,"
					+ DrugColumns.DRUG_NAME + " TEXT,"
					+ DrugColumns.DRUG_UNIT_VOLUME + " INTEGER,"
					+ DrugColumns.DRUG_UNIT_VOLUME_LABEL + " TEXT,"
					+ DrugColumns.DRUG_STRENGTH + " INTEGER,"
					+ DrugColumns.DRUG_STRENGTH_LABEL + " TEXT"
					+ ");");	
		}
		
		
		private void createPatientsTable(SQLiteDatabase db) {
			Log.d(LOG_TAG, "Creating the " + PATIENTS_TABLE_NAME + " table...");
			db.execSQL("CREATE TABLE " + PATIENTS_TABLE_NAME + " ("
					+ PatientColumns._ID + " INTEGER PRIMARY KEY,"
					+ PatientColumns.PATIENT_FIRST_NAME + " TEXT,"
					+ PatientColumns.PATIENT_LAST_NAME + " TEXT,"
					+ PatientColumns.PATIENT_GENDER + " TEXT"
					+ ");");
		}
		
		private void createPrescriptionsTable(SQLiteDatabase db) {
			Log.d(LOG_TAG, "Creating the " + PRESCRIPTIONS_TABLE_NAME + " table...");
			db.execSQL("CREATE TABLE " + PRESCRIPTIONS_TABLE_NAME + " ("
					+ PrescriptionColumns._ID + " INTEGER PRIMARY KEY,"
					// FOREIGN KEY(patient) REFERENCES patients(_id), 
					+ "FOREIGN KEY(" + PrescriptionColumns.PRESCRIPTION_PATIENT + ") REFERENCES " + PATIENTS_TABLE_NAME + "(" + PatientColumns._ID + "),"
					// FOREIGN KEY(drug) REFERENCES drugs(_id),
					+ "FOREIGN KEY(" + PrescriptionColumns.PRESCRIPTION_DRUG + ") REFERENCES " + DRUGS_TABLE_NAME + "(" + DrugColumns._ID + "),"
					+ PrescriptionColumns.PRESCRIPTION_DOSE_TYPE + " INTEGER,"
					+ PrescriptionColumns.PRESCRIPTION_DOSE_SIZE + " INTEGER,"
					+ PrescriptionColumns.PRESCRIPTION_TOTAL_UNITS + " INTEGER"
					+ ");");
		}
		
		private void createSchedulesTable(SQLiteDatabase db) {
			Log.d(LOG_TAG, "Creating the " + SCHEDULES_TABLE_NAME + " table...");
			db.execSQL("CREATE TABLE " + SCHEDULES_TABLE_NAME + " ("
					+ ScheduleColumns._ID + " INTEGER PRIMARY KEY,"
					+ ScheduleColumns.SCHEDULES_NEXT_TIME + " INTEGER,"
					+ ScheduleColumns.SCHEDULES_DELAY + " INTEGER,"
					// FOREIGN KEY(prescription) REFERENCES prescriptions(_id)
					+ "FOREIGN KEY(" + ScheduleColumns.SCHEDULES_PRESCRIPTION + ") REFERENCES " + PRESCRIPTIONS_TABLE_NAME + "(" + PrescriptionColumns._ID + ")"
					+ ");");
		}
	}
	
	public static final class DrugColumns implements BaseColumns {
		// This class cannot be instantiated
		private DrugColumns() {}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/drugs");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.risotto.drug";
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.risotto.drug";
		
		public static final String DRUG_NAME = "name";
		
	    public static final String DRUG_UNIT_VOLUME = "unit_volume";
	    
	    public static final String DRUG_UNIT_VOLUME_LABEL = "unit_volume_label";
	    
	    public static final String DRUG_STRENGTH = "strength";
	    
	    public static final String DRUG_STRENGTH_LABEL = "strength_label";
	    
	    public static final String DEFAULT_SORT_ORDER = DRUG_NAME + " DESC";	
	}
	
	public static final class PatientColumns implements BaseColumns {
		// This class cannot be instantiated
		private PatientColumns() {}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/patients");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.risotto.patient";
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.risotto.patient";
		
		private static final String PATIENT_FIRST_NAME = "first_name";
		
	    private static final String PATIENT_LAST_NAME = "last_name";
	    
	    private static final String PATIENT_GENDER = "gender";
	    
	    public static final String DEFAULT_SORT_ORDER = PATIENT_LAST_NAME + " DESC";	
	}
	
	public static final class PrescriptionColumns implements BaseColumns {
		// This class cannot be instantiated
		private PrescriptionColumns() {}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/prescriptions");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.risotto.prescription";
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.risotto.prescription";
		
	    private static final String PRESCRIPTION_DOSE_TYPE = "dose_type";
	    
	    private static final String PRESCRIPTION_DOSE_SIZE = "dose_size";
	    
	    private static final String PRESCRIPTION_TOTAL_UNITS = "total_units";
	    
	    private static final String PRESCRIPTION_PATIENT = "patient";
	    
	    private static final String PRESCRIPTION_DRUG = "drug";
	    
	    public static final String DEFAULT_SORT_ORDER = "modified DESC";	
	}
	
	public static final class ScheduleColumns implements BaseColumns {
		// This class cannot be instantiated
		private ScheduleColumns() {}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/schedules");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.risotto.schedule";
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.risotto.schedule";
		
	    private static final String SCHEDULES_NEXT_TIME = "next_time";
	    
	    private static final String SCHEDULES_DELAY = "delay";
	    
	    private static final String SCHEDULES_PRESCRIPTION = "prescription";
	    
	    public static final String DEFAULT_SORT_ORDER = SCHEDULES_NEXT_TIME + " DESC";	
	}

	private StorageDatabaseHelper mOpenHelper;
	
	@Override
	public boolean onCreate() {
		mOpenHelper = new StorageDatabaseHelper(getContext());
		return true;
	}

	/**
	 * Returns the MIME type of the data at the given URI
	 * 
	 * @param uri The URI to query
	 * @return The MIME type string, or null if there is no type
	 */
	@Override
	public String getType(Uri uri) {
		switch(sUriMatcher.match(uri)) {
		case URI_TYPE_DRUGS:
			return DrugColumns.CONTENT_TYPE;
		case URI_TYPE_DRUG_ID:
			return DrugColumns.CONTENT_ITEM_TYPE;
		case URI_TYPE_PATIENTS:
			return PatientColumns.CONTENT_TYPE;
		case URI_TYPE_PATIENT_ID:
			return PatientColumns.CONTENT_ITEM_TYPE;
		case URI_TYPE_PRESCRIPTIONS:
			return PrescriptionColumns.CONTENT_TYPE;
		case URI_TYPE_PRESCRIPTION_ID:
			return PrescriptionColumns.CONTENT_ITEM_TYPE;
		case URI_TYPE_SCHEDULES:
			return ScheduleColumns.CONTENT_TYPE;
		case URI_TYPE_SCHEDULE_ID:
			return ScheduleColumns.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		
		switch(sUriMatcher.match(uri)) {
		case URI_TYPE_DRUGS:
			Log.d(LOG_TAG, "Insert into the drugs table...");
			// Get a handle to the database
			SQLiteDatabase db = mOpenHelper.getWritableDatabase();
			long rowId = db.insert(DRUGS_TABLE_NAME, null, values);
			
			if (rowId > 0) {
				Uri drugUri = ContentUris.withAppendedId(DrugColumns.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(drugUri, null);
	            return drugUri;
			} else {
				throw new SQLException("Failed to insert row into " + uri);
			}
		case URI_TYPE_DRUG_ID:
			Log.d(LOG_TAG, "Use update to modify a row in the drug table...");
			throw new IllegalArgumentException("Invalid URI: " + uri);
		case URI_TYPE_PATIENTS:
			Log.d(LOG_TAG, "Insert into the patients table...");
			break;
		case URI_TYPE_PATIENT_ID:
			Log.d(LOG_TAG, "Use update to modify a row in the patients table...");
			throw new IllegalArgumentException("Invalid URI: " + uri);
		case URI_TYPE_PRESCRIPTIONS:
			Log.d(LOG_TAG, "Insert into the prescriptions table...");
			break;
		case URI_TYPE_PRESCRIPTION_ID:
			Log.d(LOG_TAG, "Use update to modify a row in the prescriptions table...");
			throw new IllegalArgumentException("Invalid URI: " + uri);
		case URI_TYPE_SCHEDULES:
			Log.d(LOG_TAG, "Insert into the schedules table...");
			break;
		case URI_TYPE_SCHEDULE_ID:
			Log.d(LOG_TAG, "Use update to modify a row in the schedules table...");
			throw new IllegalArgumentException("Invalid URI: " + uri);
		default:
			throw new IllegalArgumentException("Unknown URI " + uri); 
		}

		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		switch(sUriMatcher.match(uri)) {
		case URI_TYPE_DRUGS:
			Log.d(LOG_TAG, "Query for all drugs...");
			qb.setTables(DRUGS_TABLE_NAME);
			
	        String orderBy;
	        if (TextUtils.isEmpty(sortOrder)) {
	            orderBy = DrugColumns.DEFAULT_SORT_ORDER;
	        } else {
	            orderBy = sortOrder;
	        }
	        
	        // Get the database and run the query
	        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
	        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);

	        // Tell the cursor what uri to watch, so it knows when its source data changes
	        c.setNotificationUri(getContext().getContentResolver(), uri);
	        return c;
			
		case URI_TYPE_DRUG_ID:
			Log.d(LOG_TAG, "Query for one drug...");
			break;
		case URI_TYPE_PATIENTS:
			Log.d(LOG_TAG, "Query for all patients...");
			break;
		case URI_TYPE_PATIENT_ID:
			Log.d(LOG_TAG, "Query for one patient...");
			break;
		case URI_TYPE_PRESCRIPTIONS:
			Log.d(LOG_TAG, "Query for all prescriptions...");
			break;
		case URI_TYPE_PRESCRIPTION_ID:
			Log.d(LOG_TAG, "Query for one prescription...");
			break;
		case URI_TYPE_SCHEDULES:
			Log.d(LOG_TAG, "Query for all schedules...");
			break;
		case URI_TYPE_SCHEDULE_ID:
			Log.d(LOG_TAG, "Query for one schedule...");
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri); 
		}
	
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
