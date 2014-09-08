package com.erikars.criminalintent.model;
import java.util.ArrayList;
import android.content.Context;
import java.util.UUID;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class CrimeLab {
  private static CrimeLab sCrimeLab;
  
  private final Context mAppContext;
  private final ArrayList<Crime> mCrimes = new ArrayList<>();
  
  private CrimeLab(Context appContext) {
    mAppContext = appContext;
    
    // TODO(erikars): For initial testing only
    for (int i = 0; i < 100; i++) {
      Crime c = new Crime();
      c.setTitle("Crime #" + i);
      c.setSolved(i % 2 == 0);
      mCrimes.add(c);
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
  public ImmutableList<Crime> getCrimes() {
    return ImmutableList.copyOf(mCrimes);
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
}
