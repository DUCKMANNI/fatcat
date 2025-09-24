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
public class CartItemDTO {

	private String productCode;
	private String productName;
	private int price;
	private int quantity;
	private String imageUrl;
}
