package com.project.fatcat.vet.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class VetReviewResponseDto {

	private BigDecimal ratingAvg;
	private Integer ratingCount;
	private List<ReviewDetail> reviews;
	
	@Setter
	@Getter
	@AllArgsConstructor
	public static class ReviewDetail{
		private String reviewCount;
		private Integer rating;
		private LocalDateTime createDate;
		private Long likeCount;
		
		
	}
}
