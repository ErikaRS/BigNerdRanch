package com.erikars.nerdlauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import java.util.List;
import android.util.Log;

public class NerdLauncherFragment extends ListFragment {
  private static final String TAG = NerdLauncherFragment.class.getSimpleName();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    Log.i(TAG, "Finding activities");
    
    Intent startupIntent = new Intent(Intent.ACTION_MAIN);
    startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    
    PackageManager pm = getActivity().getPackageManager();
    List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);
    
    Log.i(TAG, "Found activities: " + activities.size());
  }
}
