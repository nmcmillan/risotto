package com.risotto;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.risotto.storage.StorageTester;
import com.risotto.view.drug.DrugView;
import com.risotto.view.patient.PatientView;
import com.risotto.view.prescription.PrescriptionView;

public class MainActivity extends TabActivity {
	
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

        // Create tab interface
        
        // Position 0 = Prescription tab
        /*intent = new Intent().setClass(this, PrescriptionView.class);
        spec = tabHost.newTabSpec("meds")
        				.setIndicator("Prescriptions", res.getDrawable(R.drawable.micro_white))
        				.setContent(intent);
        tabHost.addTab(spec); */
        
        
        // Position 1 = People tab
        intent = new Intent().setClass(this, PatientView.class);
        spec = tabHost.newTabSpec("people")
        				.setIndicator("People", res.getDrawable(R.drawable.micro_white))
        				.setContent(intent);
        tabHost.addTab(spec);
        
        // Position 2 = Schedules tab
        intent = new Intent().setClass(this, DrugView.class);
        spec = tabHost.newTabSpec("drugs")
        				.setIndicator("drugs", res.getDrawable(R.drawable.micro_white))
        				.setContent(intent);
        tabHost.addTab(spec);
        
        // Position 3 = Prescription Tab
        intent = new Intent().setClass(this, PrescriptionView.class);
        spec = tabHost.newTabSpec("prescriptions")
        				.setIndicator("Prescriptions", res.getDrawable(R.drawable.micro_gray))
        				.setContent(intent);
        tabHost.addTab(spec);        				
        
        tabHost.setCurrentTab(0);
    }
}
