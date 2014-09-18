package com.erikars.hellomoon;

import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.text.style.UpdateAppearance;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class HelloMoonFragment extends Fragment {
	private final AudioPlayer mAudio = new AudioPlayer();
	private final VideoPlayer mVideo = new VideoPlayer();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(
		LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(
			R.layout.fragment_hello_moon, container, false);

		final Button playPauseButton = (Button) v.findViewById(R.id.hellomoon_startButton);
    playPauseButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View unused) {
					// Don't use the player's isPlaying to update the button text
					// because it is updated asynchronously after a state change
					boolean isPlaying;
					if (mAudio.isPlaying()) {
						mAudio.pause();
						isPlaying = false;
					} else {
						mAudio.play(getActivity());
						isPlaying = true;
					}
					updatePlayPause(playPauseButton, isPlaying);
				}
			});
		mAudio.setOnStop(new AudioPlayer.OnStopListener() {
				@Override
				public void onStop() {
				  updatePlayPause(playPauseButton, false);
				}
			});

		Button stopButton = (Button) v.findViewById(R.id.hellomoon_stopButton);
		stopButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View unused) {
					mAudio.stop();
				}
			});
			
		SurfaceView videoSurface = (SurfaceView) v.findViewById(R.id.hellomoon_video);
    final SurfaceHolder holder = videoSurface.getHolder();
		
		Button playVideoButton = (Button) v.findViewById(R.id.hellomoon_startVideoButton);
		playVideoButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View unused) {
					mVideo.play(getActivity(), holder);
				}
		  });
		
		Button stopVideoButton = (Button) v.findViewById(R.id.hellomoon_stopVideoButton);
		stopVideoButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View unused) {
					mVideo.stop();
				}
			});
		return v;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mAudio.stop();
		mVideo.stop();
	}
	
	private static void updatePlayPause(Button playPause, boolean isPlaying) {
		if (isPlaying) {
	  	playPause.setText(R.string.hellomoon_pause);
		} else {
			playPause.setText(R.string.hellomoon_play);
		}
	}
}
