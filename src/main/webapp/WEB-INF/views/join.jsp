<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원가입</title>
<link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>"/>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>

<body>  
<div class="container">
<%@ include file ="/WEB-INF/include/header.jsp" %>

<div class="join-container">
    <h1>회원가입</h1>
     <form action="join" method="post">
        <div class="form-group">
            <label for="username">아이디</label>
            <div class="input-container">
                <input type="text" id="id" name="id" placeholder="※6-12자 소문자+숫자를 입력해주세요"
                	pattern="^(?=.*[a-z])(?=.*\d)[a-z0-9]{6,12}$" 
                	oninput="checkInput(event)" maxlength="13">
                <input type="button" id="id_btn" value="ID중복체크">
            </div>
        </div>
        
        <div class="form-group">
            <label for="password">비밀번호</label>
            <div class="input-container">
                <input type="password" id="pw" name="pw" placeholder="※8-16자 대문자+소문자+숫자+특수문자를 입력해주세요"
                	pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?!.*[가-힣])(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{8,16}$"
                	oninput="checkInput(event)" maxlength="16">
                	<span id="pwFeedback" style="color: red;"></span>
            </div>
        </div>
        
        <div class="form-group">
            <label for="passwordValid">비밀번호 확인</label>
            <div class="input-container">
                <input type="password" id="passwordValid" name="passwordValid"  placeholder="비밀번호 재확인 해주세요" maxlength="16">
                <span id="pwReFeedback" style="color: red;"></span>
            </div>
        </div>
        
        <div class="form-group">
            <label for="nick">닉네임</label>
            <div class="input-container">
                <input type="text" id="nick" name="nick" placeholder="특수문자를 제외한 4~6자 입력해주세요" 
                oninput="checkNickName(event)" maxlength="6" pattern="^[ㄱ-ㅎ가-힣a-zA-Z0-9]^[가-힣a-zA-Z]{4,6}$">
                <input type="button" id="nick_check" value="닉네임 중복체크">
            </div>
        </div>
        
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
   	
  
        <div class="form-group1">
        	<input type="button" class="submit-btn" id="join-btn" value="가입하기">
        	<button type="button" class="submit-btn" onclick="history.back()">취소</button>
        </div>
    </form>
</div>
    <div class="footer">
        이미 계정이 있으신가요? <a href="login">로그인</a>
    	</div>
   	</div>
</body>
<script>
//회원가입 시 아이디, 비밀번호 한글 입력 방지 이벤트===============================================================================================
function checkInput(event) {
	const id = document.getElementById("id"); // input 변수 정의
	const pw = document.getElementById("pw"); // input 변수 정의
	// 입력값에 한글이 포함되어 있는지 확인
    if (/[^a-z0-9]/.test(id.value)) {
        alert("아이디는 소문자와 숫자만 사용할 수 있습니다."); // 경고창 띄우기
        id.value = id.value.replace(/[^a-z0-9]/g, ''); // 한글 제거
    }else if(id.value.length > 12) {
        alert("아이디는 12자 이하로 입력해주세요."); // 경고창 띄우기
        id.value = id.value.substring(0, 12); // 12자 초과 시 잘라내기
    }
	// 입력값에 한글이 포함되어 있는지 확인
    if (pw.value.length == 16) {
        alert("비밀번호는 16자 이하로 입력해주세요."); // 경고창 띄우기
        pw.value = pw.value.substring(0, 15); // 16자 초과 시 잘라내기
    }
}

//아이디 중복체크 및 정규식표현 ajax===============================================================================================
let idCheck = false; 
$("#id_btn").on("click", function(){
	let id = $("#id");
	if(id.val().trim() == ""){
		alert("아이디를 입력해주세요.");
		return;
	}
	
	let pattern = /^(?=.*[a-z])(?=.*\d)(?!.*[가-힣])[a-z0-9]{6,12}$/;
	if(!pattern.test(id.val().trim())) {
		alert("아이디는 8-12자의 소문자와 숫자로만 이루어져야 합니다.");
		return;
	}
	
	$.ajax({
		type : "post",
		url : "<c:url value='/idCheck'/>",
		data : {
			"id" : id.val()
		},
		success : function(data){
			if(data.result == "success"){
				idCheck = true;
				alert("회원가입이 가능한 아이디입니다.");
			}else{
				idCheck = false;
				alert("중복된 아이디입니다.");
				id.val("");
			}
		}
	})
})

//비밀번호 정규표현식 및 유효성검사===============================================================================================
$("#pw").on("input", function() {
	let pw = $(this); // 현재 입력된 비밀번호 필드를 선택
	let pattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()\-=_+<>?,./;':"[\]{}|\\~`])[A-Za-z\d!@#$%^&*()\-=_+<>?,./;':"[\]{}|\\~`]{8,16}$/
	let value = pw.val().trim();
	let feedback = $("#pwFeedback"); // 피드백 메시지 요소 선택
	console.log(pw.val().trim()); // 입력된 비밀번호 확인
	
	if (value.length < 1) {// 8자 이상 넘어갈 시 정규표현식인지 체크
		feedback.text(''); // 피드백 초기화
        return; 
    }
	
	if (!pattern.test(value)) {
        feedback.text('비밀번호는 ※8-16자 대문자+소문자+숫자+특수문자를 입력해주세요');
        feedback.css('color', 'red'); // 부적합 시 빨간색
    } else {
        feedback.text('비밀번호가 적합합니다.'); // 적합 시 메시지
        feedback.css('color', 'green'); // 적합 시 초록색
    }
	
	
});
//비밀번호 재확인 일치여부===============================================================================================
$("#passwordValid").on("input", function(){
	 let pw = $("#pw").val().trim();
	 let passwordValid = $(this).val().trim();
	 let pwReFeedback = $("#pwReFeedback");
	 
	 if (passwordValid === "") {
		pwReFeedback.text(""); // 비밀번호 재확인 필드가 비어있을 때 초기화
        return;
	 }
	 
	 if (passwordValid !== pw) {
		 pwReFeedback.text('비밀번호가 일치하지 않습니다.');
		 pwReFeedback.css('color', 'red');
	 } else {
		 pwReFeedback.text('비밀번호가 일치합니다.');
		 pwReFeedback.css('color', 'green');
	 }
})
//회원가입 비밀번호 재확인 한글 입력 방지 이벤트=================================================================================
document.getElementById('passwordValid').addEventListener('keypress', function(event) {
    if (/[\uac00-\ud7af]/.test(inputValue)) {
        event.preventDefault();
        alert("한글 입력이 불가능합니다.");
        this.value = inputValue.replace(/[\uac00-\ud7af]/g, ''); // 한글 제거
    }
});
	


//닉네임 정규식표현 ajax===============================================================================================
//닉네임 길이제한 및 정규식표현
function checkNickName(event) {
    const nick = document.getElementById("nick");
    // 길이 제한 검사
    if (nick.value.length == 6) {
    	alert("닉네임은 6자 이하로 입력해주세요.");
    }
    
    if (/[^ㄱ-ㅎ가-힣a-zA-Z0-9]/.test(nick.value)) {
        alert("닉네임은 특수문자를 제외한 4-6자를 입력해주세요."); // 경고창 띄우기
        nick.value = nick.value.replace(/[^ㄱ-ㅎ가-힣a-zA-Z0-9]/g, ''); // 한글 및 영어 대문자, 소문자 외의 문자 제거
    }
}
//닉네임 중복체크 이벤트 밑 ajax==============================================================================================
let nickCheck = false;
$("#nick_check").on("click", function(){
	let nick = $("#nick");
	if(nick.val().trim() == ""){
		alert("닉네임을 입력해주세요.");
		return;
	}
	
	let pattern=/^[ㄱ-ㅎ가-힣a-zA-Z0-9]{4,6}$/
	if(!pattern.test(nick.val().trim())) {
		alert("닉네임은 특수문자를 제외한 4-6자를 입력해주세요.");
		nick.val("");
		return;
	}
	
	$.ajax({
		type : "post",
		url : "<c:url value='/nickCheck'/>",
		data : {
			"nick" : nick.val()
		},
		success : function(data){
			if(data.result == "success"){
				console.log(data)
				nickCheck = true;
				alert("사용 가능한 닉네임입니다.");
			}else{
				nickCheck = false;
				alert("중복된 닉네임 입니다.");
			}
		}
	})
})

//인증번호 요청이 벤트 및 ajax===============================================================================================
let pinNumber = "";
let pinCheck = false;
let joinIdCheck = false;

$("#send_pin_btn").on("click", function() {
    let contact = $("#contact");
    if (contact.val().trim() == "") {
        alert("휴대폰 번호 또는 이메일을 입력해주세요.");
        return;
    }

    // 핸드폰 번호와 이메일 구분
    let isPhone = /^\d{10,11}$/.test(contact.val().trim()); // 핸드폰 번호 정규식
    let isEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(contact.val().trim()); // 이메일 정규식

    // 중복 체크 AJAX 요청
    $.ajax({
        type: "post",
        url: "<c:url value='/joinIdCheck'/>", // 중복 체크를 위한 URL
        data: {
            "contact": contact.val().trim()
        },
        success: function(data) {
            if (data.result === "success") {
            	joinIdCheck = true;
                alert("사용 가능한 연락처 입니다.");
			}else{
				joinIdCheck = false;
				alert("이미 사용중인 연락처 입니다.")
				contact.val("");
				return;
			}

            // 중복 체크 후 핸드폰 번호 또는 이메일로 인증번호 발송
            if (isPhone) {
                $.ajax({
                    type: "post",
                    url: "<c:url value='/phoneVertify'/>",
                    data: {
                        "receiver": contact.val()
                    },
                    success: function(data) {
                        pinNumber = data[0];
                        console.log(data[0]);
                        $("#pinNumber").prop("disabled", false); // 인증번호 입력란 활성화
                        alert("인증번호가 핸드폰으로 발송되었습니다.");
                    }
                });
            } else if (isEmail) {
                $.ajax({
                    type: "post",
                    url: "<c:url value='/mailSend'/>",
                    data: {
                        "email": contact.val()
                    },
                    success: function(data) {
                        pinNumber = data; // 서버에서 반환된 인증번호 저장
                        $("#pinNumber").prop("disabled", false); // 인증번호 입력란 활성화
                        alert("인증번호가 이메일로 발송되었습니다.");
                    }
                });
            } else {
                alert("유효한 핸드폰 번호 또는 이메일을 입력해주세요.");
            }
        },
        error: function() {
            alert("오류가 발생했습니다. 다시 시도해주세요.");
        }
    });
});
	
//인증번호 확인 이벤트 및 ajax===============================================================================================
$("#pin_valid_btn").on("click", function() {
    let inputPinNumber = $("#pinNumber").val(); //입력된 인증번호
    let email = $("#contact").val().trim(); // 이메일
    console.log("입력된 인증번호:", inputPinNumber); // 인증번호 로그 출력
    console.log("입력된 :", email); // 이메일 로그 출력
    
    
    if (inputPinNumber.trim() == "") {
        alert("인증번호를 입력해주세요.");
        return;
    }
 	
    // 인증번호가 유효한 숫자인지 확인
    let parsedInputPinNumber = parseInt(inputPinNumber);
    if (isNaN(parsedInputPinNumber)) {
        alert("유효한 인증번호를 입력해주세요.");
        return;
    }
   
    if (pinNumber === inputPinNumber) {
        let contact = $("#contact").val().trim();
        if (/^\d{10,11}$/.test(contact)) {
            alert("핸드폰 인증이 완료되었습니다."); // 핸드폰 인증 확인 메시지
        } else {
            alert("이메일 인증이 완료되었습니다."); // 이메일 인증 확인 메시지
        }
        pinCheck = true;
        return;
    }else{
   		// 인증번호가 다를 경우 알림
   	    alert("입력한 인증번호가 올바르지 않습니다. 다시 시도해주세요.");
   	 	pinCheck = false;
   	 	$("#pinNumber").val(""); //인증번호 입력란 초기화
   	 	return;
    }
    
    
    // 이메일 인증번호 확인
    $.ajax({
        type: "post",
        url: "<c:url value='/mailCheck'/>",
		data: {
            "email": email,
            "inputAuthNumber": parseInt(inputPinNumber)
        },
        success: function(response) {
            alert("본인인증 되었습니다."); // 인증 성공 메시지 출력
            pinCheck = true;
        },
        error: function(xhr) {
            alert("인증에 실패하셨습니다 다시 인증해주세요."); // 인증 실패 메시지 출력
            console.log("Error Code:", xhr.status); // 에러 코드 출력
            console.log("Error Response:", xhr.responseText); // 에러 응답 출력
        }
    });
});



//회원가입 시 공백체크 알림===============================================================================================
function fillBlankAlert(element, message, element2)
{
	if(element.val().trim() == "" || (element2 != false && element.val().trim() != element2.val().trim()) ){
		alert(message)
		element.focus();
		return false;
	}
	return true
}
//가입하기 버튼 이벤트 및 공백 알림 이벤트======================================================================================
$("#join-btn").click(function(){
	let id = $("#id");
	let pw = $("#pw");
	let passwordValid = $("#passwordValid");
	let nick = $("#nick");
	let contact = $("#contact")
	let pinNumber = $("#pinNumber")
	
	if(!idCheck) {
       	alert("아이디 중복 체크를 해주세요.");
       	return false;
  		}
	
	if(!nickCheck) {
        alert("닉네임 중복 체크를 해주세요.");
        return false;
   	}
	
	if(!pinCheck){
		alert("인증 확인은 필수입니다.")
		pinNumber.val("");
		return;
	}
	
	if(!fillBlankAlert(id, "아이디는 필수 입력 항목입니다", false) 
		|| !fillBlankAlert(pw, "비밀번호는 필수 입력 항목 입니다.", false) 
		|| !fillBlankAlert(pw, "비밀번호 확인에 실패하셨습니다.", passwordValid, false) 
		|| !fillBlankAlert(nick, "닉네임은 필수 입력 항목 입니다.", false) 
		|| !fillBlankAlert(pinNumber, "본인 인증을 해주세요.", false)){
			return false;	
		}
	  $("form").submit();
})




</script>

</html>