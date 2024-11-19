package com.a.EnGround.repo;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.a.EnGround.vo.GameRecordsVO;
import com.a.EnGround.vo.HistoryVO;
@Repository
public class GameRecordsRepository {
	private static final String NAME_SPACE = "com.a.EnGround.repo.GameRecordsRepository";
	private static final Logger logger = LoggerFactory.getLogger(GameRecordsRepository.class);
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	
	public void insertGameRecords(GameRecordsVO vo)
	{
		sqlSessionTemplate.insert(NAME_SPACE + ".insertGameRecords", vo);
	}
	public int findCorrectCounts(HistoryVO vo)
	{
		return sqlSessionTemplate.selectOne(NAME_SPACE + ".findCorrectCounts", vo);
	}
}
