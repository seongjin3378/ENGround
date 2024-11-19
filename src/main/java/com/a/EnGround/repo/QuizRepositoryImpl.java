package com.a.EnGround.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.a.EnGround.vo.CategoryVO;
import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.QuizVO;

@Repository
public class QuizRepositoryImpl implements QuizRepository{
	 private static final String NAME_SPACE = "com.a.EnGround.repo.QuizRepository";
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	private static final Logger logger = LoggerFactory.getLogger(QuizRepositoryImpl.class);
	@Override
	public List<QuizVO> findWordNoByCatName(String categoryName, String table) {
		  Map<String, Object> params = new HashMap<>();
		    params.put("categoryName", categoryName);
		    params.put("table", table);
		    logger.info("findWordNoBycatName: {} {}", categoryName, table);
			return sqlSessionTemplate.selectList(NAME_SPACE + ".findWordNoByCatName", params);
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
	@Override
	public String findCatNoByCatName(GameRoomVO vo) {
		
		return sqlSessionTemplate.selectOne(NAME_SPACE + ".findCatNoByCatName", vo);
	}


	

}
