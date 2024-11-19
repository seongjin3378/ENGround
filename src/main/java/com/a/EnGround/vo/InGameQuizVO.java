package com.a.EnGround.vo;


public class InGameQuizVO {
	    private int inGameQuizNo;  // 퀴즈 고유 번호
	    private String gameMode;    // 게임 모드 (nullable)
	    private QuizVO quiz;
	    private String table;
	    private String roomId;
	    
	    
	    public String getRoomId() {
			return roomId;
		}

		public QuizVO getQuiz() {
			return quiz;
		}

		public void setQuiz(QuizVO quiz) {
			this.quiz = quiz;
		}

		public void setRoomId(String roomId) {
			this.roomId = roomId;
		}

		public String getTable() {
			return table;
		}

		public void setTable(String table) {
			this.table = table;
		}

		// Getter 및 Setter
	    public int getInGameQuizNo() {
	        return inGameQuizNo;
	    }

		public void setInGameQuizNo(int inGameQuizNo) {
	        this.inGameQuizNo = inGameQuizNo;
	    }

	    public String getGameMode() {
	        return gameMode;
	    }

	    public void setGameMode(String gameMode) {
	        this.gameMode = gameMode;
	    }

		public void setNewData(boolean b) {
			// TODO Auto-generated method stub
			
		}

}
