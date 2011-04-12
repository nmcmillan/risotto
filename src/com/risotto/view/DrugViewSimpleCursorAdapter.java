package com.risotto.view;

import android.content.Context;
import android.database.Cursor;
import android.provider.Contacts.People;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.risotto.R;

public class DrugViewSimpleCursorAdapter extends SimpleCursorAdapter {
	
	/*
	 * Information from:
	 * http://thinkandroid.wordpress.com/2010/01/11/custom-cursoradapters/
	 */
	private Context context;
	private int layout;

	public DrugViewSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.context = context;
		this.layout = layout;
	}
	
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

    	// This will get the cursor that was passed into the constructor.
        Cursor c = getCursor();

        // Inflate the view from the given context to get the parent.
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(layout, parent, false);

        // Do cursor-like things...
        int nameCol = c.getColumnIndex(People.NAME);

        String name = c.getString(nameCol);

        /**
         * Next set the name of the entry.
         */
        TextView name_text = (TextView) v.findViewById(R.id.drug_list_view_name);
        if (name_text != null) {
            name_text.setText(name);
        }

        return v;
    }

    @Override
    public void bindView(View v, Context context, Cursor c) {

    	/*
    	 * Not sure what to do here.
    	 */
//        int nameCol = c.getColumnIndex(People.NAME);
//
//        String name = c.getString(nameCol);
//
//        /**
//         * Next set the name of the entry.
//         */
//        TextView name_text = (TextView) v.findViewById(R.id.name_entry);
//        if (name_text != null) {
//            name_text.setText(name);
//        }
    }
	

}
