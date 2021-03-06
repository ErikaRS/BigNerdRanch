package com.erikars.criminalintent.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.erikars.criminalintent.R;
import android.support.v7.app.ActionBarActivity;

@SuppressWarnings("WeakerAccess")
public abstract class SingleFragmentActivity extends ActionBarActivity {
  protected abstract Fragment createFragment();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutResId());

    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

    if (fragment == null) {
      fragment = createFragment();
      fm.beginTransaction()
        .add(R.id.fragmentContainer, fragment)
        .commit();
    }
  }
  
  protected int getLayoutResId() {
    return R.layout.activity_fragment;
  }
}
