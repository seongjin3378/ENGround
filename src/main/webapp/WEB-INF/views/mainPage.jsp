<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="true"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript"
	src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>메인 페이지</title>
<link rel="stylesheet"
	href="<c:url value='/resources/css/styleYOON.css'/>" />
	
</head>
<c:if test="${not empty message}">
	<script>
		alert("${message}");
	</script>
</c:if>
<body>
	<%@ include file="/WEB-INF/include/AD.jsp"%>
	<script>
		$(document).ready(function() {
			$(document).on("click", ".main-button", function() {
				console.log(this);
				console.log($(this).find("input").val());
				val = $(this).find("input").val();
				if (val == "practice") {
					location.href = '<c:url value="/practice" />';
				}
				$.ajax({
					url : '<c:url value="/setGameModeSession" />', // 세션을 설정할 서버 엔드포인트
					method : 'POST',
					data : {
						gameMode : val
					},
					success : function(response) {
						if (val == "word") {
							location.href = '<c:url value="/category" />';
						} else if (val == "sentence") {
							location.href = '<c:url value="/game/sentence" />';
						} else if (val == "online") {
							location.href = '<c:url value="/lobby" />';
						}
					}
				});
			});
		});
	</script>
	<div class="container">
		<%@ include file="/WEB-INF/include/header.jsp"%>
		<!-- 헤더 포함 -->
		<div class="main-image">EN-Ground</div>
		<div class="button-grid">
			<div class="main-button">
				낱말 <input type="hidden" value="word" />
			</div>

			<div class="main-button">
				문장 <input type="hidden" value="sentence" />
			</div>
			<div class="main-button">
				온라인 <input type="hidden" value="online" />
			</div>
			<div class="main-button">
				연습 <input type="hidden" value="practice" />
			</div>
		</div>
	</div>
</body>
</html>
