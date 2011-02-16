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
        StatusBarNotification sbn = new StatusBarNotification(this.getApplicationContext(),"test1","test2","test3");        
        sbn.sendMessage();

    }
}