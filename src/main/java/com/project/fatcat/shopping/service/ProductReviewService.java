package com.project.fatcat.shopping.service;

import com.project.fatcat.shopping.dto.ProductReviewDTO;

public interface ProductReviewService {

	void addReview(ProductReviewDTO dto, Integer userSeq);
}
