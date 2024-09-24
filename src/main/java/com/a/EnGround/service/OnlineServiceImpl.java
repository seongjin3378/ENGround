package com.a.EnGround.service;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.a.EnGround.quiz.FindQuizSystem;
import com.a.EnGround.quiz.OnlineQuizSystemImpl;
import com.a.EnGround.quiz.QuizSystem;
import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.QuizVO;
import com.a.EnGround.vo.SessionVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
@Qualifier("onlineService")
@Service
public class OnlineServiceImpl implements QuizService{
	@Autowired
	private FindQuizSystem findQuizSystem;
	private static final Logger logger = LoggerFactory.getLogger(OnlineServiceImpl.class);
	@Override
	public QuizVO loadQuiz(HttpSession session) throws JsonMappingException, JsonProcessingException {
		QuizSystem quizService = findQuizSystem.findQuizService(session);
		SessionVO sessionVO = new SessionVO(session);
		GameRoomVO gameRoom = quizService.loadGameRoom(null, null, sessionVO.getRoomId());
		gameRoom.setHost(1);
		quizService.loadQuestion(gameRoom, sessionVO.getCategoryName(), findQuizSystem.getQuizTable(session), null);
		return null;
	}

	@Override
	public void checkPoint(HttpSession session) throws JsonMappingException, JsonProcessingException {
		//필요없음
	}

	@Override
	public void deleteGameRoom(HttpSession session) throws JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertHistoryAndDeleteGameRoom(HttpSession session, Model model)
			throws JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ResponseEntity<?> nextQuestion(HttpSession session) throws JsonMappingException, JsonProcessingException {
        QuizSystem quizService = findQuizSystem.findQuizService(session);
        SessionVO sessionVO = new SessionVO(session);
        GameRoomVO gameRoom = quizService.loadGameRoom(sessionVO.getCategoryName(), sessionVO.getUserId(), sessionVO.getRoomId());
        HashMap<Integer, QuizVO> questions = quizService.loadQuestion(gameRoom, sessionVO.getCategoryName(), findQuizSystem.getQuizTable(session), sessionVO.getUserId());

        int lastRound = questions.size();
        int currentQuizRound = quizService.findNextQuestion(questions, gameRoom);
        
        if (lastRound <= gameRoom.getQuizRound()) {
        	
            return ResponseEntity.status(302).header("Location", "/result").build();
        } else {
            QuizVO currentQuestion = questions.get(currentQuizRound);
            currentQuestion.setQuizRound(currentQuizRound);
            return ResponseEntity.ok(currentQuestion);
        }
	}

	@Override
	public String[] getHint(String hint, HttpSession session) throws JsonMappingException, JsonProcessingException {
		 QuizSystem quizService = findQuizSystem.findQuizService(session);
	        SessionVO sessionVO = new SessionVO(session);
	        
	        GameRoomVO gameRoom = quizService.loadGameRoom(null, null, sessionVO.getRoomId());
	        /*
	        if (gameRoom.getQuizRound() == -1) {
	            return null;
	        }
	        */
	        
	        HashMap<Integer, QuizVO> questions = quizService.loadQuestion(gameRoom, sessionVO.getCategoryName(), findQuizSystem.getQuizTable(session), null);
	        logger.info("hint {}, questions {}, gameRoom: {}", hint, questions, gameRoom );
	        return quizService.showStepOfHint(hint, questions, gameRoom);
	}

	@Override
	public boolean checkAnswer(String correct, HttpSession session)
			throws JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String generateAudio() {
		// TODO Auto-generated method stub
		return null;
	}


}
