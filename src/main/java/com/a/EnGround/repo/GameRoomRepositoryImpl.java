package com.a.EnGround.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.QuizVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//
@Repository
public class GameRoomRepositoryImpl implements GameRoomRepository{
	 private static final String GAME_MAPPER = "com.a.EnGround.repo.GameRoomRepository";
	 private static final String GSESSION_MAPPER = "com.a.EnGround.repo.GameSessionRepository";
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	@Autowired
	ObjectMapper objectMapper;
	@Override
	public GameRoomVO findGameInform(String roomId, String gameMode) {
	 
	    Map<String, Object> params = new HashMap<>();
	    	
	    params.put("roomId", roomId);
	    params.put("gameMode", gameMode);
		return sqlSessionTemplate.selectOne(GAME_MAPPER + ".findGameInform", params);
	}

	@Override
	public int saveGameInform() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int gameEndAfterInsertHistory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void insertGameInform(String roomId, String gameMode, HashMap<Integer, QuizVO> quiz) throws JsonProcessingException {
		 String quizJson = objectMapper.writeValueAsString(quiz);
		 Map<String, Object> params = new HashMap<>();
		 params.put("roomId", roomId);
		 params.put("gameMode", gameMode);
		 params.put("quiz", quizJson);
		 
		 System.out.println("++++++++++++++++"+roomId+gameMode+quizJson+"+++++++++++++++++++++++++++++");
		 sqlSessionTemplate.insert(GAME_MAPPER + ".insertGameInform", params);
	}

	@Override
	public void updateQuizRound(GameRoomVO vo) {
		 Map<String, Object> params = new HashMap<>();
		 

		 params.put("roomId", vo.getRoomId());
		 params.put("quizRound", vo.getQuizRound());
		 params.put("gameMode", vo.getGameMode());
		 sqlSessionTemplate.update(GAME_MAPPER + ".updateQuizRound", params);
	}
	
	/* 다른곳에서 쓸거임 ㄴㄴ
	@Override
	public void updateScore(GameRoomVO vo) {
		 Map<String, Object> params = new HashMap<>();
		 
		 params.put("roomId", vo.getRoomId());
		 params.put("gameMode", vo.getGameMode());
		 
		 System.out.println("============"+vo.getRoomId()+" "+ vo.getGameMode() + vo.getUserId());
		 sqlSessionTemplate.update(NAME_SPACE + ".updateScore", params);
	}
	*/
	

	@Override
	public void deleteGameRoom(GameRoomVO vo) {
		Map<String, Object> params = new HashMap<>();
		params.put("roomId", vo.getRoomId());
		params.put("gameMode", vo.getGameMode());
		
		System.out.println("1:"+vo.getRoomId());
		System.out.println("1:"+vo.getGameMode());
		sqlSessionTemplate.delete(GAME_MAPPER + ".deleteGameRoom", params);
	}

	@Override
	public void updateUserType(GameRoomVO vo) {
		Map<String, Object> params = new HashMap<>();
		params.put("roomId", vo.getRoomId());
		sqlSessionTemplate.update(GAME_MAPPER + ".updateUserType", params);
		
	}

	@Override
	public List<GameRoomVO> findAllGameRoom() {

		return sqlSessionTemplate.selectList(GAME_MAPPER + ".findAllOnlineGameRoom");
	}

	@Override
	public void updateQuiz(GameRoomVO vo, HashMap<Integer, QuizVO> quiz) throws JsonProcessingException {
		String quizJson = objectMapper.writeValueAsString(quiz);
		Map<String, Object> params = new HashMap<>();
		params.put("roomId", vo.getRoomId());
		params.put("quiz", quizJson);
		sqlSessionTemplate.update(GAME_MAPPER + ".updateQuiz", params);
		
	}

	//온라인전용
	@Override
	public void insertGameInform(String roomId, String roomTitle, String gameMode,
			HashMap<Integer, QuizVO> quiz) throws JsonProcessingException {
		
		 Map<String, Object> params = new HashMap<>();
		 params.put("roomId", roomId);
		 params.put("roomTitle", roomTitle);
		 params.put("gameMode", gameMode);
		 sqlSessionTemplate.insert(GAME_MAPPER + ".insertGameInform", params);
		
	}


	


}
