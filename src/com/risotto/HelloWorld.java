package com.risotto;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import com.risotto.controller.StatusBarNotification;
import com.risotto.model.Drug;
import com.risotto.model.Patient;
import com.risotto.model.Prescription;
import com.risotto.storage.StorageTester;
import com.risotto.view.AlarmView;
import com.risotto.view.DrugView;

public class HelloWorld extends TabActivity {
	
	/*
	 * When the device is rotated, Android runs through the full lifecycle of your application.  It calls pause, stop, destroy
	 * and then create, start, resume every time the phone's orientation is changed.
	 */
//	StatusBarNotification vicodin = null;
//	StatusBarNotification advil = null;
//    int vicID = -1;
//    int adID = -1;
//    Patient patient = new Patient("Bill", "Clinton", Patient.GENDER_OTHER);
//    Drug drug = new Drug();
//    Prescription prep = new Prescription(patient, drug, Prescription.DOSE_TYPE_EVERY_DAY, 2, 20);
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
//        StatusBarNotificationManager notMgr = new StatusBarNotificationManager(this.getApplicationContext());
//        
//        vicodin = new StatusBarNotification(this.getApplicationContext(), prep, "Vicodin", "Remember to take 2 pills of vicodin", "Be sure to eat something first.");
//        advil = new StatusBarNotification(this.getApplicationContext(), prep, "Advil", "Remember to take 2 pills of vicodin", "Be sure to eat something first.");
//        
//        vicID = notMgr.add(vicodin);
//        adID = notMgr.add(advil);
//        
//        vicodin.setVibrate();
//        
//        System.out.println("vicID: " + vicID);
//        System.out.println("adID: " + adID);
//        
//        notMgr.printAllNotifications();
//        
//        try {
//			notMgr.sendMessage(vicID);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        
//        System.out.println("vicID = " + vicID);
//        
//        Vector<String> strengths = new Vector<String>();
//        strengths.add("100");
//        strengths.add("200");
//        strengths.add("300");
//       
//        Drug test = new Drug(20,strengths,"Tylenol");
//        
//        ContentValues cv = test.toContentValues();
//        
//        String br = (String)cv.get(StorageProvider.DrugColumns.DRUG_NAME);
//        
//        System.out.println("BR:" + br);

        // START DATABASE TESTING      
        StorageTester.runTest(this.getApplicationContext());
        // END DATABASE TESTING
        
        //START DRUG TESTING
        //DrugTest.testToContentValues();
        //END DRUG TESTING
        
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabSpec spec;  // Reusable TabSpec for each tab
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
       

        tabHost.setCurrentTab(0);
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
