<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>멀티 로비</title>
<script type="text/javascript"
	src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<link rel="stylesheet"
	href="<c:url value='/resources/css/styleJANG.css'/>">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<!-- 장성진 -->
	<div class="modal fade" id="myModal" tabindex="-1"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content"
				style="margin-top: 50%; background-color: transparent; border: none;">

				<div class="modal-body" id="modalContent"
					style="justify-content: center;">
					<!-- 여기서 HTML 내용이 표시됩니다. -->
				</div>

			</div>
		</div>
	</div>
	<div class="back-container">
		<%@ include file="/WEB-INF/include/header.jsp"%>
		<div class="lobby-head">
			<h2>멀티플레이</h2>
		</div>
		<div class="lobby-container">
			<button class="Refresh-btn"></button>
			<div class="room-list"></div>

		</div>
		<button class="create-room" onclick="createRoom_Ajax()">방
			생성하기</button>
	</div>

	<script>
		function navigateToCategory(navigateToCategory) {
			window.location.href = '<c:url value="/" />' + waitingRoom;
		}
	</script>

	<script>
		function joinRoom_Ajax(roomIdArg, roomName) {
			$.ajax({
				type : "GET",
				url : '<c:url value="/room/userEnterRoom" />',
				data : {
					"roomId" : roomIdArg,
					"roomTitle" : roomName
				},
				success : function(data) {
					location.href = '<c:url value="/room/" />' + roomIdArg;
				}
			});
		}

		// 게임에 나가거나 새로고침 했을 경우
		function findAllRoom_Ajax() {
			$
					.ajax({
						type : "get",
						url : '<c:url value="/findAllRoom" />',
						success : function(data) {
							console.log(data);
							data
									.forEach(function(datum) {
										let roomName = datum.roomTitle;
										let userId = datum.userId;
										let roomId = datum.roomId;

										if (roomName.indexOf("\"") > -1) {
											roomName = roomName.replaceAll(
													"\"", "\\\"");
										}
										$(".room-list")
												.append(
														"<div class='lobby-item'><span class='lobby-text'>"
																+ roomName
																		.replaceAll(
																				"\\\"",
																				"\"")
																+ "</span><button class='lobby-next' onclick='joinRoom_Ajax(\""
																+ roomId
																+ "\", \""
																+ roomName
																+ "\")'>이동</button></div>");
									});
						}
					});
			$('#myModal').modal({ show: false });
		}
		function delay(ms) {
		    return new Promise(resolve => setTimeout(resolve, ms));
		}
		function createRoom_Ajax() {
			$.ajax({
				type : "get",
				url : '<c:url value="/createRoomModal" />',
				success : function(data) {
					console.log(data);
					if (typeof data === 'string'
							&& data.indexOf('<h2>방 생성하기</h2>') !== -1) {
						$('#modalContent').html(data); // 모달에 HTML 삽입
						delay(50).then(() => {
						$('#myModal').modal('show');
						});
					}
				}
			});
		}

		$(".Refresh-btn").on("click", function() {
			$(".room-list").empty();
			findAllRoom_Ajax();
		});

		$(function() {
			findAllRoom_Ajax();
		});
	</script>
	<%@ include file="/WEB-INF/include/AD.jsp"%>
</body>
</html>