package com.a.EnGround.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.a.EnGround.vertify.MailSendService;
import com.a.EnGround.vertify.SMSVertifyService;

import net.nurigo.sdk.message.response.SingleMessageSentResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class JoinController {
	
	@Autowired
	private SMSVertifyService sms;
	
	@Autowired
	private MailSendService mailSendService;
	
	private Map<String, Integer> authNumbers = new ConcurrentHashMap<>();
	
	
	
	@ResponseBody
	@RequestMapping(value = "/phoneVertify", method = RequestMethod.POST)
	public List<Object> phoneVertifyOk(@RequestParam("receiver") String receiver)
	{
		
		HashMap<String, String> param = sms.setParam(receiver);
		sms.apiAuthentication(param);
		List<Object> response =  (List<Object>) sms.sendOne(param);
		
		return response;
	}
	
	@PostMapping("/mailSend")
	@ResponseBody
	public String mailCheck(@RequestParam("email") String email) {
	    System.out.println("이메일 인증 요청이 들어옴!");
	    System.out.println("이메일 인증 이메일 : " + email);
	    return mailSendService.joinEmail(email);
	}
	
	@PostMapping("/mailCheck")
	public ResponseEntity<String> mailCheck(@RequestParam String email, @RequestParam int inputAuthNumber, HttpServletRequest request) {
		System.out.println("이메일: " + email + ", 입력된 인증번호: " + inputAuthNumber);
		// 저장된 인증번호 가져오기
		// 세션에 저장된 번호를 가져옴
		HttpSession session = request.getSession();
		int storedAuthNumber = 0;
		storedAuthNumber = (int)session.getAttribute("authNumber");
//	    Integer storedAuthNumber =  authNumbers.get(email);
	    System.out.println("저장된 인증번호: " + storedAuthNumber);
	    
	 // 저장된 인증번호가 null인지 확인
	    if (storedAuthNumber == 0) {
	        System.out.println("저장된 인증번호가 없습니다.");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일에 대한 인증번호가 존재하지 않습니다.");
	    }
	    
	    if ( storedAuthNumber == inputAuthNumber ) {
	        // 인증번호가 일치할 경우 회원가입 로직 수행
	        return ResponseEntity.ok("회원가입 성공");
	    } else {
	        // 인증번호가 일치하지 않을 경우
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증번호가 일치하지 않습니다.");
	    }
	}
	
}
