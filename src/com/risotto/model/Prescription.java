package com.risotto.model;

import java.util.Date;

public class Prescription {
	
	// Required fields
	private Doseage dose;
	private Patient patient;
	private Drug drug;
	private DOSE_TYPE doseType;
	private int doseSize;
	private int totalUnits;
	
	// Optional Fields
	private Date filled;
	private String drName;
	private int prescripID;
	private int cost;
	private int numDaysSupplied;
	private int numRefills;
	private Date expiration;

	// Possible doseage types.
	public enum DOSE_TYPE {
		EVERY_DAY_OF_WEEK,
		EVERY_HOUR,
		EVERY_HOUR_DAY_OF_WEEK,
		EVERY_DAY
	}
	
	public Prescription() {
		
	}

}
