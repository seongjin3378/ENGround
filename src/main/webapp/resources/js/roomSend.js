	
/*장성진*/
export function delegationHostMessage(element, stompClient)
	{
		const delegationHostMessage = {
				roomId: roomIdGlobal,
		        userId: [element, sessionId],
		        message: ["OK"]
		    };
		stompClient.send("/pub/delegationHost", {}, JSON.stringify(delegationHostMessage));
	}
	
	function formatDate(date) {
	    const year = date.getFullYear();
	    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
	    const day = String(date.getDate()).padStart(2, '0');
	    const hours = String(date.getHours()).padStart(2, '0');
	    const minutes = String(date.getMinutes()).padStart(2, '0');
	    const seconds = String(date.getSeconds()).padStart(2, '0');

	    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
	}

	
	export function chatMessage(stompClient)
	{
		const now = new Date(Date.now());
		const formattedDate = formatDate(now);
		
		const chatMessages = {
		        roomId: roomIdGlobal,
		        userId: [sessionId],
		        message: [$("#wait-input-answer").val(), formattedDate]
		    };
		console.log(Date.now());
		stompClient.send("/pub/chat/"+roomIdGlobal, {}, JSON.stringify(chatMessages));
	}
	export function kickMessage(element, stompClient)
	{
		const kickMessage = {
		        roomId: roomIdGlobal,
		        userId: [element, sessionId],
		        message: [element, '강퇴당하셨습니다!']
		    };
		stompClient.send("/pub/kick", {}, JSON.stringify(kickMessage));
	}
	export function reportMessage(element, stompClient)
	{
		const reportMessage = {
			roomId: roomIdGlobal,
			userId: [element, sessionId],
			message: [$('#reportType').val(), $("#reportContent").val()]
		};
		stompClient.send("/pub/report", {}, JSON.stringify(reportMessage));
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
	export function setCategoryAjax() {
	        const baseUrl = window.location.origin; // 예: http://localhost:8080
	        const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1)); // 예: /ENGround0909
	        const categoryValue = $("#category").val();
	        const gameUrl = baseUrl + contextPath + "/game/" + categoryValue;

	        $.ajax({
	            type: "get",
	            url: gameUrl,
	            success: function() {
	                console.log("성공");
	            },
	            error: function(jqXHR, textStatus, errorThrown) {
	                console.error("AJAX 요청 실패:", textStatus, errorThrown);
	                reject(errorThrown); // 실패 시 reject 호출
	            }
	        });
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
			stompClient.send("/pub/start/"+roomIdGlobal);
		console.log(isNotEmptyFromReadyUser+", "+ isNotContainFalseFromReadyUser);
		}
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
    
