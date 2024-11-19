package com.a.EnGround.vo;

public class ReportUserVO {
    private int reportNo; // 신고 번호
    private String reportedId; // 신고자 ID
    private String idReported; // 피신고자 ID
    private int userType; // 유저 타입 (변경)
    private String reportType;
    private String reportContent;
    private String chatHistoryRoomId;
    private String chatContent;
    private String chatTime;
    
    
	public String getChatContent() {
		return chatContent;
	}
	public void setChatContent(String chatContent) {
		this.chatContent = chatContent;
	}
	public String getChatTime() {
		return chatTime;
	}
	public void setChatTime(String chatTime) {
		this.chatTime = chatTime;
	}
	public int getReportNo() {
		return reportNo;
	}
	public void setReportNo(int reportNo) {
		this.reportNo = reportNo;
	}
	public String getChatHistoryRoomId() {
		return chatHistoryRoomId;
	}
	public void setChatHistoryRoomId(String chatHistoryRoomId) {
		this.chatHistoryRoomId = chatHistoryRoomId;
	}
	public String getReportedId() {
		return reportedId;
	}
	public void setReportedId(String reportedId) {
		this.reportedId = reportedId;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getReportContent() {
		return reportContent;
	}
	public void setReportContent(String reportContent) {
		this.reportContent = reportContent;
	}
	public String getIdReported() {
		return idReported;
	}
	public void setIdReported(String idReported) {
		this.idReported = idReported;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	
}
