package com.project.fatcat.care.dto;

import java.time.LocalDateTime;

import com.project.fatcat.entity.CareServiceBoard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CareBoardDto {
	
	//게시글 등록, 조회 시 모두 사용 
	private Integer careSeq;
	private String careTitle;
	private String careContent;
	private String address1;
	private String address2;
	private Double latitude;
	private Double longitude;
	private Integer price;
	
	//게시글 조회시 
		
	private Integer viewCount;
	private LocalDateTime createDate;
	private String authorId;
	
	
	public CareBoardDto(CareServiceBoard entity) {
		
		this.careSeq = entity.getCareSeq();
		this.careTitle = entity.getCareTitle();
		this.careContent = entity.getCareContent();
		this.address1 = entity.getAddress1();
		this.address2 = entity.getAddress2();
		this.latitude = entity.getLatitude();
		this.longitude = entity.getLongitude();
		this.price = entity.getPrice();
		this.viewCount = entity.getViewCount();
		this.createDate = entity.getCreateDate();
		this.authorId = String.valueOf(entity.getUser().getUserSeq());
	}
	
	
	
	

}
