package com.a.EnGround.vertify;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

//
@Component
public class CoolSMSVertifyServiceImpl implements SMSVertifyService{
	
	private DefaultMessageService messageService;



	@Override
	public String createVertifyCode() {
		Random random = new Random();	
		int createNum = 0;  		
		String ranNum = ""; 			
        	int letter    = 6;			
		String resultNum = "";  		
		
		for (int i=0; i<letter; i++) { 
            		
			createNum = random.nextInt(9);		
			ranNum =  Integer.toString(createNum);  
			resultNum += ranNum;			
		}
		return resultNum;	
	}
	
	



	@Override
	public <K, V> Object sendOne(HashMap<K, V> param) {
		 Message message = new Message();
		 message.setFrom((String) param.get("SENDER"));
	     message.setTo((String) param.get("RECEIVER"));
	     String vertifyCode = createVertifyCode();
	     message.setText("EnGround 인증번호는 ["+ vertifyCode+"] 입니다.");
	    // SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
	     //System.out.println(response);
	     
	     List<Object> result = new ArrayList<>();
	     result.add(vertifyCode);
	     //result.add(response);
	     
	     return result;
	}





	@Override
	public HashMap<String, String> setParam(String receiver) {
		// TODO Auto-generated method stub
		HashMap<String, String> param = new HashMap<>();
		param.put("INSERT_API_KEY", "API 키 입력");
		param.put("INSERT_API_SECRET_KEY", "API 시크릿 키 입력");
		param.put("API_URL", "https://api.coolsms.co.kr");
		param.put("SENDER", "등록전화번호입력");
		param.put("RECEIVER", receiver);
		return param;
	}

	@Override
	public <K, V> void apiAuthentication(HashMap<K, V> param) {
		messageService = NurigoApp.INSTANCE.initialize((String)param.get("INSERT_API_KEY"), (String)param.get("INSERT_API_SECRET_KEY"), (String)param.get("API_URL"));
		
	}

}
