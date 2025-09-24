package com.project.fatcat.shopping.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.project.fatcat.entity.Order;
import com.project.fatcat.entity.OrderItem;
import com.project.fatcat.entity.ShoppingCart;
import com.project.fatcat.shopping.dto.OrderDTO;
import com.project.fatcat.shopping.dto.OrderFormDTO;
import com.project.fatcat.shopping.dto.PaymentConfirmRequest;
import com.project.fatcat.shopping.dto.PaymentConfirmResponse;
import com.project.fatcat.shopping.repository.OrderRepository;
import com.project.fatcat.shopping.repository.ShoppingCartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final TossPaymentServiceImpl tossPaymentServiceImpl;
	
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
    public Order confirmOrder(OrderFormDTO orderForm, Integer cartId) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
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


    public PaymentConfirmResponse handlePaymentSuccess(String paymentKey, String orderId, int amount) {
        // 1. Toss API 결제 승인
        PaymentConfirmRequest request = new PaymentConfirmRequest(paymentKey, orderId, amount);
        PaymentConfirmResponse response = tossPaymentServiceImpl.confirmPayment(request);

        // 2. 주문 상태 업데이트
        Order order = orderRepository.findByOrderNumber(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다: " + orderId));
        order.setOrderStatus("PAID");
        orderRepository.save(order);

        return response;
    }

    /**
     * 결제 실패 처리
     */
    public void handlePaymentFail(String code, String message) {
        throw new RuntimeException("결제 실패: " + code + " / " + message);
    }
    
    public void updateOrderStatus(String orderNumber, String status) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다: " + orderNumber));
        order.setOrderStatus(status);
        orderRepository.save(order);
    }
    
 // 장바구니 비우기
    public void clearCart(Integer cartSeq) {
        ShoppingCart cart = shoppingCartRepository.findById(cartSeq)
                .orElseThrow(() -> new RuntimeException("장바구니 없음"));
        
        // isCompleted 상태를 true로 변경
        cart.setIsCompleted(true);
        shoppingCartRepository.save(cart);
    }
}
