package com.erikars.hellomoon;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.SurfaceHolder;
import android.media.AudioManager;

public class VideoPlayer {
	private MediaPlayer mPlayer;

	public void play(Context c, SurfaceHolder holder) {
		stop();
		mPlayer = MediaPlayer.create(c, R.raw.apollo_video);
		mPlayer.setDisplay(holder);
		mPlayer.setOnCompletionListener(
			new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer p1) {
					stop();
				}
			});
		mPlayer.start();
	}

	public void stop() {
		if (mPlayer != null) {
	  	mPlayer.release();
			mPlayer = null;
		}
	}
}
