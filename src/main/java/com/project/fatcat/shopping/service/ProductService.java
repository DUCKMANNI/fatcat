package com.project.fatcat.shopping.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.fatcat.entity.Product;

public interface ProductService {

	Page<Product> getProducts(String main, String sub, String detail, Pageable pageable);
	
	Product getProductDetail(String productCode);
}
