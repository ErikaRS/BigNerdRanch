package com.erikars.criminalintent.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import com.erikars.criminalintent.R;
import com.erikars.criminalintent.model.Crime;
import java.util.UUID;
import com.erikars.criminalintent.model.CrimeLab;

public class CrimeFragment extends Fragment {
  public static final String EXTRA_CRIME_ID = "com.erikars.criminalintent.crime_id";
  
  private Crime mCrime;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent i = getActivity().getIntent();
    UUID id = (UUID) i.getSerializableExtra(EXTRA_CRIME_ID);
    mCrime = CrimeLab.get(getActivity()).getCrime(id);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_crime, container, false /* attachToRoot */);
    
    final Crime crime = mCrime != null ? mCrime : new Crime();
    
    EditText titleField = (EditText) v.findViewById(R.id.crime_title);
    titleField.setText(crime.getTitle());
    titleField.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence c, int start, int before, int count) {
          // Intentionally blank
        }

        @Override
        public void onTextChanged(CharSequence c, int start, int count, int after) {
          crime.setTitle(c.toString());
        }

        @Override
        public void afterTextChanged(Editable c) {
          // Intentionally blank 
        }      
      });
      
    Button dateButton = (Button) v.findViewById(R.id.crime_date);
    dateButton.setText(crime.getFormattesDate());
    dateButton.setEnabled(false);
    
    CheckBox solvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
    solvedCheckBox.setChecked(crime.isSolved());
    solvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton unused, boolean isChecked) {
          crime.setSolved(isChecked);
        }
      });
    
    return v;
  }
}
