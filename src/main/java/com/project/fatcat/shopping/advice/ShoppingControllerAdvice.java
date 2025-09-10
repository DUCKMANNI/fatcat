package com.project.fatcat.shopping.advice;

import java.util.List;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.project.fatcat.shopping.dto.CategoryDTO;
import com.project.fatcat.shopping.service.CategoryServiceImpl;

import lombok.RequiredArgsConstructor;

@ControllerAdvice(basePackages = "com.project.fatcat.shopping")
@RequiredArgsConstructor
public class ShoppingControllerAdvice {

	private final CategoryServiceImpl categoryServiceImpl;

	// 모든 shopping 패키지 하위 컨트롤러에서 categoryDtos 자동 주입
    @ModelAttribute("categoryDtos")
    public List<CategoryDTO> addCategories() {
        return categoryServiceImpl.getCategoryMenu();
    }
}
