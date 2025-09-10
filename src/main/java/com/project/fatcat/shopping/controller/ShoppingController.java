package com.project.fatcat.shopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/shopping")
public class ShoppingController {
	// 쇼핑몰 메인
	@GetMapping("")
	public String shoppingMain() {
	    return "shopping/shopping_main";
	}

	
}
