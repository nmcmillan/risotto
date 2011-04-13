package com.risotto.view.drug;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.risotto.R;
import com.risotto.controller.StatusBarNotificationManager;
import com.risotto.model.Drug;
import com.risotto.service.MainService;
import com.risotto.storage.StorageProvider;


/**
 * Displays the list of drugs that have been added to the application.
 * Also allows for editing, adding, and removing drugs.
 * 
 * @author nick
 *
 */
public class DrugView extends ListActivity implements SimpleCursorAdapter.ViewBinder {
	
	protected final static String LOG_TAG = "DrugView";
	
	public static final String ACTION_VIEW_DRUG_DETAILS = "com.risotto.view.DrugDetailsViewStart";
	
	//String to define the name of the extra data being sent in intent to DrugDetailsView
	public static final String DRUG_DETAILS_DB_ID = "com.risotto.view.DrugView.DrugDetailsURI";
	
	private StatusBarNotificationManager stbm = new StatusBarNotificationManager(this);
	private ContentResolver contentResolver;
	private Drug newDrug;
	private Uri drugUri;
	
	//define location of buttons:
	public static final int MENU_ITEM_ADD_POSITION = Menu.FIRST;
	public static final int MENU_ITEM_REMOVE_ALL_POSITION = Menu.FIRST + 1;
	
	private static String[] DRUG_PROJECTION = {
		StorageProvider.DrugColumns._ID,
		StorageProvider.DrugColumns.DRUG_BRAND_NAME,
	};
	
	private static String[] DRUG_DETAILS_PROJECTION = {
		StorageProvider.DrugDetailColumns._ID,
		StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG,
		StorageProvider.DrugDetailColumns.DRUG_DETAILS_DRUG_STRENGTH
	};
	
	/**
	 * This method is only called once and that's the first time the 
	 * options menu is displayed.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
	    /*MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.drug_menu_layout, menu);
	    return true;*/
		menu.add(
				Menu.NONE, //group id for doing batch changes
				MENU_ITEM_ADD_POSITION, //position
				Menu.NONE, //order, see getOrder()
				R.string.drug_list_view_add) //name of button - link to XML
				.setIcon(android.R.drawable.ic_menu_add);
		
		return true;
	}
	
	/**
	 * This method should be utilized to update the menu every time it
	 * is displayed.  So for a view that will have it's menus change given
	 * a certain context, this method will be called.	
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		
		final boolean haveItems = this.getListAdapter().getCount() > 0;
		
		
		Log.d(LOG_TAG, "haveItems: " + haveItems);
		
		if(haveItems) {
			Uri uri = ContentUris.withAppendedId(getIntent().getData(), getSelectedItemId());
			//sending this intent, so there will have to be an
			//activity in manifest file that will "catch" this intent
			//but if there's more than one, in which order are the
			//activities called?
			Intent[] specifics = new Intent[1];
			specifics[0] = new Intent(Intent.ACTION_EDIT, uri);
			
			MenuItem[] items = new MenuItem[1];
			
			Intent intent = new Intent(null, uri);
            intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
            
            menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0, null, specifics, intent, 0, items);	
		} else {
			menu.removeGroup(Menu.CATEGORY_ALTERNATIVE);
		}
		
		return true;
	}
	
	/**
	 * Handles actions when buttons from the menu created in 'onCreateOptionsMenu'
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case MENU_ITEM_ADD_POSITION:
	    	Log.d(LOG_TAG, "MENU_ITEM_ADD clicked");
	    	
	    	//TO DO: pop up dialog
	    	startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
	    	return true;
	    case MENU_ITEM_REMOVE_ALL_POSITION:
	        Log.d(MainService.LOG_TAG, "You clicked remove all drugs");
	        
	        //for now, will only remove one drug
	        
	        if(null != drugUri) { //at least one drug has been added, for now it just points to the last drug added
	        	int numRowsDeleted = contentResolver.delete(drugUri, null, null);
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
	  
	  //sets default state of what happens when keys are pressed
	  //that are not handled by the application;
	  //multiple options here, see the javadoc
	  setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
	  
	  //This method will display all of the drugs in the database
	  //	- will we expect something to be passed in through the intent?
	  Intent intent = getIntent();
	  
	  if(null == intent.getData()) {
		  intent.setData(StorageProvider.DrugColumns.CONTENT_URI);
	  }
	
	  getListView().setOnCreateContextMenuListener(this);
	  
	  Cursor drugCursor = this.getContentResolver().query(getIntent().getData(), DRUG_PROJECTION, null, null, null);
	  Cursor detailsCursor = this.getContentResolver().query(StorageProvider.DrugDetailColumns.CONTENT_URI, null, null, null, null);
	  
	  Cursor[] cursorArray = { drugCursor, detailsCursor };
	  
	  MergeCursor mergedCursor = new MergeCursor(cursorArray);
	  
	  if(null != mergedCursor) {
		  startManagingCursor(mergedCursor);
		  
		  Log.d(LOG_TAG,"count: " + mergedCursor.getCount());
		  Log.d(LOG_TAG,"cursor column count: " + mergedCursor.getColumnCount());
		  
		  //mergedCursor
		  Cursor joinedCursor = StorageProvider.drugJoin();
		  
		  //Log.d(LOG_TAG,"joined count: " + joinedCursor.getCount());
		  
		  //joinedCursor.moveToFirst();
		  
		  /*String[] columns = joinedCursor.getColumnNames();
		  for(String name : columns) {
			  Log.d(LOG_TAG,"joined column: " + name);
		  }
		  
		  do {
			  Log.d(LOG_TAG, "joined brand name: " + joinedCursor.getString(joinedCursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_BRAND_NAME)));
		  } while (joinedCursor.moveToNext());*/
		  
		  
		  //Log.d(LOG_TAG,"joined columns: " + );
		  
		  //note: the cursor originally points to a null row, needs to move before trying to print data
		  //cursor.moveToFirst();
		  
		  //Log.d(LOG_TAG,"cursor.toString(0)" + cursor.getString(1));
		  
		  //TextView drugListItem = (TextView)this.findViewById(R.layout.drug_list_item);
		  
		  SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				  this,						//context
				  R.layout.drug_list_item,	//layout
				  joinedCursor,					//cursor
				  new String[] {StorageProvider.DrugColumns.DRUG_BRAND_NAME},	//column name 
				  //new int[] {R.id.drug_list_view_name,R.id.drug_list_view_strength}); //mapping
				  new int[] {R.id.drug_list_view_name}); //mapping
		  
		  adapter.setViewBinder(this);
		  setListAdapter(adapter);
	  }

	  registerForContextMenu(getListView());
	  
	  /*ListView lv = getListView();
	  lv.setTextFilterEnabled(true);

	  lv.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
	        int position, long id) {
	      // When clicked, show a toast with the TextView text
	      Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
	          Toast.LENGTH_SHORT).show();
	    }
	  });*/
	}
	
	
	@Override
	protected void onListItemClick(ListView l,View v,int position, long id) {		
			Intent editIntent = new Intent();
			editIntent.setAction(DrugView.ACTION_VIEW_DRUG_DETAILS);
			editIntent.setClass(getApplicationContext(), DrugDetailsView.class);
			editIntent.putExtra(DrugView.DRUG_DETAILS_DB_ID,  String.valueOf(id));
			startActivity(editIntent);
	}

	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		Log.d(LOG_TAG,"cursor index: " + cursor.getPosition());
		Drug drug;
		boolean drugsEqual = true;
		
		cursor.moveToLast();
		
		while(drugsEqual) {
			drug = Drug.fromCursor(cursor, this);
			TextView v;
			if(columnIndex == cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_BRAND_NAME)) {
				//convert drug name to correct string (drugName maps to TextView)
				v = (TextView) view;
				v.setText(drug.getBrandName());
			}
			
			//attempt to move cursor to next row, if it returns false, then we're done with the cursor
			if(cursor.moveToNext()) {
				//if the drug names aren't equal, then set drugs equal to false & move the cursor back one
				//so it will get moved ahead next time it's called
				if(!drug.getBrandName().equals(cursor.getString(cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_BRAND_NAME)))) {
					drugsEqual = false;
					}
			}
			else {
				return true;
			}
		}
		return true;
//		else if(columnIndex == cursor.getColumnIndex(StorageProvider.DrugColumns.DRUG_STRENGTH)) {
//			v = (TextView) view;
//			Vector<String> strength = drug.getStrength();
//			ListIterator<String> li = strength.listIterator();
//			//can do this because for a drug to be in the DB, it must have at least one strength
//			try {
//				String strenString = li.next() + " mg ";
//				while(li.hasNext()) {
//					strenString += ", " + li.next() + " mg ";
//				}
//				v.setText(strenString);
//				v.setTypeface(Typeface.create("null", Typeface.ITALIC));
//			} catch(NoSuchElementException e) {
//				return false;
//			}
//			
//		}
		//else {
			//return false;
		//}
		//return true;
	}
	
	
	
	
}
