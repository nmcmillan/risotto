package com.hello;

import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.risotto.controller.StatusBarNotification;
import com.risotto.controller.StatusBarNotificationManager;
import com.risotto.model.Drug;
import com.risotto.model.Patient;
import com.risotto.model.Prescription;
import com.risotto.storage.StorageProvider;
import com.risotto.view.AlarmView;
import com.risotto.view.DrugView;

public class HelloWorld extends TabActivity {
	
	/*
	 * When the device is rotated, Android runs through the full lifecycle of your application.  It calls pause, stop, destroy
	 * and then create, start, resume every time the phone's orientation is changed.
	 */
	StatusBarNotification vicodin = null;
	StatusBarNotification advil = null;
    int vicID = -1;
    int adID = -1;
    Patient patient = new Patient();
    Drug drug = new Drug();
    Prescription prep = new Prescription(patient, drug, Prescription.DOSE_TYPE.EVERY_DAY, 2, 20);

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        StatusBarNotificationManager notMgr = new StatusBarNotificationManager(this.getApplicationContext());
        
        vicodin = new StatusBarNotification(this.getApplicationContext(), prep, "Vicodin", "Remember to take 2 pills of vicodin", "Be sure to eat something first.");
        advil = new StatusBarNotification(this.getApplicationContext(), prep, "Advil", "Remember to take 2 pills of vicodin", "Be sure to eat something first.");
        
        
         * TO DO (what happens if we want to add vibrate to the notification, but we call the setVibrate method after adding
         * it to the manager?
         *  - still works because it's only a reference to the object, so when the object gets updated, there's no updating needed
         
        
        vicID = notMgr.add(vicodin);
        adID = notMgr.add(advil);
        
        vicodin.setVibrate();
        
        System.out.println("vicID: " + vicID);
        System.out.println("adID: " + adID);
        
        notMgr.printAllNotifications();
        
        try {
			notMgr.sendMessage(vicID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println("vicID = " + vicID);
        
	    //this.startActivity(new Intent(this, DialogActivity.class));
	    //this.startActivity(new Intent(this, HelloWorld.class));
   
        //DialogActivity.showDialog(this);
        
<<<<<<< HEAD
        //this.startActivity(new Intent(this, AlarmView.class));
        
        StorageProvider sp = new StorageProvider(this.getApplicationContext());
        SQLiteDatabase db = sp.getDatabase();
        db.close();
=======
        this.startActivity(new Intent(this, AlarmView.class));
>>>>>>> branch 'refs/heads/master' of git+ssh://git@github.com/tobias2143/risotto.git

    }*/
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        StatusBarNotificationManager notMgr = new StatusBarNotificationManager(this.getApplicationContext());
        
        vicodin = new StatusBarNotification(this.getApplicationContext(), prep, "Vicodin", "Remember to take 2 pills of vicodin", "Be sure to eat something first.");
        advil = new StatusBarNotification(this.getApplicationContext(), prep, "Advil", "Remember to take 2 pills of vicodin", "Be sure to eat something first.");
        
        vicID = notMgr.add(vicodin);
        adID = notMgr.add(advil);
        
        vicodin.setVibrate();
        
        System.out.println("vicID: " + vicID);
        System.out.println("adID: " + adID);
        
        notMgr.printAllNotifications();
        
        try {
			notMgr.sendMessage(vicID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println("vicID = " + vicID);
        
        int[] ar = {3,4,5,6};
        Drug test = new Drug(20,ar,"Tylenol");
        
        ContentValues cv = test.toContentValues();
        
        String br = (String)cv.get(StorageProvider.DrugColumns.DRUG_NAME);
        
        System.out.println(br);
	    //this.startActivity(new Intent(this, DialogActivity.class));
	    //this.startActivity(new Intent(this, HelloWorld.class));
   
        //DialogActivity.showDialog(this);
        
        
        // START DATABASE TESTING
        Log.d("HELLO_TAG", "Attempting to store a drug...");
        
        Uri drugUri = getContentResolver().insert(StorageProvider.DrugColumns.CONTENT_URI, cv);
        
        Log.d("HELLO_TAG", "Row URI: " + drugUri.toString());
        
        Log.d("HELLO_TAG", "Attempting a full query...");
        Cursor c = this.managedQuery(com.risotto.storage.StorageProvider.DrugColumns.CONTENT_URI, null, null, null, null);
        
        
        if (c.moveToFirst()) {

            String name; 
            byte[] strength; 
            int nameColumn = c.getColumnIndex(StorageProvider.DrugColumns.DRUG_NAME); 
            int strengthColumn = c.getColumnIndex(StorageProvider.DrugColumns.DRUG_STRENGTH);
        
            do {
                // Get the field values
                name = c.getString(nameColumn);
                strength = c.getBlob(strengthColumn);
                
                Log.d("HELLO_TAG", "Name: " + name);
                Log.d("HELLO_TAG", "Strength: " + strength);

            } while (c.moveToNext());

        }
        
        Log.d("HELLO_TAG", "Attempting a partial query...");
        Cursor newCursor = this.managedQuery(Uri.withAppendedPath(com.risotto.storage.StorageProvider.DrugColumns.CONTENT_URI, "/2"), null, null, null, null);
        newCursor.moveToFirst();
        Log.d("HELLO_TAG", "Return: " + newCursor.getString(c.getColumnIndex(StorageProvider.DrugColumns.DRUG_NAME)));
        
        
        
        // END DATABASE TESTING
        
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, DrugView.class);
        spec = tabHost.newTabSpec("drug").setIndicator("Drugs",
            	res.getDrawable(R.drawable.micro_white))
            	.setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, AlarmView.class);
        spec = tabHost.newTabSpec("alarms").setIndicator("Alarms",
                          res.getDrawable(R.drawable.micro_white))
                      .setContent(intent);
        tabHost.addTab(spec);
       

        tabHost.setCurrentTab(1);
    }
    

	/*@Override
	protected void onPause() {
		super.onPause();
		StatusBarNotification pause = new StatusBarNotification(this.getApplicationContext());
        pause.setNotificationContent("pause", "pause", "pause");
        pause.sendMessage();
		
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		StatusBarNotification restart = new StatusBarNotification(this.getApplicationContext());
        restart.setNotificationContent("restart", "restart", "restart");
        restart.sendMessage();
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatusBarNotification resume = new StatusBarNotification(this.getApplicationContext());
        resume.setNotificationContent("resume", "resume", "resume");
        resume.sendMessage();
	}

	@Override
	protected void onStart() {
		super.onStart();
		StatusBarNotification start = new StatusBarNotification(this.getApplicationContext());
        start.setNotificationContent("start", "start", "start");
        start.sendMessage();
	}

	@Override
	protected void onStop() {
		super.onStop();
		StatusBarNotification stop = new StatusBarNotification(this.getApplicationContext());
        stop.setNotificationContent("stop", "stop", "stop");
        stop.sendMessage();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		StatusBarNotification destroy = new StatusBarNotification(this.getApplicationContext());
        destroy.setNotificationContent("destroy", "destroy", "destroy");
        destroy.sendMessage();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}*/
   
}
