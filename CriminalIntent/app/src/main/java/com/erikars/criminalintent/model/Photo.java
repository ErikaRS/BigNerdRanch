package com.erikars.criminalintent.model;

public class Photo {
	private final String mFilename;
	
	public Photo(String filename) {
		mFilename = filename;
	}
	
	public String getFilename() {
		return mFilename;
	}
}
