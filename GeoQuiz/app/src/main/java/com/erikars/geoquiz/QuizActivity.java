package com.erikars.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import android.app.ActionBar;

class QuizActivity extends Activity {
	private static final String TAG = QuizActivity.class.toString();

	private static final String KEY_INDEX = "index";
	private static final String KEY_CHEAT_QUESTIONS = "cheat_questions";

	private TextView mQuestionTextView;

  private final QuestionBank mQuestionBank = new QuestionBank();
	private final Set<Integer> mCheatQuestions = new HashSet<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
		//Log.d(TAG, "onCreate(Bundle) called");
    setContentView(R.layout.activity_quiz);

		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle("Bodies of water");

		if (savedInstanceState != null) {
		  mQuestionBank.advanceTo(savedInstanceState.getInt(KEY_INDEX, 0));
		  mCheatQuestions.addAll(savedInstanceState.getIntegerArrayList(KEY_CHEAT_QUESTIONS));
		}

		mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
		updateQuestion();
		mQuestionTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View unused) {
					mQuestionBank.incrementQuestion();
					updateQuestion();
				}
	  	});

    Button mTrueButton = (Button) findViewById(R.id.true_button);
		mTrueButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View unused) {
          showGrade(mQuestionBank.isCorrect(true));
        }
      });

    Button mFalseButton = (Button) findViewById(R.id.false_button);
		mFalseButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View unused) {
          showGrade(mQuestionBank.isCorrect(false));
        }
      });

    Button mCheatButton = (Button) findViewById(R.id.cheat_button);
		mCheatButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View unused) {
          Intent i = new Intent(QuizActivity.this, CheatActivity.class);
          i.putExtra(CheatActivity.EXTRA_ANSWER, mQuestionBank.getAnswer());
          startActivityForResult(i, 0);
        }
      });


    ImageButton mPrevButton = (ImageButton) findViewById(R.id.prev_button);
		mPrevButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View unused) {
          mQuestionBank.decrementQuestion();
          updateQuestion();
        }
      });

    ImageButton mNextButton = (ImageButton) findViewById(R.id.next_button);
		mNextButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View unused) {
          mQuestionBank.incrementQuestion();
          updateQuestion();
        }
      });
  }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSavedInstanceState");
		outState.putInt(KEY_INDEX, mQuestionBank.getIndex());
		outState.putIntegerArrayList(KEY_CHEAT_QUESTIONS, new ArrayList<Integer>(mCheatQuestions));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		if (data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false)) {
			mCheatQuestions.add(mQuestionBank.getQuestion());
		}

	}

	private void updateQuestion() {
		mQuestionTextView.setText(mQuestionBank.getQuestion());
	}

	private void showGrade(boolean isCorrect) {
		int response = isCorrect
			? R.string.correct_toast
			: R.string.incorrect_toast;
		if (mCheatQuestions.contains(mQuestionBank.getQuestion())) {
			response = R.string.judgment_toast;
		}
		Toast.makeText(
			QuizActivity.this,
		  response,
			Toast.LENGTH_SHORT).show();
	}
}
