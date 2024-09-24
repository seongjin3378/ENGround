package com.a.EnGround.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.a.EnGround.vo.CategoryVO;
import com.a.EnGround.vo.QuizVO;
//
@Mapper
public interface QuizRepository {
	
	Map<Integer, QuizVO> findByCatNum(@Param("categoryNum")String categoryNum, @Param("table")String table);
	List<CategoryVO> findAllCatName();
	
	List<QuizVO> findAllQuiz(String table);

	List<QuizVO> findByQuizId(int quizId, String table);
	

	int insertQuiz(QuizVO quiz, String table);
}