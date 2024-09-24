package com.a.EnGround.service;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;


import com.a.EnGround.vo.QuizVO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface QuizService {

	    public QuizVO loadQuiz(HttpSession session) throws JsonMappingException, JsonProcessingException;


	    public void checkPoint(HttpSession session) throws JsonMappingException, JsonProcessingException;


	    public void deleteGameRoom(HttpSession session) throws JsonMappingException, JsonProcessingException;


	    public void insertHistoryAndDeleteGameRoom(HttpSession session, Model model) throws JsonMappingException, JsonProcessingException;


	    public ResponseEntity<?> nextQuestion(HttpSession session) throws JsonMappingException, JsonProcessingException;


	    public String[] getHint(String hint, HttpSession session) throws JsonMappingException, JsonProcessingException;


	    public boolean checkAnswer(String correct, HttpSession session) throws JsonMappingException, JsonProcessingException;


	    public String generateAudio();
}
