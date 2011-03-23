package com.risotto.view;

import com.hello.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

public class DrugView extends Activity {
	
	/**
	 * Create a menu that will pop up when the user presses the Menu button.
	 */
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.alarm_menu_layout, menu);
	    return true;
	}
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is the drug view class.");
        setContentView(textview);
    }
}
