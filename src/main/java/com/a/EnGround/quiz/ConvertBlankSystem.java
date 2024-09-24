package com.a.EnGround.quiz;

import java.util.HashMap;

import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.QuizVO;

public interface ConvertBlankSystem {
	public String execute(String hint, HashMap<Integer, QuizVO> quiz, GameRoomVO gameRoomVo);
}
