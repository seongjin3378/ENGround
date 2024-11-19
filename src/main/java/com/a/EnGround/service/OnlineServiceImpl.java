package com.a.EnGround.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.a.EnGround.online.OnlineGameSystem;
import com.a.EnGround.quiz.FindQuizSystem;
import com.a.EnGround.quiz.OnlineQuizSystemImpl;
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
@Qualifier("onlineService")
@Service
public class OnlineServiceImpl implements QuizService {
	@Autowired
	private FindQuizSystem findQuizSystem;
	@Autowired
	private OnlineGameSystem onlineGameSystem;
	@Autowired
	private HistoryRepository historyRepository;
	@Autowired
	private GameRecordsRepository gameRecordsRepository;
	@Autowired
	private GameSessionRepository gameSessionRepository;
	@Autowired
	private GameRoomRepository gameRoomRepository;
	@Autowired
	private InGameQuizRepository inGameQuizRepository;

	private static final Logger logger = LoggerFactory.getLogger(OnlineServiceImpl.class);

	@Override
	public QuizVO loadQuiz(HttpSession session) throws JsonMappingException, JsonProcessingException {
		QuizSystem quizService = findQuizSystem.findQuizService(session);
		SessionVO sessionVO = new SessionVO(session);
		GameRoomVO gameRoom = quizService.loadGameRoom(null, null, sessionVO.getRoomId());
		gameRoom.setHost(1);
		gameRoom.setOnlineGameStart(true);
		quizService.loadQuestion(gameRoom, sessionVO.getCategoryName(), findQuizSystem.getQuizTable(session), null);
		return null;
	}

	@Override
	public void checkPoint(HttpSession session) throws JsonMappingException, JsonProcessingException {
		// 필요없음
	}

	public void findHistoryAndDeleteGameInForm(HttpSession session, Model model)
			throws JsonMappingException, JsonProcessingException {
		QuizSystem quizService = findQuizSystem.findQuizService(session);
		SessionVO sessionVO = new SessionVO(session);
		GameRoomVO gameRoom = quizService.loadGameRoom(sessionVO.getCategoryName(), sessionVO.getUserId(),
				sessionVO.getRoomId());
		// HashMap<Integer, QuizVO> questions = quizService.loadQuestion(gameRoom,
		// sessionVO.getCategoryName(), findQuizSystem.getQuizTable(session),
		// sessionVO.getUserId());

		HistoryVO historyVO = new HistoryVO();
		historyVO.setUserId(sessionVO.getUserId());
		historyVO.setGameMode(sessionVO.getGameMode());
		historyVO.setScore(historyRepository.findScore(historyVO));

		model.addAttribute("vo", historyVO);
		int correctCounts = gameRecordsRepository.findCorrectCounts(historyVO);
		model.addAttribute("correctCounts", correctCounts);
		HistoryVO vo = new HistoryVO();
		vo.setRoomId(sessionVO.getRoomId());
		updateIsFinishedAll(vo);
		// model.addAttribute("length", questions.size());

	}

	@Override
	public ResponseEntity<?> nextQuestion(HttpSession session) throws JsonMappingException, JsonProcessingException {
		QuizSystem quizService = findQuizSystem.findQuizService(session);
		SessionVO sessionVO = new SessionVO(session);
		GameRoomVO gameRoom = quizService.loadGameRoom(sessionVO.getCategoryName(), sessionVO.getUserId(),
				sessionVO.getRoomId());
		List<InGameQuizVO> questions = quizService.loadQuestion(gameRoom, sessionVO.getCategoryName(),
				findQuizSystem.getQuizTable(session), sessionVO.getUserId());

		int lastRound = questions.size();
		int currentQuizRound = quizService.findNextQuestion(questions, gameRoom);

		if (lastRound <= gameRoom.getQuizRound()) {
			//String redirectUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/result").toUriString();
			String redirectUrl = "/result";
			return ResponseEntity.status(302).header("Location", redirectUrl).build();
		} else {
			QuizVO currentQuestion = questions.get(currentQuizRound).getQuiz();
			currentQuestion.setQuizRound(currentQuizRound);
			return ResponseEntity.ok(currentQuestion);
		}
	}

	@Override
	public String[] getHint(String hint, HttpSession session) throws JsonMappingException, JsonProcessingException {
		QuizSystem quizService = findQuizSystem.findQuizService(session);
		SessionVO sessionVO = new SessionVO(session);

		GameRoomVO gameRoom = quizService.loadGameRoom(null, null, sessionVO.getRoomId());
		gameRoom.setCategoryName(sessionVO.getCategoryName());
		if (gameRoom.getQuizRound() == -1) {
			return null;
		}

		List<InGameQuizVO> questions = quizService.loadQuestion(gameRoom, sessionVO.getCategoryName(),
				findQuizSystem.getQuizTable(session), sessionVO.getUserId());
		return quizService.showStepOfHint(hint, questions, gameRoom);
	}

	@Override
	public Boolean checkAnswerAndUpdateScore(String correct, LocalDateTime answerTimeLine, HttpSession session)
			throws JsonMappingException, JsonProcessingException {
		QuizSystem quizService = findQuizSystem.findQuizService(session);
		SessionVO sessionVO = new SessionVO(session);
		GameRoomVO gameRoom = quizService.loadGameRoom(sessionVO.getCategoryName(), sessionVO.getUserId(),
				sessionVO.getRoomId());
		List<InGameQuizVO> questions = quizService.loadQuestion(gameRoom, sessionVO.getCategoryName(),
				findQuizSystem.getQuizTable(session), sessionVO.getUserId());
		logger.info("isFirstReqUser: {}", sessionVO.isFirstReqUser());
		gameRoom.setFirstReqUser(sessionVO.isFirstReqUser());
		gameRoom.setAnswerTime(answerTimeLine);
		gameRoom.setUserId(sessionVO.getUserId());
		Boolean isFirstReq = quizService.checkAnswerAndUpdateScore(correct, questions, gameRoom);
		return isFirstReq;
	}

	@Override
	public String generateAudio() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertHistory(HttpSession session) {
		SessionVO sessionVO = new SessionVO(session);
		HistoryVO historyVO = new HistoryVO();
		historyVO.setUserId(sessionVO.getUserId());
		historyVO.setGameMode(sessionVO.getGameMode());
		historyVO.setRoomId(sessionVO.getRoomId());
		historyRepository.insertHistory(historyVO);
	}

	@Override
	public void deleteGameRoom(HttpSession session) throws JsonMappingException, JsonProcessingException {
		SessionVO sessionVO = new SessionVO(session);
		QuizSystem quizService = findQuizSystem.findQuizService(session);
		GameRoomVO gameRoom = quizService.loadGameRoom(sessionVO.getCategoryName(), sessionVO.getUserId(),
				sessionVO.getRoomId());
		gameRoom.setRoomId(sessionVO.getRoomId());
		gameRoom.setGameMode(sessionVO.getGameMode());
		gameRoomRepository.deleteGameRoom(gameRoom);

	}

	@Override
	public void deleteGameSession(HttpSession session) {
		SessionVO sessionVO = new SessionVO(session);
		gameSessionRepository.deleteGameSession(sessionVO);
	}

	@Override
	public void deleteHistory(HttpSession session) {
		// TODO Auto-generated method stub

	}


	@Override
	public void updateIsFinishedAll(HistoryVO vo) {
		historyRepository.updateIsFinishedByRoomId(vo);
		
	}

}
