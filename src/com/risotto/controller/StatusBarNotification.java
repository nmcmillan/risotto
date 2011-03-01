package com.risotto.controller;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.hello.R;
import com.risotto.model.Prescription;

public class StatusBarNotification {

	/*
	 * When defining a constructor for this class, the Context object will need
	 * to be passed in. This is because this class is not a declared activity in
	 * the manifest. The context object passed in, will be the same one that is
	 * used throughout the application
	 */
	private static Context context;
	private int icon;
	private String statusBarText, messageTitle, messageText;
	private Notification myNot = null;
	private Intent notificationIntent;
	private PendingIntent contentIntent;
	private Prescription prescription;
	
	public enum Content { 
		STATUS_BAR,
		MSG_TITLE,
		MSG_TEXT;
	}
	
	public StatusBarNotification(Context ctx, Prescription p) {
		context = ctx;
		icon = R.drawable.icon;
		myNot = new Notification();
		notificationIntent = new Intent(ctx, StatusBarNotification.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		myNot.setLatestEventInfo(ctx, "", "", contentIntent);
		this.prescription = p;
	}

	public void setVibrate() {
		myNot.defaults |= Notification.DEFAULT_VIBRATE;
	}
	
	public Notification getNotification() {
		return this.myNot;
	}
	
	public void setVibrate(long[] settings) {
		myNot.vibrate = settings;
	}
	
	public void setNotificationContent(String stBrTxt, String msgTitle, String msgText) {
		myNot.tickerText = stBrTxt;
		myNot.setLatestEventInfo(context, msgTitle, msgText, contentIntent);
	}
	
	public void changeNotification(Content c, String s) {
		switch(c) {
			case STATUS_BAR: this.statusBarText = s;
			case MSG_TITLE:  this.messageTitle = s;
			case MSG_TEXT:   this.messageText = s;
		}
	}
}