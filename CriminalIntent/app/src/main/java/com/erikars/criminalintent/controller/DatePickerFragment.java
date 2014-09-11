package com.erikars.criminalintent.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import com.erikars.criminalintent.R;

public class DatePickerFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View v = getActivity().getLayoutInflater()
		    .inflate(R.layout.dialog_date, null);
		return new AlertDialog.Builder(getActivity())
						.setTitle(R.string.date_picker_title)
						.setPositiveButton(android.R.string.ok, null)
						.setView(v)
						.create();
	}
}
