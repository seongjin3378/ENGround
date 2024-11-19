<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>신고 목록 조회</title>
<link rel="stylesheet" href="<c:url value='/resources/css/styleYOON.css'/>"/>
</head>
<body>
	<div class="container">
		<jsp:include page="/WEB-INF/include/header.jsp" />
		<div class="reportList-container">
			<div class="reportList-head">유저 신고 페이지</div>
			<div class="reportList-body">
					<form id="report" action="/reportList" method="get">
						<c:forEach var="report" items="${reportList}">
						    <div class="reportList-content">
						        <div class="reportList1">신고번호: ${report.reportNo}</div>
						        <div class="reportList1">신고자 ID: ${report.reportedId}</div>
						        <div class="reportList2">피신고자 ID: ${report.idReported}</div>
						        
						       	
						        <div class="reportList2">피신고자 유저타입: 
						            <c:choose>
						                <c:when test="${report.userType == 1}">관리자</c:when>
						                <c:when test="${report.userType == 2}">일반회원</c:when>
						                <c:when test="${report.userType == 3}">차단회원</c:when>
						                <c:when test="${report.userType == 4}">신고회원</c:when>
						                <c:otherwise>알 수 없음</c:otherwise>
						            </c:choose>
						        </div>		
						       <button type="button" class="reportlist3"
								onclick="location.href='<c:url value='/reportListInfo' />?reportNo=${report.reportNo}&reportedId=${report.reportedId}&idReported=${report.idReported}&userType=${report.userType}'">신고내역</button>
						    </div>
						</c:forEach>
					</form>
			</div>
				<div class="search">
					<select name="searchType">
					    <option value="title" ${param.searchType == 'title' ? 'selected' : ''}>번호</option>
					    <option value="body" ${param.searchType == 'body' ? 'selected' : ''}>제목</option>
					    <option value="user_id" ${param.searchType == 'user_id' ? 'selected' : ''}>작성자</option>
					</select>
					<input type="text" name="search" placeholder="검색어 입력창" value="<%=request.getParameter("search") != null ? request.getParameter("search") : "" %>">
					<input type="submit" value="검색">
				</div>
		</div>
	</div>
</body>
</html>