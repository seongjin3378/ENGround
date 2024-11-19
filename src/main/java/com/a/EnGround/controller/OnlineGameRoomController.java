package com.a.EnGround.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriBuilder;

import com.a.EnGround.online.OnlineGameSystem;
import com.a.EnGround.repo.ChatHistoryRepository;
import com.a.EnGround.repo.GameRecordsRepository;
import com.a.EnGround.repo.GameSessionRepository;
import com.a.EnGround.repo.HistoryRepository;
import com.a.EnGround.repo.ReportUserRepository;
import com.a.EnGround.scheduler.OnlineGameTimerScheduler;
import com.a.EnGround.service.QuizService;
import com.a.EnGround.vo.CategoryVO;
import com.a.EnGround.vo.ChatHistoryVO;
import com.a.EnGround.vo.GameRecordsVO;
import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.GameSessionVO;
import com.a.EnGround.vo.HistoryVO;
import com.a.EnGround.vo.MessageVO;
import com.a.EnGround.vo.QuizVO;
import com.a.EnGround.vo.ReportUserVO;
import com.a.EnGround.vo.SessionVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
public class OnlineGameRoomController {
	@Qualifier("onlineService")
	@Autowired
	private QuizService quizService;
	@Autowired
	private OnlineGameSystem onlineGameSystem;
	private static final Logger logger = LoggerFactory.getLogger(OnlineGameRoomController.class);
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private ReportUserRepository reportUserRepository;
	@ResponseBody
	@GetMapping("/findAllRoom")
	public List<GameRoomVO> findAllRoom()
	{
		List<GameRoomVO> vo = onlineGameSystem.findAllRoom();
		return vo;
	}
	
	@GetMapping("/createRoomModal")
	public String createRoomModalView()
	{
		return "createRoom";
	}


	@PostMapping("/createRoom")
	public String createRoom(GameRoomVO vo, HttpSession session, HttpServletResponse response) throws IOException
	{
		SessionVO sessionVO = new SessionVO(session);
		String roomId = onlineGameSystem.createRoom(sessionVO.getUserId(), vo.getRoomTitle());
		String roomTitle = vo.getRoomTitle();
		session.setAttribute("roomId", roomId);
		session.setAttribute("roomTitle", roomTitle);

	      
		return "redirect:/room/hostEnterRoom";
	}
	
	@GetMapping("/room/hostEnterRoom")
	public String hostEnterRoom(HttpSession session) throws JsonProcessingException
	{
		SessionVO sessionVO = new SessionVO(session);
		GameSessionVO gameSessionVO = new GameSessionVO();
		gameSessionVO.setRoomId(sessionVO.getRoomId());
		gameSessionVO.setUserId(sessionVO.getUserId());
		gameSessionVO.setHost(true); // 호스트설정
		session.setAttribute("host", true);

		onlineGameSystem.createGameSession(gameSessionVO);
		return "redirect:/room/"+gameSessionVO.getRoomId();
		
	}
	
	@GetMapping("/room/userEnterRoom")
	public 	String userEnterRoom(GameSessionVO gameSessionVO, HttpSession session) throws JsonProcessingException
	{
		SessionVO sessionVO = new SessionVO(session);
		
		

		gameSessionVO.setHost(false);
		gameSessionVO.setUserId(sessionVO.getUserId());
		session.setAttribute("host", false);		
		onlineGameSystem.createGameSession(gameSessionVO);
		session.setAttribute("roomId", gameSessionVO.getRoomId());
		session.setAttribute("roomTitle", gameSessionVO.getRoomTitle());
		
		return "redirect:/room/"+gameSessionVO.getRoomId();
	}
	
	@GetMapping("/room/{roomId}")
	public 	String testRoomView(Model model, HttpSession session) throws JsonProcessingException
	{
		List<CategoryVO> category= onlineGameSystem.findAllCategory();
		SessionVO sessionVO = new SessionVO(session);
		ChatHistoryVO vo = new ChatHistoryVO();
		vo.setRoomId(sessionVO.getRoomId());
		List<ChatHistoryVO> chatHistoryList = onlineGameSystem.findAllChatHistoryByRoomId(vo);
		model.addAttribute("category", category);
		model.addAttribute("chatHistoryList", chatHistoryList);
		quizService.insertHistory(session);
		return "testRoom";
	}
	
	@PostMapping("/room/{roomId}")
	public String enterGameRoomAndInsertHistory(HttpSession session)
	{	
		
		return "onlineGame";
	}

	@GetMapping("/deleteGameSession")
	public void deleteGameSession(HttpSession session)
	{
		SessionVO sessionVO = new SessionVO(session);
		onlineGameSystem.deleteGameSession(sessionVO);
		logger.info("게임삭제 요청");
		if(sessionVO.isHost() == true) // session안에 호스트가 방장일경우
		{
			try {
				GameRoomVO vo = new GameRoomVO();
				vo.setRoomId(sessionVO.getRoomId());
				vo.setGameMode("online");
				onlineGameSystem.deleteGameRoom(vo);
				logger.info("방장게임룸삭제");
			}
			catch(DataIntegrityViolationException e) // 방장이 나갈 경우 방장위임 처리
			{
				logger.info("방장위임 try/catch 실행");
				String userId = onlineGameSystem.delegationHost(sessionVO.getRoomId(), null);
				logger.info("userId: "+ userId);
				messagingTemplate.convertAndSendToUser(userId, "/setHostSession", "OK"); // 호스트 세션 추가
			}
		}
		messagingTemplate.convertAndSendToUser(sessionVO.getRoomId(), "/disConnected", "{\"userId\": \""+sessionVO.getUserId()+"\"}");
	}
	@MessageMapping("/delegationHost")
	public void delegationHost(@Payload MessageVO payload)
	{
		logger.info("방장위임 실행");
		String userId = onlineGameSystem.delegationHost(payload.getUserId()[0], payload.getUserId()[1]);
		logger.info("delegationHost : userId: "+ payload.getUserId()[0]);
		messagingTemplate.convertAndSendToUser(payload.getUserId()[0], "/setHostSession", "OK"); // 호스트 세션 추가
	}
	@ResponseBody
	@GetMapping("/setHostSession")
	public String setHostSession(HttpSession session)
	{
		session.setAttribute("host", true);
		return "OK";
	}
	
	@MessageMapping("/joinRoom/{roomId}")
	public void joinRoom(@DestinationVariable String roomId)
	{
		List<GameRoomVO> vo = onlineGameSystem.findUserByRoomId(roomId);
		messagingTemplate.convertAndSendToUser(roomId, "/joinRoom", vo);
		
	}

	@MessageMapping("/ready/{roomId}/{userId}")
	public void ready(@DestinationVariable String roomId, @DestinationVariable String userId)
	{
		
		messagingTemplate.convertAndSendToUser(roomId, "/ready", "{\"userId\": \""+userId+"\", \"ready\":true}");
	}
	
	@MessageMapping("/readyCancel/{roomId}/{userId}")
	public void readyCancel(@DestinationVariable String roomId, @DestinationVariable String userId)
	{
		messagingTemplate.convertAndSendToUser(roomId, "/readyCancel", "{\"userId\": \""+userId+"\", \"ready\":false}");
	}

	@MessageMapping("/sendReadyUser/{roomId}")
	public void sendReadyUser(@DestinationVariable String roomId, String messages)
	{
		logger.info("sendReadyUser");
		messagingTemplate.convertAndSendToUser(roomId, "/getReadyUser", messages);
	}
	
	@MessageMapping("/kick")
	public void kick(@Payload MessageVO payload)
	{
		String[] getMessage = (String[]) payload.getMessage();
		logger.info("kickUser {}, {}", payload.getUserId()[0], getMessage[1]);
		messagingTemplate.convertAndSendToUser(payload.getUserId()[0], "/kickedOutStatus", getMessage[1]);
		messagingTemplate.convertAndSendToUser(payload.getRoomId(), "/kickedUserNotification", getMessage[0]);
	}
	
	
	@MessageMapping("/enterMessage")
	public void enterMessageNotify(@Payload MessageVO messages)
	{
		String[] userIds = messages.getUserId(); // userId 배열

		// userId 배열을 순회하며 로그 출력 (람다식 사용)
		for(String userId : userIds)
		{
			 try {
		messagingTemplate.convertAndSendToUser(userId, "/enterMessage", messages);
			  } catch (Exception e) {
		            logger.error("Error sending message to user: " + userId, e);
		        }
		
		}
		Arrays.stream(userIds).forEach(userId -> logger.info(userId));
	
		// 메시지 배열을 순회하며 로그 출력 (람다식 사용)
		
	}
	@MessageMapping("/report")
	public void report(@Payload MessageVO messages)
	{
		//0 신고유저 1 신고한사람
		String reportedId = messages.getUserId()[0];
		String reporterId = messages.getUserId()[1];
		String reportType = messages.getMessage()[0];
		String reportContent = messages.getMessage()[1];
		ChatHistoryVO chatHistoryVO = new ChatHistoryVO();
		chatHistoryVO.setRoomId(messages.getRoomId());
		chatHistoryVO.setUserId(reportedId);
		ReportUserVO reportUserVO = new ReportUserVO();
		reportUserVO.setChatHistoryRoomId(messages.getRoomId());
		reportUserVO.setIdReported(reporterId);
		reportUserVO.setReportedId(reportedId);
		reportUserVO.setReportType(reportType);
		reportUserVO.setReportContent(reportContent);
		int isDone = reportUserRepository.insertReport(reportUserVO);
		if(isDone == 1)
		{
			messagingTemplate.convertAndSendToUser(reporterId, "/report", reportedId+"님이 신고 되었습니다!");
		}else {
			messagingTemplate.convertAndSendToUser(reporterId, "/report", "오류가 발생했습니다.");
		}
		
	}

}
