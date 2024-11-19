<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>마이 페이지</title>
<link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>" />
</head>
<body>
	<%@ include file="/WEB-INF/include/AD.jsp"%>
	<div class="container">
		<%@ include file="/WEB-INF/include/header.jsp"%>
		<!-- 헤더 포함 -->
		<div class="mypage-container">
			<div class="user-info">
				<h2>회원정보</h2>
				<form action="updateUser" method="post">
					<div class="info-row">
						<label for="nickname">닉네임</label> <input type="text" id="nick"
							name="nick" placeholder="특수문자를 제외한 4~6자로 입력해주세요"
							value="${sessionScope.user.nick}" oninput="checkNickName(event)"
							maxlength="6" pattern="^[가-힣a-zA-Z]{4,6}$"> <input
							type="button" id="nick_check" value="닉네임 중복체크">
					</div>
					<div class="info-row">
						<label for="id">ID</label> <input type="text" id="id"
							value="${sessionScope.user.id}" readonly>
					</div>
					<div class="info-row">
						<label for="pw">기존 비밀번호</label> <input type="password" id="pw"
							name="pw" placeholder="현재 비밀번호를 입력해주세요" maxlength="16" required>
						<span id="pwFeedback" style="color: red;"></span>
					</div>
					<div class="info-row">
						<label for="passwordValid2">새 비밀번호</label>
						<div class="input-container">
							<input type="password" id="passwordValid" name="passwordValid"
								placeholder="※8-16자 대문자+소문자+숫자+특수문자를 입력해주세요"
								pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?!.*[가-힣])(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,16}$"
								oninput="checkInput(event)" maxlength="16"> <span
								id="pwFeedback2" style="color: red;"></span>
						</div>
					</div>

					<div class="info-row">
						<label for="passwordValid3">새 비밀번호 확인</label>
						<div class="input-container">
							<input type="password" id="passwordValid2" name="passwordValid2"
								placeholder="새 비밀번호 재확인 해주세요" maxlength="16"> <span
								id="pwReFeedback3" style="color: red;"></span>
						</div>
					</div>
					<button type="submit" class="update-button">수정</button>
				</form>
				<c:if test="${not empty message}">
					<script type="text/javascript">
						alert("${message}"); // 서버에서 전달된 메시지를 alert로 띄움
					</script>
				</c:if>
			</div>
			<div class="recent-scores">
				<h2>최근 전적</h2>
				<c:forEach var="history" items="${historyList}">
					<div class="score-row">
						<span class="history-text">유저:${history.userId} | 점수(${history.recordCount}):
							${history.score} | 시간: ${history.time}</span>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
</body>
<script>
	//닉네임 정규식표현 ajax===============================================================================================
	//닉네임 길이제한 및 정규식표현
	function checkNickName(event) {
		const nick = document.getElementById("nick");
		// 길이 제한 검사
		if (nick.value.length == 6) {
			alert("닉네임은 6자 이하로 입력해주세요.");
		}

		if (/[^ㄱ-ㅎ가-힣a-zA-Z]/.test(nick.value)) {
			alert("닉네임은 한글과 영어 대문자,소문자만 사용할 수 있습니다."); // 경고창 띄우기
			nick.value = nick.value.replace(/[^ㄱ-ㅎ가-힣a-zA-Z]/g, ''); // 한글 및 영어 대문자, 소문자 외의 문자 제거
		}
	}

	//닉네임 중복체크 이벤트 밑 ajax==============================================================================================
	let nickCheck = false;
	$("#nick_check").on("click", function() {
		let nick = $("#nick");
		if (nick.val().trim() == "") {
			alert("닉네임을 입력해주세요.");
			return;
		}

		let pattern = /^[a-zA-Z0-9가-힣]{4,6}$/
		if (!pattern.test(nick.val().trim())) {
			alert("닉네임은 4-6자의 한글로만 이루어져야 합니다.");
			nick.val("");
			return;
		}

		$.ajax({
			type : "post",
			url : "<c:url value='/nickCheck'/>",
			data : {
				"nick" : nick.val()
			},
			success : function(data) {
				if (data.result == "success") {
					console.log(data)
					nickCheck = true;
					alert("사용 가능한 닉네임입니다.");
				} else {
					nickCheck = false;
					alert("중복된 닉네임 입니다.");
				}
			}
		})
	})

	//비밀번호 정규표현식 및 유효성검사===============================================================================================
	$("#pw")
			.on(
					"input",
					function() {
						let pw = $(this); // 현재 입력된 비밀번호 필드를 선택
						let pattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,16}$/;
						let value = pw.val().trim();
						let feedback = $("#pwFeedback"); // 피드백 메시지 요소 선택
						console.log(pw.val().trim()); // 입력된 비밀번호 확인

						if (value.length < 8) {// 8자 이상 넘어갈 시 정규표현식인지 체크
							feedback.text(''); // 피드백 초기화
							return;
						}

					});

	//새 비밀번호 정규표현식 및 유효성검사===============================================================================================
	$("#passwordValid")
			.on(
					"input",
					function() {
						let passwordValid = $(this); // 현재 입력된 비밀번호 필드를 선택
						let pattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,16}$/;
						let value = passwordValid.val().trim();
						let feedback = $("#pwFeedback2"); // 피드백 메시지 요소 선택
						console.log(passwordValid.val().trim()); // 입력된 비밀번호 확인

						if (value.length < 8) {// 8자 이상 넘어갈 시 정규표현식인지 체크
							feedback.text(''); // 피드백 초기화
							return;
						}

						if (!pattern.test(value)) {
							feedback
									.text('비밀번호는 ※8-16자 대문자+소문자+숫자+특수문자를 입력해주세요');
							feedback.css('color', 'red'); // 부적합 시 빨간색
						} else {
							feedback.text('비밀번호가 적합합니다.'); // 적합 시 메시지
							feedback.css('color', 'green'); // 적합 시 초록색
						}

					});

	//새 비밀번호 확인 일치여부
	$("#passwordValid2").on("input", function() {
		let passwordValid = $("#passwordValid").val().trim(); // 새 비밀번호 값
		let passwordValid2 = $(this).val().trim(); // 새 비밀번호 확인 값
		let pwReFeedback = $("#pwReFeedback3");

		if (passwordValid2 === "") {
			pwReFeedback.text(""); // 비밀번호 재확인 필드가 비어있을 때 초기화
			return;
		}

		if (passwordValid !== passwordValid2) {
			pwReFeedback.text('비밀번호가 일치하지 않습니다.');
			pwReFeedback.css('color', 'red');
		} else {
			pwReFeedback.text('비밀번호가 일치합니다.');
			pwReFeedback.css('color', 'green');
		}
	});

	//회원가입 비밀번호 재확인 한글 입력 방지 이벤트=================================================================================
	document.getElementById('passwordValid').addEventListener('keypress',
			function(event) {
				if (/[\uac00-\ud7af]/.test(inputValue)) {
					event.preventDefault();
					alert("한글 입력이 불가능합니다.");
					this.value = inputValue.replace(/[\uac00-\ud7af]/g, ''); // 한글 제거
				}
			});
</script>
</html>
