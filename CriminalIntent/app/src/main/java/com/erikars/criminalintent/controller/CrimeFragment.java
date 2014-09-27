package com.erikars.criminalintent.controller;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import com.erikars.criminalintent.R;
import com.erikars.criminalintent.model.Crime;
import com.erikars.criminalintent.model.CrimeLab;
import com.google.common.base.Preconditions;
import java.util.Date;
import java.util.UUID;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.MenuInflater;
import android.view.Menu;

public class CrimeFragment extends Fragment {
	private static final String TAG = CrimeFragment.class.getSimpleName();
	private static final String DIALOG_DATE_TIME = "date_time";
	private static final int REQUEST_DATE_TIME = 0;
	
  public static final String EXTRA_CRIME_ID = "com.erikars.criminalintent.crime_id";
  
  private Crime mCrime;
	private Button mDateTimeButton;
  
  public static CrimeFragment newInstance(UUID crimeId) {
    Preconditions.checkNotNull(crimeId);
		Bundle args = new Bundle();
    args.putSerializable(EXTRA_CRIME_ID, crimeId);
    CrimeFragment cf = new CrimeFragment();
    cf.setArguments(args);
    return cf;
  }
  
  private CrimeFragment() {}
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		UUID id = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
    mCrime = CrimeLab.get(getActivity()).getCrime(id);
  }

	@TargetApi(11)
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_crime, container, false /* attachToRoot */);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
		  	&& NavUtils.getParentActivityName(getActivity()) != null) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		if (mCrime == null) {
			mCrime = new Crime();
		}
    
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
    
    CheckBox solvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
    solvedCheckBox.setChecked(mCrime.isSolved());
    solvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton unused, boolean isChecked) {
          mCrime.setSolved(isChecked);
        }
      });
    
    return v;
  }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;
		if (requestCode == REQUEST_DATE_TIME) {
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

	private void goUp() {
		if (NavUtils.getParentActivityName(getActivity()) != null) {
			NavUtils.navigateUpFromSameTask(getActivity());
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_crime, menu);
	}

	@Override
	public void onPause() {
		super.onPause();
		CrimeLab.get(getActivity()).saveCrimes();
	}

	private void updateDateTime() {
		Preconditions.checkNotNull(mCrime);
		Preconditions.checkNotNull(mDateTimeButton);
		mDateTimeButton.setText(mCrime.getFormattedDateTime());
	}
}
