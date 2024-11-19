l<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>문제 수정, 삭제 페이지</title>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>"/>
</head>
<body>
	<div class="container">
	<%@ include file="/WEB-INF/include/header.jsp" %>
		<div class="quizInsert-container">
			<div class="quizUpdate-head">문제 추가 페이지</div>
			<div class="quizInsert-body">
				<form id="quizForm" action="/quizInsert" method="get">
					<div class="quizUpdate-select-All">
    				<!-- 퀴즈 유형 선택 시, 서버에 GET 요청을 보내기 위한 폼을 정의합니다. action 속성은 요청이 "/quizUpdate"로 전송됨을 나타냅니다. -->
    			
    					<select id="quizType" name="quizType" class="quizUpdate-select" onchange="resetFormAndSubmit()">
        				<option value="word" ${quizType == 'word' ? 'selected' : ''}>word</option>
        				<option value="sentence" ${quizType == 'sentence' ? 'selected' : ''}>sentence</option>
    					</select>
    				<div class="quiz-category">현재 문제타입:
        			<c:choose>
            			<c:when test="${quizType == 'word'}">word</c:when>
            			<c:when test="${quizType == 'sentence'}">sentence</c:when>
            			<c:otherwise>알 수 없음</c:otherwise>
        			</c:choose>
    				</div>
				</div>
				<div class="Insert-sample-field">
					<div class="sample-field-in">번호</div>
					<div class="sample-field-in">문제</div>
					<div class="sample-field-in">시간</div>
					<div class="sample-field-hint">힌트1</div>
					<div class="sample-field-hint">힌트2</div>
					<div class="sample-field-hint">힌트3</div>
					<div class="sample-field-in">답</div>
					<div class="sample-field-in">카테고리</div>
				</div>
				<div class="Insert-quiz-field">
        			<input type="text" class="quiz-field1-in" name="quizNo" value="" required placeholder="번호를 입력하세요">
        <!-- 문제 입력 필드 -->
        			<input type="text" class="quiz-field1-in" name="question" value="" required placeholder="문제를 입력하세요">
        <!-- 시간 입력 필드 -->
        			<input type="number" class="quiz-field1-in" name="timer" value="" required placeholder="시간을 입력하세요">
        <!-- 힌트1 입력 필드 -->
        			<input type="text" class="quiz-field1-hint" name="hint1" value="" required placeholder="힌트1을 입력하세요">
        <!-- 힌트2 입력 필드 -->
        			<input type="text" class="quiz-field1-hint" name="hint2" value="" required placeholder="힌트2를 입력하세요">
        <!-- 힌트3 입력 필드 -->
        			<input type="text" class="quiz-field1-hint" name="hint3" value="" required placeholder="힌트3을 입력하세요">
        <!-- 답 입력 필드 -->
        			<input type="text" class="quiz-field1-in" name="answer" value="" required placeholder="답을 입력하세요">
        <!-- 카테고리 선택 필드 -->
        			<select id="categoryNo" name="categoryNo" class="quiz-field1-in">
            			<option value="0-1">0-1</option>
            			<option value="0-2">0-2</option>
            			<option value="0-3">0-3</option>
            			<option value="1-1">1-1</option>
        			</select>
        <!-- 추가 버튼 -->
        			<button type="submit" class="quiz-field-Insert">추가</button>
    			</div>
    			</form>
			</div>
		</div>
	</div>
</body>
<script>
$(document).ready(function(){
    $("#quizInsertForm").submit(function(event) {
        let quizNo = $("input[name='quizNo']").val();
        let question = $("input[name='question']").val();
        let timer = $("input[name='timer']").val();
        let hint1 = $("input[name='hint1']").val();
        let hint2 = $("input[name='hint2']").val();
        let hint3 = $("input[name='hint3']").val();
        let answer = $("input[name='answer']").val();
        let categoryNo = $("#categoryNo").val();

        if (!quizNo || !question || !timer || !hint1 || !hint2 || !hint3 || !answer || !categoryNo) {
            alert("모든 필드를 채워주세요.");
            event.preventDefault(); // 폼 제출 중지
        }
    });
});


</script>
</html>