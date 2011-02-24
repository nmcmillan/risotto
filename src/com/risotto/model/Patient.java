package com.risotto.model;

public class Patient {
	
	private GENDER gender;
	private String firstName;
	private String lastName;
	private byte relations;
	
	public enum RELATION {
		MOTHER,
		FATHER,
		DAUGHTER,
		SON,
		GRANDFATHER,
		GRANDMOTHER,
		AUNT,
		UNCLE,
		COUSIN;
	}
	
	public enum GENDER {
		MALE,
		FEMALE,
		OTHER;
	}

}
