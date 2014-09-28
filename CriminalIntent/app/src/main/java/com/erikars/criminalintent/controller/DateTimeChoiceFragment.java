package com.erikars.criminalintent.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import com.erikars.criminalintent.R;
import com.google.common.base.Preconditions;
import java.util.Date;

public class DateTimeChoiceFragment extends DialogFragment {
	private static final String DIALOG_DATE = "date";
	private static final String DIALOG_TIME = "time";
	
	public static DateTimeChoiceFragment newInstance(Date date, Date time) {
		Preconditions.checkNotNull(date);
		Bundle args = new Bundle();
		args.putSerializable(DatePickerFragment.EXTRA_DATE, date);
		args.putSerializable(TimePickerFragment.EXTRA_TIME, time);

		DateTimeChoiceFragment f = new DateTimeChoiceFragment();
		f.setArguments(args);
		return f;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
    View v = (LinearLayout) getActivity().getLayoutInflater()
			.inflate(R.layout.dialog_date_time, null);
		
		Button dateButton = (Button) v.findViewById(R.id.dialog_time_date_dateButton);
		dateButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View unused) {
					Bundle args = getArguments();
					Date date = (Date) args.getSerializable(DatePickerFragment.EXTRA_DATE);
					FragmentManager fm = getActivity().getSupportFragmentManager();
					DatePickerFragment d = DatePickerFragment.newInstance(date);
					d.setTargetFragment(getTargetFragment(), getTargetRequestCode());
					d.show(fm, DIALOG_DATE);
				}
	  	});
			
		Button timeButton = (Button) v.findViewById(R.id.dialog_time_date_timeButton);
		timeButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View unused) {
					Bundle args = getArguments();
					Date time = (Date) args.getSerializable(TimePickerFragment.EXTRA_TIME);
					FragmentManager fm = getActivity().getSupportFragmentManager();
					TimePickerFragment d = TimePickerFragment.newInstance(time);
					d.setTargetFragment(getTargetFragment(), getTargetRequestCode());
					d.show(fm, DIALOG_TIME);
				}
	  	});
		
		return new AlertDialog.Builder(getActivity())
			.setTitle(R.string.date_time_choice_title)
			// No listener because we depend on the date and time dialogs
			// to communicate directly with the target fragment
			.setPositiveButton(android.R.string.ok, null)
			.setView(v)
			.create();
	}
}
