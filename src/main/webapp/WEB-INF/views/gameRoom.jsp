<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
</head>
<body>
    <h2>WebSocket Chat Client</h2>
    <div id="questionBox"></div>
    <div>
        <input type="text" id="messageInput" placeholder="Enter your message here">
        <button onClick="sendMessage()">Send</button>
    </div>
    <div id="RoomBox"></div>

<script>

function joinRoom_Ajax(roomIdArg, roomName)
{

	$.ajax(
			{
				type : "get",
				url : "/ENGround0909/room/"+roomIdArg+"/user/"+roomName,
				data :{
					
				},
				success : function(data){
					
					location.href = "/ENGround0909/room/"+roomIdArg
					
				}
					
			}
			)
}
//게임에 나가거나 새로고침 했을 경우

 
function findAllRoom_Ajax()
{
	$.ajax(
			{
				type : "get",
				url : "/ENGround0909/findAllRoom",
				success : function(data){
					console.log(data);
					data.forEach(function(datum)
					{
						let roomName = datum.roomTitle;
						let userId = datum.userId;
						let roomId = datum.roomId;
						
						if(roomName.indexOf("\"") > -1)
						{
							roomName = roomName.replaceAll("\"", "\\\"");
						}
						$("#RoomBox").append("<div>방이름:"+roomName.replaceAll("\\\"", "\"")+"</div>"+"<div>방장:"+userId+"</div><button onclick='joinRoom_Ajax(\""+roomId+"\", \""+roomName+"\")'></button>");
					})
				}
					
			}
			)
}



$(function(){
	findAllRoom_Ajax();
})
</script>



</body>
</html>