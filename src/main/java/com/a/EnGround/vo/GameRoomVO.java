package com.a.EnGround.vo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.annotation.JsonIgnore;
//
public class GameRoomVO {
	private String roomId;
	private String userId;
	private String roomTitle;
	
	private int score;
	private int time;
	private int quizRound;
	private String quiz; // json
	private String correctQuestion; // json
	private String wrongQuestion; // json
	private String gameMode;
	
	private int host; //방장은 1 아니면 0
	private LocalDateTime answerTime;
	private boolean isFirstInsertAnswerTemp;
	private boolean isFirstReqUser;
	private boolean isOnlineGameStart;
	
	@JsonIgnore
	private String id;
	
	@JsonIgnore
	private String userType;
	private String categoryName;
	
	
	public boolean isOnlineGameStart() {
		return isOnlineGameStart;
	}
	public void setOnlineGameStart(boolean isOnlineGameStart) {
		this.isOnlineGameStart = isOnlineGameStart;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public boolean isFirstReqUser() {
		return isFirstReqUser;
	}
	public void setFirstReqUser(boolean isFirstReqUser) {
		this.isFirstReqUser = isFirstReqUser;
	}
	
	public boolean isFirstInsertAnswerTemp() {
		return isFirstInsertAnswerTemp;
	}
	public void setFirstInsertAnswerTemp(boolean isFirstInsertAnswerTemp) {
		this.isFirstInsertAnswerTemp = isFirstInsertAnswerTemp;
	}
	public LocalDateTime getAnswerTime() {
		return answerTime;
	}
	public void setAnswerTime(LocalDateTime answerTimeLine) {
		this.answerTime = answerTimeLine;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}

	
	public String getRoomTitle() {
		return roomTitle;
	}
	public void setRoomTitle(String roomTitle) {
		this.roomTitle = roomTitle;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getGameMode() {
		return gameMode;
	}

	public int getHost() {
		return host;
	}
	public void setHost(int host) {
		this.host = host;
	}
	public void setGameMode(String gameMode) {
		this.gameMode = gameMode;
	}
	public int getQuizRound() {
		return quizRound;
	}
	public void setQuizRound(int quizRound) {
		this.quizRound = quizRound;
	}
	public String getQuiz() {
		return quiz;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getCorrectQuestion() {
		return correctQuestion;
	}
	public void setCorrectQuestion(String correctQuestion) {
		this.correctQuestion = correctQuestion;
	}
	public String getWrongQuestion() {
		return wrongQuestion;
	}
	public void setWrongQuestion(String wrongQuestion) {
		this.wrongQuestion = wrongQuestion;
	}
	public void setQuiz(String quiz) {
		this.quiz = quiz;
	}
	public String getId() {
		return roomId;
	}
	public void setId(String id) {
		this.roomId = id;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}


}
