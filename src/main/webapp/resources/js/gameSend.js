	export function setTimeMapMessage(stompClient, time)
	{
		const timeMapMessage = {
				roomId: roomIdGlobal,
		        userId: [],
		        message: [time]
		    };
		console.log(roomIdGlobal);
		stompClient.send("/pub/insertTimeMap", {}, JSON.stringify(timeMapMessage));
	}
	export function removeTimeMapMessage(stompClient)
	{
		var isHost = updateHostSession();
		console.log(typeof isHost); 
		if(isHost == "true")
		{
		const timeMapMessage = {
				roomId: roomIdGlobal,
		        userId: [],
		        message: []
		    };
		stompClient.send("/pub/removeTimeMap", {}, JSON.stringify(timeMapMessage));
		}
	}
	
	export function HintMessage(stompClient, hint)
	{
		var isHost = updateHostSession();
		
		if(isHost == "true")
		{
		const hintMessage = {
			roomId: roomIdGlobal,
			userId: [],
			message: [hint]
		};
		console.log(hintMessage);
		stompClient.send("/pub/getHint", {}, JSON.stringify(hintMessage));
		}
		
	}
	