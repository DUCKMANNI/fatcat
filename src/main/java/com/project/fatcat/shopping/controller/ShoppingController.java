package com.project.fatcat.shopping.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.fatcat.shopping.dto.CartItemDTO;
import com.project.fatcat.shopping.dto.CartSummaryDTO;

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
	
	@GetMapping("/cart")
	public String shoppingCart(Model model) {
		// 샘플 데이터
	    List<CartItemDTO> items = new ArrayList<>();
	    items.add(new CartItemDTO("캣매직 프로바이오틱 6.35kg", 11000, 1, "/images/no_image.jpg"));
	    items.add(new CartItemDTO("챠오츄르 연어맛", 5000, 1, "/images/no_image.jpg"));
	    items.add(new CartItemDTO("미야옹철 트릿", 8000, 1, "/images/no_image.jpg"));
	    
	    CartSummaryDTO summary = new CartSummaryDTO(20000, 5000, 3000, 18000);

	    model.addAttribute("cartItems", items);
	    model.addAttribute("cartSummary", summary);

		return "shopping/shopping_cart";
	}
	
	@GetMapping("/confirm")
	public String shoppingConfirm() {
	    return "shopping/shopping_confirm";
	}

	
}
