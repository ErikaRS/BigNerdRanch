package com.erikars.criminalintent.model;
import android.content.Context;

public class Photo {
	private final String mFilename;
  private final int mOrientation;
	private boolean mDeleted = false;
	
	public Photo(String filename, int orientation) {
		mFilename = filename;
    mOrientation = orientation;
	}
	
	public String getFilename() {
		return mFilename;
	}

  public int getOrientation() {
    return mOrientation;
  }
}
