<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>관리자 페이지</title>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<link rel="stylesheet" href="<c:url value='/resources/css/styleYOON.css'/>"/>
</head>
<body>
	<div class="container">
	<%@ include file="/WEB-INF/include/header.jsp" %>
		<div class="manager-container">
				<div class="manager-head">관리 페이지 목록</div>
				<div class="manager-boby">
					<div class="manager-align">
    					<div class="dropdown">
        					<a href="javascript:void(0)" class="manager-dropbtn" onclick="toggleDropdown()">유저 관리 페이지</a>
        					<div id="dropdown-content" class="dropdown-content">
                    			<a href="<c:url value='/userInquiry'/>">유저 조회, 관리자 전환</a>
                    			<a href="<c:url value='/reportList'/>">신고목록 조회</a>
        					</div>
    					</div>
    				</div>
    				<div class="manager-align">
    					<div class="dropdown">
        					<a href="javascript:void(0)" class="manager-dropbtn" onclick="toggleDropdown()">문제 관리 페이지</a>
        					<div id="dropdown-content" class="dropdown-content">
                    			<a href="<c:url value='/quizUpdate'/>">문제 수정, 삭제</a>
                    			<a href="<c:url value='/quizInsert'/>">문제 추가</a>
        					</div>
    					</div>
    				</div>
    				<div class="manager-align">
    					<div class="dropdown">
        					<a href="javascript:void(0)" class="manager-dropbtn" onclick="toggleDropdown()">광고 관리 페이지</a>
        					<div id="dropdown-content" class="dropdown-content">
                    			<a href="<c:url value='/AdPage'/>">광고 수정</a>
        					</div>
    					</div>
    				</div>
				</div>
		</div>
	</div>
</body>
</html>