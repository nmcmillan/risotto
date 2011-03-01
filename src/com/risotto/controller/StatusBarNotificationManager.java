package com.risotto.controller;

//import java.util.Hashtable;

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

	public StatusBarNotificationManager(Context ctx) {
		context = ctx;
		notifications = new Hashtable<Integer, StatusBarNotification>();
	}

	public int add(StatusBarNotification stbn) {
		this.notifications.put(NOTIFICATION_ID, stbn);
		NOTIFICATION_ID++;
		return NOTIFICATION_ID;
	}

	public int cancel(int id) {
		if (this.notifications.containsKey(id)) {
			this.notifications.remove(id);
			return 0;
		} else
			return 1;

	}

	public int sendMessage(int id) {
		if (this.notifications.containsKey(id)) {
			
			myNot = this.notifications.get(id);
			NotificationManager nm = (NotificationManager) context.getSystemService(ns);

			time = System.currentTimeMillis();

			nm.notify(id, myNot.getNotification());
		}
		return 0;

	}

}
