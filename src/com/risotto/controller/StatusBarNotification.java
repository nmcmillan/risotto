package com.risotto.controller;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.risotto.R;
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
	private Intent intent;
	private PendingIntent contentIntent;
	private Prescription prescription;
	
	public enum Content { 
		STATUS_BAR,
		MSG_TITLE,
		MSG_TEXT;
	}
	
	public StatusBarNotification(Context ctx, Prescription p, String stBrTxt, String msgTitle, String msgText) {
		long time = System.currentTimeMillis();
		icon = R.drawable.icon;
		context = ctx;
		intent = new Intent(ctx, StatusBarNotification.class);
		contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
		this.statusBarText = stBrTxt;
		this.messageTitle = msgTitle;
		this.messageText = msgText;
		myNot = new Notification(icon,"",time);
		myNot.tickerText = stBrTxt;
		myNot.setLatestEventInfo(context, msgTitle, msgText, contentIntent);
		
		
		this.prescription = p;
	}
	
	public void setVibrate() {
		myNot.defaults |= Notification.DEFAULT_VIBRATE;
	}
	
	public void setLight() {
		myNot.defaults |= Notification.DEFAULT_LIGHTS;
	}
	
	public Notification getNotification() {
		return this.myNot;
	}
	
	public void setVibrate(long[] settings) {
		myNot.vibrate = settings;
	}
	
	/**
	 * Set the content of the notification
	 * @param stBrTxt - the text which will appear in the status bar of the phone
	 * @param msgTitle - the text which will be the heading in the notification tray
	 * @param msgText - the text which will be in the body of the notification
	 */
	public void setNotificationContent(String stBrTxt, String msgTitle, String msgText) {
		myNot.tickerText = stBrTxt;
		myNot.setLatestEventInfo(context, msgTitle, msgText, contentIntent);
	}
	
	/**
	 * Method to change the textual content of a notification using the enum defined in the class
	 * 
	 * @param content - which element is being updated, should be enum value
	 * @param string - the new text
	 */
	public void changeNotification(Content content, String string) {
		switch(content) {
			case STATUS_BAR: this.statusBarText = string;
			case MSG_TITLE:  this.messageTitle = string;
			case MSG_TEXT:   this.messageText = string;
		}
	}

	/**
	 * @return the statusBarText
	 */
	public String getStatusBarText() {
		return statusBarText;
	}

	/**
	 * @return the messageTitle
	 */
	public String getMessageTitle() {
		return messageTitle;
	}

	/**
	 * @return the messageText
	 */
	public String getMessageText() {
		return messageText;
	}
}
