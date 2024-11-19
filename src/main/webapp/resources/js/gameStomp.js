import lobbyStomp from './lobbyStomp.js';

import * as roomSend from  './roomSend.js';
import * as gameSend from './gameSend.js';

/*장성진*/
$(".wait-answer-btn").on("click", function(){
	roomSend.chatMessage(lobbyStomp.getStompClient());
	$("#wait-input-answer").val("");
});

if(updateHostSession() == "true")
{
	gameSend.setTimeMapMessage(lobbyStomp.getStompClient(), "5");
}

$("#wait-input-answer").keypress(function(e) {
    if (e.keyCode && e.keyCode == 13) {
        $(".wait-answer-btn").trigger("click");
        return false;
    }
    if (e.keyCode && e.keyCode == 13) {
        e.preventDefault();    
    }
});
