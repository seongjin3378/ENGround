<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
 <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
</head> 
<body>
	<div class="side">
    	<div class="side-container">
    		<div class="h3"></div>
    		<div class="side-score">점수: </div>
    		<div class="dropdown">
        		<a href="javascript:void(0)" class="onlineGame-dropbtn" onclick="toggleDropdown()">ㆍ</a>
        		<div id="dropdown-content" class="dropdown-content">
  					<a href="#" class="delegation-host-button">방장 위임</a>
                    <a href="#" class="kick-button">강퇴</a>
                    <a href="#" class="report-button" data-toggle="modal" data-target="#reportModal">신고</a>

        		</div>
    		</div>
    	</div>
    	<div class="side-container">
    	
    		<div class="h3"></div>
    		<div class="side-score">점수: </div>
    		<div class="dropdown">
        		<a href="javascript:void(0)" class="onlineGame-dropbtn" onclick="toggleDropdown()">ㆍ</a>
        		<div id="dropdown-content" class="dropdown-content">
					<a href="#" class="delegation-host-button">방장 위임</a>
                    <a href="#" class="kick-button">강퇴</a>
                    <a href="#" class="report-button" data-toggle="modal" data-target="#reportModal">신고</a>
        		</div>
    		</div>
    	</div>
    	<div class="side-container">
    		<div class="h3"></div>
    		<div class="side-score">점수: </div>
    		<div class="dropdown">
        		<a href="javascript:void(0)" class="onlineGame-dropbtn" onclick="toggleDropdown()">ㆍ</a>
        		<div id="dropdown-content" class="dropdown-content">
					<a href="#" class="delegation-host-button">방장 위임</a>
                    <a href="#" class="kick-button">강퇴</a>
                    <a href="#" class="report-button" data-toggle="modal" data-target="#reportModal">신고</a>
        		</div>
    		</div>
    	</div>
    	<div class="side-container">
    		<div class="h3"></div>
    		<div class="side-score">점수: </div>
    		<div class="dropdown">
        		<a href="javascript:void(0)" class="onlineGame-dropbtn" onclick="toggleDropdown()">ㆍ</a>
        		<div id="dropdown-content" class="dropdown-content">
					<a href="#" class="delegation-host-button">방장 위임</a>
                    <a href="#" class="kick-button">강퇴</a>
                    <a href="#" class="report-button" data-toggle="modal" data-target="#reportModal">신고</a>
        		</div>
    		</div>
    	</div>
    	<div class="side-container">
    		<div class="h3"></div>
    		<div class="side-score">점수: </div>
    		<div class="dropdown">
        		<a href="javascript:void(0)" class="onlineGame-dropbtn" onclick="toggleDropdown()">ㆍ</a>
        		<div id="dropdown-content" class="dropdown-content">
					<a href="#" class="delegation-host-button">방장 위임</a>
                    <a href="#" class="kick-button">강퇴</a>
                    <a href="#" class="report-button" data-toggle="modal" data-target="#reportModal">신고</a>
        		</div>
    		</div>
    	</div>
    	<div class="side-container">
    		<div class="h3"></div>
    		<div class="side-score">점수: </div>
    		<div class="dropdown">
        		<a href="javascript:void(0)" class="onlineGame-dropbtn" onclick="toggleDropdown()">ㆍ</a>
        		<div id="dropdown-content" class="dropdown-content">
					<a href="#" class="delegation-host-button">방장 위임</a>
                    <a href="#" class="kick-button">강퇴</a>
                    <a href="#" class="report-button" data-toggle="modal" data-target="#reportModal">신고</a>
        		</div>
    		</div>
    	</div>
	</div>
	
	<div class="modal fade" id="reportModal" tabindex="-1" role="dialog" aria-labelledby="reportModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="reportModalLabel">신고하기</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <div class="form-group">
                        <label for="reportedUser">신고 유저 이름</label>
                        <input type="text" class="form-control" id="reportedUser" value="유저이름" readonly>
                    </div>
                    <div class="form-group">
                        <label for="reportType">신고 유형</label>
                        <select class="form-control" id="reportType">
                            <option>스팸</option>
                            <option>욕설</option>
                            <option>핵프로그램</option>
                            <option>기타</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="reportContent">신고 내용</label>
                        <textarea class="form-control" id="reportContent" rows="3" required></textarea>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" id="report-req-button">신고하기</button>
            </div>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
<style>
    @media (max-width: 1200px) {
        .side {
            display: none; /* 화면 너비가 1200px 이하일 때 사이드바를 숨김 */
        }
    }
</style>
</html> 