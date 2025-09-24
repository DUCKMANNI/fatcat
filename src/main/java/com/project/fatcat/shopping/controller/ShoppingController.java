package com.project.fatcat.shopping.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.fatcat.entity.ShoppingCart;
import com.project.fatcat.entity.User;
import com.project.fatcat.shopping.dto.CartItemDTO;
import com.project.fatcat.shopping.dto.CartSummaryDTO;
import com.project.fatcat.shopping.repository.ShoppingCartRepository;
import com.project.fatcat.shopping.service.CartServiceImpl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/shopping")
public class ShoppingController {
	
	private final CartServiceImpl cartServiceImpl;
	private final ShoppingCartRepository shoppingCartRepository;
	
	// 쇼핑몰 메인
	@GetMapping
	public String shoppingMain() {
	    return "shopping/shopping_main";
	}
	@PostMapping("/cart/add")
	public String add(@RequestParam("productCode") String productCode,
	                  @RequestParam(name = "qty",defaultValue = "1") int qty,
	                  HttpSession session) {
		cartServiceImpl.add(1, productCode, qty);
	    return String.format("redirect:/products/detail?productCode=%s", productCode);
	}

	@PostMapping("/cart/update")
	public String update(@RequestParam("productCode") String productCode,
	                     @RequestParam("qty") int qty,
	                     HttpSession session) {
		cartServiceImpl.updateQty(1, productCode, qty);
		return "redirect:/shopping/cart";
	}

    @PostMapping("/remove")
    public String remove(@RequestParam("productCode") String productCode, HttpSession session) {
    	cartServiceImpl.remove(1, productCode);
    	return "redirect:/shopping/cart";
    }

    @PostMapping("/clear")
    public String clear(HttpSession session) {
    	cartServiceImpl.clear(1);
        return "redirect:/shopping/cart";
    }

    @GetMapping("/cart")
    public String view(Model model, HttpSession session) {
    	Integer userSeq = 1; // 임시값

        List<CartItemDTO> cartItems = cartServiceImpl.getCartItems(userSeq);
        CartSummaryDTO summary = cartServiceImpl.summarize(userSeq);
        ShoppingCart cart = shoppingCartRepository.findFirstByUser_UserSeqAndIsCompletedFalseOrderByCartSeqDesc(userSeq)
                .orElseThrow(() -> new RuntimeException("장바구니 없음"));

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartSummary", summary);
        model.addAttribute("cartSeq", cart.getCartSeq());
        return "shopping/shopping_cart";
    }
	
	@GetMapping("/confirm")
	public String shoppingConfirm() {
	    return "shopping/shopping_confirm";
	}

	
}
