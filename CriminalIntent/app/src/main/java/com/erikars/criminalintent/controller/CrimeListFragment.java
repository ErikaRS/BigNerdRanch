package com.erikars.criminalintent.controller;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import com.erikars.criminalintent.R;
import com.erikars.criminalintent.model.Crime;
import com.erikars.criminalintent.model.CrimeLab;
import com.google.common.base.Preconditions;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class CrimeListFragment extends ListFragment {
	private boolean mSubtitleShown = false;
  private Callbacks mCallbacks;
  
  /** Required interface for hosting activities */
  public interface Callbacks {
    void showCrimeDetails(Crime crime);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    mCallbacks = (Callbacks) activity;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActivity().setTitle(R.string.crimes_title);
		setHasOptionsMenu(true);
		setRetainInstance(true);

    List<Crime> crimes = CrimeLab.get(getActivity()).getCrimes();
    ArrayAdapter<Crime> adapter = new CrimeAdapter(
			getActivity(), crimes);
    setListAdapter(adapter);
  }

	@Override
	public View onCreateView(
		LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
		setSubtitle();
		initEmptyView(v);
		initContextMenu(v);
		return v;
	}

	private void initContextMenu(View v) {
		ListView listView = (ListView) v.findViewById(android.R.id.list);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			// Floating context menu on older versions
			registerForContextMenu(listView);
		} else {
			// Contextual context menu when supported
			initContextualContextMenu(listView);
		}
	}

  @TargetApi(11)
	private void initContextualContextMenu(ListView listView) {
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
				@Override
				public void onItemCheckedStateChanged(
					ActionMode mode, int position, long id, boolean checked) {}

				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					mode.getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
					return true;
				}

				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					return false;
				}

				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					switch (item.getItemId()) {
						case R.id.menu_item_delete_crime:
							CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
							CrimeLab crimeLab = CrimeLab.get(getActivity());
							for (int i = adapter.getCount(); i >= 0; i--) {
								if (getListView().isItemChecked(i)) {
									crimeLab.deleteCrime(adapter.getItem(i));
								}
							}
							mode.finish();
							updateUI();
							return true;
						default:
							return false;
					}
				}

				@Override
				public void onDestroyActionMode(ActionMode mode) {}
			});
	}

	private void initEmptyView(View v) {
		View emptyView = v.findViewById(android.R.id.empty);
		emptyView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					newCrime();
				}
		  });
	}

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
		Crime c = ((CrimeAdapter) getListAdapter()).getItem(position);
		mCallbacks.showCrimeDetails(c);
  }

  @Override
  public void onResume() {
    super.onResume();
    updateUI();
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mCallbacks = null;
  }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
		MenuItem toggleSubtitle = menu.findItem(R.id.menu_item_toggle_subtitle);
		setSubtitleToggle(toggleSubtitle);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = menuInfo.position;
		CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
		Crime crime = adapter.getItem(position);

		switch (item.getItemId()) {
			case R.id.menu_item_delete_crime:
				CrimeLab.get(getActivity()).deleteCrime(crime);
				updateUI();
				return true;
			default:
			  return super.onContextItemSelected(item);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_new_crime:
				newCrime();
				return true;
			case R.id.menu_item_toggle_subtitle:
				mSubtitleShown = !mSubtitleShown;
				setSubtitle();
				setSubtitleToggle(item);
				return true;
			default:
		    return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(
		ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
	}

	private void newCrime() {
		Crime c = new Crime();
		CrimeLab.get(getActivity()).addCrime(c);
    updateUI();
		mCallbacks.showCrimeDetails(c);
	}
    
	private void setSubtitle() {
		ActionBarActivity activity = (ActionBarActivity) getActivity();
		if (mSubtitleShown) {
			// Show subtitle 
			activity.getSupportActionBar().setSubtitle(R.string.subtitle);
		} else {
			// Hide subtitle 
			activity.getSupportActionBar().setSubtitle(null);
		}
	}

	/**
	 * The toggle action is the opposite of the current 
	 * subtitle state 
	 */
	private void setSubtitleToggle(MenuItem item) {
		if (item == null) {
			return;
		}
		if (mSubtitleShown) {
			item.setTitle(R.string.hide_subtitle);
		} else {
			item.setTitle(R.string.show_subtitle);
		}
	}
  
  public void updateUI() {
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
    @SuppressLint("InflateParams")
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
