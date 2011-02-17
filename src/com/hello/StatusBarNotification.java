package com.hello;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class StatusBarNotification {

	/*
	 * When defining a constructor for this class, the Context object will need
	 * to be passed in. This is because this class is not a declared activity in
	 * the manifest. The context object passed in, will be the same one that is
	 * used throughout the application
	 */
	
	private static Context context;
	private static int NOTIFICATION_ID = 0;
	private String statusBarText, messageTitle, messageText;
	private int not_id;
	private String ns = Context.NOTIFICATION_SERVICE;
	private long time;
	private int icon;
	
	public StatusBarNotification(Context ctx) {
		context = ctx;
		not_id = NOTIFICATION_ID;
		icon = R.drawable.icon;
		NOTIFICATION_ID++;
	}
	
	public void setNotificationContent(String stBrTxt, String msgTitle, String msgText)
	{
		statusBarText = stBrTxt;
		messageTitle = msgTitle;
		messageText = msgText;
	}
	
	public void sendMessage() {
        
        NotificationManager nm = (NotificationManager) context.getSystemService(ns);

        time = System.currentTimeMillis();
        
        Notification myNot = new Notification(icon,statusBarText,time);
        
        myNot.defaults |= Notification.DEFAULT_VIBRATE;
        
        Intent notificationIntent = new Intent(context, HelloWorld.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        
        myNot.setLatestEventInfo(context, messageTitle, messageText, contentIntent);

        nm.notify(not_id, myNot);
	}
	
	

}
