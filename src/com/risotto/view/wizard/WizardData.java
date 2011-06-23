package com.risotto.view.wizard;

import java.util.HashMap;
import java.util.Hashtable;

import android.os.Bundle;
import android.util.Log;

public class WizardData {

	
	public static final String LOG_TAG = "com.risotto.view.wizard.WizardData";
	//class for holding general wizard data, strings, etc. needed by all classes in wizard
	/*
	 * 6/22: considered the idea of not adding anything to intents and just storing the data
	 * in this static class, but that seems to violate the principles behind intents...
	 * 
	 * 
	 * Design outline:
	 * 	- classes in the wizard will need to pass along drug, patient, & prescription objects
	 * as the user moves through the wizard; to facilitate this, classes will put them into a 
	 * hashtable and add the hashtable to the intent; this will make it easier for packing & unpacking intents
	 * and for checking if a certain piece of data exists
	 * 
	 */
	//Code for copying
	/*
	try {
		  this.wizardData = WizardData.getData(getIntent().getExtras());
	  } catch (Exception e) {
		  Log.d(LOG_TAG,"No data found in intent.");
	  }
	  */
	
	public static final String PATIENT = "com.risotto.view.wizard.wizard_patient_object";
	public static final String PRESCRIPTION = "com.risotto.view.wizard.wizard_prescription_object";
	public static final String DRUG = "com.risotto.view.wizard.wizard_drug_object";
	public static final String CREATE_NEW_DRUG = "com.risotto.view.wizard.create_new_drug";
	public static final String CONTENTS = "com.risotto.view.wizard.hashtable";
	
	/**
	 * Helper method for pulling wizard data out of bundle.
	 * 
	 * @param extras Bundle object from intent 
	 * @return the hashtable from the intent
	 * @throws Exception if the hashtable wasn't found
	 */
	public static HashMap<String,Object> getData(Bundle extras) throws Exception {
		if(extras.containsKey(WizardData.CONTENTS)) {
			 return (HashMap<String,Object>)extras.getSerializable((WizardData.CONTENTS));
		}
		else {
			Log.d(LOG_TAG,"containsKey returned false");
			Log.d(LOG_TAG,"extra empty: " + extras.isEmpty());
			Log.d(LOG_TAG,"extra contents: " + extras.size());
			throw new Exception("No data found.");
		}
	}
	
}
