package com.a.EnGround.vo;
//
public class QuizVO {
	private int quizNo;
	private String question;
	private int timer;
	private String categoryNo;
	private String hint1;
	private String hint2;
	private String hint3;
	private String answer;
	private String blankIndex;
	private boolean newData;
	private boolean lastRound;
	private int quizRound;
	private int allQuizRound;
	
	public int getQuizRound() {
		return quizRound;
	}
	public void setQuizRound(int quizRound) {
		this.quizRound = quizRound;
	}
	public boolean isNewData() {
		return newData;
	}
	public void setNewData(boolean newData) {
		this.newData = newData;
	}
	public boolean isLastRound() {
		return lastRound;
	}
	public void setLastRound(boolean lastRound) {
		this.lastRound = lastRound;
	}
	public String getBlankIndex() {
		return blankIndex;
	}
	public void setBlankIndex(String blankIndex) {
		this.blankIndex = blankIndex;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getHint1() {
		return hint1;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public void setHint1(String hint1) {
		this.hint1 = hint1;
	}
	public String getHint2() {
		return hint2;
	}
	public void setHint2(String hint2) {
		this.hint2 = hint2;
	}
	public String getHint3() {
		return hint3;
	}
	public void setHint3(String hint3) {
		this.hint3 = hint3;
	}
	public int getQuizNo() {
		return quizNo;
	}
	public void setQuizNo(int quizNo) {
		this.quizNo = quizNo;
	}

	public int getTimer() {
		return timer;
	}
	public void setTimer(int timer) {
		this.timer = timer;
	}
	public String getCategoryNo() {
		return categoryNo;
	}
	public void setCategoryNo(String categoryNo) {
		this.categoryNo = categoryNo;
	}
}
