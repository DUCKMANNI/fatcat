package com.project.fatcat.shopping.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductReviewDTO {

	private String productCode;   // 어떤 상품에 대한 리뷰인지
    private int rating;           // 별점 (1~5)
    private String content;       // 리뷰 내용
}
