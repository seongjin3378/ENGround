package com.a.EnGround.repo;

import com.a.EnGround.vo.AdVO;
import java.util.List;

public interface AdRepository {
    void insertAd(AdVO adVO);
    List<AdVO> selectAllAds();  // 이 메서드는 AdVO 객체의 리스트를 반환합니다.
	AdVO selectLatestAd();
}