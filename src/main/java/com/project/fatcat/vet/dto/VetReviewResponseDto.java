package com.project.fatcat.vet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

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
        private Integer userSeq;
        private String vetReview;
        private Integer vetRating;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime createDate;
        private LocalDateTime visitDate;
        private String userName;

      
       
        public ReviewDetail(Integer vetReviewSeq, String vetReview, Integer vetRating, LocalDateTime createDate, LocalDateTime visitDate, Integer userSeq, String userName) {
            this.vetReviewSeq = vetReviewSeq;
            this.vetReview = vetReview;
            this.vetRating = vetRating;
            this.createDate = createDate;
            this.visitDate = visitDate;
            this.userSeq = userSeq;
            this.userName = userName;
        }
    }
}