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

		// If we have received the "USER_PRESENT" intent...
		if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
			Log.d(MainService.LOG_TAG, "Sending the START intent to the service...");
			Intent newIntent = new Intent(MainService.SERVICE_START);
			newIntent.setClass(context, MainService.class);
			context.startService(newIntent);
		}
		// Else, we have received the "BOOT_COMPLETE" intent
		else {
			// Pass the intent to our service to handle
			context.startService(new Intent(context, MainService.class));
		}
	}

}
