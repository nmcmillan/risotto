package com.hello;

import android.app.Activity;
import android.os.Bundle;

public class HelloWorld extends Activity {
	
	/*
	 * When the device is rotated, Android runs through the full lifecycle of your application.  It calls pause, stop, destroy
	 * and then create, start, resume every time the phone's orientation is changed.
	 */
	StatusBarNotification vicodin = null; 
    StatusBarNotification asparin = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
     
        // Show the new DialogActivity
        //this.startActivity(new Intent(this, DialogActivity.class));
        if(null == vicodin)
        {
	        vicodin = new StatusBarNotification(this.getApplicationContext());
	        asparin = new StatusBarNotification(this.getApplicationContext());
	        vicodin.setNotificationContent("Medicine", "Remember to take 2 pills of vicodin", "Be sure to eat something first.");
	        //asparin.sendMessage();
	        //vicodin.sendMessage();
        }
        DialogActivity.showDialog(this);

    }

	/*@Override
	protected void onPause() {
		super.onPause();
		StatusBarNotification pause = new StatusBarNotification(this.getApplicationContext());
        pause.setNotificationContent("pause", "pause", "pause");
        pause.sendMessage();
		
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		StatusBarNotification restart = new StatusBarNotification(this.getApplicationContext());
        restart.setNotificationContent("restart", "restart", "restart");
        restart.sendMessage();
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatusBarNotification resume = new StatusBarNotification(this.getApplicationContext());
        resume.setNotificationContent("resume", "resume", "resume");
        resume.sendMessage();
	}

	@Override
	protected void onStart() {
		super.onStart();
		StatusBarNotification start = new StatusBarNotification(this.getApplicationContext());
        start.setNotificationContent("start", "start", "start");
        start.sendMessage();
	}

	@Override
	protected void onStop() {
		super.onStop();
		StatusBarNotification stop = new StatusBarNotification(this.getApplicationContext());
        stop.setNotificationContent("stop", "stop", "stop");
        stop.sendMessage();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		StatusBarNotification destroy = new StatusBarNotification(this.getApplicationContext());
        destroy.setNotificationContent("destroy", "destroy", "destroy");
        destroy.sendMessage();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}*/
   
}