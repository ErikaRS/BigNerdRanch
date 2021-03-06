package com.erikars.criminalintent.controller;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;
import com.erikars.criminalintent.model.Photo;

public class PictureUtils {
  
  public static BitmapDrawable getScaledPhoto(Activity a, Photo p) {
    if (p == null) {
      return null;
    }
    String path = a.getFileStreamPath(p.getFilename()).getAbsolutePath();
    return getScaledDrawable(a, path, p.getOrientation());
  }
  
	/**
	 * Get a BitmapDrawable from a local file that is scaled down 
	 * to fit the current window size.
	 */
	@SuppressWarnings("deprecation")
	public static BitmapDrawable getScaledDrawable(Activity a, String path, int orientation) {
		Dimensions dst = getDestinationDimensions(a);
		Dimensions src = getSourceDimensions(path);
		int inSampleSize = getScalingSampleSize(src, dst);
		return loadScaledDrawable(a, path, inSampleSize, orientation);
	}

  public static void cleanImageView(ImageView imageView) {
    if (!(imageView.getDrawable() instanceof BitmapDrawable)) {
      return;
    }

    // Clean up the view's image for the sake of memory
    BitmapDrawable b = (BitmapDrawable) imageView.getDrawable();
    b.getBitmap().recycle();
    imageView.setImageDrawable(null);
  }
  
  public static void deletePhoto(Activity a, Photo photo) {
    if (photo == null) {
      return;
    }
    String path = photo.getFilename();
    if (path != null) {
      a.deleteFile(path);
    }
  }
	
	private static Dimensions getDestinationDimensions(Activity a) {
		Display display = a.getWindowManager().getDefaultDisplay();
		return new Dimensions(display.getWidth(), display.getHeight());
	}
	
	private static Dimensions getSourceDimensions(String path) {
		// Read in the dimensions of the image on disk 
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
    return new Dimensions(options.outWidth, options.outHeight);
	}
	
	private static int getScalingSampleSize(Dimensions src, Dimensions dst) {
	  // Default is no scaling 
		int inSampleSize = 1;
		
		// If the image needs scaling, scale so that the smaller dimension 
		// fits the screen 
		if (src.width > dst.width || src.height > dst.height) {
			if (src.width > src.height) {
				inSampleSize = Math.round(src.height / dst.height);
			} else {
				inSampleSize = Math.round(src.width / dst.width);
			}
		}
		return inSampleSize;
	}
	
	private static BitmapDrawable loadScaledDrawable(
      Activity a, String path, int inSampleSize, int orientation) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = inSampleSize;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		if (bitmap == null) {
			return null;
		}
		
    Matrix matrix = new Matrix();
    matrix.postRotate(orientation);
    bitmap = Bitmap.createBitmap(
        bitmap, 0 /* x-coordinate */, 0 /* y-coordinate */, bitmap.getWidth(), bitmap.getHeight(),
        matrix, true /* don't filter */);
		return new BitmapDrawable(a.getResources(), bitmap);
	}
	
	private static class Dimensions {
		public final float width;
		public final float height;
		
		public Dimensions(float w, float h) {
			width = w;
			height = h;
		}
	}
}
