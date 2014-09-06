package com.erikars.criminalintent.controller;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import com.erikars.criminalintent.R;
import android.app.Fragment;

public class CrimeActivity extends Activity {
  private static final String TAG = CrimeActivity.class.toString();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crime);

		FragmentManager fm = getFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

		if (fragment == null) {
			Log.d(TAG, "creating fragment");
			fragment = new CrimeFragment();
			fm.beginTransaction()
				.add(R.id.fragmentContainer, fragment)
				.commit();
		} else {
			Log.d(TAG, "fragment already exists");
		}
	}
}
