package com.erikars.criminalintent.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import com.erikars.criminalintent.R;
import com.erikars.criminalintent.model.Crime;
import com.erikars.criminalintent.model.CrimeLab;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import java.util.List;
import java.util.UUID;
import com.google.common.base.Predicate;

public class CrimePagerActivity extends FragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewPager viewPager = new ViewPager(this);
		viewPager.setId(R.id.viewPager);
		setContentView(viewPager);
		
		final List<Crime> crimes = CrimeLab.get(this).getCrimes();
		FragmentManager fm = getSupportFragmentManager();
		viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
				@Override
				public Fragment getItem(int pos) {
					Preconditions.checkElementIndex(pos, crimes.size());
					UUID uuid = crimes.get(pos).getId();
					return CrimeFragment.newInstance(uuid);
				}

				@Override
				public int getCount() {
					return crimes.size();
				}
  		});
			
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				@Override
				public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {}

				@Override
				public void onPageSelected(int pos) {
					Preconditions.checkElementIndex(pos, crimes.size());
					String title = crimes.get(pos).getTitle();
					if (title == null) {
						title = getResources().getString(R.string.crime_details_label);
					}
				  setTitle(title);
				}

				@Override
				public void onPageScrollStateChanged(int state) {}
	  	});
			
		UUID id = (UUID) getIntent()
			.getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		viewPager.setCurrentItem(getCurrentPosition(crimes, id));
	}

	private static int getCurrentPosition(List<Crime> crimes, final UUID id) {
		int currentPosition = Iterables.indexOf(crimes, new Predicate<Crime>() {
				@Override
				public boolean apply(Crime c) {
					return c.getId().equals(id);
				}
  		});
		currentPosition = Math.max(currentPosition, 0);
		return currentPosition;
	}
}
