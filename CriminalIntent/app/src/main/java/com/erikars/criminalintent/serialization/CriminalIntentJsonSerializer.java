package com.erikars.criminalintent.serialization;
import android.content.Context;
import com.erikars.criminalintent.model.Crime;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Writer;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.io.IOException;
import org.json.JSONException;
import java.util.UUID;
import java.util.Date;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONTokener;
import android.support.annotation.Nullable;
import android.os.Environment;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileOutputStream;

public class CriminalIntentJsonSerializer {
	private Context mContext;
	private String mFilename;

	private static final String CRIME_ID = "id";
	private static final String CRIME_TITLE = "title";
	private static String CRIME_SOLVED = "solved";;
	private static String CRIME_DATE = "date";
	private static String CRIME_TIME = "time";
	
	public static void saveCrimes(
	    Context context, String filename, ArrayList<Crime> crimes) 
	    throws IOException, JSONException {
		write(context, filename, toJson(crimes));
	}
	
	public static ArrayList<Crime> loadCrimes(Context context, String filename) 
    	throws JSONException, IOException {
		return fromJson(read(context, filename));
	}

	private static void write(
		  Context context, String filename, JSONArray content) 
	    throws IOException {
		OutputStream out = getOutputStream(context, filename);
		try (Writer w = new OutputStreamWriter(out)) {
		  w.write(content.toString());
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
		try (BufferedReader r = new BufferedReader(new InputStreamReader(in))) {
			String line;
			while ((line = r.readLine()) != null) {
				jsonString.append(line);
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
		Crime c = new Crime(id)
		    .setTitle(json.getString(CRIME_TITLE))
		    .setSolved(json.getBoolean(CRIME_SOLVED))
		    .setDate(new Date(json.getLong(CRIME_DATE)))
		    .setTime(new Date(json.getLong(CRIME_TIME)));
		return c;
	}
}
