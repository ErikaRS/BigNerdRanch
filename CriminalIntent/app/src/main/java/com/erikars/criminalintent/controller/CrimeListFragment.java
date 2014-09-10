package com.erikars.criminalintent.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
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
    
    List<Crime> crimes = CrimeLab.get(getActivity()).getCrimes();
    ArrayAdapter<Crime> adapter = new CrimeAdapter(
        getActivity(), crimes);
    setListAdapter(adapter);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    Crime c = ((CrimeAdapter) getListAdapter()).getItem(position);
    Intent i = new Intent(getActivity(), CrimeActivity.class);
    i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
    startActivity(i);
  }

  @Override
  public void onResume() {
    super.onResume();
    ((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
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
      date.setText(c.getFormattesDate());
      
      CheckBox solved = (CheckBox) convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
      solved.setChecked(c.isSolved());
      
      return convertView;
    }
  }
}
