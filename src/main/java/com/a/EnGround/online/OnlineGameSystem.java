package com.a.EnGround.online;

import java.io.IOException;
import java.util.List;

import org.springframework.web.socket.WebSocketSession;

import com.a.EnGround.vo.CategoryVO;
import com.a.EnGround.vo.ChatHistoryVO;
import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.GameSessionVO;
import com.a.EnGround.vo.ReportUserVO;
import com.a.EnGround.vo.SessionVO;
import com.fasterxml.jackson.core.JsonProcessingException;
/*장성진*/
public interface OnlineGameSystem {
	List<GameRoomVO> findAllRoom();
	String createRoom(String userId, String roomTitle) throws JsonProcessingException;
	void createGameSession(GameSessionVO gameSessionVO) throws JsonProcessingException;
	GameRoomVO findRoomById(String userId); 
	List<GameRoomVO> findUserByRoomId(String roomId);
	void deleteGameSession(SessionVO sessionVO);
	void deleteGameRoom(GameRoomVO vo);
	String delegationHost(String columnType, String ownId);
	List<CategoryVO> findAllCategory();
	List<ChatHistoryVO> findAllChatHistoryByRoomId(ChatHistoryVO vo);
	int insertReportUser(ReportUserVO vo);
}
