<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<script type="text/javascript"
	src="http://code.jquery.com/jquery-3.5.1.min.js"></script>

<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
<head>
<meta charset="UTF-8">
<!-- <meta name="viewport" content="width=device-width, initial-scale=1.0"> -->
<title>게임 페이지</title>

<link rel="stylesheet"
	href="<c:url value='/resources/css/styleJANG.css'/>" />

</head>
<body>

	<%@ include file="/WEB-INF/include/header.jsp"%>
	<div id="fullTimer" style="display: none;"></div>
	<div class="lobby-head">
		<h2>온라인 대전</h2>
	</div>

		<div class="modal fade" id="myModal" tabindex="-1"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content" style="margin-top: 50%">

					<div class="modal-body" id="modalContent">
						<!-- 여기서 HTML 내용이 표시됩니다. -->
					</div>

				</div>
			</div>
		</div>
	<div class="onlineGame-container">
		<div class="onlineGame-header">
			<span class="onlineGame-score"><span id="correct-question">0</span>/10
				| <span id="myScore">0</span>점</span>
		</div>
		<div class="onlineGame-input-container">
			<div class="onlineGame-timer" id="timer">0</div>
			<div class="onlineGame-input" id="current-question">0</div>
			<div class="onlineGame-input-box" id="answer-blank"></div>
			<button class="onlineGame-button" id="play-sound">▶</button>
		</div>
		<div class="online-hint-container">
			<button class="online-hint-button" id="hint1">힌트1</button>
			<button class="online-hint-button" id="hint2">힌트2</button>
			<button class="online-hint-button" id="hint3">힌트3</button>
		</div>
		<div class="onlineGame-chatlogBox">
			<div class="onlineGame-chatlog"></div>
			<div class="onlineGame-answer-container">
				<input type="text" class="wait-input2" id="wait-input-answer"
					placeholder="입력 영역"> <span id="onlineGame-answer-result"
					style="display: none"></span> <input type="button"
					class="wait-answer-btn" value="입력">
			</div>
		</div>

	</div>
</body>

<script>
	var categoryGlobal = "${sessionScope.categoryName}";

	function updateHostSession() {
		var sessionHost = "${sessionScope.host}";
		return sessionHost;
	}
	
</script>


<script type="module" src="<c:url value='/resources/js/gameStomp.js' />"
	defer></script>
</html>

