package com.risotto.controller;

//import java.util.Hashtable;

import java.util.Enumeration;
import java.util.Hashtable;

import android.app.NotificationManager;
import android.content.Context;

public class StatusBarNotificationManager {

	private Hashtable<Integer, StatusBarNotification> notifications;
	private static Context context;
	private static int NOTIFICATION_ID = 0;
	private String statusBarText, messageTitle, messageText;
	private int not_id;
	private String ns = Context.NOTIFICATION_SERVICE;
	private long time;
	private int icon;
	private StatusBarNotification myNot;

	/**
	 * Constructor - saves a local copy of the context (TO DO: is that correct?) & initializes the hashtable
	 * 
	 * @param ctx - the application's current context
	 */
	public StatusBarNotificationManager(Context ctx) {
		context = ctx;
		notifications = new Hashtable<Integer, StatusBarNotification>();
	}

	/**
	 * Adds a notification to the manager (TO DO: should we generalize this class in the case that we have other types of notifications?)
	 * 
	 * @param stbn - the status bar notification to add to the manager
	 * @return the id for a given notification
	 */
	public int add(StatusBarNotification stbn) {
		this.notifications.put(++NOTIFICATION_ID, stbn);
		return NOTIFICATION_ID;
	}

	/**
	 * Removes notification from the manager
	 * 
	 * @param id - number which identifies which element to remove
	 * @return true if the element was in the manager's store and was removed, false if the element was not in the manager's store
	 */
	public boolean cancel(int id) {
		if (this.notifications.containsKey(id)) {
			this.notifications.remove(id);
			return true;
		}
		return false;

	}
	
	/**
	 * Returns the status bar texts for all the notifications created thus far 
	 * @return vector containing all the strings
	 */
	public Enumeration<StatusBarNotification> getAllNotifications() {
		return this.notifications.elements();
	}
	
	/**
	 * Prints all notifications contained in the local hash table to the system log.
	 */
	public void printAllNotifications() {
		Enumeration<StatusBarNotification> en = this.getAllNotifications();
		while(en.hasMoreElements()) {
			System.out.println(en.nextElement().getStatusBarText());
		}
	}
	

	/**
	 * 
	 * @param id - number of the notification to be enabled
	 * @throws Exception - (TO DO) place holder, needs to throw exception if the id isn't in the store; the application needs to
	 * 					   handle it if the situation arises because that means a notification was attempted to be enabled and doesn't
	 * 					   exist, which could be cause for concern
	 * 
	 */
	public void sendMessage(int id) throws Exception {		
		if (this.notifications.containsKey(id)) {
			System.out.println("Inside Stat Bar Not Mgr, id = " + id);
			myNot = this.notifications.get(id);
			NotificationManager nm = (NotificationManager) context.getSystemService(ns);

			nm.notify(id, myNot.getNotification());
		}
		else
			throw new Exception();
	}

}
