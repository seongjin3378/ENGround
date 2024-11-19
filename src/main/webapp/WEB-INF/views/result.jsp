<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>게임 결과</title>
<link rel="stylesheet"
	href="<c:url value='/resources/css/styleJANG.css'/>" />
</head>
<!-- 장성진 -->
<body>
	<div class="result-container">
		<div class="game-result-container">
			<div class="result-label">게임 결과</div>
			<div class="result-box">
				<div class="result-score">${correctCounts}/${length}| ${vo.score}점</div>
			</div>
			<div class="result-buttons">
				<button type="button" class="result-button"
					onclick="location.href = location.href;">다시하기</button>
				<button type="button" class="result-button"
					onclick="location.href = '<c:url value="/myPage" />'">마이페이지</button>
			</div>
		</div>
	</div>
</body>
</html>
