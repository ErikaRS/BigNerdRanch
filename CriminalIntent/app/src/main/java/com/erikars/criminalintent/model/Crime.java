package com.erikars.criminalintent.model;
import java.util.UUID;
import java.util.Date;
import java.util.Locale;

import android.os.Build;
import android.text.format.DateFormat;

public class Crime {
  private final UUID mId;
  private String mTitle;
  private Date mDate = new Date();
	private Date mTime = new Date();
  private boolean mSolved;
	private Photo mPhoto;
  
  public Crime() {
    this(UUID.randomUUID());
  }
	
	public Crime(UUID id) {
		mId = id;
	}

  public UUID getId() {
    return mId;
  }

  public Crime setTitle(String title) {
    mTitle = title;
		return this;
  }

  public String getTitle() {
    return mTitle;
  }
  
  public Crime setDate(Date date) {
    mDate = date;
		return this;
  }

  public Date getDate() {
    return mDate;
  }
  
  public String getFormattedDateTime() {
    String dateFormat = "EEEE, MMMM, d, yyyy";
    String timeFormat = "h:mm";

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      dateFormat = DateFormat.getBestDateTimePattern(
          Locale.getDefault(), "EEEE, MMMM d, yyyy");
      timeFormat = DateFormat.getBestDateTimePattern(
          Locale.getDefault(), "h:mm");
    }

    return DateFormat.format(dateFormat, getDate()).toString()
		    + ", "
				+ DateFormat.format(timeFormat, getTime()).toString();
  }
	
	public Crime setTime(Date time) {
		this.mTime = time;
		return this;
	}

	public Date getTime() {
		return mTime;
	}
	
  public Crime setSolved(boolean solved) {
    mSolved = solved;
		return this;
  }

  public boolean isSolved() {
    return mSolved;
  }
	
	public Crime setPhoto(Photo photo) {
		mPhoto = photo;
		return this;
	}
	
	public Photo getPhoto() {
		return mPhoto;
	}

  @Override
  public String toString() {
    return mTitle;
  }
}
