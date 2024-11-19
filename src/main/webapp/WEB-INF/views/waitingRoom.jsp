<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>대기방</title>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>"/>
</head>
<body>
	<%@ include file="/WEB-INF/include/AD.jsp" %>
	 <div class="back-container">
		<%@ include file="/WEB-INF/include/header.jsp"%>
		<div class="wait-head">
			<h2>user님의 게임</h2>
		</div>
	   <div class="wait-container">
			<div class="waitUser-body">
				<div class="waitUser-userboard">
            		<div class="waitUser-user1">user1</div>
            		<div class="waitUser-user2">user2</div>
            		<div class="waitUser-user3">user3</div>
            		<div class="waitUser-user4">user4</div>
            		<div class="waitUser-user5">user5</div>
            		<div class="waitUser-user6">user6</div>
            	</div>
            </div>
       <div class="wait-chatlogBox">
            <div class="wait-chatlog">
            </div>
            <div class="wait-answer-container">
            	<input type="text" class="wait-input2" id="wait-input-answer" placeholder="입력 영역">
                	<span id="wait-answer-result" style="display:none"></span>
            	<input type="button" class="wait-answer-btn" value="입력">
            	<input type="button" class="wait-ready-btn" value="레디"  onclick="location.href ='/onlineGame'">
        	</div>
       	</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/include/side.jsp" %> <!-- 헤더 포함 -->
</body> 

	
	<div id="chatMessage"></div>
</body>
<!-- 강퇴는 나중에 넣음 -->
	<script>
	function createLobbyStomp()
	{
	const readyUser= {};
	let stompClient = null;
	const enterMessages = {
		    roomId: null,  // 초기값 설정
		    userId: null,  // 초기값 설정
		    message: null  // 초기값 설정
		};
	let isNotStart = true;
	window.addEventListener('beforeunload', function () {
	    {
	    if(isNotStart)
	    {
	       $.ajax(
	       {

	           type : "get",
	           url : "/deleteGameSession",
	           success : function(){
	        	   stompClient.send("/pub/${roomId}/exit");
	           }

	       }
	       )
	       }
	    }
	   })
	   
	$("#ready-button").on("click", function(){
		readyMessage();
	});
	$("#start-button").on("click", function(){
		startMessage();
	})
	$("#send-message").on("click", function(){
		chatMessage();
	})
	$("#kick-button").on("click", function(){
		kickMessage();
	})

	function chatMessage()
	{
		const chatMessage = {
		        roomId: '${roomId}',
		        userId: ['${sessionScope.user.id}'],
		        message: $("#chat-input").val()
		    };
		stompClient.send("/pub/chat/${roomId}", {}, JSON.stringify(chatMessage));
	}
	function kickMessage()
	{
		const chatMessage = {
		        roomId: '${roomId}',
		        userId: ['${sessionScope.user.id}'],
		        message: $("#chat-input").val()
		    };
		stompClient.send("/pub/chat/${roomId}", {}, JSON.stringify(chatMessage));
	}
	function readyMessage()
	{
		const isEmptyReadyUser = readyUser["${sessionScope.user.id}"] == null;
		const isReadyUserFalse = readyUser["${sessionScope.user.id}"] == false;
		const isReadyUserTrue = readyUser["${sessionScope.user.id}"] == true;
		if(isEmptyReadyUser || isReadyUserFalse)
		{
			console.log(readyUser);
		stompClient.send("/pub/ready/${roomId}/${sessionScope.user.id}");
		}
		else if(isReadyUserTrue)
		{
			
		stompClient.send("/pub/readyCancel/${roomId}/${sessionScope.user.id}");
		}
	}
	function startMessage()
	{
		const isNotEmptyFromReadyUser = JSON.stringify(readyUser) !== '{}';
		const isNotContainFalseFromReadyUser = (function(){
			let isReadyOk = true;
			Object.entries(readyUser).forEach(function([key, value]) {
        		if(value == false)//준비 상태인 값들은 화면에 그림
        		{
        		isReadyOk = false;
        		return true; //break문
        		}
			});
			return isReadyOk;
		})(); 
		if(isNotEmptyFromReadyUser && isNotContainFalseFromReadyUser)
		{
			
		stompClient.send("/pub/start/${roomId}");
		}
		console.log(isNotEmptyFromReadyUser+", "+ isNotContainFalseFromReadyUser);
	}
	

    	
    	function connect()
    	{
        const socket = new SockJS('/ws'); // 서버의 WebSocket 엔드포인트
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function (frame) {
            setConnected = true;
            
            // 게임에 입장했을 때 모든 유저 불러옴
            stompClient.subscribe('/user/${roomId}/joinRoom', function (message) { //
            	
            	const receivedMessage = JSON.parse(message.body); // 메시지 본문 파싱
            	var roomId = receivedMessage[0].roomId;
            	var userId = [];
            	var message = "${sessionScope.user.id}님이 입장했습니다.";
            	receivedMessage.forEach(function(message)
            	{
            	if(message.userId != '${sessionScope.user.id}') // 세션아이디가 userId가 같을 경우
            	{
            			userId.push(message.userId);
            			
            	}
            	})
            	var waitUserList = $('#waitUser-userboard').find('.waitUser-user');
            	console.log("+++++++++++++++++++유저리스트"+waitUserList);
            	$("#joinList").text(userId);
            	$("#joinList").append("${sessionScope.user.id}(나)");
            	enterMessages["roomId"] = roomId;
            	enterMessages["userId"] = userId; 
            	enterMessages["message"] = message; 
				console.log(enterMessages);
            	var isHost = ${sessionScope.host};
            	if(isHost) // 호스트일 경우
            	{
            		stompClient.send("/pub/sendReadyUser/${roomId}", {}, JSON.stringify(readyUser));
            	}
  				
            });
            
            
            //입장 메시지 받기(본인은 제외)
            stompClient.subscribe('/user/${sessionScope.user.id}/enterMessage', function(message)
            {
            	const receivedMessage = JSON.parse(message.body);
            	console.log("==============입장메시지:"+message.body);
            	$("#chatMessage").append(receivedMessage.message+"<br>");
            });
            //입장 후 준비 메시지 불러오기
            stompClient.subscribe('/user/${roomId}/getReadyUser', function(message)
            {
            	const receivedMessage = JSON.parse(message.body);
            	let readyUserHTML = "";
            	Object.entries(receivedMessage).forEach(function([key, value]) {
            		if(value == true)//준비 상태인 값들은 화면에 그림
            		{
            		readyUser[key] = value;
            		readyUserHTML = readyUserHTML + "<div id="+key+">"+key+"준비</div>";
            		}
            	});
            	$("#readyList").html(readyUserHTML);
            }
            );
            //준비
            stompClient.subscribe('/user/${roomId}/ready', function(message)
                    {
                    	const receivedMessage = JSON.parse(message.body);
                    	if(!$("#"+receivedMessage.userId).length)
                    	{
                    	$("#readyList").append("<div id="+receivedMessage.userId+">"+receivedMessage.userId+"준비</div>")
                    	readyUser[receivedMessage.userId] = true;
                    	}
                    	else{
                    		$("#"+receivedMessage.userId).text(receivedMessage.userId+"준비");
                    		readyUser[receivedMessage.userId] = true;
                    	}
                    	
                    });
            //준비취소
            stompClient.subscribe('/user/${roomId}/readyCancel', function(message)
                    {
                    	const receivedMessage = JSON.parse(message.body);
                    	$("#"+receivedMessage.userId).text("");
                    	readyUser[receivedMessage.userId] = false;
                    	console.log(receivedMessage.userId);
                    });
            
            //게임 종료후 게임룸에 있는 사용자들한테 메시지 보냄
            stompClient.subscribe('/user/${roomId}/disConnected', function(message)
                    {
                    	const receivedMessage = JSON.parse(message.body);
                    	const exitUserId = receivedMessage.userId;
                    	delete readyUser[exitUserId];
                    	$("#"+exitUserId).remove();
                    	readyUser[exitUserId] = false;
                    	$("#chatMessage").append(exitUserId+"님이 나가셨습니다.");
                    	console.log("exitUserId:"+exitUserId);
                    });
            //모두 준비되었으면 시작
            stompClient.subscribe('/user/${roomId}/start', function(message)
                    {
                    	const receivedMessage = message.body;
                    	console.log("start subscribe OK");
                    	if(receivedMessage == "OK")
                    	{
                    		isNotStart = false //deleteGameSession 실행 막음
                    		location.href= "/gameRoom"	
                    	}
                    });
            
            //방장위임
           stompClient.subscribe('/user/${sessionScope.user.id}/setHostSession', function(message)
        		   {
        	   			const receivedMessage = message.body;
        	   			if(receivedMessage == "OK")
                    	{
        	   		       $.ajax(
        	   		 	       {

        	   		 	           type : "get",
        	   		 	           url : "/setHostSession",
        	   		 	           success : function(){
        	   		 	        	 $('#ready-button').attr('id', 'start-button');
        	   		 	        	 $('#start-button').text('시작');
        	   		 	           }

        	   		 	       }
        	   		 	       )
                    	}
        		   }
        		   );
           stompClient.subscribe('/user/${roomId}/chat', function(message){
        	   const receivedMessage = JSON.parse(message.body);
        	   console.log("chatOk");
        	   $("#chatMessage").append(receivedMessage.userId[0]+": "+receivedMessage.message+"<br>");
           });
        }, function (error) {
            console.error('Connection error: ' + error);
        });
    	} 
    	
        // 메시지 전송
        function joinMessage()
        {

            stompClient.send("/pub/joinRoom/${roomId}"); // 메시지 전송
     
        }
        
        function EnterMessage()
        {
        	
        		 console.log(enterMessages);
        	stompClient.send("/pub/enterMessage", {}, JSON.stringify(enterMessages));
        	 
        }
        
        return{
        	 connect : connect,
        	 joinMessage : joinMessage,
        	 EnterMessage : EnterMessage
        }
	}
        
        
	const lobbyStomp = createLobbyStomp();


        window.onload = function() {
            lobbyStomp.connect();
          
        };
        setTimeout(function() {
            lobbyStomp.joinMessage();
            
        	}, 100);
        
        setTimeout(function() {
        	lobbyStomp.EnterMessage();
            
        	}, 200);

    </script>
    
</html>