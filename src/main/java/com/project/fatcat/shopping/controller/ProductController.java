package com.project.fatcat.shopping.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/products")
public class ProductController {

	@GetMapping("")
	public String listProducts(@RequestParam(name = "category",defaultValue="all") String category,
	                           @RequestParam(name = "sub",required = false) String sub,
	                           @RequestParam(name = "detail", required = false) String detail,
	                           @RequestParam(name = "page", required = false) Integer page,
	                           Model model){

		if (page == null) page = 0;
		
		List<Map<String, Object>> dummyProducts = new ArrayList<>();
		for (int i = 1; i <= 20; i++) {
			Map<String, Object> p = new HashMap<>();
			p.put("productCode", category + sub + "00" + i);
			p.put("name", "상품 " + i);
			p.put("price", i * 1000);
			dummyProducts.add(p);
		}

		model.addAttribute("products", dummyProducts);
		model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", 5); // 예시로 5페이지 있다고 가정
		return "shopping/shopping_product";
	}
	
	@GetMapping("/detail")
	public String productDetail(@RequestParam("productCode") String productCode, Model model) {
	    // 실제로는 DB 조회
	    Map<String, Object> product = new HashMap<>();
	    product.put("productCode", productCode);
	    product.put("name", "상품 " + productCode);
	    product.put("price",  10000);
	    product.put("description", "상품 상세 설명 " + productCode);
	    product.put("reviewCount", 100);
	    product.put("mainImage", "/images/no_image.jpg");
	    product.put("detailImage", "/images/no_image.jpg");

	    model.addAttribute("product", product);

	    return "shopping/shopping_detail"; // 상세 페이지 템플릿
	}
}
