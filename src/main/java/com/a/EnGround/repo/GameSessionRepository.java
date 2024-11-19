package com.a.EnGround.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.a.EnGround.controller.OnlineGameRoomController;
import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.GameSessionVO;
import com.a.EnGround.vo.SessionVO;

@Repository
public class GameSessionRepository {
	private static final String NAME_SPACE = "com.a.EnGround.repo.GameSessionRepository";
	private static final Logger logger = LoggerFactory.getLogger(GameSessionRepository.class);
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	
	public void insertGameSession(GameSessionVO gameSessionVO)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("userId", gameSessionVO.getUserId());
		params.put("roomId", gameSessionVO.getRoomId());
		params.put("isHost", gameSessionVO.isHost());

		
		sqlSessionTemplate.insert(NAME_SPACE+".insertGameSession", params);
	}
	
	public void deleteGameSession(SessionVO vo)
	{
		sqlSessionTemplate.delete(NAME_SPACE+".deleteGameSession", vo.getUserId());
	}
	
	public List<GameRoomVO> findUserInform(String roomId) { // 온라인 전용
		  Map<String, Object> params = new HashMap<>();
	    	
		    params.put("roomId", roomId);
			return sqlSessionTemplate.selectList(NAME_SPACE + ".findUserInform", params);
	}
	public void delegationHost(GameSessionVO vo, String userId, boolean isHost)
	{
		vo.setUserId(userId);
		vo.setHost(isHost);
		sqlSessionTemplate.update(NAME_SPACE + ".updateHost", vo);
	}
	public String updateHostAndReturnId(String columnType, String ownId) // 온라인전용
	{
		GameSessionVO vo = new GameSessionVO();
		if(columnType.length() > 30) // columnType이 roomId일 경우(방장이 나갈 때 방장위임)
		{
		vo.setRoomId(columnType);
		sqlSessionTemplate.update(NAME_SPACE + ".randomHostUpdate", vo);
		return sqlSessionTemplate.selectOne(NAME_SPACE + ".findHostByRoomId", columnType);
		}else { // columnType이 user일 경우(방장 위임을 했을 경우)
			delegationHost(vo, columnType, true); //선택한 방장 위임
			delegationHost(vo, ownId, false); // 현재 내 방장 권한 삭제
			return sqlSessionTemplate.selectOne(NAME_SPACE + ".findHostByUserId", columnType);
		}
		
		
	}
	



}
