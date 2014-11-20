package com.erikars.nerdlauncher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

@SuppressWarnings("WeakerAccess")
public abstract class SingleFragmentActivity extends ActionBarActivity {
  private static final String TAG = SingleFragmentActivity.class.getSimpleName();
  
  protected abstract Fragment createFragment();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "Creating activity with fragment");
    
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
