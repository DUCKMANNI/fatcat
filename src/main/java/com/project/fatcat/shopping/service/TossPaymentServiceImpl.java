package com.project.fatcat.shopping.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

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
import com.project.fatcat.shopping.dto.PaymentConfirmRequest;
import com.project.fatcat.shopping.dto.PaymentConfirmResponse;
import com.project.fatcat.shopping.repository.OrderRepository;
import com.project.fatcat.shopping.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service("tossPaymentService")
@RequiredArgsConstructor
public class TossPaymentServiceImpl implements PaymentService{

	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Value("${toss.payments.secret-key}")
    private String secretKey;
	
	@Transactional
    public PaymentConfirmResponse confirmPayment(PaymentConfirmRequest request) {
        String url = "https://api.tosspayments.com/v1/payments/confirm";

        // 1. 인증 헤더 생성
        String encodedAuth = Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + encodedAuth);

        HttpEntity<PaymentConfirmRequest> entity = new HttpEntity<>(request, headers);

        // 2. 토스 API 호출
        ResponseEntity<PaymentConfirmResponse> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, PaymentConfirmResponse.class);

        PaymentConfirmResponse body = response.getBody();

        // 3. 주문 조회
        Order order = orderRepository.findByOrderNumber(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        // 4. 결제 성공 / 실패 분기
        if (body != null && "DONE".equalsIgnoreCase(body.getStatus())) {
            // 결제 성공 처리
            order.setOrderStatus("PAID");   // 주문 상태 변경
            orderRepository.save(order);

            Payment payment = Payment.builder()
                    .order(order)
                    .user(order.getUser())
                    .payPrice(request.getAmount())
                    .merchantUid(order.getOrderNumber())    // 주문번호
                    .pgProvider("TOSSPAYMENTS")             // 고정값
                    .method(body.getMethod())               // 카드, 가상계좌 등
                    .status("DONE")
                    .pgTid(body.getPaymentKey())            // 토스에서 내려주는 paymentKey
                    .approvedDate(LocalDateTime.now())      // 실제 응답 approvedAt 사용 가능
                    .build();

            paymentRepository.save(payment);

        } else {
            // 결제 실패 처리
            order.setOrderStatus("FAIL");
            orderRepository.save(order);

            Payment payment = Payment.builder()
                    .order(order)
                    .user(order.getUser())
                    .payPrice(request.getAmount())
                    .merchantUid(order.getOrderNumber())
                    .pgProvider("TOSSPAYMENTS")
                    .method(body != null ? body.getMethod() : "UNKNOWN")
                    .status("FAILED")
                    .pgTid(request.getPaymentKey())         // 실패해도 토스에서 준 paymentKey는 기록
                    .failedDate(LocalDateTime.now())
                    .build();

            paymentRepository.save(payment);
        }

        return body;
    }
	
}
