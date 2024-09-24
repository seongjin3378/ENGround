package com.a.EnGround.repo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.a.EnGround.vo.AdVO;
import java.util.List;

@Repository
public class AdRepositoryImpl implements AdRepository {

    @Autowired
    private SqlSession sqlSession;
    
    private static final String NAMESPACE = "com.a.EnGround.repo.AdRepository";

    @Override
    public void insertAd(AdVO adVO) {
        sqlSession.insert(NAMESPACE + ".insertAd", adVO);
    }

    @Override
    public List<AdVO> selectAllAds() {
        return sqlSession.selectList(NAMESPACE + ".selectAllAds");
    }
    
    
    public AdVO selectLatestAd() {
        return sqlSession.selectOne(NAMESPACE + ".selectLatestAd");
    }
}
