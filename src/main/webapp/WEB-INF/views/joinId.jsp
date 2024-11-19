<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<title>Insert title here</title>
</head>
<body>
	<head>
	<title>아이디 찾기</title>
	<link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>"/>
</head>
<body>
<div class="container">
<%@ include file="/WEB-INF/include/header.jsp" %>
	<div class="joinId-container">
    <h1>아이디 찾기</h1>
    <form action="<c:url value='/joinId' />" method="POST">
        <div class="form-group">
            <label for="contact">연락처</label>
            <div class="input-container">
                <input type="text" id="contact" name="contact" placeholder="휴대폰 번호 또는 이메일을 입력해주세요">
                <input type="button" id="send_pin_btn" value="인증번호 발송">
            </div>
        </div>
        <div class="form-group">
            <label for="pinNumber">인증번호</label>
            <div class="input-container">
                <input type="text" id="pinNumber" name="pinNumber" class="mail-send-input"
                 	placeholder="인증번호 6자리를 입력해주세요!" disabled="disabled" maxlength="6">
                <input type="button" id="pin_valid_btn" value="인증확인">
            </div>
        </div>
        <input type="button" class="submit-btn" id="joinIdOk" value="확인">
        <button type="button" class="submit-btn" onclick="history.back()">취소</button>
    </form>
    </div>
    <div class="footer">
        이미 계정이 있으신가요? <a href="<c:url value='login.do' />">로그인</a>
    </div>
</div>
</body>
<script>

//인증번호 요청 이벤트 및 ajax===============================================================================================
let pinNumber = ""; // 핀 넘버 초기화
let pinCheck = false; // 핀 체크 변수

$("#send_pin_btn").on("click", function() {
    let contact = $("#contact");
    if (contact.val().trim() == "") {
        alert("휴대폰 번호 또는 이메일을 입력해주세요.");
        return;
    }

    // 핸드폰 번호와 이메일 구분
    let isPhone = /^\d{10,11}$/.test(contact.val().trim()); // 핸드폰 번호 정규식
    let isEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(contact.val().trim()); // 이메일 정규식

    if (isPhone) {
        // 핸드폰 번호일 경우
        $.ajax({
            type: "post",
            url: "<c:url value='/phoneVertify'/>",
            data: {
                "receiver": contact.val()
            },
            success: function(data) {
            	// 성공 시 버튼 비활성화
                $("#send_pin_btn").prop("disabled", true);
                pinNumber = data[0];
                console.log(data[0]);
                $("#pinNumber").prop("disabled", false); // 인증번호 입력란 활성화
                alert("인증번호가 핸드폰으로 발송되었습니다."); // 알림 추가
            }
        });
        	return;
    } else if (isEmail) {
        // 이메일일 경우
        $.ajax({
            type: "post",
            url: "<c:url value='/mailSend'/>",
            data: {
                "email": contact.val()
            },
            success: function(data) {
	            	// 성공 시 버튼 비활성화
	                $("#send_pin_btn").prop("disabled", true);
	                pinNumber = data; // 서버에서 반환된 인증번호 저장
	                $("#pinNumber").prop("disabled", false); // 인증번호 입력란 활성화
	                alert("인증번호가 이메일로 발송되었습니다."); // 알림 추가
	             	// 2분 30초 후 인증번호 발송버튼 활성화
		                setTimeout(function() {
		                    $("#send_pin_btn").prop("disabled", false);
		                }, 150000); // 150000ms = 2분 30초
           			}
		});
        	return;
    } else {
        alert("유효한 핸드폰 번호 또는 이메일을 입력해주세요.");
        $("#send_pin_btn").prop("disabled", false); // 버튼 다시 활성화
        $("#contact").val(""); //인증번호 입력란 초기화
        return;
    }
});



//인증번호 확인 이벤트 및 ajax===============================================================================================
$("#pin_valid_btn").on("click", function() {
    let inputPinNumber = $("#pinNumber").val(); //입력된 인증번호
    let email = $("#contact").val().trim(); // 이메일
    console.log("입력된 인증번호:", inputPinNumber); // 인증번호 로그 출력
    console.log("입력된 :", email); // 이메일 로그 출력
   
    // 인증번호가 유효한 숫자인지 확인
    let parsedInputPinNumber = parseInt(inputPinNumber);
    if (isNaN(parsedInputPinNumber)) {
        alert("유효한 인증번호를 입력해주세요.");
        return;
    }
    
    // 인증번호 === 인증번호입력
    if (pinNumber === inputPinNumber) {
        let contact = $("#contact").val().trim();
	        if (/^\d{10,11}$/.test(contact)) {
	            alert("핸드폰 인증이 완료되었습니다."); // 핸드폰 인증 확인 메시지
	        } else {
	            alert("이메일 인증이 완료되었습니다."); // 이메일 인증 확인 메시지
	        } 
	        	pinCheck = true; //인증 확인
	        	return;
    }else{
   		// 인증번호가 다를 경우 알림
   	    alert("입력한 인증번호가 올바르지 않습니다. 다시 시도해주세요.");
   	 	pinCheck = false //인증 미확인
   	 	$("#pinNumber").val(""); //인증번호 입력란 초기화
   	 	return;
    }
	
	
});

//확인 버튼 및 아이디 찾기 이벤트=============================================================================================
$("#joinIdOk").on("click", function(){
	 let inputPinNumber = $("#pinNumber").val(); //입력된 인증번호
	 let contact = $("#contact"); //객체로 변수에 담음
	 
	if (inputPinNumber.trim() == "") {
        alert("인증번호를 입력해주세요.");
        return;
    }
	 
	// 인증 확인 여부 체크
    if (!pinCheck) {
        alert("인증 확인은 필수입니다.");
        return;
    }
	
	$.ajax({
        type: "post",
        url: "<c:url value='/joinId'/>",
        data: {
            "contact": contact.val() // 여기서 contact를 아이디로 사용
        },
        success: function(data) {
        	console.log("서버 응답:", data); // 응답 내용 확인
            if (data.result === "success") {
                alert("아이디는 : " + data.id + "입니다.");
                contact.val(""); //연락처 입력란 초기화
                $("#pinNumber").val(""); //인증번호 입력란 초기화
            } else {
                alert("아이디를 찾을 수 없습니다.");
            }
        },
        error: function(xhr) {
            alert("서버 오류가 발생했습니다.");
            console.log("Error Code:", xhr.status);
        }
    });
    	return;
    	
});



</script>
</html>