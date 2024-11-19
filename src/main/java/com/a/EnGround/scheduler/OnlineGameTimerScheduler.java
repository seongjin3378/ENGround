package com.a.EnGround.scheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import com.a.EnGround.controller.OnlineInGameController;
import com.a.EnGround.repo.GameRoomRepository;
import com.a.EnGround.service.QuizService;
import com.a.EnGround.vo.GameRoomVO;
import com.a.EnGround.vo.HistoryVO;
import com.a.EnGround.vo.MessageVO;
import com.a.EnGround.vo.SessionVO;


/*장성진*/
@Component
public class OnlineGameTimerScheduler {
	private ThreadPoolTaskScheduler OnlineTimerScheduler;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Qualifier("onlineService")
	@Autowired
	private QuizService quizService;
	private ConcurrentHashMap<String, Integer> timeMap = new ConcurrentHashMap<String, Integer>();
	private static final Logger logger = LoggerFactory.getLogger(OnlineGameTimerScheduler.class);

	public void insertTimeMap(String roomId, int timer) {
		timeMap.put(roomId, timer);
	}

	public int getTimeMap(String roomId) {
		return timeMap.get(roomId);
	}

	public void removeTimeMap(String roomId) {
		timeMap.remove(roomId);
	}

	public void startTimeScheduler() {
		OnlineTimerScheduler = new ThreadPoolTaskScheduler();
		OnlineTimerScheduler.initialize();
		OnlineTimerScheduler.schedule(ReduceTimer(), getTrigger());
	}

	public void stopTimeScheduler() {
		OnlineTimerScheduler.shutdown();
	}

	private Runnable ReduceTimer() {
		return () -> {
			MessageVO vo = new MessageVO();
			for (Map.Entry<String, Integer> entry : timeMap.entrySet()) {
				String roomId = entry.getKey();
				Integer timer = entry.getValue();
				if (timer < -2) {
					
					removeTimeMap(roomId);
					HistoryVO historyVO = new HistoryVO();
					historyVO.setRoomId(roomId);
					quizService.updateIsFinishedAll(historyVO);
					continue;
					
				}
				timeMap.put(roomId, timer - 1);
				logger.info("roomId : {} , timer : {}", roomId, timer);
				vo.setMessage(new String[] { Integer.toString(timer) });
				messagingTemplate.convertAndSendToUser(roomId, "/getTimeMap", vo);
			}

		};
	}

	private Trigger getTrigger() {
		// 작업 주기 설정
		return new PeriodicTrigger(1, TimeUnit.SECONDS);
	}

}
