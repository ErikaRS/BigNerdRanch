package com.erikars.criminalintent.controller;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class CrimeCameraActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
	  return CrimeCameraFragment.newInstance();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Hide the window title.        
		requestWindowFeature(Window.FEATURE_NO_TITLE);        
		// Hide the status bar and other OS-level chrome        
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);
	}
}
