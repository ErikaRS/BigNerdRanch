package com.erikars.criminalintent.controller;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends DialogFragment {
  public static final String EXTRA_IMAGE_PATH = "com.erikars.criminalintent.image_path";
  public static final String EXTRA_IMAGE_ORIENTATION
      = "com.erikars.criminalintent.image_orientation";

  private ImageView mImageView;

  public static ImageFragment newInstance(String image_path, int image_orientation) {
    Bundle args = new Bundle();
    args.putString(EXTRA_IMAGE_PATH, image_path);
    args.putInt(EXTRA_IMAGE_ORIENTATION, image_orientation);
    ImageFragment fragment = new ImageFragment();
    fragment.setArguments(args);

    // Image should take up the full screen
    fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);

    return fragment;
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mImageView = new ImageView(getActivity());
    String path = getArguments().getString(EXTRA_IMAGE_PATH);
    int orientation = getArguments().getInt(EXTRA_IMAGE_ORIENTATION, 0);
    BitmapDrawable image = PictureUtils.getScaledDrawable(getActivity(), path, orientation);
    mImageView.setImageDrawable(image);
    return mImageView;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    PictureUtils.cleanImageView(mImageView);
  }
}
