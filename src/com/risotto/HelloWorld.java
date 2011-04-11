package com.risotto;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import com.risotto.storage.StorageTester;
import com.risotto.view.DrugView;

public class HelloWorld extends TabActivity {
	
	/*
	 * When the device is rotated, Android runs through the full lifecycle of your application.  It calls pause, stop, destroy
	 * and then create, start, resume every time the phone's orientation is changed.
	 */
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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
        /*intent = new Intent().setClass(this, AlarmView.class);
        spec = tabHost.newTabSpec("alarms").setIndicator("Alarms",
                          res.getDrawable(R.drawable.micro_white))
                      .setContent(intent);
        tabHost.addTab(spec);*/
       

        tabHost.setCurrentTab(0);
    }
}
