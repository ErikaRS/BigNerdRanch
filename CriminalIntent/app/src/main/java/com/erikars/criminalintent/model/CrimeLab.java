package com.erikars.criminalintent.model;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.erikars.criminalintent.serialization.CriminalIntentJsonSerializer;
import org.json.JSONException;
import java.io.IOException;
import android.util.Log;

public class CrimeLab {
	private static final String TAG = CrimeLab.class.getSimpleName();
	private static final String FILENAME = "crimes.json";
	
  private static CrimeLab sCrimeLab;
  
  private final Context mAppContext;
  private final ArrayList<Crime> mCrimes;
  
  private CrimeLab(Context appContext) {
    mAppContext = appContext;
    ArrayList<Crime> parsedCrimes = null;
		try {
			parsedCrimes = CriminalIntentJsonSerializer.loadCrimes(mAppContext, FILENAME);
		} catch (IOException | JSONException e) {
			Log.e(TAG, "Error loading crimes", e);
		} finally {
      mCrimes = parsedCrimes != null ? parsedCrimes : new ArrayList<Crime>();
    }
  }
  
  // TODO(erikars): I don't like singletons where
  // the get takes an argument that is ignored is later
  // calls to get
  public static CrimeLab get(Context c) {
    if (sCrimeLab == null) {
      sCrimeLab = new CrimeLab(c.getApplicationContext());
    }
    return sCrimeLab;
  }
  
  /**
   * @return an immutable list of all crimes. The crimes 
   *      themselves are mutable, and any mutations will 
   *      be reflected in this CrimeLab
   */
  public List<Crime> getCrimes() {
    return mCrimes;
  }
  
  /**
   * @return the crime with the given id or null if there is
   *     no such crime.
   *
   * @throws NullPointerException if id is null
   */
  public @Nullable Crime getCrime(@NonNull UUID id) {
    Preconditions.checkNotNull(id);
    for (Crime c : mCrimes) {
      if (c.getId().equals(id)) {
        return c;
      }
    }
    return null;
  }
	
	public void addCrime(Crime c) {
		mCrimes.add(c);
	}
	
	public void deleteCrime(Crime c) {
		mCrimes.remove(c);
    
    // Duplicate with photo utils.
    // TODO: Move PhotoUtils so it can be used from here
    if (c.getPhoto() == null) {
      return;
    }
    String path = c.getPhoto().getFilename();
    if (path != null) {
      mAppContext.deleteFile(path);
    }
	}
	
	public void saveCrimes() {
		try {
			CriminalIntentJsonSerializer.saveCrimes(
		    mAppContext, FILENAME, mCrimes);
				Log.d(TAG, "Crimes saved to file.");
		} catch (IOException | JSONException e) {
			Log.e(TAG, "Error saving crimes", e);
		}
	}
}
