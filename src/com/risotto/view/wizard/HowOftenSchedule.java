 package com.risotto.view.wizard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.risotto.R;

public class HowOftenSchedule extends Activity implements OnClickListener {
	
	public static final String LOG_TAG = "com.risotto.view.wizard.HowOftenSchedule";
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
		
		Intent intent = getIntent();

		setContentView(R.layout.wizard_how_often_schedule);
		
		TextView setTime = (TextView) this.findViewById(R.id.wizard_how_often_schedule_hour_display);
		setTime.setOnClickListener(this);
		
		RadioButton am = (RadioButton) this.findViewById(R.id.wizard_how_often_schedule_am);
		am.setOnClickListener(this);
		
		RadioButton pm = (RadioButton) this.findViewById(R.id.wizard_how_often_schedule_pm);
		pm.setOnClickListener(this);
		
		Button addTime = (Button) this.findViewById(R.id.wizard_how_often_schedule_add_time_button);
		addTime.setOnClickListener(this);
		
	}

	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.wizard_how_often_schedule_hour_display:
				Log.d(LOG_TAG,"display time picker");
				break;
			case R.id.wizard_how_often_schedule_am:
				Log.d(LOG_TAG,"am picked");
				break;
			case R.id.wizard_how_often_schedule_pm:
				Log.d(LOG_TAG,"PM picked.");
				break;
			case R.id.wizard_how_often_schedule_add_time_button:
				Log.d(LOG_TAG,"Add time.");
				break;
			default:
				
		}
		
	}
}
