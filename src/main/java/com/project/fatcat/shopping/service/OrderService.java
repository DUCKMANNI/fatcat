package com.project.fatcat.shopping.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.fatcat.entity.Order;
import com.project.fatcat.entity.OrderItem;
import com.project.fatcat.entity.ShoppingCart;
import com.project.fatcat.shopping.dto.OrderFormDTO;
import com.project.fatcat.shopping.repository.OrderRepository;
import com.project.fatcat.shopping.repository.ShoppingCartRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
	
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
    
    public Order prepareOrderForm(Integer cartSeq) {
        ShoppingCart cart = shoppingCartRepository.findById(cartSeq)
                .orElseThrow(() -> new RuntimeException("장바구니 없음"));

        // 장바구니 → 주문 기본 정보 세팅
        return Order.builder()
                .user(cart.getUser())
                .orderName(cart.getUser().getUserName())       // 유저 이름
                .orderPhonenum(cart.getUser().getPhoneNumber()) // 유저 전화번호
                .orderAddress1(cart.getUser().getAddress1()) // 기본 주소
                .orderAddress2(cart.getUser().getAddress2()) // 상세 주소
                .orderZipcode(cart.getUser().getZipCode())   // 우편번호
                .orderRequest("") // 요청사항은 폼에서 입력받도록 비워둠
                .orderStatus("PENDING")
                .orderDate(LocalDateTime.now())
                .build();
    }
    
    
    /**
     * 주문 확정 (장바구니 → Order + OrderItem 저장)
     */
    public Order confirmOrder(OrderFormDTO orderForm, Integer cartSeq) {
        ShoppingCart cart = shoppingCartRepository.findById(cartSeq)
                .orElseThrow(() -> new RuntimeException("장바구니 없음"));

        int totalAmount = cart.getCartItemList().stream()
                .mapToInt(ci -> ci.getProductQuantity() * ci.getProduct().getProductPrice())
                .sum();

        
        Order order = Order.builder()
                .user(cart.getUser())
                .orderNumber("ORDER-" + System.currentTimeMillis())
                .orderPrice(totalAmount)
                .orderName(orderForm.getReceiverName())
                .orderPhonenum(orderForm.getReceiverPhone())
                .orderAddress1(orderForm.getReceiverAddress())
	            .orderAddress2(orderForm.getReceiverDetail())
	            .orderZipcode(orderForm.getReceiverZipcode())
	            .orderRequest(orderForm.getOrderRequest())
	            .latitude(orderForm.getLatitude())
	            .longitude(orderForm.getLongitude())
	            .orderDate(LocalDateTime.now())
	            .orderStatus("PENDING")
                .build();
        
        System.out.println("getReceiverName : " + orderForm.getReceiverName());

        cart.getCartItemList().forEach(ci -> {
            OrderItem orderItem = OrderItem.builder()
                    .orderInfo(order)
                    .product(ci.getProduct())
                    .productQuantity(ci.getProductQuantity())
                    .productPrice(ci.getProduct().getProductPrice())
                    .discountTotal(0)
                    .totalPrice(ci.getProductQuantity() * ci.getProduct().getProductPrice())
                    .build();
            order.getOrderItemList().add(orderItem);
        });

        return orderRepository.save(order);
    }
    
    @Transactional
    public void completeCart(Integer cartSeq) {
        ShoppingCart cart = shoppingCartRepository.findById(cartSeq)
                .orElseThrow(() -> new RuntimeException("장바구니 없음"));

        cart.setIsCompleted(true);
        cart.setUpdateDate(LocalDateTime.now());

        shoppingCartRepository.save(cart);
    }
    
 // 🔹 사용자 ID(userSeq)로 주문 내역 조회 함수
    public List<Order> getOrdersByUserId(Integer userSeq) {
        // Repository를 호출하여 해당 사용자의 주문 목록만 가져옵니다.
        return orderRepository.findOrdersWithDetailsByUserSeq(userSeq);
    }
    

}
