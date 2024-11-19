package com.a.EnGround.quiz;

import java.util.HashMap;
import java.util.List;

import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.InGameQuizVO;
import com.a.EnGround.vo.QuizVO;
/*장성진*/
public interface ConvertBlankSystem {

	public String execute(String hint, List<InGameQuizVO> quiz, GameRoomVO gameRoomVo);
}
