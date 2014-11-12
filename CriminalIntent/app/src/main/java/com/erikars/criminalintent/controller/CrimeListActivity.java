package com.erikars.criminalintent.controller;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.erikars.criminalintent.R;
import com.erikars.criminalintent.model.Crime;
import android.support.v4.app.FragmentTransaction;

public class CrimeListActivity 
    extends SingleFragmentActivity
implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {

  @Override
  protected Fragment createFragment() {
    return new CrimeListFragment();
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_masterdetail;
  }

  @Override
  public void showCrimeDetails(Crime crime) {
    if (findViewById(R.id.detailFragmentContainer) == null) {
      showCrimeDetailsInSeparateActivity(crime);
    } else {
      showCrimeDetailsInline(crime);
    }
  }

  private void showCrimeDetailsInSeparateActivity(Crime crime) {
    Intent i = new Intent(this, CrimePagerActivity.class);
    i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
    startActivity(i);
  }
  
  private void showCrimeDetailsInline(Crime crime) {
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();

    Fragment oldDetail = fm.findFragmentById(R.id.detailFragmentContainer);
    Fragment newDetail = CrimeFragment.newInstance(crime.getId());

    if (oldDetail != null) {
      ft.remove(oldDetail);
    }

    ft.add(R.id.detailFragmentContainer, newDetail);
    ft.commit();
  }
  

  @Override
  public void onCrimeUpdated(Crime c) {
    FragmentManager fm = getSupportFragmentManager();
    CrimeListFragment listFragment = 
        (CrimeListFragment) fm.findFragmentById(R.id.fragmentContainer);
    listFragment.updateUI();
  }
}
