package com.hello;

import android.app.Activity;
import android.os.Bundle;

public class HelloWorld extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Show the new DialogActivity
        DialogActivity.showDialog(this);

    }
}