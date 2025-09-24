package com.project.fatcat.shopping.service;

import com.project.fatcat.shopping.dto.PaymentConfirmRequest;
import com.project.fatcat.shopping.dto.PaymentConfirmResponse;

public interface PaymentService {

	PaymentConfirmResponse confirmPayment(PaymentConfirmRequest request);
}
