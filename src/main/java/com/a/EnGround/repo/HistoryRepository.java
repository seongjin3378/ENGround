package com.a.EnGround.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.HistoryVO;

@Repository 
public class HistoryRepository {
	
	@Autowired
    JdbcTemplate template; 
	
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
    private final String NAME_SPACE = "HistoryMapper";
    
    public Page<HistoryVO> getAllData(Pageable pageable) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offset", pageable.getOffset());
        map.put("limit", pageable.getPageSize());
        
        int total = count();
        List<HistoryVO> histories = sqlSessionTemplate.selectList(NAME_SPACE + ".getAll", map);
        return new PageImpl<>(histories, pageable, total);
    }
    
    public int count() {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".count");
    }
    
    public HistoryVO selectOne(int scoreNum) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".findById", scoreNum);
    }
    
    public List<HistoryVO> findByUserId(String userId) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findByUserId", userId);
        
    }
    
    public int insertHistory(GameRoomVO vo)
    {
    	Map<String, Object> params = new HashMap<>();
		params.put("userId", vo.getUserId());
		params.put("score", vo.getScore());
    	return sqlSessionTemplate.insert(NAME_SPACE + ".insertHistory", params);
    }
    
}
