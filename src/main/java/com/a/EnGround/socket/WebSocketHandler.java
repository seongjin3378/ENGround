package com.a.EnGround.socket;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class WebSocketHandler extends TextWebSocketHandler{

	
	
	private final ObjectMapper objectMapper; 
    //payload를 ChatMessage 객체로 만들어 주기 위한 objectMapper
	private Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
	@Autowired
	public WebSocketHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    	/*
        String payload = message.getPayload(); //메세지를 가져오기

        final ChatMessageVO chatMessage = objectMapper.readValue(payload, ChatMessageVO.class);
        //payload를 ChatMessage 객체로 만들어주기
        if(chatMessage.getType().equals(ChatMessageVO.MessageType.ENTER))
        {
        	session.sendMessage(new TextMessage("Hello Client!"));
        	sessions.add(session);
        }
        if(chatMessage.getType().equals(ChatMessageVO.MessageType.TALK))
        {
        	sessions.parallelStream().forEach(s -> {
                try {
                    if (s.isOpen()) {
                        s.sendMessage(new TextMessage(chatMessage.getMessage()));
                      
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        	
        }
        */
    
}




}
