package com.a.EnGround.controller;

import java.time.DateTimeException;
import java.time.LocalDateTime;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.a.EnGround.service.QuizService;
import com.a.EnGround.service.QuizServiceImpl;
import com.a.EnGround.vo.AnswerVO;
import com.a.EnGround.vo.QuizVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class GameController {

	@Qualifier("quizService")
	@Autowired
    private QuizService quizService;
    @Autowired
    private ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    //문제 불러오는 코드
    @ResponseBody
    @RequestMapping(value = "/load", method = RequestMethod.GET)
    public QuizVO gameQuiz(HttpSession session) throws JsonMappingException, JsonProcessingException {
        return quizService.loadQuiz(session);
    }
    //홈페이지 새로고침하거나 홈페이지를 끄면 현재 몇라운드 까지 진행했는지를 저장함
    @ResponseBody
    @RequestMapping(value = "/checkPoint", method = RequestMethod.GET)
    public void checkPoint(HttpSession session) throws JsonMappingException, JsonProcessingException {
        quizService.checkPoint(session);
    }
    
    //게임전적 불러오기를 취소 할 때 gameRoom db가 삭제됨
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public void deleteGameRoom(HttpSession session) throws JsonMappingException, JsonProcessingException {
    	quizService.deleteGameRoom(session);
    	quizService.deleteHistory(session);
    }

    //게임이 끝난 후 전적 db에 저장 후 gameRoom db에 데이터들 삭제
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public String resultPage(Model model, HttpSession session) throws JsonMappingException, JsonProcessingException {
        quizService.findHistoryAndDeleteGameInForm(session, model);
        logger.info("result controller 실행");
        return "result";
    }

    //다음 문제를 불러오는 알고리즘
    @RequestMapping(value = "/next", method = RequestMethod.GET)
    public ResponseEntity<?> nextQuestion(HttpSession session) throws JsonMappingException, JsonProcessingException {
        return quizService.nextQuestion(session);
    }

    
    //힌트1, 힌트2, 힌트3이 타이머에 따라 불러옴
    @ResponseBody
    @RequestMapping(value = "/hint", method = RequestMethod.POST)
    public String[] hint(@RequestParam(name = "hint") String hint, HttpSession session) throws JsonMappingException, JsonProcessingException {
        return quizService.getHint(hint, session);
    }

    //문제를 맞출 경우 gameroom db에 score를 업데이트 시킴
    @ResponseBody
    @RequestMapping(value = "/correct", method = RequestMethod.POST)
    public boolean correct(AnswerVO vo, HttpSession session) throws JsonMappingException, JsonProcessingException {
    	LocalDateTime now = LocalDateTime.now();
    	LocalDateTime answerTime = null;
    	logger.info("answerTime: {}"+vo.getAnswerTime());
    	  try {
    		  answerTime = now.withSecond(vo.getAnswerTime());
          } catch (DateTimeException e) {
              answerTime = now.withSecond(59);
          }
    
        return quizService.checkAnswerAndUpdateScore(vo.getCorrect(), answerTime, session);
    }
    
    //안사용함
    @RequestMapping(value = "/generate-audio", method = RequestMethod.GET)
    public String generateAudio() {
        return quizService.generateAudio();
    }
}
