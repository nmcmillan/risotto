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
	
	// A log tag used for debugging.
	public static final String LOG_TAG = "RISOTTO_SERVICE";
	
	// The intent used to start this service.
	public static final String ACTION_START_SERVICE = "com.risotto.service.START_SERVICE";
	// The intent used to schedule an alarm.
	public static final String ACTION_ALARM_TRIGGER = "com.risotto.service.ALARM_TRIGGER";
	// The intent used to schedule an alarm.
	public static final String ACTION_ALARM_SCHEDULE = "com.risotto.service.ALARM_SCHEDULE";
	
	private static boolean alarmScheduled = false;
	

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(LOG_TAG, "onCreate has been called...");
		super.onCreate();
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
	public int onStartCommand(Intent intent, int flags, int startId) {
		// If this is NOT the BOOT_COMPLETE or USER_PRESENT action...
		if ( !(intent.getAction().equals(ACTION_START_SERVICE)) ) {
			// ...send the intent to the handler.
			handleCommand(intent);
		}	
		return Service.START_STICKY;
	}
	
	
	private void scheduleAlarm() {
		
		// If we have not scheduled the alarm yet...
		if ( ! this.isAlarmScheduled() ) {
			// Get an instance of the AlarmManager
			AlarmManager am = (AlarmManager)this.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
			
			// Create an intent that will launch when the trigger is done.
			Intent newIntent = new Intent(ACTION_ALARM_TRIGGER);
			
			newIntent.putExtra("TOP_TEXT", "Blake is an ass.");
			newIntent.putExtra("TITLE_TEXT", "Always");
			newIntent.putExtra("BODY_TEXT", "Side of jack.");
			
			newIntent.setClass(this, MainService.class);
			
			// Create a pending intent which will be sent when the alarm is done.
			PendingIntent pi = PendingIntent.getService(this, 0, newIntent, PendingIntent.FLAG_ONE_SHOT);
			
			// Have the AlarmManager schedule the alarm.
			am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pi );
			
			// Set the alarm flag to true, as we have scheduled it
			alarmScheduled = true;
			
		} else {
			Log.d(LOG_TAG, "No need to schedule an alarm, already done once...");
		}
		
	}
	
	private void notifyAlarmDone(Intent intent) {
		// Get an instance of the SBNM
		StatusBarNotificationManager sbnm = new StatusBarNotificationManager(this.getApplicationContext());
		
		// Create a new prescription.
		Prescription prep = new Prescription(new Patient(),new Drug(10,null,"Name"),Prescription.DOSE_TYPE.EVERY_DAY,10,10);
		
		// Create the new status bar notification.
		//StatusBarNotification not = new StatusBarNotification(this, prep, "Blake is an ass.", "Always", "Side of jack.");
		StatusBarNotification not = new StatusBarNotification(this, prep, intent.getStringExtra("TOP_TEXT"), intent.getStringExtra("TITLE_TEXT"), intent.getStringExtra("BODY_TEXT"));
		
		// Get the index of this notification
		int x = sbnm.add(not);
		try {
			// Send the message to the NM to display
			sbnm.sendMessage(x);
		}
		catch(Exception e) {
			Log.d(LOG_TAG, "THERE WAS AN EXCEPTION!!");
		}
		
		Log.d(LOG_TAG, "ALARM UP!!!!!");
	}
	
	private boolean isAlarmScheduled() {
		return alarmScheduled;
	}
	
	/**
	 * Handles the given intent by performing a class specific action.
	 * @param intent the intent to handle
	 */
	private void handleCommand(Intent intent) {
		Log.d(LOG_TAG, "handleCommand has been called...");
		Log.d(LOG_TAG, "Intent Info: " + intent.getAction());
		
		// Get the string action from the intent object.
		String intentAction = intent.getAction();
		
		if ( intentAction.equals(ACTION_ALARM_TRIGGER) ) {
			// An alarm has completed!
			notifyAlarmDone(intent);
		} else if ( intentAction.equals(ACTION_ALARM_SCHEDULE) ) {
			// Schedule the alarm...
			this.scheduleAlarm();
		} else {
			// How did we get this action? What is this!?
			Log.d(LOG_TAG, "handleCommand() - Unknown intent: " + intentAction);
		}	
		
	}

}
