package com.risotto.model;

import java.util.Date;

import android.graphics.Color;

public class Prescription {
	
	private int totalUnits;
	private int unitVolume;
	private int numRefills;
	private Date expiration;
	private String drName;
	private int prescripID;
	private Patient patient;
	private Color color;
	
	public enum SHAPE {
		ROUND,
		OBLONG,
		OVAL,
		SQUARE,
		RECTANGLE,
		DIAMOND,
		THREE_SIDED,
		FIVE_SIDED,
		SIX_SIDED,
		SEVEN_SIDED,
		EIGHT_SIDED,
		OTHER,
		NONE
	}
	
 	public enum FORM {
		CAPSULES,
		TABLETS,
		POWDERS,
		DROPS,
		LIQUIDS,
		SPRAY,
		SKIN,
		SUPPOSITORIES,
		NONE,
		OTHER;
		
	}

 	public enum SIZE {
 		SMALL,
 		MEDIUM,
 		LARGE,
 		NONE,
 		OTHER
 	}

	public Prescription() {
		
	}

}
