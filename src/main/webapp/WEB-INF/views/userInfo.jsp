<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>유저 상세정보 페이지</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/styleYOON.css'/>"/>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
    $(document).ready(function() {
        $('#userTypeSelect').change(function() {
            var selectedValue = $(this).val();
            var userTypeText = '';

            switch (selectedValue) {
                case '1':
                    userTypeText = '관리자';
                    break;
                case '2':
                    userTypeText = '일반회원';
                    break;
                case '3':
                    userTypeText = '차단회원';
                    break;
                case '4':
                    userTypeText = '신고회원';
                    break;
                default:
                    userTypeText = '알수없음';
            }

            $('#userType').val(userTypeText); // Update the hidden input with the selected user type
        });

        $('.userType-btn').click(function(event) {
            event.preventDefault(); // Prevent default form submission
            var userId = $('#id').val();
            var userType = $('#userTypeSelect').val(); // Get the selected user type
            var createdate = $('#createdate').val();

            // AJAX 요청을 통해 DB에 업데이트 후 이동
            $.ajax({
                url: 'userInfoUpdate', // 사용자 정보를 업데이트하는 URL
                type: 'post', // POST 요청으로 변경
                data: {
                    id: userId,
                    userType: userType,
                    createdate: createdate
                },
                success: function(response) {
                    // 업데이트 성공 후 userInquiry.jsp로 이동
                    window.location.href = '<c:url value="/userInquiry" />';
                    alert('회원정보가 변경되었습니다');
                },
                error: function() {
                    alert('업데이트 중 오류가 발생했습니다.');
                }
            });
        });
    });
    </script>
</head>
<body>
    <div class="container">
        <jsp:include page="/WEB-INF/include/header.jsp" />
        <div class="userInfo-container">
            <div class="userInfo-head">유저 정보</div>
            <div class="userInfo-body">
                <form id="userInfoForm" action="userInfoUpdate" method="post">
                    <div class="userInfo-Info">
                        <label for="id">아이디 :</label>
                        <input type="text" id="id" name="id" value="${user.id}" readonly>
                    </div>
                    <div class="userInfo-Info">
					    <label for="userType">유저 타입 :</label>
					    <input type="text" id="userType" name="userType" 
					    value="${userType == 1 ? '관리자' : userType == 2 ? '일반회원' : userType == 3 ? 
					    '차단회원' : userType == 4 ? '신고회원' : '알수없음'}" readonly/>
					</div>
                    <div class="userInfo-Info">
                        <label for="createdate">가입 날짜 :</label>
                        <input type="text" id="createdate" name="createdate" value="${user.createdate}" readonly>
                    </div>
                    
                    <div class="userInfo-usertype-container">
					    <div class="userInfo-usertype">유저타입</div>
					    <select id="userTypeSelect" name="userTypeSelect" class="userType-select">
					        <option value="1">관리자</option>
					        <option value="2">일반회원</option>
					        <option value="3">차단회원</option>
					        <option value="4">신고회원</option>
					    </select>
					    <div class="userInfo-submit-group">
					        <button type="submit" class="userType-btn">확인</button>
					        <button type="button" class="userType-btn" onclick="history.back()">취소</button>
					    </div>
					</div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>