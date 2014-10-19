package com.erikars.criminalintent.controller;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.erikars.criminalintent.R;
import com.erikars.criminalintent.model.Crime;
import com.erikars.criminalintent.model.CrimeLab;
import com.erikars.criminalintent.model.Photo;
import com.google.common.base.Preconditions;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
	private static final String DIALOG_DATE_TIME = "date_time";
  private static final String DIALOG_IMAGE = "image";

	private static final int REQUEST_DATE_TIME = 0;
	private static final int REQUEST_PHOTO = 1;

  public static final String EXTRA_CRIME_ID = "com.erikars.criminalintent.crime_id";

  private Crime mCrime;
	private Button mDateTimeButton;
	private ImageView mPhotoView;

  public static CrimeFragment newInstance(UUID crimeId) {
    Preconditions.checkNotNull(crimeId);
		Bundle args = new Bundle();
    args.putSerializable(EXTRA_CRIME_ID, crimeId);
    CrimeFragment cf = new CrimeFragment();
    cf.setArguments(args);
    return cf;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
    initCrime();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_crime, container, false /* attachToRoot */);
    initActionBar();
    initTitle(v);
    initDateTimePicker(v);
    initSolvedButton(v);
		initTakePictureButton(v);
		initPhotoView(v);
    
    return v;
  }

  @Override
  public void onStart() {
    super.onStart();
    showPhoto();
  }

  @Override
  public void onPause() {
    super.onPause();
    CrimeLab.get(getActivity()).saveCrimes();
  }

  @Override
  public void onStop() {
    super.onStop();
    PictureUtils.cleanImageView(mPhotoView);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.fragment_crime, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        goUp();
        return true;
      case R.id.menu_item_delete_crime:
        CrimeLab.get(getActivity()).deleteCrime(mCrime);
        goUp();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != Activity.RESULT_OK) return;
    switch (requestCode) {
      case REQUEST_DATE_TIME:
        handleDateTimeResult(data);
        break;
      case REQUEST_PHOTO:
        handlePhotoResult(data);
        break;
    }
  }

  private void initCrime() {
    UUID id = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
    mCrime = CrimeLab.get(getActivity()).getCrime(id);
    if (mCrime == null) {
      mCrime = new Crime();
    }
  }

  private void initActionBar() {
    if (NavUtils.getParentActivityName(getActivity()) != null) {
      ((ActionBarActivity) getActivity()).getSupportActionBar()
          .setDisplayHomeAsUpEnabled(true);
    }
  }

	private void initTitle(View v) {
		EditText titleField = (EditText) v.findViewById(R.id.crime_title);
    titleField.setText(mCrime.getTitle());
    titleField.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence c, int start, int before, int count) {
          // Intentionally blank
        }

        @Override
        public void onTextChanged(CharSequence c, int start, int count, int after) {
          mCrime.setTitle(c.toString());
        }

        @Override
        public void afterTextChanged(Editable c) {
          // Intentionally blank 
        }      
      });
	}
	
	private void initDateTimePicker(View v) {
		mDateTimeButton = (Button) v.findViewById(R.id.crime_date_time);
    updateDateTime();
		mDateTimeButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					FragmentManager fm = getActivity().getSupportFragmentManager();
					DateTimeChoiceFragment d = DateTimeChoiceFragment.newInstance(
						mCrime.getDate(), mCrime.getTime());
					d.setTargetFragment(CrimeFragment.this, REQUEST_DATE_TIME);
					d.show(fm, DIALOG_DATE_TIME);
				}
	  	});
	}
	
	private void initSolvedButton(View v) {
		CheckBox solvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
    solvedCheckBox.setChecked(mCrime.isSolved());
    solvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton unused, boolean isChecked) {
          mCrime.setSolved(isChecked);
        }
      });
	}
	
	@TargetApi(9)
	private void initTakePictureButton(View v) {
		ImageButton takePicture = (ImageButton) v.findViewById(R.id.crime_takePictureButton);
		takePicture.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
					startActivityForResult(i, REQUEST_PHOTO);
				}
	  	});
			
		// If a camera is not available, disable the take picture button
		PackageManager pm = getActivity().getPackageManager();
		boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
		    || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) 
				|| (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
				    && Camera.getNumberOfCameras() > 0);
		if (!hasCamera) {
			takePicture.setEnabled(false);
		}
	}
	
	private void initPhotoView(View v) {
		mPhotoView = (ImageView) v.findViewById(R.id.crime_photoPreview);
    mPhotoView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Photo p = mCrime.getPhoto();
        if (p == null) {
          return;
        }

        FragmentManager fm = getActivity().getSupportFragmentManager();
        String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
        ImageFragment.newInstance(path, p.getOrientation()).show(fm, DIALOG_IMAGE);
      }
    });
	}

	private void showPhoto() {
		// (Re)set the image to use the crime's photo 
		Photo p = mCrime.getPhoto();
		BitmapDrawable b = null;
		if (p != null) {
			String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
			b = PictureUtils.getScaledDrawable(getActivity(), path, p.getOrientation());
		}
		mPhotoView.setImageDrawable(b);
	}

	private void handleDateTimeResult(Intent data) {
		Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
		if (date != null) {
			mCrime.setDate(date);
		}
		Date time = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
		if (time != null) {
			mCrime.setTime(time);
		}
		updateDateTime();
	}

	private void handlePhotoResult(Intent data) {
		String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
    int orientation = data.getIntExtra(CrimeCameraFragment.EXTRA_PHOTO_ORIENTATION, 0);
		if (filename != null) {
			mCrime.setPhoto(new Photo(filename, orientation));
		}
		showPhoto();
	}

	private void goUp() {
		if (NavUtils.getParentActivityName(getActivity()) != null) {
			NavUtils.navigateUpFromSameTask(getActivity());
		}
	}

  private void updateDateTime() {
		Preconditions.checkNotNull(mCrime);
		Preconditions.checkNotNull(mDateTimeButton);
		mDateTimeButton.setText(mCrime.getFormattedDateTime());
	}
}
