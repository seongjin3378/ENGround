package com.a.EnGround.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

	
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    
    	  registry.addEndpoint("/ws").setAllowedOrigins("*")
          .withSockJS().setInterceptors(new CustomHandshakeInterceptor());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
    
    	 registry.setUserDestinationPrefix("/user");
    	 registry.setApplicationDestinationPrefixes("/pub");  // 클라이언트에서 보낼 때 사용할 접두사
         registry.enableSimpleBroker("/sub", "/user");// 메시지를 전달할 브로커 설정
    }




}
