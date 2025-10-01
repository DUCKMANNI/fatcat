package com.project.fatcat.vet.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.fatcat.entity.User;

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
	private LocalDateTime createDate;
	private LocalDateTime visitDate;

}