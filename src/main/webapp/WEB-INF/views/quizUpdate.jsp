<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script type="text/javascript"
	src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>문제 수정, 삭제 페이지</title>
<link rel="stylesheet"
	href="<c:url value='/resources/css/style.css'/>" />
</head>
<body>
	<div class="container">
		<jsp:include page="/WEB-INF/include/header.jsp" />
		<div class="quiz-container">
			<div class="quizUpdate-head">문제 수정, 삭제 페이지</div>
			<div class="quizUpdate-body">
				<div class="quizUpdate-select-All">
					<form id="quizForm" action="<c:url value='/quizUpdate' />"
						method="get">
						<!-- 퀴즈 유형 선택 시, 서버에 GET 요청을 보내기 위한 폼을 정의합니다. action 속성은 요청이 "/quizUpdate"로 전송됨을 나타냅니다. -->

						<select id="quizType" name="quizType" class="quizUpdate-select"
							onchange="document.getElementById('quizForm').submit();">
							<!-- 퀴즈 유형을 선택할 수 있는 드롭다운 메뉴입니다. 사용자가 선택을 변경하면 폼이 자동으로 제출됩니다. -->

							<option value="word" ${quizType eq 'word' ? 'selected' : ''}>word</option>
							<!-- 'word' 유형이 선택되었을 경우, "selected" 속성을 추가하여 드롭다운에서 기본 선택된 항목으로 표시합니다. -->

							<option value="sentence"
								${quizType eq 'sentence' ? 'selected' : ''}>sentence</option>
							<!-- 'sentence' 유형이 선택되었을 경우, "selected" 속성을 추가하여 드롭다운에서 기본 선택된 항목으로 표시합니다. -->

						</select>
					</form>
					<div class="quiz-category">
						현재 문제타입:
						<c:choose>
							<c:when test="${quizType == 'word'}">word</c:when>
							<c:when test="${quizType == 'sentence'}">sentence</c:when>
							<c:otherwise>알 수 없음</c:otherwise>
						</c:choose>
					</div>
				</div>
				<div class="sample-field">
					<div class="sample-field-in">번호</div>
					<div class="sample-field-in">문제</div>
					<div class="sample-field-in">시간</div>
					<div class="sample-field-hint">힌트1</div>
					<div class="sample-field-hint">힌트2</div>
					<div class="sample-field-hint">힌트3</div>
					<div class="sample-field-in">답</div>
				</div>
				<!-- quiz-container에 새로운 퀴즈 항목이 추가됨 -->
				<div id="quiz-list">
					<!-- 반복문을 사용하여 데이터를 출력 -->
					<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
					<c:forEach var="quiz" items="${quizzes}">
						<div class="quiz-field">
							<div class="quiz-field-in">${quiz.quizNo}</div>
							<!-- 번호 -->
							<div class="quiz-field-in">${quiz.question}</div>
							<!-- 문제 -->
							<div class="quiz-field-in">${quiz.timer}</div>
							<!-- 시간 -->
							<div class="quiz-field-hint">${quiz.hint1}</div>
							<!-- 힌트1 -->
							<div class="quiz-field-hint">${quiz.hint2}</div>
							<!-- 힌트2 -->
							<div class="quiz-field-hint">${quiz.hint3}</div>
							<!-- 힌트3 -->
							<div class="quiz-field-in">${quiz.answer}</div>
							<!-- 답 -->
							<button type="button" class="quiz-field-up"
								onclick="location.href='<c:url value='/quizUpdateInfo' />?quizNo=${quiz.quizNo}&quizType=${quizType}'">수정</button>
							<button type="button" class="quiz-field-del"
								onclick="deleteQuiz(${quiz.quizNo}, '${quizType}')">삭제</button>
							<script type="text/javascript">
						function deleteQuiz(quizNo, quizType) {
   						 if(confirm("정말로 이 문제를 삭제하시겠습니까?")) {
   							location.href = '<c:url value="/quizDelete" />?quizNo=' + quizNo + '&quizType=' + quizType;
   							}
						}
					</script>
						</div>
					</c:forEach>
					<!-- 로딩 스피너 -->
					<div id="loading" style="display: none;">
						<img src="<c:url value='/resources/images/loading.gif'/>"
							alt="Loading...">
					</div>
				</div>
			</div>
			<!-- 검색과 페이징 부분은 그대로 유지 -->
			<div class="search">
				<select name="searchType">
					<option value="title">번호</option>
					<option value="body">제목</option>
				</select> <input type="text" name="search" placeholder="검색어 입력창"> <input
					type="submit" value="검색">
			</div>
		</div>
	</div>

	<!-- 무한 스크롤을 처리하는 JavaScript -->
	<script>
        $(document).ready(function(){
            let page = 1; // 현재 페이지 번호
            let isLoading = false; // 데이터를 로드 중인지 여부

            // 스크롤 이벤트를 감지
            $(window).scroll(function() {
                // 페이지 끝에서 100px 위로 스크롤 되었는지 확인
                if ($(window).scrollTop() + $(window).height() > $(document).height() - 100) {
                    if (!isLoading) { // 현재 로딩 중이 아니면 데이터 로드
                        loadMoreQuizzes(); // 데이터 로드 함수 호출
                    }
                }
            });

            function loadMoreQuizzes() {
                isLoading = true;
                $("#loading").show(); // 로딩 스피너 표시
                page++; // 다음 페이지 번호 증가

                $.ajax({
                	url: '<c:url value="/loadMoreQuizzes" />',
                    method: "GET",
                    data: {
                        page: page, // 서버로 보낼 페이지 번호
                        quizType: "${quizType}" // 현재 퀴즈 유형
                    },
                    success: function(data) {
                        $("#quiz-list").append(data); // 새로운 퀴즈 항목을 추가
                        isLoading = false;
                        $("#loading").hide(); // 로딩 스피너 숨김
                    },
                    error: function() {
                        isLoading = false;
                        $("#loading").hide();
                    }
                });
            }
        });
    </script>
</body>
</html>
