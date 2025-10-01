package com.project.fatcat.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewResponseDTO {

    private Double ratingAvg;   // 평균 평점
    private Long ratingCount;   // 리뷰 참여자 수

    @Builder.Default
    private List<ReviewItem> reviews = new ArrayList<>(); // 리뷰 리스트

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewItem {
        private String authorName; // 작성자 닉네임
        private int rating;        // 별점
        private String content;    // 리뷰 내용
        private String createDate; // yyyy-MM-dd 형식
    }
}
