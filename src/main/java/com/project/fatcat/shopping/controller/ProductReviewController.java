package com.project.fatcat.shopping.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.fatcat.SecurityUtils;
import com.project.fatcat.shopping.dto.ProductReviewDTO;
import com.project.fatcat.shopping.dto.ProductReviewResponseDTO;
import com.project.fatcat.shopping.service.ProductReviewServiceImpl;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ProductReviewController {

	
	private final ProductReviewServiceImpl productReviewServiceImpl;

    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestBody ProductReviewDTO dto) {
    	
        productReviewServiceImpl.addReview(dto, SecurityUtils.getCurrentUserSeq());

        // 작성한 리뷰를 다시 응답으로 내려주면 JS에서 바로 append 가능
        return ResponseEntity.ok(dto);
    }
    
 // 상품 리뷰 조회
    @GetMapping("/{productCode}")
    public ResponseEntity<ProductReviewResponseDTO> getReviews(@PathVariable("productCode") String productCode) {
    	
        ProductReviewResponseDTO response = productReviewServiceImpl.getReviews(productCode);
        return ResponseEntity.ok(response);
    }
}
