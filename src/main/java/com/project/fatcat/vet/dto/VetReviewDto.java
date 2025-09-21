package com.project.fatcat.vet.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VetReviewDto {
	
	private String placeName;
	private String address;
	private String reviewContent;
	private Integer rating;
}