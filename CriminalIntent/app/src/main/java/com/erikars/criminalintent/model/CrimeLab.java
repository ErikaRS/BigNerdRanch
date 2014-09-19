package com.erikars.criminalintent.model;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
  private static CrimeLab sCrimeLab;
  
  private final Context mAppContext;
  private final ArrayList<Crime> mCrimes = new ArrayList<>();
  
  private CrimeLab(Context appContext) {
    mAppContext = appContext;
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
   * @throws NullPointerExceptipn if id is null
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
}
