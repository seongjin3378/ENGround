package com.a.EnGround.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.Repository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.a.EnGround.repo.ReportUserRepository;
import com.a.EnGround.repo.UserRepository;
import com.a.EnGround.vo.ReportUserVO;
import com.a.EnGround.vo.UserVO;

@Controller
public class AdminUserController {
	 private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReportUserRepository reportUserRepository;
    
    @Autowired
    private SqlSessionTemplate template; 
    
    @RequestMapping(value="/manager",method=RequestMethod.GET)
	public String join() {
		
		return "manager";
	}
    
    @GetMapping("/userInquiry")
    public String userSelect(
    		@RequestParam(value= "userType", required =false, defaultValue="-1")int userType,
    		@RequestParam(value="id", required =false, defaultValue ="")String id,
    		@RequestParam(value="createdate", defaultValue ="")String createdate,
    		Model model,
    		HttpSession session){
        	
        	logger.info("Received userType: {}", userType);
        	
        	// 세션에 userType 저장
        	session.setAttribute("userType", userType);
        	
        	// 선택된 유저 타입에 따라 적절한 테이블에서 유저 목록을 조회 합니다.
        	List<UserVO> users ;
        	
        	if(userType == 1){
            	users = userRepository.findUserType(1);
            	// "유저타입이 1(관리자)인" 유형의 유저를 조회
            } else if(userType == 2){
            	users = userRepository.findUserType(2);
            	// "유저타입이 2(일반유저)인" 유형의 유저를 조회
            } else if(userType == 3){
            	users = userRepository.findUserType(3);
            	// "유저타입이 3(차단유저)인" 유형의 유저를 조회
            } else if(userType == 4){
            	users = userRepository.findUserType(4);
            	// "유저타입이 4(신고유저)인" 유형의 유저를 조회
            } else {
            	users = userRepository.findAllUser();
            	alert("변경되지 않았습니다.");
            	// "유저타입이 0(전체유저)인" 유형의 유저를 조회
            }

            // 조회된 유저 목록을 모델에 추가하여 뷰에서 사용할 수 있도록 설정
            model.addAttribute("users", users);
            model.addAttribute("userType", userType);
            users.forEach(vo-> logger.info("Received users: {},{}" , vo.getUserType() , vo.getId()));
            // userInquiry 페이지로 이동
            return "userInquiry";
        }
    
    private void alert(String string) {
		// TODO Auto-generated method stub
		
	}

	/*@PostMapping("/userInquiry") // 검색 했을 경우 PostMapping "/userInquiry"으로 데이터를 요청한다.
    // userInquiry
    // @PostMapping에서는 되도록 RequestParam 지양한다. 그 이유는 아래와 같이 value값으로 id를 요청 했을 경우 그 아래 
    // UserVO id와 타입이 맞지 않기 때문이다. 그리고 UserVO에서 컬럼값을 갖고 오던지 jsp에서 name속성을 요청 하던지...
    public String userInquiry( 
    		// @RequestParam(value= "id", required =false)
	    	UserVO id,  // 객체  UserVO안의 변수id, userType jsp에서 form 전송할때  name이 id, userType 
	    	Model model,
	        HttpSession session){
        	
        	logger.info("Received id: {}, {}, {}", id.getId(), id.getUserType(), id.getCreatedate());
        	
        	session.setAttribute("id", id);
        	
        	List<UserVO> ids = null;
        	
        	if(id != null) {
        		id = userRepository.findUserById(id.getId());
        		// FindUserById 함수 인자는 String이기 때문에
        		// UserVo인 id를 넣지 못함 
        	}else {
        		return "redirect:/userInquiry";
        	}

            // 조회된 유저 목록을 모델에 추가하여 뷰에서 사용할 수 있도록 설정
//            model.addAttribute("ids", ids);
            
            // userInquiry 페이지로 이동
            return "userInquiry";
        }*/
    
    @GetMapping("/userInfo")
    public String userInfo(
            @RequestParam("id") String id,
            @RequestParam("userType") Integer userType, 
            @RequestParam("createdate") String createdate, 
            Model model) {
        
        UserVO user = userRepository.findUserById(id); // id로 사용자 정보 조회
        model.addAttribute("user", user); // 사용자 정보를 모델에 추가
        model.addAttribute("userType", userType); // 비밀번호 추가
        model.addAttribute("createdate", createdate); // 닉네임 추가
        
        return "userInfo"; // userinfo.jsp로 이동
    }
    
    @ResponseBody
    @PostMapping("/userSearch")
    public UserVO userSearch(UserVO id) {
        logger.info("Received id: {}, {}, {}", id.getId(), id.getUserType(), id.getCreatedate());

        if (id.getId() != null && !id.getId().equals("")) {
            id = userRepository.findUserById(id.getId());
        } else {
            return null;
        }
        
        logger.info("Received id: {}, {}, {}", id.getId(), id.getUserType(), id.getCreatedate());
        
        return id;
    }
    
        
    @PostMapping("/userInfoUpdate")
    public String userInfoUpdate(
            @RequestParam("id") String id,
            @RequestParam("userType") Integer userType, 
            @RequestParam("createdate") String createdate, 
            Model model,
            HttpSession session) {
    	/*	첫번째 방법은 맵퍼로 두가지 타입을 전달 해서 업데이트
	    	업데이트할 데이터를 맵에 담아서 매퍼에 전달
	        Map<String, Object> userTypeMap = new HashMap<>();
	        userTypeMap.put("id", id);
	        userTypeMap.put("userType", userType);
	
	        DB에 유저 타입 업데이트        
	    	template.update("UserMapper.userTypeUpdate", userTypeMap);
    	*/
    	// vo에 UserVO타입을 생성하여 id,userType을 set하여 전달 
    	UserVO vo = new UserVO();
    	vo.setId(id);
    	vo.setUserType(userType);
    	int userTypeOK = userRepository.userTypeUpdate(vo);
    	// userType이 업데이트 성공시, 실패시 
        if(userTypeOK == 1){
    	UserVO user = userRepository.findUserById(id); 
        model.addAttribute("user", user); 
        model.addAttribute("userType",  userType); 
        model.addAttribute("createdate", createdate);
        }
        else{
        	return "userInfo";
        }
        // 업데이트 완료 후 다시 userInquiry 페이지로 이동
        return "userInquiry"; 
    }
    
//    @GetMapping("/reportListInfo")
//    public String reportListInfo(
//            @RequestParam("reportNo") int reportNo,
//            @RequestParam("reportedId") String reportedId,
//            @RequestParam("idReported") String idReported,
//            @RequestParam(value = "userType", required = false, defaultValue = "0") Integer userType, 
//            Model model,
//            HttpSession session) {
//        
//        // 요청 파라미터 로그 출력
//        System.out.println("Received parameters: ");
//        System.out.println("reportNo: " + reportNo);
//        System.out.println("reportedId: " + reportedId);
//        System.out.println("idReported: " + idReported);
//        System.out.println("userType: " + userType);
//        
//        // reportNo에 해당하는 보고서 조회
//        ReportUserVO vo = template.selectOne("ReportUserMapper.findIdReportedUserType", idReported);
//        
//        if (vo == null) {
//            model.addAttribute("errorMessage", "신고자를 찾을 수 없습니다.");
//            return "reportList";
//        }
//        model.addAttribute("vo", vo);
//        return "reportListInfo";
//    }
    @GetMapping("/reportList")	
    public String reportList(
            @RequestParam(value= "reportNo", required = false) Integer reportNo,
            @RequestParam(value="reportedId", required = false) String reportedId,
            @RequestParam(value="idReported", required = false) String idReported,
            @RequestParam(value="userType", required = false) Integer userType,
            Model model,
            HttpSession session) {

        List<ReportUserVO> reportList;
        reportList = reportUserRepository.findAllReports();
        model.addAttribute("reportList", reportList); 
        return "reportList";
    }
    
    @GetMapping("/reportListInfo")	
    public String reportListInfo(
            @RequestParam(value= "reportNo", required = false) Integer reportNo,
            @RequestParam(value="reportedId", required = false) String reportedId,
            @RequestParam(value="idReported", required = false) String idReported,
            @RequestParam(value="userType", required = false) Integer userType,
            Model model,
            HttpSession session) {
    	System.out.println("reportNo: " + reportNo);
    	System.out.println("userType: " + userType);
        
        ReportUserVO vo = reportUserRepository.findReportNo(reportNo);
        
        model.addAttribute("vo", vo); 
        System.out.println("reportNo: " + reportNo);
        return "reportListInfo";
    }

    
 
    @GetMapping("/reportUserTypeUpdate")
    public String reportUserTypeUpdate(
            @RequestParam(value = "reportNo", required = false) Integer reportNo,
            @RequestParam(value = "reportedId", required = false) String reportedId,
            @RequestParam(value = "idReported", required = false) String idReported,
            @RequestParam(value = "userType", required = false) Integer userType,
            Model model) {

        // Null 체크 추가
        if (idReported == null || userType == null) {
            // 적절한 예외 처리 또는 리다이렉트
            model.addAttribute("error", "필수 정보가 누락되었습니다.");
            return "reportListInfo"; // 오류 페이지로 리턴
        }

        UserVO vo = new UserVO();
        vo.setId(idReported);
        System.out.println("Reported ID: " + idReported);
        vo.setUserType(userType);
        System.out.println("User Type: " + userType);

        int userTypeOK = userRepository.userTypeUpdate(vo);

        // userType이 업데이트 성공시, 실패시 
        if (userTypeOK == 1) {
            model.addAttribute("reportNo", reportNo);
            model.addAttribute("userType", userType);
            System.out.println("Updated User Type: " + userType);
            System.out.println("Reported ID: " + idReported);
        } else {
            model.addAttribute("error", "유저 타입 업데이트 실패.");
            return "reportListInfo"; // 실패 시 리다이렉트
        }

        return "redirect:/reportList";
    }
}


   