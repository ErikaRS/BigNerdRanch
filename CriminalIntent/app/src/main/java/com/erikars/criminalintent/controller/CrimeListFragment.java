package com.erikars.criminalintent.controller;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;
import com.erikars.criminalintent.R;
import com.erikars.criminalintent.model.Crime;
import com.erikars.criminalintent.model.CrimeLab;
import java.util.List;
import android.widget.ListView;
import android.view.View;
import android.util.Log;

public class CrimeListFragment extends ListFragment {
  private static final String TAG = CrimeListFragment.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.crimes_title);
		
		List<Crime> crimes = CrimeLab.get(getActivity()).getCrimes();
		ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(
		    getActivity(),
				android.R.layout.simple_list_item_1,
				crimes);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Crime c = (Crime) l.getItemAtPosition(position);
		Log.d(TAG, c.getTitle());
	}
}
