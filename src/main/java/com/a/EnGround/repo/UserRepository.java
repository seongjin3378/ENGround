package com.a.EnGround.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.a.EnGround.vo.UserVO;

@Repository
public class UserRepository {
	
	@Autowired
	 SqlSessionTemplate sqlSessionTemplate;
    private final String NAME_SPACE = "UserMapper";
	

    public int join(UserVO vo) {
        return sqlSessionTemplate.insert(NAME_SPACE + ".insert", vo);
    }

    public UserVO login(UserVO vo) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".login", vo);
    }

    public UserVO findUserById(String id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".findUserById", id);
    }
    
    public UserVO findUserByContact(String contact) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".findUserByContact", contact);
    }
    
    public int joinIdCheck(String contact) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".joinIdCheck", contact);
    }

    public int idCheck(String id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".idCheck", id);
    }

    public int nickCheck(String nick) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".nickCheck", nick);
    }

    public int updateUser(UserVO vo) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateUser", vo);
    }
    
    public int updateNick(UserVO vo) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateNick", vo);
    }
    
    public int userTypeUpdate(UserVO vo) {
        return sqlSessionTemplate.update(NAME_SPACE + ".userTypeUpdate", vo);
    }
    public List<UserVO> findUserType(int userType) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findUserType", userType);
    }
	public List<UserVO> findAllUser() {
		return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllUser");
	}
	public int updatePassword(String id, String pw) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("pw", pw);
        return sqlSessionTemplate.update(NAME_SPACE + ".updatePassword", params);
    }
    
    public UserVO findUserWithEmailById(String id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".findUserWithEmailById", id);
    }

}
