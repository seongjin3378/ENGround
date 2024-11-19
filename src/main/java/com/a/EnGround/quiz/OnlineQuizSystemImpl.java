package com.a.EnGround.quiz;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.a.EnGround.repo.GameRecordsRepository;
import com.a.EnGround.repo.GameRoomRepository;
import com.a.EnGround.repo.GameSessionRepository;
import com.a.EnGround.repo.HistoryRepository;
import com.a.EnGround.repo.InGameQuizRepository;
import com.a.EnGround.repo.QuizRepository;
import com.a.EnGround.scheduler.OnlineGameTimerScheduler;
import com.a.EnGround.vo.AnswerVO;
import com.a.EnGround.vo.GameRecordsVO;
import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.HistoryVO;
import com.a.EnGround.vo.InGameQuizVO;
import com.a.EnGround.vo.QuizVO;
import com.a.EnGround.vo.SessionVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
/*장성진*/
@Qualifier("onlineQuiz")
@Component
public class OnlineQuizSystemImpl implements QuizSystem {

	private final QuizRepository quizRepository;
	private final GameRoomRepository gameRoomRepository;
	private final GameSessionRepository gameSessionRepository;
	private final ObjectMapper objectMapper;
	private static final Logger logger = LoggerFactory.getLogger(OnlineQuizSystemImpl.class);
	private final HistoryRepository historyRepository;
	private final FindQuizSystem findQuizSystem;
	private final GameRecordsRepository gameRecordsRepository;
	private final InGameQuizRepository inGameQuizRepository;

	private ConcurrentHashMap<String, AnswerVO> answerUserTimeLine = new ConcurrentHashMap<>();

	@Autowired
	public OnlineQuizSystemImpl(QuizRepository quizRepository, GameRoomRepository gameRoomRepository,
			ObjectMapper objectMapper, HistoryRepository historyRepository, GameSessionRepository gameSessionRepository,
			OnlineGameTimerScheduler onlineGameTimerScheduler, FindQuizSystem findQuizSystem, GameRecordsRepository gameRecordsRepository, InGameQuizRepository inGameQuizRepository) {
		this.quizRepository = quizRepository;
		this.gameRoomRepository = gameRoomRepository;
		this.objectMapper = objectMapper;
		this.historyRepository = historyRepository;
		this.findQuizSystem = findQuizSystem;
		this.gameSessionRepository = gameSessionRepository;
		this.gameRecordsRepository = gameRecordsRepository;
		this.inGameQuizRepository = inGameQuizRepository;

	}

	@Override
	public String ConvertWordToBlank(String hint, List<InGameQuizVO> quiz, GameRoomVO gameRoomVo) {
		String hintType = quizRepository.findCatNoByCatName(gameRoomVo);
		logger.info("hintType {}", hintType);
		int lastIndex = hintType.lastIndexOf('-');
		hintType = hintType.substring(lastIndex+1);
		if (hintType.equals("SENTENCE")) // db에서 단어인지 문장인지 확인
		{
			ConvertBlankSystem convertBlankSystem = findQuizSystem.findConvertBlankSystem("sentence");
			String result = convertBlankSystem.execute(hint, quiz, gameRoomVo);
			return result;
		} else {
			ConvertBlankSystem convertBlankSystem = findQuizSystem.findConvertBlankSystem("word");
			String result = convertBlankSystem.execute(hint, quiz, gameRoomVo);
			return result;
		}
	}

	@Override
	public int findNextQuestion(List<InGameQuizVO> quiz, GameRoomVO vo) {
		int currentIndex = vo.getQuizRound();
		vo.setQuizRound(currentIndex + 1);
		gameRoomRepository.updateQuizRound(vo);
		answerUserTimeLine.put(vo.getRoomId(), new AnswerVO()); // 다음라운드가 되면 타임라인 map을 초기화
		return vo.getQuizRound();
	}

	@Override
	public void setPrevQuestion(List<InGameQuizVO> quiz, GameRoomVO gameRoomVo) {
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
	public Boolean checkAnswerAndUpdateScore(String userInput, List<InGameQuizVO> quiz, GameRoomVO vo) {
		try {
			String answer = quiz.get(vo.getQuizRound()).getQuiz().getQuestion();
			logger.info("정답 : {}", userInput);
			logger.info(answer);
			logger.info("isFirstInsertAnswerTemp {} ");
			boolean isFirstInsertAnswerTemp = answerUserTimeLine.get(vo.getRoomId()).getUserTimeMap().isEmpty();
			logger.info("firstAnswerUser: {}", isFirstInsertAnswerTemp && userInput.equals(answer));
			if (userInput.equals(answer) && vo.isFirstReqUser() == true) {
				addUserTime(vo.getRoomId(), vo.getUserId(), vo.getAnswerTime());
				logger.info("처음 요청한사람");
				return Boolean.TRUE;
			} else if (userInput.equals(answer)) {
				logger.info("정답처리 프로세스");
				logger.info("룸아이디: {}, 유저아이디: {} ", vo.getRoomId(), vo.getUserId());
				GameRecordsVO gameRecordsVO = setGameRecordsAndReturn(vo.getRoomId(), vo.getUserId());
				gameRecordsRepository.insertGameRecords(gameRecordsVO);
				return Boolean.FALSE;
			}
			return null;/* 정답이 안맞으면 null 반환 */
		} catch (NullPointerException e) {
			return null;
		} catch(ArrayIndexOutOfBoundsException e)
		{
			return null;
		}
	}
	public GameRecordsVO setGameRecordsAndReturn(String roomId, String userId) {
	    // 점수 계산
	    int score = checkUserTime(roomId, userId);
	    
	    // HistoryVO 객체 생성 및 점수 설정
	    HistoryVO historyVO = new HistoryVO();
	    historyVO.setScore(score);
	    historyVO.setUserId(userId);
	    historyVO.setGameMode("online");
	    // 점수 업데이트
	    historyRepository.updateScore(historyVO);
	    
	    // historyNo 찾기
	    int historyNo = historyRepository.findHistoryNoByUserIdAndIsFinished(historyVO);
	    
	    // GameRecordsVO 객체 생성 및 값 설정
	    GameRecordsVO gameRecordsVO = new GameRecordsVO();
	    gameRecordsVO.setAnswerCount(1);
	    gameRecordsVO.setHistoryNo(historyNo);
		return gameRecordsVO;
	    
	    // 추가적인 로직이 필요하다면 여기에 작성
	    // 예: gameRecordsRepository.save(gameRecordsVO);
	}
	/* checkAnswerAndUpdateScore */
	public void addUserTime(String roomId, String userId, LocalDateTime answerTime) {
		AnswerVO answerVO = answerUserTimeLine.getOrDefault(roomId, new AnswerVO());
		TreeMap<LocalDateTime, String> userTimeMap = answerVO.getUserTimeMap();
		userTimeMap.put(answerTime, userId);
		answerVO.setUserTimeMap(userTimeMap);
		answerUserTimeLine.put(roomId, answerVO);
		logger.info("addUserTime");
	}

	/* checkAnswerAndUpdateScore */
	public int checkUserTime(String roomId, String userId) {
		AnswerVO timeLinesOfOneRoom = answerUserTimeLine.get(roomId);
		logger.info("answerVO {}", timeLinesOfOneRoom);
		TreeMap<LocalDateTime, String> timeLines = timeLinesOfOneRoom.getUserTimeMap();
		int idx = 0;
		logger.info("timeLines {}", timeLines);
		LocalDateTime myTimeLine = getKeyByValueToTimeLines(timeLines, userId);
		for (Entry<LocalDateTime, String> entry : timeLines.entrySet()) {
			LocalDateTime currentKey = entry.getKey();
			String currentUserId = entry.getValue();
			logger.info("currentKey테스트: {}", currentKey);
			logger.info("currentvalue테스트: {}", currentUserId);

		}
		LocalDateTime prevTimeLineKey = null;
		for (Entry<LocalDateTime, String> entry : timeLines.entrySet()) {
			LocalDateTime currentKey = entry.getKey();
			String currentUserId = entry.getValue();
			logger.info("currentKey: {}", currentKey);
			boolean isEqualBothId = currentUserId.equals(userId);
			if (isEqualBothId) {
				return 10 - idx;
			}
			boolean isKeyDuplicateForEach = myTimeLine.isEqual(currentKey);
			if (!isKeyDuplicateForEach) {
				idx += 1;
			} else {
				prevTimeLineKey = currentKey;
			}
		}
		//
		//
		//
		return 10; /* timeLine이 한명밖에 없을 경우 10반환 */
	}

	/* checkAnswerAndUpdateScore */
	private static LocalDateTime getKeyByValueToTimeLines(TreeMap<LocalDateTime, String> map, String value) {
		for (Map.Entry<LocalDateTime, String> entry : map.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey(); // 해당 값에 대한 키를 반환
			}
		}
		return null; // 값이 없을 경우
	}

	@Override
	public String[] showStepOfHint(String hint, List<InGameQuizVO>quiz,  GameRoomVO gameRoomVo) {
		QuizVO vo = quiz.get(gameRoomVo.getQuizRound()).getQuiz();
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
	public List<InGameQuizVO> loadQuestion(GameRoomVO vo, String categoryNum, String table, String userId)
			throws JsonMappingException, JsonProcessingException {

		logger.info("{}, {} {} ", categoryNum, table, userId);
		if(vo.isOnlineGameStart() == true)
		{
			List<QuizVO> quizNoList =  quizRepository.findWordNoByCatName(categoryNum, table);
			Iterable<QuizVO> iterableQuizNo = quizNoList;
			for(QuizVO item: iterableQuizNo)
			{
				inGameQuizRepository.insertInGameQuiz(vo.getRoomId(), vo.getGameMode(), item.getQuizNo());
			}
	        
			InGameQuizVO inGameQuizVO = new InGameQuizVO();
			inGameQuizVO.setGameMode(vo.getGameMode());
			inGameQuizVO.setRoomId(vo.getRoomId());
			inGameQuizVO.setTable(table);
	        List<InGameQuizVO> quiz = inGameQuizRepository.findInGameQuizByRoomIdAndGameMode(inGameQuizVO);
	        return quiz;
		}
		else 
		{
			InGameQuizVO inGameQuizVO = new InGameQuizVO();
			inGameQuizVO.setGameMode(vo.getGameMode());
			inGameQuizVO.setRoomId(vo.getRoomId());
			inGameQuizVO.setTable(table);
	        List<InGameQuizVO> quiz = inGameQuizRepository.findInGameQuizByRoomIdAndGameMode(inGameQuizVO);
			
			return quiz;
		}
	}


	@Override
	public GameRoomVO loadGameRoom(String categoryNum, String userId, String roomId)
			throws JsonMappingException, JsonProcessingException {

		GameRoomVO vo = gameRoomRepository.findGameInform(roomId, "online");
		return vo;

	}


	// 방장 권한 부여
	public void generalHost(GameRoomVO vo) {
		gameRoomRepository.updateUserType(vo);
	}


}
