package com.a.EnGround.vo;

public class ReportUserVO {
    private int reportNo; // 신고 번호
    private String reportedId; // 신고자 ID
    private String idReported; // 피신고자 ID
    private int userType; // 유저 타입 (변경)
	public int getReportNo() {
		return reportNo;
	}
	public void setReportNo(int reportNo) {
		this.reportNo = reportNo;
	}
	public String getReportedId() {
		return reportedId;
	}
	public void setReportedId(String reportedId) {
		this.reportedId = reportedId;
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
