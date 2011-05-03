package com.risotto.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MainServiceManager extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(MainService.LOG_TAG, "onReceive called...");
		Log.d(MainService.LOG_TAG, "Intent info: " + intent.toString());

		// If we have received the "USER_PRESENT" intent (screen unlocked)...
		if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
			Log.d(MainService.LOG_TAG, "Sending the ALARM SCHEDULE intent to the service...");
			this.sendIntentToService(context, MainService.ACTION_ALARM_SCHEDULE);
		
		// Is this intent for the a time/time zone change or update?
		} else if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED) || intent.getAction().equals(Intent.ACTION_TIME_CHANGED)) {
			Log.d(MainService.LOG_TAG, "The time or time zone has been changed/updated, sending a reschedule...");
			this.sendIntentToService(context, MainService.ACTION_ALARM_RESCHEDULE);
		}
		
		// Else, we have received the "BOOT_COMPLETE" intent
		else {
			Log.d(MainService.LOG_TAG, "Received the BOOT_COMPLETE intent, starting the service...");
			this.sendIntentToService(context, MainService.ACTION_START_SERVICE);
		}
	}
	
	private void sendIntentToService(Context context, String intent) {
		// Create the new intent with the schedule string
		Intent newIntent = new Intent(intent);
		// Set the directed class as the main service.
		newIntent.setClass(context, MainService.class);
		// Start the service with the above intent
		context.startService(newIntent);
	}

}
