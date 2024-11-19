<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<script type="text/javascript"
	src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>게임 페이지</title>
<link rel="stylesheet"
	href="<c:url value='/resources/css/styleJANG.css'/>" />
</head>
<!-- 장성진 -->
<body>
	<div class="modal fade" id="myModal" tabindex="-1"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" style="margin-top: 50%">

				<div class="modal-body" id="modalContent">
					<!-- 여기서 HTML 내용이 표시됩니다. -->
				</div>

			</div>
		</div>
	</div>

	<div class="container">
		<%@ include file="/WEB-INF/include/header.jsp"%>
		<!-- 헤더 포함 -->
		<div class="game-container">
			<div class="game-header">
				<span class="game-score"><span id="correct-question">0</span>/10
					| 100점</span>
			</div>
			<div class="game-input-container">
				<div class="game-timer" id="timer">0</div>
				<div class="game-input" id="current-question">0</div>
				<div class="game-input-box" id="answer-blank">5</div>
				<button class="game-button" id="play-sound">▶</button>
			</div>
			<div class="hint-container">
				<button class="hint-button" id="hint1">힌트1</button>
				<button class="hint-button" id="hint2">힌트2</button>
				<button class="hint-button" id="hint3">힌트3</button>
			</div>
			<div class="answer-container">
				<input type="text" class="game-input2" id="input-answer"
					placeholder="정답을 입력하세요"> <span id="answer-result"
					style="display: none"></span> <input type="button" id="answer-btn"
					value="정답">
			</div>
		</div>
	</div>

</body>
<script>
let isGameEndGlobal = false;
load_ajax(true);
function hint_ajax(hintId)
{
	$.ajax({
		type : "post",
		url : "<c:url value='/hint'/>",
		data : {
			"hint" : hintId
		},
		success : function(data){
			if(data)
			{
				console.log(data);
				$("#"+hintId).text(data[0]);
				var replaceText = data[1].replace(/_/g, "⬜");
				$("#answer-blank").text(replaceText);
			}
		}
	});
}



function load_ajax(isAlert)
{
	$.ajax({
		type : "get",
		url : "<c:url value='/load'/>",
		success : function(data){
			console.log(data);
			if(data.newData == false)
			{
				if (confirm("이전 게임 내역을 불러오시겠습니까?") == true){    //확인
					// 처리 로직 추가
				}
				else{ 
					delete_ajax();
					location.reload(true);
				}
			}
			
			console.log(data.timer);
			timer = parseInt(data.timer);
			fulltimer = timer;
		}
	});
}

function delete_ajax()
{
	$.ajax({
		type : "get",
		url : "<c:url value='/delete'/>",
		success : function(data){
			console.log("삭제 성공");
		}
	});
}


window.addEventListener('beforeunload', function () {
	if($("#current-question").text() == "1" || $("#current-question").text() == "0")
	{
		delete_ajax();
	}
	else if(!isGameEndGlobal)
	{
		$.ajax({
			type : "get",
			url : "<c:url value='/checkPoint'/>",
			success : function(){
				// 처리 로직 추가
			}
		});
	}
});



$(function() {
    let timer = $("#timer").text();
    let fulltimer = $("#timer").text();
    let answerBlank = $("#answer-blank").text();
    let updateInterval;

    function startTimer() {
        answerBlank = answerBlank - 1;
        $("#answer-blank").text(answerBlank);
        if(answerBlank == "0") {
            $("#answer-blank").text("Game Start!");
            clearInterval(startInterval);
            updateInterval = setInterval(updateTimer, 1000);
        }
    }

    function clear_hint() {
        $("#hint1").text("힌트1");
        $("#hint2").text("힌트2");
        $("#hint3").text("힌트3");
    }

    function next_question_ajax() {
        $.ajax({
            type: "get",
            url: "<c:url value='/next'/>",
            data: { cate: "fruit" },
            success: function(data) {
                console.log(data);
                if (typeof data === 'string' && data.indexOf('<title>게임 결과</title>') !== -1) {
                    clearInterval(updateInterval);
                    $('#modalContent').html(data); // 모달에 HTML 삽입
                    $('#myModal').modal('show'); 
                    isGameEndGlobal = true;
                }

                if (data.quizRound != null) {
                    $("#current-question").text((data.quizRound + 1).toString());
                    timer = parseInt(data.timer);
                    $(document).off("click", "#play-sound");
                    play_sound(data.question);
                    fulltimer = timer;
                }
                clear_hint();
            }
        });
    }

    $("#input-answer").keypress(function(e) {
        if (e.keyCode && e.keyCode == 13) {
            $("#answer-btn").trigger("click");
            return false;
        }
        if (e.keyCode && e.keyCode == 13) {
            e.preventDefault();    
        }
    });

    $("#answer-btn").click(function() {
        $.ajax({
            type: "post",
            url: "<c:url value='/correct'/>",
            data: {
                "correct": $("#input-answer").val(),
                "answerTime": parseInt($("#timer").text(), 10)
            },
            success: function(data) {
                $("#answer-result").text(data);
                console.log($("#answer-result").text());
                if ($("#answer-result").text() == "true") {
                    next_question_ajax();
                    $("#answer-result").text("false");
                }
            }
        });
        $("#input-answer").val("");
    });

    function play_sound(val) {
        $(document).on("click", "#play-sound", function() {
            let utter = new window.SpeechSynthesisUtterance(val);
            utter.lang = "en";
            window.speechSynthesis.speak(utter);
            console.log(121212122112);
        });
    }

    // startInterval을 startTimer 정의 이후로 이동
    let startInterval = setInterval(startTimer, 1000);

    function updateTimer() {
        console.log("updateTimer 호출");
        if (timer == 0) {
            next_question_ajax();
        }
        $("#timer").text(timer);
        if (timer == Math.floor(fulltimer * 1)) {
            hint_ajax("allBlank");
        }
        timer = timer - 1;
        if (timer == Math.floor(fulltimer * 0.85)) {
            hint_ajax("hint1");
        }
        if (timer == Math.floor(fulltimer * 0.5)) {
            hint_ajax("hint2");
        }
        if (timer == Math.floor(fulltimer * 0.17)) {
            hint_ajax("hint3");
        }
    }
});


</script>
</html>

