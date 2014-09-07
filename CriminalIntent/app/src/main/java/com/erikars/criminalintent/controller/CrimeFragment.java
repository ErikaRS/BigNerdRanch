package com.erikars.criminalintent.controller;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.erikars.criminalintent.R;
import com.erikars.criminalintent.model.Crime;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.text.format.DateFormat;
import java.util.Locale;
import android.support.v4.app.Fragment;

public class CrimeFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime, container, false /* attachToRoot */);
		
		final Crime crime = new Crime();
		
		EditText titleField = (EditText) v.findViewById(R.id.crime_title);
		titleField.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence c, int start, int before, int count) {
					// Intentionally blank
				}

				@Override
				public void onTextChanged(CharSequence c, int start, int count, int after) {
					crime.setTitle(c.toString());
				}

				@Override
				public void afterTextChanged(Editable c) {
					// Intentionally blank 
				}		  
	  	});
			
		Button dateButton = (Button) v.findViewById(R.id.crime_date);
		String format = DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEE, MMMM d, yyyy");
		String formattedDate = DateFormat.format(format, crime.getDate()).toString();
		dateButton.setText(formattedDate);
		dateButton.setEnabled(false);
		
		CheckBox solvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
		solvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton unused, boolean isChecked) {
					crime.setSolved(isChecked);
				}
	  	});
		
		return v;
	}
}
