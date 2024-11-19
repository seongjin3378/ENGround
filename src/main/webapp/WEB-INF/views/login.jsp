<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<head>
<title>로그인</title>
<link rel="stylesheet"
	href="<c:url value='/resources/css/styleKIM.css'/>" />
</head>
<c:if test="${not empty message}">
	<script>
 	 alert("${message}");
 	 
 	</script>
</c:if>
<body>
<script>alert("관리자 계정 id:test0 pw:test");</script>
	<div class="container">
		<%@ include file="/WEB-INF/include/header.jsp"%>
		<div class="login-container">
			<h1>로그인</h1>
			<form action="<c:url value='/login' />" method="post">
				<div class="form-group">
					<label for="id">ID</label> <input type="text" id="id" name="id"
						placeholder="아이디를 입력해주세요">
				</div>
				<div class="form-group">
					<label for="pw">비밀번호</label> <input type="password" id="pw"
						name="pw" placeholder="비밀번호를 입력해주세요">
				</div>
				<button type="submit" class="submit-btn">로그인</button>

				<div class="footer">
					<button type="button" class="submit-btn"
						onclick="location.href='<c:url value='joinId' />'">아이디 찾기</button>
					<button type="button" class="submit-btn"
						onclick="location.href='<c:url value='joinPw' />'">비밀번호
						찾기</button>
					<button type="button" class="submit-btn"
						onclick="location.href='<c:url value='join' />'">회원가입</button>
				</div>
		</div>
	</div>
	</form>
</body>
</html>