package com.a.EnGround.vertify;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.core.net.MimeMessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Service
public class MailSendService {

	@Autowired
	private JavaMailSender mailSender;
	private int authNumber;
	private String email;
	
	private Map<String, Integer> authNumbers = new HashMap<>(); // 사용자 이메일과 인증 번호 맵
    private Map<String, Long> authTimestamps = new HashMap<>(); // 인증 번호 생성 시간
	
	
	public void makeRandomNumber(String email) {
		
		// 난수의 범위 111111 ~ 999999 (6자리 난수)
		Random r = new Random();
		int checkNum = r.nextInt(888888) + 111111;
		authNumbers.put(email, checkNum);
        authTimestamps.put(email, System.currentTimeMillis()); // 현재 시간 저장
        System.out.println("이메일: " + email + ", 인증번호: " + checkNum);
        System.out.println("현재 authNumbers 상태: " + authNumbers); // 추가된 로그
		authNumber = checkNum;
		//세션에 저장
		ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession httpSession = servletRequestAttribute.getRequest().getSession(true);
		httpSession.setAttribute("authNumber", checkNum);
	}
		
		
	//이메일 보낼 양식! 
	public String joinEmail(String email) {
		this.email = email;
		makeRandomNumber(email);
		String setFrom = "nomagold15@naver.com"; // email-config에 설정한 자신의 이메일 주소를 입력
		String toMail = email;
		String title = "회원 가입 인증 이메일 입니다."; // 이메일 제목 
		String content = 
				"홈페이지를 방문해주셔서 감사합니다." + 	//html 형식으로 작성 ! 
                "<br><br>" + 
			    "인증 번호는 " + authNumber  +  "입니다." + 
			    "<br>" + 
			    "해당 인증번호를 인증번호 확인란에 기입하여 주세요."; //이메일 내용 삽입
		mailSend(setFrom, toMail, title, content);
		return Integer.toString(authNumber);
	}
		
		//이메일 전송 메소드
		public void mailSend(String setFrom, String toMail, String title, String content) { 
			MimeMessage message = mailSender.createMimeMessage();
			
			try {
				MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
				helper.setFrom(setFrom);
				helper.setTo(toMail);
				helper.setSubject(title);
				// true 전달 > html 형식으로 전송 , 작성하지 않으면 단순 텍스트로 전달.
				helper.setText(content,true);
				System.out.println("이메일 전송 시작: " + toMail); 
				mailSender.send(message);
				 System.out.println("메일 전송 완료"); 
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		
		public boolean mailVertify(String email, String mailVertify) {
			
			if (authNumbers.containsKey(email)) {
	            long currentTime = System.currentTimeMillis();
	            long generatedTime = authTimestamps.get(email);

	            // 2분 30초 후 인증 번호 만료 (150000ms)
	            if (currentTime - generatedTime > 150000) {
	                System.out.println("인증 번호가 만료되었습니다.");
	                authNumbers.remove(email);
	                authTimestamps.remove(email);
	                return false;
	            }

	            if (Integer.toString(authNumbers.get(email)).equals(mailVertify)) {
	                System.out.println("인증 성공!");
	                return true;
	            } else {
	                System.out.println("인증 실패!");
	                return false;
	            }
	        }
	        System.out.println("이메일이 존재하지 않습니다.");
	        return false;
	    }
			
		
}
