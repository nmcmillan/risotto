package com.risotto.view.wizard;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.risotto.R;

public class TestActivity extends Activity implements OnItemClickListener, OnClickListener {
	
	private ListView myList;
	private TestAdapter myAdapter;
	private static final int TIME_DIALOG_ID = 0;
	//private String[] strings = {"Tylenol", "Vicadin", "Crazy Pills", "Another", "More", "Testing", "Can we fill the view", "This is a good test", "More?!"};
	private String[] strings = {"Tylenol", "Vicadin", "Crazy Pills", "Another"};
	private static final String LOG_TAG = "RISOTTO_TEST_UI";
	
	int mHour;
	int mMinute;
	
	private ViewHolder viewHolder = null; 
	
	private TimePickerDialog.OnTimeSetListener mTimeSetListener =
	    new TimePickerDialog.OnTimeSetListener() {
	        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	            mHour = hourOfDay;
	            mMinute = minute;
	            Log.d(LOG_TAG, "Time: " + mHour + ":" + mMinute);
	            //TestAdapter.updateDisplay();
	        }
	    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	        super.onCreate(savedInstanceState);
	        
	        setContentView(R.layout.test_layout);
	        
	        myList = (ListView) findViewById(R.id.listView1);
	        myAdapter = new TestAdapter(this, strings, this);
	        myList.setAdapter(myAdapter);
	        //myList.setOnItemClickListener(this);
	        
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case TIME_DIALOG_ID:
	        return new TimePickerDialog(this,
	                mTimeSetListener, mHour, mMinute, false);
	    }
	    return null;
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "String => " + strings[position], Toast.LENGTH_SHORT).show();
	}
	
	public void onClick(View v) {
		Log.d(LOG_TAG, "On Click...");
		showDialog(TIME_DIALOG_ID);
		viewHolder = (ViewHolder)v.getTag();
		
	}

	private class TestAdapter extends BaseAdapter implements OnItemClickListener {
		
		LayoutInflater inflater = null;
		String[] text;
		
		OnClickListener clickListener;
		
		
		public TestAdapter(Context context, String[] text, OnClickListener clickListener) {
			super();
	        this.text = text;
	        this.clickListener = clickListener;
	        //this.description = description;
	        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			//return 0;
			return text.length + 1;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int positionId) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder;
			if(convertView==null)
			{
				holder = new ViewHolder();
				
				// If we are at the last position, show the '+' view
				if (position == (getCount()-1)) {
					convertView = inflater.inflate(R.layout.add_new_time_list_item, null);
					holder.icon = (ImageView) convertView.findViewById(R.id.imageView1);
					
				} 
				// Else, create a new time view
				else {
				
					convertView = inflater.inflate(R.layout.add_time_list_item, null);
					holder.icon = (ImageView) convertView.findViewById(R.id.imageView1);
					holder.icon.setImageResource(android.R.drawable.ic_delete);
					holder.text = (TextView) convertView.findViewById(R.id.textView1);
					holder.text.setOnClickListener(this.clickListener);
					holder.id = convertView.getId();
					Log.d(LOG_TAG, "ID: " + holder.id);
					Log.d(LOG_TAG, "Position: " + position);
					updateDisplay(holder.text);
					
				}
				convertView.setTag(holder);
				
			} else {
				holder=(ViewHolder)convertView.getTag();
			}

			return convertView;
			
		}
		
		void updateDisplay(TextView textView) {
			
			final Calendar c = Calendar.getInstance();
	        int mHour = c.get(Calendar.HOUR);
	        int mMinute = c.get(Calendar.MINUTE);
	        int amPm = c.get(Calendar.AM_PM);
	        
	        String amOrPm;
	        
	        if (amPm == Calendar.AM) {
	        	amOrPm = "AM";
	        } else {
	        	amOrPm = "PM";
	        }
	        
	        textView.setText(
		        new StringBuilder()
		                .append(mHour).append(":")
		                .append(pad(mMinute)).append(" " + amOrPm));
		}

		private String pad(int c) {
		    if (c >= 10)
		        return String.valueOf(c);
		    else
		        return "0" + String.valueOf(c);
		}

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			
			
		}
		
	}
	
	static class ViewHolder {
		int id;
		TextView text;
		ImageView icon;
	}

}
