package com.a.EnGround.repo;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.a.EnGround.vo.ReportUserVO;

@Repository
public class ReportUserRepository {

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;
    private final String NAME_SPACE = "ReportUserMapper";
    // Reported_Id에 따른 보고서 조회
    public List<ReportUserVO> findReportedId(String reportedId) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findReportedId", reportedId);
    }

    // Id_Reported에 따른 보고서 조회
    public List<ReportUserVO> findIdReported(String idReported) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findIdReported", idReported);
    }

    // Id_Reported를 기준으로 userType 가져오기 (user 테이블과 report 테이블 조인)
    public List<ReportUserVO> findAllReports() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllReports");
        // sqlSessionTemplate객체가 mapper에 접근하는 dao객체인데 NAME_SPACE에 있는 mapper id가 .findAllReports 쿼리에 접근 한다.
    }

	public ReportUserVO findReportNo(Integer reportNo) {
		return sqlSessionTemplate.selectOne(NAME_SPACE + ".findReportNo", reportNo);
	}
	public int userTypeUpdate(ReportUserVO vo) {
		return sqlSessionTemplate.update(NAME_SPACE + ".userTypeUpdate", vo);
	}
	
	public int insertReport(ReportUserVO vo)
	{
		return sqlSessionTemplate.insert(NAME_SPACE +".insertReport", vo);
	}
	}
