package com.risotto.view.drug;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.risotto.R;
import com.risotto.controller.StatusBarNotificationManager;
import com.risotto.model.Drug;
import com.risotto.service.MainService;
import com.risotto.storage.StorageProvider;

/**
 * Displays the list of drugs that have been added to the application. Also
 * allows for editing, adding, and removing drugs.
 * 
 * @author nick
 * 
 */
public class DrugView extends ListActivity implements SimpleCursorAdapter.ViewBinder {

	protected final static String LOG_TAG = "DrugView";

	public static final String ACTION_VIEW_DRUG_DETAILS = "com.risotto.view.DrugDetailsViewStart";

	// String to define the name of the extra data being sent in intent to
	public static final String DRUG_DETAILS_DB_ID = "com.risotto.view.drug.DrugView.DrugDetails_ID";
	public static final String DRUG_DETAILS_BRAND_NAME = "com.risotto.view.drug.DrugView.DrugDetails_NAME";
	private ContentResolver contentResolver;
	private Uri drugUri;

	// define location of buttons:
	public static final int MENU_ITEM_ADD_POSITION = Menu.FIRST;
	public static final int MENU_ITEM_REMOVE_ALL_POSITION = Menu.FIRST + 1;

	private static String[] DRUG_PROJECTION = {
			StorageProvider.DrugColumns._ID,
			StorageProvider.DrugColumns.DRUG_BRAND_NAME,
			StorageProvider.DrugColumns.DRUG_STRENGTH,
			StorageProvider.DrugColumns.DRUG_STRENGTH_LABEL};

	/**
	 * This method is only called once and that's the first time the options
	 * menu is displayed.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		/*
		 * MenuInflater inflater = getMenuInflater();
		 * inflater.inflate(R.layout.drug_menu_layout, menu); return true;
		 */
		menu.add(Menu.NONE, // group id for doing batch changes
				MENU_ITEM_ADD_POSITION, // position
				Menu.NONE, // order, see getOrder()
				R.string.drug_view_menu_add) // name of button - link to XML
				.setIcon(android.R.drawable.ic_menu_add);

		return true;
	}

	/**
	 * This method should be utilized to update the menu every time it is
	 * displayed. So for a view that will have it's menus change given a certain
	 * context, this method will be called.
	 */
	/*@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		final boolean haveItems = this.getListAdapter().getCount() > 0;

		Log.d(LOG_TAG, "haveItems: " + haveItems);

		if (haveItems) {
			Uri uri = ContentUris.withAppendedId(getIntent().getData(),
					getSelectedItemId());
			// sending this intent, so there will have to be an
			// activity in manifest file that will "catch" this intent
			// but if there's more than one, in which order are the
			// activities called?
			Intent[] specifics = new Intent[1];
			specifics[0] = new Intent(Intent.ACTION_EDIT, uri);

			MenuItem[] items = new MenuItem[1];

			Intent intent = new Intent(null, uri);
			intent.addCategory(Intent.CATEGORY_ALTERNATIVE);

			menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0, null,
					specifics, intent, 0, items);
		} else {
			//menu.removeGroup(Menu.CATEGORY_ALTERNATIVE);
		//}

		return true;
	}*/

	/**
	 * Handles actions when buttons from the menu created in
	 * 'onCreateOptionsMenu'
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case MENU_ITEM_ADD_POSITION:
			Log.d(LOG_TAG, "MENU_ITEM_ADD clicked");

			Intent intent = new Intent();
			
			intent.setClass(getApplicationContext(), DrugAdd.class);
			startActivity(intent);
			
			return true;
		case MENU_ITEM_REMOVE_ALL_POSITION:
			Log.d(MainService.LOG_TAG, "You clicked remove all drugs");

			// for now, will only remove one drug

			if (null != drugUri) { // at least one drug has been added, for now
									// it just points to the last drug added
				int numRowsDeleted = contentResolver
						.delete(drugUri, null, null);
				System.out.println("Num rows: " + numRowsDeleted);
			}

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// sets default state of what happens when keys are pressed
		// that are not handled by the application;
		// multiple options here, see the javadoc
		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
		
		Intent intent = getIntent();

		if (null == intent.getData()) {
			intent.setData(StorageProvider.DrugColumns.CONTENT_URI);
		}

		getListView().setOnCreateContextMenuListener(this);

		Cursor drugCursor = this.getContentResolver().query(
				StorageProvider.DrugColumns.CONTENT_URI, DRUG_PROJECTION, null, null, null);

		if (null != drugCursor) {
			startManagingCursor(drugCursor);

			Log.d(LOG_TAG, "count: " + drugCursor.getCount());
			Log.d(LOG_TAG, "cursor column count: " + drugCursor.getColumnCount());

			SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, // context
					R.layout.drug_list_item, // layout
					drugCursor, // cursor
					new String[] { StorageProvider.DrugColumns.DRUG_BRAND_NAME,
							StorageProvider.DrugColumns.DRUG_STRENGTH }, // column
																			// name
					new int[] { R.id.drug_list_view_name,
							R.id.drug_list_view_strength }); // mapping

			adapter.setViewBinder(this);
			setListAdapter(adapter);
		}
		
		registerForContextMenu(getListView());
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent editIntent = new Intent();
		editIntent.setAction(DrugView.ACTION_VIEW_DRUG_DETAILS);
		editIntent.setClass(getApplicationContext(), DrugDetailsView.class);
		editIntent.putExtra(DrugView.DRUG_DETAILS_DB_ID, String.valueOf(id));
		
		Uri drugUri = StorageProvider.DrugColumns.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
		
		Cursor dCursor = getContentResolver().query(
				drugUri, 
				DRUG_PROJECTION, 
				null, 
				null, 
				null);
		if(null != dCursor) {
			dCursor.moveToFirst();
			Drug selectedDrug = Drug.fromCursor(dCursor);
			editIntent.putExtra(DrugView.DRUG_DETAILS_BRAND_NAME, selectedDrug.getBrandName());
			startActivity(editIntent);
		}
		else {
			Log.d(LOG_TAG,"Couldn't find drug in database.");
		}
		dCursor.close();
	}

	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		Log.d(LOG_TAG, "cursor index: " + cursor.getPosition());
		Drug newDrug = Drug.fromCursor(cursor);

		if (columnIndex == cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_BRAND_NAME)) {
			((TextView)view).setText(newDrug.getBrandName());
		} else if (columnIndex == cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_STRENGTH)) {
			((TextView)view).setText(newDrug.getPrintableStrength());
			((TextView)view).setTypeface(Typeface.create("null", Typeface.ITALIC));
		} else {
			return false;
		}		
		return true;
	}
	
	/**
	 * Method called when a user presses and holds on an item in the list - creates the menu.
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	  super.onCreateContextMenu(menu, v, menuInfo);
	  AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
	  Cursor dCursor = (Cursor) this.getListView().getItemAtPosition(info.position);
	  
	  String drugName = dCursor.getString(dCursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_BRAND_NAME));
	  String strength = dCursor.getString(dCursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_STRENGTH));
	  String label = dCursor.getString(dCursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_STRENGTH_LABEL));

	  menu.setHeaderTitle(drugName + " at " + strength + label);
	  MenuInflater inflater = getMenuInflater();
	  inflater.inflate(R.layout.drug_view_context_menu_layout, menu);
	}
	
	/**
	 * Method called when a user presses a button on the context menu.
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.drug_view_context_menu_edit:
				return true;
			case R.id.drug_view_context_menu_remove:
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
				Cursor dCursor = (Cursor)this.getListView().getItemAtPosition((int)info.position);
				
				//Log.d(LOG_TAG,"first: " + pCursor.getString(pCursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_FIRST_NAME)));
				//Log.d(LOG_TAG,"last: " + pCursor.getString(pCursor.getColumnIndex(StorageProvider.PatientColumns.PATIENT_LAST_NAME)));
				//Log.d(LOG_TAG,"drug: " + pCursor.getString(pCursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_BRAND_NAME)));
				//Log.d(LOG_TAG, "prescription_patient: " + pCursor.getString(pCursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_PATIENT)));
				//Log.d(LOG_TAG, "prescription_drug: " + pCursor.getString(pCursor.getColumnIndex(StorageProvider.PrescriptionColumns.PRESCRIPTION_DRUG)));
				//Log.d(LOG_TAG,"_id: " + pCursor.getInt(pCursor.getColumnIndex(StorageProvider.PrescriptionColumns._ID)));
				//Log.d(LOG_TAG,"_id : (3) " + _id);
				
				int _id = dCursor.getInt(dCursor.getColumnIndex(StorageProvider.DrugColumns._ID));
				
				Uri drugUri = StorageProvider.DrugColumns.CONTENT_URI.buildUpon().appendPath(String.valueOf(_id)).build();
				if(getContentResolver().delete(drugUri,null,null) > 0) {
					Log.d(LOG_TAG,"delete success.");
					return true;
				} else {
					Log.d(LOG_TAG,"delete fail.");
					return false;
				}
			default:
				return super.onContextItemSelected(item);
		}
	}
}



