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
	private int NOTIFICATION_ID = 0;
	
	public StatusBarNotification(Context ctx) {
		context = ctx;
	}
	
	public void notify(String statusBarText,String title, String messageText){
		final int INTENT = 1;
        String ns = Context.NOTIFICATION_SERVICE;
        
        NotificationManager nm = (NotificationManager) context.getSystemService(ns);
        
        int n = R.drawable.icon;
        long time = 400000000;
        
        Notification myNot = new Notification(n,statusBarText,time);
        
        Intent notificationIntent = new Intent(context, HelloWorld.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        
        myNot.setLatestEventInfo(context, title, messageText, contentIntent);

        nm.notify(INTENT,myNot);
	}
	
	

}
