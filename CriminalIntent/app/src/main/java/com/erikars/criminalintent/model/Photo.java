package com.erikars.criminalintent.model;

public class Photo {
	private final String mFilename;
  private final int mOrientation;
	
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
