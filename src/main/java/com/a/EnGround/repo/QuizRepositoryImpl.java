package com.a.EnGround.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.a.EnGround.vo.CategoryVO;
import com.a.EnGround.vo.QuizVO;

@Repository
public class QuizRepositoryImpl implements QuizRepository{
	 private static final String NAME_SPACE = "com.a.EnGround.repo.QuizRepository";
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	@Override
	public Map<Integer, QuizVO> findByCatNum(String categoryNum, String table) {
		System.out.println(categoryNum);
	    Map<String, Object> params = new HashMap<>();
	    params.put("categoryNum", categoryNum);
	    params.put("table", table);
		return sqlSessionTemplate.selectMap(NAME_SPACE + ".findByCatNum", params, "quizNo");
	}
	@Override
	public List<CategoryVO> findAllCatName() {
		
		return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllCatName");
	}
	
	public List<QuizVO> findAllQuiz(String table){
		System.out.println(table);
		return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllQuiz", table);
	}
    

	
	@Override
	public List<QuizVO> findByQuizId(int quizId, String table) {
        Map<String, Object> params = new HashMap<>();
        params.put("quizId", quizId);
        params.put("table", table);
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findByQuizId", params);
    }
	@Override
	public int insertQuiz(QuizVO quiz, String table) {
		Map<String, Object> params = new HashMap<>();
	    params.put("quizNo", quiz.getQuizNo());
	    params.put("question", quiz.getQuestion());
	    params.put("timer", quiz.getTimer());
	    params.put("hint1", quiz.getHint1());
	    params.put("hint2", quiz.getHint2());
	    params.put("hint3", quiz.getHint3());
	    params.put("answer", quiz.getAnswer());
	    params.put("categoryNo", quiz.getCategoryNo());
	    params.put("table", table); // table 값을 따로 전달

	    return sqlSessionTemplate.insert(NAME_SPACE + ".insertQuiz", params);
	}

	

}
