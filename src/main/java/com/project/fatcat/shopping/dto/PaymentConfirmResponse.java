package com.project.fatcat.shopping.dto;

import lombok.Getter;

@Getter
public class PaymentConfirmResponse {

	private String orderId;
    private String paymentKey;
    private String method;
    private String status;
    private String approvedAt;
	
}
