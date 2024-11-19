package com.a.EnGround.repo;

import java.util.HashMap;
import java.util.List;

import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.QuizVO;
import com.a.EnGround.vo.UserVO;
import com.fasterxml.jackson.core.JsonProcessingException;
//
public interface GameRoomRepository {
	GameRoomVO findGameInform(String roomId, String gameMode);
	int saveGameInform();
	int gameEndAfterInsertHistory();
	void updateQuizRound(GameRoomVO vo);
	void updateUserType(GameRoomVO vo);
	void deleteGameRoom(GameRoomVO vo);
	List<GameRoomVO> findAllGameRoom(); // 온라인전용
	void updateQuiz(GameRoomVO vo, HashMap<Integer, QuizVO> quiz) throws JsonProcessingException;
	void insertGameInform(String roomId, String roomTitle, String gameMode)
			throws JsonProcessingException;

}
