package com.hello;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.risotto.controller.StatusBarNotification;
import com.risotto.controller.StatusBarNotificationManager;
import com.risotto.model.Dosage;
import com.risotto.model.Drug;
import com.risotto.model.Patient;
import com.risotto.model.Prescription;

public class HelloWorld extends Activity {
	
	/*
	 * When the device is rotated, Android runs through the full lifecycle of your application.  It calls pause, stop, destroy
	 * and then create, start, resume every time the phone's orientation is changed.
	 */
	StatusBarNotification vicodin = null; 
    int vicID = -1;
    Patient patient = new Patient();
    Drug drug = new Drug();
    Prescription prep = new Prescription(patient, drug, Prescription.DOSE_TYPE.EVERY_DAY, 2, 20);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        StatusBarNotificationManager notMgr = new StatusBarNotificationManager(this.getApplicationContext());
        
        vicodin = new StatusBarNotification(this.getApplicationContext(), prep);
        vicodin.setNotificationContent("Medicine", "Remember to take 2 pills of vicodin", "Be sure to eat something first.");
        vicID = notMgr.add(vicodin);
        
        notMgr.sendMessage(vicID);
        
	    //this.startActivity(new Intent(this, DialogActivity.class));
	    //this.startActivity(new Intent(this, HelloWorld.class));
   
        //DialogActivity.showDialog(this);

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