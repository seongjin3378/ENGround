package com.a.EnGround.vo;

public class GameSessionVO {
	private Long id; // mapper 자동 생성될 키
	private String userId;
	private String roomId;
	private String chatHistory;
	private boolean isHost;
	private String correctAnswers;
	private String wrongAnswers;
	private int score;
	private String roomTitle;
	
	public String getRoomTitle() {
		return roomTitle;
	}
	public void setRoomTitle(String roomTitle) {
		this.roomTitle = roomTitle;
	}
	public String getUserId() {
		return userId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getChatHistory() {
		return chatHistory;
	}
	public void setChatHistory(String chatHistory) {
		this.chatHistory = chatHistory;
	}
	public boolean isHost() {
		return isHost;
	}
	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}
	public String getCorrectAnswers() {
		return correctAnswers;
	}
	public void setCorrectAnswers(String correctAnswers) {
		this.correctAnswers = correctAnswers;
	}
	public String getWrongAnswers() {
		return wrongAnswers;
	}
	public void setWrongAnswers(String wrongAnswers) {
		this.wrongAnswers = wrongAnswers;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	

	
	
}
