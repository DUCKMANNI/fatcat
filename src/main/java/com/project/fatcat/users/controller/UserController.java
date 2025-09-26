package com.project.fatcat.users.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.fatcat.users.dto.SignupDTO;
import com.project.fatcat.users.service.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserServiceImpl userServiceImpl;
	
	@GetMapping("/login")
	public String login() {
		return "home/login_page";
	}
	
	@GetMapping("/signup")
	public String signUp() {
		return "home/signup";
	}
	
	@PostMapping("/signup")
    public String signupSubmit(@ModelAttribute SignupDTO dto, Model model) {
        try {
        	userServiceImpl.register(dto);
            return "redirect:/login"; // 회원가입 완료 후 로그인 페이지로 이동
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user/signup";
        }
    }
}
