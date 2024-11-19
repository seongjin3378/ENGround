<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/include/side.jsp"%>
<%@ include file="/WEB-INF/include/AD.jsp"%>
<script
	src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script type="text/javascript"
	src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>대기방</title>
<script type="text/javascript"
	src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<link rel="stylesheet"
	href="<c:url value='/resources/css/styleJANG.css'/>" />
</head>
<body>


	
	<div class="back-container">
		<%@ include file="/WEB-INF/include/header.jsp"%>
		<div class="wait-head">
			<h2>"${sessionScope.roomTitle}"</h2>
		</div>
		<div class="wait-container">
			<div class="waitUser-body">
				<div class="waitUser-userboard">

					<div class="wait-board-name">
						<label for="room-name">제목</label><br> <input type="text"
							id="roomName" name="roomName" value="제목">
					</div>
					<div class="wait-board-name">
						<label for="timer">타이머</label><br> <input type="text"
							id="timer" name="timer" value="60">
					</div>
					<div class="wait-board-name">
						<label for="room-cate">카테고리</label><br> <select id="category"
							name="cateType">
							<c:forEach var="item" items="${category}">
								<option value="${item.categoryName}"
									${cateType eq "${item.categoryName}" ? 'selected' : ''}>${item.categoryName}</option>
							</c:forEach>
						</select>

					</div>


				</div>
			</div>
			<div class="wait-chatlogBox">
				<div class="wait-chatlog">
				<c:forEach var="item" items="${chatHistoryList}">
					<div class="chatMessage">${item.userId }: ${item.content }</div>
				</c:forEach>
				</div>
				<div class="wait-answer-container">
					<input type="text" class="wait-input2" id="wait-input-answer"
						placeholder="입력 영역"> <span id="wait-answer-result"
						style="display: none"></span> <input type="button"
						class="wait-answer-btn" value="입력">

					<c:choose>
						<c:when test="${sessionScope.host == true}">
							<button class="wait-ready-btn" id="start-button">시작</button>

						</c:when>
						<c:otherwise>
							<button class="wait-ready-btn" id="ready-button">레디</button>

						</c:otherwise>
					</c:choose>
					<!--  <input type="button" class="wait-ready-btn" value="레디"  onclick="location.href ='/onlineGame'">-->
				</div>
			</div>
		</div>
	</div>

	<!-- 헤더 포함 -->
</body>



<!-- 강퇴는 나중에 넣음 -->
<script>
	function updateHostSession() {
		var sessionHost = "${sessionScope.host}";
		return sessionHost;
	}

	const sessionId = "${sessionScope.user.id}";
	const roomIdGlobal = "${roomId}";
	var enterMessages = {
		roomId : null, // 초기값 설정
		userId : null, // 초기값 설정
		message : null
	// 초기값 설정
	};
	let fullTimerGlobal = 0;
	let banListGlobal = [];
</script>
<script type="module"
	src="<c:url value='/resources/js/lobbyStomp.js' />" defer></script>

</html>