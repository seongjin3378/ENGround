package com.a.EnGround.service;

import java.time.LocalDateTime;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.HistoryVO;
import com.a.EnGround.vo.InGameQuizVO;
import com.a.EnGround.vo.QuizVO;
import com.a.EnGround.vo.SessionVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface QuizService {

	    public QuizVO loadQuiz(HttpSession session) throws JsonMappingException, JsonProcessingException;


	    public void checkPoint(HttpSession session) throws JsonMappingException, JsonProcessingException;

	    public void findHistoryAndDeleteGameInForm(HttpSession session, Model model) throws JsonMappingException, JsonProcessingException;


	    public ResponseEntity<?> nextQuestion(HttpSession session) throws JsonMappingException, JsonProcessingException;


	    public String[] getHint(String hint, HttpSession session) throws JsonMappingException, JsonProcessingException;


	    public String generateAudio();


		public Boolean checkAnswerAndUpdateScore(String correct, LocalDateTime answerTime, HttpSession session) throws JsonMappingException, JsonProcessingException;

		void deleteGameRoom(HttpSession session) throws JsonMappingException, JsonProcessingException;
		
		public void deleteGameSession(HttpSession session);
		public void deleteHistory(HttpSession session);
		
		public void insertHistory(HttpSession session);
		public void updateIsFinishedAll(HistoryVO vo); //온라인용
		
		
}
