package com.a.EnGround.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.a.EnGround.repo.AdRepositoryImpl;
import com.a.EnGround.vo.AdVO;

@Controller
public class AdController {

    @Autowired
    private AdRepositoryImpl adRepository;

    @Autowired
    private ServletContext servletContext;

    @GetMapping("/AdPage")
    public String showAdPage(Model model) {
        List<AdVO> ads = adRepository.selectAllAds();
        model.addAttribute("ads", ads);
        return "AdPage";  // adPage.jsp로 이동
    }
    
    /* ad데이터베이스 마지막 데이터를 불러오는 ajax 에 데이터를 보내줌*/
    @ResponseBody
    @GetMapping("/AdAjax")
    public Map<String, Object> getAdData(Model model) {
    	AdVO ads = adRepository.selectLatestAd();
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	if(ads == null) {
    		map.put("result", "fail");
    		return map;
    	}
    	
    	map.put("result", "success");
    	map.put("targetUrl", ads.getTargetUrl());
    	map.put("imageUrl", ads.getImageUrl());
        
    	return map;
    }

    @PostMapping("/ad/insert")
    public String insertAd(@RequestParam("imageFile") MultipartFile imageFile,
                           @RequestParam("url") String url,
                           RedirectAttributes redirectAttributes) {
        if (!imageFile.isEmpty()) {
            try {
                // 이미지 파일을 저장할 경로 설정 (프로젝트 상대 경로)
                String fileName = imageFile.getOriginalFilename();
                String uploadDir = servletContext.getRealPath("/resources/images/");  // 이미지 저장 경로

                File uploadDirFile = new File(uploadDir);

                // 디렉토리가 존재하지 않으면 생성
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }

                // 파일 저장
                String savePath = uploadDir + fileName;
                File file = new File(savePath);
                imageFile.transferTo(file);

                // AdVO 객체에 값 설정
                AdVO adVO = new AdVO();
                adVO.setImageUrl("/resources/images/" + fileName);  // 저장된 이미지의 웹 경로 설정
                adVO.setTargetUrl(url);

                // 광고 데이터 삽입
                adRepository.insertAd(adVO);

                redirectAttributes.addFlashAttribute("message", "광고가 성공적으로 등록되었습니다.");
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("message", "파일 업로드 중 오류가 발생했습니다.");
                e.printStackTrace();
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "이미지 파일을 선택해주세요.");
        }

        return "redirect:/AdPage";
    }
    
    @GetMapping("/latestAd")
    public String showLatestAd(Model model) {
        AdVO latestAd = adRepository.selectLatestAd();
        model.addAttribute("ad", latestAd);
        return "AdPage";  // AdPage.jsp로 이동
    }
}

