package com.a.EnGround.quiz;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.InGameQuizVO;
import com.a.EnGround.vo.QuizVO;

@Component
@Qualifier("word")
public class ConvertWordImpl implements ConvertBlankSystem{

	@Override
	public String execute(String hint, List<InGameQuizVO> quiz, GameRoomVO gameRoomVo) {
		System.out.println("rounds : "+gameRoomVo.getQuizRound());
		QuizVO vo = quiz.get(gameRoomVo.getQuizRound()).getQuiz();
		
		String blankIndex = vo.getBlankIndex();
		int length = blankIndex.length();
		StringBuilder wordStb = new StringBuilder(vo.getQuestion());
		if(hint.equals("allBlank"))
		{
			for(int i = 0; i<vo.getQuestion().length(); i++)
			{
				wordStb.setCharAt(i, '_');
			}
			String word = wordStb.toString();
			return word;
		}
		
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
		}
		return wordStb.toString();
	}

}
