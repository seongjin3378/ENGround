package com.a.EnGround.repo;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.a.EnGround.vo.ChatHistoryVO;

@Repository
public class ChatHistoryRepository {
	private static final String NAME_SPACE = "com.a.EnGround.repo.ChatHistoryRepository";
	private static final Logger logger = LoggerFactory.getLogger(ChatHistoryRepository.class);
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	public int insertChatHistory(ChatHistoryVO vo)
	{
		return sqlSessionTemplate.insert(NAME_SPACE + ".insertChatHistory", vo);
	}
	public List<ChatHistoryVO> selectChatHistory(ChatHistoryVO vo)
	{
		return sqlSessionTemplate.selectList(NAME_SPACE + ".selectChatHistory", vo);
	}
}
