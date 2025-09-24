package com.project.fatcat.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartSummaryDTO {

	private int totalPrice;
	private int discount;
	private int deliveryFee;
	private int finalPrice;
}
