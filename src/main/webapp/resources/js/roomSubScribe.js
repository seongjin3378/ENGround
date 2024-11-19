import * as roomSend from  './roomSend.js';  
export function subscribeJoinRoom(stompClient, readyUser, topic) {
    stompClient.subscribe(topic, function (message) {
        let receivedMessage = JSON.parse(message.body);
        console.log(enterMessages);
        var roomId = receivedMessage[0].roomId;
        var message = [sessionId + "님이 입장했습니다."];
        let userId = [];
        receivedMessage.forEach(function (msg) {
            if (msg.userId != sessionId) {
                userId.push(msg.userId);
            }
        });

        let waitUserList = [];
        $('.side-container').each(function () {
            waitUserList.push($(this).children().first());
        });

        waitUserList.forEach(function (element, index) {
            if (index == 0) {
                element.text(sessionId + "(나)");
                element.attr('id', sessionId);
            } else {
                element.text(userId[index - 1]);
                element.attr('id', userId[index - 1]);
            }
        });

        $("#joinList").text(userId);
        $("#joinList").append(sessionId + "(나)");
        enterMessages["roomId"] = roomId;
        enterMessages["userId"] = userId;
        enterMessages["message"] = message;

        const isHost = updateHostSession();
        if (isHost == "true") {
        	userId.forEach(user => {
        	    if (banListGlobal.includes(user)) {
        	        console.log(`${user}는 차단된 사용자입니다.`);
        	        roomSend.kickMessage(`${user}`, stompClient);
        	    }
        	});
            stompClient.send("/pub/sendReadyUser/" + roomId, {}, JSON.stringify(readyUser));
        }
    });
}

export function subscribeEnterMessage(stompClient, topic) {
    stompClient.subscribe(topic, function (message) {
        const receivedMessage = JSON.parse(message.body);
        $(".wait-chatlog").append("<div class='chatMessage'>" + receivedMessage.message[0] + "</div>");
    });
}

export function subscribeGetReadyUser(stompClient, topic) {
    stompClient.subscribe(topic, function (message) {
        const receivedMessage = JSON.parse(message.body);
        let readyUserHTML = "";
        Object.entries(receivedMessage).forEach(function ([key, value]) {
            if (value == true) {
                readyUser[key] = value;
                readyUserHTML += "<div id=" + key + ">" + key + "준비</div>";
            }
        });
        $("#readyList").html(readyUserHTML);
    });
}

export function subscribeReady(stompClient, readyUser, topic) {
    stompClient.subscribe(topic, function (message) {
        const receivedMessage = JSON.parse(message.body);
        if (!$("#" + receivedMessage.userId).length) {
            $("#readyList").append("<div id=" + receivedMessage.userId + ">" + receivedMessage.userId + "준비</div>");
            readyUser[receivedMessage.userId] = true;
        } else {
            $("#" + receivedMessage.userId).parent().css("backgroundColor", "yellow");
            readyUser[receivedMessage.userId] = true;
        }
    });
    return readyUser;
}

export function subscribeReadyCancel(stompClient, readyUser, topic) {
    stompClient.subscribe(topic, function (message) {
        const receivedMessage = JSON.parse(message.body);
        $("#" + receivedMessage.userId).parent().css("backgroundColor", "white");
        readyUser[receivedMessage.userId] = false;
    });
    return readyUser;
}

export function subscribeDisConnected(stompClient, readyUser, topic) {
    stompClient.subscribe(topic, function (message) {
        const receivedMessage = JSON.parse(message.body);
        const exitUserId = receivedMessage.userId;
        delete readyUser[exitUserId];

        $("#" + exitUserId).text("");
        let waitUserList = [];
        $('.side-container').each(function () {
            waitUserList.push($(this).children().first());
        });

        let temp = null;
        waitUserList.forEach(function (element, index) {
            if (element.text() == "") {
                temp = element;
            } else if (temp != null) {
                temp.text(element.text());
                temp.attr("id", element.attr("id"));
                element.removeAttr("id");
                element.text("");
            }
        });

        readyUser[exitUserId] = false;
        if (!banListGlobal.includes(exitUserId)) {
        $(".wait-chatlog").append("<div class='chatMessage'>" + exitUserId + "님이 나가셨습니다. </div>");
        }
    });
    return readyUser;
}

export function subscribeStart(stompClient, topic) {
    var isNotStart = true;
    stompClient.subscribe(topic, function (message) {
        const receivedMessage = message.body;
        if (receivedMessage === "OK") {
            isNotStart = false;

            const timerValue = $('#timer').val(); // id가 'timer'인 요소의 값을 가져옴
            const categoryValue = $('#category').val(); // id가 'category'인 요소의 값을 가져옴

            // 데이터가 올바른지 확인
            console.log("/room/" + roomIdGlobal);
            console.log("Timer: ", timerValue);
            console.log("Category: ", categoryValue);
            const baseUrl = window.location.origin; // 예: http://localhost:8080
	        const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1)); // 예: /ENGround0909
	        const startUrl = baseUrl + contextPath + "/room/" + roomIdGlobal;
            $.ajax({
                type: "POST",
                url: startUrl,
                data: {
                    "timer": timerValue,
                    "category": categoryValue
                },
                success: function (response) {
                	console.log(sessionId);
                	$('[style*="background-color: yellow"]').each(function() {
                	    $(this).css("backgroundColor", "white");
                	});
                    $('.back-container').empty();
                    $('.back-container').html(response); // 서버에서 받아온 HTML로 body를 업데이트
                },
                error: function (xhr, status, error) {
                    console.error("Error: " + error); // 오류 처리
                }
            });
        }
    });
    return isNotStart;
}

export function subscribeSetHostSession(stompClient, readyUser, topic) {
	const baseUrl = window.location.origin; // 예: http://localhost:8080
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1)); // 예: /ENGround0909
	const setHostSessionUrl = baseUrl + contextPath + "/setHostSession";
    stompClient.subscribe(topic, function (message) {
        const receivedMessage = message.body;
        if (receivedMessage == "OK") {
            $.ajax({
                type: "get",
                url: setHostSessionUrl,
                success: function () {
                	var convertStartButton = $('#ready-button');
                	convertStartButton.attr('id', 'start-button');
                	convertStartButton.text('시작');
                	convertStartButton.off("click");
                	convertStartButton.on("click", function(){
                		roomSend.startMessage(stompClient, readyUser);
                	})
            		$('.delegation-host-button').css('display', '');
            		$('.kick-button').css('display', '');
                }
            });
        }
    });
}

export function subscribeChat(stompClient, topic) {
    stompClient.subscribe(topic, function (message) {
        const receivedMessage = JSON.parse(message.body);
        $(".wait-chatlog").append("<div class='chatMessage'>" + receivedMessage.userId[0] + ": " + receivedMessage.message[0] + "</div>");
        $(".onlineGame-chatlog").append("<div class='chatMessage'>" + receivedMessage.userId[0] + ": " + receivedMessage.message[0] + "</div>");
    });
}

export function subscribeKickedOutStatus(stompClient, topic) {
	const baseUrl = window.location.origin; // 예: http://localhost:8080
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1)); // 예: /ENGround0909
	const lobbyUrl = baseUrl + contextPath + "/lobby";

    stompClient.subscribe(topic, function (message) {
        location.href = lobbyUrl;
    });
}

export function subscribeKickedUserNotification(stompClient, topic) {
    stompClient.subscribe(topic, function (message) {
        const kickedUserId = message.body;
        if (!banListGlobal.includes(kickedUserId)) {
        $(".wait-chatlog").append("<div class='chatMessage' style='color:red;'>" + kickedUserId +"유저가 강퇴됐습니다." + "</div>");
        banListGlobal.push(kickedUserId);
        }
    });
}
		
    
export function subscribeReportSuccessNotification(stompClient, topic)
{
    stompClient.subscribe(topic, function (message) {
        const receivedMessage = message.body;
        $(".wait-chatlog").append("<div class='chatMessage' style='color:red;'>" + receivedMessage + "</div>");
        $('.btn-secondary[data-dismiss="modal"]').click();
    });
}