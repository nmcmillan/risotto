package com.hello;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class HelloWorld extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Show the new DialogActivity
        //this.startActivity(new Intent(this, DialogActivity.class));
        new StatusBarNotification(this.getApplicationContext()).notify("This is just a test", "It's only a test", "If it were a real emergency, we'd be fucked.");        
        

    }
}