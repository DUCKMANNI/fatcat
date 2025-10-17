package com.project.fatcat.users.controller;


import jakarta.validation.Valid; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; 
import org.springframework.web.multipart.MultipartFile;

import com.project.fatcat.SecurityUtils;
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
	public String signUp(Model model) {
		
		model.addAttribute("SignupDTO", new SignupDTO());
	    model.addAttribute("mode", "signup");
		return "home/signup";
	}
	
	@PostMapping("/signup")
    public String signupSubmit(@Valid @ModelAttribute("SignupDTO") SignupDTO dto, 
                             BindingResult bindingResult, 
                             @RequestParam("profileImageFile") MultipartFile profileImageFile, 
                             @RequestParam("vetLicenseImageFile") MultipartFile vetLicenseImageFile, 
                             Model model) {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "signup");
            return "home/signup"; 
        }

        try {
            dto.setProfileImageFile(profileImageFile);
            dto.setVetLicenseImageFile(vetLicenseImageFile);

        	userServiceImpl.register(dto);
            return "redirect:/users/login"; 
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("mode", "signup");
            return "home/signup";
        }
    }
	
	@GetMapping("/edit")
	public String editForm(Model model) {
		SignupDTO dto = userServiceImpl.getUserInfo(SecurityUtils.getCurrentUser().getUsername());
	    model.addAttribute("SignupDTO", dto);
	    model.addAttribute("mode", "update");
	    return "home/signup";
	}
}