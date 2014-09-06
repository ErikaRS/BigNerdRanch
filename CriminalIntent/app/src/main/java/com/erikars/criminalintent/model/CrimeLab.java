package com.erikars.criminalintent.model;
import java.util.ArrayList;
import android.content.Context;

public class CrimeLab {
	private static CrimeLab sCrimeLab;
	private final Context mAppContext;
	
	private CrimeLab(Context appContext) {
		mAppContext = appContext;
	}
	
	// TODO(erikars): I don't like singletons where
	// the get takes an argument that is ignored after
	// initialization
	public static CrimeLab get(Context c) {
		if (sCrimeLab == null) {
			sCrimeLab = new CrimeLab(c.getApplicationContext());
		}
		return sCrimeLab;
	}
}
