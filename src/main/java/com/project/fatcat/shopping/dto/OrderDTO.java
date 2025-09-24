package com.project.fatcat.shopping.dto;

import java.util.List;

import com.project.fatcat.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class OrderDTO {

	 private Integer cartSeq;        // 장바구니 번호

	    // 주문자(구매자) 기본 정보
	    private String userName;
	    private String userEmail;
	    private String userPhone;

	    // 배송 정보 (기본값은 사용자 정보, 화면에서 변경 가능)
	    private String orderName;       // 수령인 이름
	    private String orderPhonenum;   // 수령인 연락처
	    private String orderAddress1;   // 주소
	    private String orderAddress2;   // 상세 주소
	    private String orderZipcode;    // 우편번호
	    private String orderRequest;    // 배송 요청사항

	    // 위치 정보
	    private Double latitude;
	    private Double longitude;

	    // 장바구니 상품 목록
	    private List<CartItemDTO> items;
}
