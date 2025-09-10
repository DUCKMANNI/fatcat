package com.project.fatcat.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartSummaryDTO {

	private int totalPrice;
	private int discount;
	private int deliveryFee;
	private int finalPrice;
}
