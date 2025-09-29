package com.project.fatcat.shopping.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.fatcat.entity.Product;
import com.project.fatcat.shopping.service.ProductServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
@Log4j2
public class ProductController {

	private final ProductServiceImpl productServiceImpl;
	
	@GetMapping("")
	public String listProducts(
            @RequestParam(name = "category", defaultValue = "all") String main,
            @RequestParam(name = "sub", required = false) String sub,
            @RequestParam(name = "detail", required = false) String detail,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,   // 0-base
            @RequestParam(name = "size", defaultValue = "12") int size,
            Model model) {

        String m = (main == null || main.isBlank()) ? "all" : main.trim();   // main 필수
        String s = (sub == null || sub.isBlank()) ? null : sub.trim();
        String d = (detail == null || detail.isBlank()) ? null : detail.trim();
        String k = (keyword == null || keyword.isBlank()) ? null : keyword.trim();

        int p = Math.max(0, page);
        int sz = Math.max(1, size);

        Pageable pageable = PageRequest.of(p, sz, Sort.by(Sort.Direction.DESC, "createDate"));
        Page<Product> productPage = productServiceImpl.getProducts(m, s, d, k, pageable);

        // 뷰로 전달
        model.addAttribute("productPage", productPage);           // 페이지 정보
        model.addAttribute("products", productPage.getContent()); // 실제 데이터
        model.addAttribute("main", m);
        model.addAttribute("sub", s);       // null이면 Thymeleaf URL에서 자동 제거
        model.addAttribute("detail", d);    // null이면 Thymeleaf URL에서 자동 제거
        model.addAttribute("keyword", k);
        model.addAttribute("selectedCategory", main);
        model.addAttribute("selectedSub", sub);
        model.addAttribute("selectedDetail", detail); 

        return "shopping/shopping_product"; // 뷰 이름
    }

	
	@GetMapping("/detail")
	public String productDetail(@RequestParam("productCode") String productCode, Model model) {
	    
	    Product product = productServiceImpl.getProductDetail(productCode);

	    model.addAttribute("product", product);

	    return "shopping/shopping_detail"; // 상세 페이지 템플릿
	}
	
}
