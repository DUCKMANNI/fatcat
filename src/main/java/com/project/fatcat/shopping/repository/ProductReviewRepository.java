package com.project.fatcat.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.fatcat.entity.Product;
import com.project.fatcat.entity.ProductReview;
import com.project.fatcat.entity.User;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer>{

	boolean existsByUserAndProduct(User user, Product product);
	
	// 특정 상품의 리뷰 전체 조회
    List<ProductReview> findByProduct(Product product);


 // 특정 상품 리뷰 평균 별점
    @Query("SELECT AVG(r.rating) FROM ProductReview r WHERE r.product = ?1")
    Double findAverageRatingByProduct(Product product);

    // 특정 상품 리뷰 개수
    @Query("SELECT COUNT(r) FROM ProductReview r WHERE r.product = :product")
    Long countByProduct(@Param(value = "product") Product product);


}
