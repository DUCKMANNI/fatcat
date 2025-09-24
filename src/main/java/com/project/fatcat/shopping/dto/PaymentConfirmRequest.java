package com.project.fatcat.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PaymentConfirmRequest {

	private String paymentKey;
    private String orderId;
    private int amount;
}
