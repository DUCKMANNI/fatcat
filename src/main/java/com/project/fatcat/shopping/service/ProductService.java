package com.project.fatcat.shopping.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.fatcat.entity.Product;

public interface ProductService {

	Page<Product> getProducts(String main, String sub, String detail, String keyword, Pageable pageable);
	
	Product getProductDetail(String productCode);
	
	//List<Product> searchProducts(String keyword);
}
