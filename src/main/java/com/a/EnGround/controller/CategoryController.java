package com.a.EnGround.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.a.EnGround.quiz.FindQuizSystem;
import com.a.EnGround.repo.CategoryRepository;
import com.a.EnGround.vo.CategoryVO;

@Controller
public class CategoryController {

	@Autowired
	CategoryRepository repository;

	@Autowired
	FindQuizSystem findQuizService; // 추가

	@Autowired
	ServletContext servletContext;

	@RequestMapping(value="/category", method=RequestMethod.GET)
    public String category(Model model, @RequestParam(name="page", required=false, defaultValue = "1") int page, HttpSession session) {
        String tableName = findQuizService.getQuizTable(session);
        Pageable pageable = PageRequest.of(page - 1, 10);
       
        Page<CategoryVO> data = repository.getAllData(pageable, tableName);//추가
        
        int totalPage = data.getTotalPages();
        int startPage = (page - 1) / 10 * 10 + 1;
        int endPage = startPage + 9;
        if (endPage > totalPage) {
            endPage = totalPage;
        }
        
        model.addAttribute("vo", data.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("PageSize", 10);
        return "category";
    }
}
