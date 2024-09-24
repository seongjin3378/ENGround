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

export function subscribeGetNextQuiz(stompClient, topic)
{
	 stompClient.subscribe(topic, function (message) {
		 const receivedMessage = JSON.parse(message.body);
		 console.log(receivedMessage);
		 gameSend.HintMessage(stompClient, "allBlank");
		 gameSend.setTimeMapMessage(stompClient, receivedMessage.timer);
		 $("#fullTimer").text(receivedMessage.timer)
		 $("#hint1").text("힌트1");
		 $("#hint2").text("힌트2");
		 $("#hint3").text("힌트3");
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