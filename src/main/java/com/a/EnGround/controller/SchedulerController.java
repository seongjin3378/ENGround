package com.a.EnGround.controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.a.EnGround.scheduler.OnlineGameTimerScheduler;
import com.a.EnGround.service.QuizService;
import com.a.EnGround.vo.MessageVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Controller
public class SchedulerController {
	private static final Logger logger = LoggerFactory.getLogger(SchedulerController.class);
	@Autowired
	OnlineGameTimerScheduler onlineGameTimerScheduler;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Qualifier("onlineService")
	@Autowired
    private QuizService quizService;
	
	@PostConstruct
	void startTimeScheduler() 
	{
		onlineGameTimerScheduler.startScheduler();
		
	}
	@PreDestroy
    public void cleanUp() {
		onlineGameTimerScheduler.stopScheduler();
	}
	@MessageMapping("/insertTimeMap")
	void addTimeMap(@Payload MessageVO payload, SimpMessageHeaderAccessor headerAccessor) 
	{
		int timer = Integer.parseInt(payload.getMessage()[0]);
		onlineGameTimerScheduler.insertTimeMap(payload.getRoomId(), timer);
		String[] message = { String.valueOf(onlineGameTimerScheduler.getTimeMap(payload.getRoomId())) };
		payload.setMessage(message);
		messagingTemplate.convertAndSendToUser(payload.getRoomId(), "/getTimeMap", payload);
		HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get("HTTP_SESSION");
	}
	
	@MessageMapping("/removeTimeMap")
	void deleteTimeMapAndNextQuestion(@Payload MessageVO payload, SimpMessageHeaderAccessor headerAccessor) throws JsonMappingException, JsonProcessingException
	{
		HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get("HTTP_SESSION");
		onlineGameTimerScheduler.removeTimeMap(payload.getRoomId());
		ResponseEntity<?> result = quizService.nextQuestion(session);
		messagingTemplate.convertAndSendToUser(payload.getRoomId(), "/getNextQuiz", result.getBody());
		logger.info("0초에 실행 성공");
	}
	/*
	 * 
	@ResponseBody
	@RequestMapping(value="/schedulerGet", method = RequestMethod.GET)
	int getTimeMap(@RequestParam("roomId") String roomId)
	{
		return onlineGameTimerScheduler.getTimeMap(roomId);
	}
	*/
	
}
