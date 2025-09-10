package com.project.fatcat.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
@AllArgsConstructor
public class CartItemDTO {

	private String productName;
	private int price;
	private int quantity;
	private String imageUrl;
}
