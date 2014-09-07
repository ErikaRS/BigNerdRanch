package com.erikars.criminalintent.controller;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.erikars.criminalintent.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class CrimeActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return new CrimeFragment();
	}
}
