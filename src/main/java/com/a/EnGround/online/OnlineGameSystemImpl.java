package com.a.EnGround.online;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.a.EnGround.quiz.QuizSystem;
import com.a.EnGround.repo.CategoryRepository;
import com.a.EnGround.repo.ChatHistoryRepository;
import com.a.EnGround.repo.GameRoomRepository;
import com.a.EnGround.repo.GameSessionRepository;
import com.a.EnGround.repo.ReportUserRepository;
import com.a.EnGround.vo.CategoryVO;
import com.a.EnGround.vo.ChatHistoryVO;
import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.GameSessionVO;
import com.a.EnGround.vo.ReportUserVO;
import com.a.EnGround.vo.SessionVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class OnlineGameSystemImpl implements OnlineGameSystem{

	@Autowired
	private ObjectMapper objectMapper;
	private final GameRoomRepository gameRoomRepository;
	private final GameSessionRepository gameSessionRepository;
	private final CategoryRepository categoryRepository;
	private final ChatHistoryRepository chatHistoryRepository;
	private final ReportUserRepository reportUserRepository;
	@Autowired
    public OnlineGameSystemImpl(GameRoomRepository gameRoomRepository, GameSessionRepository gameSessionRepository, CategoryRepository categoryRepository, ChatHistoryRepository chatHistoryRepository, ReportUserRepository reportUserRepository) {
		this.gameRoomRepository = gameRoomRepository;
		this.gameSessionRepository = gameSessionRepository;
		this.categoryRepository = categoryRepository;
		this.chatHistoryRepository = chatHistoryRepository;
		this.reportUserRepository = reportUserRepository;
	}
	//방을 만들었을 때 gameroom db 생성
	public String createRoom(String userId, String room_title) throws JsonProcessingException { 
		 String roomId = UUID.randomUUID().toString();
		 gameRoomRepository.insertGameInform(roomId, room_title, "online");
		 return roomId;
	}
	
	//방에 입장했을 때 gameroom db 생성
	public void createGameSession(GameSessionVO gameSessionVO) throws JsonProcessingException {
		gameSessionRepository.insertGameSession(gameSessionVO);
	}
	
	public void deleteGameSession(SessionVO sessionVO)
	{
		gameSessionRepository.deleteGameSession(sessionVO);
	}
	
	@Override
	public List<GameRoomVO> findAllRoom() {
		// TODO Auto-generated method stub
		return gameRoomRepository.findAllGameRoom();
	}


	@Override
	public GameRoomVO findRoomById(String roomId) {
		
		return null;
	}

	@Override
	public List<GameRoomVO> findUserByRoomId(String roomId) {
		
		return gameSessionRepository.findUserInform(roomId);
	}
	@Override
	public void deleteGameRoom(GameRoomVO vo) {
		gameRoomRepository.deleteGameRoom(vo);
		
	}
	@Override
	public String delegationHost(String columnType, String ownId) {
		return gameSessionRepository.updateHostAndReturnId(columnType, ownId);
		
	}
	@Override
	public List<CategoryVO> findAllCategory() {
		
		return categoryRepository.getAllFromOnline();
	}
	@Override
	public List<ChatHistoryVO> findAllChatHistoryByRoomId(ChatHistoryVO vo) {
		
		return chatHistoryRepository.selectChatHistory(vo);
	}
	@Override
	public int insertReportUser(ReportUserVO vo) {

		return reportUserRepository.insertReport(vo);
		
	}

}
