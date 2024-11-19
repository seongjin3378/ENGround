import * as roomSend from  './roomSend.js';
import * as roomSubScribe from  './roomSubScribe.js';
import * as gameSubScribe from  './gameSubscribe.js';
function createLobbyStomp()
	{
	let readyUser= {};
	let isNotStart = true;
	let stompClient = null;
	window.addEventListener('beforeunload', function () {
	    {
	    if(isNotStart)
	    {
	        const baseUrl = window.location.origin; // http://localhost:8080
	        const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1)); // /ENGround0909

	        // 최종 URL을 생성합니다.
	        const deleteGameSessionUrl = baseUrl + contextPath + "/deleteGameSession";
	       $.ajax(
	       {
	    	   
	           type : "get",
	           url : deleteGameSessionUrl,
	           success : function(){
	        	   stompClient.send("/pub/"+roomIdGlobal+"/exit");
	           }

	       }
	       );
	       }
	    }
	   });
	   
	$("#ready-button").on("click", function(){
	    roomSend.readyMessage(stompClient, readyUser);
	});
	$("#start-button").on("click", function(){
		roomSend.startMessage(stompClient, readyUser);
	})
	$(".wait-answer-btn").on("click", function(){
		roomSend.chatMessage(stompClient);
		$("#wait-input-answer").val("");
	})
	$("#wait-input-answer").keypress(function(e) {
        if (e.keyCode && e.keyCode == 13) {
            $(".wait-answer-btn").trigger("click");
            return false;
        }
        if (e.keyCode && e.keyCode == 13) {
            e.preventDefault();    
        }
    });
	$(".kick-button").on("click", function(){
		const element = $(this).closest('.dropdown').prev().prev();
		const kickedUserId = element.attr('id');
		roomSend.kickMessage(kickedUserId, stompClient);
		
	})
	
	$(".delegation-host-button").on("click", function(){
		var element = $(this).closest('.dropdown').prev().prev();
		roomSend.delegationHostMessage(element.attr('id'), stompClient);
       	var convertReadyButton = $("#start-button");
       	convertReadyButton.removeAttr("id");
       	convertReadyButton.attr('id', 'ready-button');
       	convertReadyButton.text("레디");
       	convertReadyButton.off("click");
       	convertReadyButton.on("click", function(){
        roomSend.readyMessage(stompClient, readyUser);
    	})
		$('.delegation-host-button').css('display', 'none');
		$('.kick-button').css('display', 'none');
	})
	$(".report-button").on("click", function(){
		var element = $(this).closest('.dropdown').prev().prev();
		$("#reportedUser").val(element.attr('id'));
		//element = $("#reportedUser").val();
		//roomSend.reportMessage(element, stompClient);
	})
	$("#report-req-button").on("click", function(){
		var element = $("#reportedUser").val();
		roomSend.reportMessage(element, stompClient);
	})
	roomSend.setCategoryAjax();
	$('#category').change(function() {
		roomSend.setCategoryAjax();
	});
   
    	function connect()
    	{
    		
		const baseUrl = window.location.origin;
		const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1)); // 


		const socketUrl = baseUrl + contextPath + '/ws';
		const socket = new SockJS(socketUrl);
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function (frame) {
        	roomSubScribe.subscribeJoinRoom(stompClient, readyUser, '/user/' + roomIdGlobal + '/joinRoom');
        	roomSubScribe.subscribeEnterMessage(stompClient, '/user/' + sessionId + '/enterMessage');
        	roomSubScribe.subscribeGetReadyUser(stompClient, '/user/' + roomIdGlobal + '/getReadyUser');
        	readyUser = roomSubScribe.subscribeReady(stompClient, readyUser, '/user/' + roomIdGlobal + '/ready');
        	readyUser = roomSubScribe.subscribeReadyCancel(stompClient, readyUser, '/user/' + roomIdGlobal + '/readyCancel');
        	readyUser = roomSubScribe.subscribeDisConnected(stompClient, readyUser, '/user/' + roomIdGlobal + '/disConnected');
        	isNotStart = roomSubScribe.subscribeStart(stompClient, '/user/' + roomIdGlobal + '/start');
        	roomSubScribe.subscribeSetHostSession(stompClient, readyUser, '/user/' + sessionId + '/setHostSession');
        	roomSubScribe.subscribeChat(stompClient, '/user/' + roomIdGlobal + '/chat');
        	roomSubScribe.subscribeKickedOutStatus(stompClient, '/user/' + sessionId + '/kickedOutStatus');
        	roomSubScribe.subscribeKickedUserNotification(stompClient, '/user/' + roomIdGlobal + '/kickedUserNotification');
        	roomSubScribe.subscribeReportSuccessNotification(stompClient, '/user/'+ sessionId + '/report');
        	gameSubScribe.subscribeGetTimeMap(stompClient,  '/user/' + roomIdGlobal + '/getTimeMap');
        	gameSubScribe.subscribeGetNextQuiz(stompClient, '/user/' + roomIdGlobal + '/getNextQuiz');
        	gameSubScribe.subscribeGetHint(stompClient, '/user/'+ roomIdGlobal + "/getHint");
        	gameSubScribe.subscribeFirstReqUserDelay(stompClient, "/user/"+ sessionId + "/firstReqUserDelay", "/pub/chat/"+roomIdGlobal);
        	gameSubScribe.subscribeCorrectUserAlert(stompClient, "/user/"+ roomIdGlobal + "/correctUserAlert");
        	gameSubScribe.subscribeShowCorrectCounts(stompClient, "/user/"+ sessionId + "/showCorrectCounts");
        	
        }, function (error) {
            console.error('Connection error: ' + error);
        });
    	} 
    	
        // 메시지 전송
        function getStompClient()
        {
        	return stompClient;
        }
        return{
        	 connect : connect,
        	 getStompClient : getStompClient,
        }
	}

const lobbyStomp = createLobbyStomp();
export default lobbyStomp; 

setTimeout(function() {
	lobbyStomp.connect();
	const ishost = updateHostSession();
	console.log(ishost);
	if(ishost != "true")
	{
		$('.delegation-host-button').css('display', 'none');
		$('.kick-button').css('display', 'none');
	}
	}, 50);
setTimeout(function() {
	roomSend.joinMessage(lobbyStomp.getStompClient(), roomIdGlobal);
    
	}, 200);

setTimeout(function() {
	roomSend.enterMessage(lobbyStomp.getStompClient(), enterMessages);
    
	}, 300);