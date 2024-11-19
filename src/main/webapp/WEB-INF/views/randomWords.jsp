<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Archivo+Black&display=swap" rel="stylesheet">
<title>단어 뿌시기 게임</title>
<style>

													
	html,body {
		font-family: "Archivo Black", sans-serif;
	    font-weight: 400;
	    font-style: normal;
	    color : white;
	    
        background-image: url("resources/css/gameBackground.gif");
        background-size: cover; /* 배경 이미지 크기 조정 (전체 화면에 맞춤) */
	    background-position: center center; /* 배경 이미지 위치 조정 (중앙 정렬) */
	    background-repeat: no-repeat; /* 배경 이미지 반복 방지 */
	    height: 100vh; /* 높이는 자동으로 조정 */
	    
        text-align: center; /* 중앙 정렬 */
        display: flex; /* Flexbox 사용 */
    	flex-direction: column; /* 세로 방향 정렬 */
    	justify-content: center; /* 수직 중앙 정렬 */
    	user-select: none; /* 마우스 드레그 금지 */
    	overflow: hidden; /* 스크롤바 제거 */
    }
    
    
    h1 {
	    color: #ff6f61; /* 제목 색상 */
	    font-size: 4em; /* 제목 크기 */
	    margin: 20px 0; /* 여백 */
	    position: fixed; /* 고정 위치 설정 */
	    top: 20px; /* 화면 상단에서의 거리 */
	    left: 50%; /* 화면 중앙 */
	    transform: translateX(-50%); /* 중앙 정렬을 위한 변환 */
	    z-index: 10; /* 다른 요소 위에 위치하도록 설정 */
    }
	
	.energy-bar {
		
	    position: absolute; /* 절대 위치 설정 */
	    text-align: center; /* 텍스트 중앙 정렬 */
	    line-height: 50px; /* 텍스트 수직 정렬 */
	    top: 0px; /* 상단 여백 */
	    left: 100px; /* 왼쪽 여백 */
	    width: 200%; /* 바의 너비 */
	    height: 50px; /* 바 높이 */
	    background-color: green; /* 기본 색상 */
	    border-radius: 5px; /* 모서리 둥글게 */
	    transition: width 0.5s ease, background-color 0.5s ease; /* 애니메이션 효과 */
	}
	
	.energy-text {
	    font-weight: bold; /* 글자 두껍게 */
	    position: absolute; /* 위치 절대 설정 */
	    left: 40px; /* 왼쪽 여백 */
	    top: 50px; /* 상단 여백 */
	    font-size: 30px;
	}
		
	   
    
    .game-scoreView {
	    position: fixed; /* 고정 위치 설정 */
	    top: 50px; /* 화면 맨 위에 위치 */
	    right: 40px; /* 화면 오른쪽에 위치 */
	    transform: translateY(0); /* 세로 위치 조정 (필요 시 조정 가능) */
	    z-index: 10; /* 다른 요소 위에 위치하도록 설정 */
	    font-size: 2em; /* 글자 크기 조정 (원하는 크기로 조정 가능) */
	    color : white;
	}
	
	.bottom-container {
	    position: fixed; /* 고정 위치 설정 */
	    bottom: 0; /* 화면 하단에 위치 */
	    left: 50%; /* 화면 중앙 */
	    transform: translateX(-50%); /* 중앙 정렬을 위한 변환 */
	    display: flex; /* Flexbox 사용 */
	    flex-direction: row; /* 가로 방향 정렬 */
	    align-items: center; /* 중앙 정렬 */
	    margin-bottom: 50px; /* 하단 여백 추가 */
	}
	
	.bottom-container button {
	    font-size: 1.5em; /* 버튼 글자 크기 조정 */
	    padding: 15px 30px; /* 버튼 패딩 조정 */
	}
	
	.bottom-container input[type="text"] {
	    font-size: 2em; /* 입력란 글자 크기 조정 */
	    padding: 15px; /* 입력란 패딩 조정 */
	}
	
	

	a {
   	 text-decoration: none; /* 밑줄 제거 */
   	 color: white;
	}
	
	
	
    button {
        background-color: #ff6f61; /* 버튼 배경색 */
        color: white; /* 버튼 글자색 */
        border: none; /* 테두리 제거 */
        border-radius: 5px; /* 모서리 둥글게 */
        padding: 10px 20px; /* 버튼 패딩 */
        font-size: 1.2em; /* 버튼 글자 크기 */
        cursor: pointer; /* 커서 모양 변경 */
        margin: 5px; /* 버튼 간 간격 */
        transition: background-color 0.3s; /* 배경색 전환 효과 */
        text-decoration: none; /* 밑줄 제거 */
    }

    button:hover {
        background-color: #ff4a3d; /* 호버 시 색상 변화 */
    }

    input[type="text"] {
       	padding: 10px; /* 입력란 패딩 */
        font-size: 1.2em; /* 입력란 글자 크기 */
        border: 2px solid #ff6f61; /* 테두리 색상 */
        border-radius: 5px; /* 모서리 둥글게 */
        width: 40%; /* 입력란 너비 */
		margin: 5px;  /* 입력란 간격 */
		margin-right: 10px; /* 버튼과의 간격 */
      
    }

    .word{
    	font-family: "Bungee Tint", sans-serif;
    	color : white !important;
    	display: inline-block;
        position: fixed; /* 단어 위치 절대 설정 */
        padding : 20px;
        top : 10px;
        left :20px;
        font-size: 4em; /* 단어 크기 */
        color: #ff6f61; /* 단어 색상 */
        text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5); /* 텍스트 그림자 효과 */
    	transition: transform 0.5s; /* 변형에 대한 전환 효과 */
    	animation: explode 0.5s forwards; /* 애니메이션 적용 */
    }
    
     @keyframes explode {
	    0% {
	        transform: scale(1);
	        color : white;
	    }
	    50% {
	        transform: scale(1.5);
	        
	    }
	    100% {
	        transform: scale(1);
	        color: white;
	    }
	}
	
	.modal {
	    display: none; /* 기본적으로 숨김 */
	    position: fixed;
	    z-index: 1;
	    left: 0;
	    top: 0;
	    width: 100%;
	    height: 100%;
	    overflow: auto;
	    background-color: rgb(0,0,0);
	    background-color: rgba(0,0,0,0.4); /* 반투명 배경 */
	}

	.modal-content {
	    background-color: #fefefe;
	    margin: auto; /* 수평 중앙 정렬 */
	    padding: 20px;
	    border: 1px solid #888;
	    width: 50%; /* 모달 너비 */
	    top: 50%; /* 수직 중앙 정렬 */
	    position: relative; /* position을 relative로 변경 */
	    transform: translateY(-50%); /* 수직 중앙 정렬을 위해 Y축으로 이동 */
	}
	
	.modal-content h2 {
	 	font-family: "Archivo Black", sans-serif;
	    font-weight: 400;
	    font-style: normal;
	    color : black;
        font-size: 100px; /* 제목 크기 */
    }

    .madalGame-scoreView div {
     	font-family: "Archivo Black", sans-serif;
	    font-weight: 400;
	    font-style: normal;
	    color : black;
        font-size: 70px; /* 각 결과 항목 크기 */
    }
	
	.close {
	    color: #aaa;
	    float: right;
	    font-size: 50px;
	    font-weight: bold;
	}
	
	.close:hover,
	.close:focus {
	    color: black;
	    text-decoration: none;
	    cursor: pointer;
	}


</style>
</head>
<body>
<div class="game-container">
	<h1 id="gameTitle">단어 뿌시기 게임</h1>
		<div class="energy-text">에너지: <span class="energy-bar" id="energyBar">100%</span></div>
		
	<div class="game-scoreView">
	    <div id="startTime">경과 시간 : 0초</div>
	    <div id="typingSpeed">타자 속도 : 0타</div>
	    <div id="typingAccuracy">자리 정확도 : 0%</div>
	    <div id="accuracy">낱말 정확도 : 0%</div>
	</div>
   	
	<div id="resultModal" class="modal">
   		<div class="modal-content">
        	<span class="close" onclick="closeModal()">&times;</span>
    		<h2>게임 결과</h2>
   			<div class="madalGame-scoreView">
				<div id="modalStartTime"></div>
				<div id="modalTypingSpeed"></div> 
				<div id="modalTypingAccuracy"></div>
				<div id="modalAccuracy"></div>
				<button id="reGameStart">다시하기</button>
				<button><a href="/ENGround0909/mainPage">메인으로</a></button>
			</div>
		</div>
	</div>
	
	<div class="bottom-container">
   		<button id="startButton">게임 시작</button>
   		<input type="text" id="answer" placeholder="단어를 입력하세요" onkeypress="checkEnter(event)">
   		<button id="checkButton" onclick="checkAnswer()">확인</button>
	</div>
		
		
</div>
</body>
<script>
// id값을 변수에 담음
const answerElement = document.getElementById("answer");
const checkButtonElement = document.getElementById("checkButton");
const startButtonElement = document.getElementById("startButton");
const energyBarElement = document.getElementById("energyBar");
const startTimeElement = document.getElementById("startTime");
const accuracyElement = document.getElementById("accuracy");
const typingAccuracyElement = document.getElementById("typingAccuracy");
const typingSpeedElement = document.getElementById("typingSpeed");
const gameTitleElement = document.getElementById("gameTitle");

const resultModalElement = document.getElementById("resultModal");
const modalStartTimeElement = document.getElementById("modalStartTime");
const modalTypingSpeedElement = document.getElementById("modalTypingSpeed");
const modalTypingAccuracyElement = document.getElementById("modalTypingAccuracy");
const modalAccuracyElement = document.getElementById("modalAccuracy");


// 모달창 불러오기 -------------------------------------------------------------------------------------------------
function openModal(startTime, typingSpeed, typingAccuracy, accuracy) {
	const elapsedTime = Math.floor((Date.now() - startTime) / 1000); // 경과 시간 (초) 계산
	const minutes = Math.floor(elapsedTime / 60); // 분 계산
    const seconds = elapsedTime % 60; // 초 계산
    
	modalStartTimeElement.innerText = "경과 시간 : " + minutes + "분 " + seconds + "초"; 
	modalTypingSpeedElement.innerText = "타자 속도 : " + typingSpeed + "타";
	modalTypingAccuracyElement.innerText = "자리 정확도 : " + typingAccuracy + "%";
	modalAccuracyElement.innerText = "낱말 정확도 : " + accuracy + "%";
    resultModalElement.style.display = "block";
}

// 모달창 닫기 ----------------------------------------------------------------------------------------------------
function closeModal() {
	// 게임 진행 중 요소 다시 보이기
    startTimeElement.style.display = 'block';
    typingSpeedElement.style.display = 'block';
    typingAccuracyElement.style.display = 'block';
    accuracyElement.style.display = 'block';
	resultModalElement.style.display = 'none'; //결과창 안보이기
}




//낱말 모음
const words = 
  	["writer", "apple", "lion", "pineapple", "pasta", "tree", "pig", "java",
  		"tiger","kiwi","coffee","computer","zebra","banana","watermelon","orange",
  		"soccer","dog","pizza","car","golf","yellow","tennis","grape"];
   	
   	//전역변수
	let currentWord = ""; // 현재 떨어지는 단어 저장
	let currentWordElement = null; // 현재 단어 요소 저장
	
	let gameActive = false;//게임 실행 모드(비활성화)
	let isPaused = false; // 일시정지 상태(비활성화)
	let startTime; // 시작시간
	let elapsedTime = 0; // 경과시간 초기화
	let typingSpeed = 0; // 타자속도 초기화
	let energy = 100; //에너지  
	
	let fallSpeed = 5; //떨어지는 단어 기본속도
	let fallwordcount = 0; //떨어지는 단어 
	let wordCount = 0; // 맞춘 단어 수
	let wrongWordCount = 0; // 틀린 단어 수
	let digitWord = 0; //맞춘 단어 자리 수(길이)
	let totalWord = 0; // 총 입력된 자리 수(길이)
	let typingAccuracy = 0; //자리 정확도 
	let accuracy = 0; // 낱말 정확도
	
	let timeInterval; // 타임 업데이트
	let typingSpeedInterval; // 타자 속도 업데이트
	
	
	

// 페이지 로드 시 입력란과 확인 버튼 비활성화-----------------------------------------------------------------------------------------
window.onload = function() {
	answerElement.disabled = true; // 입력란 비활성화
   	checkButtonElement.disabled = true; // 확인 버튼 비활성화
    startButtonElement.disabled = false; // 게임 시작 버튼 활성화
    answerElement.value = ""; // 입력 필드 초기화
};


// 키 입력 방지 함수 (화면 확대,축소 방지)------------------------------------------------------------------------------------------
function handleEvent(event) {
    // 컨트롤 키가 눌렸을 때
    if (event.type === 'keydown' && event.ctrlKey) {
        event.preventDefault(); // 컨트롤키 누름 동작을 막음
        alert("컨트롤 키를 누를 수 없습니다.");
    }
    // 마우스 휠 스크롤 시
    if (event.type === 'wheel') {
        event.preventDefault(); // 마우스휠 동작을 막음
       	alert("마우스 휠을 누를 수 없습니다.");
    }
}
// 이벤트 리스너 등록
window.addEventListener('keydown', handleEvent);
window.addEventListener('wheel', handleEvent);



//온키 이벤트 '엔터' 확인버튼 적용----------------------------------------------------------------------------------------------
function checkEnter(event) {
   	if(event.key === "Enter") {
   		 checkAnswer();
   	}
}



//인풋 이벤트 리스너-----------------------------------------------------------------------------------------------------
answer.addEventListener("input", function(event) {
	if(!gameActive){
		return;
	}
 	const inputValue = this.value;
 	// 영어가 아닌 문자가 포함되어 있는지 체크
 	if (/[^a-z]/.test(inputValue)) {
 		alert("영어 '소문자'만 입력할 수 있습니다.");
 	    	this.value = inputValue.replace(/[^a-z]/g, ''); // 영어가 아닌 문자 제거
 	}
 	     	onWordTyped(); // 자리 정확도 이벤트 리스너
 	     	
});



//게임 시작 버튼에 onclick----------------------------------------------------------------------------------------------
startButton.onclick = function() {
	if (!gameActive) {
		startFalling(); // 게임 시작
	} else {
	    togglePause(); // 일시정지/이어하기
	}
};


//다시하기 버튼에 onclick----------------------------------------------------------------------------------------------
reGameStart.onclick = function() {
	if (!gameActive) {
		startFalling(); // 게임 시작
		closeModal()
	} else {
	    togglePause(); // 일시정지/이어하기
	}
};


//게임 시작 업데이트----------------------------------------------------------------------------------------------
function startSub(){
	gameTitleElement.style.display = 'none'; // <h1> 숨기기
	startButtonElement.innerText = "일시정지"; // 버튼 텍스트를 '일시정지'로 변경
	accuracyElement.innerText = "낱말 정확도 : " + 0 + "%"; //낱말 정확도 0으로 초기화
	typingAccuracyElement.innerText = "자리 정확도 : " + 0 + "%"; //낱말 정확도 0으로 초기화
	startTimeElement.innerText = "경과 시간 : " + 0 + "초"; //경과 시간 0으로 초기화
	typingSpeedElement.innerText = "타자 속도 : " + 0 + "타";//타자 속도 0으로 초기화
	energyBarElement.innerHTML = energy + "%";//현재 에너지 표시
	answerElement.value = "";//이전 입력값 초기화
	checkButtonElement.disabled = false;//확인버튼 클릭할 수 있도록 활성화
  	answerElement.disabled = false; //입력란 활성화
  	answerElement.classList.remove('blink'); // 깜빡임 효과 제거
  	answerElement.focus(); //게임 시작 시 입력란 커서
 	//게임 초기 진행
   	energy = 100; //에너지 초기화
   	fallSpeed = 5; //단어 속도 초기화
   	updateEnergy(); // 에너지 업데이트
   	wordCount = 0; // 맞춘 단어 
   	wrongWordCount = 0; //틀린 단어 
   	fallwordcount = 0;; //떨어진 단어
   	nextWord(); //문제 출제
} 


//일시정지 업데이트 함수----------------------------------------------------------------------------------------------
function isPausedSub(){
	pausedTime= Math.floor((Date.now() - startTime) / 1000);// 일시정지된 시간을 기록(1초 단위로)
	 
	clearInterval(typingSpeedInterval); // 타자 속도 업데이트 중지
  	clearInterval(timeInterval); // 경과 시간 업데이트 중지
 	
  	answerElement.value = ""; //입력 필드 초기화
	checkButtonElement.disabled = true; //확인 버튼 비활성화
	answerElement.disabled = true; // 입력란 비활성화
}


//이어하기 업데이트 함수----------------------------------------------------------------------------------------------
function reStart(){
	startTime = Date.now() - (pausedTime * 1000); // 일시정지된 시간을 감안하여 시작 시점 조정
	
	typingSpeedInterval = setInterval(updateTypingSpeed, 1000); // 타자 속도 업데이트  
	
	timeInterval = setInterval(updateTime, 1000); // 경과 시간 업데이트
	
  	checkButtonElement.disabled = false; // 확인 버튼 활성화
  	answerElement.disabled = false; // 입력란 활성화
  	answerElement.focus(); //이어하기 시 입력란 커서
}


//게임 오버---------------------------------------------------------------------------------------------------------
function gameOver(){
	clearInterval(typingSpeedInterval); // 타자속도 업데이트 중지 
	clearInterval(timeInterval); // 경과 시간 업데이트 중지
	gameActive = false; //게임 비활성화 상태로 변경
	energy = 100; //에너지 표시
	document.body.removeChild(currentWordElement); // 현재 단어 삭제
  	answerElement.value = ""; // 입력 필드 초기화
  	checkButtonElement.disabled = true; //확인 버튼 비활성화
 	answerElement.disabled = true; // 입력란 비활성화
  	startButtonElement.disabled = false; // "게임 시작" 버튼 활성화
  	startButtonElement.innerText = "게임 시작";
  
  	updateWordAccuracy(); // 정확도 계산 및 업데이트
  
  	openModal(startTime, typingSpeed, typingAccuracy, accuracy); //모달 결과창 업데이트
  
  	startTimeElement.style.display = 'none';
 	typingSpeedElement.style.display = 'none';
  	typingAccuracyElement.style.display = 'none';
  	accuracyElement.style.display = 'none';
	console.log("게임종료 :gameActive " + gameActive);
  	console.log("경과 시간 타이머 중지: ", timeInterval);
}



//에너지 업데이트 함수------------------------------------------------------------------------------------------------
function updateEnergy() {
   
    energyBarElement.style.width = energy + "%"; //에너지바의 길이
    energyBarElement.innerHTML = energy + "%"; //에너지바 화면 표시

    // 색상 변화 로직
    if (energy >= 70) {
        energyBar.style.backgroundColor = 'green'; // 높은 에너지
    } else if (energy >= 40) {
        energyBar.style.backgroundColor = 'yellow'; // 중간 에너지
    } else {
        energyBar.style.backgroundColor = 'red'; // 낮은 에너지
    }
}



// 단어 속도 업데이트 함수 -------------------------------------------------------------------------------------------------
function updateFallSpeed(){
	
	if(wordCount != 0 && wordCount % 5 == 0){ //맞춘 단어 수가 0이 아니고 && 5와 나눴을 때 나머지값이 0일때만
		alert("단어 속도가 빨라집니다.");
		fallSpeed = wordCount * 2; //맞춘단어 수 곱하기 2 = 단어속도
	}
}



//경과 시간----------------------------------------------------------------------------------------------------------
function updateTime() {
    if (gameActive && !isPaused) { 
        elapsedTime = Math.floor((Date.now() - startTime) / 1000); // 경과 시간 (초)
        startTimeElement.innerText = "경과 시간 : " + elapsedTime + "초"; //결과 시간 표시
    }
}



//타자 속도----------------------------------------------------------------------------------------------------------
function updateTypingSpeed(){
	elapsedTime = Math.floor((Date.now() - startTime) / 1000); // 경과 시간 (초)
	elapsedMinutes = elapsedTime / 60;
	
	typingSpeed = elapsedMinutes > 0 ? Math.floor((wordCount / elapsedMinutes) * 20) : 0; //20배를 곱해 정수로 변환
   	typingSpeedElement.innerText = "타자 속도 : " + typingSpeed + "타";//타자 속도 표시
}
    
    
    
//자리 정확도  -입력창 input 이벤트 리스너- ---------------------------------------------------------------------------------
function onWordTyped() {
 	
  	typedWord = answer.value; // 입력된 단어 가져오기
   	console.log("입력값:" +typedWord);
   
  	totalWord = currentWord.length; // 현재 단어의 길이를 총 문자 길이로 설정
  	
  	digitWord = 0; // 매 입력마다 맞춘 자리 수 초기화
 	
  	// 입력된 문자 수에 따라 자리 수 증가
    for (let i = 0; i < typedWord.length; i++) {
        if (typedWord[i] === currentWord[i]) {
            digitWord++;
        }
    }
	  	// 자리 정확도 계산: (맞춘 단어 자리 수 / 총 단어 자리 수) * 100
	 	typingAccuracy = Math.round((digitWord / totalWord) * 100);
	 	// 정확도 100%
    	console.log(typingAccuracy);
	 	typingAccuracyElement.innerText = "자리 정확도 : " + typingAccuracy + "%"; //자리 정확도 표시
}


// 낱말 정확도 ------------------------------------------------------------------------------------------------------
function updateWordAccuracy(){
	
	const totalCount = wordCount + wrongWordCount; // 총 맞춘 단어 수와 틀린 단어 수의 합
	accuracy = totalCount > 0 ? Math.round((wordCount / totalCount) * 100) : 0; // 0으로 나누는 오류 방지
	accuracyElement.innerText = "낱말 정확도 : " + accuracy + "%"; 
	console.log("낱말 정확도 테스트 : " + accuracy);
}




/*------------------------------------------------------------------------------------------------------------  */


//게임 시작 버튼 눌렀을 시
function startFalling() {
	//게임 활성화 및 GO&STOP
    console.log("게임 시작!");
	gameActive = true;//게임 활성화
	isPaused = false; // 일시정지 상태(비활성화)
	
	//경과시간 업데이트
	clearInterval(timeInterval); // 경과 시간 업데이트 중지
	elapsedTime = 0; // 경과시간 초기화
	startTime = Date.now(); // 시작 시간 기록
	timeInterval = setInterval(() => {
        elapsedTime += 1; // 1초마다 경과 시간 증가
        startTimeElement.innerText = "경과 시간 : " + elapsedTime + "초"; // 결과 시간 표시
        console.log("경과 시간 :" + elapsedTime);
    }, 1000); // 1초마다 업데이트

	
	//타자속도 업데이트
   	clearInterval(typingSpeedInterval); // 타자 속도 업데이트 중지
   	typingSpeedInterval = setInterval(updateTypingSpeed, 1000); // 1초마다 타자 속도 업데이트
   	
   	startSub(); // 게임 시작 업데이트
   	updateFallSpeed(); //단어속도 업데이트
}  	
	



//일시정지 및 이어하기--------------------------------------------------------------------------------------------------
function togglePause() {
    if (gameActive) {
        isPaused = !isPaused; // 일시정지 상태로 변환
        startButtonElement.innerText = isPaused ? "이어하기" : "일시정지"; // 버튼 텍스트 변경
        console.log("isPaused:", isPaused); // 게임 일시정지 상태 확인
        
        if (isPaused) {
        	isPausedSub(); //일시정지 함수 호출
        } else {
        	reStart(); //이어하기 함수 호출
    	}
	}
}


//문제생성---------------------------------------------------------------------------------------------------------
function nextWord() {
	fallwordcount++; //떨어지는 단어
	console.log("현재까지 나온 단어 수 :"+fallwordcount);
	
	//words 배열에서 랜덤으로 인덱스를 생성하여 단어 선택
    currentWord = words[Math.floor(Math.random() * words.length)];// words 배열에서 랜덤으로 인덱스를 생성하여 단어 선택
 	
    //새로운 단어 요소 생성
    currentWordElement = document.createElement("div"); //div요소 생성
    currentWordElement.className = "word"; //생성된 div에 "word" 클래스를 부여
    currentWordElement.innerText = currentWord; // div의 텍스트로 현재 단어를 설정

    // 랜덤 위치 설정
    const randomPosition = Math.floor(Math.random() * (window.innerWidth - 200)); // 화면상의 랜덤 위치 설정
    currentWordElement.style.left = randomPosition + "px"; 
    currentWordElement.style.top = "0px"; //단어 랜덤 위치 css
    document.body.appendChild(currentWordElement);//본문에 단어를 사용자가 볼 수 있게 표시

    fallWord(currentWordElement); //떨어지는 함수 호출 및 단어 떨어짐
}


//떨어지는 단어 위치 및 떨어지는 이동거리------------------------------------------------------------------------------------
function fallWord(wordElement) {
	console.log("단어 떨어짐: " + wordElement.innerText);
	let topPosition = 0; // 단어 위치 초기화(0이면 화면 상단에 위치함)
	
	
	//떨어지는 단어 위치 업데이트 함수=>반복 실행
	const fallInterval = setInterval(()=>{  
			
		if (!isPaused && gameActive) { // 일시정지 상태가 아니고 게임 활성화 일때만
				topPosition += fallSpeed; // 단어가 아래로 이동하는 거리
				wordElement.style.top = topPosition + "px"; //css 스타일 업데이트
		}
		    // 화면 아래로 떨어지면 삭제
			if (topPosition > window.innerHeight) {//화면밖에 벗어날 경우
			    clearInterval(fallInterval); //단어 위치 반복 중단
			    document.body.removeChild(wordElement); //현재 단어 삭제
			    energy -= 10; // 에너지 감소
			    energyBarElement.innerHTML = energy + "%"; //에너지표시
			    updateEnergy(); //에너지 업데이트
			    wrongWordCount++; // 틀린 단어 증가
			    console.log("틀린 단어 수 :" + wrongWordCount + "개" );
			    console.log("단어삭제 정상작동");
			    updateWordAccuracy(); //낱말 정확도 함수 호출
			    onWordTyped(); //자리 정확도 함수 호출
			  	nextWord(); // 다음 단어 시작
			}
			
			if(energy <= 0) {
		    	alert("에너지가 모두 소모되었습니다. 게임 종료!");
				gameOver();//게임오버 함수 호출
			}
	}, 50); // 50ms마다 이동(속도조절)
}


// 정답입력 함수------------------------------------------------------------------------------------------------------
function checkAnswer() {
	const answerInput = answerElement.value; //입력값 가져오기
    
	if(!gameActive){
 		 return; //게임 비활성화 시 함수 종료
    }
   
	if(answerInput.trim() === "") {
	 	alert("정답을 입력해주세요!");
	 	answerElement.value =""; //입력 필드 초기화
		return; 	 
	}
	
    if(answerInput === currentWord) { //입력값과 문제단어가 같다면
    	updateTypingSpeed(); //타자속도 함수 호출
    	wordCount++; // 맞춘 단어 증가
    	console.log("맞춘 단어 수 :" + wordCount + "개" );
    	updateFallSpeed(); //단어속도 함수 호출
    	
    	document.body.removeChild(currentWordElement); // 현재 단어 삭제
        answerElement.value = ""; // 입력 필드 초기화
        nextWord(); // 다음 단어 시작 
       	
    }else{
    	alert("틀렸습니다. 체력 10% 감소합니다.");
        energy -= 10; //에너지 감소 -10
        updateEnergy();
        energyBarElement.innerHTML = energy + "%";
        wrongWordCount++; // 틀린 단어 증가
        console.log("틀린 단어 수 :" + wrongWordCount + "개" );
        
        document.body.removeChild(currentWordElement); // 현재 단어 삭제
        answerElement.value = ""; // 입력 필드 초기화
        nextWord(); // 다음 단어 시작    
    }
    	
    updateWordAccuracy() // 낱말 정확도 함수 호출
	
    if(energy <= 0) {
        alert("에너지가 모두 소모되었습니다. 게임 종료!") 
        gameOver(); //게임오버 함수 호출
    }
          
}

</script>
</html>