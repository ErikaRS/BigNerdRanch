package com.erikars.hellomoon;
import android.media.MediaPlayer;
import android.content.Context;

public class AudioPlayer {
	MediaPlayer mPlayer;
	private OnStopListener mOnStopListener;
	
	/**
	 * Start the audio clip and ensure that old and completed
	 * playbacks are cleanedup promptly.
	 */
	public void play(Context c) {
    // Don't destroy a player that is only paused
		if (isPlaying()) {
			stop();
		}

		// Start the player. Note that this will be called when
		// the player is restarted during playback since the previous
		// call to stop clears the previous player.
		if (mPlayer == null) {
			mPlayer = MediaPlayer.create(c, R.raw.one_small_step);
			mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer player) {
						stop();
					}
				});
		}
		
		mPlayer.start();
	}
	
	public void pause() {
		if (mPlayer != null) {
			mPlayer.pause();
		}
	}
	
	/**
	 * Updated asynchronously after a state change, so don't use
	 * when the state has just been updated.
	 */
	public boolean isPlaying() {
		return mPlayer != null && mPlayer.isPlaying();
	}
	
	/**
	 * Destructively stops the media player so as to not hog
	 * the limited audio resources.
	 */
	public void stop() {
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
		if (mOnStopListener != null) {
			mOnStopListener.onStop();
		}
	}
	
	public void setOnStop(OnStopListener onStopListener) {
		mOnStopListener = onStopListener;
	}

	public interface OnStopListener {
		public void onStop();
	}
}
