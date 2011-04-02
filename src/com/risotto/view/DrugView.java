package com.risotto.view;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
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
public class DrugView extends ListActivity {
	
	protected final static String LOG_TAG = "DrugView";
	
	private StatusBarNotificationManager stbm = new StatusBarNotificationManager(this);
	private ContentResolver contentResolver;
	private Drug newDrug;
	private Uri drugUri;
	
	//define location of buttons:
	public static final int MENU_ITEM_ADD_POSITION = Menu.FIRST;
	public static final int MENU_ITEM_REMOVE_ALL_POSITION = Menu.FIRST + 1;
	
	private static String[] PROJECTION = {
		StorageProvider.DrugColumns._ID,
		StorageProvider.DrugColumns.DRUG_NAME,
		StorageProvider.DrugColumns.DRUG_STRENGTH,
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
				this.MENU_ITEM_ADD_POSITION, //position
				Menu.NONE, //order, see getOrder()
				R.string.drug_list_view_add) //resource id - link to XML
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
            
            menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0, null, specifics, intent, 0,
                    items);	
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
	    	/*contentResolver = this.getContentResolver();
	    	int[] strength = {0,1,2,3};
	    	newDrug = new Drug(10,strength,"Tylenol");
	    	ContentValues newCv = newDrug.toContentValues();
	    	drugUri = contentResolver.insert(StorageProvider.DrugColumns.CONTENT_URI, newCv);
	    	System.out.println("Uri of newly inserted drug (Tylenol)" + drugUri.toString());
	        return true;*/
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
	  
	  Cursor cursor = this.getContentResolver().query(getIntent().getData(), PROJECTION, null, null, null);
	  
	  if(null != cursor) {
		  startManagingCursor(cursor);
		  
		  Log.d(LOG_TAG,"count: " + cursor.getCount());
		  Log.d(LOG_TAG,"cursor column count: " + cursor.getColumnCount());
		  
		  //note: the cursor originally points to a null row, needs to move before trying to print data
		  //cursor.moveToFirst();
		  
		  //Log.d(LOG_TAG,"cursor.toString(0)" + cursor.getString(1));
		  
		  TextView drugListItem = (TextView)this.findViewById(R.layout.drug_list_item);
		  
		  SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				  this,						//context
				  R.layout.drug_list_item,	//layout
				  cursor,					//cursor
				  new String[] {StorageProvider.DrugColumns.DRUG_NAME, StorageProvider.DrugColumns.DRUG_STRENGTH},	//column name 
				  new int[] {android.R.id.text1});	  //mapping
		  
		  
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
		
		Log.d(LOG_TAG,"inside onlistitemclick");
		Log.d(LOG_TAG,"position: " + position);
		Log.d(LOG_TAG,"id: " + id);
		
		Drug drugToDelete = (Drug) l.getItemAtPosition(position);
		
		//Uri drugUri = this.getContentResolver().query(StorageProvider.DrugColumns.CONTENT_URI, PROJECTION, null, null, null);
		
		
		
		//this.getContentResolver().delete(url, "", "");
	}
	
	
	
	
}
