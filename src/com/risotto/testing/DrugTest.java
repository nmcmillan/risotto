package com.risotto.testing;

import java.util.Arrays;
import java.util.Vector;

import android.content.ContentValues;
import android.util.Log;

import com.risotto.model.Drug;

public class DrugTest {
	
	private static final String LOG_TAG = "DrugTest";
	
	public static void testToContentValues() {
		
		//String[] strength = {"50","100","150","200"};
		Vector<String> strengths = new Vector<String>();
		strengths.add("50");
		strengths.add("100");
		strengths.add("150");
		strengths.add("200");		
		Drug drug = new Drug("Tylenol");
		//ContentValues cv = drug.toContentValues();
		//Drug newDrug = Drug.fromContentValues(cv);
		
		//Log.d(LOG_TAG,"oldDrug : " + Arrays.toString(drug.getStrength().toArray(new String[1])));
		//Log.d(LOG_TAG,"newDrug : " + Arrays.toString(newDrug.getStrength()));
		
		
	}
}
