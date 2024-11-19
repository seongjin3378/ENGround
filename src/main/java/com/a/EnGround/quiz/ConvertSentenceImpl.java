package com.a.EnGround.quiz;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.a.EnGround.controller.OnlineGameRoomController;
import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.InGameQuizVO;
import com.a.EnGround.vo.QuizVO;

@Component
@Qualifier("sentence")
public class ConvertSentenceImpl implements ConvertBlankSystem{
	private static final Logger logger = LoggerFactory.getLogger(ConvertSentenceImpl.class);
	@Override
	public String execute(String hint, List<InGameQuizVO> quiz, GameRoomVO gameRoomVo) {
		QuizVO vo = quiz.get(gameRoomVo.getQuizRound()).getQuiz();
		String blankIndex = vo.getBlankIndex();
		int length = blankIndex.length();
		logger.info("blankIndex {} " , blankIndex);
		String[] wordArray = vo.getQuestion().split(" ");
		
		for(char c : blankIndex.toCharArray())
		{

			wordArray[Character.getNumericValue(c)] = wordArray[Character.getNumericValue(c)].replaceAll("\\w", "_");
		}
		

		if(length < 3 || hint.equals("hint1"))
		{
			String word = String.join(" ", wordArray);
			System.out.println("word 값:"+ word);
			return word;
		}
		
		int blankCount = 0;
		
		if(hint.equals("hint2"))
		{
			blankCount = (int)( (int) Arrays.stream(wordArray)
                    .filter(word -> word.contains("_"))
                    .count() * 0.5);
		}else if(hint.equals("hint3"))
		{
			blankCount =(int)( (int) Arrays.stream(wordArray)
                    .filter(word -> word.contains("_"))
                    .count() * 0.85);
		}
		
		String[] question_part = vo.getQuestion().split(" ");
		for(int i = 0; i<question_part.length; i++) 
		{
			if(wordArray[i].contains("_") && blankCount > 0)
			{
			wordArray[i] = question_part[i];
			blankCount--;
			}
			
			if(blankCount == 0)
			{
				break;
			}
		}
		String word = String.join(" ", wordArray);
		return word;
}
}

