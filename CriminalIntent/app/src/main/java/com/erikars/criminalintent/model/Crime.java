package com.erikars.criminalintent.model;
import java.util.UUID;

public class Crime {
	private final UUID mId;
	private String mTitle;
	
	public Crime() {
		mId = UUID.randomUUID();
	}

	public UUID getId() {
		return mId;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getTitle() {
		return mTitle;
	}
}
