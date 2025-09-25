package com.project.fatcat.shopping.service;

import com.project.fatcat.shopping.dto.PaymentConfirmResponse;

public interface PaymentService {

	PaymentConfirmResponse confirmPayment(String paymentKey, String orderId, int amount, Integer cartSeq);
}
