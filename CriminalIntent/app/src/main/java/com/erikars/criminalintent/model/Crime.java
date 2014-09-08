package com.erikars.criminalintent.model;
import java.util.UUID;
import java.util.Date;
import java.util.Locale;
import android.text.format.DateFormat;

public class Crime {
	private final UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean mSolved;
	
	public Crime() {
		mId = UUID.randomUUID();
		mDate = new Date();
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
	
	public void setDate(Date date) {
		mDate = date;
	}

	public Date getDate() {
		return mDate;
	}
	
	public String getFormattesDate() {
		String format = DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEE, MMMM d, yyyy");
		return DateFormat.format(format, getDate()).toString();
	}

	public void setSolved(boolean solved) {
		mSolved = solved;
	}

	public boolean isSolved() {
		return mSolved;
	}

	@Override
	public String toString() {
		return mTitle;
	}
}
