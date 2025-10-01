package com.project.fatcat.shopping.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.fatcat.entity.Product;
import com.project.fatcat.entity.ProductReview;
import com.project.fatcat.entity.User;
import com.project.fatcat.shopping.dto.ProductReviewDTO;
import com.project.fatcat.shopping.dto.ProductReviewResponseDTO;
import com.project.fatcat.shopping.repository.OrderItemRepository;
import com.project.fatcat.shopping.repository.ProductRepository;
import com.project.fatcat.shopping.repository.ProductReviewRepository;
import com.project.fatcat.users.repository.UserRepository;

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
        boolean purchased = orderItemRepository.existsByOrderInfo_UserAndProduct(user, product);
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
    
 // 리뷰 조회 + 평균, 참여자수
    //@Transactional(readOnly = true)
    public ProductReviewResponseDTO getReviews(String productCode) {
    	
    	
    	
    	System.out.println("-----------------------------------------------------------");
    	System.out.println("productCode4444444444 : " + productCode);
    	
    	Optional<Product> op = productRepository.findByProductCode(productCode);
//    	System.out.println("-----------------------------------------------------------");
//    	System.out.println("op" + op);
    	
    	Product p = op.get();
    	System.out.println("-----------------------------------------------------------");
    	System.out.println("productCode222" + p);
    	
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new RuntimeException("상품 없음"));

        List<ProductReview> reviews = reviewRepository.findByProduct(product);
        Long count = reviewRepository.countByProduct(product);
        Double avg = reviewRepository.findAverageRatingByProduct(product);
       

        // 엔티티 → 응답 DTO 변환
        List<ProductReviewResponseDTO.ReviewItem> reviewList = reviews.stream()
                .map(r -> new ProductReviewResponseDTO.ReviewItem(
                        r.getUser().getNickname(),
                        r.getRating(),
                        r.getContent(),
                        r.getCreateDate() != null
                                ? r.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                : ""
                ))
                .toList();
        
        System.out.println("---------------------------------------------------------------------");
        System.out.println("reviewList : " + reviewList.size());
        System.out.println("avg : " + avg);

        return ProductReviewResponseDTO.builder()
                .ratingAvg(avg == null ? 0.0 : avg)
                .ratingCount(count == null ? 0L : count)
                .reviews(reviewList)
                .build();


    }
    
//    public ProductReviewResponseDTO getReviews(String productCode) {
//    	
//    	Optional<Product> op = productRepository.findById(productCode);
//    	
//    	System.out.println("-------------------------------------------------");
//    	System.out.println("op : " + op);
//    	
//    	Product p = op.get();
//    	
//    	return null;
//    }
}
