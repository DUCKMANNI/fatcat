package com.project.fatcat.vet.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class VetReviewResponseDto {
    private BigDecimal ratingAvg;
    private Integer ratingCount;
    private List<ReviewDetail> reviews;

    @Getter
    @Setter
    public static class ReviewDetail {
        private Integer vetReviewSeq; // 리뷰 ID 필드 추가
        private String vetReview;
        private Integer vetRating;
        private LocalDateTime createDate;
        private LocalDateTime visitDate;

        // 기존 생성자
        public ReviewDetail(String vetReview, Integer vetRating, LocalDateTime createDate, LocalDateTime visitDate) {
            this.vetReview = vetReview;
            this.vetRating = vetRating;
            this.createDate = createDate;
            this.visitDate = visitDate;
        }
        
        // vetReviewSeq를 포함하는 새로운 생성자
        public ReviewDetail(Integer vetReviewSeq, String vetReview, Integer vetRating, LocalDateTime createDate, LocalDateTime visitDate) {
            this.vetReviewSeq = vetReviewSeq;
            this.vetReview = vetReview;
            this.vetRating = vetRating;
            this.createDate = createDate;
            this.visitDate = visitDate;
        }
    }
}