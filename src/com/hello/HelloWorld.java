package com.hello;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class HelloWorld extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Show the new DialogActivity
        this.startActivity(new Intent(this, DialogActivity.class));
        
    }
}