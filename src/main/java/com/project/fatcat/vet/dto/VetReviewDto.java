package com.project.fatcat.vet.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VetReviewDto {
	
	private String placeName; //병원 이름 
	private String address; //병원 주소 
	private String reviewContent; //리뷰 내용 
	private Integer rating; //별점 
	private String visitDate; //방문 날짜(선택 사항)
	

}
