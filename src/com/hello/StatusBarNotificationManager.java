package com.hello;

import java.util.Hashtable;

import android.content.Context;

public class StatusBarNotificationManager {
	
	private Hashtable notifications;
	private static Context context;
	
	StatusBarNotificationManager(Context ctx) {
		context = ctx;
		//notifications = new Hashtable<StatusBarNotification>();
	}
	
	public void addNotification(StatusBarNotification stbn)
	{
		//this.notifications.put(key, value)
	}

}
