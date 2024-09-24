package com.a.EnGround.quiz;

import java.util.List;
import java.util.Map;

import com.a.EnGround.vo.QuizVO;
//
public interface QuizService {
	
	public String ConvertWordToBlank(String hint); // ConvertBlnakToChar 안에 결합

	public QuizVO findNextQuestion();
	
	public List<String> findValByCatDB();
	
	public String[] showStepOfHint(String hint);
	
	public boolean checkAnswer(String userInput);
	
	public int insertRecord();
	
	public Map<Integer, QuizVO> loadQuestionAndReIndex(String categoryNum, String table);
	
	public void setParam(String categoryNum); //quiz 5개를 불러와 quizService 전역 변수에 저장
}
