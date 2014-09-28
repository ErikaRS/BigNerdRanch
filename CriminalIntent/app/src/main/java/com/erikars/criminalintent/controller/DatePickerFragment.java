package com.erikars.criminalintent.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import com.erikars.criminalintent.R;
import com.google.common.base.Preconditions;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import android.content.Intent;
import android.content.DialogInterface;
import android.app.Activity;

public class DatePickerFragment extends DialogFragment {
	private static final String TAG = DatePickerFragment.class.getSimpleName();
	
	public static final String EXTRA_DATE = "com.erikars.criminalintent.date";

	public static DatePickerFragment newInstance(Date date) {
		Preconditions.checkNotNull(date);
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DATE, date);
		
		DatePickerFragment f = new DatePickerFragment();
		f.setArguments(args);
		return f;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Date date = (Date) getArguments().getSerializable(EXTRA_DATE);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		DatePicker v = (DatePicker) getActivity().getLayoutInflater()
		    .inflate(R.layout.dialog_date, null);
		v.init(
			c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
			new OnDateChangedListener() {
				@Override
				public void onDateChanged(DatePicker view, int year, int month, int day) {
					Date updated = new GregorianCalendar(year, month, day).getTime();
					getArguments().putSerializable(EXTRA_DATE, updated);
				}
			});
		v.updateDate(
		    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		return new AlertDialog.Builder(getActivity())
			.setTitle(R.string.date_picker_title)
			.setPositiveButton(android.R.string.ok,
		    	new DialogInterface.OnClickListener() {
			    	@Override
		    		public void onClick(DialogInterface dialog, int which) {
				    	setResult(Activity.RESULT_OK);
		    		}
		    	})
			.setView(v)
			.create();
	}
	
	private void setResult(int resultCode) {
		if (getTargetFragment() == null) {
			return;
		}
		Intent data = new Intent();
		data.putExtra(EXTRA_DATE, getArguments().getSerializable(EXTRA_DATE));
		getTargetFragment().onActivityResult(
		    getTargetRequestCode(), resultCode, data);
	}
}
