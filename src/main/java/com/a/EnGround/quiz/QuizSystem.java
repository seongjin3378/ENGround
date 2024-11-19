package com.a.EnGround.quiz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.HistoryVO;
import com.a.EnGround.vo.InGameQuizVO;
import com.a.EnGround.vo.QuizVO;
import com.a.EnGround.vo.SessionVO;
import com.a.EnGround.vo.UserVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
/*장성진*/
public interface QuizSystem {
	
	public String ConvertWordToBlank(String hint, List<InGameQuizVO> quiz, GameRoomVO gameRoomVo); // ConvertBlnakToChar 안에 결합

	public int findNextQuestion(List<InGameQuizVO>quiz, GameRoomVO gameRoomVo);
	public void setPrevQuestion(List<InGameQuizVO>quiz, GameRoomVO gameRoomVo);
	public List<String> findValByCatDB();
	
	public String[] showStepOfHint(String hint, List<InGameQuizVO>quiz, GameRoomVO gameRoomVo);
	
	public Boolean checkAnswerAndUpdateScore(String userInput, List<InGameQuizVO>quiz, GameRoomVO gameRoomVo);
	
	
	public List<InGameQuizVO> loadQuestion(GameRoomVO vo, String categoryNum, String table, String userId) throws JsonMappingException, JsonProcessingException;
	
	public GameRoomVO loadGameRoom(String categoryNum, String userId, String roomId) throws JsonMappingException, JsonProcessingException; //quiz 5개를 불러와 quizService 전역 변수에 저장
	
}
