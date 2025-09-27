package com.project.fatcat.care.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CareReviewResopnseDto {
	
	private Integer reviewSeq;
	private String careReview;
	private String authorNickname;
	private Integer careRating;
	private LocalDateTime createDate;

	
	private Integer authorUserSeq; 
}
