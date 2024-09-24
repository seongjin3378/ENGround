package com.a.EnGround.quiz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.a.EnGround.repo.QuizRepository;
import com.a.EnGround.vo.QuizVO;

@Component
@Scope("prototype")
@Qualifier("wordQuiz")
public class WordQuizServiceImpl implements QuizService{
	private final QuizRepository quizRepository;
	private MultiValueMap<Integer, QuizVO> history = new LinkedMultiValueMap<>();
	HashMap<Integer, QuizVO> quiz;
	int num = 10;



	@Autowired
	public WordQuizServiceImpl(QuizRepository quizRepository) {
		this.quizRepository = quizRepository;
	}

	public String ConvertBlankToChar(int remainCount, StringBuilder wordStb, String word, int charCount, QuizVO vo)
	{
		int lastIndex = 0;
		if(remainCount == 0 )
		{
			lastIndex = 0;
		}else {
			lastIndex = -1;
		}
		int Idx = -1;
		for(int i = 0; i<charCount+lastIndex; i++)
		{
			Idx = word.indexOf('_', Idx+1);
			wordStb.setCharAt(Idx, vo.getQuestion().charAt(Idx));
		}
		return wordStb.toString();
	}
	@Override
	public String ConvertWordToBlank(String hint) {
		QuizVO vo = quiz.get(num);
		String blankIndex = vo.getBlankIndex();
		int length = blankIndex.length();
		StringBuilder wordStb = new StringBuilder(vo.getQuestion());
		
		for(char c : blankIndex.toCharArray())
		{
			wordStb.setCharAt(Character.getNumericValue(c), '_');
		}
		String word = wordStb.toString();
		
		if(length < 3 || hint.equals("hint1"))
		{
			System.out.print("뷁");
			return word;
		}
		
		int blankCount = 0;
		
		if(hint.equals("hint2"))
		{
			blankCount = (int)((int) word.codePoints().filter(c -> c == '_').count() *0.5);
		}else if(hint.equals("hint3"))
		{
			blankCount = (int) ((int) word.codePoints().filter(c -> c == '_').count() * 0.8);
		}
		
		int Idx = -1;
		System.out.print(blankCount);
		for(int i = 0; i<blankCount; i++)
		{
			Idx = word.indexOf('_', Idx+1);
			wordStb.setCharAt(Idx, vo.getQuestion().charAt(Idx));
			System.out.print("뷁2");
		}
		return wordStb.toString();
	
	}
	

	@Override
	public QuizVO findNextQuestion() {
		System.out.print(quiz.get(num));
		num++;
		return null;
	}

	@Override
	public List<String> findValByCatDB() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkAnswer(String userInput) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int insertRecord() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String[] showStepOfHint(String hint) {
		QuizVO vo = quiz.get(num);
		if(hint.equals("hint1"))
		{
			String[] hints = {vo.getHint1(), ConvertWordToBlank("hint1")};
			return hints;
		}
		else if(hint.equals("hint2"))
		{
			String[] hints = {vo.getHint2(), ConvertWordToBlank("hint2")};
			return hints;
		}
		else if(hint.equals("hint3"))
		{
			String[] hints = {vo.getHint3(), ConvertWordToBlank("hint3")};
			return hints;
		}
		return null;
	}


	@Override
	public HashMap<Integer, QuizVO> loadQuestionAndReIndex(String categoryNum, String table) {
		
		HashMap<Integer, QuizVO> quiz = (HashMap<Integer, QuizVO>)quizRepository.findByCatNum(categoryNum, table);
		HashMap<Integer, QuizVO> newQuiz = new HashMap<>();
        int newKey = 0;
        for (Entry<Integer, QuizVO> entry : quiz.entrySet()) {
            System.out.println(newKey + ", " + entry.getValue());
        	newQuiz.put(newKey, entry.getValue());
            
            newKey++;
        }
		return newQuiz;
	}


	@Override
	public void setParam(String categoryNum) {
		this.quiz = loadQuestionAndReIndex(categoryNum, "select_word");
	}

	
}
