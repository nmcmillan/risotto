package com.hello;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class DialogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		
		// Hide the title bar on our DialogActivity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        // Set the content of our activity.
        setContentView(R.layout.dialog_layout);
        
	}
	
	/**
	 * 
	 * Will display a custom dialog prompt to the user. This dialog can be 
	 * displayed without a foreground activity and over the home screen.
	 * 
	 * @param context the current application context.
	 */
	public static void showDialog(Context context) {
		/* Send a new intent to show a new DialogActivity using the
		 * specified context object. */ 
		context.startActivity(new Intent(context, DialogActivity.class));
	}

}
