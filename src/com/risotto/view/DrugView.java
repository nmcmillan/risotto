package com.risotto.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DrugView extends Activity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is the drug view class.");
        setContentView(textview);
    }
}
