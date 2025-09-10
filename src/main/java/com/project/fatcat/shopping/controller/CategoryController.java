package com.project.fatcat.shopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/shopping")
public class CategoryController {

    //private final CategoryServiceImpl categoryServiceImpl;

	
	// 쇼핑몰 메인
//    @GetMapping
//    public String shoppingMain(Model model) {
//        model.addAttribute("categoryDtos", categoryServiceImpl.getCategoryMenu());
//        return "shopping/shopping_main";
//    }
     

}
