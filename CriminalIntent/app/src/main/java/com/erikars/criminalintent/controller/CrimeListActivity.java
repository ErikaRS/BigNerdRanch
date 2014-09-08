package com.erikars.criminalintent.controller;
import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {

  @Override
  protected Fragment createFragment() {
    return new CrimeListFragment();
  }
}
