package com.erikars.geoquiz;

class QuestionBank {
	private static final TrueFalse[] mQuestions = {
		new TrueFalse(R.string.question_oceans, true),        
		new TrueFalse(R.string.question_mideast, false),        
		new TrueFalse(R.string.question_africa, false),        
		new TrueFalse(R.string.question_americas, true),
		new TrueFalse(R.string.question_asia, true),
	};

	private int mCurrentIndex = 0;
	
	public void advanceTo(int index) {
		mCurrentIndex = inBounds(index);
	}
	
	public QuestionBank() {
		mCurrentIndex = 0;
	}

	public int getIndex() {
		return mCurrentIndex;
	}

	public int getQuestion() {
		return mQuestions[mCurrentIndex].getQuestion();
	}

	public void decrementQuestion() {
		mCurrentIndex = inBounds(--mCurrentIndex);
	}

	public void incrementQuestion() {
		mCurrentIndex = inBounds(++mCurrentIndex);
  }
	
	public boolean getAnswer() {
		return mQuestions[mCurrentIndex].getAnswer();
	}

	public boolean isCorrect(boolean givenAnswer) {
		boolean expectedAnswer = mQuestions[mCurrentIndex].getAnswer();
		return expectedAnswer == givenAnswer;
	}
	
	private static int makePositive(int index) {
		while (index < 0) {
			index += mQuestions.length;
		}
		return index;
	}
	
	private static int inBounds(int index) {
		return makePositive(index) % mQuestions.length;
	}
}
