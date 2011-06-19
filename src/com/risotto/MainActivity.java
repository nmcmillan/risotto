package com.risotto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.risotto.storage.StorageTester;
import com.risotto.view.drug.DrugView;
import com.risotto.view.patient.PatientView;
import com.risotto.view.prescription.PrescriptionView;
import com.risotto.view.wizard.WhenTakeIt;
import com.risotto.view.wizard.WhoWillBeTaking;

public class MainActivity extends Activity implements OnClickListener {
	
	public static final String ACTION_LAUNCH_FROM_HOME_PATIENTS = "com.risotto.view.patient.PatientView";
	public static final String ACTION_LAUNCH_FROM_HOME_SCHEDULE = "com.risotto.view.wizard.WhenTakeIt";
	public static final String ACTION_LAUNCH_FROM_HOME_DRUGS = "com.risotto.view.drug.DrugView";
	//public static final String ACTION_LAUNCH_FROM_HOME_HISTORY = "com.risotto.view.patient.PatientView";
	public static final String ACTION_LAUNCH_FROM_HOME_PRESCRIPTION = "com.risotto.view.prescription.PrescriptionView";
	
	public static final String LOG_TAG = "com.risotto.MainActivity";
	
	public static final int MENU_ITEM_ABOUT_POSITION = Menu.FIRST;
	public static final int MENU_ITEM_SETTINGS_POSITION = Menu.FIRST + 1;
	
	/*
	 * When the device is rotated, Android runs through the full lifecycle of your application.  It calls pause, stop, destroy
	 * and then create, start, resume every time the phone's orientation is changed.
	 */
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // START DATABASE TESTING      
        //StorageTester.runTest(this.getApplicationContext());
        // END DATABASE TESTING
        
        //START DRUG TESTING
        //DrugTest.testToContentValues();
        //END DRUG TESTING
        
        Button patients = (Button) findViewById(R.id.button_main_patients);
        patients.setOnClickListener(this);
        
        Button drugs = (Button) findViewById(R.id.button_main_drugs);
        drugs.setOnClickListener(this);
        
        Button schedule = (Button) findViewById(R.id.button_main_schedule);
        schedule.setOnClickListener(this);
        
        Button history = (Button) findViewById(R.id.button_main_history);
        history.setOnClickListener(this);
        
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
		/*
		 * MenuInflater inflater = getMenuInflater();
		 * inflater.inflate(R.layout.drug_menu_layout, menu); return true;
		 */
		menu.add(Menu.FIRST, // group id for doing batch changes
				MENU_ITEM_ABOUT_POSITION, // position
				Menu.NONE, // order, see getOrder()
				R.string.home_menu_about) // name of button - link to XML
				.setIcon(android.R.drawable.ic_menu_info_details);
		menu.add(Menu.FIRST, // group id for doing batch changes
				MENU_ITEM_SETTINGS_POSITION, // position
				Menu.NONE, // order, see getOrder()
				R.string.home_menu_settings) // name of button - link to XML
				.setIcon(android.R.drawable.ic_menu_preferences);
		return true;
    }
    
    /*
     * (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
	public void onClick(View v) {
		Intent intent = new Intent();
		switch(v.getId()) {
			case R.id.button_main_drugs:
				Log.d(LOG_TAG,"launching drug view");
				intent.setAction(MainActivity.ACTION_LAUNCH_FROM_HOME_DRUGS);
		    	intent.setClass(getApplicationContext(), DrugView.class);
				startActivity(intent);
				break;
			case R.id.button_main_history:
				//will launch prescription view for now
				Log.d(LOG_TAG,"launching prescription view");
				intent.setAction(MainActivity.ACTION_LAUNCH_FROM_HOME_PRESCRIPTION);
				intent.setClass(getApplicationContext(), PrescriptionView.class);
				startActivity(intent);
				break;
			case R.id.button_main_patients:
				Log.d(LOG_TAG,"launching patient view");
				intent.setAction(MainActivity.ACTION_LAUNCH_FROM_HOME_PATIENTS);
				intent.setClass(getApplicationContext(), PatientView.class);
				startActivity(intent);
				break;
			case R.id.button_main_schedule:
				Log.d(LOG_TAG,"launching wizard");
				intent.setAction(MainActivity.ACTION_LAUNCH_FROM_HOME_SCHEDULE);
				intent.setClass(getApplicationContext(), WhoWillBeTaking.class);
				startActivity(intent);
				break;
			default:
				break;
		}
	}
}
