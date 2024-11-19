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

import com.a.EnGround.vo.CategoryVO;
 
@Repository 
public class CategoryRepository {
    
    @Autowired
    JdbcTemplate template; 
    
    @Autowired
    SqlSessionTemplate sqlSessionTemplate;
    private final String NAME_SPACE = "CategoryMapper";
    
    public Page<CategoryVO> getAllData(Pageable pageable, String table) {
        Map<String, Object> map = new HashMap<String, Object>();
        System.out.println(pageable.getOffset());
        System.out.println(pageable.getPageSize());
        map.put("offset", pageable.getOffset());
        map.put("limit", pageable.getPageSize());
        map.put("table", table);
        
        int total = count();
        List<CategoryVO> categories = sqlSessionTemplate.selectList(NAME_SPACE + ".getAll", map);
        return new PageImpl<>(categories, pageable, total);
    }
    
    public int count() {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".count");
    }
    
    public CategoryVO selectOne(int categoryNo) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".findById", categoryNo);
    }
    
    public List<CategoryVO> getAllFromOnline()
    {
		return sqlSessionTemplate.selectList(NAME_SPACE + ".getAllFromOnline");
    	
    }
}
