package com.project.fatcat.care.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CareReviewRequestDto {
	
	private Integer authorUserSeq; 
    private Integer targetUserSeq;
    private String careReview;
    private Integer careRating;
    private String targetRole = "provider";
}