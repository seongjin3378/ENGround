import * as gameSend from './gameSend.js';
export function subscribeGetTimeMap(stompClient, topic)
{
	 stompClient.subscribe(topic, function (message) {
		 const receivedMessage = JSON.parse(message.body);
		 $("#timer").text(receivedMessage.message[0]);
		 const fullTimer = Number($("#fullTimer").text());
		 const timer = receivedMessage.message[0];
		 console.log(timer);
		 
		 if(timer <= 0)
		 {
			 gameSend.removeTimeMapMessage(stompClient);
		 }
		 	
		 else if(timer ==  Math.floor(fullTimer*0.85)){
				
				gameSend.HintMessage(stompClient, "hint1");
		 }
		 else if(timer ==  Math.floor(fullTimer*0.5)){
				
				gameSend.HintMessage(stompClient, "hint2");
		 }
		 else if(timer ==  Math.floor(fullTimer*0.17)){
				
				gameSend.HintMessage(stompClient, "hint3");
			}
		
	 });
}
function result_ajax() {
    const baseUrl = window.location.origin; // http://localhost:8080
    const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1)); // /ENGround0909

    // 최종 URL을 생성합니다.
    const resultUrl = baseUrl + contextPath + "/resultOnline";
    $.ajax({
        type: "get",
        url: resultUrl,
        success: function(data) {
            console.log(data);
            $('#modalContent').html(data); // 모달에 HTML 삽입
            $('#myModal').modal('show');
        }
    });
}
export function subscribeGetNextQuiz(stompClient, topic)
{
	 stompClient.subscribe(topic, function (message) {
		 if (typeof message.body === 'string' && message.body.indexOf('전적불러오기') !== -1) {
			 result_ajax();
         }
		 else{
		 const receivedMessage = JSON.parse(message.body);
		 console.log(receivedMessage);
		 $(document).off("click", "#play-sound");
		 play_sound(receivedMessage.question);
		 gameSend.HintMessage(stompClient, "allBlank");
		 gameSend.setTimeMapMessage(stompClient, receivedMessage.timer);
		 $("#fullTimer").text(receivedMessage.timer)
		 $("#hint1").text("힌트1");
		 $("#hint2").text("힌트2");
		 $("#hint3").text("힌트3");
		 }
	 });
}
function play_sound(val) {
    $(document).on("click", "#play-sound", function() {
        let utter = new window.SpeechSynthesisUtterance(val);
        utter.lang = "en";
        window.speechSynthesis.speak(utter);
        console.log(121212122112);
    });
}

export function subscribeGetHint(stompClient, topic)
{
	 stompClient.subscribe(topic, function (message) {
		 const receivedMessage = JSON.parse(message.body);
		 $("#answer-blank").text(receivedMessage[1]);
		 if($("#hint1").text() === "힌트1"  && receivedMessage[0] != "")
		 {
			 $("#hint1").text(receivedMessage[0]);
		 }else if($("#hint2").text() === "힌트2"  && receivedMessage[0] != "")
		 {
			 $("#hint2").text(receivedMessage[0]);
		 }
		 else if($("#hint3").text() === "힌트3"  && receivedMessage[0] != ""){
			 $("#hint3").text(receivedMessage[0]);
		 }
		
	 });
}

export function subscribeFirstReqUserDelay(stompClient, topic, topic2)
{
	function delay(ms) {
	    return new Promise(resolve => setTimeout(resolve, ms));
	}
	
	stompClient.subscribe(topic, function (message) {
	const receivedMessage = JSON.parse(message.body);

	delay(500).then(() => {
		stompClient.send(topic2, {}, JSON.stringify(receivedMessage));
		
	});
	});
	
}
export function subscribeCorrectUserAlert(stompClient, topic)
{
	stompClient.subscribe(topic, function (message) {
		const receivedMessage = JSON.parse(message.body);
		console.log(receivedMessage.message);
		const receivedId = receivedMessage.message[0];
		const receivedScore = receivedMessage.message[1];

        $('.side-container').each(function () {
        	const userName = $(this).children().first().text();
            if(userName == receivedId || userName == receivedId + "(나)")
            {
            	const userScore = $(this).children().eq(1);
        		let scoreText = userScore.text();

        		// "점수: " 부분을 제거하고, 공백을 제거한 후 정수형으로 변환합니다.
        		let prevScore = userScore.text().replace("점수: ", "").trim();
        		userScore.text("점수: " + receivedScore);
        		if(userName == receivedId + "(나)")
        		{
        			$("#myScore").text(receivedScore);
        		}
        		const score = parseInt(receivedScore) - parseInt(prevScore)
        		if(!isNaN(score))
        		{
            	$(".onlineGame-chatlog").append("<div class='chatMessage' style='color:green;'>" + receivedId + "님이 " + score + "점을 획득했습니다!</div>");
            	const lastMessage = $('.chatMessage').last();
    			const offset = lastMessage.offset();
    			$('.onlineGame-chatlog').animate({scrollTop: offset.top}, 500);
        		}else{
        			$(".onlineGame-chatlog").append("<div class='chatMessage' style='color:green;'>" + receivedId + "님이 " + parseInt(receivedScore) + "점을 획득했습니다!</div>");
                	const lastMessage = $('.chatMessage').last();
        			const offset = lastMessage.offset();
        			$('.onlineGame-chatlog').animate({scrollTop: offset.top}, 500);
        		}
            }
        });
        
	});
}

export function subscribeShowCorrectCounts(stompClient, topic)
{
	stompClient.subscribe(topic, function (message)
	{
		const receivedMessage = JSON.parse(message.body);
		const correctCounts = receivedMessage.message[0];
		console.log("receivedMessage: "+ correctCounts);
		$("#correct-question").text(correctCounts);
	});
}