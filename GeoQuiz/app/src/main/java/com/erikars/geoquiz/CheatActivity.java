package com.erikars.geoquiz;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.os.PersistableBundle;
import android.util.Log;

public class CheatActivity extends Activity {
	private static final String TAG = CheatActivity.class.toString();

	public static final String EXTRA_ANSWER = "com.erikars.geoquiz.answer";
	public static final String EXTRA_ANSWER_SHOWN = "com.erikars.geoquiz.answer_shown";

	private static final String KEY_ANSWER_SHOWN = "answer_shown";

	private TextView mAnswerTextView;

	private boolean mAnswer;
	private boolean mAnswerShown = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cheat);

		if (savedInstanceState != null) {
			mAnswerShown = savedInstanceState.getBoolean(KEY_ANSWER_SHOWN, false);
		}
		Log.d(TAG, "onCreate() mAnswerShown: " + mAnswerShown);

		mAnswer = getIntent().getBooleanExtra(EXTRA_ANSWER, false);
		mAnswerTextView = (TextView) findViewById(R.id.answerTextView);

		setAnswerShownResult();

		if (mAnswerShown) {
			showAnswer();
		}

		Button showAnswerButton = (Button) findViewById(R.id.showAnswerButton);
		showAnswerButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View unused) {
					showAnswer();
				}
			});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d(TAG, "onSaveInstanceState() mAnswerShown: " + mAnswerShown);
		outState.putBoolean(KEY_ANSWER_SHOWN, mAnswerShown);
	}

	private void showAnswer() {
		mAnswerTextView.setText(mAnswer == true 
														? R.string.true_button
														: R.string.false_button);
		mAnswerShown = true;
		setAnswerShownResult();
	}

	private void setAnswerShownResult() {
		Intent i = new Intent();
		i.putExtra(EXTRA_ANSWER_SHOWN, mAnswerShown);
		setResult(RESULT_OK, i);
	}
}
