package com.a.EnGround.controller;

import java.time.LocalDateTime;

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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.a.EnGround.repo.ChatHistoryRepository;
import com.a.EnGround.repo.GameRecordsRepository;
import com.a.EnGround.repo.GameSessionRepository;
import com.a.EnGround.repo.HistoryRepository;
import com.a.EnGround.scheduler.OnlineGameTimerScheduler;
import com.a.EnGround.service.QuizService;
import com.a.EnGround.vo.ChatHistoryVO;
import com.a.EnGround.vo.GameRecordsVO;
import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.HistoryVO;
import com.a.EnGround.vo.MessageVO;
import com.a.EnGround.vo.SessionVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
/*장성진*/
@Controller
public class OnlineInGameController {
	@Qualifier("onlineService")
	@Autowired
	private QuizService quizService;
	private static final Logger logger = LoggerFactory.getLogger(OnlineInGameController.class);
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private GameSessionRepository gameSessionRepository;
	
	@Autowired 
	private HistoryRepository historyRepository;
	@Autowired
	private GameRecordsRepository gameRecordsRepository;
	@Autowired
	private ChatHistoryRepository chatHistoryRepository; 
	@MessageMapping("/start/{roomId}")
	public void startGameAndLoadQuizAndInsertHistory(@DestinationVariable String roomId, SimpMessageHeaderAccessor headerAccessor)
			throws JsonMappingException, JsonProcessingException {
		HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get("HTTP_SESSION");
		quizService.loadQuiz(session);
		messagingTemplate.convertAndSendToUser(roomId, "/start", "OK");
	}

	@MessageMapping("/getHint")
	public void getHint(@Payload MessageVO payload, SimpMessageHeaderAccessor headerAccessor)
			throws JsonMappingException, JsonProcessingException {
		HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get("HTTP_SESSION");
		String hint = payload.getMessage()[0];
		logger.info("hint : {}, roomId: {}, session: {}", hint, payload.getRoomId(), session);
		messagingTemplate.convertAndSendToUser(payload.getRoomId(), "/getHint", quizService.getHint(hint, session));
	}

	@MessageMapping("/chat/{roomId}")
	public void sendChatOrAnswerCheck(@DestinationVariable String roomId, @Payload MessageVO payload,
			SimpMessageHeaderAccessor headerAccessor) throws JsonMappingException, JsonProcessingException {
		HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get("HTTP_SESSION");
		String correct = payload.getMessage()[0];
		LocalDateTime answerTime = LocalDateTime.parse(payload.getMessage()[1]);
		Boolean isFirstReq = quizService.checkAnswerAndUpdateScore(correct, answerTime, session);
		SessionVO sessionVO = new SessionVO(session);
		String sessionUserId = sessionVO.getUserId();
		if (isFirstReq == null) { /* 정답이 아니고 일반적인 채팅일 경우 */
			ChatHistoryVO chatHistoryVO = new ChatHistoryVO();
			chatHistoryVO.setUserId(payload.getUserId()[0]);
			chatHistoryVO.setContent(payload.getMessage()[0]);
			chatHistoryVO.setRoomId(payload.getRoomId());
			chatHistoryRepository.insertChatHistory(chatHistoryVO);
			
			
			messagingTemplate.convertAndSendToUser(roomId, "/chat", payload);
			logger.info("null 실행");
		} else if (isFirstReq) {
			messagingTemplate.convertAndSendToUser(sessionUserId, "/firstReqUserDelay", payload);
			session.setAttribute("firstReqUser", false);
			logger.info("isFirstReq false 실행");
		} else {
			MessageVO messageVO = new MessageVO();
			HistoryVO historyVO = new HistoryVO();
			historyVO.setUserId(sessionUserId);
			historyVO.setGameMode("online");
			int score = historyRepository.findScore(historyVO);
			messageVO.setMessage(new String[]{sessionUserId, String.valueOf(score)});
			logger.info("MESSAGE VO: {}", messageVO);
			messagingTemplate.convertAndSendToUser(roomId, "/correctUserAlert", messageVO);
			int correctCounts = gameRecordsRepository.findCorrectCounts(historyVO);
			logger.info("correctCounts: {}", correctCounts);
			messageVO.setMessage(new String[]{String.valueOf(correctCounts)});
			messagingTemplate.convertAndSendToUser(sessionUserId, "/showCorrectCounts", messageVO);
			
			logger.info("isFirstReq true 실행");
			session.setAttribute("firstReqUser", true);
		}

	}
	
    @RequestMapping(value = "/resultOnline", method = RequestMethod.GET)
    public String resultPage(Model model, HttpSession session) throws JsonMappingException, JsonProcessingException {
        quizService.findHistoryAndDeleteGameInForm(session, model);
        logger.info("result controller 실행");
        return "result";
    }

}
