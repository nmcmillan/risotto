package com.risotto.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.risotto.controller.StatusBarNotification;
import com.risotto.controller.StatusBarNotificationManager;
import com.risotto.model.Drug;
import com.risotto.model.Patient;
import com.risotto.model.Prescription;

public class MainService extends Service {
	
	protected static final String LOG_TAG = "RISOTTO_SERVICE";
	private static boolean serviceStarted = false;
	private StatusBarNotificationManager sbnm = null;
	
	public static final String SERVICE_START = "com.risotto.service.SERVICE_START";
	public static final String ALARM_TRIGGER = "com.risotto.service.ALARM_TRIGGER";
	

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(LOG_TAG, "onCreate has been called...");
		super.onCreate();
		serviceStarted = true;
		sbnm = new StatusBarNotificationManager(this.getApplicationContext());
	}

	@Override
	public void onDestroy() {
		Log.d(LOG_TAG, "onDestroy has been called...");
		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
		Log.d(LOG_TAG, "onLowMemory has been called...");
		super.onLowMemory();
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		Log.d(LOG_TAG, "onStart has been called...");
		Log.d(LOG_TAG, "Intent Info: " + intent.getAction());
		Log.d(LOG_TAG, "Start ID: " + startId);
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		int returnValue;
//		Log.d(LOG_TAG, "onStartCommand has been called...");
//		Log.d(LOG_TAG, "Intent Info: " + intent.getAction());
//		Log.d(LOG_TAG, "Flags: " + flags);
//		Log.d(LOG_TAG, "Start ID: " + startId);
//		returnValue = super.onStartCommand(intent, flags, startId);
//		Log.d(LOG_TAG, "Returning: " + returnValue);
		
		handleCommand(intent);
		
		// If this is an alarm trigger
		if (intent.getAction().equals("com.risotto.service.ALARM_TRIGGER")) {
			Prescription prep = new Prescription(new Patient(),new Drug(10,10,"Name"),Prescription.DOSE_TYPE.EVERY_DAY,10,10);
			StatusBarNotification not = new StatusBarNotification(this, prep, "Blake is an ass.", "Always", "Side of jack.");
			int x = sbnm.add(not);
			try {
				sbnm.sendMessage(x);
			}
			catch(Exception e) {Log.d(LOG_TAG, "THERE WAS AN EXCEPTION!!");}
			Log.d(LOG_TAG, "ALARM UP!!!!!");
		}
		
		if (isAlarmScheduled()) {
			scheduleAlarm();
		}
		
		
		return Service.START_STICKY;
	}
	
	private void scheduleAlarm() {
		AlarmManager am = (AlarmManager)this.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
		
		Intent newIntent = new Intent("com.risotto.service.ALARM_TRIGGER");
		newIntent.setClass(this, MainService.class);
		
		
		
		
		
		PendingIntent pi = PendingIntent.getService(this, 0, newIntent, PendingIntent.FLAG_ONE_SHOT);
		
		am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pi );
		
		
	}
	
	private boolean isAlarmScheduled() {
		return true;
	}
	
	private int handleCommand(Intent intent) {
		int returnValue;
		Log.d(LOG_TAG, "handleCommand has been called...");
		Log.d(LOG_TAG, "Intent Info: " + intent.getAction());
		
		
		returnValue = 0;
		Log.d(LOG_TAG, "Returning: " + returnValue);
		return returnValue;
	}

}
