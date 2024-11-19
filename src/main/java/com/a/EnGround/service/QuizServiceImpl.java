package com.a.EnGround.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.a.EnGround.quiz.FindQuizSystem;
import com.a.EnGround.quiz.QuizSystem;
import com.a.EnGround.repo.GameRecordsRepository;
import com.a.EnGround.repo.GameRoomRepository;
import com.a.EnGround.repo.GameSessionRepository;
import com.a.EnGround.repo.HistoryRepository;
import com.a.EnGround.repo.InGameQuizRepository;
import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.HistoryVO;
import com.a.EnGround.vo.InGameQuizVO;
import com.a.EnGround.vo.QuizVO;
import com.a.EnGround.vo.SessionVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;


/*장성진*/
@Qualifier("quizService")
@Service
public class QuizServiceImpl implements QuizService{
		@Autowired
	    private FindQuizSystem findQuizSystem;
	 	@Autowired
	 	private GameRecordsRepository gameRecordsRepository;
	 	@Autowired
	 	private HistoryRepository historyRepository;
	 	@Autowired
	 	private InGameQuizRepository inGameQuizRepository;
	 	@Autowired
	 	private GameRoomRepository gameRoomRepository;
	 	@Autowired
	 	private GameSessionRepository gameSessionRepository;
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
	            List<InGameQuizVO> questions = quizService.loadQuestion(gameRoom, sessionVO.getCategoryName(), findQuizSystem.getQuizTable(session), sessionVO.getUserId());
	            return questions.get(gameRoom.getQuizRound()).getQuiz();
	            
	            
	        } catch (NullPointerException e) {//GameRoom이 DB에 없을 경우 문제 생성 및 게임룸 DB 생성
	            List<InGameQuizVO> questions = quizService.loadQuestion(null, sessionVO.getCategoryName(), findQuizSystem.getQuizTable(session), sessionVO.getUserId());
	            insertHistory(session);
	            GameRoomVO gameRoom = quizService.loadGameRoom(sessionVO.getCategoryName(), sessionVO.getUserId(), sessionVO.getRoomId());
	            questions.get(gameRoom.getQuizRound() + 1).getQuiz().setNewData(true);
	            return questions.get(gameRoom.getQuizRound() + 1).getQuiz();
	        }
	    }
	    void historyScoreReset(String gameMode, String userId, int score)
	    {
            HistoryVO vo = new HistoryVO();
            vo.setGameMode(gameMode);
            vo.setUserId(userId);
            vo.setScore(score);
            historyRepository.updateScore(vo);
	    }
	    public void checkPoint(HttpSession session) throws JsonMappingException, JsonProcessingException {
	    	SessionVO sessionVO = new SessionVO(session);
	        try {
	            QuizSystem quizService = findQuizSystem.findQuizService(session);
	            GameRoomVO gameRoom = quizService.loadGameRoom(sessionVO.getCategoryName(), sessionVO.getUserId(), sessionVO.getRoomId());
	            List<InGameQuizVO> questions = quizService.loadQuestion(gameRoom, sessionVO.getCategoryName(), findQuizSystem.getQuizTable(session), sessionVO.getUserId());
	            quizService.setPrevQuestion(questions, gameRoom);
	            //deleteGameRoom(session);
	        } catch (NullPointerException e) {
	            // Handle exception
	        }
	    }



	    public void findHistoryAndDeleteGameInForm(HttpSession session, Model model) throws JsonMappingException, JsonProcessingException {
	        QuizSystem quizService = findQuizSystem.findQuizService(session);
	        SessionVO sessionVO = new SessionVO(session);
	        GameRoomVO gameRoom = quizService.loadGameRoom(sessionVO.getCategoryName(), sessionVO.getUserId(), sessionVO.getRoomId());
	        List<InGameQuizVO> questions = quizService.loadQuestion(gameRoom, sessionVO.getCategoryName(), findQuizSystem.getQuizTable(session), sessionVO.getUserId());
	        
	        HistoryVO historyVO = new HistoryVO();
	        historyVO.setUserId(sessionVO.getUserId());
	        historyVO.setGameMode(sessionVO.getGameMode());
	        historyVO.setScore(historyRepository.findScore(historyVO));
	        
	        model.addAttribute("vo", historyVO);
	        int correctCounts = gameRecordsRepository.findCorrectCounts(historyVO);
	        model.addAttribute("correctCounts", correctCounts);
	        model.addAttribute("length", questions.size());
	        historyRepository.updateIsFinishedByUserId(historyVO);
	        //deleteGameSession(session);
	        try {
	        deleteGameRoom(session);
	        }catch(DataIntegrityViolationException e)
	        {
	        	
	        }
	    }

	    public ResponseEntity<?> nextQuestion(HttpSession session) throws JsonMappingException, JsonProcessingException {
	        QuizSystem quizService = findQuizSystem.findQuizService(session);
	        SessionVO sessionVO = new SessionVO(session);
	        GameRoomVO gameRoom = quizService.loadGameRoom(sessionVO.getCategoryName(), sessionVO.getUserId(), sessionVO.getRoomId());
	        List<InGameQuizVO> questions = quizService.loadQuestion(gameRoom, sessionVO.getCategoryName(), findQuizSystem.getQuizTable(session), sessionVO.getUserId());

	        int lastRound = questions.size();
	        int currentQuizRound = quizService.findNextQuestion(questions, gameRoom);
	        
	        if (lastRound <= gameRoom.getQuizRound()) {
	        	String redirectUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/result")
                        .toUriString();
	            return ResponseEntity.status(302).header("Location", redirectUrl).build();
	        } else {
	            QuizVO currentQuestion = questions.get(currentQuizRound).getQuiz();
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
	        
	        List<InGameQuizVO> questions = quizService.loadQuestion(gameRoom, sessionVO.getCategoryName(), findQuizSystem.getQuizTable(session), sessionVO.getUserId());
	        return quizService.showStepOfHint(hint, questions, gameRoom);
	    }

	    public Boolean checkAnswerAndUpdateScore(String correct, LocalDateTime answerTime, HttpSession session) throws JsonMappingException, JsonProcessingException {
	        QuizSystem quizService = findQuizSystem.findQuizService(session);
	        SessionVO sessionVO = new SessionVO(session);
	        GameRoomVO gameRoom = quizService.loadGameRoom(sessionVO.getCategoryName(), sessionVO.getUserId(), sessionVO.getRoomId());
	        List<InGameQuizVO> questions = quizService.loadQuestion(gameRoom, sessionVO.getCategoryName(), findQuizSystem.getQuizTable(session), sessionVO.getUserId());
	        gameRoom.setAnswerTime(answerTime);
	        gameRoom.setUserId(sessionVO.getUserId());
	        return quizService.checkAnswerAndUpdateScore(correct, questions, gameRoom);
	    }

	    public String generateAudio() {
	        return "tts";
	    }


		@Override
		public void insertHistory(HttpSession session) {
			SessionVO sessionVO = new SessionVO(session);
	        HistoryVO historyVO = new HistoryVO();
	        historyVO.setUserId(sessionVO.getUserId());
	        historyVO.setGameMode(sessionVO.getGameMode());
	        historyRepository.insertHistory(historyVO);
			
		}
		@Override
		public void deleteGameSession(HttpSession session) {
			SessionVO sessionVO = new SessionVO(session);
			gameSessionRepository.deleteGameSession(sessionVO);
			
		}
	    public void deleteGameRoom(HttpSession session) throws JsonMappingException, JsonProcessingException {
			SessionVO sessionVO = new SessionVO(session);
	        QuizSystem quizService = findQuizSystem.findQuizService(session);
	        GameRoomVO gameRoomVO = quizService.loadGameRoom(sessionVO.getCategoryName(), sessionVO.getUserId(), sessionVO.getRoomId());
	        gameRoomVO.setRoomId(sessionVO.getUserId() + "_" + sessionVO.getGameMode());
	        gameRoomVO.setGameMode(sessionVO.getGameMode());
	        gameRoomRepository.deleteGameRoom(gameRoomVO);
	    }
	    
		@Override
		public void deleteHistory(HttpSession session) {
			SessionVO sessionVO = new SessionVO(session);
			HistoryVO vo = new HistoryVO();
			vo.setGameMode(sessionVO.getGameMode());
			vo.setUserId(sessionVO.getUserId());
			historyRepository.deleteHistory(vo);
			
		}
		@Override
		public void updateIsFinishedAll(HistoryVO vo) {
			
			historyRepository.updateIsFinishedByUserId(vo);
			
		}


	    

}