package com.erikars.criminalintent.controller;
import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.erikars.criminalintent.R;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.io.FileNotFoundException;
import android.app.Activity;
import android.content.Intent;

public class CrimeCameraFragment extends Fragment {
	private static final String TAG = CrimeCameraFragment.class.getSimpleName();

  private static final int CAMERA_ID = 0;
	
	public static final String EXTRA_PHOTO_FILENAME = "com.erikars.criminalintent.photo_filename";
  public static final String EXTRA_PHOTO_ORIENTATION
      = "com.erikars.criminalintent.photo_orientation";
	
	private Camera mCamera;
	private View mProgressContainer;
	
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
		final Camera.ShutterCallback showProgress = new Camera.ShutterCallback() {
  			@Override
		  	public void onShutter() {
					// Display the progress indicator 
  				mProgressContainer.setVisibility(View.VISIBLE);
	  		}
  		};

		final Camera.PictureCallback saveJpeg = new Camera.PictureCallback() {
        @TargetApi(10)
	  		@Override
	  		public void onPictureTaken(byte[] data, Camera camera) {
		  		String filename = UUID.randomUUID().toString() + ".jpg";
					FileOutputStream os = null;
					boolean success = true;

					try {
						os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
						os.write(data);
					} catch (IOException e) {
						Log.e(TAG, "Error writing to file: " + filename, e);
						success = false;
					} finally {
						if (os != null) {
							try {
								os.close();
							} catch (IOException e) {
								Log.e(TAG, "Error closing file: " + filename, e);
								success = false;
							}
						}
					}
					
					if (success) {
						Intent result = new Intent();
						result.putExtra(EXTRA_PHOTO_FILENAME, filename);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
              Camera.CameraInfo info = new Camera.CameraInfo();
              mCamera.getCameraInfo(CAMERA_ID, info);
              result.putExtra(EXTRA_PHOTO_ORIENTATION, info.orientation);
            }

						getActivity().setResult(Activity.RESULT_OK, result);
					} else {
						getActivity().setResult(Activity.RESULT_CANCELED);
					}
					
					getActivity().finish();
				}
	  	};
			
		Button takeButton = (Button) v.findViewById(R.id.crime_camera_takePictureButton);
		takeButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mCamera != null) {
						mCamera.takePicture(showProgress, null, saveJpeg);
					}
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
				
					Camera.Size previewSize = getBestSupportesPreviewSize(parameters.getSupportedPreviewSizes());
					parameters.setPreviewSize(previewSize.width, previewSize.height);
					
					Camera.Size pictureSize = getBestSupportesPreviewSize(parameters.getSupportedPictureSizes());
					parameters.setPictureSize(pictureSize.width, pictureSize.height);
					
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
		mProgressContainer = v.findViewById(
		    R.id.crime_camera_progressContainer);
		mProgressContainer.setVisibility(View.INVISIBLE);
	}

	@TargetApi(9)
	@Override
	public void onResume() {
		super.onResume();
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
				mCamera = Camera.open(CAMERA_ID);
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
