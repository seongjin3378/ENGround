	export function delegationHostMessage(element, stompClient)
	{
		const delegationHostMessage = {
				roomId: roomIdGlobal,
		        userId: [element, sessionId],
		        message: ["OK"]
		    };
		stompClient.send("/pub/delegationHost", {}, JSON.stringify(delegationHostMessage));
	}
	export function chatMessage(stompClient)
	{
		const chatMessages = {
		        roomId: roomIdGlobal,
		        userId: [sessionId],
		        message: [$("#wait-input-answer").val()]
		    };
		stompClient.send("/pub/chat/"+roomIdGlobal, {}, JSON.stringify(chatMessages));
	}
	export function kickMessage(element, stompClient)
	{
		const kickMessage = {
		        roomId: roomIdGlobal,
		        userId: [element, sessionId],
		        message: [element+'님이 강퇴 당하셨습니다.', '강퇴당하셨습니다!']
		    };
		stompClient.send("/pub/kick", {}, JSON.stringify(kickMessage));
	}
	export function readyMessage(stompClient, readyUser)
	{
		const isEmptyReadyUser = readyUser[sessionId] == null;
		const isReadyUserFalse = readyUser[sessionId] == false;
		const isReadyUserTrue = readyUser[sessionId] == true;
		if(isEmptyReadyUser || isReadyUserFalse)
		{
		stompClient.send("/pub/ready/"+roomIdGlobal+"/"+sessionId);
		}
		else if(isReadyUserTrue)
		{
			
		stompClient.send("/pub/readyCancel/"+roomIdGlobal+"/"+sessionId);
		}
		
	}
	function setCategoryAjax()
	{
		const baseUrl = window.location.origin; // 예: http://localhost:8080
		const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1)); // 예: /ENGround0909
		const categoryValue = $("#category").val();
		const gameUrl = baseUrl + contextPath + "/game/" + categoryValue;
		 $.ajax(
			       {
			    	   
			           type : "get",
			           url : gameUrl,
			           success : function(){
			        	  console.log("성고오오옹");
			           }

			       }
			       );
	}
	function delay(ms) {
	    return new Promise(resolve => setTimeout(resolve, ms));
	}



	export function startMessage(stompClient, readyUser)
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
		setCategoryAjax();	
		delay(100).then(() => {
			stompClient.send("/pub/start/"+roomIdGlobal);
		});
		}
		console.log(isNotEmptyFromReadyUser+", "+ isNotContainFalseFromReadyUser);
	}
    export function joinMessage(stompClient, roomId)
    {
    	console.log(stompClient);
        stompClient.send("/pub/joinRoom/"+roomId); // 메시지 전송
 
    }
    
    export function enterMessage(stompClient, enterMessages)
    {
    	
    		 console.log(enterMessages);
    	stompClient.send("/pub/enterMessage", {}, JSON.stringify(enterMessages));
    	 
    }
    
