package com.erikars.criminalintent.controller;

import android.app.Activity;
import android.hardware.SensorManager;
import android.view.OrientationEventListener;

abstract public class OrthogonalOrienatationEventListener {
	private final OrientationEventListener mAdapted;
	
	public OrthogonalOrienatationEventListener(Activity a) {
	  mAdapted = new OrientationEventListener(a, SensorManager.SENSOR_DELAY_NORMAL) {
		  	private int mRoundedOrientaion = OrientationEventListener.ORIENTATION_UNKNOWN;
			  // The proper orthogonal angles to round to, in 15 degreeincrements
		  	private final int ROUNDED_ANGLES[] = {
			    	0, 0, 0,  // 0, 15, 30
			    	90, 90, 90, 90, 90, 90,  // 45, 60, 75, 90, 105, 120
				    180, 180, 180, 180, 180, 180,  // 135, 150, 165, 180, 195, 210
			    	270, 270, 270, 270, 270, 270,  // 225, 240, 255, 270, 285, 300
			    	0, 0, 0};  // 315, 330, 355
				
				@Override
		  	public void onOrientationChanged(int angle) {
					if (angle != ORIENTATION_UNKNOWN) {
						int newOrientation = ROUNDED_ANGLES[angle / 15];
						if (mRoundedOrientaion != newOrientation) {
							mRoundedOrientaion = newOrientation;
							OrthogonalOrienatationEventListener.this.onOrientationChanged(mRoundedOrientaion);
						}
					}
		  	}
	  	};
	}
	
	public boolean canDetectOrientation() {
		return mAdapted.canDetectOrientation();
	}
	
	public void enable() {
		mAdapted.enable();
	}
	
	public void disable() {
		mAdapted.disable();
	}
	
	abstract public void onOrientationChanged(int angle);
}
