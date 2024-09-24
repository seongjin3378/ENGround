package com.a.EnGround.controller;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.a.EnGround.repo.QuizRepository;
import com.a.EnGround.vo.QuizVO;

@Controller
public class QuizManagementController {
    private static final Logger logger = LoggerFactory.getLogger(QuizManagementController.class);

    @Autowired
    private QuizRepository quizRepository; 
    
    @Autowired
    private SqlSessionTemplate template;
    
    /**
     * 퀴즈 업데이트 페이지를 반환합니다.
     * @param quizType 퀴즈 타입 (단어 또는 문장)
     * @param model 뷰에 데이터를 전달하기 위한 모델
     * @param session 사용자의 세션 (게임 모드에 따라 적절한 QuizService를 선택하기 위해 사용)
     * @return 퀴즈 업데이트 페이지
     */
    @GetMapping("/quizUpdate")
    public String quizUpdate(
            @RequestParam(value = "quizType", required = false, defaultValue = "word") String quizType,
            Model model,
            HttpSession session) {

        logger.info("Received quizType: {}", quizType);
        
        
        // 세션에 quizType 저장
        session.setAttribute("quizType", quizType);
        
        // 선택된 퀴즈 타입에 따라 적절한 테이블에서 퀴즈 목록을 조회합니다.
        List<QuizVO> quizzes;

        if ("sentence".equals(quizType)) {
            // "sentence" 유형의 퀴즈를 조회
            quizzes = template.selectList("com.a.EnGround.repo.QuizRepository.findAllQuiz", "select_sentence");
        } else {
            // 기본적으로 "word" 유형의 퀴즈를 조회
            quizzes = template.selectList("com.a.EnGround.repo.QuizRepository.findAllQuiz", "select_word");
        }

        // 조회된 퀴즈 목록을 모델에 추가하여 뷰에서 사용할 수 있도록 설정
        model.addAttribute("quizzes", quizzes);
        quizzes.forEach(vo -> logger.info("quizzes for log : {}", vo.getQuizNo())); // 각 퀴즈의 번호를 로그에 기록
        model.addAttribute("quizType", quizType); // 현재 퀴즈 타입을 모델에 추가

        // quizUpdate 페이지로 이동
        return "quizUpdate";
    }

    /**
     * 특정 퀴즈의 업데이트 정보를 보여주는 페이지를 반환합니다.
     * @param quizNo 퀴즈 번호
     * @param quizType 퀴즈 타입 (단어 또는 문장)
     * @param model 뷰에 데이터를 전달하기 위한 모델
     * @param session 사용자의 세션 (게임 모드에 따라 적절한 테이블을 선택하기 위해 사용)
     * @return 퀴즈 업데이트 정보 페이지
     */
    @GetMapping("/quizUpdateInfo")
    public String quizUpdateInfo(
            @RequestParam(value = "quizType", required = false, defaultValue = "word") String quizType,
            @RequestParam("quizNo") int quizNo,
            Model model,
            HttpSession session) {

        logger.info("Received quizNo: {}, quizType: {}", quizNo, quizType);

        // 조회할 퀴즈 데이터를 담을 객체 선언
        QuizVO quiz; 
        // 쿼리에 전달할 파라미터를 담을 맵을 선언
        Map<String, Object> params = new HashMap<>(); 

        // quizType에 따라 적절한 테이블 이름을 설정
        if ("sentence".equals(quizType)) {
            params.put("table", "select_sentence");
        } else {
            params.put("table", "select_word");
        }
        params.put("quizId", quizNo); // 퀴즈 번호를 파라미터에 추가

        // MyBatis 쿼리를 실행하여 데이터를 조회
        quiz = template.selectOne("com.a.EnGround.repo.QuizRepository.findByQuizId", params);

        // 조회된 퀴즈 데이터를 모델에 추가하여 뷰에서 사용할 수 있도록 설정
        if (quiz != null) {
            model.addAttribute("quiz", quiz);
            logger.info("Quiz loaded for quizNo: {}", quizNo);
        } else {
            logger.warn("No quiz found for quizNo: {}", quizNo);
        }

        // quizUpdateInfo.jsp 페이지로 이동
        return "quizUpdateInfo";
    }
    
    /**
     * 새로운 퀴즈를 데이터베이스에 추가합니다.
     * @param quizType 퀴즈 타입 (단어 또는 문장)
     * @param quizNo 퀴즈 번호
     * @param question 퀴즈 질문
     * @param timer 제한 시간
     * @param hint1 힌트1
     * @param hint2 힌트2
     * @param hint3 힌트3
     * @param answer 정답
     * @param categoryNo 카테고리 번호
     * @param model 뷰에 데이터를 전달하기 위한 모델
     * @param session 사용자의 세션
     * @return 퀴즈 업데이트 페이지로 리다이렉트
     */
    @GetMapping("/quizInsert")
    public String quizInsert(
            @RequestParam(value = "quizType", required = false, defaultValue = "word") String quizType,
            @RequestParam(value = "quizNo", required = false) Integer quizNo,
            @RequestParam(value = "question", required = false, defaultValue = "") String question,
            @RequestParam(value = "timer", required = false) Integer timer,
            @RequestParam(value = "hint1", required = false, defaultValue = "") String hint1,
            @RequestParam(value = "hint2", required = false, defaultValue = "") String hint2,
            @RequestParam(value = "hint3", required = false, defaultValue = "") String hint3,
            @RequestParam(value = "answer", required = false, defaultValue = "") String answer,
            @RequestParam(value = "categoryNo", required = false, defaultValue = "") String categoryNo,
            Model model, 
            HttpSession session) {

        // 선택한 quizType에 따라 퀴즈 목록을 조회하여 모델에 추가
        List<QuizVO> quizzes;

        if ("sentence".equals(quizType)) {
            quizzes = template.selectList("com.a.EnGround.repo.QuizRepository.findAllQuiz", "select_sentence");
        } else {
            quizzes = template.selectList("com.a.EnGround.repo.QuizRepository.findAllQuiz", "select_word");
        }

        quizzes.forEach(vo -> logger.info("quizzes for log : {}", vo.getQuizNo()));
        model.addAttribute("quizType", quizType);

        // 필수 입력 필드가 하나라도 누락된 경우 에러 메시지를 로그에 기록하고 다시 입력 페이지로 이동
        if (quizNo == null || question.isEmpty() || timer == null || hint1.isEmpty() || hint2.isEmpty() || hint3.isEmpty() || answer.isEmpty() || categoryNo.isEmpty()) {
            logger.error("필수 입력 필드가 누락되었습니다.");
            model.addAttribute("errorMessage", "모든 필드를 입력해주세요.");
            return "quizInsert"; // 다시 입력 페이지로 돌아가기
        }

        // 중복 확인 로직 추가
        String table = "word".equals(quizType) ? "select_word" : "select_sentence";
        System.out.println(table);
        Map<String, Object> checkParams = new HashMap<>();
        checkParams.put("quizId", quizNo);
        checkParams.put("table", table);

        QuizVO existingQuiz = template.selectOne("com.a.EnGround.repo.QuizRepository.findByQuizId", checkParams);
        if (existingQuiz != null) {
            logger.error("QuizNo {} is already taken.", quizNo);
            model.addAttribute("errorMessage", "이미 존재하는 번호입니다. 다른 번호를 입력하세요.");
            return "quizInsert"; // 중복된 경우 다시 입력 페이지로 돌아가기
        }

        // 퀴즈 정보를 저장할 Map 객체 생성
        Map<String, Object> params = new HashMap<>();
        params.put("quizNo", quizNo);
        params.put("question", question);
        params.put("timer", timer);
        params.put("hint1", hint1);
        params.put("hint2", hint2);
        params.put("hint3", hint3);
        params.put("answer", answer);
        params.put("categoryNo", categoryNo);
        params.put("table", table); // 테이블 이름을 명시적으로 포함

        // MyBatis를 사용하여 insert 쿼리를 실행하고, 결과를 반환
        int result = template.insert("com.a.EnGround.repo.QuizRepository.insertQuiz", params);

        // 삽입 성공 여부를 로그에 기록
        if (result > 0) {
            logger.info("Quiz inserted successfully into {}", table);
        } else {
            logger.warn("Failed to insert quiz into {}", table);
        }

        // 삽입이 완료되면 quizUpdate 페이지로 리다이렉트, quizType을 포함하여 전달
        return "redirect:/quizUpdate?quizType=" + quizType;
    }
    
    /**
     * 특정 퀴즈를 데이터베이스에서 삭제합니다.
     * @param quizNo 퀴즈 번호
     * @param quizType 퀴즈 타입 (단어 또는 문장)
     * @param model 뷰에 데이터를 전달하기 위한 모델
     * @return 퀴즈 업데이트 페이지로 리다이렉트
     */
    @GetMapping("/quizDelete")
    public String quizDelete(@RequestParam("quizNo") int quizNo,
                             @RequestParam("quizType") String quizType,
                             Model model) {
        // quizType에 따라 적절한 테이블 이름을 설정
        String table = "word".equals(quizType) ? "select_word" : "select_sentence";
        
        try {
            // 삭제할 퀴즈의 번호와 테이블 이름을 매핑하여 파라미터로 전달
            Map<String, Object> params = new HashMap<>();
            params.put("quizNo", quizNo);
            params.put("table", table);

            // MyBatis를 사용하여 delete 쿼리를 실행하고, 결과를 반환
            int result = template.delete("com.a.EnGround.repo.QuizRepository.deleteQuiz", params);

            // 삭제 성공 여부를 로그에 기록
            if (result > 0) {
                logger.info("Quiz deleted successfully from {}", table);
            } else {
                logger.warn("Failed to delete quiz from {}", table);
            }
        } catch (Exception e) {
            // 삭제 중 오류 발생 시 로그에 기록하고 에러 메시지를 모델에 추가
            logger.error("Error occurred while deleting quiz", e);
            model.addAttribute("errorMessage", "퀴즈 삭제 중 오류가 발생했습니다.");
            return "quizUpdate"; // 오류 발생 시 다시 퀴즈 업데이트 페이지로 이동
        }

        // 삭제가 완료되면 quizUpdate 페이지로 리다이렉트, quizType을 포함하여 전달
        return "redirect:/quizUpdate?quizType=" + quizType;
    }
    @GetMapping("/quizUpdateSubmit")
    public String quizUpdateSubmit(
    		@RequestParam(value = "quizNo", required = false) Integer quizNo,
            @RequestParam(value = "question", required = false, defaultValue = "") String question,
            @RequestParam(value = "timer", required = false) Integer timer,
            @RequestParam(value = "hint1", required = false, defaultValue = "") String hint1,
            @RequestParam(value = "hint2", required = false, defaultValue = "") String hint2,
            @RequestParam(value = "hint3", required = false, defaultValue = "") String hint3,
            @RequestParam(value = "answer", required = false, defaultValue = "") String answer,
            @RequestParam(value = "categoryNo", required = false, defaultValue = "") String categoryNo,
            Model model,
            HttpSession session) {

        // 세션에서 quizType을 가져옴
        String quizType = (String) session.getAttribute("quizType");
        if (quizType == null || quizType.isEmpty()) {
            quizType = "word"; // 기본값 설정
        }

        // 필수 입력 필드가 하나라도 누락된 경우 에러 메시지를 기록하고 입력 페이지로 이동
        if (quizNo == null || question.isEmpty() || timer == null || hint1.isEmpty() || hint2.isEmpty() || hint3.isEmpty() || answer.isEmpty() || categoryNo.isEmpty()) {
            logger.error("필수 입력 필드가 누락되었습니다.");
            model.addAttribute("errorMessage", "모든 필드를 입력해주세요.");
            return "quizUpdateInfo"; // 다시 입력 페이지로 돌아가기
        }

        // 퀴즈 정보를 저장할 Map 객체 생성
        Map<String, Object> params = new HashMap<>();
        params.put("quizNo", quizNo);
        params.put("question", question);
        params.put("timer", timer);
        params.put("hint1", hint1);
        params.put("hint2", hint2);
        params.put("hint3", hint3);
        params.put("answer", answer);
        params.put("categoryNo", categoryNo);

        // quizType에 따라 적절한 테이블 이름을 설정하고 Map에 추가
        String table = "word".equals(quizType) ? "select_word" : "select_sentence";
        params.put("table", table);

        // MyBatis를 사용하여 update 쿼리를 실행하고, 결과를 반환
        int result = template.update("com.a.EnGround.repo.QuizRepository.updateQuiz", params);

        // 업데이트 성공 여부를 로그에 기록
        if (result > 0) {
            logger.info("Quiz updated successfully in {}", table);
        } else {
            logger.warn("Failed to update quiz in {}", table);
        }

        // 업데이트가 완료되면 quizUpdate 페이지로 리다이렉트
        return "redirect:/quizUpdate?quizType=" + quizType;
    }
    
    @GetMapping("/loadMoreQuizzes")
    public String loadMoreQuizzes(
            @RequestParam("page") int page,
            @RequestParam("quizType") String quizType,
            Model model) {

        int pageSize = 10; // 한 번에 로드할 퀴즈 항목 수
        int offset = (page - 1) * pageSize; // 페이지에 따른 오프셋 계산

        // 매개변수를 담기 위한 HashMap 생성
        Map<String, Object> params = new HashMap<>();
        params.put("table", "sentence".equals(quizType) ? "select_sentence" : "select_word");
        params.put("offset", offset);
        params.put("limit", pageSize);

        // 해당 페이지에 대한 퀴즈 목록을 조회
        List<QuizVO> quizzes = template.selectList("com.a.EnGround.repo.QuizRepository.findQuizzesByPage", params);

        model.addAttribute("quizzes", quizzes);

        // 퀴즈 목록만 반환하는 작은 JSP fragment (일반적으로 퀴즈 항목들을 로드하는 HTML 코드만 포함)
        return "fragments/quizItems"; 
    }
}
