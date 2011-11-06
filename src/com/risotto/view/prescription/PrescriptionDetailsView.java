package com.risotto.view.prescription;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.risotto.R;
import com.risotto.model.Prescription;
import com.risotto.storage.StorageProvider;

public class PrescriptionDetailsView extends Activity {

	private Prescription prep;
	
	private static final String LOG_TAG = "com.risotto.view.prescription.PrescriptionDetailsView";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.prescription_details_view);
		
		int _id = Integer.parseInt(getIntent().getExtras().getString(PrescriptionView.PRESCRIPTION_DETAILS_DB_ID));

		Uri pUri = StorageProvider.PrescriptionColumns.CONTENT_URI.buildUpon().appendPath(String.valueOf(_id)).build();
		
		Cursor pCursor = this.getContentResolver().query(pUri, Prescription.DEFAULT_PRESCRIPTION_PROJECTION, null, null, null);
		
		//TODO: what do we do if the prescription can't be found in the DB?  Something has gone very wrong if so b/c it was displayed
		//in the list of all prescriptions
		if(pCursor.moveToFirst())
			prep = Prescription.fromCursor(pCursor, getApplicationContext());
		else
			throw new IllegalArgumentException("Could not find prescription in db with id: " + _id);
		
		
		TextView firstName = (TextView)findViewById(R.id.prescription_details_view_patient_first_name);
		firstName.setText(prep.getPatient().getFirstName());
		
		TextView lastName = (TextView)findViewById(R.id.prescription_details_view_patient_last_name);
		lastName.setText(prep.getPatient().getLastName());
		
		TextView drugName = (TextView)findViewById(R.id.prescription_details_view_drug_name);
		drugName.setText(prep.getDrug().getBrandName());
		
		TextView time = (TextView)findViewById(R.id.prescription_details_view_time_one);
		time.setText(prep.toString());
	}
	
}

