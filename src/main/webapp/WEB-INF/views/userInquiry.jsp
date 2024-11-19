<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>관리자 페이지</title>
<link rel="stylesheet"
	href="<c:url value='/resources/css/styleYOON.css'/>" />
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script>
    $(document).ready(function() {
        $('#userType').change(function() {
            var userType = $(this).val();
            $.ajax({
                url: '<c:url value="/userInquiry" />', // 서버에서 사용자 정보를 가져오는 URL
                type: 'GET',
                data: { userType: userType },
                success: function(data) {
                    $('#userInfoDisplay').html(data);
                },
                error: function() {
                    $('#userInfoDisplay').html('해당되는 사용자가 없습니다.');
                }
            });
        });
        $('#user_search').on('submit', function(e) {
            e.preventDefault();
            var userId = $('#userIdInput').val();
            $.ajax({
                url: '<c:url value="/userSearch" />', 
                type: 'post',
                data: { id: userId },
                success: function(data) {
                    var str = "";
                    console.log(data);
                    console.log(data.userType);
                    switch (data.userType){
                        case 0: str = '전체'; break;
                        case 1: str = '관리자'; break;
                        case 2: str = '일반회원'; break;
                        case 3: str = '차단회원'; break;
                        case 4: str = '신고회원'; break;
                        default: str = '알 수 없음';
                    }
                    
                    $('#user_list').html(
                            `<div class="user_field">
                                <div class="user_field_in1">\${data.id}</div>
                                <div class="user_field_in2">\${str}</div>
                                <div class="user_field_in3">\${data.createdate}</div>
                            </div>`
                        );
                },
                error: function() {
                    $('#user_list').html('해당되는 사용자가 없습니다.');
                }
            });
        });
        function updateUserField() {
            $('#user_list .user_field').each(function() {
                var userTypeValue = $(this).find('.user_field_in:eq(1)').data('usertype'); // 사용자 유형 데이터 가져오기
                var userTypeText = '';
                switch (userTypeValue) {
                    case 0:
                        userTypeText= '전체';
                        break;
                    case 1:
                        userTypeText = '관리자';
                        break;
                    case 2:
                        userTypeText = '일반회원';
                        break;
                    case 3:
                        userTypeText = '차단회원';
                        break;
                    case 4:
                        userTypeText = '신고회원';
                        break;
                    default:
                        userTypeText = '알 수 없음';
                }
                $(this).find('.user_field_in:eq(1)').text(userTypeText);
            });
        }
    });
</script>
</head>
<body>
	<div class="container">
		<jsp:include page="/WEB-INF/include/header.jsp" />
		<div class="manager-container">
			<div class="manager-head">유저조회목록</div>
			<div class="manager-body">
				<div class="User-select-All">
					<form id="user_Form" action="<c:url value='/userInquiry' />"
						method="get">
						<select id="userType" name="userType" class="userType_select"
							onchange="document.getElementById('user_Form').submit();">
							<option value="0" ${userType == 0 ? 'selected' : ''}>전체</option>
							<option value="1" ${userType == 1 ? 'selected' : ''}>관리자</option>
							<option value="2" ${userType == 2 ? 'selected' : ''}>일반회원</option>
							<option value="3" ${userType == 3 ? 'selected' : ''}>차단회원</option>
							<option value="4" ${userType == 4 ? 'selected' : ''}>신고회원</option>
						</select>
					</form>
					<div class="userInfoDisplay">
						현재 유저타입 :
						<c:choose>
							<c:when test="${userType == 0}">전체회원</c:when>
							<c:when test="${userType == 1}">관리자</c:when>
							<c:when test="${userType == 2}">일반회원</c:when>
							<c:when test="${userType == 3}">차단회원</c:when>
							<c:when test="${userType == 4}">신고회원</c:when>
							<c:otherwise>알 수 없음</c:otherwise>
						</c:choose>
					</div>
				</div>
				<div id="user_list">
					<c:forEach var="user" items="${users}">
						<div class="user_field">
							<div class="user_field_in1">${user.id}</div>
							<div class="user_field_in2">
								<c:choose>
									<c:when test="${user.userType == 0}">전체회원</c:when>
									<c:when test="${user.userType == 1}">관리자</c:when>
									<c:when test="${user.userType == 2}">일반회원</c:when>
									<c:when test="${user.userType == 3}">차단회원</c:when>
									<c:when test="${user.userType == 4}">신고회원</c:when>
									<c:otherwise>알수없음</c:otherwise>
								</c:choose>
							</div>
							<div class="user_field_in3">${user.createdate}</div>
							<button type="button" class="user_field_in"
								onclick="location.href='<c:url value='/userInfo' />?id=${user.id}&userType=${user.userType}&createdate=${user.createdate}'">유저정보</button>
						</div>
					</c:forEach>
				</div>
			</div>
			<form id="user_search" action="<c:url value='/userInquiry' />"
				method="post">
				<input type="text" id="userIdInput" name="id" placeholder="유저 ID 입력">
				<input type="submit" id="userIdInput2" value="검색">
			</form>
		</div>
	</div>
</body>

</html>
