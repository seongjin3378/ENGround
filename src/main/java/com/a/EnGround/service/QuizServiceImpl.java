package com.a.EnGround.service;

import java.util.HashMap;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import com.a.EnGround.quiz.FindQuizSystem;
import com.a.EnGround.quiz.QuizSystem;
import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.QuizVO;
import com.a.EnGround.vo.SessionVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Qualifier("quizService")
@Service
public class QuizServiceImpl implements QuizService{
	 @Autowired
	    private FindQuizSystem findQuizSystem;

	    public QuizVO loadQuiz(HttpSession session) throws JsonMappingException, JsonProcessingException {
	        QuizSystem quizService = findQuizSystem.findQuizService(session);
	        SessionVO sessionVO = new SessionVO(session);
	        try {
	        	
	        	GameRoomVO gameRoom = quizService.loadGameRoom(sessionVO.getCategoryName(), sessionVO.getUserId(), sessionVO.getRoomId());
	            if(gameRoom.getQuizRound() == -1)
	            {
	            	System.out.println("quizService 실행 quizRound == -1");
	            	return null;
	            	
	            	
	            }
	            HashMap<Integer, QuizVO> questions = quizService.loadQuestion(gameRoom, sessionVO.getCategoryName(), findQuizSystem.getQuizTable(session), sessionVO.getUserId());
	            return questions.get(gameRoom.getQuizRound());
	            
	            
	        } catch (NullPointerException e) {
	            HashMap<Integer, QuizVO> questions = quizService.loadQuestion(null, sessionVO.getCategoryName(), findQuizSystem.getQuizTable(session), sessionVO.getUserId());
	            
	            GameRoomVO gameRoom = quizService.loadGameRoom(sessionVO.getCategoryName(), sessionVO.getUserId(), sessionVO.getRoomId());
	            questions.get(gameRoom.getQuizRound() + 1).setNewData(true);
	            return questions.get(gameRoom.getQuizRound() + 1);
	        }
	    }

	    public void checkPoint(HttpSession session) throws JsonMappingException, JsonProcessingException {
	    	SessionVO sessionVO = new SessionVO(session);
	        try {
	            QuizSystem quizService = findQuizSystem.findQuizService(session);
	            GameRoomVO gameRoom = quizService.loadGameRoom(sessionVO.getCategoryName(), sessionVO.getUserId(), sessionVO.getRoomId());
	            HashMap<Integer, QuizVO> questions = quizService.loadQuestion(gameRoom, sessionVO.getCategoryName(), findQuizSystem.getQuizTable(session), sessionVO.getUserId());
	            quizService.setPrevQuestion(questions, gameRoom);
	            deleteGameRoom(session);
	        } catch (NullPointerException e) {
	            // Handle exception
	        }
	    }

	    public void deleteGameRoom(HttpSession session) throws JsonMappingException, JsonProcessingException {
	    	SessionVO sessionVO = new SessionVO(session);
	        QuizSystem quizService = findQuizSystem.findQuizService(session);
	        GameRoomVO gameRoom = quizService.loadGameRoom(sessionVO.getCategoryName(), sessionVO.getUserId(), sessionVO.getRoomId());
	        quizService.deleteGameRoom(gameRoom);
	    }

	    public void insertHistoryAndDeleteGameRoom(HttpSession session, Model model) throws JsonMappingException, JsonProcessingException {
	        QuizSystem quizService = findQuizSystem.findQuizService(session);
	        SessionVO sessionVO = new SessionVO(session);
	        GameRoomVO gameRoom = quizService.loadGameRoom(sessionVO.getCategoryName(), sessionVO.getUserId(), sessionVO.getRoomId());
	        HashMap<Integer, QuizVO> questions = quizService.loadQuestion(gameRoom, sessionVO.getCategoryName(), findQuizSystem.getQuizTable(session), sessionVO.getUserId());
	        
	        
	        model.addAttribute("vo", gameRoom);
	        model.addAttribute("length", questions.size());
	        gameRoom.setUserId(sessionVO.getUserId());
	        quizService.insertHistory(gameRoom);
	        deleteGameRoom(session);
	    }

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

	    public String[] getHint(String hint, HttpSession session) throws JsonMappingException, JsonProcessingException {
	        QuizSystem quizService = findQuizSystem.findQuizService(session);
	        SessionVO sessionVO = new SessionVO(session);
	        
	        GameRoomVO gameRoom = quizService.loadGameRoom(sessionVO.getCategoryName(), sessionVO.getUserId(), sessionVO.getRoomId());
	        if (gameRoom.getQuizRound() == -1) {
	            return null;
	        }
	        
	        HashMap<Integer, QuizVO> questions = quizService.loadQuestion(gameRoom, sessionVO.getCategoryName(), findQuizSystem.getQuizTable(session), sessionVO.getUserId());
	        return quizService.showStepOfHint(hint, questions, gameRoom);
	    }

	    public boolean checkAnswer(String correct, HttpSession session) throws JsonMappingException, JsonProcessingException {
	        QuizSystem quizService = findQuizSystem.findQuizService(session);
	        SessionVO sessionVO = new SessionVO(session);
	        GameRoomVO gameRoom = quizService.loadGameRoom(sessionVO.getCategoryName(), sessionVO.getUserId(), sessionVO.getRoomId());
	        HashMap<Integer, QuizVO> questions = quizService.loadQuestion(gameRoom, sessionVO.getCategoryName(), findQuizSystem.getQuizTable(session), sessionVO.getUserId());
	        return quizService.checkAnswerAndUpdateScore(correct, questions, gameRoom);
	    }

	    public String generateAudio() {
	        return "tts";
	    }
	    

}