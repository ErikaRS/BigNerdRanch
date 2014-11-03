package com.erikars.criminalintent.serialization;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;
import com.erikars.criminalintent.model.Crime;
import com.erikars.criminalintent.model.Photo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class CriminalIntentJsonSerializer {
	private static final String CRIME_ID = "id";
	private static final String CRIME_TITLE = "title";
	private static final String CRIME_SOLVED = "solved";
	private static final String CRIME_DATE = "date";
	private static final String CRIME_TIME = "time";
	private static final String CRIME_PHOTO = "photo";
	private static final String CRIME_PHOTO_FILENAME = "filename";
  private static final String CRIME_PHOTO_ORIENTATION = "orientation";
  private static final String CRIME_SUSPECT = "suspect";
  private static final String CRIME_SUSPECT_CONTACT_ID = "suspect_contact_id";
  
  // If ever the crimes can't be loaded, don't save them. That would lead to data loss 
  private static boolean forceSkipSave = false;

	public static void saveCrimes(
	    Context context, @SuppressWarnings("SameParameterValue") String filename, ArrayList<Crime> crimes)
	    throws IOException, JSONException {
    if (forceSkipSave) {
      throw new IOException("Saving skipped due to earlier error loading.");
    }
		write(context, filename, toJson(crimes));
	}
	
	public static ArrayList<Crime> loadCrimes(Context context, String filename) 
    	throws JSONException, IOException {
    try {
	  	return fromJson(read(context, filename));
    } catch (JSONException e) {
      forceSkipSave = true;
      throw e;
    } catch (IOException e) {
      forceSkipSave = true;
      throw e;
    }
	}

	private static void write(
		  Context context, String filename, JSONArray content) 
	    throws IOException {
		OutputStream out = getOutputStream(context, filename);
    Writer w = null;
		try {
      w = new OutputStreamWriter(out);
		  w.write(content.toString());
		} finally {
      if (w != null) {
        w.close();
      }
    }
	}

	private static JSONArray read(Context context, String filename) 
	    throws IOException, JSONException {
		InputStream in;
		try {
			in = getInputStream(context, filename);
		} catch (FileNotFoundException ignored) {
			// This is expected to happen when starting fresh
			return null;
		}

		StringBuilder jsonString = new StringBuilder();
    BufferedReader r = null;
		try {
      r = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = r.readLine()) != null) {
				jsonString.append(line);
			}
		} finally {
      if (r != null) {
        r.close();
      }
    }

		return (JSONArray)new JSONTokener(jsonString.toString())
			.nextValue();
	}
	
	private static OutputStream getOutputStream(Context context, String filename) 
	    throws FileNotFoundException {
		if (Environment.MEDIA_MOUNTED.equals(
		    Environment.getExternalStorageState())) {
			return new FileOutputStream(getExternalFile(context, filename));
		} else {
	  	return context.openFileOutput(filename, Context.MODE_PRIVATE);
		}
	}
	
	private static InputStream getInputStream(Context context, String filename) 
	    throws FileNotFoundException {
		if (Environment.MEDIA_MOUNTED.equals(
		    Environment.getExternalStorageState())) {
			return new FileInputStream(getExternalFile(context, filename));
		} else {	
	  	return context.openFileInput(filename);
		}
	}
	
	private static File getExternalFile(Context context, String filename) {
		return new File(context.getExternalFilesDir(null), filename);
	}
	
	private static JSONArray toJson(List<Crime> crimes) 
	    throws JSONException {
		JSONArray converted = new JSONArray();
		for (Crime c : crimes) {
			converted.put(toJson(c));
		}
		return converted;
	}
	
	private static JSONObject toJson(Crime c) 
	throws JSONException {
		JSONObject result = new JSONObject();
		result.put(CRIME_ID, c.getId().toString());
		result.put(CRIME_TITLE, c.getTitle());
		result.put(CRIME_SOLVED, c.isSolved());
		result.put(CRIME_DATE, c.getDate().getTime());
		result.put(CRIME_TIME, c.getTime().getTime());
    if (c.hasSuspect()) {
      result.put(CRIME_SUSPECT, c.getSuspect());
      result.put(CRIME_SUSPECT_CONTACT_ID, c.getSuspectLookupKey());
    }
		if (c.getPhoto() != null) {
			JSONObject photo = new JSONObject();
	  	photo.put(CRIME_PHOTO_FILENAME, c.getPhoto().getFilename());
      photo.put(CRIME_PHOTO_ORIENTATION, c.getPhoto().getOrientation());
			result.put(CRIME_PHOTO, photo);
		}
		return result;
	}
	
	private static ArrayList<Crime> fromJson(@Nullable JSONArray json)
	    throws JSONException {
		ArrayList<Crime> crimes = new ArrayList<>();
		for (int i = 0; json != null && i < json.length(); i++) {
			crimes.add(fromJson(json.getJSONObject(i)));
		}
		return crimes;
	}
	
	private static Crime fromJson(JSONObject json) 
	    throws JSONException {
		UUID id = UUID.fromString(json.getString(CRIME_ID));
		Crime c =  new Crime(id)
		    .setTitle(json.getString(CRIME_TITLE))
		    .setSolved(json.getBoolean(CRIME_SOLVED))
		    .setDate(new Date(json.getLong(CRIME_DATE)))
		    .setTime(new Date(json.getLong(CRIME_TIME)))
        .setSuspect(json.optString(CRIME_SUSPECT, null))
        .setSuspectLookupKey(json.optString(CRIME_SUSPECT_CONTACT_ID, null));

		if (json.has(CRIME_PHOTO)) {
			JSONObject photo = json.getJSONObject(CRIME_PHOTO);
      int orientation = photo.optInt(CRIME_PHOTO_ORIENTATION);
			c.setPhoto(
          new Photo(photo.getString(CRIME_PHOTO_FILENAME), orientation));
		}
		
		return c;
	}
}
