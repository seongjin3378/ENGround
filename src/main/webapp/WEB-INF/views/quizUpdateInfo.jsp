<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>문제 수정 페이지</title>
<link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>"/>
</head>
<body>
    <div class="container">
        <jsp:include page="/WEB-INF/include/header.jsp" />
        <div class="userInfo-container">
            <div class="userInfo-head">문제 정보</div>
            <div class="userInfo-body">
                <form action="<c:url value='/quizUpdateSubmit' />" method="get">
                    <div class="userInfo-Info">
                        <label for="quizNo">번호</label>
                        <input type="text" id="quizNo" name="quizNo" value="${quiz.quizNo}" readonly>
                    </div>
                    <div class="userInfo-Info">
                        <label for="question">문제</label>
                        <input type="text" id="question" name="question" value="${quiz.question}">
                    </div>
                    <div class="userInfo-Info">
                        <label for="timer">시간</label>
                        <input type="text" id="timer" name="timer" value="${quiz.timer}">
                    </div>
                    <div class="userInfo-Info">
                        <label for="hint1">힌트1</label>
                        <input type="text" id="hint1" name="hint1" value="${quiz.hint1}">
                    </div>
                    <div class="userInfo-Info">
                        <label for="hint2">힌트2</label>
                        <input type="text" id="hint2" name="hint2" value="${quiz.hint2}">
                    </div>
                    <div class="userInfo-Info">
                        <label for="hint3">힌트3</label>
                        <input type="text" id="hint3" name="hint3" value="${quiz.hint3}">
                    </div>
                    <div class="userInfo-Info">
                        <label for="answer">답</label>
                        <input type="text" id="answer" name="answer" value="${quiz.answer}">
                    </div>
                    <div class="userInfo-Info">
                        <label for="categoryNo">카테고리</label>
                        <input type="text" id="categoryNo" name="categoryNo" value="${quiz.categoryNo}">
                    </div>
                    </div>
                    <div class="userInfo-submit-group">
                        <button type="submit" class="userType-btn">업데이트</button>
                        <button type="button" class="userType-btn" onclick="history.back()">취소</button>
               		</div>
              </form> 
        </div>
    </div>
</body>
</html>
