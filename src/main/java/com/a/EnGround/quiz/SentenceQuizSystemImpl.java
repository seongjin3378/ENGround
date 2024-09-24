package com.a.EnGround.quiz;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.a.EnGround.repo.GameRoomRepository;
import com.a.EnGround.repo.HistoryRepository;
import com.a.EnGround.repo.QuizRepository;
import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.QuizVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Qualifier("sentenceQuiz")
public class SentenceQuizSystemImpl implements QuizSystem{
	private final QuizRepository quizRepository;
	private final GameRoomRepository gameRoomRepository;
	private final ObjectMapper objectMapper; 

	@Autowired
	private HistoryRepository historyRepository;
	
	@Autowired
	FindQuizSystem findQuizSystem;

	@Autowired
	public SentenceQuizSystemImpl(QuizRepository quizRepository, GameRoomRepository gameRoomRepository, ObjectMapper objectMapper) {
		this.quizRepository = quizRepository;
		this.gameRoomRepository = gameRoomRepository;
		this.objectMapper = objectMapper;
	}


	@Override
	public String ConvertWordToBlank(String hint, HashMap<Integer, QuizVO> quiz, GameRoomVO gameRoomVo) {

		ConvertBlankSystem convertBlankSystem = findQuizSystem.findConvertBlankSystem("sentence");
		String result = convertBlankSystem.execute(hint, quiz, gameRoomVo);
		return result;
	
	}
	

	@Override
	public int findNextQuestion(HashMap<Integer, QuizVO> quiz, GameRoomVO vo) {
		int currentIndex = vo.getQuizRound();
		vo.setQuizRound(currentIndex+1);
		gameRoomRepository.updateQuizRound(vo);
		return vo.getQuizRound();
	}

	@Override
	public void setPrevQuestion(HashMap<Integer, QuizVO> quiz, GameRoomVO gameRoomVo) {
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
	public boolean checkAnswerAndUpdateScore(String userInput, HashMap<Integer, QuizVO> quiz, GameRoomVO vo) {
		String answer = quiz.get(vo.getQuizRound()).getQuestion();
		System.out.println("정답:"+userInput);
		System.out.println(answer);
		if(userInput.equals(answer))
		{
			vo.setScore(vo.getScore()+1);
			return true;
		}
		
		return false;
	}


	@Override
	public String[] showStepOfHint(String hint, HashMap<Integer, QuizVO> quiz, GameRoomVO gameRoomVo) {
		QuizVO vo = quiz.get(gameRoomVo.getQuizRound());
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
	public HashMap<Integer, QuizVO>loadQuestion(GameRoomVO vo, String categoryNum, String table, String userId) throws JsonMappingException, JsonProcessingException {
		
		System.out.println("                 "+categoryNum+" " + table +" "+  userId + "                                 ");
		if(vo != null)
		{
			JsonNode jsonNode = objectMapper.readTree(vo.getQuiz());
			HashMap<Integer, QuizVO> result = objectMapper.convertValue(jsonNode, new TypeReference<HashMap<Integer, QuizVO>>() {});
			return result;
		}
		else 
		{
		HashMap<Integer, QuizVO> quiz = (HashMap<Integer, QuizVO>)quizRepository.findByCatNum(categoryNum, table);
		HashMap<Integer, QuizVO> newQuiz = new HashMap<>();
        int newKey = 0;
        for (Entry<Integer, QuizVO> entry : quiz.entrySet()) {
            System.out.println(newKey + ", " + entry.getValue());
        	newQuiz.put(newKey, entry.getValue());
            
            newKey++;
        }
        gameRoomRepository.insertGameInform(userId+"_sentence", "sentence", newQuiz);
        return newQuiz;
		}
		
	}


	@Override
	public GameRoomVO loadGameRoom(String categoryNum, String userId, String roomId) throws JsonMappingException, JsonProcessingException {
		
		GameRoomVO vo = gameRoomRepository.findGameInform(userId+"_sentence", "sentence");
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

}
