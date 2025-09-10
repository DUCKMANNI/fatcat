package com.project.fatcat.shopping.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.fatcat.entity.Order;
import com.project.fatcat.entity.Payment;
import com.project.fatcat.entity.User;
import com.project.fatcat.shopping.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service("tossPaymentService")
@RequiredArgsConstructor
public class TossPaymentServiceImpl implements PaymentService{

	private final PaymentRepository paymentRepository;
	
	@Value("${toss.payments.secret-key}")
    private String secretKey;
	
	@Override
	public Payment confirmAndSave(String paymentKey, String orderId, int amount, User user, Order order) {
		
		Payment payment = new Payment();
		
		return payment;
	}
	
}
