<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<style>
    .reportListInfo-chatlog {
        max-width: 600px; /* 최대 너비 설정 */
        margin: 20px auto; /* 중앙 정렬 */
        padding: 10px;
        border: 1px solid #ccc; /* 테두리 */
        border-radius: 8px; /* 둥근 모서리 */
        background-color: #f9f9f9; /* 배경색 */
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
        overflow-y: scroll;
    }

    .chat-message {
        background-color: #fff; /* 각 메시지의 배경색 */
        border: 1px solid #e0e0e0; /* 메시지 테두리 */
        border-radius: 5px; /* 메시지 둥근 모서리 */
        padding: 10px; /* 패딩 */
        margin: 5px 0; /* 메시지 간격 줄이기 */
        display: flex; /* 플렉스 박스 사용 */
        justify-content: space-between; /* 내용과 시간 간격 조정 */
        align-items: center; /* 수직 정렬 */
        width: 100%;
    }

    .chat-time {
        font-size: 0.9em; /* 시간 폰트 크기 조정 */
        color: #888; /* 시간 색상 */
    }
</style>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>유저 상세정보 페이지</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/styleYOON.css'/>"/>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
    $(document).ready(function() {
        $('#userTypeSelect').change(function() {
            var userType = $(this).val();
            var userTypeName = '';

            switch (userType) {
                case '1':
                    userTypeName = '관리자';
                    break;
                case '2':
                    userTypeName = '일반회원';
                    break;
                case '3':
                    userTypeName = '차단회원';
                    break;
                case '4':
                    userTypeName = '신고회원';
                    break;
                default:
                    userTypeName = '알 수 없음';
                    break;
            }

            $('#userCase').val(userTypeName); // userCase 업데이트
            $('#userType').val(userType); // hidden input도 업데이트
        });

        $('.userType-btn[type="submit"]').click(function(event) {
            var reportNo = $('#reportNo').val();
            var reportedId = $('#reportedId').val();
            var idReported = $('#idReported').val();
            var userCase = $('#userCase').val();
            var userType = $('#userType').val();

            $.ajax({
                url: '<c:url value="/reportUserTypeUpdate" />',
                type: 'GET',
                data: {
                    reportNo: reportNo,
                    reportedId: reportedId,
                    idReported: idReported,
                    userCase: userCase,
                    userType: userType
                },
                success: function(response) {
                	window.location.href = '<c:url value="/reportList" />';
                    alert('유저 타입이 성공적으로 업데이트되었습니다.');
                    // 필요한 경우 추가 작업 수행
                },
                error: function(xhr, status, error) {
                    alert('오류가 발생했습니다. 다시 시도해 주세요.');
                }
            });
        });
    });
    </script>
</head>
<body>
    <div class="container">
        <jsp:include page="/WEB-INF/include/header.jsp" />
        <div class="reportListInfo-container">
            <div class="reportListInfo-head">신고된 유저 정보</div>
            <div class="reportListInfo-body">
                <form id="reportForm" action ="<c:url value="/reportUserTypeUpdate" />" method ="get"> <!-- form id 추가 -->
                    <div class="reportListInfo-Info">
                        <label for="reportNo">신고 번호:</label>
                        <input type="text" id="reportNo" name="reportNo" value="${vo.reportNo}" readonly>
                    </div>
                    <div class="reportListInfo-Info">
                        <label for="reportedId">신고자:</label>
                        <input type="text" id="reportedId" name="reportedId" value="${vo.reportedId}" readonly>
                    </div>
                    <div class="reportListInfo-Info">
                        <label for="idReported">피신고자:</label>
                        <input type="text" id="idReported" name="idReported" value="${vo.idReported}">
                    </div>
                    <div class="reportListInfo-Info">
                        <label for="userCase">피신고자 유저타입:</label>
                        <input type="text" id="userCase" name="userCase" 
                            value="${vo.userType == 1 ? '관리자' : 
                                   (vo.userType == 2 ? '일반회원' : 
                                   (vo.userType == 3 ? '차단회원' : 
                                   (vo.userType == 4 ? '신고회원' : '알 수 없음')))}" readonly>
                        <input type="hidden" id="userType" name="userType" value="${vo.userType}">
                    </div>                         
	                <div class="reportListInfo-chatlogInfo">신고 채팅 기록</div>
	                <div class="reportListInfo-chatlog"></div>
	                
	                <div class="reportListInfo-group">
	                    <select id="userTypeSelect" name="userTypeSelect" class="reportListInfo-select">
	                        <option value="1" ${vo.userType == 1 ? 'selected' : ''}>관리자</option>
	                        <option value="2" ${vo.userType == 2 ? 'selected' : ''}>일반회원</option>
	                        <option value="3" ${vo.userType == 3 ? 'selected' : ''}>차단회원</option>
	                        <option value="4" ${vo.userType == 4 ? 'selected' : ''}>신고회원</option>
	                    </select>
	                </div>
	               
	                <div class="reportListInfo-submit-group">
	                    <button class="userType-btn" type="submit">확인</button>
	                    <button class="userType-btn" type="button" onclick="history.back()">취소</button>
	                </div>
                </form>
            </div>
        </div>
    </div>
    <script>
    $(document).ready(function() {
        const chatContents = "${vo.chatContent}".split(', ');
        const chatTimes = "${vo.chatTime}".split(', ');
        const length = Math.min(chatContents.length, chatTimes.length);

        for (let index = 0; index < length; index++) {
            const content = chatContents[index].trim(); // chat_content 가져오기
            const time = chatTimes[index].trim(); // 같은 인덱스의 chat_time 가져오기
            
            $('.reportListInfo-chatlog').append(
                "<div class='chat-message'>" +
                    "<span>" + content + "</span><br>" +
                    "<span class='chat-time'>(" + time + ")</span>" +
                "</div>"
            );
        }
    });
    </script>
</body>
</html>
