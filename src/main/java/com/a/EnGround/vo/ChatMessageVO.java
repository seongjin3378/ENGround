package com.a.EnGround.vo;

public class ChatMessageVO {
	    public enum MessageType{
	        ENTER, TALK
	        //처음 입장인지 아닌지 구별하는 Enum
	    }
	    //단순 DTO
	    private MessageType type;
		private String roomId;
	    private String sender;
	    private String message;
	    
	    
	    public MessageType getType() {
			return type;
		}
		public void setType(MessageType type) {
			this.type = type;
		}
		public String getRoomId() {
			return roomId;
		}
		public void setRoomId(String roomId) {
			this.roomId = roomId;
		}
		public String getSender() {
			return sender;
		}
		public void setSender(String sender) {
			this.sender = sender;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}

}
