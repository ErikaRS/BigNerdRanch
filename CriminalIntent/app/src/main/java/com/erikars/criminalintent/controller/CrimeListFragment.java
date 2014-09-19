package com.erikars.criminalintent.controller;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import com.erikars.criminalintent.R;
import com.erikars.criminalintent.model.Crime;
import com.erikars.criminalintent.model.CrimeLab;
import com.google.common.base.Preconditions;
import java.util.List;

public class CrimeListFragment extends ListFragment {
  private static final String TAG = CrimeListFragment.class.getSimpleName();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActivity().setTitle(R.string.crimes_title);
		setHasOptionsMenu(true);
    
    List<Crime> crimes = CrimeLab.get(getActivity()).getCrimes();
    ArrayAdapter<Crime> adapter = new CrimeAdapter(
        getActivity(), crimes);
    setListAdapter(adapter);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    Crime c = ((CrimeAdapter) getListAdapter()).getItem(position);
		showCrimeDetails(c);
  }

  @Override
  public void onResume() {
    super.onResume();
    ((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
  }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
	}

	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_new_crime:
				Crime c = new Crime();
				CrimeLab.get(getActivity()).addCrime(c);
				showCrimeDetails(c);
				return true;
			case R.id.menu_item_show_subtitle:
				if (getActivity().getActionBar().getSubtitle() == null) {
					// Show subtitle 
			  	getActivity().getActionBar().setSubtitle(R.string.subtitle);
				  item.setTitle(R.string.hide_subtitle);
				} else {
					// Hide subtitle 
					getActivity().getActionBar().setSubtitle(null);
					item.setTitle(R.string.show_subtitle);
				}
				return true;
			default:
		    return super.onOptionsItemSelected(item);
		}
	}

	private void showCrimeDetails(Crime c) {
		Intent i = new Intent(getActivity(), CrimePagerActivity.class);
		i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
		startActivityForResult(i, 0);
	}
  
  private static class CrimeAdapter extends ArrayAdapter<Crime> {
    final Activity mActivity;
    
    CrimeAdapter(Activity activity, List<Crime> crimes) {
      super(activity, 0 /* not using a predefined layout */, 
          crimes);
      mActivity = Preconditions.checkNotNull(activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView = mActivity.getLayoutInflater()
            .inflate(R.layout.list_item_crime, null);
      }
      
      Crime c = getItem(position);
      
      TextView title = (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
      title.setText(c.getTitle());
      
      TextView date = (TextView) convertView.findViewById(R.id.crime_list_item_dateTextView);
      date.setText(c.getFormattedDateTime());
      
      CheckBox solved = (CheckBox) convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
      solved.setChecked(c.isSolved());
      
      return convertView;
    }
  }
}
