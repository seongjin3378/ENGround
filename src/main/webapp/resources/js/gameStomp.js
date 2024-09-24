import lobbyStomp from './lobbyStomp.js';
import * as roomSend from  './roomSend.js';
import * as gameSend from './gameSend.js';
$(".wait-answer-btn").on("click", function(){
	roomSend.chatMessage(lobbyStomp.getStompClient());
});

if(updateHostSession() == "true")
{
	gameSend.setTimeMapMessage(lobbyStomp.getStompClient(), "5");
}


