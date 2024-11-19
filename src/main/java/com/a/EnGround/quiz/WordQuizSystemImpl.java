package com.a.EnGround.quiz;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.a.EnGround.repo.GameRecordsRepository;
import com.a.EnGround.repo.GameRoomRepository;
import com.a.EnGround.repo.GameSessionRepository;
import com.a.EnGround.repo.HistoryRepository;
import com.a.EnGround.repo.InGameQuizRepository;
import com.a.EnGround.repo.QuizRepository;
import com.a.EnGround.vo.GameRecordsVO;
import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.HistoryVO;
import com.a.EnGround.vo.InGameQuizVO;
import com.a.EnGround.vo.QuizVO;
import com.a.EnGround.vo.SessionVO;
import com.a.EnGround.vo.UserVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


/*장성진*/
@Component
@Qualifier("wordQuiz")
public class WordQuizSystemImpl implements QuizSystem{
	private final QuizRepository quizRepository;
	private final GameRoomRepository gameRoomRepository;
	private final ObjectMapper objectMapper; 
	private final GameSessionRepository gameSessionRepository;
	private final GameRecordsRepository gameRecordsRepository;
	private final InGameQuizRepository inGameQuizRepository;
	@Autowired
	private HistoryRepository historyRepository;
	private static final Logger logger = LoggerFactory.getLogger(WordQuizSystemImpl.class);
	@Autowired
	FindQuizSystem findQuizSystem;

	@Autowired
	public WordQuizSystemImpl(QuizRepository quizRepository, GameRoomRepository gameRoomRepository, ObjectMapper objectMapper, GameSessionRepository gameSessionRepository, GameRecordsRepository gameRecordsRepository, InGameQuizRepository inGameQuizRepository) {
		this.quizRepository = quizRepository;
		this.gameRoomRepository = gameRoomRepository;
		this.objectMapper = objectMapper;
		this.gameSessionRepository = gameSessionRepository;
		this.gameRecordsRepository = gameRecordsRepository;
		this.inGameQuizRepository = inGameQuizRepository;
	}


	@Override
	public String ConvertWordToBlank(String hint, List<InGameQuizVO> quiz, GameRoomVO gameRoomVo) {
		ConvertBlankSystem convertBlankSystem = findQuizSystem.findConvertBlankSystem("word");
		String result = convertBlankSystem.execute(hint, quiz, gameRoomVo);
		return result;
	
	}
	

	@Override
	public int findNextQuestion(List<InGameQuizVO> quiz, GameRoomVO vo) {
		int currentIndex = vo.getQuizRound();
		vo.setQuizRound(currentIndex+1);
		gameRoomRepository.updateQuizRound(vo);
		return vo.getQuizRound();
	}

	@Override
	public void setPrevQuestion(List<InGameQuizVO> quiz, GameRoomVO gameRoomVo) {
		int currentIndex = gameRoomVo.getQuizRound();
		gameRoomVo.setQuizRound(currentIndex-1);
		gameRoomRepository.updateQuizRound(gameRoomVo);
	}
	@Override
	public List<String> findValByCatDB() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean checkAnswerAndUpdateScore(String userInput, List<InGameQuizVO> quiz, GameRoomVO vo) {
		String answer = quiz.get(vo.getQuizRound()).getQuiz().getQuestion();
		System.out.println("정답:"+userInput);
		System.out.println(answer);
		int answerTime = vo.getAnswerTime().getSecond();
		if(userInput.equals(answer))
		{
			int score = setScore(answerTime);
			logger.info("{}, {}, {}", score, vo.getRoomId(), vo.getUserId());
			GameRecordsVO gameRecordsVO= setGameRecordsAndReturn(score, vo.getRoomId(), vo.getUserId());
			gameRecordsRepository.insertGameRecords(gameRecordsVO);
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
	}
	
	public GameRecordsVO setGameRecordsAndReturn(int score, String roomId, String userId) {
	    // 점수 계산
	    
	    // HistoryVO 객체 생성 및 점수 설정
	    HistoryVO historyVO = new HistoryVO();
	    historyVO.setScore(score);
	    historyVO.setUserId(userId);
	    historyVO.setGameMode("word");
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
	
	int setScore(int answerTime)
	{
		logger.info("{}", answerTime);
        if (answerTime > 50) {
            return 10;
        } else if (answerTime > 40) {
        	return 9;
        } else if (answerTime > 30) {
        	return 8;
        } else if (answerTime > 20) {
        	return 7;
        } else if (answerTime > 10) {
        	return 6;
        } else {
        	return 5;
        }
	}


	@Override
	public String[] showStepOfHint(String hint, List<InGameQuizVO> quiz, GameRoomVO gameRoomVo) {
		QuizVO vo = quiz.get(gameRoomVo.getQuizRound()).getQuiz();
		if(hint.equals("hint1"))
		{
			String[] hints = {vo.getHint1(), ConvertWordToBlank("hint1", quiz, gameRoomVo)};
			return hints;
		}
		else if(hint.equals("hint2"))
		{
			String[] hints = {vo.getHint2(), ConvertWordToBlank("hint2", quiz, gameRoomVo)};
			return hints;
		}
		else if(hint.equals("hint3"))
		{
			String[] hints = {vo.getHint3(), ConvertWordToBlank("hint3", quiz, gameRoomVo)};
			return hints;
		}
		else if(hint.equals("allBlank"))
		{
			String[] hints  = {"", ConvertWordToBlank("allBlank", quiz, gameRoomVo)};
			return hints;
		}
		return null;
	}

	
	@Override
	public List<InGameQuizVO>loadQuestion(GameRoomVO vo, String categoryNum, String table, String userId) throws JsonMappingException, JsonProcessingException {
		logger.info("{} {} {}", categoryNum, table, userId);
		if(vo != null)
		{
			InGameQuizVO inGameQuizVO = new InGameQuizVO();
			inGameQuizVO.setGameMode("word");
			inGameQuizVO.setRoomId(userId+"_word");
			inGameQuizVO.setTable(table);
	        List<InGameQuizVO> quiz = inGameQuizRepository.findInGameQuizByRoomIdAndGameMode(inGameQuizVO);
			
			return quiz;
		}
		else 
		{
		gameRoomRepository.insertGameInform(userId+"_word", "word", "word");
		List<QuizVO> quizNoList =  quizRepository.findWordNoByCatName(categoryNum, table);
		Iterable<QuizVO> iterableQuizNo = quizNoList;
		for(QuizVO item: iterableQuizNo)
		{
			inGameQuizRepository.insertInGameQuiz(userId+"_word", "word", item.getQuizNo());
		}
        
		InGameQuizVO inGameQuizVO = new InGameQuizVO();
		inGameQuizVO.setGameMode("word");
		inGameQuizVO.setRoomId(userId+"_word");
		inGameQuizVO.setTable(table);
        List<InGameQuizVO> quiz = inGameQuizRepository.findInGameQuizByRoomIdAndGameMode(inGameQuizVO);
        return quiz;
		}
		
	}


	@Override
	public GameRoomVO loadGameRoom(String categoryNum, String userId, String roomId) throws JsonMappingException, JsonProcessingException {
		
		GameRoomVO vo = gameRoomRepository.findGameInform(userId+"_word", "word");
		return vo;

	
}

}
