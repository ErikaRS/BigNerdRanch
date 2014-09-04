package com.erikars.criminalintent.controller;

import com.erikars.criminalintent.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class CrimeActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime);
    }
}
