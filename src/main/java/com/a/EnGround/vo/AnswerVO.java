package com.a.EnGround.vo;
//온라인전용

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.TreeMap;

public class AnswerVO {
	/* 온라인 */
	private TreeMap<LocalDateTime, String> userTimeMap = new TreeMap<>(); // <유저아이디, 타임라인>
	private int correctAnswerPlayerCount;

	/* 오프라인 */
	private String correct;
	private int answerTime;

	public int getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(int answerTime) {
		this.answerTime = answerTime;
	}

	public String getCorrect() {
		return correct;
	}

	public void setCorrect(String correct) {
		this.correct = correct;
	}

	public TreeMap<LocalDateTime, String> getUserTimeMap() {
		return userTimeMap;
	}

	public void setUserTimeMap(TreeMap<LocalDateTime, String> userTimeMap) {
		this.userTimeMap = userTimeMap;
	}

	public int getCorrectAnswerPlayerCount() {
		return correctAnswerPlayerCount;
	}

	public void setCorrectAnswerPlayerCount(int correctAnswerPlayerCount) {
		this.correctAnswerPlayerCount = correctAnswerPlayerCount;
	}

}
