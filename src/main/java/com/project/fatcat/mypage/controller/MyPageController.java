package com.project.fatcat.mypage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MyPageController {
	
	@GetMapping("/mypage") 
    public String showMyPage() {
		
        return "mypage/mypage"; 
    }

}
