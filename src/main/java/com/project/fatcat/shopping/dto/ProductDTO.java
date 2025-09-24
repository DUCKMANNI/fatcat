package com.project.fatcat.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductDTO {

	private String productCode;
	private String productName;
	private String productContent;
	private int productPrice;
	private int productStock;
	

	private String mainCategory;    
    private String subCategory;
    private String detailCategory;
}
