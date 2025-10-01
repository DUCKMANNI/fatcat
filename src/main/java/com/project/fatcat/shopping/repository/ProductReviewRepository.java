package com.project.fatcat.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.Product;
import com.project.fatcat.entity.ProductReview;
import com.project.fatcat.entity.User;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer>{

	boolean existsByUserAndProduct(User user, Product product);
}
