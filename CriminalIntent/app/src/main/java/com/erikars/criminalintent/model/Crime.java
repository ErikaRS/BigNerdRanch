package com.erikars.criminalintent.model;
import java.util.UUID;
import java.util.Date;
import java.util.Locale;

import android.os.Build;
import android.text.format.DateFormat;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import android.support.annotation.Nullable;

public class Crime {
  private final UUID mId;
  private String mTitle;
  private Date mDate = new Date();
	private Date mTime = new Date();
  private boolean mSolved;
	private Photo mPhoto;
  private String mSuspect;
  private String mSuspectLookupKey;
  
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
  
  public boolean hasSuspect() {
    return mSuspect != null;
  }
  
  public @Nullable Crime setSuspect(String suspect) {
    mSuspect = Strings.isNullOrEmpty(suspect)
        ? null 
        : suspect;
    return this;
  }
  
  public String getSuspect() {
    return mSuspect;
  }
  
  public Crime setSuspectLookupKey(String suspectLookupKey) {
    mSuspectLookupKey = suspectLookupKey;
    return this;
  }

  public String getSuspectLookupKey() {
    return mSuspectLookupKey;
  }
  
  @Override
  public String toString() {
    return mTitle;
  }
}
