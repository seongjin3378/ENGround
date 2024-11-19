package com.a.EnGround.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.a.EnGround.online.OnlineGameSystem;
import com.a.EnGround.repo.HistoryRepository;
import com.a.EnGround.repo.UserRepository;
import com.a.EnGround.service.QuizService;
import com.a.EnGround.vertify.MailSendService;
import com.a.EnGround.vo.HistoryVO;
import com.a.EnGround.vo.SessionVO;
import com.a.EnGround.vo.UserVO;

//
@Controller
public class UserController {

	@Autowired
	HistoryRepository historyRepository;

	@Autowired
	UserRepository Repository;

	@Autowired
	OnlineGameSystem onlineGameSystem;
	@Autowired
	private SqlSessionTemplate template;
	@Autowired
	private MailSendService mailSendService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Qualifier("quizService")
	@Autowired
	private QuizService quizService;

	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public String join() {

		return "join";
	}

	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String joinUser(UserVO user, RedirectAttributes redirectAttributes) {
		// 비밀번호 암호화
		user.setUserType(2);
		String encryptedPassword = passwordEncoder.encode(user.getPw());
		user.setPw(encryptedPassword);

		int result = Repository.join(user); // userRepository를 사용
		if (result > 0) {
			redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
			return "redirect:/login";
		} else {
			redirectAttributes.addFlashAttribute("message", "회원가입에 실패했습니다.");
			return "redirect:/join";
		}
	}

	@RequestMapping(value = "/joinId", method = RequestMethod.GET)
	public String joinId() {

		return "joinId";
	}

	@ResponseBody
	@RequestMapping(value = "/joinIdCheck", method = RequestMethod.POST)
	public Map joinIdCheck(@RequestParam("contact") String contact) {

		int count = Repository.joinIdCheck(contact);

		Map<String, String> map = new HashMap<>();

		if (count > 0) {
			// 아이디 중복 값이 있다.있다.
			map.put("result", "failed");
		} else {
			// 아이디 중복 값이 없다.
			map.put("result", "success");
		}

		return map;
	}

	@ResponseBody
	@RequestMapping(value = "/joinId", method = RequestMethod.POST)
	public Map<String, String> joinId(@RequestParam("contact") String contact) {

		System.out.println("전달된 contact: " + contact);
		UserVO user = Repository.findUserByContact(contact);

		System.out.println("findUserByContact 호출: " + contact);
		Map<String, String> result = new HashMap<>();

		System.out.println("찾은 아이디" + user);
		if (user != null) {
			System.out.println(user.getId());
			result.put("result", "success");
			result.put("id", user.getId());
		} else {
			result.put("result", "fail");
		}

		return result;
	}

	@RequestMapping(value = "/joinPw", method = RequestMethod.GET)
	public String joinPwOk() {

		return "joinPw";
	}

	@RequestMapping(value = "/findPw", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> findPw(@RequestParam("id") String id, @RequestParam("contact") String contact) {
		UserVO user = Repository.findUserById(id); // 사용자 정보 조회
		Map<String, String> result = new HashMap<>();

		if (user != null) {
			// 사용자 연락처 확인 (휴대폰 번호 또는 이메일)
			if (user.getContact().equals(contact)) {
				// 임시 비밀번호 생성
				String tempPw = generateTempPassword();
				String encryptedPw = passwordEncoder.encode(tempPw); // 임시 비밀번호 암호화

				// DB에 비밀번호 업데이트
				Repository.updatePassword(id, encryptedPw);

				// 이메일로 임시 비밀번호 전송
				sendEmail(contact, "임시 비밀번호 안내", "임시 비밀번호는: " + tempPw + "입니다.");

				result.put("result", "success");
				result.put("message", "임시 비밀번호가 이메일로 전송되었습니다.");
			} else {
				result.put("result", "fail");
				result.put("message", "아이디와 연락처가 일치하지 않습니다.");
			}
		} else {
			result.put("result", "fail");
			result.put("message", "해당 아이디의 사용자를 찾을 수 없습니다.");
		}

		return result;
	}

	private String generateTempPassword() {
		int length = 8; // 임시 비밀번호 길이
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder tempPw = new StringBuilder();
		Random rnd = new Random();
		for (int i = 0; i < length; i++) {
			tempPw.append(chars.charAt(rnd.nextInt(chars.length())));
		}
		return tempPw.toString();
	}

	// 이메일 전송 메서드 (예시로 구현, 실제 구현 필요)
	private void sendEmail(String to, String subject, String content) {

		mailSendService.mailSend("nomagold15@naver.com", to, subject, content);
	}

	@ResponseBody
	@RequestMapping(value = "/idCheck", method = RequestMethod.POST)
	public Map<String, String> idCheck(@RequestParam("id") String id) {

		int count = Repository.idCheck(id);

		Map<String, String> map = new HashMap<>();

		if (count > 0) {
			// 아이디 중복 값이 있다.있다.
			map.put("result", "failed");
		} else {
			// 아이디 중복 값이 없다.
			map.put("result", "success");
		}

		return map;
	}

	@ResponseBody
	@RequestMapping(value = "/nickCheck", method = RequestMethod.POST)
	public Map<String, String> nickCheck(@RequestParam("nick") String nick) {
		int count = Repository.nickCheck(nick);

		Map<String, String> map = new HashMap<>();

		if (count > 0) {
			// 아이디 중복 값이 있다.있다.
			map.put("result", "failed");
		} else {
			// 아이디 중복 값이 없다.
			map.put("result", "success");
		}

		return map;
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public String updatePassword(
            @RequestParam("nick") String nick,
            @RequestParam("pw") String currentPassword, // 입력된 기존 비밀번호
            @RequestParam("passwordValid") String newPassword, // 새 비밀번호
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        UserVO user = (UserVO) session.getAttribute("user");

        // 기존 비밀번호 확인 (암호화된 비밀번호와 비교)
        if (!passwordEncoder.matches(currentPassword, user.getPw())) {
            redirectAttributes.addFlashAttribute("message", "기존 비밀번호가 일치하지 않습니다.");
            return "redirect:/myPage";
        }

        // 새 비밀번호 암호화
        user.setNick(nick);
        user.setPw(passwordEncoder.encode(newPassword)); // 새 비밀번호 암호화 후 저장
        int result = Repository.updateUser(user); // userRepository 사용

        if (result > 0) {
            session.setAttribute("user", user);
            redirectAttributes.addFlashAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("message", "비밀번호 변경에 실패했습니다.");
        }

        return "redirect:/myPage";
    }

	@RequestMapping(value = "/myPage", method = RequestMethod.GET)
	public String getUserDetails(Model model, HttpSession session) {
		UserVO userVO = (UserVO) session.getAttribute("user");
		if (userVO != null) {
			UserVO user = Repository.findUserById(userVO.getId());
			session.setAttribute("user", user);
			List<HistoryVO> historyList = historyRepository.findByUserId(userVO.getId());
			model.addAttribute("historyList", historyList);
		}
		return "myPage";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {

		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginOk(@ModelAttribute UserVO vo, HttpSession session, RedirectAttributes redirectAttributes) {
        UserVO user = Repository.login(vo);

        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "로그인에 실패하셨습니다.");
            return "redirect:/login";
        }

        // 비밀번호가 암호화된 상태인지 확인하고 처리
        if (passwordEncoder.matches(vo.getPw(), user.getPw())) {
            // 평문으로 저장된 비밀번호와 일치하는지 확인
//            if (vo.getPw().equals(user.getPw())) {
//                // 평문 비밀번호가 일치하면 암호화하여 업데이트
//                user.setPw(passwordEncoder.encode(vo.getPw()));
//                userRepository.updateUser(user); // DB에 암호화된 비밀번호 저장
//            } else {
//                redirectAttributes.addFlashAttribute("message", "비밀번호가 일치하지 않습니다.");
//                return "redirect:/login";
//            }
        	
        	session.setAttribute("user", user);
            redirectAttributes.addFlashAttribute("message", user.getNick() + "님 환영합니다.");
            quizService.deleteGameSession(session);
            HistoryVO historyVO = new HistoryVO();
            historyVO.setUserId(user.getId());
            try {
                // 원래 수행하려던 작업
                quizService.updateIsFinishedAll(historyVO);
            } catch (DataIntegrityViolationException e) {
                // 예외 발생 시 로깅 또는 처리
                System.err.println("데이터 무결성 위반 오류가 발생했습니다: " + e.getMessage());
                // 로그를 기록하는 방식이나 사용자에게 알리는 추가 코드 작성 가능
            }
            return "redirect:/mainPage";
        }
        
        redirectAttributes.addFlashAttribute("message", "비밀번호가 일치하지 않습니다.");
        return "redirect:/login";
    }

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}

	@GetMapping("/userTypeUpdate")
	public String userTypeUpdate(@RequestParam("userType") int userType, @RequestParam("id") int id, Model model) {
		Map<String, Object> params = new HashMap<>();
		params.put("userType", userType);

		template.update("com.a.EnGround.repo.QuizRepository.userTypeUpdate", params);

		return "redirect:/reportListInfo?id=" + id;
	}

}
