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
			// Create the new intent with the schedule string
			Intent newIntent = new Intent(MainService.ACTION_ALARM_SCHEDULE);
			// Set the directed class as the main service.
			newIntent.setClass(context, MainService.class);
			// Start the service with the above intent
			context.startService(newIntent);
		}
		// Else, we have received the "BOOT_COMPLETE" intent
		else {
			// Create the new intent with the schedule string
			Intent newIntent = new Intent(MainService.ACTION_START_SERVICE);
			// Set the directed class as the main service.
			newIntent.setClass(context, MainService.class);
			// Start the service with the above intent
			context.startService(newIntent);
		}
	}

}
