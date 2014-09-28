package com.erikars.criminalintent.controller;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;
import com.erikars.criminalintent.R;
import com.google.common.base.Preconditions;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {
	public static final String EXTRA_TIME = "com.erikars.criminalintent.time";

	public static TimePickerFragment newInstance(Date time) {
		Preconditions.checkNotNull(time);
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TIME, time);
		
		TimePickerFragment f = new TimePickerFragment();
		f.setArguments(args);
		return f;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Date time = (Date) getArguments().getSerializable(EXTRA_TIME);
		TimePicker v = (TimePicker) getActivity().getLayoutInflater().
		    inflate(R.layout.dialog_time, null);
		Calendar c = Calendar.getInstance();
		c.setTime(time);
	  v.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
		v.setCurrentMinute(c.get(Calendar.MINUTE));
		v.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
				@Override
				public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
					Calendar c = new GregorianCalendar();
					c.set(Calendar.HOUR_OF_DAY, hourOfDay);
				  c.set(Calendar.MINUTE, minute);
				  getArguments().putSerializable(EXTRA_TIME, c.getTime());
				}
	  	});
			
		return new AlertDialog.Builder(getActivity())
		    .setTitle(R.string.time_picker_title)
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
		data.putExtra(EXTRA_TIME, getArguments().getSerializable(EXTRA_TIME));
		getTargetFragment().onActivityResult(
			getTargetRequestCode(), resultCode, data);
	}
}
