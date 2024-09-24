package com.a.EnGround.quiz;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.a.EnGround.controller.AdminUserController;
import com.a.EnGround.repo.GameRoomRepository;
import com.a.EnGround.repo.HistoryRepository;
import com.a.EnGround.repo.QuizRepository;
import com.a.EnGround.scheduler.OnlineGameTimerScheduler;
import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.QuizVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Qualifier("onlineQuiz")
@Component
public class OnlineQuizSystemImpl implements QuizSystem {

	private final QuizRepository quizRepository;
	private final GameRoomRepository gameRoomRepository;
	private final ObjectMapper objectMapper; 
	private static final Logger logger = LoggerFactory.getLogger(OnlineQuizSystemImpl.class);
	private final HistoryRepository historyRepository;
	private final OnlineGameTimerScheduler onlineGameTimerScheduler;
	private final FindQuizSystem findQuizSystem;

	@Autowired
	public OnlineQuizSystemImpl(QuizRepository quizRepository, GameRoomRepository gameRoomRepository,
			ObjectMapper objectMapper, HistoryRepository historyRepository, OnlineGameTimerScheduler onlineGameTimerScheduler, FindQuizSystem findQuizSystem) {
		this.quizRepository = quizRepository;
		this.gameRoomRepository = gameRoomRepository;
		this.objectMapper = objectMapper;
		this.historyRepository = historyRepository;
		this.onlineGameTimerScheduler = onlineGameTimerScheduler;
		this.findQuizSystem = findQuizSystem;

	}

	@Override
	public String ConvertWordToBlank(String hint, HashMap<Integer, QuizVO> quiz, GameRoomVO gameRoomVo) {

		if (hint !=null) // db에서 단어인지 문장인지 확인
		{
			ConvertBlankSystem convertBlankSystem = findQuizSystem.findConvertBlankSystem("sentence");
			String result = convertBlankSystem.execute(hint, quiz, gameRoomVo);
			return result;
		} else {
			ConvertBlankSystem convertBlankSystem = findQuizSystem.findConvertBlankSystem("sentence");
			String result = convertBlankSystem.execute(hint, quiz, gameRoomVo);
			return result;
		}
	}

	@Override
	public int findNextQuestion(HashMap<Integer, QuizVO> quiz, GameRoomVO vo) {
		int currentIndex = vo.getQuizRound();
		vo.setQuizRound(currentIndex + 1);
		gameRoomRepository.updateQuizRound(vo);
		return vo.getQuizRound();
	}

	@Override
	public void setPrevQuestion(HashMap<Integer, QuizVO> quiz, GameRoomVO gameRoomVo) {
		int currentIndex = gameRoomVo.getQuizRound();
		gameRoomVo.setQuizRound(currentIndex - 1);
		gameRoomRepository.updateQuizRound(gameRoomVo);
	}

	@Override
	public List<String> findValByCatDB() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkAnswerAndUpdateScore(String userInput, HashMap<Integer, QuizVO> quiz, GameRoomVO vo) {
		String answer = quiz.get(vo.getQuizRound()).getQuestion();
		System.out.println("정답:" + userInput);
		System.out.println(answer);
		if (userInput.equals(answer)) {
			vo.setScore(vo.getScore() + 1);

			return true;
		}

		return false;
	}

	@Override
	public String[] showStepOfHint(String hint, HashMap<Integer, QuizVO> quiz, GameRoomVO gameRoomVo) {
		QuizVO vo = quiz.get(gameRoomVo.getQuizRound());
		if (hint.equals("hint1")) {
			String[] hints = { vo.getHint1(), ConvertWordToBlank("hint1", quiz, gameRoomVo) };
			return hints;
		} else if (hint.equals("hint2")) {
			String[] hints = { vo.getHint2(), ConvertWordToBlank("hint2", quiz, gameRoomVo) };
			return hints;
		} else if (hint.equals("hint3")) {
			String[] hints = { vo.getHint3(), ConvertWordToBlank("hint3", quiz, gameRoomVo) };
			return hints;
		} else if (hint.equals("allBlank")) {
			String[] hints = { "", ConvertWordToBlank("allBlank", quiz, gameRoomVo) };
			return hints;
		}
		return null;
	}

	@Override
	public HashMap<Integer, QuizVO> loadQuestion(GameRoomVO vo, String categoryNum, String table, String userId)
			throws JsonMappingException, JsonProcessingException {

		logger.info("{}, {} {} ", categoryNum, table, userId);
		
		if(vo.getQuiz() !=null)
		{
			JsonNode jsonNode = objectMapper.readTree(vo.getQuiz());
			HashMap<Integer, QuizVO> result = objectMapper.convertValue(jsonNode, new TypeReference<HashMap<Integer, QuizVO>>() {});
			logger.info("result : {}", result);
			return result;
		}
		else //quiz가 다 비었을 경우
		{
			HashMap<Integer, QuizVO> quiz = (HashMap<Integer, QuizVO>) quizRepository.findByCatNum(categoryNum, table);
			HashMap<Integer, QuizVO> newQuiz = new HashMap<>();
			int newKey = 0;
			for (Entry<Integer, QuizVO> entry : quiz.entrySet()) {
				System.out.println(newKey + ", " + entry.getValue());
				newQuiz.put(newKey, entry.getValue());

				newKey++;
			}
			gameRoomRepository.updateQuiz(vo, newQuiz); // 방장에게 퀴즈를 부여
			return newQuiz;
		} 

	}

	@Override
	public GameRoomVO loadGameRoom(String categoryNum, String userId, String roomId)
			throws JsonMappingException, JsonProcessingException {

		GameRoomVO vo = gameRoomRepository.findGameInform(roomId, "online");
		return vo;

	}

	@Override
	public void deleteGameRoom(GameRoomVO vo) {
		gameRoomRepository.deleteGameRoom(vo);

	}

	@Override
	public void insertHistory(GameRoomVO vo) {
		historyRepository.insertHistory(vo);

	}

	// 방장 권한 부여
	public void generalHost(GameRoomVO vo) {
		gameRoomRepository.updateUserType(vo);
	}

}
