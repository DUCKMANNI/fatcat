package com.project.fatcat.shopping.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.project.fatcat.entity.Order;
import com.project.fatcat.entity.Payment;
import com.project.fatcat.entity.ShoppingCart;
import com.project.fatcat.shopping.dto.PaymentConfirmResponse;
import com.project.fatcat.shopping.repository.OrderRepository;
import com.project.fatcat.shopping.repository.PaymentRepository;
import com.project.fatcat.shopping.repository.ShoppingCartRepository;

import lombok.RequiredArgsConstructor;

@Service("tossPaymentService")
@RequiredArgsConstructor
public class TossPaymentServiceImpl implements PaymentService{

	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;
	private final RestTemplate restTemplate = new RestTemplate();
    private final ShoppingCartRepository shoppingCartRepository;
	
	/* @Value("${toss.payments.secret-key}") */
    private String secretKey = "test_sk_DpexMgkW36zN776bZ9Q93GbR5ozO";
	
    @Transactional
    public PaymentConfirmResponse confirmPayment(String paymentKey, String orderNum, int amount, Integer cartSeq) {
    	
    	System.out.println("사용 중인 시크릿 키: " + secretKey);
    	
        String url = "https://api.tosspayments.com/v1/payments/confirm";

        // 1. 인증 헤더 생성
        String encodedAuth = Base64.getEncoder()
                .encodeToString((secretKey.trim() + ":").getBytes(StandardCharsets.UTF_8));

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Basic " + encodedAuth);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(secretKey, "");

        Map<String, Object> body = new HashMap<>();
        body.put("paymentKey", paymentKey);
        body.put("orderId", orderNum);
        body.put("amount", amount);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // 3. 토스 API 호출
        ResponseEntity<PaymentConfirmResponse> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, PaymentConfirmResponse.class);


        PaymentConfirmResponse confirm = response.getBody();

        // 3. 주문 조회
        Order order = orderRepository.findByOrderNumber(orderNum)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        // 4. 결제 성공 / 실패 처리
        if (confirm != null && "DONE".equalsIgnoreCase(confirm.getStatus())) {
            order.setOrderStatus("PAID");
            orderRepository.save(order);

            Payment payment = Payment.builder()
                    .order(order)
                    .user(order.getUser())
                    .payPrice(amount)
                    .merchantUid(order.getOrderNumber())
                    .pgProvider("TOSSPAYMENTS")
                    .method(confirm.getMethod())
                    .status("DONE")
                    .pgTid(confirm.getPaymentKey())
                    .approvedDate(LocalDateTime.now())
                    .build();

            paymentRepository.save(payment);

            // 장바구니 완료 처리
            clearCart(cartSeq);

        } else {
            order.setOrderStatus("FAIL");
            orderRepository.save(order);

            Payment payment = Payment.builder()
                    .order(order)
                    .user(order.getUser())
                    .payPrice(amount)
                    .merchantUid(order.getOrderNumber())
                    .pgProvider("TOSSPAYMENTS")
                    .method(confirm != null ? confirm.getMethod() : "UNKNOWN")
                    .status("FAILED")
                    .pgTid(paymentKey)
                    .failedDate(LocalDateTime.now())
                    .build();

            paymentRepository.save(payment);
        }

        return confirm;
    }

    private void clearCart(Integer cartSeq) {
        ShoppingCart cart = shoppingCartRepository.findById(cartSeq)
                .orElseThrow(() -> new RuntimeException("장바구니를 찾을 수 없습니다."));
        cart.setIsCompleted(true);
        shoppingCartRepository.save(cart);
    }
}
	