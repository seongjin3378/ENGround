<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<title>비밀번호 찾기</title>
</head>
<body>
	<head>
	<title>비밀번호 찾기</title>
	<link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>"/>
</head>
<body>
<div class="container">
<%@ include file ="/WEB-INF/include/header.jsp" %>
	<div class="joinPw-container">
    <h1>비밀번호 찾기</h1>
  	  	<div class="form-group">
            <label for="username">아이디</label>
            <input type="text" id="id" name="id">
        </div>
       <div class="form-group">
            <label for="contact">연락처 (휴대폰 번호 또는 이메일)</label>
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
        <div>
        	<input type="button" class="submit-btn" id="joinPwOk" value="확인">
        	<button type="button" class="submit-btn" onclick="history.back()">취소</button>
		</div>
    </div>
    <div class="footer">
        이미 계정이 있으신가요? <a href="login">로그인</a>
    </div>
</div>

</body>
<script>
// 아이디 중복 체크
let idCheck = false; 
$("#id_btn").on("click", function(){
	let id = $("#id");
	if(id.val().trim() == ""){
		alert("아이디를 입력해주세요.");
		return;
	}
	
	$.ajax({
		type : "post",
		url : "<c:url value='/idCheck'/>",
		data : {
			"id" : id.val()
		},
		success : function(data){
			if(data.result == "failed"){
				idCheck = true;
				alert("아이디 확인 되었습니다.");
			}else{
				idCheck = false;
				alert("없는 아이디입니다.");
			}
		}
	})
});

// 인증번호 발송 이벤트
let pinNumber = "";
let pinCheck = false;

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
                $("#send_pin_btn").prop("disabled", true);
                pinNumber = data[0];
                console.log(data[0]);
                $("#pinNumber").prop("disabled", false); // 인증번호 입력란 활성화
                alert("인증번호가 핸드폰으로 발송되었습니다.");
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
                $("#send_pin_btn").prop("disabled", true);
                pinNumber = data; // 서버에서 반환된 인증번호 저장
                $("#pinNumber").prop("disabled", false); // 인증번호 입력란 활성화
                alert("인증번호가 이메일로 발송되었습니다.");
                setTimeout(function() {
                    $("#send_pin_btn").prop("disabled", false);
                }, 150000); // 2분 30초 후 인증번호 발송버튼 활성화
            }
        });
        return;
    } else {
        alert("유효한 핸드폰 번호 또는 이메일을 입력해주세요.");
        $("#send_pin_btn").prop("disabled", false);
        $("#contact").val(""); // 입력란 초기화
        return;
    }
});

// 인증번호 확인 이벤트
$("#pin_valid_btn").on("click", function() {
    let inputPinNumber = $("#pinNumber").val(); // 입력된 인증번호
    let email = $("#contact").val().trim(); // 이메일

    if (pinNumber === inputPinNumber) {
        let contact = $("#contact").val().trim();
        if (/^\d{10,11}$/.test(contact)) {
            alert("핸드폰 인증이 완료되었습니다.");
        } else {
            alert("이메일 인증이 완료되었습니다.");
        }
        pinCheck = true;
        return;
    } else {
        alert("입력한 인증번호가 올바르지 않습니다. 다시 시도해주세요.");
        pinCheck = false;
        $("#pinNumber").val(""); // 입력란 초기화
        return;
    }
});

// 비밀번호 찾기 완료 버튼 이벤트
$("#joinPwOk").click(function() {
    let id = $("#id");
    let pinNumber = $("#pinNumber");
    let contact = $("#contact");

    if (id.val().trim() == "") {
        alert("아이디는 필수 입력 항목입니다.");
        return;
    }

    if (pinNumber.val().trim() == "") {
        alert("본인인증은 필수입니다.");
        return;
    }

    // 인증 확인 여부 체크
    if (!pinCheck) {
        alert("인증 확인은 필수입니다.");
        return;
    }

    $.ajax({
        type: "post",
        url: "<c:url value='/findPw'/>",
        data: {
            "id": id.val(),
            "contact": contact.val()
        },
        success: function(data) {
            if (data.result == "success") {
                alert("임시 비밀번호가 이메일로 전송되었습니다.");
                id.val("");
                pinNumber.val("");
                contact.val("");
            } else {
                alert(data.message);
            }
        },
        error: function(xhr, status, error) {
            alert("AJAX 에러 발생: " + xhr.status + " - " + error);
            console.log("AJAX 에러 상태: ", status);
            console.log("AJAX 에러 상세: ", error);
        }
    });
});
</script>
</html>
