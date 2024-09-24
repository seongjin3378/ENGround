package com.a.EnGround.vo;

import java.util.Optional;

import javax.servlet.http.HttpSession;

public class SessionVO {
	private UserVO user;
	private String categoryName;
	private String roomId;
	private boolean host;
	public String getUserId() {
		return user.getId();
	}

	public String getCategoryName() {
		return categoryName;
	}

	public String getRoomId() {
		return roomId;
	}

	public <T> SessionVO(HttpSession session) {
		this.user = (UserVO) session.getAttribute("user");
		this.categoryName = (String) session.getAttribute("categoryName");
		Optional<String> optionalRoomId = Optional.ofNullable((String)session.getAttribute("roomId"));
		this.roomId = optionalRoomId.orElse("null");
		Optional<Boolean> optionalHost = Optional.ofNullable((Boolean)session.getAttribute("host"));
		if(optionalHost.isPresent())
		{
		this.host = optionalHost.get();
		}
		
	}

	public boolean isHost() {
		return host;
	}




	
	
}
