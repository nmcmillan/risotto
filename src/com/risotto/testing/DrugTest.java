package com.risotto.testing;

import java.util.Arrays;

import android.content.ContentValues;
import android.util.Log;

import com.risotto.model.Drug;

public class DrugTest {
	
	private static final String LOG_TAG = "DrugTest";
	
	public static void testToContentValues() {
		
		String[] strength = {"50","100","150","200"};
		Drug drug = new Drug(100,strength,"Tylenol");
		ContentValues cv = drug.toContentValues();
		//Drug newDrug = Drug.fromContentValues(cv);
		
		Log.d(LOG_TAG,"oldDrug : " + Arrays.toString(drug.getStrength()));
		//Log.d(LOG_TAG,"newDrug : " + Arrays.toString(newDrug.getStrength()));
		
		
	}
}
