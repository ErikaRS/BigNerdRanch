package com.erikars.geoquiz;

class TrueFalse {
	private final int mQuestion;
	private final boolean mAnswer;
	
	public TrueFalse(int question, boolean answer) {
		mQuestion = question;
		mAnswer = answer;
	}
	
	public int getQuestion() {
		return mQuestion;
	}
	
	public boolean getAnswer() {
		return mAnswer;
	}
}
