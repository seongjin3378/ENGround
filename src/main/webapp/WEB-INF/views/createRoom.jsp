<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<link rel="stylesheet" href="<c:url value='/resources/css/styleJANG.css'/>"/>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
</head>
<body>
		<form action="<c:url value='/createRoom' />" method="post">
		<div class="createBody-container" style="margin-left:7%; text-align:center;">
		<h2>방 생성하기</h2>
			<div class="create-body1">
				<label for="roomName">방 이름</label>
				<input type="text" id="roomTitle" name="roomTitle">
			</div>
			<div class="create-body1">
				<label for=numberPeople>인원수</label>
				<input type="text" id="numberPeople">
			</div>
			<div class="create-body1" style="width: 200px;">
			<input type="submit" class="create-btn" value="생성하기"/>
			<button type="button" class="back-btn" onClick="location.reload();">취소</button>
			</div>
		</div>
		</form>
		
</body>
</html>