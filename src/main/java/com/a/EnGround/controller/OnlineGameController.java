package com.a.EnGround.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.a.EnGround.scheduler.OnlineGameTimerScheduler;
import com.a.EnGround.service.QuizService;
import com.a.EnGround.vo.MessageVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class OnlineGameController {
	@Qualifier("onlineService")
	@Autowired
    private QuizService quizService;
	private static final Logger logger = LoggerFactory.getLogger(OnlineGameController.class);
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private ObjectMapper objectMapper;
	
	@MessageMapping("/start/{roomId}")
	public void startGameAndLoadQuiz(@DestinationVariable String roomId, SimpMessageHeaderAccessor headerAccessor) throws JsonMappingException, JsonProcessingException
	{
		HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get("HTTP_SESSION");
		messagingTemplate.convertAndSendToUser(roomId, "/start", "OK");
		String loadQuizJson = objectMapper.writeValueAsString(quizService.loadQuiz(session));
	}
	@MessageMapping("/getHint")
	public void getHint(@Payload MessageVO payload, SimpMessageHeaderAccessor headerAccessor) throws JsonMappingException, JsonProcessingException
	{
		HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get("HTTP_SESSION");
		String hint = payload.getMessage()[0];
		logger.info("hint : {}, roomId: {}, session: {}", hint, payload.getRoomId(), session);
		messagingTemplate.convertAndSendToUser(payload.getRoomId(), "/getHint", quizService.getHint(hint, session));
	}
}

