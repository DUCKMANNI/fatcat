package com.project.fatcat.order;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.fatcat.entity.Order;

import lombok.RequiredArgsConstructor;

@Service("myPageOrderService")
@RequiredArgsConstructor
public class OrderService {
	
	 private final OrderRepository orderRepository;

	    // 🔹 사용자 ID(userSeq)로 주문 내역 조회 함수
	    public List<Order> getOrdersByUserId(Integer userSeq) {
	        // Repository를 호출하여 해당 사용자의 주문 목록만 가져옵니다.
	        return orderRepository.findByUserUserSeq(userSeq); 
	    }

}
