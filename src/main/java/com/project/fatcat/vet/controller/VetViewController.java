package com.project.fatcat.vet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.fatcat.SecurityUtils;

@Controller
public class VetViewController {
	
	@GetMapping("/vetclinic") // 혹은 해당 페이지의 매핑 주소
	public String vetMapPage(Model model) { 
	    Integer loggedInUserSeq = null;
	    try {
	        loggedInUserSeq = SecurityUtils.getCurrentUserSeq();
	    } catch (IllegalStateException ignored) {
	        // 비로그인 상태일 때 null로 처리됨
	    }
	    // loggedInUserSeq가 null이 아니라 실제 userSeq 숫자여야 합니다.
	    model.addAttribute("loggedInUserSeq", loggedInUserSeq); 
	    return "vet/vetclinic_map"; // 템플릿 이름
	}

}
