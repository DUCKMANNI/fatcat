package com.project.fatcat.shopping.dto;

import java.util.List;

import com.project.fatcat.entity.OrderItem;
import com.project.fatcat.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderFormDTO {

	//구매자 정보
    private User user;   // 이미 로그인 유저 객체 (간단히 User 엔티티 통째로 담아도 됨)
    
    //장바구니 / 주문 정보
    private Integer cartSeq;            // 장바구니 번호
    private String orderNumber;			//주문번호
    private List<OrderItem> items;      // 주문 상품 리스트
    private Double latitude;            // 배송지 위도 (선택)
    private Double longitude;           // 배송지 경도 (선택)
    private Integer totalPrice;
    private Integer discount;
    private Integer deliveryFee;
    private Integer finalPrice;

    //배송 정보
    private String receiverName;        // 수령인
    private String receiverPhone;       // 연락처
    private String receiverZipcode;     // 우편번호
    private String receiverAddress;     // 기본 주소
    private String receiverDetail;      // 상세 주소

    //요청사항
    private String orderRequest;        // 배송 요청사항
}
