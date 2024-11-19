package com.a.EnGround.controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.a.EnGround.online.OnlineGameSystem;
import com.a.EnGround.repo.UserRepository;
import com.a.EnGround.scheduler.OnlineGameTimerScheduler;
import com.a.EnGround.vo.SessionVO;
import com.a.EnGround.vo.UserVO;
import com.fasterxml.jackson.core.JsonProcessingException;
/*장성진*/
@Controller
public class TestController {
	
	@Autowired
	OnlineGameSystem onlineGameSystem;
	@Autowired
	OnlineGameTimerScheduler onlineGameTimerScheduler;
	
	@Autowired
	UserRepository userRepository;
	private String roomId = "1";
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	
	@RequestMapping(value="/schedulerAdd", method = RequestMethod.GET)
	void addTimeMap() 
	{
		roomId+="1";
		onlineGameTimerScheduler.insertTimeMap(roomId, 5);
		
	}
	@ResponseBody
	@RequestMapping(value="/schedulerGet", method = RequestMethod.GET)
	int getTimeMap(@RequestParam("roomId") String roomId)
	{
		return onlineGameTimerScheduler.getTimeMap(roomId);
	}
	
	@GetMapping(value="/userAdd")
	void addUser()
	{
		 BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	     String rawPassword = "test"; // 사용자가 입력한 비밀번호
	     String encodedPassword = encoder.encode(rawPassword);
	     for(int i=0; i<100; i++)
	     {
	     UserVO vo = new UserVO();
	     vo.setId("test"+i);
	     vo.setPw(encodedPassword);
	     vo.setNick("테스트유저아이디"+i);
	     vo.setUserType(1);
	     vo.setContact("010-"+i);
	     vo.setCreatedate("2024-09-13 11:36:46");
	     userRepository.join(vo);
	     }
	     
	}
}
