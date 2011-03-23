package com.risotto.view;

import java.util.Enumeration;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hello.R;
import com.risotto.controller.StatusBarNotification;
import com.risotto.controller.StatusBarNotificationManager;
import com.risotto.service.MainService;
import com.risotto.storage.StorageProvider;

public class DrugView extends ListActivity {
	
	StatusBarNotificationManager stbm = new StatusBarNotificationManager(this);
	StorageProvider storProv = new StorageProvider();
	
	/**
	 * Create a menu that will pop up when the user presses the Menu button.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.drug_menu_layout, menu);
	    return true;
	}
	
	/**
	 * Handles actions when buttons from the menu created in 'onCreateOptionsMenu'
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.drug_menu_add:
	    	Log.d(MainService.LOG_TAG, "You clicked add drug");
	        return true;
	    case R.id.drug_menu_remove_all:
	        Log.d(MainService.LOG_TAG, "You clicked remove all drugs");
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);

	  String[] nots = new String[2];
	  
	  Enumeration<StatusBarNotification> n = stbm.getAllNotifications();
	  
	  int count = 0;
	  
	  while(null != n && n.hasMoreElements()) {
			nots[count] = n.nextElement().getStatusBarText();
			count++;
	  }
	  
	  setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nots));

	  registerForContextMenu(getListView());
	  
	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);

	  lv.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
	        int position, long id) {
	      // When clicked, show a toast with the TextView text
	      Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
	          Toast.LENGTH_SHORT).show();
	    }
	  });
	}
	
//	public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        TextView textview = new TextView(this);
//        textview.setText("This is the drug view class.");
//        setContentView(textview);
//    }
}
