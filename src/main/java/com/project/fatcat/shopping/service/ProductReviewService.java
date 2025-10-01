package com.project.fatcat.shopping.service;

import com.project.fatcat.shopping.dto.ProductReviewDTO;
import com.project.fatcat.shopping.dto.ProductReviewResponseDTO;

public interface ProductReviewService {

	void addReview(ProductReviewDTO dto, Integer userSeq);
	ProductReviewResponseDTO getReviews(String productCode);
}
