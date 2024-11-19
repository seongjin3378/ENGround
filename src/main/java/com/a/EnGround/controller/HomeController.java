package com.a.EnGround.controller;
 
import java.text.DateFormat;
import java.util.Date; 
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.a.EnGround.repo.QuizRepository;
import com.a.EnGround.service.QuizService;
import com.a.EnGround.vo.QuizVO;
import com.a.EnGround.vo.UserVO;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	
	/**
	 * Simply selects the home view to render by returning its name.1
	 */
	@Qualifier("quizService")
	@Autowired
    private QuizService quizService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		
		return "login";
	}
	
	@RequestMapping(value = "/mainPage", method = RequestMethod.GET)
	public String mainPage() {
		
		return "mainPage";
	}
	
	@RequestMapping(value = "/lobby", method = RequestMethod.GET)
	public String lobby() {
		
		return "lobby";
	}
	
	@RequestMapping(value = "/onlineGame", method = RequestMethod.GET)
	public String onlineGame() {
		
		return "onlineGame";
	}
	@RequestMapping(value = "/waitingRoom", method = RequestMethod.GET)
	public String waitingRoom() {
		
		return "waitingRoom";
	}

	

	
	@ResponseBody
	@RequestMapping(value = "/setGameModeSession", method = RequestMethod.POST)
	public void mainPage(HttpSession session, @RequestParam(name = "gameMode") String gameMode) {
		
		session.setAttribute("gameMode", gameMode);
	}
	
	@RequestMapping(value = "/game/*", method = RequestMethod.GET)
	public String game(HttpServletRequest request, HttpSession session, Model model) {
		String categoryName = request.getServletPath().split("/")[2];
		System.out.println("ㅇㅈㅇㅈㅇㅈㅇㅈ"+categoryName);
		session.setAttribute("categoryName", categoryName);
		return "game";
	}
	
	@GetMapping("/practice")
	public String practiceGameView()
	{
		return "randomWords";
	}
	
	/*@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login(HttpSession session) {
		UserVO user = (UserVO)session.getAttribute("user");
		if(user != null) {
			return "Redirect:/join";
		}else {
			return "Redirect:/login";
		}
	}*/
	


}
