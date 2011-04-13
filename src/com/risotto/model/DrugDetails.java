package com.risotto.model;

import java.io.Serializable;

public class DrugDetails implements Serializable {

	// Required Fields
	private int type;
	private int strength;
	private String strengthLabel;
	private int drugId = INVALID_ID;

	// Optional Fields
	private String nickName;
	private FORM form = FORM.NONE;
	private int color = -1;
	private SHAPE shape = SHAPE.NONE;
	private SIZE size = SIZE.NONE;

	// Unique id used for storage references
	private int _id;
	private static final int INVALID_ID = -1;

	// Over-the-counter drug.
	public static final int TYPE_OTC = 0;
	// Prescription drug.
	public static final int TYPE_PRE = 1;

	// Possible solid shapes.
	public enum SHAPE {
		ROUND, OBLONG, OVAL, SQUARE, RECTANGLE, DIAMOND, THREE_SIDED, FIVE_SIDED, SIX_SIDED, SEVEN_SIDED, EIGHT_SIDED, OTHER, NONE
	}

	// The many forms which the drug can be.
	public enum FORM {
		CAPSULES, TABLETS, POWDERS, DROPS, LIQUIDS, SPRAY, SKIN, SUPPOSITORIES, NONE, OTHER
	}

	// Possible sizes of solid drugs.
	public enum SIZE {
		SMALL, MEDIUM, LARGE, NONE, OTHER
	}

	public DrugDetails(int type, int strength, String strengthLabel) {
		this(INVALID_ID, type, strength, strengthLabel);
	}

	private DrugDetails(int _id, int type, int strength, String strengthLabel) {
		this._id = _id;
		this.type = type;
		this.strength = strength;
		this.strengthLabel = strengthLabel;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public String getStrengthLabel() {
		return strengthLabel;
	}

	public void setStrengthLabel(String strengthLabel) {
		this.strengthLabel = strengthLabel;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public FORM getForm() {
		return form;
	}

	public void setForm(FORM form) {
		this.form = form;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public SHAPE getShape() {
		return shape;
	}

	public void setShape(SHAPE shape) {
		this.shape = shape;
	}

	public SIZE getSize() {
		return size;
	}

	public void setSize(SIZE size) {
		this.size = size;
	}

	public int get_id() {
		return _id;
	}

	public int getDrugId() {
		return drugId;
	}

	public void setDrugId(int drugId) {
		this.drugId = drugId;
	}

	/*
	 * If we want more control over how this object is serialized we can
	 * implement the functions below.
	 */

/*	 private void writeObject(java.io.ObjectOutputStream out) throws
	 IOException {
	
	 }
	
	 private void readObject(java.io.ObjectInputStream in) throws IOException,
	 ClassNotFoundException {
	
	 }*/

}
