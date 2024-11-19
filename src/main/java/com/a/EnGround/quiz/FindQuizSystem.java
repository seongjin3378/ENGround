package com.a.EnGround.quiz;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
/*장성진*/
@Component
public class FindQuizSystem {
	@Autowired
	@Qualifier("wordQuiz")
	private QuizSystem quizSystem;
	
	@Autowired
	@Qualifier("sentenceQuiz")
	private QuizSystem quizSystem2;
	
	@Autowired
	@Qualifier("onlineQuiz")
	private QuizSystem quizSystem3;
	
	@Autowired
	@Qualifier("sentence")
	ConvertBlankSystem convertBlankSystem;
	
	@Autowired
	@Qualifier("word")
	ConvertBlankSystem convertBlankSystem2;
	
	
	public QuizSystem findQuizService(HttpSession session)
	{
		String gameMode = (String) session.getAttribute("gameMode");
		System.out.println(gameMode);
		if(gameMode.equals("word"))
		{
		return quizSystem;
		}
		else if(gameMode.equals("sentence"))
		{
		return quizSystem2;
		}
		else if(gameMode.equals("online"))
		{
		return quizSystem3;
		}
		return null;
		
	}
	public String getQuizTable(HttpSession session)
	{
		String gameMode = (String) session.getAttribute("gameMode");
		System.out.println(gameMode);
		if(gameMode.equals("word"))
		{
		return "select_word";
		}
		else if(gameMode.equals("sentence"))
		{
		return "select_sentence";
		}
		else if(gameMode.equals("online"))
		{
		return "select_online";
		}
		return null;
	}
	public ConvertBlankSystem findConvertBlankSystem(String val)
	{
		if(val.equals("sentence"))
		{
			return convertBlankSystem;
		}else {
			return convertBlankSystem2;
		}
	}
}
