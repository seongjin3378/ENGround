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


/*장성진*/
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
    
    public int insertHistory(HistoryVO vo)
    {

    	return sqlSessionTemplate.insert(NAME_SPACE + ".insertHistory", vo);
    }
    
	public int updateScore(HistoryVO vo) {
		 
		 return sqlSessionTemplate.update(NAME_SPACE + ".updateScore", vo);
	}
	
	public int findHistoryNoByUserIdAndIsFinished(HistoryVO vo)
	{
		return sqlSessionTemplate.selectOne(NAME_SPACE + ".findHistoryNoByUserIdAndIsFinished", vo);
	}
	
	public int findScore(HistoryVO vo) {
		 
		 return sqlSessionTemplate.selectOne(NAME_SPACE + ".findUserByScore", vo);
	}
	public void updateIsFinishedByUserId(HistoryVO vo)
	{
		sqlSessionTemplate.update(NAME_SPACE + ".updateIsFinishedByUserId", vo);
	}
	public void deleteHistory(HistoryVO vo)
	{
		sqlSessionTemplate.delete(NAME_SPACE + ".deleteHistory", vo);
	}
	
	public void updateIsFinishedByRoomId(HistoryVO vo)
	{
		sqlSessionTemplate.update(NAME_SPACE + ".updateIsFinishedByRoomId", vo);
	}
    
}
