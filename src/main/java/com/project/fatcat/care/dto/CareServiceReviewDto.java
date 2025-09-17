package com.project.fatcat.care.dto;

import com.project.fatcat.entity.CareServiceReview;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CareServiceReviewDto {

    private Integer careReviewSeq;
    private Integer careRating;
    private String careReview;
    private String authorUserName;
    private String targetUserName;
    private LocalDateTime createDate;

    // 엔티티를 DTO로 변환하는 생성자 (Response용)
    public CareServiceReviewDto(CareServiceReview review) {
        this.careReviewSeq = review.getCareReviewSeq();
        this.careRating = review.getCareRating();
        this.careReview = review.getCareReview();
        this.createDate = review.getCreateDate();
        
        if (review.getAuthorUser() != null) {
            this.authorUserName = review.getAuthorUser().getUserName();
        }
        
        if (review.getTargetUser() != null) {
            this.targetUserName = review.getTargetUser().getUserName();
    }
}
}