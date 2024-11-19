package com.a.EnGround.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.a.EnGround.vo.CategoryVO;
import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.QuizVO;


/*장성진*/
@Mapper
public interface QuizRepository {
	
	List<QuizVO>findWordNoByCatName(@Param("categoryName")String categoryName, @Param("table")String table);
	List<CategoryVO> findAllCatName();
	
	List<QuizVO> findAllQuiz(String table);

	List<QuizVO> findByQuizId(int quizId, String table);
	
	String findCatNoByCatName(GameRoomVO vo);
	int insertQuiz(QuizVO quiz, String table);
}