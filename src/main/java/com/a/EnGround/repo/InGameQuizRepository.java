package com.a.EnGround.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.a.EnGround.quiz.SentenceQuizSystemImpl;
import com.a.EnGround.vo.InGameQuizVO;
import com.a.EnGround.vo.QuizVO;
import com.fasterxml.jackson.core.JsonProcessingException;

@Repository
public class InGameQuizRepository {
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
    private final String NAME_SPACE = "com.a.EnGround.repo.InGameQuizRepository";
    private static final Logger logger = LoggerFactory.getLogger(InGameQuizRepository.class);
	public void insertInGameQuiz(String roomId, String gameMode, int wordNo) throws JsonProcessingException {
		 Map<String, Object> params = new HashMap<>();
		 params.put("roomId", roomId);
		 params.put("gameMode", gameMode);
		 params.put("wordNo", wordNo);
		 
		 logger.info("{}, {}, {}", roomId, gameMode, wordNo);
		 sqlSessionTemplate.insert(NAME_SPACE + ".insertInGameQuiz", params);
	}
	public List<InGameQuizVO> findInGameQuizByRoomIdAndGameMode(InGameQuizVO vo)
	{
		return sqlSessionTemplate.selectList(NAME_SPACE + ".findInGameQuizByRoomIdAndGameMode", vo);
	}
	
}
