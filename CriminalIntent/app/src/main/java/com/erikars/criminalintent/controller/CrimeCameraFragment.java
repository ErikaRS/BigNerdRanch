package com.erikars.criminalintent.controller;
import android.annotation.TargetApi;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.erikars.criminalintent.R;
import java.io.IOException;
import java.util.List;
import android.widget.ProgressBar;

public class CrimeCameraFragment extends Fragment {
	private static final String TAG = CrimeCameraFragment.class.getSimpleName();
	
	private Camera mCamera;
	
	public static CrimeCameraFragment newInstance() {
		return new CrimeCameraFragment();
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime_camera, parent, false);
		initTakePictureButton(v);
		initProgressIndicator(v);
		initCameraPreview(v);
		return v;
	}

	private void initTakePictureButton(View v) {
		Button takeButton = (Button) v.findViewById(R.id.crime_camera_takePictureButton);
		takeButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getActivity().finish();
				}
			});
	}

	private void initCameraPreview(View v) {
		SurfaceView cameraPreview = (SurfaceView) v.findViewById(R.id.crime_camera_surfaceView);
		SurfaceHolder holder = cameraPreview.getHolder();
		// setType() and SURFACE_TYPE_PUSH_BUFFERS are both deprecated, 
		// but are required for Camera preview to work on pre-3.0 devices. 
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		holder.addCallback(new SurfaceHolder.Callback() {
				@Override
				public void surfaceCreated(SurfaceHolder holder) {
				  if (mCamera != null) {
						try {
							mCamera.setPreviewDisplay(holder);
						} catch (IOException e) {
							Log.d(TAG, "Error setting up preview display.", e);
						}
					}
				}

				@Override
				public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
					if (mCamera == null) {
						return;
					}
					
					// The surface has changed size; update the preview size
					Camera.Parameters parameters = mCamera.getParameters();
					Camera.Size s = getBestSupportesPreviewSize(parameters.getSupportedPreviewSizes());
					parameters.setPreviewSize(s.width, s.height);
					mCamera.setParameters(parameters);
					try {
				  	mCamera.startPreview();
					} catch (Exception e) {
						Log.e(TAG, "Could not start preview.", e);
						releaseCamera();
					}
				}

				/**
				 * A simple algorithm to get the largest supported preview size.
				 */
				private Camera.Size getBestSupportesPreviewSize(List<Camera.Size> supportedSizes) {
		
					Camera.Size bestSize = null;
					for (Camera.Size s : supportedSizes) {
						if (area(s) > area(bestSize)) {
							bestSize = s;
						}
					}
					return bestSize;
				}
				
				private int area(Camera.Size s) {
					return s == null ? 0 :  s.width * s.height;
				}

				@Override
				public void surfaceDestroyed(SurfaceHolder holder) {
					if (mCamera != null) {
						mCamera.stopPreview();
					}
				}
	  	});
	}
	
	private void initProgressIndicator(View v) {
//		View progressContainer = v.findViewById(
//		    R.id.crime_camera_progressContainer);
//		progressContainer.setVisibility(View.INVISIBLE);
	}

	@TargetApi(9)
	@Override
	public void onResume() {
		super.onResume();
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
				mCamera = Camera.open(0);
			} else {
				mCamera = Camera.open();
			}
		} catch (Exception e) {
			releaseCamera();
			getActivity().finish();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		releaseCamera();
	}
	
	private void releaseCamera() {
		if (mCamera != null) {
		  mCamera.release();
	  	mCamera = null;
		}
	}
}
