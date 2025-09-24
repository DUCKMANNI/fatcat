package com.project.fatcat.mypage.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.fatcat.cats.service.CatService;
import com.project.fatcat.entity.Cat;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {
	

	   private final CatService catService;

	    // 마이페이지 URL로 접속하면 이 메소드가 호출됩니다.
	    // URL에 userId가 포함되지 않아도 되도록 수정합니다.
	    @GetMapping
	    public String myPage(Model model) {
	        // TODO: 로그인된 사용자의 실제 ID를 가져와야 합니다.
	        // 현재는 테스트를 위해 임시로 userId 1을 사용합니다.
	        int userId = 1; 

	        // 1. CatService를 사용하여 현재 사용자의 고양이 리스트를 가져옵니다.
	        List<Cat> cats = catService.findAllByUserId(userId);

	        // 2. 가져온 'cats' 리스트를 'model' 객체에 담아 HTML 템플릿으로 전달합니다.
	        model.addAttribute("cats", cats);
	        
	        // 3. 'myPage.html' 템플릿을 반환하여 렌더링합니다.
	        return "mypage/myPage";
	    }
	


}
