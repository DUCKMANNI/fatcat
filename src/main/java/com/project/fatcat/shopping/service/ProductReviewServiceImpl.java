package com.project.fatcat.shopping.service;

import org.springframework.stereotype.Service;

import com.project.fatcat.entity.Product;
import com.project.fatcat.entity.ProductReview;
import com.project.fatcat.entity.User;
import com.project.fatcat.shopping.dto.ProductReviewDTO;
import com.project.fatcat.shopping.repository.OrderItemRepository;
import com.project.fatcat.shopping.repository.ProductRepository;
import com.project.fatcat.shopping.repository.ProductReviewRepository;
import com.project.fatcat.users.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductReviewServiceImpl implements ProductReviewService{

	private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductReviewRepository reviewRepository;
    private final OrderItemRepository orderItemRepository; // 구매 여부 확인용

    @Transactional
    @Override
    public void addReview(ProductReviewDTO dto, Integer userSeq) {

        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        Product product = productRepository.findById(dto.getProductCode())
                .orElseThrow(() -> new RuntimeException("상품 없음"));

        // 🚨 구매 여부 체크
        boolean purchased = orderItemRepository.existsByOrderUserAndProduct(user, product);
        if (!purchased) {
            throw new RuntimeException("구매자만 리뷰 작성 가능");
        }

        // 🚨 중복 리뷰 방지 (선택사항)
        if (reviewRepository.existsByUserAndProduct(user, product)) {
            throw new RuntimeException("이미 리뷰를 작성했습니다.");
        }

        ProductReview review = ProductReview.builder()
                .user(user)
                .product(product)
                .rating(dto.getRating())
                .content(dto.getContent())
                .build();

        reviewRepository.save(review);
    }
}
