package com.a.EnGround.vo;

public class ChatHistoryVO {
	private int chatHistoryNo;
	private String roomId;
	private String userId;
	private String content;
	private String time;
	
	public int getChatHistoryNo() {
		return chatHistoryNo;
	}
	public void setChatHistoryNo(int chatHistoryNo) {
		this.chatHistoryNo = chatHistoryNo;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
